/**
 * 
 */
package com.chinarewards.qqgbvpn.main.guice;

import com.chinarewards.qqgbvpn.main.ApplicationModule;
import com.chinarewards.qqgbvpn.main.QQApiModule;
import com.google.inject.AbstractModule;

/**
 * 
 * 
 * 
 * @author cyril
 * @since 1.0.0
 */
public class AppModule extends AbstractModule {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {

		install(new ApplicationModule());

		install(new QQApiModule());
	}

}
