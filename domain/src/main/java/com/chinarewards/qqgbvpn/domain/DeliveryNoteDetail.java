package com.chinarewards.qqgbvpn.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class DeliveryNoteDetail {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	String id;

	@ManyToOne
	DeliveryNote dn;

	/**
	 * POS ID for this Delivery Note (copied from Pos Entity)
	 */
	String posId;

	/**
	 * POS ID for this Delivery Note (copied from Pos Entity)
	 */
	String model;

	/**
	 * POS S/N for this Delivery Note (copied from Pos Entity)
	 */
	String sn;

	/**
	 * POS SIM Phone Number for this Delivery Note (copied from Pos Entity)
	 */
	String simPhoneNo;

}
