package com.chinarewards.qqgbvpn.domain.event;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Business Event Journal for replay and audit purpose. This record should not
 * be modified by any program once written.
 * 
 * @author kmtong
 * @since 0.1.0
 */
@Entity
public class Journal {

	@Id
	@GeneratedValue
	Long id;

	/**
	 * Timestamp. Accuracy down to milliseconds. Should not be <code>null</code>
	 * .
	 */
	Date ts;

	/**
	 * The type of event. Should not be <code>null</code>. Refer to the value of
	 * DomainEvent.
	 * 
	 * @see DomainEvent
	 */
	// @Enumerated(EnumType.STRING)
	String event;

	/**
	 * The type of entity which is related to this event. Should not be
	 * <code>null</code>.
	 * <p>
	 * 
	 * @see DomainEntity
	 */
	// @Enumerated(EnumType.STRING)
	String entity;

	/**
	 * The identity of the entity. This value, together with <code>entity</code>
	 * , can uniquely identify the entity which is related to this journal item.
	 */
	String entityId;

	/**
	 * Details related to this event. Optional. Should be <code>null</code> if
	 * no detail is available.
	 */
	String eventDetail;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the ts
	 */
	public Date getTs() {
		return ts;
	}

	/**
	 * @param ts
	 *            the ts to set
	 */
	public void setTs(Date ts) {
		this.ts = ts;
	}

	/**
	 * @return the event
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * @param event
	 *            the event to set
	 */
	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}

	/**
	 * @return the entityId
	 */
	public String getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId
	 *            the entityId to set
	 */
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the eventDetail
	 */
	public String getEventDetail() {
		return eventDetail;
	}

	/**
	 * @param eventDetail
	 *            the eventDetail to set
	 */
	public void setEventDetail(String eventDetail) {
		this.eventDetail = eventDetail;
	}

}
