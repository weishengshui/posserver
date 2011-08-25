package com.chinarewards.qqgbvpn.main.logic.qqapi.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.domain.event.Journal;
import com.chinarewards.qqgbvpn.main.dao.qqapi.GroupBuyingDao;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.qqapi.service.GroupBuyingService;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingSearchListVO;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class GroupBuyingManagerImpl implements GroupBuyingManager {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
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
		Journal journal = new Journal();
		journal.setTs(new Date());
		journal.setEntity(DomainEntity.GROUPON_INFORMATION.toString());
		journal.setEntityId(params.get("posId"));
		journal.setEvent(DomainEvent.POS_PRODUCT_SEARCH.toString());
		String resultCode = (String) map.get("resultCode");
		ObjectMapper mapper = new ObjectMapper();
		if ("0".equals(resultCode) && map.get("items") != null) {
			journal.setEventDetail(mapper.writeValueAsString((List<GroupBuyingSearchListVO>) map.get("items")));
		} else {
			switch (Integer.valueOf(resultCode)) {
	    		case -1 :
	    			journal.setEventDetail("服务器繁忙");
	    			break;
	    		case -2 :
	    			journal.setEventDetail("md5校验失败");
	    			break;
	    		case -3 :
	    			journal.setEventDetail("没有权限");
	    			break;
	    		default :
	    			journal.setEventDetail("未知错误");
	    			break;
			}
		}
		try {
			dao.get().handleGroupBuyingSearch(journal);
		} catch (Exception e) {
			log.error("group buying search save journal error");
			log.error("ts: " + journal.getTs());
			log.error("entity: " + journal.getEntity());
			log.error("entityId: " + journal.getEntityId());
			log.error("event: " + journal.getEvent());
			log.error("eventDetail: " + journal.getEventDetail());
		}
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
		return map;
	}
    
}
