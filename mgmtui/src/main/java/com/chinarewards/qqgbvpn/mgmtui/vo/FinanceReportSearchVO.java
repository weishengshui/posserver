package com.chinarewards.qqgbvpn.mgmtui.vo;

import java.io.Serializable;
import java.util.Date;

import com.chinarewards.qqgbvpn.domain.FinanceReportHistory;
import com.chinarewards.qqgbvpn.domain.status.FinanceReportHistoryStatus;

public class FinanceReportSearchVO implements Serializable {

	private static final long serialVersionUID = 2672324894689357121L;

	private String agentId;
	
	private String agentName;
	
	private Date startDate;
	
	private Date endDate;
	
	private FinanceReportHistoryStatus financeReportHistoryStatus;

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

	public FinanceReportHistoryStatus getFinanceReportHistoryStatus() {
		return financeReportHistoryStatus;
	}

	public void setFinanceReportHistoryStatus(String financeReportHistoryStatus) {
		if(financeReportHistoryStatus == null)return;
		if(financeReportHistoryStatus == "CREATING"){
			this.financeReportHistoryStatus = FinanceReportHistoryStatus.CREATING;
		}else if(financeReportHistoryStatus == "COMPLETION"){
			this.financeReportHistoryStatus = FinanceReportHistoryStatus.COMPLETION;
		}else if(financeReportHistoryStatus == "FAILED"){
			this.financeReportHistoryStatus = FinanceReportHistoryStatus.FAILED;
		}
	}
	

}
