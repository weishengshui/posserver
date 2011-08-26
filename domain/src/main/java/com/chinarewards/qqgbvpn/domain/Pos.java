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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getSimPhoneNo() {
		return simPhoneNo;
	}

	public void setSimPhoneNo(String simPhoneNo) {
		this.simPhoneNo = simPhoneNo;
	}

	public PosDeliveryStatus getDstatus() {
		return dstatus;
	}

	public void setDstatus(PosDeliveryStatus dstatus) {
		this.dstatus = dstatus;
	}

	public PosInitializationStatus getIstatus() {
		return istatus;
	}

	public void setIstatus(PosInitializationStatus istatus) {
		this.istatus = istatus;
	}

	public PosOperationStatus getOstatus() {
		return ostatus;
	}

	public void setOstatus(PosOperationStatus ostatus) {
		this.ostatus = ostatus;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
}
