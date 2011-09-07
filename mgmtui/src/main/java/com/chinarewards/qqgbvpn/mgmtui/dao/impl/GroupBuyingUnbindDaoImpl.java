package com.chinarewards.qqgbvpn.mgmtui.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.PosAssignment;
import com.chinarewards.qqgbvpn.domain.ReturnNote;
import com.chinarewards.qqgbvpn.domain.ReturnNoteDetail;
import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.domain.event.Journal;
import com.chinarewards.qqgbvpn.domain.status.ReturnNoteStatus;
import com.chinarewards.qqgbvpn.mgmtui.dao.GroupBuyingUnbindDao;
import com.chinarewards.qqgbvpn.mgmtui.exception.SaveDBException;
import com.chinarewards.qqgbvpn.mgmtui.util.Tools;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingUnbindVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
						if (items != null) {
							try {
								// FIXME use Jackson JSON processor
								GsonBuilder builder = new GsonBuilder();
								Gson gson = builder.create();
								journal.setEventDetail(gson.toJson(pa));
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
			// XXX why mute the exception!?
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
	
	/* (non-Javadoc)
	 * @see com.chinarewards.qqgbvpn.mgmtui.dao.GroupBuyingUnbindDao#getPosByAgentId(com.chinarewards.qqgbvpn.domain.PageInfo, java.lang.String)
	 * 回收单页面查询调用
	 */
	public PageInfo getPosByAgentId(PageInfo pageInfo, String agentId) {
		try {
			String sql = "select p from Pos p, PosAssignment pa where p.id = pa.pos.id and pa.agent.id = ?1";
			List params = new ArrayList();
			params.add(agentId);
			PageInfo resultList = this.findPageInfo(sql, params, pageInfo);
			return resultList;
		} catch (Exception e) {
			// XXX why mute the exception!?!?!
			return pageInfo;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.chinarewards.qqgbvpn.mgmtui.dao.GroupBuyingUnbindDao#getAgentByName(java.lang.String)
	 * 发送URL页面查询调用
	 */
	public Agent getAgentByName(String agentName) {
		try {
			Query jql = em.get().createQuery("select a from Agent a where a.name = ?1");
			jql.setParameter(1, agentName);
			List resultList = jql.getResultList();
			log.debug("Number of agent matched name '{}': {}", agentName, resultList.size());
			Agent agent = null;
			if (resultList != null) {
				agent = (Agent) resultList.get(0);
			}
			return agent;
		} catch (Exception e) {
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.chinarewards.qqgbvpn.mgmtui.dao.GroupBuyingUnbindDao#getPosByPosInfo(java.lang.String)
	 * 解绑页面查询调用
	 */
	public List<Pos> getPosByPosInfo(String info) {
		try {
			Query jql = em.get().createQuery("select p from Pos p where p.posId = ?1 or p.sn = ?1 or p.simPhoneNo = ?1");
			jql.setParameter(1, info);
			List<Pos> resultList = jql.getResultList();
			return resultList;
		} catch (Exception e) {
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.chinarewards.qqgbvpn.mgmtui.dao.GroupBuyingUnbindDao#createReturnNoteByAgentId(java.lang.String)
	 * 生成URL时调用
	 */
	public ReturnNote createReturnNoteByAgentId(String agentId) throws JsonGenerationException,SaveDBException {
		Agent a = this.getAgentById(agentId);
		if (a != null) {
			Date date = new Date();
			ReturnNote rn = new ReturnNote();
			rn.setRnNumber(Tools.getOnlyNumber());
			rn.setAgent(a);
			rn.setAgentName(a.getName());
			rn.setStatus(ReturnNoteStatus.DRAFT);
			rn.setCreateDate(date);
			
			try {
				
				saveReturnNote(rn);
				
				Journal journal = new Journal();
				journal.setTs(date);
				journal.setEntity(DomainEntity.RETURN_NOTE.toString());
				journal.setEntityId(rn.getId());
				journal.setEvent(DomainEvent.USER_ADDED_RNOTE.toString());
				try {
					GsonBuilder builder = new GsonBuilder();
					Gson gson = builder.create();
					journal.setEventDetail(gson.toJson(rn));
				} catch (Exception e) {
					throw new JsonGenerationException(e);
				}
				saveJournal(journal);
				/*if (em.get().getTransaction().isActive()) {
					em.get().getTransaction().commit();
				}*/
			} catch (Exception e) {
				/*if (em.get().getTransaction().isActive()) {
					em.get().getTransaction().rollback();
				}*/
				throw new SaveDBException(e);
			}
			return rn;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.chinarewards.qqgbvpn.mgmtui.dao.GroupBuyingUnbindDao#confirmReturnNote(java.lang.String, java.lang.String, java.util.List)
	 * 回收单页面调用
	 */
	public ReturnNote confirmReturnNote(String agentId,String rnId, List<String> posIds) throws JsonGenerationException,SaveDBException {
		ReturnNote rn = null;
		Date date = new Date();
		Agent a = this.getAgentById(agentId);
		if (a != null) {
			log.debug("Agent({}): id={}, name={}", new Object[] { agentId, a.getId(), a.getName()} );
		} else {
			log.debug("Agent({}) not found", new Object[] { agentId } );
		}
		if (a != null) {
			if (rnId != null && !"".equals(rnId.trim())) {
				rn = this.getReturnNote(rnId);
				if (rn != null && ReturnNoteStatus.CONFIRMED.equals(rn.getStatus())) {
					log.warn("Return Note already confirmed!");
					throw new SaveDBException("Return Note already confirmed!");
				}
			}
			if (rn == null) {
				rn = new ReturnNote();
				rn.setRnNumber(Tools.getOnlyNumber());
//				Agent tmpAgent = new Agent(agentId);
//				rn.setAgent(tmpAgent);
				rn.setAgent(a);
				rn.setAgentName(a.getName());
				rn.setStatus(ReturnNoteStatus.CONFIRMED);
				rn.setCreateDate(date);
				rn.setConfirmDate(date);
			} else {
				rn.setStatus(ReturnNoteStatus.CONFIRMED);
				rn.setConfirmDate(date);
			}
			try {
				
				saveReturnNote(rn);
				
				Journal journal = new Journal();
				journal.setTs(date);
				journal.setEntity(DomainEntity.RETURN_NOTE.toString());
				journal.setEntityId(rn.getId());
				journal.setEvent(DomainEvent.USER_CONFIRMED_RNOTE.toString());
				GsonBuilder builder = new GsonBuilder();
				Gson gson = builder.create();
				journal.setEventDetail(gson.toJson(rn));
				saveJournal(journal);
				
				List<Pos> posList = getPosListByIds(posIds);
				
				if (posList != null && posList.size() > 0) {
					for (Pos p : posList) {
						ReturnNoteDetail rnd = new ReturnNoteDetail();
						rnd.setRn(rn);
						rnd.setPosId(p.getPosId());
						rnd.setModel(p.getModel());
						rnd.setSimPhoneNo(p.getSimPhoneNo());
						rnd.setSn(p.getSn());
						saveReturnNoteDetail(rnd);
					}
				}
				
			} catch (Exception e) {
				throw new SaveDBException(e);
			}
			return rn;
		}
		return null;
	}
	
	private void saveReturnNote(ReturnNote rn) {
		em.get().persist(rn);
	}
	
	private void saveReturnNoteDetail(ReturnNoteDetail rnd) {
		em.get().persist(rnd);
	}
	
	private ReturnNote getReturnNote(String rnId) {
		ReturnNote rn = em.get().find(ReturnNote.class, rnId);
		return rn;
	}
	
	private Agent getAgentById(String agentId) {
		Agent a = em.get().find(Agent.class, agentId);
		return a;
	}
	
	private List<Pos> getPosListByIds(List<String> posIds) {
		try {
			log.debug("posIds:{}", posIds);
			Query jql = em.get().createQuery("select p from Pos p where p.id in (?1)");
			jql.setParameter(1, posIds);
			List<Pos> resultList = jql.getResultList();
			return resultList;
		} catch (Exception e) {
			log.error("Transaction Exception catch!", e);
			return null;
		}
	}
	
	public Agent getAgentByRnId(String rnId) {
		try {
			Query jql = em.get().createQuery("select a from Agent a,ReturnNote rn where a.id = rn.agent.id and rn.status = '"
						+ ReturnNoteStatus.DRAFT.toString() + "' and rn.id = ?1");
			jql.setParameter(1, rnId);
			List resultList = jql.getResultList();
			Agent agent = null;
			if (resultList != null) {
				agent = (Agent) resultList.get(0);
			}
			return agent;
		} catch (Exception e) {
			// XXX why mute the exception!?
			return null;
		}
	}
	
}
