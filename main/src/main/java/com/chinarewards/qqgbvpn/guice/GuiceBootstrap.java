/**
 * 
 */
package com.chinarewards.qqgbvpn.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Entry point for all guice configurations. Register any Guice module here.
 * 
 * @author cyril
 * @since 0.1.0
 */
public class GuiceBootstrap extends GuiceServletContextListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.inject.servlet.GuiceServletContextListener#getInjector()
	 */
	@Override
	protected Injector getInjector() {

		Injector injector = null;

		Module[] modules = getModules();
		injector = Guice.createInjector(modules);

		return injector;
	}

	protected Module[] getModules() {

		Module[] modules = new Module[] {
				new QqgbvpnServletModule()
				// JPA module
		};

		return modules;
	}
}
