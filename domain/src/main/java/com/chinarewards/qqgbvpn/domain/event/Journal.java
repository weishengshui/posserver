package com.chinarewards.qqgbvpn.domain.event;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
	 * Timestamp. Accuracy down to milliseconds. Should not be
	 * <code>null</code>>
	 */
	Date ts;

	/**
	 * The type of event. Should not be <code>null</code>.
	 */
	@Enumerated(EnumType.STRING)
	DomainEvent event;

	/**
	 * The type of entity which is related to this event. Should not be
	 * <code>null</code>.
	 */
	@Enumerated(EnumType.STRING)
	DomainEntity entity;

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
}
