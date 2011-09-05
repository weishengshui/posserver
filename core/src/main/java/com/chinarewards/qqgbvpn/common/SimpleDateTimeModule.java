/**
 * 
 */
package com.chinarewards.qqgbvpn.common;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class SimpleDateTimeModule extends AbstractModule {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		bind(DateTimeProvider.class).to(SimpleDateTimeProvider.class).in(
				Singleton.class);
	}

}
