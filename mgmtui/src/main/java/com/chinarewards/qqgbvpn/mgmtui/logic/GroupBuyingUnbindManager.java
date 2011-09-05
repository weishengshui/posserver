package com.chinarewards.qqgbvpn.mgmtui.logic;

import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.ReturnNote;
import com.chinarewards.qqgbvpn.mgmtui.exception.SaveDBException;
import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;

public interface GroupBuyingUnbindManager {
	
	/**
	 * 团购取消绑定
	 * 
	 * @author iori
	 * @param params
	 * @return
	 */
	public HashMap<String, Object> groupBuyingUnbind(
			HashMap<String, Object> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException, JsonGenerationException, SaveDBException;
	
	/**
	 * 根据第三方ID分页查询POS机列表
	 * @param pageInfo
	 * @param agentId
	 * @return
	 */
	public PageInfo getPosByAgentId(PageInfo pageInfo, String agentId);
	
	/**
	 * 根据第三方名称查询第三方
	 * @param agentName
	 * @return
	 */
	public Agent getAgentByName(String agentName);
	
	/**
	 * 根据posId、sn、simPhoneNo查询相应POS机
	 * @param info
	 * @return
	 */
	public List<Pos> getPosByPosInfo(String info);
	
	/**
	 * 根据第三方ID生成空回收单
	 * @param agentId
	 * @return
	 * @throws JsonGenerationException
	 * @throws SaveDBException
	 */
	public ReturnNote createReturnNoteByAgentId(String agentId) throws JsonGenerationException,SaveDBException;
	
	/**
	 * 确认回收单
	 * @param agentId
	 * @param rnId
	 * @param posList
	 * @return
	 * @throws JsonGenerationException
	 * @throws SaveDBException
	 */
	public ReturnNote confirmReturnNote(String agentId,String rnId,String posIds) throws JsonGenerationException,SaveDBException;
	
	/**
	 * 根据回收单ID查询第三方
	 * @param rnId
	 * @return
	 */
	public Agent getAgentByRnId(String rnId);
}
