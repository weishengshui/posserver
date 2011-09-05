/**
 * 
 */
package com.chinarewards.qqgbvpn.common;

import java.util.Date;

/**
 * Simple implementation of <code>DateTimeProvider</code> which simply returns
 * the current date by calling <code>new java.util.Date()</code>.
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class SimpleDateTimeProvider implements DateTimeProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbvpn.common.DateTimeProvider#getTime()
	 */
	@Override
	public Date getTime() {
		return new Date();
	}

}
