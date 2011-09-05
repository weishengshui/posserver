package com.chinarewards.qqgbvpn.mgmtui.dao;

import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.ReturnNote;
import com.chinarewards.qqgbvpn.mgmtui.exception.SaveDBException;


public interface GroupBuyingUnbindDao {
	
	public void handleGroupBuyingUnbind(HashMap<String, Object> params) throws SaveDBException, JsonGenerationException;
	
	public PageInfo getPosByAgentId(PageInfo pageInfo, String agentId);
	
	public Agent getAgentByName(String agentName);
	
	public List<Pos> getPosByPosInfo(String info);
	
	public ReturnNote createReturnNoteByAgentId(String agentId) throws JsonGenerationException,SaveDBException;
	
	public ReturnNote confirmReturnNote(String agentId,String rnId,String posIds) throws JsonGenerationException,SaveDBException;
	
	public Agent getAgentByRnId(String rnId);
}
