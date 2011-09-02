package com.chinarewards.qqgbvpn.mgmtui.model.pos;

import com.chinarewards.qqgbvpn.domain.status.PosDeliveryStatus;
import com.chinarewards.qqgbvpn.domain.status.PosInitializationStatus;
import com.chinarewards.qqgbvpn.domain.status.PosOperationStatus;

public class PosVO implements java.io.Serializable{
	
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

	PosDeliveryStatus dstatus;

	PosInitializationStatus istatus;

	PosOperationStatus ostatus;

	// POS 内置唯一标识。6位字符
	String secret;

	//-------------------------------------------//

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
