package com.chinarewards.qqgbvpn.mgmtui.struts.agent;

import java.util.List;

import org.apache.struts2.ServletActionContext;
import com.chinarewards.qqgbvpn.mgmtui.logic.agent.AgentLogic;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentStore;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentVO;
import com.chinarewards.qqgbvpn.mgmtui.struts.BasePagingToolBarAction;
import com.chinarewards.qqgbvpn.mgmtui.util.Tools;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * third part manager action
 * 
 * @author Seek	
 *
 */
public class AgentAction extends BasePagingToolBarAction {
	
	private static final long serialVersionUID = 6936323925311944355L;
	
	private String agentId;
	
	private AgentVO agentVO;

	private AgentLogic agentLogic;
	
	private List<AgentVO> agentVOList;

	private AgentSearchVO agentSearchVO = new AgentSearchVO();
	
	@Override
	public String execute(){
		agentSearchVO.setSize(getPageSize());
		
		if(agentSearchVO.getPage() == null){
			agentSearchVO.setPage(getCurrentPage()==0?1:getCurrentPage());
		}
		return SUCCESS;
	}
	
	/**
	 * description：获取列表
	 * @return
	 * @time 2011-9-5   下午07:12:24
	 * @author Seek
	 */
	public String agentList(){
		log.debug("agentAction call list");
		try{		
			AgentStore agentStore = getAgentLogic().queryAgent(agentSearchVO);
			agentVOList = agentStore.getAgentVOList();
			
			
			setCurrentPage(agentSearchVO.getPage());	//当前页
			setCountTotal(agentStore.getCountTotal());	//总数据量
			
			final String urlMark = "{*}";
			String urlTemplate = super.buildURLTemplate(super.getCurrentPath(), "&", "agentSearchVO.page=", urlMark);
			
			setUrlTemplate(urlTemplate);
			setUrlMark(urlMark);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * description：显示添加或修改
	 * @return
	 * @time 2011-9-5   下午04:43:29
	 * @author Seek
	 */
	public String showEditAgent(){
		try {
			if(!Tools.isEmptyString(agentId)){
				agentVO = getAgentLogic().findById(agentId);
			}
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * description：编辑或修改
	 * @return
	 * @time 2011-9-5   下午07:12:46
	 * @author Seek
	 */
	public String editAgent(){
		log.debug("posAction call editPos");
		try {
			if(Tools.isEmptyString(agentVO.getId())){
				getAgentLogic().save(agentVO);
			}else{
				getAgentLogic().update(agentVO);
			}
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * description：删除agent
	 * @return
	 * @time 2011-9-5   下午07:13:06
	 * @author Seek
	 */
	public String deleteAgent(){
		log.debug("posAction call delPos");
		try {
			getAgentLogic().delete(agentId);
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	//---------------------------------------------------//
	
	private AgentLogic getAgentLogic() {
		Injector injector = (Injector)ServletActionContext.getServletContext().getAttribute(Injector.class.getName());
		agentLogic = injector.getInstance(AgentLogic.class);
		return agentLogic;
	}
	
	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public AgentVO getAgentVO() {
		return agentVO;
	}

	public void setAgentVO(AgentVO agentVO) {
		this.agentVO = agentVO;
	}

	public List<AgentVO> getAgentVOList() {
		return agentVOList;
	}

	public void setAgentVOList(List<AgentVO> agentVOList) {
		this.agentVOList = agentVOList;
	}
	
	public AgentSearchVO getAgentSearchVO() {
		return agentSearchVO;
	}

	public void setAgentSearchVO(AgentSearchVO agentSearchVO) {
		this.agentSearchVO = agentSearchVO;
	}

}
