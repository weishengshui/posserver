/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.impl;

import java.util.HashMap;
import java.util.Map;

import com.chinarewards.qqgbvpn.main.protocol.ServiceSession;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class SimpleSession implements ServiceSession {

	Map<String, Object> attributes;

	public SimpleSession() {
		this.attributes = new HashMap<String, Object>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chinarewards.qqgbvpn.main.protocol.ServiceSession#get(java.lang.String
	 * )
	 */
	@Override
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chinarewards.qqgbvpn.main.protocol.ServiceSession#set(java.lang.String
	 * , java.lang.Object)
	 */
	@Override
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}

}
