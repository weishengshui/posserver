package com.chinarewards.qqgbvpn.mgmtui.dao.pos.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.PosAssignment;
import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.domain.status.PosDeliveryStatus;
import com.chinarewards.qqgbvpn.domain.status.PosInitializationStatus;
import com.chinarewards.qqgbvpn.domain.status.PosOperationStatus;
import com.chinarewards.qqgbvpn.logic.journal.JournalLogic;
import com.chinarewards.qqgbvpn.mgmtui.adapter.pos.PosAdapter;
import com.chinarewards.qqgbvpn.mgmtui.dao.pos.PosDao;
import com.chinarewards.qqgbvpn.mgmtui.exception.PosNotExistException;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.ParamsException;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.PosIdIsExitsException;
import com.chinarewards.qqgbvpn.mgmtui.logic.exception.SimPhoneNoIsExitsException;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosVO;
import com.chinarewards.qqgbvpn.mgmtui.model.util.PaginationTools;
import com.chinarewards.qqgbvpn.mgmtui.util.Tools;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * pos dao
 * 
 * @author huangwei
 * 
 */
public class PosDaoImpl implements PosDao {

	Logger log = LoggerFactory.getLogger(PosDaoImpl.class);

	@Inject
	Provider<EntityManager> em;

	@Inject
	Provider<PosAdapter> posAdapter;

	@Inject
	Provider<JournalLogic> journalLogic;

	public EntityManager getEm() {
		return em.get();
	}

