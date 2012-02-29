/**
 * 
 */
package com.chinarewards.qqgbvpn.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.CodecUtil;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class ToolsTest {

	@Test
	public void testFindFirstZero_NoZero() {

		byte[] data = new byte[] { 0x01, 0x02, 0x03 };
		assertEquals(-1, Tools.findFirstZero(data));

	}

	@Test
	public void testFindFirstZero_LeadingZero() {

		byte[] data = new byte[] { 0x0, 0x02, 0x03 };
		assertEquals(0, Tools.findFirstZero(data));

	}

	@Test
	public void testFindFirstZero_MiddleZero() {

		byte[] data = new byte[] { 0x01, 0x00, 0x03 };
		assertEquals(1, Tools.findFirstZero(data));

	}

	@Test
	public void testFindFirstZero_LastZero() {

		byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x00 };
		assertEquals(3, Tools.findFirstZero(data));

	}

	@Test
	public void testByteToString_OK() {

		byte[] data = new byte[] { 0x30, 0x31, 0x00 };
		assertEquals("01",
				Tools.byteToString(data, Charset.forName("ISO-8859-1")));

	}

	@Test
	public void testByteToString_AllNumericZero() {

		byte[] data = null;
		
		data = new byte[] { 0x00 };
		assertNull(Tools.byteToString(data, Charset.forName("ISO-8859-1")));
		
		data = new byte[] { 0x00, 0x01 };
		assertNull(Tools.byteToString(data, Charset.forName("ISO-8859-1")));

		data = new byte[] { 0x00, 0x00 };
		assertNull(Tools.byteToString(data, Charset.forName("ISO-8859-1")));

	}

	@Test
	public void testByteToString_NoZeroString() {

		byte[] data = null;
		
		data = new byte[] { 0x30, 0x31 };
		assertEquals("01", Tools.byteToString(data, Charset.forName("ISO-8859-1")));

	}
	
	@Test
	public void testPutDate() {
		Calendar ca = Calendar.getInstance();
		//2012-2-29 16:46:28:125
		ca.setTimeInMillis(Long.parseLong("1330505188125"));		
		ca.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		byte[] bb = new byte[10];
		Tools.putDate(bb, ca.getTime(), 0);
		assertEquals((byte)0x07, (byte)bb[0]);
		assertEquals((byte)0xDC, (byte)bb[1]);
		assertEquals((byte)0x02, (byte)bb[2]);
		assertEquals((byte)0x1D, (byte)bb[3]);
		assertEquals((byte)0x10, (byte)bb[4]);
		assertEquals((byte)0x2E, (byte)bb[5]);
		assertEquals((byte)0x1C, (byte)bb[6]);
		assertEquals((byte)0x00, (byte)bb[7]);
		assertEquals((byte)0x7D, (byte)bb[8]);
		assertEquals((byte)0x08, (byte)bb[9]);

	}
}
