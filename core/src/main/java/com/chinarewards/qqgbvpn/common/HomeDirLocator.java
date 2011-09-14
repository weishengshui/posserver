/**
 * 
 */
package com.chinarewards.qqgbvpn.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class HomeDirLocator {

	String hardCodedDir;

	String envKey = "POSNET_HOME";
	
	Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Specifies the directory which will be served as the final
	 * 
	 * @param hardCodedDir
	 */
	public HomeDirLocator(String hardCodedDir) {
		this.hardCodedDir = hardCodedDir;
	}

	public String getHomeDir() {

		String r = hardCodedDir;
		if (r != null)
			return r;

		// look at environment variable
		r = this.getHomeDirFromSysEnv();
		if (r != null)
			return r;

		// look at the executing directory
		// TODO

		return r;

	}

	/**
	 * Returns the home directory based on the environment variable settings.
	 * The key of the environment variable is based on the method
	 * {@link #getHomeDirEnvName()}.
	 * 
	 * @return the home directory as found in the system environment.
	 *         <code>nulL</code> if not found.
	 */
	protected String getHomeDirFromSysEnv() {
		
		String envKey = getHomeDirEnvName();
		log.trace("Environment key for home directory: {}", envKey);

		String v = System.getProperties().getProperty(envKey);

		if (v == null) {
			// fallback
			v = System.getenv(envKey);
		}

		return v;

	}

	/**
	 * 
	 * @return
	 */
	public String getHomeDirEnvName() {
		return envKey;
	}

	public void setHomeDirEnvName(String envKey) {
		this.envKey = envKey;
	}

}
