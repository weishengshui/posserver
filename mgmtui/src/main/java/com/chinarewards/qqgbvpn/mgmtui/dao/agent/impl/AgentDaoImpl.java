package com.chinarewards.qqgbvpn.mgmtui.dao.agent.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.core.BaseDao;
import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.logic.journal.JournalLogic;
import com.chinarewards.qqgbvpn.mgmtui.dao.agent.AgentDao;
import com.chinarewards.qqgbvpn.mgmtui.exception.ServiceException;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentVO;
import com.chinarewards.qqgbvpn.mgmtui.util.Tools;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * description：agent dao 实现
 * 
 * @author Seek
 */
public class AgentDaoImpl extends BaseDao implements AgentDao {

	Logger log = LoggerFactory.getLogger(AgentDaoImpl.class);

	@Inject
	Provider<JournalLogic> journalLogic;

	private void addLog(Agent agent, String processType){
		// Add journal.
		ObjectMapper mapper = new ObjectMapper();
		String eventDetail = null;
		try {
			eventDetail = mapper.writeValueAsString(agent);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			eventDetail = e.toString();
		}

		journalLogic.get().logEvent(processType,
				DomainEntity.AGENT.toString(), agent.getId(), eventDetail);
	}
	
	@Override
	public PageInfo<AgentVO> queryAgent(AgentSearchVO agentSearchVO)
			throws ServiceException {
		PageInfo<AgentVO> pageInfo = null;
		try{
			//获取总数 以及 列表		
			Map<String,Object> paramMap = new HashMap<String,Object>();
			StringBuffer searchSql = new StringBuffer(" SELECT new com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentVO(ag.id, ag.name, ag.email) FROM Agent ag WHERE 1=1 ");
			StringBuffer countSql = new StringBuffer(" SELECT COUNT(ag.id) FROM Agent ag WHERE 1=1 ");
			
			
			if(agentSearchVO != null && !Tools.isEmptyString(agentSearchVO.getAgentName())){
				searchSql.append(" AND upper(ag.name) like :agentName ");
				countSql.append(" AND upper(ag.name) like :agentName ");
				paramMap.put("agentName", "%"+agentSearchVO.getAgentName().toUpperCase()+"%");
			}
			
			pageInfo = this.findPageInfo(countSql.toString(), searchSql.toString(), paramMap, 
					agentSearchVO.getPage(), agentSearchVO.getSize());
		}catch(Throwable e){
			throw new ServiceException(e);
		}
		return pageInfo;
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
			
			
			agentVO = entityToVO(agent);
			//加入日志
			addLog(agent, DomainEvent.USER_ADDED_AGENT.toString());
			
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
			
			agentVO = entityToVO(agent);
			
			
			//加入日志
			addLog(agent, DomainEvent.USER_UPDATED_AGENT.toString());
			
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
			
			
			//加入日志
			addLog(agent, DomainEvent.USER_REMOVED_AGENT.toString());
			
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
			
			
			Agent agent = getEm().find(Agent.class, agentId);
			
			if(agent == null){
				return null;
			}
			
			AgentVO agentVO = entityToVO(agent);
			
			return agentVO;
		}catch(Throwable e){
			throw new ServiceException(e);
		}	
	}

	@Override
	public boolean agentIsExist(String id, String name) throws ServiceException {
		log.trace("calling agentIsExist() agentId={0} agentName={1} ", id, name);
		
		Map<String, Object> param = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		try {
			hql.append(" SELECT COUNT(ag.id) FROM Agent ag WHERE 1=1 ");
			
			if (!Tools.isEmptyString(id)) {
				hql.append(" AND ag.id != :agentId");
				param.put("agentId", id);
			}
			
			if (!Tools.isEmptyString(name)) {
				hql.append(" AND ag.name = :agentName");
				param.put("agentName", name);
			}
			
			log.debug(" agentIsExist SQL : " + hql);
			
			Query query = getEm().createQuery(hql.toString());
			
			if (param.size() > 0) {
				Set<String> key = param.keySet();
				for (String s : key) {
					query.setParameter(s, param.get(s));
				}
			}
			
			Long count = (Long) query.getSingleResult();
			
			if(count > 0){
				return true;
			}else {
				return false;
			}
		}catch(Throwable e){
			throw new ServiceException(e);
		}
	}

	@Override
	public List<AgentVO> findAllAgent() throws ServiceException {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT ag FROM Agent ag");
		
		Query query = getEm().createQuery(hql.toString());
		List<Agent> agentList = query.getResultList();
		
		List<AgentVO> agentVOList = new ArrayList<AgentVO>();
		for(Agent agent:agentList){
			AgentVO agentVO = entityToVO(agent);
			agentVOList.add(agentVO);
		}
		
		return agentVOList;
	}
	
	/**
	 * description：实体转VO
	 * @param agent
	 * @return
	 * @time 2011-9-15   下午07:24:31
	 * @author Seek
	 */
	private AgentVO entityToVO(Agent agent){
		AgentVO agentVO = new AgentVO();
		agentVO.setId(agent.getId());
		agentVO.setName(agent.getName());
		agentVO.setEmail(agent.getEmail());
		return agentVO;
	}
	
	/**
	 * description：VO转实体
	 * @param agentVO
	 * @return
	 * @time 2011-9-15   下午07:25:32
	 * @author Seek
	 */
	private Agent voToEntity(AgentVO agentVO){
		Agent agent = new Agent();
		agent.setId(agentVO.getId());
		agent.setName(agentVO.getName());
		agent.setEmail(agentVO.getEmail());
		return agent;
	}
	
}
