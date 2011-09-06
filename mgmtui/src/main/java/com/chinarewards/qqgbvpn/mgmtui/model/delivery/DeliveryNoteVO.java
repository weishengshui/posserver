package com.chinarewards.qqgbvpn.mgmtui.model.delivery;

import java.util.Date;

import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentVO;

public class DeliveryNoteVO {

	String id;

	String dnNumber;

	String status;

	Date createDate;
	Date confirmDate;
	Date printDate;

	AgentVO agent;

	/**
	 * Copy of the agent name (for redundancy)
	 */
	String agentName;

	@Override
	public String toString() {
		return "DeliveryNoteVO [id=" + id + ", dnNumber=" + dnNumber
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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

	public AgentVO getAgent() {
		return agent;
	}

	public void setAgent(AgentVO agent) {
		this.agent = agent;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

}
