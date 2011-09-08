/**
 * 
 */
package com.chinarewards.qqgbvpn.common;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;

import org.junit.Test;

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
}
