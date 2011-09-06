package com.chinarewards.qqgbvpn.mgmtui.logic.agent.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.mgmtui.dao.agent.AgentDao;
import com.chinarewards.qqgbvpn.mgmtui.exception.ServiceException;
import com.chinarewards.qqgbvpn.mgmtui.logic.agent.AgentLogic;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentStore;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentVO;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

/**
 * description：third part manage
 * @author Seek
 * 
 */
public class AgentLogicImpl implements AgentLogic {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Inject
	Provider<AgentDao> agentDao;
	
	@Override
	public AgentStore queryAgent(AgentSearchVO agentSearchVO)
			throws ServiceException {
		try{
			return agentDao.get().queryAgent(agentSearchVO);
		}catch(Throwable e){
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	@Transactional
	@Override
	public AgentVO save(AgentVO agentVO) throws ServiceException {
		try{
			return agentDao.get().save(agentVO);
		}catch(Throwable e){
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Transactional
	@Override
	public AgentVO update(AgentVO agentVO) throws ServiceException {
		try{
			return agentDao.get().update(agentVO);
		}catch(Throwable e){
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	@Transactional
	@Override
	public void delete(String agentId) throws ServiceException {
		try{
			agentDao.get().delete(agentId);
		}catch(Throwable e){
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public AgentVO findById(String agentId) throws ServiceException {
		try{
			return agentDao.get().findById(agentId);
		}catch(Throwable e){
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	public boolean agentIsExist(String id, String name) throws ServiceException {
		try{
			return agentDao.get().agentIsExist(id, name);
		}catch(Throwable e){
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
}