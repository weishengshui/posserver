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
		assertEquals("01",
				Tools.byteToString(data, Charset.forName("ISO-8859-1")));

	}

	@Test
	public void testPutDate() {
		Calendar ca = Calendar.getInstance();
		// 2012-3-2 16:17:17:390 +8时区
		ca.setTimeInMillis(Long.parseLong("1330676237390"));
		ca.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));//America/Denver
		byte[] bb = new byte[10];
		Tools.putDate(bb, ca, 0);
//		System.out.println(ca.toString());
//		System.out.println(CodecUtil.hexDumpAsString(bb));
		assertEquals((byte) 0x07, (byte) bb[0]);
		assertEquals((byte) 0xDC, (byte) bb[1]);
		assertEquals((byte) 0x03, (byte) bb[2]);
		assertEquals((byte) 0x02, (byte) bb[3]);
		assertEquals((byte) 0x10, (byte) bb[4]);
		assertEquals((byte) 0x11, (byte) bb[5]);
		assertEquals((byte) 0x11, (byte) bb[6]);
		assertEquals((byte) 0x01, (byte) bb[7]);
		assertEquals((byte) 0x86, (byte) bb[8]);
		assertEquals((byte) 0x08, (byte) bb[9]);
		
		// 2012-3-2 1:17:17:390 -8时区
		ca.setTimeZone(TimeZone.getTimeZone("America/Vancouver"));//太平洋标准时间(America/Vancouver): -8.0
		System.out.println(ca.toString());
		bb = new byte[10];
		Tools.putDate(bb, ca, 0);
		assertEquals((byte) 0x07, (byte) bb[0]);
		assertEquals((byte) 0xDC, (byte) bb[1]);
		assertEquals((byte) 0x03, (byte) bb[2]);
		assertEquals((byte) 0x02, (byte) bb[3]);
		assertEquals((byte) 0x00, (byte) bb[4]);
		assertEquals((byte) 0x11, (byte) bb[5]);
		assertEquals((byte) 0x11, (byte) bb[6]);
		assertEquals((byte) 0x01, (byte) bb[7]);
		assertEquals((byte) 0x86, (byte) bb[8]);
		assertEquals((byte) 0xF8, (byte) bb[9]);

	}

}
