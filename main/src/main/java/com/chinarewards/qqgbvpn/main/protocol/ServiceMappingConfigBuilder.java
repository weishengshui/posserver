/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol;

import java.util.Iterator;

import org.apache.commons.configuration.Configuration;

import com.chinarewards.qqgbvpn.main.protocol.impl.SimpleServiceMapping;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class ServiceMappingConfigBuilder {

	/**
	 * We assumed that the passed-in configuration is the complete configuration
	 * item.
	 * 
	 * @param config
	 * @return
	 */
	public ServiceMapping buildMapping(Configuration configuration) {

		ServiceMapping mapping = new SimpleServiceMapping();

		Configuration msgConf = configuration.subset("msg");
		Iterator iter = msgConf.getKeys();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			// skip unwanted conf keys.
			if (!key.endsWith(".handler")) {
				continue;
			}

			// get the class name.
			String className = msgConf.getString(key);
			
			// extract the command ID
			String sCmdId = key.substring(0, key.indexOf("."));
			long commandId = Long.parseLong(sCmdId);
			
			try {
				Class<ServiceHandler> clazz = (Class<ServiceHandler>) Class
						.forName(className);
				mapping.addMapping(commandId, clazz);
			} catch (ClassNotFoundException e) {
				// XXX throws better exception
				throw new RuntimeException(e);
			}

		}
		
		return mapping;
		
	}

}
