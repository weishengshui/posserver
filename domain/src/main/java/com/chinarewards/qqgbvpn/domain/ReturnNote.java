package com.chinarewards.qqgbvpn.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.chinarewards.qqgbvpn.domain.status.ReturnNoteStatus;

/**
 * 
 * 
 * @author kmtong
 * @since 0.1.0
 */
@Entity
public class ReturnNote {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	String id;

	String rnNumber;

	@ManyToOne
	Agent agent;

	@Enumerated(EnumType.STRING)
	ReturnNoteStatus status;

	Date createDate;
	Date confirmDate;
	Date printDate;

	/**
	 * Copy of the agent name (for redundancy)
	 */
	String agentName;
	
	/**
	 * Copy of the ReturnNoteInvitation token
	 */
	String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRnNumber() {
		return rnNumber;
	}

	public void setRnNumber(String rnNumber) {
		this.rnNumber = rnNumber;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public ReturnNoteStatus getStatus() {
		return status;
	}

	public void setStatus(ReturnNoteStatus status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

}
