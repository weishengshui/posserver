/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.status.helloworld;

import com.chinarewards.qqgbvpn.mgmtui.struts.BaseAction;

/**
 * Helloworld action.
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class HelloAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1831397240110974022L;

	@Override
	public String execute() throws Exception {

		log.info("HelloAction.execute() invoked");

		return SUCCESS;
	}

}
