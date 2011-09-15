/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.handler;

import org.apache.commons.configuration.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.chinarewards.qqgbpvn.main.CommonTestConfigModule;
import com.chinarewards.qqgbpvn.main.test.JpaGuiceTest;
import com.chinarewards.qqgbvpn.core.jpa.JpaPersistModuleBuilder;
import com.chinarewards.qqgbvpn.main.ServerModule;
import com.chinarewards.qqgbvpn.main.guice.AppModule;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandlerModule;
import com.chinarewards.qqgbvpn.main.protocol.ServiceMapping;
import com.chinarewards.qqgbvpn.main.protocol.ServiceMappingConfigBuilder;
import com.chinarewards.qqgbvpn.main.protocol.guice.ServiceHandlerGuiceModule;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.util.Modules;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class PosRequestFirmwareUpdateHandlerTest extends JpaGuiceTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbpvn.main.test.JpaGuiceTest#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbpvn.main.test.JpaGuiceTest#tearDown()
	 */
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbpvn.main.test.GuiceTest#getModules()
	 */
	@Override
	protected Module[] getModules() {
		CommonTestConfigModule confModule = new CommonTestConfigModule();
		Configuration configuration = confModule.getConfiguration();

		JpaPersistModule jpaModule = new JpaPersistModule("posnet");
		JpaPersistModuleBuilder builder = new JpaPersistModuleBuilder();
		builder.configModule(jpaModule,  configuration, "db");

		ServiceMappingConfigBuilder mappingBuilder = new ServiceMappingConfigBuilder();
		ServiceMapping mapping = mappingBuilder.buildMapping(confModule
				.getConfiguration());

		// build the Guice modules.
		Module[] modules = new Module[] {
				new CommonTestConfigModule(),
				jpaModule,
				new ServerModule(),
				new AppModule(),
				Modules.override(
						new ServiceHandlerModule(confModule.getConfiguration()))
						.with(new ServiceHandlerGuiceModule(mapping)) };

		return modules;
	}

	/**
	 * Demo the test of the most normal case: A POS sends a request to the
	 * server.
	 */
	@Test
	public void testSendRequest() {

		// prepare the server

	}

}
