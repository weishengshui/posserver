package com.chinarewards.qqgbvpn.mgmtui.struts.agent;

import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.logic.agent.AgentLogic;
import com.chinarewards.qqgbvpn.mgmtui.logic.pos.PosLogic;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentVO;
import com.chinarewards.qqgbvpn.mgmtui.struts.BasePagingToolBarAction;
import com.chinarewards.qqgbvpn.mgmtui.util.Tools;
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
	
	private boolean agentIsExist;
	
	private PageInfo<AgentVO> pageInfo;
	
	private static final int SIZE = 10;

	@Override
	public String execute(){
		if (pageInfo == null) {
			pageInfo = new PageInfo<AgentVO>();
			pageInfo.setPageId(1);
			pageInfo.setPageSize(SIZE);
		}
		agentSearchVO.setSize(pageInfo.getPageSize());
		agentSearchVO.setPage(pageInfo.getPageId());
		
		try{
			pageInfo = getAgentLogic().queryAgent(agentSearchVO);
		}catch(Throwable e){
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
	 * description：agent是否存在
	 * @return
	 * @time 2011-9-6   上午10:37:24
	 * @author Seek
	 */
	public String agentIsExist(){
		try{
			agentIsExist = getAgentLogic().agentIsExist(agentVO.getId(), agentVO.getName());
			return SUCCESS;
		}catch(Throwable e){
			log.error(e.getMessage(), e);
			return ERROR;
		}
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
			agentVO.setName(agentVO.getName().trim());
			agentVO.setEmail(agentVO.getEmail().trim());
					
			if(Tools.isEmptyString(agentVO.getId())){
				getAgentLogic().save(agentVO);
			}else{
				getAgentLogic().update(agentVO);
			}
			log.debug("agent add success!");
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
		agentLogic = super.getInstance(AgentLogic.class);
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
	
	public boolean isAgentIsExist() {
		return agentIsExist;
	}

	public void setAgentIsExist(boolean agentIsExist) {
		this.agentIsExist = agentIsExist;
	}
	
	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}
	
}
