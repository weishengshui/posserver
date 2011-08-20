/**
 * 
 */
package com.chinarewards.qqgbvpn.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * 
 * 
 * @author cyril
 * 
 */
public class LogConfigImpl implements LogConfig {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.microblogger.Log#setVerboseLevel(int)
	 */
	public void setVerboseLevel(int level) {
		
		if (true) return;
		
		// TODO implements verbose level runtime setting.

		Properties props = new Properties();
		try {
			InputStream configStream = Main.class
					.getResourceAsStream("/log4j.properties");
			props.load(configStream);
			configStream.close();
		} catch (IOException e) {
			System.err.println("Error: Cannot laod configuration file ");
		}
		
		
		if (level == 0) {
			
		}
		
		if (level == 1) {
			props.setProperty("log4j.logger.org.apache", "DEBUG");
			props.setProperty("log4j.logger.com.chinarewards.microblogger", "DEBUG");
		}
		
//		LogManager.resetConfiguration();
//		PropertyConfigurator.configure(props);
	}

}
