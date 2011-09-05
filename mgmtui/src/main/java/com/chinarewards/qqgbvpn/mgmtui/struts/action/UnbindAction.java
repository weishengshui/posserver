package com.chinarewards.qqgbvpn.mgmtui.struts.action;

import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.ReturnNote;
import com.chinarewards.qqgbvpn.mgmtui.exception.SaveDBException;
import com.chinarewards.qqgbvpn.mgmtui.logic.GroupBuyingUnbindManager;
import com.chinarewards.qqgbvpn.mgmtui.struts.BaseAction;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * pos unbind action
 * 
 * @author iori
 *
 */
public class UnbindAction extends BaseAction {

	private static final long serialVersionUID = -4872248136823406437L;
	
	@Inject
	private Provider<GroupBuyingUnbindManager> groupBuyingUnbindMgr;

	private Agent agent;
	
	private PageInfo pageInfo;
	
	private String posIds;
	
	private String rnId;
	
	public String getRnId() {
		return rnId;
	}

	public void setRnId(String rnId) {
		this.rnId = rnId;
	}

	public String getPosIds() {
		return posIds;
	}

	public void setPosIds(String posIds) {
		this.posIds = posIds;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	
	@Override
	public String execute() {
		agent = new Agent();
		pageInfo = new PageInfo();
		pageInfo.setPageId(1);
		pageInfo.setPageSize(10);
		return SUCCESS;
	}

	public String search() {
		if (agent.getName() != null && !"".equals(agent.getName().trim())) {
			Agent a = groupBuyingUnbindMgr.get().getAgentByName(agent.getName().trim());
			if (a != null) {
				pageInfo.setPageId(1);
				pageInfo.setPageSize(10);
				pageInfo = groupBuyingUnbindMgr.get().getPosByAgentId(pageInfo, agent.getId());
				this.setAgent(a);
			} else {
				//这里应该报找不到的提示
				System.out.println("!!!!!!!!!!!agent 为空!!");
			}
		}
		return SUCCESS;
	}
	
	public String createRnNumber() throws JsonGenerationException, SaveDBException{
		if (agent.getId() != null && !"".equals(agent.getId().trim())) {
			ReturnNote rn = groupBuyingUnbindMgr.get().createReturnNoteByAgentId(agent.getId());
		} else {
			//这里应该报第三方不能为空的提示
			System.out.println("!!!!!!!!!!!agent.getId(): 为空!!");
		}
		return SUCCESS;
	}
	
	public String confirmRnNumber() throws JsonGenerationException, SaveDBException{
		if (posIds != null && !"".equals(posIds.trim())) {
			System.out.println("!!!!!!!!!!!posIds: " + posIds);
			ReturnNote rn = groupBuyingUnbindMgr.get().confirmReturnNote(agent.getId(), rnId, posIds);
		} else {
			//这里应该报POS机不能为空的提示
			System.out.println("!!!!!!!!!!!posIds: 为空!!");
		}
		return SUCCESS;
	}
	
	public String unbind(){
		return SUCCESS;
	}
	
	public String sendURL() {
		return SUCCESS;
	}

}
