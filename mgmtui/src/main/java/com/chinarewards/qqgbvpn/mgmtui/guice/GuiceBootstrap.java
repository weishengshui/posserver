/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.guice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	Logger log = LoggerFactory.getLogger(getClass());

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.inject.servlet.GuiceServletContextListener#getInjector()
	 */
	protected Injector getInjector() {

		System.out.println("Guice bootstrapping");
		log.info("Guice bootstrapping");
		
		testLogVerboseLevel();

		Injector injector = null;

		Module[] modules = getModules();
		injector = Guice.createInjector(modules);
		log.info("Guice injector created");

		return injector;
	}

	protected Module[] getModules() {

		Module[] modules = new Module[] { new QqgbvpnServletModule()
		// JPA module
		};

		return modules;
	}
	
	
	protected void testLogVerboseLevel() {
		System.out.println("Testing java logging verbose level");
		log.error("ERROR message (testing)");
		log.warn("WARN  message (testing)");
		log.info("INFO  message (testing)");
		log.debug("DEBUG message (testing)");
		log.trace("TRACE message (testing)");
	}
	
}
