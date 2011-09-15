package com.chinarewards.qqgbvpn.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import com.chinarewards.qqgbvpn.domain.status.PosDeliveryStatus;
import com.chinarewards.qqgbvpn.domain.status.PosInitializationStatus;
import com.chinarewards.qqgbvpn.domain.status.PosOperationStatus;

/**
 * Represents a physical POS in the network.
 * <p>
 * 
 * This entity should be created by management UI, used by POS server.
 * <p>
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

	@Column(unique = true, nullable = false)
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

	// POS 内置唯一标识。6位字符
	String secret;
	
	String firmware;
	
	Boolean upgradeRequired;
	
//	// save the challenge code.
//	/**
//	 * FIXME should not use this. This is session specific.
//	 * 
//	 * @deprecated
//	 */
//	byte[] challenge;
	
	@Version
    long version;	//2011-09-13   Seek

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the POS machine serial number. No more than one POS machines
	 * should have the same POS ID.
	 * <p>
	 * 
	 * Chinese: POS机编号
	 * 
	 * @return
	 */
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

	/**
	 * Returns the manufacturer-assigned serial number
	 * 
	 * @return
	 */
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

	public String getFirmware() {
		return firmware;
	}

	public void setFirmware(String firmware) {
		this.firmware = firmware;
	}

	public Boolean getUpgradeRequired() {
		return upgradeRequired;
	}

	public void setUpgradeRequired(Boolean upgradeRequired) {
		this.upgradeRequired = upgradeRequired;
	}
	

//	/**
//	 * FIXME should not use this. This is session specific.
//	 * 
//	 * @deprecated
//	 */
//	public byte[] getChallenge() {
//		return challenge;
//	}
//
//	/**
//	 * FIXME should not use this. This is session specific.
//	 * 
//	 * @deprecated
//	 */
//	public void setChallenge(byte[] challenge) {
//		this.challenge = challenge;
//	}
	
	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
	
	@Override
	public String toString() {
		return "Pos [id=" + id + ", posId=" + posId + ", model=" + model
				+ ", sn=" + sn + ", simPhoneNo=" + simPhoneNo + ", dstatus="
				+ dstatus + ", istatus=" + istatus + ", ostatus=" + ostatus
				+ ", secret=" + secret + ", firmware=" + firmware
				+ "upgradeRequired=" + upgradeRequired + "]";
	}

}
