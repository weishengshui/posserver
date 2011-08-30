package com.chinarewards.qqgpvn.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.chinarewards.qqgbvpn.main.PosServer;
import com.chinarewards.qqgbvpn.main.ServerModule;
import com.chinarewards.qqgbvpn.main.jpa.JpaPersistModuleBuilder;
import com.chinarewards.qqgpvn.main.test.GuiceTest;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;

/**
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class DefaultPosServerTest extends GuiceTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgpvn.main.test.GuiceTest#getModules()
	 */
	@Override
	protected Module[] getModules() {

		TestConfigModule confModule = (TestConfigModule) buildTestConfigModule();

		// build the Guice modules.
		Module[] modules = new Module[] { confModule,
				buildPersistModule(confModule.getConfiguration()),
				new ServerModule() };

		return modules;
	}

	protected Module buildTestConfigModule() {

		Configuration conf = new BaseConfiguration();
		// hard-coded config
		conf.setProperty("server.port", 1235);
		// persistence
		conf.setProperty("db.user", "sa");
		conf.setProperty("db.password", "");
		conf.setProperty("db.driver", "org.hsqldb.jdbcDriver");
		conf.setProperty("db.url", "jdbc:hsqldb:.");
		// additional Hibernate properties
		conf.setProperty("db.hibernate.dialect",
				"org.hibernate.dialect.HSQLDialect");
		conf.setProperty("db.hibernate.show_sql", true);
		// URL for QQ
		conf.setProperty("qq.groupbuy.url.groupBuyingSearchGroupon",
				"http://localhost:8086/qqapi");
		conf.setProperty("qq.groupbuy.url.groupBuyingValidationUrl",
				"http://localhost:8086/qqapi");
		conf.setProperty("qq.groupbuy.url.groupBuyingUnbindPosUrl",
				"http://localhost:8086/qqapi");

		TestConfigModule confModule = new TestConfigModule(conf);
		return confModule;
	}

	protected Module buildPersistModule(Configuration config) {

		JpaPersistModule jpaModule = new JpaPersistModule("posnet");
		// config it.

		JpaPersistModuleBuilder b = new JpaPersistModuleBuilder();
		b.configModule(jpaModule, config, "db");

		return jpaModule;
	}

	@Test
	public void testStart() throws Exception {

		// get an new instance of PosServer
		PosServer server = getInjector().getInstance(PosServer.class);

		// make sure it is stopped
		assertTrue(server.isStopped());

		// start it!
		server.start();

		// make sure it is started, and port is correct
		assertFalse(server.isStopped());
		assertEquals(1235, server.getLocalPort());

		// sleep for a while...
		Thread.sleep(500); // 0.5 seconds

		// stop it, and make sure it is stopped.
		server.stop();
		assertTrue(server.isStopped());
		
		log.info("Server stopped");

	}
}
