package com.chinarewards.qqgbvpn.main;

import javax.inject.Singleton;

import com.chinarewards.qqgbvpn.config.URLProperties;
import com.chinarewards.qqgbvpn.main.dao.qqapi.GroupBuyingDao;
import com.chinarewards.qqgbvpn.main.dao.qqapi.PosDao;
import com.chinarewards.qqgbvpn.main.dao.qqapi.impl.GroupBuyingDaoImpl;
import com.chinarewards.qqgbvpn.main.dao.qqapi.impl.PosDaoImpl;
import com.chinarewards.qqgbvpn.main.logic.login.LoginManager;
import com.chinarewards.qqgbvpn.main.logic.login.impl.LoginManagerImpl;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.main.logic.qqapi.impl.GroupBuyingManagerImpl;
import com.chinarewards.qqgbvpn.qqapi.service.GroupBuyingService;
import com.chinarewards.qqgbvpn.qqapi.service.impl.GroupBuyingServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;

public class QQApiModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(GroupBuyingService.class).toProvider(new Provider<GroupBuyingService>() {
			@Override
			public GroupBuyingService get() {
				try {
					return new GroupBuyingServiceImpl(new URLProperties().getProperties());
				} catch (Exception e) {
					return null;
				}
			}
		}).in(Singleton.class);

		bind(GroupBuyingManager.class).to(GroupBuyingManagerImpl.class).in(Singleton.class);
		bind(LoginManager.class).to(LoginManagerImpl.class);

		bind(GroupBuyingDao.class).to(GroupBuyingDaoImpl.class);

		bind(PosDao.class).to(PosDaoImpl.class);
	}

}
