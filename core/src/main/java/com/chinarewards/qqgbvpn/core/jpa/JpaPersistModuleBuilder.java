/**
 * 
 */
package com.chinarewards.qqgbvpn.core.jpa;

import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;

import com.google.inject.persist.jpa.JpaPersistModule;

/**
 * 
 * 
 * @author Cyril
 * @sicne 0.1.0
 */
public class JpaPersistModuleBuilder {

	/**
	 * Configure the JPA module.
	 * 
	 * @param module
	 * @param config
	 * @param configNamePrefix
	 */
	public void configModule(JpaPersistModule module, Configuration config,
			String configNamePrefix) {
		
		Properties props = buildHibernateProperties(config, configNamePrefix);
		module.properties(props);

	}

	/**
	 * 
	 * @param config
	 * @param configNamePrefix
	 * @return
	 */
	public Properties buildHibernateProperties(Configuration config,
			String configNamePrefix) {

		Properties prop = new Properties();
		// read the subset configurations
		Configuration dbConfig = null;

		// put all hibernate specific names into it.
		if (configNamePrefix == null) {
			dbConfig = config;
		} else {
			dbConfig = config.subset(configNamePrefix);
		}

		// set the hibernate properties first
		Configuration hibernateConfig = dbConfig.subset(getExtensionPath());
		@SuppressWarnings("rawtypes")
		Iterator iter = hibernateConfig.getKeys();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			//prop.put("hibernate." + key, hibernateConfig.getProperty(key));
			prop.put(key, hibernateConfig.getProperty(key));
		}
		
		// translate important hibernate attribute(s). This will override any hibernate specific attributes.
		
		
		if (dbConfig.containsKey("user")) {
			prop.put("hibernate.connection.username", dbConfig.getString("user"));
		}

		if (dbConfig.containsKey("password")) {
			prop.put("hibernate.connection.password", dbConfig.getString("password"));
		}
		
		if (dbConfig.containsKey("driver")) {
			prop.put("hibernate.connection.driver_class", dbConfig.getString("driver"));
		}
		
		if (dbConfig.containsKey("url")) {
			prop.put("hibernate.connection.url", dbConfig.getString("url"));
		}
		
		return prop;

	}
	
	/**
	 * Returns the path which distinguish between standard and extension 
	 * configuration keys.
	 * 
	 * @return
	 */
	protected String getExtensionPath() {
		return "ext";
	}

}
