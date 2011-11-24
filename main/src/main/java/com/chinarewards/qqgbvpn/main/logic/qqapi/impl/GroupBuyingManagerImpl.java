package com.chinarewards.qqgbvpn.main.logic.qqapi.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.DateTimeProvider;
import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.GrouponCache;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.Validation;
import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.domain.status.CommunicationStatus;
import com.chinarewards.qqgbvpn.domain.status.ValidationStatus;
import com.chinarewards.qqgbvpn.logic.journal.JournalLogic;
import com.chinarewards.qqgbvpn.main.dao.qqapi.GroupBuyingDao;
import com.chinarewards.qqgbvpn.main.exception.CopyPropertiesException;
import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;
import com.chinarewards.qqgbvpn.qqapi.service.GroupBuyingService;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingSearchListVO;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingValidateResultVO;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

public class GroupBuyingManagerImpl implements GroupBuyingManager {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject   
	private Provider<GroupBuyingService> service;
	
	@Inject
	private Provider<GroupBuyingDao> dao;
	
	@Inject
	private JournalLogic journalLogic;
	
	@Inject
	private DateTimeProvider dtProvider;
	
	
	/**
	 * 初始化团购商品缓存
	 * 
	 * @author iori
	 * @param params
	 * @return
	 * @throws CopyPropertiesException 
	 */
	@Transactional
	public String initGrouponCache(HashMap<String, String> params)
			throws MD5Exception, ParseXMLException, SendPostTimeOutException,
			JsonGenerationException, SaveDBException, CopyPropertiesException {
		//调用腾讯的接口查询商品列表
		HashMap<String, Object> map = service.get().groupBuyingSearch(params);
		map.putAll(params);
		//处理缓存
		String posId = (String)map.get("posId");
		Date date = dtProvider.getTime();
		ObjectMapper mapper = new ObjectMapper();
		String resultCode = (String) map.get("resultCode");
		//商品缓存列表
		List<GrouponCache> oldCache = new ArrayList<GrouponCache>();
		List<GroupBuyingSearchListVO> items = (List<GroupBuyingSearchListVO>) map.get("items");
		//新商品列表
		List<GrouponCache> grouponCacheList = new ArrayList<GrouponCache>();
		if (items != null && items.size() > 0) {
			for (GroupBuyingSearchListVO vo : items) {
				GrouponCache grouponCache = new GrouponCache();
				grouponCache.setPosId(posId);
				grouponCache.setCreateDate(date);
				grouponCache.setResultCode(resultCode);
				try {
					BeanUtils.copyProperties(grouponCache, vo);
				} catch (Exception e) {
					throw new CopyPropertiesException(e);
				}
				grouponCacheList.add(grouponCache);
			}
		}
		
		String initEventDetail = "";
		try {
			if (grouponCacheList != null && grouponCacheList.size() > 0) {
				initEventDetail = mapper.writeValueAsString(grouponCacheList);
			}
		} catch (Exception e) {
			throw new JsonGenerationException(e);
		}
		
		String delEventDetail = "";
		try {
			//删除缓存
			oldCache = dao.get().deleteGrouponCache(posId);
			try {
				if (oldCache != null && oldCache.size() > 0) {
					delEventDetail = mapper.writeValueAsString(oldCache);
				}
			} catch (Exception e) {
				throw new JsonGenerationException(e);
			}
			//保存删除缓存日志
			journalLogic.logEvent(DomainEvent.GROUPON_CACHE_DELETE.toString(),
					DomainEntity.GROUPON_CACHE.toString(), posId, delEventDetail);
			if ("0".equals(resultCode)) {
				//保存商品缓存
				if (grouponCacheList != null && grouponCacheList.size() > 0) {
					for (GrouponCache vo : grouponCacheList) {
						dao.get().saveGrouponCache(vo);
					}
				}
			} else {
				//如果result code不为0，保存一个只有posId和result code的缓存记录
				//用于查询时获取result code
				GrouponCache errorCache = new GrouponCache();
				errorCache.setPosId(posId);
				errorCache.setCreateDate(date);
				errorCache.setResultCode(resultCode);
				dao.get().saveGrouponCache(errorCache);
				
				//当result code不为0时，日志的detail中记录的是result code
				initEventDetail = resultCode;
			}
			//保存商品日志
			journalLogic.logEvent(DomainEvent.GROUPON_CACHE_INIT.toString(),
					DomainEntity.GROUPON_CACHE.toString(), posId, initEventDetail);
		} catch (Exception e) {
			//记日志
			log.error("groupon cache init error");
			log.error("ts: " + date);
			log.error("old groupon cache information:");
			log.error("entity: " + DomainEntity.GROUPON_CACHE.toString());
			log.error("entityId: " + posId);
			log.error("event: " + DomainEvent.GROUPON_CACHE_DELETE.toString());
			log.error("eventDetail: " + delEventDetail);
			log.error("new groupon information:");
			log.error("entity: " + DomainEntity.GROUPON_CACHE.toString());
			log.error("entityId: " + posId);
			log.error("event: " + DomainEvent.GROUPON_CACHE_INIT.toString());
			log.error("eventDetail: " + initEventDetail);
			throw new SaveDBException(e);
		}
		
		return resultCode;
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
	@Transactional
	public HashMap<String, Object> groupBuyingSearch(
			HashMap<String, String> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException, JsonGenerationException, SaveDBException {
		Date date = dtProvider.getTime();
		HashMap<String, Object> relustMap = new HashMap<String, Object>();
		
		String posId = params.get("posId");
		
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageId(Integer.valueOf(params.get("curpage")));
		pageInfo.setPageSize(Integer.valueOf(params.get("pageSize")));
		
		//分页查询商品
		pageInfo = dao.get().getGrouponCachePagination(pageInfo,posId);
		
		//缓存商品列表
		List<GrouponCache> cacheList = (List<GrouponCache>) pageInfo.getItems();
		
		//获取result code
		String resultCode = getResultCode(cacheList);
		
		ObjectMapper mapper = new ObjectMapper();
		String eventDetail = "";
		if (cacheList != null && cacheList.size() > 0) {
			try {
				eventDetail = mapper.writeValueAsString(cacheList);
			} catch (Exception e) {
				throw new JsonGenerationException(e);
			}
		}
		
		try {
			//记查询日志
			journalLogic.logEvent(DomainEvent.POS_PRODUCT_SEARCH.toString(),
					DomainEntity.GROUPON_CACHE.toString(), posId, eventDetail);
		} catch (Exception e) {
			log.error("group buying search save journal error");
			log.error("ts: " + date);
			log.error("entity: " + DomainEntity.GROUPON_CACHE.toString());
			log.error("entityId: " + posId);
			log.error("event: " + DomainEvent.POS_PRODUCT_SEARCH.toString());
			log.error("eventDetail: " + eventDetail);
			throw new SaveDBException(e);
		}
		relustMap.put("resultCode", resultCode);
		relustMap.put("pageInfo", pageInfo);
		return relustMap;
	}
	
	/**
	 * 根据缓存商品列表取result code,默认为0
	 * @author iori
	 * @param posId
	 * @return
	 */
	private String getResultCode(List<GrouponCache> cacheList) {
		String resultCode = "0";
		if (cacheList != null && cacheList.size() > 0) {
			GrouponCache cache = cacheList.get(0);
			resultCode = cache.getResultCode();
		}
		return resultCode;
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
	@Transactional
	public HashMap<String, Object> groupBuyingValidate(
			HashMap<String, String> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException, SaveDBException {
		//调用腾讯接口验证团购
		HashMap<String, Object> map = service.get().groupBuyingValidate(params);
		map.putAll(params);
		//处理后台验证结果
		String posId = (String) map.get("posId");
		String resultCode = (String) map.get("resultCode");
		String token = (String) map.get("token");
		String grouponId = (String) map.get("grouponId");
		//查询POS机
		Pos pos = dao.get().getPosByPosId(posId);
		List<GroupBuyingValidateResultVO> items = (List<GroupBuyingValidateResultVO>) map.get("items");
		String resultStatus = "";
		String resultName = "";
		String resultExplain = "";
		String currentTime = "";
		String useTime = "";
		String validTime = "";
		String refundTime = "";
		if (items != null && items.size() > 0) {
			GroupBuyingValidateResultVO item = items.get(0);
			resultStatus = item.getResultStatus();
			resultName = item.getResultName();
			resultExplain = item.getResultExplain();
			currentTime = item.getCurrentTime();
			useTime = item.getUseTime();
			validTime = item.getValidTime();
			refundTime = item.getRefundTime();
		}
		//查询第三方
		Agent agent = dao.get().getAgentByPosId(posId);
		//保证验证的POS存在，并且绑定了第三方
		if (pos != null && agent != null) {
			Validation validation = new Validation();
			Date date = dtProvider.getTime();
			String event = "0".equals(resultCode) ? DomainEvent.POS_ORDER_VALIDATED_OK.toString() : DomainEvent.POS_ORDER_VALIDATED_FAILED.toString();
			String eventDetail = "";
			try {
				validation.setTs(date);
				validation.setVcode(token);
				validation.setPcode(grouponId);
				validation.setPosId(pos.getPosId());
				validation.setPosModel(pos.getModel());
				validation.setPosSimPhoneNo(pos.getSimPhoneNo());
				validation.setStatus("0".equals(resultStatus) ? ValidationStatus.SUCCESS : ValidationStatus.FAILED);
				validation.setCstatus("0".equals(resultCode) ? CommunicationStatus.SUCCESS : CommunicationStatus.FAILED);
				validation.setResultStatus(resultStatus);
				validation.setResultName(resultName);
				validation.setResultExplain(resultExplain);
				validation.setCurrentTime(currentTime);
				validation.setUseTime(useTime);
				validation.setValidTime(validTime);
				validation.setRefundTime(refundTime);
				validation.setAgentId(agent.getId());
				validation.setAgentName(agent.getName());
				//保存验证信息
				dao.get().saveValidation(validation);
				
				ObjectMapper mapper = new ObjectMapper();
				if ("0".equals(resultCode) && map.get("items") != null) {
					eventDetail = mapper.writeValueAsString(validation);
				} else {
					eventDetail = resultCode;
				}
				//记录保存日志
				journalLogic.logEvent(event, DomainEntity.VALIDATION.toString(), posId, eventDetail);
			} catch (Exception e) {
				log.error("group buying validate save error");
				log.error("posId: " + posId);
				log.error("ts: " + date);
				log.error("token: " + token);
				log.error("grouponId" + grouponId);
				log.error("entity: " + DomainEntity.VALIDATION.toString());
				log.error("entityId: " + validation.getId());
				log.error("event: " + event);
				log.error("eventDetail: " + eventDetail);
				throw new SaveDBException(e);
			}
		} else {
			log.error("group buying validate get pos or agent error");
			log.error("pos or agent not found by posId : " + posId);
			throw new SaveDBException("group buying validate error,pos or agent not found by posId : " + posId);
		}
		
		return map;
	}


	@Override
	public GroupBuyingValidateResultVO groupBuyingValidateLocal(String grouponId,
			String grouponVCode)throws SaveDBException,JsonGenerationException{
		return dao.get().groupBuyingValidateLocal(grouponId, grouponVCode);
	}


	@Override
	public void createValidateResultLocal(String grouponId,String grouponVCode,
			GroupBuyingValidateResultVO groupBuyingValidateResultVO)
			throws SaveDBException,JsonGenerationException{
		dao.get().createValidateResultLocal(grouponId, grouponVCode, groupBuyingValidateResultVO);
	}


	@Override
	public void groupBuyValidateCallBack(String grouponId, String grouponVCode)
			throws SaveDBException {
		dao.get().groupBuyValidateCallBack(grouponId, grouponVCode);
	}

}
