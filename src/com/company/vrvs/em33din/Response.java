package com.company.vrvs.em33din;

import com.company.vrvs.em33din.functions.Register;

import java.util.List;

/**
 * Created by Adam Krhovják on 17.3.2016.
 */
public class Response {
    public interface Listener {
        void onResponse(Register... response);
    }
	private final byte[] result;

    public Response(byte[] result) {
        this.result = result;
    }

    public byte[] getResult() {
        return result;
    }
}
