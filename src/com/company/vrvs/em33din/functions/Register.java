package com.company.vrvs.em33din.functions;


public class Register<T> {
    /**
     * This class represents a register
     */

    private final RegisterAddress registerAddress;
    private T value;


    public Register(RegisterAddress registerAddress) {
        this.registerAddress = registerAddress;
    }

    public Register(RegisterAddress registerAddress, T value) {
    	this.registerAddress = registerAddress;
        this.value = value;
    }

    public RegisterAddress getRegisterAddress() {
        return this.registerAddress;
    }

    public T getValue() {
        return value;
    }

}


