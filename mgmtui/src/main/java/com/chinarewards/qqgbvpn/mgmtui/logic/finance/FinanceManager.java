package com.chinarewards.qqgbvpn.mgmtui.logic.finance;

import java.util.List;

import com.chinarewards.qqgbvpn.domain.FinanceReportHistory;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportVO;


public interface FinanceManager {
	
	/**
	 * 报表查询（无分页）
	 * @author iori
	 * @param searchVO
	 * @return
	 */
	public List<FinanceReportVO> searchFinanceReport(FinanceReportSearchVO searchVO);
	
	/**
	 * 报表查询（有分页）
	 * @author iori
	 * @param searchVO
	 * @return
	 */
	public PageInfo<FinanceReportVO> searchFinanceReport(FinanceReportSearchVO searchVO, PageInfo pageInfo);
	
	/**
	 * 报表历史（有分页）
	 * @param searchVO
	 * @param pageInfo
	 * @return
	 */
	public PageInfo<FinanceReportHistory> searchFinanceReportHistory(FinanceReportSearchVO searchVO, PageInfo pageInfo);
	
	public FinanceReportHistory createFinanceReportHistory(FinanceReportSearchVO searchVO);
}
