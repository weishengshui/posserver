package com.chinarewards.qqgbvpn.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.chinarewards.qqgbvpn.domain.status.DeliveryNoteStatus;

@Entity
public class DeliveryNote {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	String id;

	String dnNumber;

	@Enumerated(EnumType.STRING)
	DeliveryNoteStatus status;

	Date createDate;
	Date confirmDate;
	Date printDate;

	@ManyToOne(fetch = FetchType.LAZY)
	Agent agent;

	/**
	 * Copy of the agent name (for redundancy)
	 */
	String agentName;

	@Override
	public String toString() {
		return "DeliveryNote [id=" + id + ", dnNumber=" + dnNumber
				+ ", status=" + status + ", createDate=" + createDate
				+ ", confirmDate=" + confirmDate + ", printDate=" + printDate
				+ ", agent=" + agent + ", agentName=" + agentName + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDnNumber() {
		return dnNumber;
	}

	public void setDnNumber(String dnNumber) {
		this.dnNumber = dnNumber;
	}

	public DeliveryNoteStatus getStatus() {
		return status;
	}

	public void setStatus(DeliveryNoteStatus status) {
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

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
}
