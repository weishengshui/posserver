package com.chinarewards.qqgbvpn.mgmtui.dao.agent.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.mgmtui.dao.agent.AgentDao;
import com.chinarewards.qqgbvpn.mgmtui.exception.ServiceException;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentStore;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentVO;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.util.Tools;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * description：agent dao 实现
 * 
 * @author Seek
 */
public class AgentDaoImpl implements AgentDao {

	Logger log = LoggerFactory.getLogger(AgentDaoImpl.class);

	@Inject
	Provider<EntityManager> em;

	public EntityManager getEm() {
		return em.get();
	}

	@Override
	public AgentStore queryAgent(AgentSearchVO agentSearchVO)
			throws ServiceException {
		AgentStore agentStore = new AgentStore();
		
		try{
			//获取总数 以及 列表
			Integer countTotal = Integer.parseInt(queryAgent(COUNT, agentSearchVO).getSingleResult().toString());
			agentStore.setCountTotal(countTotal);
			agentStore.setAgentVOList(queryAgent(LIST, agentSearchVO).getResultList());
		}catch(Throwable e){
			throw new ServiceException(e);
		}
		
		return agentStore;
	}
	
	private Query queryAgent(String queryType, AgentSearchVO agentSearchVO){
		log.debug(" process in queryAgent() method ");
		log.debug("agentSearchVO:" + agentSearchVO);
		
		Map<String, Object> param = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		
		if(COUNT.equals(queryType)){
			hql.append(" SELECT COUNT(DISTINCT ag.id) FROM Agent ag WHERE 1=1 ");
		}else {
			hql.append(" SELECT ag FROM Agent ag WHERE 1=1 ");	
		}
		
		if(agentSearchVO != null && !Tools.isEmptyString(agentSearchVO.getAgentName())){
			hql.append(" AND upper(ag.name) like :agentName ");
			param.put("agentName", "%"+agentSearchVO.getAgentName().toUpperCase()+"%");
		}
		
		log.debug(" queryShop SQL : " + hql);
		
		Query query = getEm().createQuery(hql.toString());
		
		if (LIST.equals(queryType) && agentSearchVO != null && agentSearchVO.getPage() != null 
				&& agentSearchVO.getSize() != null) {
			query.setFirstResult( (agentSearchVO.getPage()-1) * agentSearchVO.getSize() );
			query.setMaxResults( agentSearchVO.getSize() );
		}
		
		if (param.size() > 0) {
			Set<String> key = param.keySet();
			for (String s : key) {
				query.setParameter(s, param.get(s));
			}
		}
		
		return query;
	}

	@Override
	public AgentVO save(AgentVO agentVO) throws ServiceException {
		log.trace("calling saveAgent start and params is {}", Tools
				.objToString(agentVO));
		try{
			if (Tools.isEmptyString(agentVO)) {
				throw new ServiceException("agentVO is null");
			}
			Agent agent = new Agent();
			agent.setName(agentVO.getName());
			agent.setEmail(agentVO.getEmail());
			
			getEm().persist(agent);
			
			agentVO.setId(agent.getId());
			return agentVO;
		}catch(Throwable e){
			throw new ServiceException(e);
		}
	}

	@Override
	public AgentVO update(AgentVO agentVO) throws ServiceException {
		log.trace("calling updateAgent start and params is {}", Tools
				.objToString(agentVO));
		
		try{
			if (Tools.isEmptyString(agentVO)) {
				throw new ServiceException("agentVO is null");
			}
			if (Tools.isEmptyString(agentVO.getId())) {
				throw new ServiceException("agentVO.getId() is null");
			}
			
			Agent agent = getEm().find(Agent.class, agentVO.getId());
			agent.setName(agentVO.getName());
			agent.setEmail(agentVO.getEmail());
			
			getEm().merge(agent);
			
			agentVO.setId(agent.getId());
			return agentVO;
		}catch(Throwable e){
			throw new ServiceException(e);
		}
	}

	@Override
	public void delete(String agentId) throws ServiceException {
		log.trace("calling deleteAgent start and params is {}", Tools
				.objToString(agentId));
		try{
			if (Tools.isEmptyString(agentId)) {
				throw new ServiceException("agentId is null");
			}
			Agent agent = getEm().find(Agent.class, agentId);
			getEm().remove(agent);
		}catch(Throwable e){
			throw new ServiceException(e);
		}
	}
	
	@Override
	public AgentVO findById(String agentId) throws ServiceException {
		log.trace("calling findByIdAgent start and params is {}", Tools
				.objToString(agentId));
		try{
			if (Tools.isEmptyString(agentId)) {
				return null;
			}
			
			AgentVO agentVO = new AgentVO();
			Agent agent = getEm().find(Agent.class, agentId);
			
			if(agent == null){
				return null;
			}
			
			agentVO.setId(agent.getId());
			agentVO.setName(agent.getName());
			agentVO.setEmail(agent.getEmail());
			return agentVO;
		}catch(Throwable e){
			throw new ServiceException(e);
		}	
	}
	
}
