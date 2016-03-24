package com.company.vrvs;

import com.company.vrvs.em33din.*;
import com.company.vrvs.em33din.functions.BlockRegisterAddress;
import com.company.vrvs.em33din.functions.ReadRegisterRequest;
import com.company.vrvs.em33din.functions.Register;
import com.company.vrvs.em33din.functions.RegisterAddress;

import java.nio.ByteBuffer;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        CommunicationChannel communicationChannel = null;
        try {
            communicationChannel = EM33Din.openCommunicationChannel("COM4");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Request request = new ReadRegisterRequest(0x01,Request.FunctionCode.READ_HOLDING_REGISTER,
                new BlockRegisterAddress(RegisterAddress.VOLTAGE_L1_N, 6), new Response.Listener() {
            @Override
            public void onResponse(Register... response ) {
               for(Register r : response) {
                   System.out.println(r.getRegisterAddress().toString());
                   System.out.println(r.getValue().toString());
               }
            }
        });
        request.getFrame();
        RequestQueue requestQueue = new RequestQueue(communicationChannel);
        requestQueue.add(request);
    }

    public static void printArray(byte[] valueArray) {
        for(byte b: valueArray) {
            System.out.print(b+":");
        }
    }
}
