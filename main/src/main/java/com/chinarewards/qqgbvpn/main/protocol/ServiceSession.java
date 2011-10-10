/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public interface ServiceSession {
	
	public static final String CHALLENGE_SESSION_KEY = "_CHALLENGE_SESSION_KEY";

	public Object getAttribute(String key);

	public void setAttribute(String key, Object value);

}
