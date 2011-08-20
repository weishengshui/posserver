/**
 * 
 */
package com.chinarewards.qqgbvpn.main;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The application. Write your code here.
 * 
 * @author cyril
 * @since 1.0.0
 */
public class Application {

	// dependency
	final AppPreference appPreference;

	/**
	 * Initial value should be <code>false</code>. If the value is turned to
	 * <code>true</code>, this instance will start to terminate.
	 */
	boolean stop;

	Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	public Application(AppPreference appPreference) {
		this.appPreference = appPreference;
	}

	/**
	 * The real main routine starts here.
	 */
	public void run() {

		// write you application here.

		stop = false;

		log.info("Application done");
	}

	public void requestShutdown() {
		this.stop = true;
	}
}
