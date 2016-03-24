package com.company.vrvs.em33din;

import java.nio.ByteBuffer;

/**
 * Created by Adam Krhovják on 11.3.2016.
 */
public class CRC16 {
    public static final int BYTES = 2;   // byte count

    public static short calculate(byte[] data, int len) {
        int crc = 0xffff;
        for (int pos = 0; pos < len; pos++) {
            crc ^= data[pos] & 0xff;          // XOR byte into least sig. byte of crc

            for (int i = 8; i != 0; i--) {    // Loop over each bit
                if ((crc & 0x0001) != 0) {      // If the LSB is set
                    crc >>= 1;                    // Shift right and XOR 0xA001
                    crc ^= 0xA001;
                }
                else                            // Else LSB is not set
                    crc >>= 1;                    // Just shift right
            }
        }
        return (short)crc;
    }
}
