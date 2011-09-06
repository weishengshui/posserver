/**
 * 
 */
package com.chinarewards.qqgbvpn.common;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class HomeDirLocator {

	String hardCodedDir;

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

		String v = System.getProperties().getProperty(getHomeDirEnvName());

		if (v == null) {
			// fallback
			v = System.getenv(getHomeDirEnvName());
		}

		return v;

	}

	/**
	 * 
	 * @return
	 */
	public String getHomeDirEnvName() {
		return "POSNET_HOME";
	}

}
