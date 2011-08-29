package com.chinarewards.qqgbvpn.common;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * byte tools
 * 
 * @author huangwei
 *
 */
public class Tools {
	

	
	public static byte[] unsignedShortToByte(int src){
		byte[] des = new byte[2];
		des[0] = (byte) (src >> 8);
		des[1] = (byte) (src >> 0);
		return des;
	}
	
	public static int byteToUnsignedShort(byte[] bb){
		return (int) ((((bb[0] & 0xff) << 8) | ((bb[1] & 0xff) << 0)));
	}
	
	public static byte[] unsignedIntToByte(long src){
		byte[] des = new byte[4];
		des[0] = (byte) (src >> 24);
		des[1] = (byte) (src >> 16);
		des[2] = (byte) (src >> 8);
		des[3] = (byte) (src >> 0);
		return des;
	}
	
	public static long byteToUnsignedInt(byte[] bb){
		return ((((long) bb[0] & 0xff) << 24)
				| (((long) bb[1] & 0xff) << 16)
				| (((long) bb[2] & 0xff) << 8) | (((long) bb[3] & 0xff) << 0));
	}

	// ///////////////////////////////////////////////////////
	public static void putUnsignedShort(byte[] bb, int x, int index) {
		bb[index + 0] = (byte) (x >> 8);
		bb[index + 1] = (byte) (x >> 0);

	}

	public static int getUnsignedShort(byte[] bb, int index) {
		return (int) ((((bb[index + 0] & 0xff) << 8) | ((bb[index + 1] & 0xff) << 0)));
	}

	// /////////////////////////////////////////////////////////
	public static void putUnsignedInt(byte[] bb, long x, int index) {
		bb[index + 0] = (byte) (x >> 24);
		bb[index + 1] = (byte) (x >> 16);
		bb[index + 2] = (byte) (x >> 8);
		bb[index + 3] = (byte) (x >> 0);
	}
	
	public static void putBytes(byte[] bb, byte[] x, int index) {
		for(int i=0;i<x.length;i++){
			bb[index + i] = x[i];
		}
	}

	public static long getUnsignedInt(byte[] bb, int index) {
		return ((((long) bb[index + 0] & 0xff) << 24)
				| (((long) bb[index + 1] & 0xff) << 16)
				| (((long) bb[index + 2] & 0xff) << 8) | (((long) bb[index + 3] & 0xff) << 0));
	}
	
	public static void main(String[] args) {
		byte[] b = new byte[2];
		IoBuffer buf = IoBuffer.allocate(2);

		int i = 65535;
		b = new byte[2];
		putUnsignedShort(b, i, 0);
		buf = IoBuffer.allocate(2);
		buf.put(b);
		buf.flip();
		System.out.println(getUnsignedShort(b, 0));
		System.out.println(byteToUnsignedShort(b));
		System.out.println(buf.getUnsignedShort());
		System.out.println(byteToUnsignedShort(unsignedShortToByte(i)));
		System.out.println("***************************");
		long l = 50;
		b = new byte[4];
		putUnsignedInt(b, l, 0);
		buf = IoBuffer.allocate(4);
		buf.put(b);
		buf.flip();
		System.out.println(getUnsignedInt(b, 0));
		System.out.println(byteToUnsignedInt(b));
		System.out.println(buf.getUnsignedInt());
		System.out.println(byteToUnsignedInt(unsignedIntToByte(l)));
		System.out.println("***************************");
	}
}
