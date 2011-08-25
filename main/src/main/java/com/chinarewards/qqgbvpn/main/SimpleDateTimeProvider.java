/**
 * 
 */
package com.chinarewards.qqgbvpn.main;

import java.util.Date;

import com.chinarewards.qqgbvpn.common.DateTimeProvider;

/**
 * Simple implementation of <code>DateTimeProvider</code> which returns the
 * current date and time by returning an new instance of
 * <code>java.util.Date</code>.
 * 
 * @author Cyril
 * @since 1.0.0
 */
public class SimpleDateTimeProvider implements DateTimeProvider {

	/**
	 * Returns the current system date and time by returning a new instance of
	 * <code>java.util.Date</code>.
	 */
	public Date getTime() {
		return new Date();
	}

}
