package com.chinarewards.qqgbvpn.mgmtui.dao.finance;

import java.util.List;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportVO;



public interface FinanceDao {
	
	public List<FinanceReportVO> searchFinanceReport(FinanceReportSearchVO searchVO);
	
	public PageInfo<FinanceReportVO> searchFinanceReport(FinanceReportSearchVO searchVO, PageInfo pageInfo);
}
