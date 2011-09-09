package com.chinarewards.qqgbvpn.main.dao.qqapi;

import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.main.exception.CopyPropertiesException;
import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingValidateResultVO;


public interface GroupBuyingDao {
	
	public void initGrouponCache(HashMap<String, Object> params) throws SaveDBException, JsonGenerationException, CopyPropertiesException;

	public HashMap<String, Object> handleGroupBuyingSearch(HashMap<String, String> params) throws SaveDBException, JsonGenerationException;
	
	public PageInfo getGrouponCachePagination(PageInfo pageInfo, String posId);
	
	public void handleGroupBuyingValidate(HashMap<String, Object> params) throws SaveDBException;
	
	/*public void handleGroupBuyingUnbind(HashMap<String, Object> params) throws SaveDBException, JsonGenerationException;*/
	
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
