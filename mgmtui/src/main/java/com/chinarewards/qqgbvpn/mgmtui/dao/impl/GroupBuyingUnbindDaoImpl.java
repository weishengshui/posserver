package com.chinarewards.qqgbvpn.mgmtui.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.PosAssignment;
import com.chinarewards.qqgbvpn.domain.ReturnNote;
import com.chinarewards.qqgbvpn.domain.ReturnNoteDetail;
import com.chinarewards.qqgbvpn.domain.ReturnNoteInvitation;
import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.domain.event.Journal;
import com.chinarewards.qqgbvpn.domain.status.PosDeliveryStatus;
import com.chinarewards.qqgbvpn.domain.status.ReturnNoteStatus;
import com.chinarewards.qqgbvpn.mgmtui.dao.GroupBuyingUnbindDao;
import com.chinarewards.qqgbvpn.mgmtui.exception.SaveDBException;
import com.chinarewards.qqgbvpn.mgmtui.exception.UnUseableRNException;
import com.chinarewards.qqgbvpn.mgmtui.util.Tools;
import com.chinarewards.qqgbvpn.mgmtui.vo.ReturnNoteInfo;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingUnbindVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GroupBuyingUnbindDaoImpl extends BaseDaoImpl implements GroupBuyingUnbindDao {

	public void handleGroupBuyingUnbind(HashMap<String, Object> params) throws SaveDBException {
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
						journal.setEntity(DomainEntity.POS_ASSIGNMENT.toString());
						journal.setEntityId(pa.getId());
						journal.setEvent(DomainEvent.POS_UNBIND_SUCCESS.toString());
						if (items != null) {
							// FIXME use Jackson JSON processor 这里用那个转换延迟加载的对象会出错
							GsonBuilder builder = new GsonBuilder();
							Gson gson = builder.create();
							journal.setEventDetail(gson.toJson(pa));
						}
						try {
							
							if ("0".equals(resultCode)) {
								log.debug("resultCode : {}", resultCode);
								//删除绑定关系
								em.get().remove(pa);
								//更新POS机交付状态
								Pos p = pa.getPos();
								log.debug("posId : {}", p.getPosId());
								p.setDstatus(PosDeliveryStatus.RETURNED);
								savePos(p);
								//更新POS机所关联的所有回收单中POS机的交付状态
								List<ReturnNoteDetail> rnDetailList = getReturnNoteDetailListByPosId(p.getPosId());
								log.debug("rnDetailList.size : {}", rnDetailList.size());
								if (rnDetailList != null && rnDetailList.size() > 0) {
									HashMap<String,ReturnNote> tempMap = new HashMap<String,ReturnNote>();
									for (ReturnNoteDetail rnd : rnDetailList) {
										rnd.setDstatus(PosDeliveryStatus.RETURNED);
										saveReturnNoteDetail(rnd);
										//收集还没有完全回收的回收单
										if (!ReturnNoteStatus.RETURNED.equals(rnd.getRn().getStatus()) 
												&& !tempMap.containsKey(rnd.getRn().getId())) {
											tempMap.put(rnd.getRn().getId(), rnd.getRn());
										}
									}
									for (String rnId : tempMap.keySet()) {
										//如果回收单中的所有POS机都已经回收，则修改回收单的状态为全部回收
										if (isReadyToReturned(rnId)) {
											ReturnNote returnNote = tempMap.get(rnId);
											returnNote.setStatus(ReturnNoteStatus.RETURNED);
											saveReturnNote(returnNote);
										}
									}
								}
							}
							saveJournal(journal);
						} catch (Exception e) {
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
						journal.setEntity(DomainEntity.POS_ASSIGNMENT.toString());
						journal.setEntityId(posId);
						journal.setEvent(DomainEvent.POS_UNBIND_FAILED.toString());
						journal.setEventDetail("group buying unbind error,pos assignment not found by posId : " + posId);
						try {
							saveJournal(journal);
						} catch (Exception e) {
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
					journal.setEntity(DomainEntity.POS_ASSIGNMENT.toString());
					journal.setEntityId(posId);
					journal.setEvent(DomainEvent.POS_UNBIND_FAILED.toString());
					journal.setEventDetail(resultStatus);
					try {
						saveJournal(journal);
					} catch (Exception e) {
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
			journal.setEntity(DomainEntity.POS_ASSIGNMENT.toString());
			journal.setEntityId(posIds.toString());
			journal.setEvent(DomainEvent.POS_UNBIND_FAILED.toString());
			journal.setEventDetail(resultCode);
			try {
				saveJournal(journal);
			} catch (Exception e) {
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
	
	private void savePos(Pos p) {
		em.get().persist(p);
	}
	
	private PosAssignment getPosAssignmentByIdPosId(String posId) {
		Query jql = em.get().createQuery("select pa from PosAssignment pa,Pos p where pa.pos.id = p.id and p.posId = ?1");
		jql.setParameter(1, posId);
		List resultList = jql.getResultList();
		PosAssignment pa = null;
		if (resultList != null && resultList.size() > 0) {
			pa = (PosAssignment) resultList.get(0);
		}
		return pa;
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
		String countSql = "select count(p.id) from Pos p, PosAssignment pa where p.id = pa.pos.id and pa.agent.id = :agentId";
		String searchSql = "select p from Pos p, PosAssignment pa where p.id = pa.pos.id and pa.agent.id = :agentId order by p.posId desc";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("agentId", agentId);
		
		pageInfo = this.findPageInfo(countSql.toString(),searchSql.toString(),paramMap,pageInfo);
		return pageInfo;
	}
	
	private List<Pos> getPosByAgentId(String agentId) {
		Query jql = em.get().createQuery("select p from Pos p, PosAssignment pa where p.id = pa.pos.id and pa.agent.id = ?1");
		jql.setParameter(1, agentId);
		List<Pos> resultList = jql.getResultList();
		return resultList;
	}
	
	/* (non-Javadoc)
	 * @see com.chinarewards.qqgbvpn.mgmtui.dao.GroupBuyingUnbindDao#getAgentByName(java.lang.String)
	 * 发送URL页面查询调用
	 */
	public Agent getAgentByName(String agentName) {
		Query jql = em.get().createQuery("select a from Agent a where a.name = ?1");
		jql.setParameter(1, agentName);
		List resultList = jql.getResultList();
		Agent agent = null;
		if (resultList != null && resultList.size() > 0) {
			agent = (Agent) resultList.get(0);
		}
		return agent;
	}
	
	public List<Agent> getAgentLikeName(String agentName) {
		Query jql = em.get().createQuery("select a from Agent a where upper(a.name) like ?1");
		jql.setParameter(1, "%" + agentName.toUpperCase() + "%");
		List resultList = jql.getResultList();
		return resultList;
	}
	
	/* (non-Javadoc)
	 * @see com.chinarewards.qqgbvpn.mgmtui.dao.GroupBuyingUnbindDao#getPosByPosInfo(java.lang.String)
	 * 解绑页面查询调用
	 */
	public List<Pos> getPosByPosInfo(String info) {
		Query jql = em.get().createQuery("select p from Pos p where p.posId = ?1 or p.sn = ?1 or p.simPhoneNo = ?1");
		jql.setParameter(1, info);
		List<Pos> resultList = jql.getResultList();
		return resultList;
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
			rn.setRnNumber(Tools.getOnlyNumber("POSRN"));
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
			} catch (Exception e) {
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
	public ReturnNote confirmReturnNote(String agentId,String inviteCode, List<String> posIds) throws SaveDBException,UnUseableRNException {
		ReturnNote rn = null;
		Date date = new Date();
		Agent a = this.getAgentById(agentId);
		if (a != null) {
			if (inviteCode != null && !"".equals(inviteCode.trim())) {
				rn = this.getReturnNoteByToken(inviteCode);
				if (rn != null && ReturnNoteStatus.CONFIRMED.equals(rn.getStatus())) {
					log.warn("Return Note already confirmed!");
					throw new UnUseableRNException(rn.getId() + "," + rn.getRnNumber() + "," + rn.getCreateDate());
				}
			}
			if (rn == null) {
				rn = new ReturnNote();
				rn.setRnNumber(Tools.getOnlyNumber("POSRN"));
				rn.setAgent(a);
				rn.setAgentName(a.getName());
				rn.setStatus(ReturnNoteStatus.CONFIRMED);
				rn.setCreateDate(date);
				rn.setConfirmDate(date);
				rn.setToken(inviteCode);
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
						log.debug("p.getDstatus() : {}", p.getDstatus());
						ReturnNoteDetail rnd = new ReturnNoteDetail();
						rnd.setRn(rn);
						rnd.setPosId(p.getPosId());
						rnd.setModel(p.getModel());
						rnd.setSimPhoneNo(p.getSimPhoneNo());
						rnd.setSn(p.getSn());
						rnd.setDstatus(p.getDstatus());
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
	
	public ReturnNoteInfo confirmAllReturnNote(String agentId) throws SaveDBException {
		List<Pos> posList = null;
		Date date = new Date();
		Agent a = this.getAgentById(agentId);
		if (a != null) {
			ReturnNote rn = new ReturnNote();
			rn.setRnNumber(Tools.getOnlyNumber("POSRN"));
			rn.setAgent(a);
			rn.setAgentName(a.getName());
			rn.setStatus(ReturnNoteStatus.CONFIRMED);
			rn.setCreateDate(date);
			rn.setConfirmDate(date);
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
				
				posList = getPosByAgentId(agentId);
				
				if (posList != null && posList.size() > 0) {
					for (Pos p : posList) {
						ReturnNoteDetail rnd = new ReturnNoteDetail();
						rnd.setRn(rn);
						rnd.setPosId(p.getPosId());
						rnd.setModel(p.getModel());
						rnd.setSimPhoneNo(p.getSimPhoneNo());
						rnd.setSn(p.getSn());
						rnd.setDstatus(p.getDstatus());
						saveReturnNoteDetail(rnd);
					}
				}
				
			} catch (Exception e) {
				throw new SaveDBException(e);
			}
			ReturnNoteInfo rnInfo = new ReturnNoteInfo(a, rn, posList);
			return rnInfo;
		}
		return null;
	}
	
	private void saveReturnNote(ReturnNote rn) {
		em.get().persist(rn);
	}
	
	private void saveReturnNoteDetail(ReturnNoteDetail rnd) {
		em.get().persist(rnd);
	}
	
	private void saveReturnNoteInvitation(ReturnNoteInvitation rni) {
		em.get().persist(rni);
	}
	
	private ReturnNote getReturnNote(String rnId) {
		ReturnNote rn = em.get().find(ReturnNote.class, rnId);
		return rn;
	}
	
	private ReturnNote getReturnNoteByToken(String token) {
		log.debug("token: {}", token);
		Query jql = em.get().createQuery("select rn from ReturnNote rn where rn.token = ?1");
		jql.setParameter(1, token);
		List resultList = jql.getResultList();
		ReturnNote rn = null;
		log.debug("resultList size {}", resultList.size());
		if (resultList != null && resultList.size() > 0) {
			rn = (ReturnNote) resultList.get(0);
		}
		return rn;
	}
	
	private Agent getAgentById(String agentId) {
		Agent a = em.get().find(Agent.class, agentId);
		return a;
	}
	
	private List<Pos> getPosListByIds(List<String> posIds) {
		log.debug("posIds:{}", posIds);
		Query jql = em.get().createQuery("select p from Pos p where p.id in (?1)");
		jql.setParameter(1, posIds);
		List<Pos> resultList = jql.getResultList();
		return resultList;
	}
	
	public Agent getAgentByRnId(String rnId) {
		Query jql = em.get().createQuery("select a from Agent a,ReturnNote rn where a.id = rn.agent.id and rn.status = '"
				+ ReturnNoteStatus.DRAFT.toString() + "' and rn.id = ?1");
		jql.setParameter(1, rnId);
		List resultList = jql.getResultList();
		Agent agent = null;
		if (resultList != null && resultList.size() > 0) {
			agent = (Agent) resultList.get(0);
		}
		return agent;
	}
	
	public Agent getAgentByInviteCode(String inviteCode) {
		Query jql = em.get().createQuery("select a from Agent a,ReturnNoteInvitation rni where a.id = rni.agent.id and rni.token = ?1" +
				" and not EXISTS (select 1 from ReturnNote rn where rni.token = rn.token)");
		jql.setParameter(1, inviteCode);
		List resultList = jql.getResultList();
		Agent agent = null;
		if (resultList != null && resultList.size() > 0) {
			agent = (Agent) resultList.get(0);
		}
		return agent;
	}
	
	public String createInviteCode(String agentId) {
		Date date = new Date();
		Agent a = this.getAgentById(agentId);
		if (a != null) {
			ReturnNoteInvitation rni = new ReturnNoteInvitation();
			rni.setAgent(a);
			rni.setRequestDate(date);
			rni.setToken(Tools.getOnlyNumber("POSRN-DRAFT"));
			
			saveReturnNoteInvitation(rni);
			
			Journal journal = new Journal();
			journal.setTs(date);
			journal.setEntity(DomainEntity.RETURN_NOTE_INVITATION.toString());
			journal.setEntityId(rni.getId());
			journal.setEvent(DomainEvent.USER_ADDED_RNOTE_INVITATION.toString());
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			journal.setEventDetail(gson.toJson(rni));
			saveJournal(journal);
			
			return rni.getToken();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.chinarewards.qqgbvpn.mgmtui.dao.GroupBuyingUnbindDao#getReturnNoteInfoByRnNumber(java.lang.String)
	 * 查回收单详情
	 */
	public ReturnNoteInfo getReturnNoteInfoByRnId(String rnId) {
		ReturnNoteInfo rnInfo = new ReturnNoteInfo();
		ReturnNote rn = em.get().find(ReturnNote.class, rnId);
		if (rn != null) {
			Query jql = em.get().createQuery("select rnd from ReturnNoteDetail rnd where rnd.rn.id = ?1");
			jql.setParameter(1, rnId);
			List<ReturnNoteDetail> rnDetailList = jql.getResultList();
			
			rnInfo.setRn(rn);
			rnInfo.setAgent(rn.getAgent());
			rnInfo.setRnDetailList(rnDetailList);
		}
		return rnInfo;
	}
	
	/**
	 * @param rnNumber
	 * @return
	 * 查回收单列表
	 */
	public PageInfo getReturnNoteLikeRnNumber(String rnNumber, String status, PageInfo pageInfo) {
		StringBuffer countSql = new StringBuffer("select count(rn.id) from ReturnNote rn where upper(rn.rnNumber) like :rnNumber");
		StringBuffer searchSql = new StringBuffer("select rn from ReturnNote rn where upper(rn.rnNumber) like :rnNumber");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("rnNumber", "%" + rnNumber.toUpperCase() + "%");
		if (!Tools.isEmptyString(status)) {
			countSql.append(" and rn.status = :status");
			searchSql.append(" and rn.status = :status");
			paramMap.put("status", ReturnNoteStatus.valueOf(status));
		}
		searchSql.append(" order by rn.createDate desc");
		
		pageInfo = this.findPageInfo(countSql.toString(),searchSql.toString(),paramMap,pageInfo);
		return pageInfo;
	}
	
	private List<ReturnNoteDetail> getReturnNoteDetailListByPosId(String posId) {
		Query jql = em.get().createQuery("select rnd from ReturnNoteDetail rnd where rnd.dstatus = '" 
					+ PosDeliveryStatus.DELIVERED.toString() + "' and rnd.posId = ?1");
		jql.setParameter(1, posId);
		return jql.getResultList();
	}
	
	private boolean isReadyToReturned(String rnId) {
		log.debug("rnId : {}", rnId);
		Query jql = em.get().createQuery("select count(rnd.id) from ReturnNoteDetail rnd where rnd.dstatus = '" 
				+ PosDeliveryStatus.DELIVERED.toString() + "' and rnd.rn.id = ?1");
		jql.setParameter(1, rnId);
		Long count = (Long) jql.getSingleResult();
		log.debug("count : {}", count);
		return count != null && count > 0 ? false : true;
	}
	
}
