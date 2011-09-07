package com.chinarewards.qqgbvpn.mgmtui.dao;

import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.ReturnNote;
import com.chinarewards.qqgbvpn.mgmtui.exception.SaveDBException;
import com.chinarewards.qqgbvpn.mgmtui.exception.UnUseableRNException;


public interface GroupBuyingUnbindDao {
	
	public void handleGroupBuyingUnbind(HashMap<String, Object> params) throws SaveDBException;
	
	public PageInfo getPosByAgentId(PageInfo pageInfo, String agentId);
	
	public Agent getAgentByName(String agentName);
	
	public List<Agent> getAgentLikeName(String agentName);
	
	public List<Pos> getPosByPosInfo(String info);
	
	public ReturnNote createReturnNoteByAgentId(String agentId) throws JsonGenerationException,SaveDBException;
	
	public ReturnNote confirmReturnNote(String agentId,String inviteCode,List<String> posIds) throws SaveDBException,UnUseableRNException;
	
	public Agent getAgentByRnId(String rnId);
	
	public String createInviteCode(String agentId);
	
	public Agent getAgentByInviteCode(String inviteCode);
}
