package com.chinarewards.qqgbvpn.main.logic.qqapi.impl;

import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.main.dao.qqapi.GroupBuyingDao;
import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;
import com.chinarewards.qqgbvpn.qqapi.service.GroupBuyingService;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingSearchListVO;
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
     * @throws SendPostTimeOutException 
     * @throws ParseXMLException 
     * @throws MD5Exception 
     * @throws SaveDBException 
     * @throws JsonGenerationException 
	 */
	public HashMap<String, Object> groupBuyingSearch(
			HashMap<String, String> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException, JsonGenerationException, SaveDBException {
		HashMap<String, Object> map = service.get().groupBuyingSearch(params);
		List<GroupBuyingSearchListVO> items = (List<GroupBuyingSearchListVO>) map.get("items");
		//总页数
		Integer pageCount = 0;
		//团购总数量
		Integer totalnum = items != null ? items.size() : 0;
		//当前页的团购数量
		int pageSize = 4;
		if (params.get("curpage") != null && !"".equals(params.get("curpage").trim())) {
			String s = params.get("curpage");
			int pageId = Integer.valueOf(s).intValue();
			if (items != null && items.size() > pageSize) {
				if (items.size() % pageSize == 0) {
					pageCount = items.size() / pageSize;
				} else {
					pageCount = items.size() / pageSize + 1;
				}
				//当前页数不能大于总页数并且不能小于1
				if (!(pageId > pageCount || pageId < 1)) {
					int startIndex = (pageId - 1) * pageSize;
					int endIndex;
					if (pageId == pageCount) {
						endIndex = items.size();
					} else {
						endIndex = pageId * pageSize;
					}
					map.put("items", items.subList(startIndex, endIndex));
				}
			}
		}
		map.put("totalpage", pageCount);
		map.put("totalnum", totalnum);
		map.put("curnum", pageSize);
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
	public HashMap<String, Object> groupBuyingUnbind(
			HashMap<String, Object> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException, JsonGenerationException, SaveDBException {
		HashMap<String, Object> map = service.get().groupBuyingUnbind(params);
		map.putAll(params);
		dao.get().handleGroupBuyingUnbind(map);
		return map;
	}
    
}
