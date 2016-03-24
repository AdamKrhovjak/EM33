package com.company.vrvs.em33din;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by Adam Krhovják on 11.3.2016.
 */
public class CommunicationChannel {
    /**
     * This class is used to provide low level communication
     *
     */
    private InputStream in;
    private OutputStream out;
    private CommunicationThread communicationThread;
    private CommunicationChannel.Listener listener;

    /**
     *
     */
    public static interface Listener {
        void onNewData(byte[] data, int length);
    }

    /**
     * create new communication channel
     * @param in
     * @param out
     */
    public CommunicationChannel(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    /**
     *
     * @param data
     * @throws IOException
     */

    public void write(byte[] data) throws IOException {
        this.communicationThread.write(data);
    }

    /**
     *
     * @param listener
     */
    public void setListener(CommunicationChannel.Listener listener) {
        this.listener = listener;
    }

    /**
    * method start new communication thread
    */
    public void start() {
        this.communicationThread = new CommunicationThread(this.in, this.out);
        this.communicationThread.start();
    }

    /**
     * method cancel currently running communication thread
     */
    public void cancel() {

    }

    private class CommunicationThread extends Thread {

        private InputStream in;
        private OutputStream out;

        /**
         * create new communication thread
         * @param in
         * @param out
         */
        public CommunicationThread(InputStream in, OutputStream out) {
            this.in = in;
            this.out = out;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[32768];
            int len = 0;
            try {
                while(true) {
                    len = in.read(buffer);
                    if(listener != null) {
                        listener.onNewData(buffer, len);
                    }
                    Thread.sleep(200);
                }
            } catch( IOException e ) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * write data to the serial outputstream
         * @param data
         * @throws IOException
         */
        public void write(byte[] data) throws IOException {
            this.out.write(data);
        }
    }
}
