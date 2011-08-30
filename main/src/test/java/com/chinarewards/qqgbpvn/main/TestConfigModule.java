/**
 * 
 */
package com.chinarewards.qqgbpvn.main;

import org.apache.commons.configuration.Configuration;
import org.junit.Ignore;

import com.google.inject.AbstractModule;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
@Ignore
public class TestConfigModule extends AbstractModule {

	private Configuration configuration;

	public TestConfigModule(Configuration configuration) {
		this.configuration = configuration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		bind(Configuration.class).toInstance(configuration);
	}

	/**
	 * @return the configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

}
