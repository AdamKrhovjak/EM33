package com.company.vrvs.em33din;


import com.company.vrvs.em33din.utils.ByteArrayUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

public class RequestQueue implements CommunicationChannel.Listener {

	/** Flag whether can process new request */
	private boolean isDelivered;

	/** Current requests */
	private Queue<Request> currentRequests;

	/** Communication channel to which the queue is associated with */
	private CommunicationChannel communicationChannel;

    private ResponseDelivery responseDelivery;

	/**
	 *
	 * @param communicationChannel
     */
	public RequestQueue(CommunicationChannel communicationChannel) {
		this.communicationChannel = communicationChannel;
		this.communicationChannel.setListener(this);
		this.currentRequests = new LinkedList<>();
        this.responseDelivery = null;
		this.isDelivered = true;
	}

	/**
	 *
	 * @param request
     */
	public void add(Request request) {
		this.currentRequests.add(request);
		request.setRequestQueue(this);
		serve();
	}

	/** Serve requests*/
	private void serve() {
		if(isDelivered && !this.currentRequests.isEmpty()) {
			Request req = this.currentRequests.poll();
            this.responseDelivery = new ResponseDelivery(req);
			try {
				this.communicationChannel.write(req.getFrame());
			} catch (IOException e) {
				e.printStackTrace();
			}
			isDelivered = false;
		}
	}

	/**
	 *
	 */
	public void finish() {
		this.isDelivered = true;
        this.responseDelivery = null;
		serve();
	}

    @Override
    public void onNewData(byte[] data, int length) {
        if(this.responseDelivery != null) {
            this.responseDelivery.process(data, length);
        }
    }

    /**
     * This class is only used to handle raw data into meaningful response
     */

    private class ResponseDelivery {
        private List<Byte> bytes;
        private final Request request;
        private final Timer timer;

        public ResponseDelivery(Request request) {
            this.bytes = new ArrayList<>();
            this.request = request;
            this.timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    error();
                }
            }, request.getTimeout());
        }

        public void process(byte[] data, int length) {
            // insert new data
            for(int i = 0; i < length; i++) {
                bytes.add(data[i]);
                byte[] tmp = ByteArrayUtil.toPrimitive(this.bytes.toArray(new Byte[0]));
                if(Arrays.equals(this.request.getFrame(), tmp)) {
                    this.bytes.clear();
                }
            }
            if(bytes.size() > 2) {
                byte[] frame = new byte[bytes.size()-2];
                ByteBuffer crc16 = ByteBuffer.allocate(CRC16.BYTES);
                for(int i =  0; i < frame.length; i++) {
                    frame[i] = bytes.get(i);
                }
                for(int i = 1; i <= CRC16.BYTES; i++) {
                    crc16.put(bytes.get(bytes.size()-i));
                }


                // check crc16
                short res = CRC16.calculate(frame, frame.length);
                if(res == crc16.getShort(0)) {
                    finish();
                }
            }
        }

        private void finish() {
            // take sublist
            List<Byte> resultRes = bytes.subList(2,bytes.size()-2);
            this.request.deliverResponse(new Response(ByteArrayUtil.toPrimitive(resultRes.toArray(new Byte[0]))));
            timer.cancel();
        }

        private void error() {
            this.request.deliverResponse(null);
        }
    }
}