/**
 * 
 */
package com.chinarewards.qqgbvpn.main;

import javax.inject.Singleton;

import com.chinarewards.qqgbvpn.common.SimpleDateTimeModule;
import com.chinarewards.qqgbvpn.config.CmdProperties;
import com.chinarewards.qqgbvpn.main.protocol.filter.TransactionFilter;
import com.google.inject.AbstractModule;

/**
 * 
 * 
 * @author Cyril
 * @since 1.0.0
 */
public class ApplicationModule extends AbstractModule {

	@Override
	protected void configure() {

		install(new SimpleDateTimeModule());

		bind(AppPreference.class).in(Singleton.class);

		// singleton first. can change.
		bind(Application.class).in(Singleton.class);

		// log
		bind(LogConfig.class).to(LogConfigImpl.class).in(Singleton.class);
		
		bind(CmdProperties.class).in(Singleton.class);

		bind(TransactionFilter.class);
	}

}
