package com.chinarewards.qqgbvpn.mgmtui.logic.agent;

import com.chinarewards.qqgbvpn.mgmtui.exception.ServiceException;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentStore;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentVO;

/**
 * description：third part manage
 * @author Seek
 * 
 */
public interface AgentLogic {

	/**
	 * description：查询第三方
	 * @param agentSearchVO
	 * @param object
	 * @return
	 * @throws ServiceException
	 * @time 2011-9-5   下午02:29:32
	 * @author Seek
	 */
	AgentStore queryAgent(AgentSearchVO agentSearchVO) throws ServiceException;

	/**
	 * description：保存
	 * @param agentVO
	 * @throws ServiceException
	 * @time 2011-9-5   下午03:34:25
	 * @author Seek
	 */
	AgentVO save(AgentVO agentVO) throws ServiceException;

	/**
	 * description：更新
	 * @param agentVO
	 * @throws ServiceException
	 * @time 2011-9-5   下午03:46:40
	 * @author Seek
	 */
	AgentVO update(AgentVO agentVO) throws ServiceException;

	/**
	 * description：删除
	 * @param agentId
	 * @throws ServiceException
	 * @time 2011-9-5   下午04:01:34
	 * @author Seek
	 */
	void delete(String agentId) throws ServiceException;

	/**
	 * description：根据ID找实体
	 * @param id
	 * @return
	 * @throws ServiceException
	 * @time 2011-9-5   下午05:41:28
	 * @author Seek
	 */
	AgentVO findById(String id) throws ServiceException;

	/**
	 * description：agent是否存在
	 * @param id 	agentId
	 * @param name 	agentName
	 * @return {true存在    false不存在}
	 * @throws ServiceException
	 * @time 2011-9-6   上午09:52:26
	 * @author Seek
	 */
	boolean agentIsExist(String id, String name) throws ServiceException;
	
}