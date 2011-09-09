package com.chinarewards.qqgbvpn.mgmtui.logic.impl;

import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.ReturnNote;
import com.chinarewards.qqgbvpn.mgmtui.dao.GroupBuyingUnbindDao;
import com.chinarewards.qqgbvpn.mgmtui.exception.SaveDBException;
import com.chinarewards.qqgbvpn.mgmtui.exception.UnUseableRNException;
import com.chinarewards.qqgbvpn.mgmtui.logic.GroupBuyingUnbindManager;
import com.chinarewards.qqgbvpn.mgmtui.vo.ReturnNoteInfo;
import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;
import com.chinarewards.qqgbvpn.qqapi.service.GroupBuyingService;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

public class GroupBuyingUnbindManagerImpl implements GroupBuyingUnbindManager {
	
	@Inject   
	private Provider<GroupBuyingService> service;
	
	@Inject
	private Provider<GroupBuyingUnbindDao> dao;
	
	
	/**
	 * 团购取消绑定
	 * 
	 * @author iori
	 * @param params
	 * @return
	 * @throws SendPostTimeOutException 
	 * @throws ParseXMLException 
	 * @throws MD5Exception 
	 * @throws SaveDBException 
	 * @throws JsonGenerationException 
	 */
	@Transactional
	public HashMap<String, Object> groupBuyingUnbind(
			HashMap<String, Object> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException, SaveDBException {
		HashMap<String, Object> map = service.get().groupBuyingUnbind(params);
		map.putAll(params);
		dao.get().handleGroupBuyingUnbind(map);
		return map;
	}
	
	/**
	 * 根据第三方ID分页查询POS机列表
	 * @param pageInfo
	 * @param agentId
	 * @return
	 */
	public PageInfo getPosByAgentId(PageInfo pageInfo, String agentId) {
		return dao.get().getPosByAgentId(pageInfo, agentId);
	}
	
	/**
	 * 根据第三方名称查询第三方
	 * @param agentName
	 * @return
	 */
	public Agent getAgentByName(String agentName) {
		return dao.get().getAgentByName(agentName);
	}
	
	public List<Agent> getAgentLikeName(String agentName) {
		return dao.get().getAgentLikeName(agentName);
	}
	
	/**
	 * 根据posId、sn、simPhoneNo查询相应POS机
	 * @param info
	 * @return
	 */
	public List<Pos> getPosByPosInfo(String info) {
		return dao.get().getPosByPosInfo(info);
	}
	
	/**
	 * 根据第三方ID生成空回收单
	 * @param agentId
	 * @return
	 * @throws JsonGenerationException
	 * @throws SaveDBException
	 */
	@Transactional
	public ReturnNote createReturnNoteByAgentId(String agentId) throws JsonGenerationException,SaveDBException {
		return dao.get().createReturnNoteByAgentId(agentId);
	}
	
	/**
	 * 确认回收单
	 * @param agentId
	 * @param inviteCode
	 * @param posList
	 * @return
	 * @throws JsonGenerationException
	 * @throws SaveDBException
	 */
	@Transactional
	public ReturnNote confirmReturnNote(String agentId,String inviteCode,List<String> posIds) throws SaveDBException,UnUseableRNException {
		return dao.get().confirmReturnNote(agentId, inviteCode, posIds);
	}
	
	public Agent getAgentByRnId(String rnId) {
		return dao.get().getAgentByRnId(rnId);
	}
	
	@Transactional
	public String createInviteCode(String agentId) {
		return dao.get().createInviteCode(agentId);
	}
    
	public Agent getAgentByInviteCode(String inviteCode) {
		return dao.get().getAgentByInviteCode(inviteCode);
	}
	
	public PageInfo getReturnNoteLikeRnNumber(String rnNumber, PageInfo pageInfo) {
		return dao.get().getReturnNoteLikeRnNumber(rnNumber, pageInfo);
	}
	
	public ReturnNoteInfo getReturnNoteInfoByRnId(String rnId) {
		return dao.get().getReturnNoteInfoByRnId(rnId);
	}
	
	@Transactional
	public ReturnNoteInfo confirmAllReturnNote(String agentId) throws SaveDBException {
		return dao.get().confirmAllReturnNote(agentId);
	}
}
