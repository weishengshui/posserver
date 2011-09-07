/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.guice;

import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.google.inject.AbstractModule;

/**
 * FIXME this is for temporary solution only.
 * 
 * @author cyril
 * @since 0.1.0
 * @deprecated
 */
public class HardCodedConfigModule extends AbstractModule {

	@Override
	protected void configure() {

		Configuration configuration = null;
		try {
			configuration = readConfigFromResource();
			debugPrintConfiguration(configuration);
		} catch (ConfigurationException e) {
			System.err.println("Error loading configuration from resource");
			e.printStackTrace();
		}
		// and bind it
		bind(Configuration.class).toInstance(configuration);

	}

	protected Configuration readConfigFromResource()
			throws ConfigurationException {

		PropertiesConfiguration configuration = new PropertiesConfiguration();

		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("posnet.ini");
		configuration.load(is);
		try {
			is.close();
		} catch (Throwable t) {
			// mute it.
		}

		return configuration;
	}

	protected void debugPrintConfiguration(Configuration conf) {
		Iterator iter = conf.getKeys();
		System.out.println("Configuration listing:");
		while (iter.hasNext()) {
			String key = (String)iter.next();
			System.out.println("- " + key + ": " + conf.getString(key));
		}
	}

}
