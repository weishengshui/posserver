/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.struts;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public abstract class BaseAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4043054562328091468L;

	protected Logger log = LoggerFactory.getLogger(getClass());

	public Injector getInjector() {
		return (Injector) ServletActionContext.getServletContext()
				.getAttribute(Injector.class.getName());
	}
}
