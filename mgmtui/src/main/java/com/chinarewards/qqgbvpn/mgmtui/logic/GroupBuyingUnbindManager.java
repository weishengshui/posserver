package com.chinarewards.qqgbvpn.mgmtui.logic;

import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.ReturnNote;
import com.chinarewards.qqgbvpn.mgmtui.exception.SaveDBException;
import com.chinarewards.qqgbvpn.mgmtui.exception.UnUseableRNException;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosVO;
import com.chinarewards.qqgbvpn.mgmtui.vo.ReturnNoteInfo;
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
			HashMap<String, Object> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException, SaveDBException;
	
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
	 * 根据第三方名称查询第三方(模糊)
	 * @param agentName
	 * @return
	 */
	public List<Agent> getAgentLikeName(String agentName);
	
	/**
	 * 根据posId、sn、simPhoneNo查询相应POS机
	 * @param info
	 * @return
	 */
	public List<Pos> getPosByPosInfo(String info);
	
	/**
	 * 根据posId、sn、simPhoneNo查询相应POS机信息
	 * @param info
	 * @return
	 */
	public List<PosVO> getPosVOByPosInfo(String info);
	
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
	 * @param inviteCode
	 * @param posList
	 * @return
	 * @throws JsonGenerationException
	 * @throws SaveDBException
	 */
	public ReturnNote confirmReturnNote(String agentId,String inviteCode,List<String> posIds) throws SaveDBException,UnUseableRNException;
	
	/**
	 * 根据回收单ID查询第三方
	 * @param rnId
	 * @return
	 */
	public Agent getAgentByRnId(String rnId);
	
	/**
	 * 生成邀请号
	 * @return
	 */
	public String createInviteCode(String agentId);
	
	/**
	 * 根据邀请号查询第三方
	 * @param rnId
	 * @return
	 */
	public Agent getAgentByInviteCode(String inviteCode);
	
	/**
	 * 根据回收单号查询回收单列表（模糊）
	 * @param rnNumber
	 * @param pageInfo
	 * @return
	 */
	public PageInfo getReturnNoteLikeRnNumber(String rnNumber, String status, PageInfo pageInfo);
	
	/**
	 * 根据回收单ID查询回收单具体信息
	 * @param rnNumber
	 * @param pageInfo
	 * @return
	 */
	public ReturnNoteInfo getReturnNoteInfoByRnId(String rnId);
	
	/**
	 * 全部回收
	 * @param agentId
	 * @return
	 * @throws SaveDBException
	 */
	public ReturnNoteInfo confirmAllReturnNote(String agentId) throws SaveDBException;
	
}
