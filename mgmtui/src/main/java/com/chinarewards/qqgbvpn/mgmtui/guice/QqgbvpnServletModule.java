/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.guice;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

import com.chinarewards.common.web.appinfo.AppInfoServlet;
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
		// appinfo servlet
		bind(AppInfoServlet.class).in(Singleton.class);
		serve("/appinfo").with(AppInfoServlet.class);
		// Struts 2 setup
        bind(StrutsPrepareAndExecuteFilter.class).in(Singleton.class);
        filter("/*").through(StrutsPrepareAndExecuteFilter.class);
		
		//sitemesh
        //没有起作用，修改成struts tiles或者页面采用iframe框架
        bind(SiteMeshFilter.class).in(Singleton.class);
		filter("/*").through(SiteMeshFilter.class);
	}

}
