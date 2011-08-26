package com.chinarewards.qqgbvpn.main.logic.qqapi.impl;

import java.util.HashMap;

import com.chinarewards.qqgbvpn.main.dao.qqapi.GroupBuyingDao;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.qqapi.service.GroupBuyingService;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class GroupBuyingManagerImpl implements GroupBuyingManager {
	
	@Inject   
	private Provider<GroupBuyingService> service;
	
	@Inject
	private Provider<GroupBuyingDao> dao;
	  
    /**
	 * 团购查询
	 * 
	 * @author iori
	 * @param params
	 * @return
	 */
	public HashMap<String, Object> groupBuyingSearch(
			HashMap<String, String> params) throws Exception {
		HashMap<String, Object> map = service.get().groupBuyingSearch(params);
		map.putAll(params);
		dao.get().handleGroupBuyingSearch(map);
		return map;
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
		HashMap<String, Object> map = service.get().groupBuyingValidate(params);
		map.putAll(params);
		dao.get().handleGroupBuyingValidate(map);
		return map;
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
		HashMap<String, Object> map = service.get().groupBuyingUnbind(params);
		map.putAll(params);
		dao.get().handleGroupBuyingUnbind(map);
		return map;
	}
    
}
