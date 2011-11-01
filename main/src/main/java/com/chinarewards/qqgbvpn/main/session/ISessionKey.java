/**
 * 
 */
package com.chinarewards.qqgbvpn.main.session;

/**
 * Defines the structure of a session key.
 * 
 * @author Cyril
 * @since 0.1.0
 */
public interface ISessionKey {

	/**
	 * Returns the version for the session key.
	 * 
	 * @return
	 */
	public int getVersion();

	/**
	 * 
	 * @return
	 */
	public String getSessionId();

}
