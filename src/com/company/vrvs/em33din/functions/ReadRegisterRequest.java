package com.company.vrvs.em33din.functions;

import com.company.vrvs.em33din.CRC16;
import com.company.vrvs.em33din.Request;
import com.company.vrvs.em33din.Response;
import com.company.vrvs.em33din.utils.ByteArrayUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Adam Krhovják on 17.3.2016.
 */
public class ReadRegisterRequest extends Request {
    public static int FRAME_LENGTH = 8;

    private final RegisterAddress registerAddress;
        
    public ReadRegisterRequest(int physicalAddress, FunctionCode functionCode, RegisterAddress registerAddress, Response.Listener listener) {
        super(physicalAddress, functionCode, listener);
        this.registerAddress = registerAddress;
    }

    @Override
    public byte[] getFrame() {
        ByteBuffer frame = ByteBuffer.allocate(ReadRegisterRequest.FRAME_LENGTH-2);
        // add physical address
        frame.put((byte)this.physicalAddress);
        // add function code
        frame.put(this.functionCode.value());
        // register address + quantity
        frame.put(this.registerAddress.getAddress());
        // calculate CRC16
        byte[] tmpframe = frame.array();
        ByteBuffer crc = ByteBuffer.allocate(2);
        crc.order(ByteOrder.LITTLE_ENDIAN);
        crc.putShort(CRC16.calculate(tmpframe, tmpframe.length));
        ByteBuffer crcframe = ByteBuffer.allocate(ReadRegisterRequest.FRAME_LENGTH);
        crcframe.put(tmpframe);
        crcframe.put(crc.array());
        return crcframe.array();
    }

    @Override
    public void deliverResponse(Response response) {
        if(response!=null) {
            this.responseListener.onResponse(parseResponse(response));
        }
        this.requestQueue.finish();
    }

    private Register[] parseResponse(Response response) {
        List<Register> registers = new LinkedList<>();
        byte[] rawRes = response.getResult();
        BlockRegisterAddress blockRegisterAddress = (BlockRegisterAddress)this.registerAddress;
        int requestedBytes = rawRes[0];
        if(rawRes[0] == blockRegisterAddress.getRegisterCount()*2
            && rawRes.length == requestedBytes + 1) {
            List<Byte> regData = new LinkedList<>();
            byte[] currAddress = Arrays.copyOfRange(blockRegisterAddress.getAddress(),0,2);
            ByteBuffer addressBuff;
            ByteBuffer dataBuff;
            short v;
            for(int i = 1; i < rawRes.length; i++) {
                regData.add(rawRes[i]);
                if(regData.size() == Integer.SIZE/Byte.SIZE) {
                    dataBuff = ByteBuffer.allocate(4);
                    dataBuff.put(ByteArrayUtil.toPrimitive(regData.toArray(new Byte[0])));
                    registers.add(new Register<Integer>(new RegisterAddress(currAddress), dataBuff.getInt(0)));
                    // increment address
                    addressBuff = ByteBuffer.allocate(2);
                    addressBuff.put(currAddress);
                    v = addressBuff.getShort(0);
                    v+=2;
                    addressBuff = ByteBuffer.allocate(2);
                    addressBuff.putShort(v);
                    currAddress = addressBuff.array();
                    regData.clear();
                }
            }
            return registers.toArray(new Register[0]);
        } else {
            return null;
        }
    }

}