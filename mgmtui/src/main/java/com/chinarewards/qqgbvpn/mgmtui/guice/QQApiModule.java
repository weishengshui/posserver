package com.chinarewards.qqgbvpn.mgmtui.guice;

import javax.inject.Singleton;

import com.chinarewards.qqgbvpn.config.URLProperties;
import com.chinarewards.qqgbvpn.mgmtui.dao.GroupBuyingUnbindDao;
import com.chinarewards.qqgbvpn.mgmtui.dao.impl.GroupBuyingUnbindDaoImpl;
import com.chinarewards.qqgbvpn.mgmtui.logic.GroupBuyingUnbindManager;
import com.chinarewards.qqgbvpn.mgmtui.logic.impl.GroupBuyingUnbindManagerImpl;
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

		bind(GroupBuyingUnbindManager.class).to(GroupBuyingUnbindManagerImpl.class).in(Singleton.class);

		bind(GroupBuyingUnbindDao.class).to(GroupBuyingUnbindDaoImpl.class);

	}

}
