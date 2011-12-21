package com.chinarewards.qqgbpvn.main.encoder;

import java.util.Arrays;

import junit.framework.TestCase;

import com.chinarewards.qqgbvpn.main.encoder.IUUIDEncoder;
import com.chinarewards.qqgbvpn.main.encoder.UUIDEncoderImpl;

public class TestUUIDEncodingImpl extends TestCase {

	IUUIDEncoder encoder = new UUIDEncoderImpl();

	public void testEncoder() throws Exception {
		String uuid = "012345678901234567890123456789ab";
		byte[] expected = new byte[] { 0x01, 0x23, 0x45, 0x67, (byte) 0x89,
				0x01, 0x23, 0x45, 0x67, (byte) 0x89, 0x01, 0x23, 0x45, 0x67,
				(byte) 0x89, (byte) 0xab };

		byte[] actual = encoder.encode(uuid);
		assertTrue(Arrays.equals(expected, actual));
	}

	public void testDecoder() throws Exception {
		String expected = "012345678901234567890123456789ab";
		byte[] result = new byte[] { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, 0x01,
				0x23, 0x45, 0x67, (byte) 0x89, 0x01, 0x23, 0x45, 0x67,
				(byte) 0x89, (byte) 0xab };
		String actual = encoder.decode(result);
		assertEquals(expected, actual);
	}
	
	public void testAll() throws Exception{
		String expected = "0769b47cde7b43e98b4d79e0ff3702fb";
		String actual = encoder.decode(encoder.encode(expected));
		assertEquals(expected, actual);
	}
}
