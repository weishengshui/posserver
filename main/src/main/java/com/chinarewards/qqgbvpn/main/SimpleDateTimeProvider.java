/**
 * 
 */
package com.chinarewards.qqgbvpn.main;

import java.util.Date;

/**
 * Simple implementation of <code>DateTimeProvider</code> which returns the
 * current date and time by returning an new instance of
 * <code>java.util.Date</code>.
 * 
 * @author Cyril
 * @since 1.0.0
 */
public class SimpleDateTimeProvider implements DateTimeProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.microblogger.DateTimeProvider#getTime()
	 */
	public Date getTime() {
		return new Date();
	}

}
