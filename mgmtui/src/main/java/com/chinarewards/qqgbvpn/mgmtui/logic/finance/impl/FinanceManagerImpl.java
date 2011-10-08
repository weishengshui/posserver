package com.chinarewards.qqgbvpn.mgmtui.logic.finance.impl;

import java.util.List;

import com.chinarewards.qqgbvpn.domain.FinanceReportHistory;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.dao.finance.FinanceDao;
import com.chinarewards.qqgbvpn.mgmtui.dao.finance.FinanceReportHistoryDao;
import com.chinarewards.qqgbvpn.mgmtui.logic.finance.FinanceManager;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportVO;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

public class FinanceManagerImpl implements FinanceManager {
	
	@Inject
	private Provider<FinanceDao> dao;
	
	@Inject
	private Provider<FinanceReportHistoryDao> hdao;
	
	public List<FinanceReportVO> searchFinanceReport(FinanceReportSearchVO searchVO) {
		return dao.get().searchFinanceReport(searchVO);
	}
	
	public PageInfo<FinanceReportVO> searchFinanceReport(FinanceReportSearchVO searchVO, PageInfo pageInfo) {
		return dao.get().searchFinanceReport(searchVO, pageInfo);
	}

	@Override
	public PageInfo<FinanceReportHistory> searchFinanceReportHistory(
			FinanceReportSearchVO searchVO, PageInfo pageInfo) {
		return hdao.get().searchFinanceReportHistory(searchVO, pageInfo);
	}

	@Transactional
	public FinanceReportHistory createFinanceReportHistory(
			FinanceReportSearchVO searchVO) {
		return hdao.get().createFinanceReportHistory(searchVO);
	}
	
	public FinanceReportHistory getFinanceReportHistoryById(String id) {
		return hdao.get().getFinanceReportHistoryById(id);
	}
	
	@Transactional
	public FinanceReportHistory saveFinanceReportHistory(FinanceReportHistory financeReportHistory) {
		return hdao.get().saveFinanceReportHistory(financeReportHistory);
	}
	
	
}
