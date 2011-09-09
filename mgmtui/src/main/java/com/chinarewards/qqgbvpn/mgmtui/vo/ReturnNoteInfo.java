package com.chinarewards.qqgbvpn.mgmtui.vo;

import java.io.Serializable;
import java.util.List;

import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.ReturnNote;
import com.chinarewards.qqgbvpn.domain.ReturnNoteDetail;

public class ReturnNoteInfo implements Serializable {

	private static final long serialVersionUID = -6240147909286741566L;
	
	private ReturnNote rn;
	
	private Agent agent;
	
	private List<ReturnNoteDetail> rnDetailList;

	public ReturnNote getRn() {
		return rn;
	}

	public void setRn(ReturnNote rn) {
		this.rn = rn;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public List<ReturnNoteDetail> getRnDetailList() {
		return rnDetailList;
	}

	public void setRnDetailList(List<ReturnNoteDetail> rnDetailList) {
		this.rnDetailList = rnDetailList;
	}

}
