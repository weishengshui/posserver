package com.chinarewards.qqgbvpn.mgmtui.logic.finance.impl;

import java.util.List;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.dao.finance.FinanceDao;
import com.chinarewards.qqgbvpn.mgmtui.logic.finance.FinanceManager;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportVO;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class FinanceManagerImpl implements FinanceManager {
	
	@Inject
	private Provider<FinanceDao> dao;
	
	public List<FinanceReportVO> searchFinanceReport(FinanceReportSearchVO searchVO) {
		return dao.get().searchFinanceReport(searchVO);
	}
	
	public PageInfo<FinanceReportVO> searchFinanceReport(FinanceReportSearchVO searchVO, PageInfo pageInfo) {
		return dao.get().searchFinanceReport(searchVO, pageInfo);
	}
}
