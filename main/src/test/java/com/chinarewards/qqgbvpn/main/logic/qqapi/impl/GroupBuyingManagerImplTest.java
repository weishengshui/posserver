/**
 * 
 */
package com.chinarewards.qqgbvpn.main.logic.qqapi.impl;

import java.util.Enumeration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.LocalConnector;
import org.mortbay.jetty.testing.ServletTester;

import com.chinarewards.qqgbpvn.main.test.GuiceTest;

/**
 * @author Cyril
 * 
 */
public class GroupBuyingManagerImplTest extends GuiceTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgpvn.main.test.GuiceTest#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {

		ServletTester tester = new ServletTester();
		LocalConnector conn = tester.createLocalConnector();
		
		tester.start();
		
		System.out.println("getLocalPort = " + conn.getLocalPort());
		System.out.println("port = " + conn.getPort());
		Enumeration<String> names = conn.getServer().getAttributeNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			System.out.println("name = " + name);
		}
		
		
	}

}
