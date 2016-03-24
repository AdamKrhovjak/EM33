package com.company.vrvs.em33din;


/**
 * Created by Adam Krhovják on 16.3.2016.
 */
public abstract class Request {

    /**
     * Supported function codes for EM33
     */
    public enum FunctionCode {
        READ_HOLDING_REGISTER((byte)0x03),
        READ_INPUT_REGISTER((byte)(0x04));

        private final byte val;
        FunctionCode(byte val) {
            this.val = val;
        }

        public byte value() {
            return this.val;
        }
    }

    /** Request timeout */
    protected long timeout;
    
    /**
	*	physical address of the device
    */
    protected int physicalAddress;

    /**
     * function code
     */
    protected FunctionCode functionCode;

    /**
    * response Listener
    */
    protected Response.Listener responseListener;


    protected RequestQueue requestQueue;


    /**
     *
     * @param physicalAddress
     * @param functionCode
     * @param responseListener
     */
    public Request(int physicalAddress, FunctionCode functionCode, Response.Listener responseListener) {
        this.physicalAddress = physicalAddress;
        this.functionCode = functionCode;
        this.responseListener = responseListener;
        this.timeout = 2000;
    }

    /**
     *
     * @return raw request frame
     */
    abstract public byte[] getFrame();
    abstract public void deliverResponse(Response response);

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public long getTimeout() {
        return this.timeout;
    }
}
