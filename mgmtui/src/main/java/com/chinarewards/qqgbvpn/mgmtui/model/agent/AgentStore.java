package com.chinarewards.qqgbvpn.mgmtui.model.agent;

import java.util.ArrayList;
import java.util.List;

public class AgentStore implements java.io.Serializable {
	
	private static final long serialVersionUID = -395898425576959974L;

	//数据列表
	private List<AgentVO> agentVOList = new ArrayList<AgentVO>();
	
	//数据总量
	private int countTotal;

	public List<AgentVO> getAgentVOList() {
		return agentVOList;
	}

	public void setAgentVOList(List<AgentVO> agentVOList) {
		this.agentVOList = agentVOList;
	}

	public int getCountTotal() {
		return countTotal;
	}

	public void setCountTotal(int countTotal) {
		this.countTotal = countTotal;
	}
	
}
