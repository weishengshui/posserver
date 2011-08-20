/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.guice;

import com.chinarewards.common.web.appinfo.AppInfoServlet;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

/**
 * 
 * 
 * @author cyril
 * @since 0.1.0
 */
public class QqgbvpnServletModule extends ServletModule {

	protected void configureServlets() {

		// appinfo servlet
		bind(AppInfoServlet.class).in(Singleton.class);
		serve("/appinfo").with(AppInfoServlet.class);

	}

}
