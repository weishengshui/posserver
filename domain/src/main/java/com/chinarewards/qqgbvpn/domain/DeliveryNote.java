package com.chinarewards.qqgbvpn.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.chinarewards.qqgbvpn.domain.status.DeliveryNoteStatus;

@Entity
public class DeliveryNote {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	String id;

	String dnNumber;

	@Enumerated(EnumType.STRING)
	DeliveryNoteStatus status;

	Date createDate;
	Date confirmDate;
	Date printDate;

	@ManyToOne
	Agent agent;

	/**
	 * Copy of the agent name (for redundancy)
	 */
	String agentName;
}
