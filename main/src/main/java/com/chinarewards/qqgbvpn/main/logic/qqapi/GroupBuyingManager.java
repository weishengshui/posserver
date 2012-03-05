package com.chinarewards.qqgbvpn.main.logic.qqapi;

import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.main.exception.CopyPropertiesException;
import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.main.vo.ValidateResponseMessageVO;
import com.chinarewards.qqgbvpn.main.vo.ValidationVO;
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
	
	/**
	 * 根据pcode和vcode查询最后一次的验证信息
	 * 
	 * @author huangwei
	 * @param grouponId
	 * @param grouponVCode
	 * @return
	 */
	public ValidationVO getValidationByPcodeVcodeLastTs(String pcode,String vcode)throws SaveDBException,JsonGenerationException;
	
	/**
	 * 根据pcode和vcode查询第一次的验证信息
	 * 
	 * @author huangwei
	 * @param grouponId
	 * @param grouponVCode
	 * @return
	 */
	public ValidationVO getValidationByPcodeVcodeFirstTs(String pcode,String vcode)throws SaveDBException,JsonGenerationException;
	
	
	/**
	 * 根据pcode和vcode查询验证过多少次
	 * 
	 * @author huangwei
	 * @param grouponId
	 * @param grouponVCode
	 * @return
	 */
	public int getValidationCountByPcodeVcode(String pcode,String vcode)throws SaveDBException,JsonGenerationException;
	
	/**
	 * 保存一条验证记录
	 * @param posId
	 * @param validationVo
	 * @throws SaveDBException
	 * @throws JsonGenerationException
	 */
	public void createValidation(String posId, ValidationVO validationVo)throws SaveDBException,JsonGenerationException;
	
	/**
	 * QQ团购验证业务的验证请求业务处理
	 * @param HashMap<String, String> postParams
	 * @return ValidateResponseMessageVO
	 */
	public ValidateResponseMessageVO qqgbvValidationCommand(HashMap<String, String> postParams)throws SaveDBException,JsonGenerationException;
}
