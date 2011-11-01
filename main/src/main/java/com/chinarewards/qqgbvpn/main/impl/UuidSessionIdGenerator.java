/**
 * 
 */
package com.chinarewards.qqgbvpn.main.impl;

import java.util.UUID;

import com.chinarewards.qqgbvpn.main.SessionIdGenerator;

/**
 * Generate UUID. The UUID is 32 characters in lower case alphabets and numbers.
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class UuidSessionIdGenerator implements SessionIdGenerator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbvpn.main.SessionIdGenerator#generate()
	 */
	@Override
	public String generate() {
		return UUID.randomUUID().toString().toLowerCase().replace("-", "");
	}

}
