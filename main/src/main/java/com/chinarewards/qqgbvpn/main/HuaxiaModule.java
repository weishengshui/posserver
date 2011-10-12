package com.chinarewards.qqgbvpn.main;

import javax.inject.Singleton;

import com.chinarewards.qqgbvpn.main.dao.huaxia.HuaxiaRedeemDao;
import com.chinarewards.qqgbvpn.main.dao.huaxia.impl.HuaxiaRedeemDaoImpl;
import com.chinarewards.qqgbvpn.main.logic.huaxia.HuaxiaRedeemManager;
import com.chinarewards.qqgbvpn.main.logic.huaxia.impl.HuaxiaRedeemManagerImpl;
import com.google.inject.AbstractModule;

public class HuaxiaModule extends AbstractModule {

	@Override
	protected void configure() {


		bind(HuaxiaRedeemManager.class).to(HuaxiaRedeemManagerImpl.class).in(
				Singleton.class);

		bind(HuaxiaRedeemDao.class).to(HuaxiaRedeemDaoImpl.class);

	}

}
