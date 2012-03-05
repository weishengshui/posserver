/**
 * 
 */
package com.chinarewards.qq.meishi.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.chinarewards.qq.meishi.domain.status.QQMeishiXactionResult;

/**
 * Defines the data structure for storing QQ Meishi Q Mi transaction.
 * 
 * The transaction data between the POS client and the QQ Meishi server is
 * stored in this entity.
 * 
 * @author Cyril
 * @since 0.1.3
 */
@Entity
public class QQMeishiXaction {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	String id;

	Date ts;

	String posId;

	String posModel;

	String posSimPhoneNo;

	String agentId;

	String agentName;

	String qqUserToken;

	double consumeAmount;

	String xactPwd;

	int xactResultCode;

	boolean forcePwdOnNextAction;

	Date remoteXactDate;

	String receiptTitle;

	String receiptTip;

	String remoteXactPwd;

	/**
	 * A unique identifier of this object.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Transaction timestamp which the transaction request is sent from POSNet
	 * client and is received by the server.
	 * 
	 * @return the ts
	 */
	public Date getTs() {
		return ts;
	}

	/**
	 * @param ts
	 *            the ts to set
	 */
	public void setTs(Date ts) {
		this.ts = ts;
	}

	/**
	 * POS ID, Should be copied from <code>PosAssignment</code>. The value
	 * should be <code>Pos.getPosId()</code>.
	 * 
	 * @return the posId
	 */
	public String getPosId() {
		return posId;
	}

	/**
	 * @param posId
	 *            the posId to set
	 */
	public void setPosId(String posId) {
		this.posId = posId;
	}

	/**
	 * POS Model, Should be copied from <code>PosAssignment</code>. The value
	 * should be <code>Pos.getModel()</code>.
	 * 
	 * @return the posModel
	 */
	public String getPosModel() {
		return posModel;
	}

	/**
	 * @param posModel
	 *            the posModel to set
	 */
	public void setPosModel(String posModel) {
		this.posModel = posModel;
	}

	/**
	 * POS SIM Phone Number, Should be copied from <code>PosAssignment</code>.
	 * The value should be <code>Pos.getSimPhoneNo()</code>.
	 * 
	 * @return the posSimPhoneNo
	 */
	public String getPosSimPhoneNo() {
		return posSimPhoneNo;
	}

	/**
	 * @param posSimPhoneNo
	 *            the posSimPhoneNo to set
	 */
	public void setPosSimPhoneNo(String posSimPhoneNo) {
		this.posSimPhoneNo = posSimPhoneNo;
	}

	/**
	 * Should be copied from <code>PosAssignment</code>. The value should be
	 * <code>Agent.getId()</code>.
	 * 
	 * @return the agentId
	 */
	public String getAgentId() {
		return agentId;
	}

	/**
	 * @param agentId
	 *            the agentId to set
	 */
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	/**
	 * Should be copied from <code>PosAssignment</code>. The value should be
	 * <code>Agent.getName()</code>.
	 * 
	 * @return the agentName
	 */
	public String getAgentName() {
		return agentName;
	}

	/**
	 * @param agentName
	 *            the agentName to set
	 */
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	/**
	 * QQ Meishi transaction specific data. The token which represents the user
	 * identity. This matched the attribute 'verifyCode' of QQ Meishi's Q-Mi
	 * transaction.
	 * 
	 * @return the qqUserToken
	 */
	public String getQqUserToken() {
		return qqUserToken;
	}

	/**
	 * @param qqUserToken
	 *            the qqUserToken to set
	 */
	public void setQqUserToken(String qqUserToken) {
		this.qqUserToken = qqUserToken;
	}

	/**
	 * QQ Meishi transaction specific data. The user's consumption amount
	 * (spendings) in this transaction.
	 * 
	 * @return the consumeAmount
	 */
	public double getConsumeAmount() {
		return consumeAmount;
	}

	/**
	 * @param consumeAmount
	 *            the consumeAmount to set
	 */
	public void setConsumeAmount(double consumeAmount) {
		this.consumeAmount = consumeAmount;
	}

	/**
	 * QQ Meishi transaction specific data. Transaction password entered by the
	 * merchant which will be sent to the server.
	 * 
	 * @return the xactPwd
	 */
	public String getXactPwd() {
		return xactPwd;
	}

	/**
	 * @param xactPwd
	 *            the xactPwd to set
	 */
	public void setXactPwd(String xactPwd) {
		this.xactPwd = xactPwd;
	}

	/**
	 * The transaction result code returned from QQ Meishi web service API.
	 * Related to the value of 'validCode' in the response.
	 * 
	 * See the class {@link QQMeishiXactionResult} which contains the list of
	 * possible values.
	 * 
	 * @return the xactResultCode
	 * @see QQMeishiXactionResult
	 */
	public int getXactResultCode() {
		return xactResultCode;
	}

	/**
	 * @param xactResultCode
	 *            the xactResultCode to set
	 */
	public void setXactResultCode(int xactResultCode) {
		this.xactResultCode = xactResultCode;
	}

	/**
	 * QQ Meishi transaction specific data. Whether password input should be
	 * enforced on the next action on the POS client.
	 * 
	 * @return the forcePwdOnNextAction
	 */
	public boolean isForcePwdOnNextAction() {
		return forcePwdOnNextAction;
	}

	/**
	 * @param forcePwdOnNextAction
	 *            the forcePwdOnNextAction to set
	 */
	public void setForcePwdOnNextAction(boolean forcePwdOnNextAction) {
		this.forcePwdOnNextAction = forcePwdOnNextAction;
	}

	/**
	 * QQ Meishi transaction specific data. The transaction date reported by the
	 * QQ Meishi server.
	 * 
	 * @return the remoteXactDate
	 */
	public Date getRemoteXactDate() {
		return remoteXactDate;
	}

	/**
	 * @param remoteXactDate
	 *            the remoteXactDate to set
	 */
	public void setRemoteXactDate(Date remoteXactDate) {
		this.remoteXactDate = remoteXactDate;
	}

	/**
	 * QQ Meishi transaction specific data. The receipt title returned from the
	 * server.
	 * 
	 * @return the receiptTitle
	 */
	public String getReceiptTitle() {
		return receiptTitle;
	}

	/**
	 * @param receiptTitle
	 *            the receiptTitle to set
	 */
	public void setReceiptTitle(String receiptTitle) {
		this.receiptTitle = receiptTitle;
	}

	/**
	 * QQ Meishi transaction specific data. The receipt content returned from
	 * the QQ server.
	 * 
	 * @return the receiptTip
	 */
	public String getReceiptTip() {
		return receiptTip;
	}

	/**
	 * @param receiptTip
	 *            the receiptTip to set
	 */
	public void setReceiptTip(String receiptTip) {
		this.receiptTip = receiptTip;
	}

	/**
	 * QQ Meishi transaction specific data. Returns the correct transaction
	 * password returned from the
	 * 
	 * @return the remoteXactPwd
	 */
	public String getRemoteXactPwd() {
		return remoteXactPwd;
	}

	/**
	 * @param remoteXactPwd
	 *            the remoteXactPwd to set
	 */
	public void setRemoteXactPwd(String remoteXactPwd) {
		this.remoteXactPwd = remoteXactPwd;
	}

}
