package com.chinarewards.qqgbvpn.main;

import javax.inject.Singleton;

import com.chinarewards.qq.meishi.service.QQMeishiService;
import com.chinarewards.qq.meishi.service.impl.QQMeishiServiceImpl;
import com.chinarewards.qq.meishi.util.QQMeishiConnect;
import com.chinarewards.qq.meishi.util.impl.QQMeishiConnectImpl;
import com.chinarewards.qqgbvpn.main.logic.login.LoginManager;
import com.chinarewards.qqgbvpn.main.logic.login.impl.LoginManagerImpl;
import com.google.inject.AbstractModule;

public class QQMeishiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(QQMeishiService.class).to(QQMeishiServiceImpl.class).in(
				Singleton.class);
		bind(QQMeishiConnect.class).to(QQMeishiConnectImpl.class).in(
				Singleton.class);

		bind(LoginManager.class).to(LoginManagerImpl.class);
	}

}
