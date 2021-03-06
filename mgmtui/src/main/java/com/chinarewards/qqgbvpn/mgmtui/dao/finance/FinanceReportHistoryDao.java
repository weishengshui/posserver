package com.chinarewards.qqgbvpn.mgmtui.dao.finance;

import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.FinanceReportHistory;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportSearchVO;

public interface FinanceReportHistoryDao {
	
	public PageInfo<FinanceReportHistory> searchFinanceReportHistory(FinanceReportSearchVO searchVO, PageInfo pageInfo);
	
	public FinanceReportHistory getFinanceReportHistoryById(String id);
	
	public FinanceReportHistory saveFinanceReportHistory(FinanceReportHistory financeReportHistory);
	
	public Agent getAgentById(String agentId);
	
	public FinanceReportHistory createFinanceReportHistory(FinanceReportHistory financeReportHistory);
	
}