	@Override
	public void deletePosById(String id) throws ParamsException {
		log.trace("calling deletePosById start and params is {}", id);
		if (Tools.isEmptyString(id)) {
			throw new ParamsException("id is null");
		}
		Pos pos = getEm().find(Pos.class, id);
		if (pos != null) {
			getEm().remove(pos);

			// Add journal.
			ObjectMapper mapper = new ObjectMapper();
			String eventDetail = null;
			try {
				eventDetail = mapper.writeValueAsString(pos);
			} catch (Exception e) {
				log.error("mapping Pos error.", e);
				eventDetail = e.toString();
			}

			journalLogic.get().logEvent(
					DomainEvent.USER_REMOVED_POS.toString(),
					DomainEntity.POS.toString(), pos.getId(), eventDetail);

		}
		log.trace("calling deletePosById end ");
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<PosVO> queryPos(PosSearchVO posSearchVO,
			PaginationTools paginationTools) {
		log
				.trace(
						"calling queryPos start and params is  posSearchVO:({}),paginationTools:({})",
						new Object[] { Tools.objToString(posSearchVO),
								Tools.objToString(paginationTools) });
		int recordCount = this.countPos(posSearchVO);
		PageInfo<PosVO> pageinfo = new PageInfo<PosVO>();
		pageinfo.setRecordCount(recordCount);

		StringBuffer hql = new StringBuffer();
		hql.append("SELECT p FROM Pos p WHERE 1=1 ");
		Map<String, Object> paramMap = new HashMap<String, Object>();

		log.debug("posSearchVO============:" + posSearchVO);

		if (!Tools.isEmptyString(posSearchVO)) {
			if (!Tools.isEmptyString(posSearchVO.getDstatus())) {
				hql.append(" AND p.dstatus = :dstatus ");
				paramMap.put("dstatus", PosDeliveryStatus.valueOf(posSearchVO
						.getDstatus()));
			}
			if (!Tools.isEmptyString(posSearchVO.getIstatus())) {
				hql.append(" AND p.istatus = :istatus ");
				paramMap.put("istatus", PosInitializationStatus
						.valueOf(posSearchVO.getIstatus()));
			}
			if (!Tools.isEmptyString(posSearchVO.getModel())) {
				hql.append(" AND lower(p.model) like :model ");
				paramMap.put("model", "%"
						+ posSearchVO.getModel().toLowerCase().trim() + "%");
			}
			if (!Tools.isEmptyString(posSearchVO.getOstatus())) {
				hql.append(" AND p.ostatus = :ostatus ");
				paramMap.put("ostatus", PosOperationStatus.valueOf(posSearchVO
						.getOstatus().trim()));
			}
			if (!Tools.isEmptyString(posSearchVO.getPosId())) {
				hql.append(" AND lower(p.posId) like :posid ");
				paramMap.put("posid", "%"
						+ posSearchVO.getPosId().toLowerCase().trim() + "%");
			}
			if (!Tools.isEmptyString(posSearchVO.getSecret())) {
				hql.append(" AND lower(p.secret) like :secret ");
				paramMap.put("secret", "%"
						+ posSearchVO.getSecret().toLowerCase().toLowerCase()
								.trim() + "%");
			}
			if (!Tools.isEmptyString(posSearchVO.getSimPhoneNo())) {
				hql.append(" AND lower(p.simPhoneNo) like :simPhoneNo ");
				paramMap.put("simPhoneNo", "%"
						+ posSearchVO.getSimPhoneNo().toLowerCase().trim()
						+ "%");
			}
			if (!Tools.isEmptyString(posSearchVO.getSn())) {
				hql.append(" AND lower(p.sn) like :sn ");
				paramMap.put("sn", "%" + posSearchVO.getSn().toLowerCase()
						+ "%");
			}
		}

		Query query = getEm().createQuery(hql.toString());
		log.debug("hql========:" + hql.toString());
		for (Entry<String, Object> entry : paramMap.entrySet()) {
			query = query.setParameter(entry.getKey(), entry.getValue());
			log.debug("key===({})value===({})", new Object[] { entry.getKey(),
					entry.getValue() });
		}
		if (paginationTools != null) {
			query = query.setFirstResult(paginationTools.getStartIndex())
					.setMaxResults(paginationTools.getCountOnEachPage());
		}
		List<Pos> posList = query.getResultList();
		List<PosVO> posVOList = new ArrayList<PosVO>();
		for (Pos pos : posList) {
			PosVO posVO = posAdapter.get().convertToPosVO(pos);
			posVOList.add(posVO);
		}
		pageinfo.setItems(posVOList);
		log.trace("calling queryPos end and result is :({})", Tools
				.objToString(pageinfo));
		return pageinfo;
	}

	private int countPos(PosSearchVO posSearchVO) {
		log.trace("calling countPos start and params is  posSearchVO:({})",
				Tools.objToString(posSearchVO));
		StringBuffer hql = new StringBuffer();
		hql.append("SELECT count(p.id) FROM Pos p WHERE 1=1 ");
		Map<String, Object> paramMap = new HashMap<String, Object>();

		log.debug("posSearchVO======count======:" + posSearchVO);
		if (!Tools.isEmptyString(posSearchVO)) {
			if (!Tools.isEmptyString(posSearchVO.getDstatus())) {
				hql.append(" AND p.dstatus = :dstatus ");
				paramMap.put("dstatus", PosDeliveryStatus.valueOf(posSearchVO
						.getDstatus()));
			}
			if (!Tools.isEmptyString(posSearchVO.getIstatus())) {
				hql.append(" AND p.istatus = :istatus ");
				paramMap.put("istatus", PosInitializationStatus
						.valueOf(posSearchVO.getIstatus()));
			}
			if (!Tools.isEmptyString(posSearchVO.getModel())) {
				hql.append(" AND lower(p.model) like :model ");
				paramMap.put("model", "%"
						+ posSearchVO.getModel().toLowerCase().trim() + "%");
			}
			if (!Tools.isEmptyString(posSearchVO.getOstatus())) {
				hql.append(" AND p.ostatus = :ostatus ");
				paramMap.put("ostatus", PosOperationStatus.valueOf(posSearchVO
						.getOstatus().trim()));
			}
			if (!Tools.isEmptyString(posSearchVO.getPosId())) {
				hql.append(" AND lower(p.posId) like :posId ");
				paramMap.put("posId", "%"
						+ posSearchVO.getPosId().toLowerCase().trim() + "%");
			}
			if (!Tools.isEmptyString(posSearchVO.getSecret())) {
				hql.append(" AND lower(p.secret) like :secret ");
				paramMap.put("secret", "%"
						+ posSearchVO.getSecret().toLowerCase().toLowerCase()
								.trim() + "%");
			}
			if (!Tools.isEmptyString(posSearchVO.getSimPhoneNo())) {
				hql.append(" AND lower(p.simPhoneNo) like :simPhoneNo ");
				paramMap.put("simPhoneNo", "%"
						+ posSearchVO.getSimPhoneNo().toLowerCase().trim()
						+ "%");
			}
			if (!Tools.isEmptyString(posSearchVO.getSn())) {
				hql.append(" AND lower(p.sn) like :sn ");
				paramMap.put("sn", "%" + posSearchVO.getSn().toLowerCase()
						+ "%");
			}
		}
		log.debug("hql====count====:" + hql.toString());
		log.debug("paramMap.keySet().size()==============:"
				+ paramMap.keySet().size());
		Query query = getEm().createQuery(hql.toString());
		for (Entry<String, Object> entry : paramMap.entrySet()) {
			query = query.setParameter(entry.getKey(), entry.getValue());
			log.debug("count   key===({})value===({})", new Object[] {
					entry.getKey(), entry.getValue() });
		}
		int count = ((Long) query.getSingleResult()).intValue();
		log.trace("calling queryPos end and result is :({})", count);
		return count;
	}

	@Override
	public PosVO savePos(PosVO posVO) throws PosIdIsExitsException,
			ParamsException, SimPhoneNoIsExitsException {
		log.trace("calling savePos start and params is {}", Tools
				.objToString(posVO));
		if (Tools.isEmptyString(posVO)) {
			throw new ParamsException("posVO is null");
		}
		if (Tools.isEmptyString(posVO.getPosId())) {
			throw new ParamsException("posId is null");
		}
		if (posIdIsExits(posVO.getPosId())) {
			throw new PosIdIsExitsException("posId is exits");
		}
		if (Tools.isEmptyString(posVO.getSimPhoneNo())) {
			throw new ParamsException("simPhoneNo is null");
		}
		long count = (Long) getEm()
				.createQuery(
						"select count(p.id) from Pos p where p.simPhoneNo = :simPhoneNo")
				.setParameter("simPhoneNo", posVO.getSimPhoneNo().trim())
				.getSingleResult();
		if (count > 0) {
			throw new SimPhoneNoIsExitsException("simPhoneNo is exits");
		}

		Pos pos = posAdapter.get().convertToPos(posVO);
		getEm().persist(pos);
		posVO.setId(pos.getId());

		// Add journal.
		ObjectMapper mapper = new ObjectMapper();
		String eventDetail = null;
		try {
			eventDetail = mapper.writeValueAsString(pos);
		} catch (Exception e) {
			log.error("mapping Pos error.", e);
			eventDetail = e.toString();
		}

		journalLogic.get().logEvent(DomainEvent.USER_ADDED_POS.toString(),
				DomainEntity.POS.toString(), pos.getId(), eventDetail);

		log.trace("calling savePos end and result is :({})", Tools
				.objToString(posVO));
		return posVO;
	}

	private boolean posIdIsExits(String posId) {
		long count = (Long) getEm().createQuery(
				"select count(p.id) from Pos p where p.posId = :posId")
				.setParameter("posId", posId.trim()).getSingleResult();
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void updatePos(PosVO posVO) throws PosIdIsExitsException,
			ParamsException, SimPhoneNoIsExitsException {
		log.trace("calling updatePos start and params is {}", Tools
				.objToString(posVO));
		if (Tools.isEmptyString(posVO)) {
			throw new ParamsException("posVO is null");
		}
		if (Tools.isEmptyString(posVO.getId())) {
			throw new ParamsException("id is null");
		}

		if (Tools.isEmptyString(posVO.getPosId())) {
			throw new ParamsException("posId is null");
		}

		long count = (Long) getEm()
				.createQuery(
						"select count(p.id) from Pos p where p.posId = :posId and p.id != :id")
				.setParameter("posId", posVO.getPosId().trim()).setParameter("id",
						posVO.getId()).getSingleResult();
		if (count > 0) {
			throw new PosIdIsExitsException("posId is exits");
		}
		if (Tools.isEmptyString(posVO.getSimPhoneNo())) {
			throw new ParamsException("simPhoneNo is null");
		}
		count = (Long) getEm()
				.createQuery(
						"select count(p.id) from Pos p where p.simPhoneNo = :simPhoneNo and p.id != :id")
				.setParameter("simPhoneNo", posVO.getSimPhoneNo().trim()).setParameter(
						"id", posVO.getId()).getSingleResult();
		if (count > 0) {
			throw new SimPhoneNoIsExitsException("simPhoneNo is exits");
		}

		Pos pos = getEm().find(Pos.class, posVO.getId());
		if (pos == null) {
			throw new ParamsException("pos is not exits,id is:" + posVO.getId());
		}
		Pos newPos = posAdapter.get().convertToPos(posVO);
		getEm().merge(newPos);

		// Add journal.
		ObjectMapper mapper = new ObjectMapper();
		String eventDetail = null;
		try {
			eventDetail = mapper.writeValueAsString(pos);
		} catch (Exception e) {
			log.error("mapping Pos error.", e);
			eventDetail = e.toString();
		}

		log.debug("here========:" + eventDetail);

		journalLogic.get().logEvent(DomainEvent.USER_EDITED_POS.toString(),
				DomainEntity.POS.toString(), pos.getId(), eventDetail);

		log.trace("calling savePos end ");
	}

	@Override
	public void updatePosStatusToWorking(List<String> posIds) {
		if (posIds == null || posIds.isEmpty()) {
			throw new IllegalArgumentException("posId is missing");
		}
		String d_hql = "UPDATE Pos p SET p.dstatus=:dstatus WHERE p.posId IN (:posIds)";
		String o_hql = "UPDATE Pos p SET p.ostatus=:ostatus WHERE p.posId IN (:posIds)";
		int d_success = getEm().createQuery(d_hql)
				.setParameter("dstatus", PosDeliveryStatus.DELIVERED)
				.setParameter("posIds", posIds).executeUpdate();
		log.debug("{} pos status updated to PosDeliveryStatus.DELIVERED",
				d_success);
		int o_success = getEm().createQuery(o_hql)
				.setParameter("ostatus", PosOperationStatus.ALLOWED)
				.setParameter("posIds", posIds).executeUpdate();
		log.debug("{} pos status updated to PosOperationStatus.ALLOWED",
				o_success);
	}
	
	@Override
	public PosVO getPosById(String id) throws ParamsException {
		log.trace("calling getPosById start and params is {}", id);
		if (Tools.isEmptyString(id)) {
			throw new ParamsException("id is null");
		}
		Pos pos = getEm().find(Pos.class, id);
		if (pos != null) {
			PosVO posVO = posAdapter.get().convertToPosVO(pos);
			if(pos.getDstatus() == PosDeliveryStatus.DELIVERED){
				posVO.setDeliveryAgent(getDeliveryAgentByPos(pos.getId()));
			}
			posVO.setCreateAt(this.getPosCreateAtById(pos.getId()));
			posVO.setLastModifyAt(this.getPosLastModifyAtById(pos.getId()));
			return posVO;
		} else {
			return null;
		}

	}
	
	@SuppressWarnings("unchecked")
	private Date getPosCreateAtById(String entityId){
		String hql = "select j.ts from Journal j where j.entityId = :entityId and j.event = :event";
		List<Date> list = this.getEm().createQuery(hql).setParameter("entityId", entityId).setParameter("event", DomainEvent.USER_ADDED_POS.toString()).getResultList();
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private Date getPosLastModifyAtById(String entityId){
		String hql = "select j.ts from Journal j where j.entityId = :entityId and j.event = :event order by j.ts desc";
		List<Date> list = this.getEm().createQuery(hql).setParameter("entityId", entityId).setParameter("event", DomainEvent.USER_EDITED_POS.toString()).setFirstResult(0).setMaxResults(1).getResultList();
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}

	
	@SuppressWarnings("unchecked")
	private String getDeliveryAgentByPos(String id){
		String hql = "select pa.agent.name from PosAssignment pa where pa.pos.id = :pid";
		List<String> list = this.getEm().createQuery(hql).setParameter("pid", id).getResultList();
		if(list!= null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public void createPosAssignment(String agentId, List<String> posIds) throws PosNotExistException {
		Agent agent = getEm().find(Agent.class, agentId);
		for (String posId : posIds) {
			Pos pos = getEm().find(Pos.class, posId);
			if (pos == null) {
				throw new PosNotExistException("pos id=" + posId + " not found");
			}
			PosAssignment pa = new PosAssignment();
			pa.setAgent(agent);
			pa.setPos(pos);
			getEm().persist(pa);
		}
	}

}
