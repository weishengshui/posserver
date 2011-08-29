/**
 * 
 */
package com.chinarewards.qqgbvpn.main;

import com.chinarewards.qqgbvpn.main.impl.DefaultPosServer;
import com.google.inject.AbstractModule;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class ServerModule extends AbstractModule {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {

		bind(PosServer.class).to(DefaultPosServer.class);

	}

}
