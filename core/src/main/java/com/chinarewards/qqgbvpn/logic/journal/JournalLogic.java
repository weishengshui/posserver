/**
 * 
 */
package com.chinarewards.qqgbvpn.logic.journal;

import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;

/**
 * Defines the interface of journalling service.
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
	 * @param entity
	 * @param entityId
	 * @param eventDetail
	 */
	public void logEvent(String event, String entity, String entityId,
			String eventDetail);

	/**
	 * Log the specified event.
	 * 
	 * @param event
	 *            name of the event
	 * @param entity
	 * @param entityId
	 * @param eventDetail
	 */
	public void logEvent(DomainEvent event, DomainEntity entity,
			String entityId, String eventDetail);

}
