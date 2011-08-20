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
 * 
 */
@Entity
public class Journal {

	@Id
	@GeneratedValue
	Long id;

	/**
	 * Timestamp
	 */
	Date ts;

	@Enumerated(EnumType.STRING)
	DomainEvent event;

	@Enumerated(EnumType.STRING)
	DomainEntity entity;

	String entityId;

	String eventDetail;
}
