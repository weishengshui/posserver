/**
 * 
 */
package com.chinarewards.qqgbvpn.main.logic.journal;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public interface JournalLogic {

	/**
	 * Log the specified event.
	 * 
	 * @param event
	 *            name of the event
	 * @param domain
	 * @param entityId
	 * @param detail
	 */
	public void logEvent(String event, String domain, String entityId,
			String detail);

}
