package com.chinarewards.qqgbvpn.main;

import javax.inject.Singleton;

import com.chinarewards.qq.meishi.conn.QQMeishiConnect;
import com.chinarewards.qq.meishi.conn.impl.QQMeishiConnectImpl;
import com.chinarewards.qq.meishi.service.QQMeishiService;
import com.chinarewards.qq.meishi.service.impl.QQMeishiServiceImpl;
import com.chinarewards.qqgbvpn.main.dao.qqapi.QQMeishiDao;
import com.chinarewards.qqgbvpn.main.dao.qqapi.impl.QQMeishiDaoImpl;
import com.chinarewards.qqgbvpn.main.logic.login.LoginManager;
import com.chinarewards.qqgbvpn.main.logic.login.impl.LoginManagerImpl;
import com.chinarewards.qqgbvpn.main.logic.qqapi.QQMeishiManager;
import com.chinarewards.qqgbvpn.main.logic.qqapi.impl.QQMeishiManagerImpl;
import com.google.inject.AbstractModule;

public class QQMeishiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(QQMeishiService.class).to(QQMeishiServiceImpl.class).in(
				Singleton.class);
		bind(QQMeishiConnect.class).to(QQMeishiConnectImpl.class).in(
				Singleton.class);

		bind(LoginManager.class).to(LoginManagerImpl.class);
		
		bind(QQMeishiManager.class).to(QQMeishiManagerImpl.class).in(
				Singleton.class);
		bind(QQMeishiDao.class).to(QQMeishiDaoImpl.class);
	}

}
