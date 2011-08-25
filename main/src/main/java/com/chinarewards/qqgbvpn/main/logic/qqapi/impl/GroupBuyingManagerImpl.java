package com.chinarewards.qqgbvpn.main.logic.qqapi.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.domain.event.Journal;
import com.chinarewards.qqgbvpn.main.dao.qqapi.GroupBuyingDao;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.qqapi.service.GroupBuyingService;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingSearchListVO;
import com.google.inject.Inject;

public class GroupBuyingManagerImpl implements GroupBuyingManager {
	
	private GroupBuyingService service;
	private GroupBuyingDao dao;
	  
    @Inject   
    public GroupBuyingManagerImpl(GroupBuyingService service, GroupBuyingDao dao) {   
        this.service = service;   
        this.dao = dao;
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
		Journal journal = new Journal();
		journal.setTs(new Date());
		journal.setEntity(DomainEntity.GROUPON_INFORMATION.toString());
		journal.setEntityId(params.get("posId"));
		journal.setEvent(DomainEvent.POS_PRODUCT_SEARCH.toString());
		
		ObjectMapper mapper = new ObjectMapper();
		journal.setEventDetail(mapper.writeValueAsString((List<GroupBuyingSearchListVO>) map.get("items")));
		
		dao.handleGroupBuyingSearch(journal);
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
		HashMap<String, Object> map = service.groupBuyingValidate(params);
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
		HashMap<String, Object> map = service.groupBuyingUnbind(params);
		return map;
	}
    
}
