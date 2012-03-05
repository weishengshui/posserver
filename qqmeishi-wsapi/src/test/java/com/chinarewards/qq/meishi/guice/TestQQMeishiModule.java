package com.chinarewards.qq.meishi.guice;

import javax.inject.Singleton;

import org.junit.Ignore;

import com.chinarewards.qq.meishi.service.QQMeishiService;
import com.chinarewards.qq.meishi.service.impl.QQMeishiServiceImpl;
import com.chinarewards.qq.meishi.util.QQMeishiConnect;
import com.chinarewards.qq.meishi.util.impl.QQMeishiConnectImpl;
import com.google.inject.AbstractModule;

/**
 * @time 2012-3-5   下午04:24:38
 * @author Seek
 */
@Ignore
public class TestQQMeishiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(QQMeishiService.class).to(QQMeishiServiceImpl.class).in(
				Singleton.class);
		bind(QQMeishiConnect.class).to(QQMeishiConnectImpl.class).in(
				Singleton.class);
	}

}
