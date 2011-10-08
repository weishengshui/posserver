package com.chinarewards.qqgbvpn.mgmtui.dao.finance;

import com.chinarewards.qqgbvpn.domain.FinanceReportHistory;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportSearchVO;

public interface FinanceReportHistoryDao {
	
	public FinanceReportHistory createFinanceReportHistory(FinanceReportSearchVO searchVO);
	
	public PageInfo<FinanceReportHistory> searchFinanceReportHistory(FinanceReportSearchVO searchVO, PageInfo pageInfo);

}
