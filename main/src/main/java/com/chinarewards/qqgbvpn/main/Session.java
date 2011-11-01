/**
 * 
 */
package com.chinarewards.qqgbvpn.main;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public interface Session {
	
	public void setAttribute(Object key, Object value);

	public Object getAttribute(Object key);

	public boolean containsAttribute(String key);

}
