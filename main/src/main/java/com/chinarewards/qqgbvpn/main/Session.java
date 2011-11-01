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
	
	public void putAttribute(Object key, Object value);

	public Object getAttribute(Object key);

}
