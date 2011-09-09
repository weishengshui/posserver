package com.chinarewards.qqgbvpn.mgmtui.model.delivery;

import java.util.Date;

import com.chinarewards.qqgbvpn.mgmtui.model.util.PaginationTools;

public class DeliverySearchVO {

	String id;

	String dnNumber;

	String status;

	Date createDate;
	Date confirmDate;
	Date printDate;

	String agentId;
	String agentName;

	PaginationTools pagination;

	@Override
	public String toString() {
		return "DeliverySearchVO [id=" + id + ", dnNumber=" + dnNumber
				+ ", status=" + status + ", createDate=" + createDate
				+ ", confirmDate=" + confirmDate + ", printDate=" + printDate
				+ ", agentId=" + agentId + ", agentName=" + agentName
				+ ", pagination=" + pagination + "]";
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

	public PaginationTools getPagination() {
		return pagination;
	}

	public void setPagination(PaginationTools pagination) {
		this.pagination = pagination;
	}

}
