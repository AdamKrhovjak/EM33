package com.company.vrvs.em33din.functions;


import java.nio.ByteBuffer;
import java.util.Arrays;

public class BlockRegisterAddress extends RegisterAddress {

	private int quantityOfRegisters;

	public BlockRegisterAddress(byte[] address, int quantityOfRegisters) {
		super(address);
		this.quantityOfRegisters = quantityOfRegisters;
	}

	@Override
	public byte[] getAddress() {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.put(super.getAddress());
        ByteBuffer quantity = ByteBuffer.allocate(2);
        quantity.putShort((short)this.quantityOfRegisters);
		buffer.put(quantity.array());
		return buffer.array();
	}

	public int getRegisterCount() {
		return quantityOfRegisters;
	}
}