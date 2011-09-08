package com.chinarewards.qqgbvpn.main.logic.qqapi;

import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.main.exception.CopyPropertiesException;
import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingValidateResultVO;

public interface GroupBuyingManager {
	
	/**
	 * 初始化团购商品缓存
	 * 
	 * @author iori
	 * @param params
	 * @return
	 */
	public String initGrouponCache(
			HashMap<String, String> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException, JsonGenerationException, SaveDBException, CopyPropertiesException;

	/**
	 * 团购查询
	 * 
	 * @author iori
	 * @param params
	 * @return
	 */
	public HashMap<String, Object> groupBuyingSearch(
			HashMap<String, String> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException, JsonGenerationException, SaveDBException;

	/**
	 * 团购验证
	 * 
	 * @author iori
	 * @param params
	 * @return
	 */
	public HashMap<String, Object> groupBuyingValidate(
			HashMap<String, String> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException, JsonGenerationException, SaveDBException;

	/**
	 * 团购取消绑定
	 * 
	 * @author iori
	 * @param params
	 * @return
	 */
	/*public HashMap<String, Object> groupBuyingUnbind(
			HashMap<String, Object> params) throws MD5Exception, ParseXMLException, SendPostTimeOutException, JsonGenerationException, SaveDBException;*/
	
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
