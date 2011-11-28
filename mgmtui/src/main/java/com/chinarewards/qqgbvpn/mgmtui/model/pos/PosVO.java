package com.chinarewards.qqgbvpn.mgmtui.model.pos;

import java.util.Date;

public class PosVO implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6142804305459084636L;

	String id;

	String posId;

	String model;

	/**
	 * Serial number.
	 */
	String sn;

	String simPhoneNo;

	String dstatus;

	String deliveryAgent;

	String istatus;

	String ostatus;

	// POS 内置唯一标识。6位字符
	String secret;

	Date createAt;

	Date lastModifyAt;
	
	String firmware;
	
	Boolean upgradeRequired;

	long version;
	
	
	public PosVO(){}

	public PosVO(String id, String posId, String model, String sn,
			String simPhoneNo, String dstatus, String deliveryAgent,
			String istatus, String ostatus, String secret, Date createAt,
			Date lastModifyAt, String firmware, Boolean upgradeRequired,
			long version) {
		super();
		this.id = id;
		this.posId = posId;
		this.model = model;
		this.sn = sn;
		this.simPhoneNo = simPhoneNo;
		this.dstatus = dstatus;
		this.deliveryAgent = deliveryAgent;
		this.istatus = istatus;
		this.ostatus = ostatus;
		this.secret = secret;
		this.createAt = createAt;
		this.lastModifyAt = lastModifyAt;
		this.firmware = firmware;
		this.upgradeRequired = upgradeRequired;
		this.version = version;
	}

	// -------------------------------------------//


	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

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

	public String getDstatus() {
		return dstatus;
	}

	public void setDstatus(String dstatus) {
		this.dstatus = dstatus;
	}

	public String getIstatus() {
		return istatus;
	}

	public void setIstatus(String istatus) {
		this.istatus = istatus;
	}

	public String getOstatus() {
		return ostatus;
	}

	public void setOstatus(String ostatus) {
		this.ostatus = ostatus;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getDeliveryAgent() {
		return deliveryAgent;
	}

	public void setDeliveryAgent(String deliveryAgent) {
		this.deliveryAgent = deliveryAgent;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getLastModifyAt() {
		return lastModifyAt;
	}

	public void setLastModifyAt(Date lastModifyAt) {
		this.lastModifyAt = lastModifyAt;
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

}
