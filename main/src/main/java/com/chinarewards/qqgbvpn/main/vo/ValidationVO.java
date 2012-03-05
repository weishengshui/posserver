package com.chinarewards.qqgbvpn.main.vo;

import java.util.Date;
import com.chinarewards.qqgbvpn.domain.status.CommunicationStatus;
import com.chinarewards.qqgbvpn.domain.status.ValidationStatus;


public class ValidationVO {


	String id;

	/**
	 * Validation Timestamp
	 */
	Date ts;

	/**
	 * Validation Code
	 */
	String vcode;

	/**
	 * Product Code
	 */
	String pcode;

	/**
	 * POS ID, Should be copied from <code>PosAssignment</code>. The value
	 * should be <code>Pos.getPosId()</code>.
	 */
	String posId;

	/**
	 * POS Model, Should be copied from <code>PosAssignment</code>. The value
	 * should be <code>Pos.getModel()</code>.
	 */
	String posModel;

	/**
	 * POS SIM Phone Number, Should be copied from <code>PosAssignment</code>.
	 * The value should be <code>Pos.getSimPhoneNo()</code>.
	 */
	String posSimPhoneNo;

	/**
	 * Validation Status returned by QQ.
	 */
	ValidationStatus status;
	
	/**
	 * Communication Status returned by QQ.
	 */
	CommunicationStatus cstatus;

	/**
	 * Result Status returned by QQ.
	 */
	String resultStatus;
	
	/**
	 * Result Name returned by QQ.
	 */
	String resultName;
	
	/**
	 * Result Explain returned by QQ.
	 */
	String resultExplain;
	
	/**
	 * Current Time returned by QQ.
	 */
	String currentTime;
	
	/**
	 * Use Time returned by QQ.
	 */
	String useTime;
	
	/**
	 * Valid Time returned by QQ.
	 */
	String validTime;
	
	/**
	 * Refund Time returned by QQ.
	 */
	String refundTime;
	
	/**
	 * Should be copied from <code>PosAssignment</code>. The value should be
	 * <code>Agent.getId()</code>.
	 */
	String agentId;

	/**
	 * Should be copied from <code>PosAssignment</code>. The value should be
	 * <code>Agent.getName()</code>.
	 */
	String agentName;

	public String getResultName() {
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public String getResultExplain() {
		return resultExplain;
	}

	public void setResultExplain(String resultExplain) {
		this.resultExplain = resultExplain;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public String getUseTime() {
		return useTime;
	}

	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}

	public String getValidTime() {
		return validTime;
	}

	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}

	public String getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}

	public CommunicationStatus getCstatus() {
		return cstatus;
	}

	public void setCstatus(CommunicationStatus cstatus) {
		this.cstatus = cstatus;
	}

	public String getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public String getPosModel() {
		return posModel;
	}

	public void setPosModel(String posModel) {
		this.posModel = posModel;
	}

	public String getPosSimPhoneNo() {
		return posSimPhoneNo;
	}

	public void setPosSimPhoneNo(String posSimPhoneNo) {
		this.posSimPhoneNo = posSimPhoneNo;
	}

	public ValidationStatus getStatus() {
		return status;
	}

	public void setStatus(ValidationStatus status) {
		this.status = status;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	
}
