package com.chinarewards.qqgbvpn.mgmtui.guice;

import javax.inject.Singleton;

import com.chinarewards.qqgbvpn.core.excel.ExcelService;
import com.chinarewards.qqgbvpn.core.excel.impl.ExcelServiceImpl;
import com.chinarewards.qqgbvpn.mgmtui.dao.finance.FinanceDao;
import com.chinarewards.qqgbvpn.mgmtui.dao.finance.FinanceReportHistoryDao;
import com.chinarewards.qqgbvpn.mgmtui.dao.finance.impl.FinanceDaoImpl;
import com.chinarewards.qqgbvpn.mgmtui.dao.finance.impl.FinanceReportHistoryDaoImpl;
import com.chinarewards.qqgbvpn.mgmtui.logic.finance.FinanceManager;
import com.chinarewards.qqgbvpn.mgmtui.logic.finance.impl.FinanceManagerImpl;
import com.google.inject.AbstractModule;

public class FinanceModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(FinanceManager.class).to(
				FinanceManagerImpl.class).in(Singleton.class);

		bind(FinanceDao.class).to(FinanceDaoImpl.class).in(Singleton.class);
		
		bind(FinanceReportHistoryDao.class).to(FinanceReportHistoryDaoImpl.class).in(Singleton.class);
		
		bind(ExcelService.class).to(ExcelServiceImpl.class).in(Singleton.class);
		
	}

}
