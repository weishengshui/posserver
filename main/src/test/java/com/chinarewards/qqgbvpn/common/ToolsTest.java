/**
 * 
 */
package com.chinarewards.qqgbvpn.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

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
		byte[] bb = new byte[11];
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
		assertEquals((byte) 0x01, (byte) bb[9]);
		assertEquals((byte) 0xE0, (byte) bb[10]);
		
		// 2012-3-2 1:17:17:390 -8时区
		ca = Calendar.getInstance();
		ca.setTimeInMillis(Long.parseLong("1330676237390"));
		ca.setTimeZone(TimeZone.getTimeZone("America/Vancouver"));//太平洋标准时间(America/Vancouver): -8.0
		System.out.println(ca.toString());
		bb = new byte[11];
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
		assertEquals((byte) 0xFE, (byte) bb[9]);
		assertEquals((byte) 0x20, (byte) bb[10]);
		
		// test minus timzzone
		ca = Calendar.getInstance();
		ca.set(2012, Calendar.JANUARY, 31, 23, 58, 59);
		ca.set(Calendar.MILLISECOND, 987);
		ca.setTimeZone(TimeZone.getTimeZone("GMT-12:00"));
		ca.setLenient(false);
		bb = new byte[11];
		Tools.putDate(bb, ca, 0);
		assertEquals((byte) 0x07, (byte) bb[0]);
		assertEquals((byte) 0xDC, (byte) bb[1]);
		assertEquals((byte) 1, (byte) bb[2]);
		assertEquals((byte) 31, (byte) bb[3]);
		assertEquals((byte) 23, (byte) bb[4]);
		assertEquals((byte) 58, (byte) bb[5]);
		assertEquals((byte) 59, (byte) bb[6]);
		assertEquals((byte) 0x03, (byte) bb[7]);
		assertEquals((byte) 0xDB, (byte) bb[8]);
		assertEquals((byte) 0xFD, (byte) bb[9]);
		assertEquals((byte) 0x30, (byte) bb[10]);
		
	}
	
	@Test
	public void test_GetDate()throws Exception{
		Calendar cal = Calendar.getInstance();
		// 设置year为2008
		cal.set(Calendar.YEAR, 2008);
		// 设置month为12
		cal.set(Calendar.MONTH, 12 - 1);
		// 设置day为31
		cal.set(Calendar.DAY_OF_MONTH, 31);
		// 设置hour为15
		cal.set(Calendar.HOUR_OF_DAY, 15);
		// 设置minute为35
		cal.set(Calendar.MINUTE, 35);
		// 设置second为20
		cal.set(Calendar.SECOND, 20);
		// 设置millisecond为500
		cal.set(Calendar.MILLISECOND, 500);
		// 时区
		cal.setTimeZone(TimeZone.getDefault());
		// 将cal写进一个字节数组
		byte[] xact_time = new byte[ProtocolLengths.CR_DATE_LENGTH];
		// 调用putDate方法将时间日期信息写进字节数组

		Tools.putDate(xact_time, cal, 0);

		// 从数组中将数据读取出来
		int year = Tools.getUnsignedShort(xact_time, 0);
		int month = xact_time[2];
		int day = xact_time[3];
		int hour = xact_time[4];
		int minute = xact_time[5];
		int second = xact_time[6];
		int millsecond = Tools.getUnsignedShort(xact_time, 7);
		int timezone = Tools.getUnsignedShort(xact_time, 9);
		// 验证比对
		assertTrue(year == 2008);
		assertTrue(month == 12);
		assertTrue(day == 31);
		assertTrue(hour == 15);
		assertTrue(minute == 35);
		assertTrue(second == 20);
		assertTrue(millsecond == 500);
		assertTrue(timezone == 480);
		// putDate验证完毕

		Calendar cal2 = Tools.getDate(xact_time);
		System.out.println("cal2.get(Calendar.YEAR): "
				+ cal2.get(Calendar.YEAR));
		assertTrue(year == cal2.get(Calendar.YEAR));
		assertTrue(month == cal2.get(Calendar.MONTH) + 1);
		assertTrue(day == cal2.get(Calendar.DAY_OF_MONTH));
		assertTrue(hour == cal2.get(Calendar.HOUR_OF_DAY));
		assertTrue(minute == cal2.get(Calendar.MINUTE));
		assertTrue(second == cal2.get(Calendar.SECOND));
		assertTrue(millsecond == cal2.get(Calendar.MILLISECOND));
		assertTrue(timezone == cal2.getTimeZone().getRawOffset() / (60 * 1000));

	}
}
