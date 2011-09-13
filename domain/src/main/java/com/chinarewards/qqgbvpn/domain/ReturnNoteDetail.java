package com.chinarewards.qqgbvpn.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.chinarewards.qqgbvpn.domain.status.PosDeliveryStatus;

/**
 * 
 * 
 * @author kmtong
 * @since 0.1.0
 */
@Entity
public class ReturnNoteDetail {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	String id;

	@ManyToOne(fetch = FetchType.LAZY)
	ReturnNote rn;

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
	
	@Enumerated(EnumType.STRING)
	PosDeliveryStatus dstatus;

	public PosDeliveryStatus getDstatus() {
		return dstatus;
	}

	public void setDstatus(PosDeliveryStatus dstatus) {
		this.dstatus = dstatus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ReturnNote getRn() {
		return rn;
	}

	public void setRn(ReturnNote rn) {
		this.rn = rn;
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

}
