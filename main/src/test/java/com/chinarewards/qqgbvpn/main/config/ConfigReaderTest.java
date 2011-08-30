/**
 * 
 */
package com.chinarewards.qqgbvpn.main.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.chinarewards.qqgbpvn.main.test.GuiceTest;
import com.chinarewards.qqgbvpn.main.HomeDirLocator;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class ConfigReaderTest extends GuiceTest {

	File homeDir = null;

	File confDir = null;

	ConfigReader api = null;

	HomeDirLocator homeDirLocator = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgpvn.main.test.GuiceTest#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();

		// prepare API
		homeDir = this.makeTmpDir();

		// as well as conf directory
		confDir = new File(homeDir, "conf");
		confDir.mkdir();

		// create the APIs
		homeDirLocator = new HomeDirLocator(homeDir.getAbsolutePath());
		api = new ConfigReader(homeDirLocator);
	}

	@After
	public void tearDown() throws Exception {
		if (confDir != null)
			confDir.delete();
		if (homeDir != null)
			homeDir.delete();

		api = null;
		homeDirLocator = null;
	}

	@Test
	public void testRead_OK() throws Exception {

		//
		// create a fake config file.
		//
		Properties p = new Properties();
		p.put("username", "root");
		p.put("password", "password123");
		//
		PrintWriter pw = new PrintWriter(new File(confDir, "posnet.ini"));
		p.store(pw, null);
		pw.close();

		// now, read it
		Configuration conf = api.read("posnet.ini");

		// validate it.
		assertNotNull(conf);
		// make sure the content is correct
		assertEquals("root", conf.getString("username"));
		assertEquals("password123", conf.getString("password"));
		// make sure there are only 2 entries
		Iterator iter = conf.getKeys();
		int count = 0;
		while (iter.hasNext()) {
			iter.next();
			count++;
		}
		assertEquals(2, count);

	}

	/**
	 * Create a temporary directory.
	 * 
	 * @return
	 * @throws IOException
	 */
	protected File makeTmpDir() throws IOException {
		File tmpDir = File.createTempFile("test", null);
		tmpDir.delete();
		tmpDir.mkdir();
		return tmpDir;
	}

}
