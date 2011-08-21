package com.chinarewards.qqgbvpn.main.logic.qqapi.impl;

import java.util.HashMap;

import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.qqapi.service.GroupBuyingService;
import com.google.inject.Inject;

public class GroupBuyingManagerImpl implements GroupBuyingManager {
	
	private GroupBuyingService service;  
	  
    @Inject   
    public GroupBuyingManagerImpl(GroupBuyingService service) {   
        this.service = service;   
    }
    
    /**
	 * 团购查询
	 * 
	 * @author iori
	 * @param params
	 * @return
	 */
	public HashMap<String, Object> groupBuyingSearch(
			HashMap<String, String> params) throws Exception {
		HashMap<String, Object> map = service.groupBuyingSearch(params);
		return null;
	}

	/**
	 * 团购验证
	 * 
	 * @author iori
	 * @param params
	 * @return
	 */
	public HashMap<String, Object> groupBuyingValidate(
			HashMap<String, String> params) throws Exception {
		HashMap<String, Object> map = service.groupBuyingValidate(params);
		return null;
	}

	/**
	 * 团购取消绑定
	 * 
	 * @author iori
	 * @param params
	 * @return
	 */
	public HashMap<String, Object> groupBuyingUnbind(
			HashMap<String, Object> params) throws Exception {
		HashMap<String, Object> map = service.groupBuyingUnbind(params);
		return null;
	}
    
}
