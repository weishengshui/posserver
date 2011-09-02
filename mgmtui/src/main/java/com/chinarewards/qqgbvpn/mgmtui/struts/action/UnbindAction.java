package com.chinarewards.qqgbvpn.mgmtui.struts.action;

import com.chinarewards.qqgbvpn.mgmtui.logic.GroupBuyingUnbindManager;
import com.chinarewards.qqgbvpn.mgmtui.struts.BaseAction;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * pos unbind action
 * 
 * @author iori
 *
 */
public class UnbindAction extends BaseAction {

	private static final long serialVersionUID = -4872248136823406437L;
	
	//@Inject
	//private Provider<GroupBuyingUnbindManager> groupBuyingUnbindMgr;

	@Override
	public String execute(){
		return SUCCESS;
	}
	
	public String unbind(){
		return SUCCESS;
	}
	
	public String sendURL() {
		return SUCCESS;
	}

}
