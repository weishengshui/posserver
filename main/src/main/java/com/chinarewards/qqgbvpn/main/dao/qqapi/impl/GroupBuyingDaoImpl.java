package com.chinarewards.qqgbvpn.main.dao.qqapi.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;

import com.chinarewards.qqgbvpn.core.BaseDao;
import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.GroupBuyValidateResult;
import com.chinarewards.qqgbvpn.domain.GrouponCache;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.Validation;
import com.chinarewards.qqgbvpn.domain.status.GroupBuyValidateResultStatus;
import com.chinarewards.qqgbvpn.main.dao.qqapi.GroupBuyingDao;
import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingValidateResultVO;

public class GroupBuyingDaoImpl extends BaseDao implements GroupBuyingDao {

	public void saveGrouponCache(GrouponCache grouponCache) {
		getEm().persist(grouponCache);
	}
	
	public List<GrouponCache> deleteGrouponCache(String posId) {
		List<GrouponCache> list = getGrouponCacheByPosId(posId);
		if (list != null && list.size() > 0) {
			Query query = getEm().createQuery("delete from GrouponCache gc where gc.posId = ?1");
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
		Query query = getEm().createQuery("select gc from GrouponCache gc where gc.posId = ?1");
		query.setParameter(1, posId);
		return query.getResultList() != null ? (List<GrouponCache>) query.getResultList() : null;
	}
	
	public void saveValidation(Validation validation) {
		getEm().persist(validation);
	}
	
	public Pos getPosByPosId(String posId) {
		try {
			Query jql = getEm().createQuery("select p from Pos p where p.posId = ?1");
			jql.setParameter(1, posId);
			Pos p = (Pos) jql.getSingleResult();
			return p;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Agent getAgentByPosId(String posId) {
		try {
			Query jql = getEm().createQuery("select a from PosAssignment pa,Pos p,Agent a " +
					"where pa.pos.id = p.id and pa.agent.id = a.id and p.posId = ?1");
			jql.setParameter(1, posId);
			Agent a = (Agent) jql.getSingleResult();
			return a;
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public GroupBuyingValidateResultVO groupBuyingValidateLocal(
			String grouponId, String grouponVCode) throws SaveDBException,
			JsonGenerationException {
		if (grouponId == null || grouponId.length() == 0) {
			throw new SaveDBException("grouponId is null");
		}
		if (grouponVCode == null || grouponVCode.length() == 0) {
			throw new SaveDBException("grouponVCode is null");
		}
		String hql = "select r from GroupBuyValidateResult r where r.grouponId = :grouponId and r.grouponVCode = :grouponVCode and r.status = :status";
		List<GroupBuyValidateResult> list = getEm().createQuery(hql)
				.setParameter("grouponId", grouponId).setParameter(
						"grouponVCode", grouponVCode).setParameter("status",
						GroupBuyValidateResultStatus.QQVALIDATION).getResultList();
		if (list != null && list.size() > 0) {
			GroupBuyValidateResult groupBuyValidateResult = list.get(0);
			String result = groupBuyValidateResult.getResult();

			ObjectMapper mapper = new ObjectMapper();
			GroupBuyingValidateResultVO groupBuyingValidateResultVO;
			try {
				groupBuyingValidateResultVO = mapper.readValue(result,
						GroupBuyingValidateResultVO.class);
			} catch (Exception e) {
				throw new JsonGenerationException("json转换错误");
			}

			return groupBuyingValidateResultVO;
		} else {
			return null;
		}
	}

	@Override
	public void createValidateResultLocal(String grouponId,
			String grouponVCode,
			GroupBuyingValidateResultVO groupBuyingValidateResultVO)
			throws SaveDBException, JsonGenerationException {

		if (grouponId == null || grouponId.length() == 0) {
			throw new SaveDBException("grouponId is null");
		}
		if (grouponVCode == null || grouponVCode.length() == 0) {
			throw new SaveDBException("grouponVCode is null");
		}
		if (groupBuyingValidateResultVO == null) {
			throw new SaveDBException("groupBuyingValidateResultVO is null");
		}
		ObjectMapper mapper = new ObjectMapper();
		String result = null;
		try {
			result = mapper.writeValueAsString(groupBuyingValidateResultVO);
		} catch (Exception e) {
			throw new JsonGenerationException("json转换错误");
		}
		if (!getEm().getTransaction().isActive()) {
			getEm().getTransaction().begin();
		}
		GroupBuyValidateResult groupBuyValidateResult = new GroupBuyValidateResult();
		groupBuyValidateResult.setCreateAt(new Date());
		groupBuyValidateResult.setGrouponId(grouponId);
		groupBuyValidateResult.setGrouponVCode(grouponVCode);
		groupBuyValidateResult.setResult(result);
		groupBuyValidateResult.setStatus(GroupBuyValidateResultStatus.QQVALIDATION);
		getEm().persist(groupBuyValidateResult);
		if (getEm().getTransaction().isActive()) {
			getEm().getTransaction().commit();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void groupBuyValidateCallBack(String grouponId, String grouponVCode)
			throws SaveDBException {
		if (grouponId == null || grouponId.length() == 0) {
			throw new SaveDBException("grouponId is null");
		}
		if (grouponVCode == null || grouponVCode.length() == 0) {
			throw new SaveDBException("grouponVCode is null");
		}
		if (!getEm().getTransaction().isActive()) {
			getEm().getTransaction().begin();
		}
		
		String hql = "select r from GroupBuyValidateResult r where r.grouponId = :grouponId and r.grouponVCode = :grouponVCode and r.status = :status";
		List<GroupBuyValidateResult> list = getEm().createQuery(hql)
				.setParameter("grouponId", grouponId).setParameter(
						"grouponVCode", grouponVCode).setParameter("status",
						GroupBuyValidateResultStatus.QQVALIDATION).getResultList();
		
		for (GroupBuyValidateResult groupBuyValidateResult:list) {
			groupBuyValidateResult.setModifyAt(new Date());
			groupBuyValidateResult.setStatus(GroupBuyValidateResultStatus.POSVALIDATION);
			getEm().merge(groupBuyValidateResult);
		} 
		if (!getEm().getTransaction().isActive()) {
			getEm().getTransaction().commit();
		}
	}
	
}
