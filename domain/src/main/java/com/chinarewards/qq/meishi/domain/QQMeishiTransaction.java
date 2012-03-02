/**
 * 
 */
package com.chinarewards.qq.meishi.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

/**
 * Defines the data structure for storing QQ Meishi Q Mi transaction.
 * 
 * @author Cyril
 * @since 0.1.3
 */
@Entity
public class QQMeishiTransaction {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	String id;

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
	 * Transaction timestamp which the transaction is sent from POSNet client
	 * and is received by the server.
	 */
	Date ts;

	/**
	 * The token which represents the user identity. This matched the 
	 * attribute 'verifyCode' of QQ Meishi's Q-Mi transaction.
	 */
	String qqUserToken;
	
	/**
	 * The user's consumption amount (spendings) in this transaction.
	 */
	double consumeAmount;
	
	/**
	 * Transaction password entered by the merchant.
	 */
	String xactPwd;

	/**
	 * The transaction result code returned from QQ Meishi web service API.
	 * Related to the value of 'validCode' in the response.
	 */
	long xactResultCode;
	
}
