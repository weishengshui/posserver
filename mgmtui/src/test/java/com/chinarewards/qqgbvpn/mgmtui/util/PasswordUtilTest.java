/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author cream
 * 
 */
public class PasswordUtilTest {

	@Test
	public void testEncodePassword() {
		String pwd = "password";
		assertEquals("5f4dcc3b5aa765d61d8327deb882cf99",
				PasswordUtil.encodePassword(pwd));
	}

}
