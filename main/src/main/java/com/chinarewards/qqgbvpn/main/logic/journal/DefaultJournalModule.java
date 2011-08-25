/**
 * 
 */
package com.chinarewards.qqgbvpn.main.logic.journal;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * Install this module to obtain journalling service.
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class DefaultJournalModule extends AbstractModule {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {

		bind(JournalLogic.class).to(DbJournalLogic.class).in(Singleton.class);

	}

}
