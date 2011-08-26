package com.chinarewards.qqgbvpn.main.dao.qqapi.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.Validation;
import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.domain.event.Journal;
import com.chinarewards.qqgbvpn.domain.status.ValidationStatus;
import com.chinarewards.qqgbvpn.main.dao.qqapi.GroupBuyingDao;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingSearchListVO;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class GroupBuyingDaoImpl implements GroupBuyingDao {

	Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
	private Provider<EntityManager> em;

	public void handleGroupBuyingSearch(HashMap<String, Object> params) throws Exception {
		Journal journal = new Journal();
		journal.setTs(new Date());
		journal.setEntity(DomainEntity.GROUPON_INFORMATION.toString());
		journal.setEntityId((String)params.get("posId"));
		journal.setEvent(DomainEvent.POS_PRODUCT_SEARCH.toString());
		String resultCode = (String) params.get("resultCode");
		ObjectMapper mapper = new ObjectMapper();
		if ("0".equals(resultCode) && params.get("items") != null) {
			journal.setEventDetail(mapper.writeValueAsString((List<GroupBuyingSearchListVO>) params.get("items")));
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
			em.get().getTransaction().begin();
			saveJournal(journal);
			em.get().getTransaction().commit();
		} catch (Exception e) {
			log.error("group buying search save journal error");
			log.error("ts: " + journal.getTs());
			log.error("entity: " + journal.getEntity());
			log.error("entityId: " + journal.getEntityId());
			log.error("event: " + journal.getEvent());
			log.error("eventDetail: " + journal.getEventDetail());
		}
	}
	
	public void handleGroupBuyingValidate(HashMap<String, Object> params) throws Exception {
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
				em.get().getTransaction().begin();
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
				em.get().getTransaction().commit();
			} catch (Exception e) {
				log.error("group buying validate save error");
				log.error("posId: " + posId);
				log.error("ts: " + date);
				log.error("token: " + token);
				log.error("grouponId" + grouponId);
				log.error("entity: " + journal.getEntity());
				log.error("entityId: " + journal.getEntityId());
				log.error("event: " + journal.getEvent());
				log.error("eventDetail: " + journal.getEventDetail());
			}
		} else {
			log.error("group buying validate get pos or agent error");
			log.error("pos or agent not found by posId : " + posId);
		}
	}
	
	public void handleGroupBuyingUnbind() throws Exception {
		if (em.get() != null) {
			System.out.println("注入成功");
		} else {
			System.out.println("注入不成功");
		}
		Journal j = new Journal();
		j.setTs(new Date());
		j.setEntity(DomainEntity.VALIDATION.toString());
		j.setEntityId("Validation_Id");
		j.setEvent(DomainEvent.POS_ORDER_VALIDATED_OK.toString());
		ObjectMapper mapper = new ObjectMapper();
		j.setEventDetail(mapper.writeValueAsString(j));
		em.get().getTransaction().begin();
		em.get().persist(j);
		em.get().getTransaction().commit();
	}
	
	private void saveJournal(Journal journal) {
		em.get().persist(journal);
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

}
