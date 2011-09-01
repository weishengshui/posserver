/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.struts.action.util;

import com.chinarewards.qqgbvpn.mgmtui.struts.BaseAction;

/**
 * This action prepares the menu.
 * 
 * @author cyril
 * @since 0.1.0 2010-04-01
 */
public class MenuBarAction extends BaseAction {

	private static final long serialVersionUID = 8650264436450536058L;

	/**
	 * Current implementation always return
	 * com.opensymphony.xwork2.Action#SUCCESS.
	 */
	@Override
	public String execute() throws Exception {

		log.trace("execute() invoked");

		return SUCCESS;

	}

}
