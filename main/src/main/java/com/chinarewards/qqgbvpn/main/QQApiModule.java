package com.chinarewards.qqgbvpn.main;

import javax.inject.Singleton;

import com.chinarewards.qqgbvpn.main.dao.qqapi.GroupBuyingDao;
import com.chinarewards.qqgbvpn.main.dao.qqapi.PosDao;
import com.chinarewards.qqgbvpn.main.dao.qqapi.impl.GroupBuyingDaoImpl;
import com.chinarewards.qqgbvpn.main.dao.qqapi.impl.PosDaoImpl;
import com.chinarewards.qqgbvpn.main.logic.firmware.FirmwareManager;
import com.chinarewards.qqgbvpn.main.logic.firmware.impl.FirmwareManagerImpl;
import com.chinarewards.qqgbvpn.main.logic.login.LoginManager;
import com.chinarewards.qqgbvpn.main.logic.login.impl.LoginManagerImpl;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.main.logic.qqapi.impl.GroupBuyingManagerImpl;
import com.chinarewards.qqgbvpn.qqapi.service.GroupBuyingService;
import com.chinarewards.qqgbvpn.qqapi.service.impl.GroupBuyingServiceImpl;
import com.google.inject.AbstractModule;

public class QQApiModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(GroupBuyingService.class).to(GroupBuyingServiceImpl.class).in(
				Singleton.class);

		bind(GroupBuyingManager.class).to(GroupBuyingManagerImpl.class).in(
				Singleton.class);
		bind(LoginManager.class).to(LoginManagerImpl.class);
		bind(FirmwareManager.class).to(FirmwareManagerImpl.class).in(
				Singleton.class);

		bind(GroupBuyingDao.class).to(GroupBuyingDaoImpl.class);

		bind(PosDao.class).to(PosDaoImpl.class);
	}

}
