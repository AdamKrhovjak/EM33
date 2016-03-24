package com.company.vrvs.em33din.utils;
public class ByteArrayUtil {
	public static Byte[] fromPrimitive(byte[] primitive) {
        Byte[] ByteObjects = new Byte[primitive.length];
        int i = 0;
        for(byte b: primitive) {
            ByteObjects[i++] = b;
        }
        return ByteObjects;
    }

    public static byte[] toPrimitive(Byte[] wrapper) {

        byte[] primitives = new byte[wrapper.length];
        int i = 0;
        for(Byte b : wrapper) {
            primitives[i++] = b;
        }
        return primitives;
    }
}
