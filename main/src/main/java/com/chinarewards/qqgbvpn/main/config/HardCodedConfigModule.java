/**
 * 
 */
package com.chinarewards.qqgbvpn.main.config;

import org.apache.commons.configuration.Configuration;

import com.google.inject.AbstractModule;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class HardCodedConfigModule extends AbstractModule {

	Configuration configuration;

	public HardCodedConfigModule(Configuration configuration) {
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

}
