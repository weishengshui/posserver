package com.chinarewards.qqgbvpn.mgmtui.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;

import com.chinarewards.qqgbvpn.domain.PosAssignment;
import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.domain.event.Journal;
import com.chinarewards.qqgbvpn.mgmtui.dao.GroupBuyingUnbindDao;
import com.chinarewards.qqgbvpn.mgmtui.exception.SaveDBException;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingUnbindVO;

public class GroupBuyingUnbindDaoImpl extends BaseDaoImpl implements GroupBuyingUnbindDao {

	public void handleGroupBuyingUnbind(HashMap<String, Object> params) throws SaveDBException, JsonGenerationException {
		String[] posIds = (String[]) params.get("posId");
		String resultCode = (String) params.get("resultCode");
		List<GroupBuyingUnbindVO> items = (List<GroupBuyingUnbindVO>) params.get("items");
		Date data = new Date();
		String resultStatus = "0";
		//响应成功
		if ("0".equals(resultCode)) {
			for (String posId : posIds) {
				resultStatus = this.getResultStatusByPosIdForUnbind(items, posId);
				//resultStatus取消状态，0代表成功
				if ("0".equals(resultStatus)) {
					PosAssignment pa = getPosAssignmentByIdPosId(posId);
					if (pa != null) {
						Journal journal = new Journal();
						journal.setTs(data);
						journal.setEntity(DomainEntity.UNBIND_POS_ASSIGNMENT.toString());
						journal.setEntityId(pa.getId());
						journal.setEvent(DomainEvent.POS_UNBIND_SUCCESS.toString());
						ObjectMapper mapper = new ObjectMapper();
						if (items != null) {
							try {
								journal.setEventDetail(mapper.writeValueAsString(pa));
							} catch (Exception e) {
								throw new JsonGenerationException(e);
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
						Journal journal = new Journal();
						journal.setTs(data);
						journal.setEntity(DomainEntity.UNBIND_POS_ASSIGNMENT.toString());
						journal.setEntityId(posId);
						journal.setEvent(DomainEvent.POS_UNBIND_FAILED.toString());
						journal.setEventDetail("group buying unbind error,pos assignment not found by posId : " + posId);
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
							log.error("group buying unbind save journal error");
							log.error("posId: " + posId);
							log.error("ts: " + journal.getTs());
							log.error("entity: " + journal.getEntity());
							log.error("entityId: " + journal.getEntityId());
							log.error("event: " + journal.getEvent());
							log.error("eventDetail: " + journal.getEventDetail());
							throw new SaveDBException(e);
						}
					}
				} else {
					//resultStatus取消状态，非0代表不成功，直接写失败日志
					Journal journal = new Journal();
					journal.setTs(data);
					journal.setEntity(DomainEntity.UNBIND_POS_ASSIGNMENT.toString());
					journal.setEntityId(posId);
					journal.setEvent(DomainEvent.POS_UNBIND_FAILED.toString());
					journal.setEventDetail(resultStatus);
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
						log.error("group buying unbind save journal error");
						log.error("posId: " + posId);
						log.error("ts: " + journal.getTs());
						log.error("entity: " + journal.getEntity());
						log.error("entityId: " + journal.getEntityId());
						log.error("event: " + journal.getEvent());
						log.error("eventDetail: " + journal.getEventDetail());
						throw new SaveDBException(e);
					}
				}
			}
		} else {
			//resultCode不等于0说明QQ响应失败，直接写错误日志
			Journal journal = new Journal();
			journal.setTs(data);
			journal.setEntity(DomainEntity.UNBIND_POS_ASSIGNMENT.toString());
			journal.setEntityId(posIds.toString());
			journal.setEvent(DomainEvent.POS_UNBIND_FAILED.toString());
			journal.setEventDetail(resultCode);
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
				log.error("group buying unbind save journal error");
				log.error("posIds: " + StringUtils.join(posIds, ","));
				log.error("ts: " + journal.getTs());
				log.error("entity: " + journal.getEntity());
				log.error("entityId: " + journal.getEntityId());
				log.error("event: " + journal.getEvent());
				log.error("eventDetail: " + journal.getEventDetail());
				throw new SaveDBException(e);
			}
		}
	}
	
	private void saveJournal(Journal journal) {
		em.get().persist(journal);
	}
	
	private PosAssignment getPosAssignmentByIdPosId(String posId) {
		try {
			Query jql = em.get().createQuery("select pa from PosAssignment pa,Pos p where pa.pos.id = p.id and p.posId = ?1");
			jql.setParameter(1, posId);
			List resultList = jql.getResultList();
			PosAssignment pa = null;
			if (resultList != null) {
				pa = (PosAssignment) resultList.get(0);
			}
			return pa;
		} catch (Exception e) {
			return null;
		}
	}
	
	private String getResultStatusByPosIdForUnbind(List<GroupBuyingUnbindVO> items, String posId) {
		String resultStatus = "-1";
		if (posId != null && items != null && items.size() > 0) {
			for (GroupBuyingUnbindVO item : items) {
				if (posId.equals(item.getPosId())) {
					resultStatus = item.getResultStatus();
					break;
				}
			}
		}
		return resultStatus;
	}
	
}
