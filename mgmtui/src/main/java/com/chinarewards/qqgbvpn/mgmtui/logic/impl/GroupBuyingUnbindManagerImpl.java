package com.chinarewards.qqgbvpn.mgmtui.logic.impl;

import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.mgmtui.dao.GroupBuyingUnbindDao;
import com.chinarewards.qqgbvpn.mgmtui.exception.SaveDBException;
import com.chinarewards.qqgbvpn.mgmtui.logic.GroupBuyingUnbindManager;
import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;
import com.chinarewards.qqgbvpn.qqapi.service.GroupBuyingService;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class GroupBuyingUnbindManagerImpl implements GroupBuyingUnbindManager {
	
	@Inject   
	private Provider<GroupBuyingService> service;
	
	@Inject
	private Provider<GroupBuyingUnbindDao> dao;
	
	
	/**
	 * 团购取消绑定
	 * 
	 * @author iori
	 * @param params
	 * @return
	 * @throws SendPostTimeOutException 
	 * @throws ParseXMLException 
	 * @throws MD5Exception 
	 * @throws SaveDBException 
	 * @throws JsonGenerationException 
	 */
	public HashMap<String, Object> groupBuyingUnbind(
			HashMap<String, Object> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException, JsonGenerationException, SaveDBException {
		HashMap<String, Object> map = service.get().groupBuyingUnbind(params);
		map.putAll(params);
		dao.get().handleGroupBuyingUnbind(map);
		return map;
	}
    
}
