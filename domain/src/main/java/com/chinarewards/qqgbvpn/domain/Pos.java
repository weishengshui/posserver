package com.chinarewards.qqgbvpn.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.chinarewards.qqgbvpn.domain.status.PosDeliveryStatus;
import com.chinarewards.qqgbvpn.domain.status.PosInitializationStatus;
import com.chinarewards.qqgbvpn.domain.status.PosOperationStatus;

/**
 * Represents a physical POS in the network.
 * 
 * This entity should be created by management UI, used by POS server.
 * 
 * @author kmtong
 * @since 0.1.0
 */
@Entity
public class Pos {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	String id;

	String posId;

	String model;

	/**
	 * Serial number.
	 */
	String sn;

	String simPhoneNo;

	@Enumerated(EnumType.STRING)
	PosDeliveryStatus dstatus;

	@Enumerated(EnumType.STRING)
	PosInitializationStatus istatus;

	@Enumerated(EnumType.STRING)
	PosOperationStatus ostatus;

	String secret;
}
