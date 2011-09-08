package com.chinarewards.qqgbvpn.mgmtui.model.delivery;

public class DeliveryNoteDetailVO {

	String id;

	DeliveryNoteVO dn;

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

	/**
	 * POS initialization status.
	 */
	String istatus;

	/**
	 * POS secret.
	 */
	String secret;

	@Override
	public String toString() {
		return "DeliveryNoteDetailVO [id=" + id + ", dn=" + dn + ", posId="
				+ posId + ", model=" + model + ", sn=" + sn + ", simPhoneNo="
				+ simPhoneNo + ", istatus=" + istatus + ", secret=" + secret
				+ "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DeliveryNoteVO getDn() {
		return dn;
	}

	public void setDn(DeliveryNoteVO dn) {
		this.dn = dn;
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

	public String getIstatus() {
		return istatus;
	}

	public void setIstatus(String istatus) {
		this.istatus = istatus;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

}
