package com.chinarewards.qqgbvpn.main.dao.qqapi;

import java.util.List;

import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.GrouponCache;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.Validation;
import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingValidateResultVO;


public interface GroupBuyingDao {
	
	/**
	 * 分页取商品缓存列表
	 * @author iori
	 * @param pageInfo
	 * @param posId
	 * @return
	 */
	public PageInfo getGrouponCachePagination(PageInfo pageInfo, String posId);
	
	/**
	 * 查询POS机
	 * @author iori
	 * @param posId
	 * @return
	 */
	public Pos getPosByPosId(String posId);
	
	/**
	 * 查询第三方
	 * @author iori
	 * @param posId
	 * @return
	 */
	public Agent getAgentByPosId(String posId);
	
	/**
	 * 删除POS机的缓存
	 * @author iori
	 * @param posId
	 * @return
	 */
	public List<GrouponCache> deleteGrouponCache(String posId);
	
	/**
	 * 保存缓存
	 * @author iori
	 * @param grouponCache
	 */
	public void saveGrouponCache(GrouponCache grouponCache);
	
	/**
	 * 保存验证信息
	 * @author iori
	 * @param validation
	 */
	public void saveValidation(Validation validation);
	
	/**
	 * 本地验证团购
	 * 
	 * @author huangwei
	 * @param grouponId
	 * @param grouponVCode
	 * @return
	 */
	public GroupBuyingValidateResultVO groupBuyingValidateLocal(String grouponId,String grouponVCode)throws SaveDBException,JsonGenerationException;
	
	/**
	 * 在腾讯验证成功时本地保存验证信息
	 * 
	 * @author huangwei
	 * @param grouponId
	 * @param grouponVCode
	 * @param groupBuyingValidateResultVO
	 */
	public void createValidateResultLocal(String grouponId,String grouponVCode,GroupBuyingValidateResultVO groupBuyingValidateResultVO)throws SaveDBException,JsonGenerationException;
	
	/**
	 * 团购验证后Pos机回调
	 * 
	 * @param grouponId
	 * @param grouponVCode
	 * @throws SaveDBException
	 */
	public void groupBuyValidateCallBack(String grouponId,String grouponVCode)throws SaveDBException;
}
