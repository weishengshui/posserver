package com.chinarewards.qqgbvpn.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.GenericGenerator;

import com.chinarewards.qqgbvpn.domain.status.FinanceReportHistoryStatus;

/**
 * 
 * 
 * @author iori
 * @since 0.1.0
 */
@Entity
public class FinanceReportHistory {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	String id;

	/**
	 * search condition, Copy of the agent id (for redundancy)
	 */
	String agentId;

	/**
	 * search condition, Copy of the agent name (for redundancy)
	 */
	String agentName;
	
	/**
	 * search condition, Copy of the startDate (for redundancy)
	 */
	Date startDate;
	
	/**
	 * search condition, Copy of the endDate (for redundancy)
	 */
	Date endDate;

	@Enumerated(EnumType.STRING)
	FinanceReportHistoryStatus status;
	
	@Lob
	String reportDetail;

	Date createDate;
	
	Date modifyDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public FinanceReportHistoryStatus getStatus() {
		return status;
	}

	public void setStatus(FinanceReportHistoryStatus status) {
		this.status = status;
	}

	public String getReportDetail() {
		return reportDetail;
	}

	public void setReportDetail(String reportDetail) {
		this.reportDetail = reportDetail;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}


}
