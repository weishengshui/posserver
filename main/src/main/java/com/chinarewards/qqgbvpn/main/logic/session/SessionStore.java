/**
 * 
 */
package com.chinarewards.qqgbvpn.main.logic.session;

import java.util.Date;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public interface SessionStore {

	/**
	 * Creates a new session using the specified POS ID.
	 * 
	 * @param posId
	 * @return the session ID.
	 */
	public String createSession(String posId);

	/**
	 * Creates a new session using the specified POS ID.
	 * 
	 * @param sessionId
	 *            the session ID
	 * @return the exact date which the session will be expired.
	 */
	public Date getExpiryDate(String sessionId);

	/**
	 * Immediately invalidate the session with matching session ID.
	 * 
	 * @param sessiondId
	 *            the session ID to be queried.
	 */
	public void invalidateSession(String sessiondId);

	/**
	 * Check whether the session is expired. This method returns
	 * <code>true</code> if the session ID is not found, or really expired.
	 * 
	 * @param sessionId
	 * @return <code>true</code> if the session ID is not found, or really
	 *         expired, <code>false</code> otherwise.
	 */
	public boolean isExpired(String sessionId);

}
