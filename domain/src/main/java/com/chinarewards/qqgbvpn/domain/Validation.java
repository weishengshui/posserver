package com.chinarewards.qqgbvpn.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.chinarewards.qqgbvpn.domain.status.ValidationStatus;

/**
 * Represents a Validation transaction, should be created by POS server. This
 * record should not be modified by any program once written.
 * 
 * @author kmtong
 * 
 */
@Entity
public class Validation {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
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
	 * POS ID, Should be copied from <code>PosAssignment</code>
	 */
	String posId;

	/**
	 * POS Model, Should be copied from <code>PosAssignment</code>
	 */
	String posModel;

	/**
	 * POS SIM Phone Number, Should be copied from <code>PosAssignment</code>
	 */
	String posSimPhoneNo;

	/**
	 * Validation Status returned by QQ.
	 */
	@Enumerated(EnumType.STRING)
	ValidationStatus status;

	/**
	 * Should be copied from <code>PosAssignment</code>
	 */
	String agentId;

	/**
	 * Should be copied from <code>PosAssignment</code>
	 */
	String agentName;
}
