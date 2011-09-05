package com.chinarewards.qqgbvpn.main.logic.qqapi.impl;

import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.main.dao.qqapi.GroupBuyingDao;
import com.chinarewards.qqgbvpn.main.exception.CopyPropertiesException;
import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;
import com.chinarewards.qqgbvpn.qqapi.service.GroupBuyingService;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class GroupBuyingManagerImpl implements GroupBuyingManager {
	
	@Inject   
	private Provider<GroupBuyingService> service;
	
	@Inject
	private Provider<GroupBuyingDao> dao;
	
	
	/**
	 * 初始化团购商品缓存
	 * 
	 * @author iori
	 * @param params
	 * @return
	 * @throws CopyPropertiesException 
	 */
	public String initGrouponCache(HashMap<String, String> params)
			throws MD5Exception, ParseXMLException, SendPostTimeOutException,
			JsonGenerationException, SaveDBException, CopyPropertiesException {
		HashMap<String, Object> map = service.get().groupBuyingSearch(params);
		map.putAll(params);
		dao.get().initGrouponCache(map);
		return (String) map.get("resultCode");
	}

	  
    /**
	 * 团购查询
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
	public HashMap<String, Object> groupBuyingSearch(
			HashMap<String, String> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException, JsonGenerationException, SaveDBException {
		HashMap<String, Object> result = dao.get().handleGroupBuyingSearch(params);
		return result;
	}

	/**
	 * 团购验证
	 * 
	 * @author iori
	 * @param params
	 * @return
	 * @throws SendPostTimeOutException 
	 * @throws ParseXMLException 
	 * @throws MD5Exception 
	 * @throws SaveDBException 
	 */
	public HashMap<String, Object> groupBuyingValidate(
			HashMap<String, String> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException, SaveDBException {
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
	 * @throws SendPostTimeOutException 
	 * @throws ParseXMLException 
	 * @throws MD5Exception 
	 * @throws SaveDBException 
	 * @throws JsonGenerationException 
	 */
	/*public HashMap<String, Object> groupBuyingUnbind(
			HashMap<String, Object> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException, JsonGenerationException, SaveDBException {
		HashMap<String, Object> map = service.get().groupBuyingUnbind(params);
		map.putAll(params);
		dao.get().handleGroupBuyingUnbind(map);
		return map;
	}*/
    
}
