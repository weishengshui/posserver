package com.chinarewards.qqgbvpn.mgmtui.guice;

import javax.inject.Singleton;

import com.chinarewards.qqgbvpn.core.mail.MailService;
import com.chinarewards.qqgbvpn.mgmtui.dao.GroupBuyingUnbindDao;
import com.chinarewards.qqgbvpn.mgmtui.dao.impl.GroupBuyingUnbindDaoImpl;
import com.chinarewards.qqgbvpn.mgmtui.logic.GroupBuyingUnbindManager;
import com.chinarewards.qqgbvpn.mgmtui.logic.impl.GroupBuyingUnbindManagerImpl;
import com.chinarewards.qqgbvpn.mgmtui.service.impl.MailServiceImpl;
import com.chinarewards.qqgbvpn.qqapi.service.GroupBuyingService;
import com.chinarewards.qqgbvpn.qqapi.service.impl.GroupBuyingServiceImpl;
import com.google.inject.AbstractModule;

public class QQApiModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(GroupBuyingService.class).to(GroupBuyingServiceImpl.class).in(
				Singleton.class);

		bind(GroupBuyingUnbindManager.class).to(
				GroupBuyingUnbindManagerImpl.class).in(Singleton.class);

		bind(GroupBuyingUnbindDao.class).to(GroupBuyingUnbindDaoImpl.class);
		
		bind(MailService.class).to(MailServiceImpl.class).in(Singleton.class);

	}

}
