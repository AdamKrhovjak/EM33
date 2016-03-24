package com.company.vrvs.em33din.functions;


import com.company.vrvs.em33din.Request;
import com.company.vrvs.em33din.Response;

public class WriteRegisterRequest extends Request {
    /**
     * @param physicalAddress
     * @param functionCode
     * @param responseListener
     */
    public WriteRegisterRequest(int physicalAddress, FunctionCode functionCode, Response.Listener responseListener) {
        super(physicalAddress, functionCode, responseListener);
    }

    @Override
    public byte[] getFrame() {
        return new byte[0];
    }

    @Override
    public void deliverResponse(Response response) {
        //TODO
    }
}
