/**
 * 
 */
package com.chinarewards.qqgbpvn.main;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Ignore;

import com.google.inject.AbstractModule;

/**
 * Use this module if you want a global test configuration.
 * 
 * @author Cyril
 * @since 0.1.0
 */
@Ignore
public class CommonTestConfigModule extends AbstractModule {

	private Configuration configuration;

	public CommonTestConfigModule() {
		try {
			buildConfiguration();
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
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

	protected void buildConfiguration() throws ConfigurationException {

		PropertiesConfiguration conf = new PropertiesConfiguration();

		// load from resource
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("posnet.ini");
		conf.load(is);
		try {
			is.close();
			this.configuration = conf;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @return the configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

}
