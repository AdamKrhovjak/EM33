package com.company.vrvs.em33din.functions;


public class RegisterAddress {
	/**
	* This class represents a register address
	*/

	/**
	*	EM33 register's addresses
	*/
    public static final byte[] VOLTAGE_L1_N = new byte[]{0x00,0x00};
    public static final byte[] VOLTAGE_L2_N = new byte[]{0x00,0x02};
    public static final byte[] VOLTAGE_L3_N = new byte[]{0x00,0x04};
    public static final byte[] CURRENT_L1_N = new byte[]{0x00,0x06};
    public static final byte[] CURRENT_L2_N = new byte[]{0x00,0x08};
    public static final byte[] CURRENT_L3_N = new byte[]{0x00,0x0A};
    public static final byte[] POWER = new byte[]{0x00,0x0C};
    public static final byte[] ELECTRIC_CONSUMPTION = new byte[]{0x00,0x0E};

	/**
    * address - 2 BYTES
    */
    protected final byte[] address;

    public RegisterAddress(byte[] address) {
        this.address = address;
    }

    public byte[] getAddress() {
        return this.address;
    }

    @Override
    public String toString() {
        StringBuilder sb  = new StringBuilder();
        for(byte b : this.address) {
            sb.append(b);
        }
        return sb.toString();
    }
}