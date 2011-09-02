/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol;

import java.util.Iterator;

import org.apache.commons.configuration.Configuration;

import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class CodecMappingConfigBuilder {

	/**
	 * We assumed that the passed-in configuration is the complete configuration
	 * item.
	 * 
	 * @param config
	 * @return
	 */
	public CmdMapping buildMapping(Configuration configuration) {

		CmdMapping mapping = new SimpleCmdMapping();

		Configuration msgConf = configuration.subset("msg");
		Iterator iter = msgConf.getKeys();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			// skip unwanted conf keys.
			if (!key.endsWith(".codec")) {
				continue;
			}

			// get the class name.
			String className = msgConf.getString(key);
			
			// extract the command ID
			String sCmdId = key.substring(0, key.indexOf("."));
			long commandId = Long.parseLong(sCmdId);
			
			try {
				Class<ICommandCodec> clazz = (Class<ICommandCodec>) Class
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
