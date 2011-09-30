package com.chinarewards.qqgbvpn.common;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Hex;

/**
 * byte tools
 * 
 * @author huangwei
 * 
 */
public abstract class Tools {

	/**
	 * Find out the first occurrence of 0 (numeric 0) in the byte array.
	 * 
	 * @param array
	 * @return 0-based position of the first occurrence of numeric 0 in the
	 *         array, or -1 if not found.
	 */
	public static int findFirstZero(byte[] array) {
		int pos = 0;
		for (pos = 0; pos < array.length; pos++) {
			if (array[pos] == 0)
				return pos;
		}
		// not found
		return -1;
	}

	public static String byteToString(byte[] array, Charset charset) {
		int pos = findFirstZero(array);
		
		// first byte is zero, or is an empty string
		if (pos == 0 || (pos < 0 && array.length == 0)) {
			return null;
		}
		
		// the whole string, if no numeric zero is found in array
		if (pos < 0) {
			pos = array.length;
		}

		return new String(array, 0, pos, charset);
	}

	public static byte[] unsignedShortToByte(int src) {
		byte[] des = new byte[2];
		des[0] = (byte) (src >> 8);
		des[1] = (byte) (src >> 0);
		return des;
	}

	public static int byteToUnsignedShort(byte[] bb) {
		return (int) ((((bb[0] & 0xff) << 8) | ((bb[1] & 0xff) << 0)));
	}

	public static byte[] unsignedIntToByte(long src) {
		byte[] des = new byte[4];
		des[0] = (byte) (src >> 24);
		des[1] = (byte) (src >> 16);
		des[2] = (byte) (src >> 8);
		des[3] = (byte) (src >> 0);
		return des;
	}

	public static long byteToUnsignedInt(byte[] bb) {
		return ((((long) bb[0] & 0xff) << 24) | (((long) bb[1] & 0xff) << 16)
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
		for (int i = 0; i < x.length; i++) {
			bb[index + i] = x[i];
		}
	}

	public static long getUnsignedInt(byte[] bb, int index) {
		return ((((long) bb[index + 0] & 0xff) << 24)
				| (((long) bb[index + 1] & 0xff) << 16)
				| (((long) bb[index + 2] & 0xff) << 8) | (((long) bb[index + 3] & 0xff) << 0));
	}

	public static String dateToString(String format, Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	/**
	 * Calculate the Internet Checksum of a buffer (RFC 1071 -
	 * http://www.faqs.org/rfcs/rfc1071.html) Algorithm is 1) apply a 16-bit 1's
	 * complement sum over all octets (adjacent 8-bit pairs [A,B], final odd
	 * length is [A,0]) 2) apply 1's complement to this final sum
	 * 
	 * Notes: 1's complement is bitwise NOT of positive value. Ensure that any
	 * carry bits are added back to avoid off-by-one errors
	 * 
	 * 
	 * content from
	 * http://stackoverflow.com/questions/4113890/how-to-calculate-the
	 * -internet-checksum-from-a-byte-in-java
	 * 
	 * @param packageContent
	 *            The message
	 * @return The checksum
	 */
	public static final int checkSum(byte[] packageContent, int packageSize) { // 原本是return
																				// long
		int i = 0;

		int sum = 0;
		long data;

		// Handle all pairs
		while (packageSize > 1) {
			// Corrected to include @Andy's edits and various comments on Stack
			// Overflow
			data = (((packageContent[i] << 8) & 0xFF00) | ((packageContent[i + 1]) & 0xFF));
			sum += data;
			// 1's complement carry bit correction in 16-bits (detecting sign
			// extension)
			if ((sum & 0xFFFF0000) > 0) {
				sum = sum & 0xFFFF;
				sum += 1;
			}

			i += 2;
			packageSize -= 2;
		}

		// Handle remaining byte in odd length buffers
		if (packageSize > 0) {
			// Corrected to include @Andy's edits and various comments on Stack
			// Overflow
			sum += (packageContent[i] << 8 & 0xFF00);
			// 1's complement carry bit correction in 16-bits (detecting sign
			// extension)
			if ((sum & 0xFFFF0000) > 0) {
				sum = sum & 0xFFFF;
				sum += 1;
			}
		}

		// Final 1's complement value correction to 16-bits
		sum = ~sum;
		sum = sum & 0xFFFF;
		return sum;
	}
	
	public static final String byteToHexString(byte[] src) {
		return new String(Hex.encodeHex(src));
	}

	public static void main(String[] args) {
		// byte[] b = new byte[2];
		// IoBuffer buf = IoBuffer.allocate(2);
		//
		// int i = 65535;
		// b = new byte[2];
		// putUnsignedShort(b, i, 0);
		// buf = IoBuffer.allocate(2);
		// buf.put(b);
		// buf.flip();
		// System.out.println(getUnsignedShort(b, 0));
		// System.out.println(byteToUnsignedShort(b));
		// System.out.println(buf.getUnsignedShort());
		// System.out.println(byteToUnsignedShort(unsignedShortToByte(i)));
		// System.out.println("***************************");
		// long l = 50;
		// b = new byte[4];
		// putUnsignedInt(b, l, 0);
		// buf = IoBuffer.allocate(4);
		// buf.put(b);
		// buf.flip();
		// System.out.println(getUnsignedInt(b, 0));
		// System.out.println(byteToUnsignedInt(b));
		// System.out.println(buf.getUnsignedInt());
		// System.out.println(byteToUnsignedInt(unsignedIntToByte(l)));
		// System.out.println("***************************");

		byte b[] = { 0, 0, 0, 1, 32, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 32, 0, 0, 0,
				5, 67, 82, 45, 48, 48, 48, 48, 48, 48, 49, 54, 56 };
		System.out.println(checkSum(b, b.length));

	}
}
