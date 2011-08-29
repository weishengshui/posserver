package com.chinarewards.qqgbvpn.main.dao.qqapi.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.beanutils.BeanUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;

import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.GrouponCache;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.PosAssignment;
import com.chinarewards.qqgbvpn.domain.Validation;
import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.domain.event.Journal;
import com.chinarewards.qqgbvpn.domain.status.ValidationStatus;
import com.chinarewards.qqgbvpn.main.dao.qqapi.GroupBuyingDao;
import com.chinarewards.qqgbvpn.main.exception.CopyPropertiesException;
import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingSearchListVO;

public class GroupBuyingDaoImpl extends BaseDaoImpl implements GroupBuyingDao {

	public void initGrouponCache(HashMap<String, Object> params) throws SaveDBException, JsonGenerationException, CopyPropertiesException {
		String posId = (String)params.get("posId");
		Date date = new Date();
		ObjectMapper mapper = new ObjectMapper();
		String resultCode = (String) params.get("resultCode");
		//商品缓存列表
		List<GrouponCache> oldCache = new ArrayList<GrouponCache>();
		List<GroupBuyingSearchListVO> items = (List<GroupBuyingSearchListVO>) params.get("items");
		//新商品列表
		List<GrouponCache> grouponCacheList = new ArrayList<GrouponCache>();
		if (items != null && items.size() > 0) {
			for (GroupBuyingSearchListVO vo : items) {
				GrouponCache grouponCache = new GrouponCache();
				grouponCache.setPosId(posId);
				grouponCache.setCreateDate(date);
				try {
					BeanUtils.copyProperties(grouponCache, vo);
				} catch (Exception e) {
					throw new CopyPropertiesException(e);
				}
				grouponCacheList.add(grouponCache);
			}
		}
		
		//商品日志对象
		Journal journal = new Journal();
		journal.setTs(date);
		journal.setEntity(DomainEntity.GROUPON_CACHE.toString());
		journal.setEntityId(posId);
		journal.setEvent(DomainEvent.GROUPON_CACHE_INIT.toString());
		try {
			if (grouponCacheList != null && grouponCacheList.size() > 0) {
				journal.setEventDetail(mapper.writeValueAsString(grouponCacheList));
			}
		} catch (Exception e) {
			throw new JsonGenerationException(e);
		}
		
		//删除缓存日志对象
		Journal oldCacheJournal = new Journal();
		oldCacheJournal.setTs(date);
		oldCacheJournal.setEntity(DomainEntity.GROUPON_CACHE.toString());
		oldCacheJournal.setEntityId(posId);
		oldCacheJournal.setEvent(DomainEvent.GROUPON_CACHE_DELETE.toString());
		
		if (!em.get().getTransaction().isActive()) {
			em.get().getTransaction().begin();
		}
		try {
			if ("0".equals(resultCode)) {
				//删除缓存
				oldCache = deleteGrouponCache(posId);
				try {
					if (oldCache != null && oldCache.size() > 0) {
						oldCacheJournal.setEventDetail(mapper.writeValueAsString(oldCache));
					}
				} catch (Exception e) {
					throw new JsonGenerationException(e);
				}
				//保存删除缓存日志
				saveJournal(oldCacheJournal);
				//保存商品
				if (grouponCacheList != null && grouponCacheList.size() > 0) {
					for (GrouponCache vo : grouponCacheList) {
						saveGrouponCache(vo);
					}
				}
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
			//保存商品日志
			saveJournal(journal);
			if (em.get().getTransaction().isActive()) {
				em.get().getTransaction().commit();
			}
		} catch (Exception e) {
			if (em.get().getTransaction().isActive()) {
				em.get().getTransaction().rollback();
			}
			//记日志
			log.error("groupon cache init error");
			log.error("ts: " + date);
			log.error("old groupon cache information:");
			log.error("entity: " + oldCacheJournal.getEntity());
			log.error("entityId: " + oldCacheJournal.getEntityId());
			log.error("event: " + oldCacheJournal.getEvent());
			log.error("eventDetail: " + oldCacheJournal.getEventDetail());
			log.error("new groupon information:");
			log.error("entity: " + journal.getEntity());
			log.error("entityId: " + journal.getEntityId());
			log.error("event: " + journal.getEvent());
			log.error("eventDetail: " + journal.getEventDetail());
			throw new SaveDBException(e);
		}
	}

	public PageInfo handleGroupBuyingSearch(HashMap<String, String> params) throws SaveDBException, JsonGenerationException {
		String posId = params.get("posId");
		
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageId(Integer.valueOf(params.get("curpage")));
		pageInfo.setPageSize(Integer.valueOf(params.get("pageSize")));
		//分页查询商品
		pageInfo = getGrouponCachePagination(pageInfo,posId);
		
		Journal journal = new Journal();
		journal.setTs(new Date());
		journal.setEntity(DomainEntity.GROUPON_CACHE.toString());
		journal.setEntityId(posId);
		journal.setEvent(DomainEvent.POS_PRODUCT_SEARCH.toString());
		ObjectMapper mapper = new ObjectMapper();
		if (pageInfo.getItems() != null && pageInfo.getItems().size() > 0) {
			try {
				journal.setEventDetail(mapper.writeValueAsString((List<GrouponCache>) pageInfo.getItems()));
			} catch (Exception e) {
				throw new JsonGenerationException(e);
			}
		}
		try {
			if (!em.get().getTransaction().isActive()) {
				em.get().getTransaction().begin();
			}
			saveJournal(journal);
			if (em.get().getTransaction().isActive()) {
				em.get().getTransaction().commit();
			}
		} catch (Exception e) {
			if (em.get().getTransaction().isActive()) {
				em.get().getTransaction().rollback();
			}
			log.error("group buying search save journal error");
			log.error("ts: " + journal.getTs());
			log.error("entity: " + journal.getEntity());
			log.error("entityId: " + journal.getEntityId());
			log.error("event: " + journal.getEvent());
			log.error("eventDetail: " + journal.getEventDetail());
			throw new SaveDBException(e);
		}
		
		return pageInfo;
	}
	
	public void handleGroupBuyingValidate(HashMap<String, Object> params) throws SaveDBException {
		String posId = (String) params.get("posId");
		String resultCode = (String) params.get("resultCode");
		String token = (String) params.get("token");
		String grouponId = (String) params.get("grouponId");
		Pos pos = getPosByPosId(posId);
		Agent agent = getAgentByPosId(posId);
		if (pos != null && agent != null) {
			Validation validation = new Validation();
			Journal journal = new Journal();
			Date date = new Date();
			try {
				if (!em.get().getTransaction().isActive()) {
					em.get().getTransaction().begin();
				}
				validation.setTs(date);
				validation.setVcode(token);
				validation.setPcode(grouponId);
				validation.setPosId(pos.getPosId());
				validation.setPosModel(pos.getModel());
				validation.setPosSimPhoneNo(pos.getSimPhoneNo());
				validation.setStatus("0".equals(resultCode) ? ValidationStatus.SUCCESS : ValidationStatus.FAILED);
				validation.setAgentId(agent.getId());
				validation.setAgentName(agent.getName());
				saveValidation(validation);
				
				journal.setTs(date);
				journal.setEntity(DomainEntity.VALIDATION.toString());
				journal.setEntityId(validation.getId());
				journal.setEvent("0".equals(resultCode) ? DomainEvent.POS_ORDER_VALIDATED_OK.toString() : DomainEvent.POS_ORDER_VALIDATED_FAILED.toString());
				ObjectMapper mapper = new ObjectMapper();
				if ("0".equals(resultCode) && params.get("items") != null) {
					journal.setEventDetail(mapper.writeValueAsString(validation));
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
				saveJournal(journal);
				if (em.get().getTransaction().isActive()) {
					em.get().getTransaction().commit();
				}
			} catch (Exception e) {
				if (em.get().getTransaction().isActive()) {
					em.get().getTransaction().rollback();
				}
				log.error("group buying validate save error");
				log.error("posId: " + posId);
				log.error("ts: " + date);
				log.error("token: " + token);
				log.error("grouponId" + grouponId);
				log.error("entity: " + journal.getEntity());
				log.error("entityId: " + journal.getEntityId());
				log.error("event: " + journal.getEvent());
				log.error("eventDetail: " + journal.getEventDetail());
				throw new SaveDBException(e);
			}
		} else {
			log.error("group buying validate get pos or agent error");
			log.error("pos or agent not found by posId : " + posId);
			throw new SaveDBException("group buying validate error,pos or agent not found by posId : " + posId);
		}
	}
	
	public void handleGroupBuyingUnbind(HashMap<String, Object> params) throws SaveDBException, JsonGenerationException {
		String[] posIds = (String[]) params.get("posId");
		String resultCode = (String) params.get("resultCode");
		for (String posId : posIds) {
			PosAssignment pa = getPosAssignmentByIdPosId(posId);
			if (pa != null) {
				Journal journal = new Journal();
				journal.setTs(new Date());
				journal.setEntity(DomainEntity.UNBIND_POS_ASSIGNMENT.toString());
				journal.setEntityId(pa.getId());
				journal.setEvent("0".equals(resultCode) ? DomainEvent.POS_UNBIND_SUCCESS.toString() : DomainEvent.POS_UNBIND_FAILED.toString());
				ObjectMapper mapper = new ObjectMapper();
				if ("0".equals(resultCode) && params.get("items") != null) {
					try {
						journal.setEventDetail(mapper.writeValueAsString(pa));
					} catch (Exception e) {
						throw new JsonGenerationException(e);
					}
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
					if (!em.get().getTransaction().isActive()) {
						em.get().getTransaction().begin();
					}
					if ("0".equals(resultCode)) {
						em.get().remove(pa);
					}
					saveJournal(journal);
					if (em.get().getTransaction().isActive()) {
						em.get().getTransaction().commit();
					}
				} catch (Exception e) {
					if (em.get().getTransaction().isActive()) {
						em.get().getTransaction().rollback();
					}
					log.error("group buying unbind save error");
					log.error("posId: " + posId);
					log.error("ts: " + journal.getTs());
					log.error("entity: " + journal.getEntity());
					log.error("entityId: " + journal.getEntityId());
					log.error("event: " + journal.getEvent());
					log.error("eventDetail: " + journal.getEventDetail());
					throw new SaveDBException(e);
				}
			} else {
				log.error("group buying unbind get pos assignment error");
				log.error("pos assignment not found by posId : " + posId);
				throw new SaveDBException("group buying unbind error,pos assignment not found by posId : " + posId);
			}
		}
	}
	
	private void saveJournal(Journal journal) {
		em.get().persist(journal);
	}
	
	private void saveGrouponCache(GrouponCache grouponCache) {
		em.get().persist(grouponCache);
	}
	
	private List<GrouponCache> deleteGrouponCache(String posId) {
		List<GrouponCache> list = getGrouponCacheByPosId(posId);
		if (list != null && list.size() > 0) {
			Query query = em.get().createQuery("delete from GrouponCache gc where gc.posId = ?1");
			query.setParameter(1, posId);
			query.executeUpdate();
		}
		return list;
	}
	
	public PageInfo getGrouponCachePagination(PageInfo pageInfo, String posId) {
		String sql = "select gc from GrouponCache gc where gc.posId = ?1 order by gc.id asc";
		List<Object> params = new ArrayList<Object>();
		params.add(posId);
		return this.findPageInfo(sql, params, pageInfo);
	}
	
	private List<GrouponCache> getGrouponCacheByPosId(String posId) {
		Query query = em.get().createQuery("select gc from GrouponCache gc where gc.posId = ?1");
		query.setParameter(1, posId);
		return query.getResultList() != null ? (List<GrouponCache>) query.getResultList() : null;
	}
	
	private void saveValidation(Validation validation) {
		em.get().persist(validation);
	}
	
	private Pos getPosByPosId(String posId) {
		try {
			Query jql = em.get().createQuery("select p from Pos p where p.posId = ?1");
			jql.setParameter(1, posId);
			Pos p = (Pos) jql.getSingleResult();
			return p;
		} catch (Exception e) {
			return null;
		}
	}
	
	private Agent getAgentByPosId(String posId) {
		try {
			Query jql = em.get().createQuery("select a from PosAssignment pa,Pos p,Agent a " +
					"where pa.pos.id = p.id and pa.agent.id = a.id and p.posId = ?1");
			jql.setParameter(1, posId);
			Agent a = (Agent) jql.getSingleResult();
			return a;
		} catch (Exception e) {
			return null;
		}
	}
	
	private PosAssignment getPosAssignmentByIdPosId(String posId) {
		try {
			Query jql = em.get().createQuery("select pa from PosAssignment pa,Pos p where pa.pos.id = p.id and p.posId = ?1");
			jql.setParameter(1, posId);
			PosAssignment pa = (PosAssignment) jql.getSingleResult();
			return pa;
		} catch (Exception e) {
			return null;
		}
	}
	
}
