package com.chinarewards.qqgbvpn.mgmtui.struts.action;

import java.util.ArrayList;
import java.util.List;

import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.mgmtui.struts.BaseAction;

/**
 * pos unbind action
 * 
 * @author iori
 *
 */
public class UnbindAction extends BaseAction {

	private static final long serialVersionUID = -4872248136823406437L;
	
	//@Inject
	//private Provider<GroupBuyingUnbindManager> groupBuyingUnbindMgr;

	String agentName;
	List<Agent> agentList;
	
	public List<Agent> getAgentList() {
		return agentList;
	}

	public void setAgentList(List<Agent> agentList) {
		this.agentList = agentList;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	@Override
	public String execute(){
		return SUCCESS;
	}
	
	public String unbind(){
		return SUCCESS;
	}
	
	public String sendURL() {
		System.out.println(agentName);
		if ("a1".equals(agentName)) {
			agentList = new ArrayList<Agent>();
			Agent a1 = new Agent();
			a1.setId("id1");
			a1.setName("a1");
			Agent a2 = new Agent();
			a2.setId("id2");
			a2.setName("a2");
			agentList.add(a1);
			agentList.add(a2);
		}
		return SUCCESS;
	}

}
