/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.guice;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

import com.chinarewards.qqgbvpn.mgmtui.filter.DisableUrlSessionFilter;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.opensymphony.sitemesh.webapp.SiteMeshFilter;

/**
 * 
 * 
 * @author cyril
 * @since 0.1.0
 */
public class QqgbvpnServletModule extends ServletModule {

	protected void configureServlets() {
		bind(DisableUrlSessionFilter.class).in(Singleton.class);

		// Struts 2 setup
		bind(StrutsPrepareAndExecuteFilter.class).in(Singleton.class);

		// 没有起作用，已改成struts tiles
//		bind(SiteMeshFilter.class).in(Singleton.class);

		filter("/*").through(DisableUrlSessionFilter.class);
//		filter("/*").through(SiteMeshFilter.class);
		filter("/*").through(StrutsPrepareAndExecuteFilter.class);
	}

}
