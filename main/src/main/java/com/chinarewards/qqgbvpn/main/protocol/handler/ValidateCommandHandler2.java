package com.chinarewards.qqgbvpn.main.protocol.handler;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.codehaus.jackson.JsonGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;
import com.chinarewards.qqgbvpn.main.protocol.ServiceResponse;
import com.chinarewards.qqgbvpn.main.protocol.ServiceSession;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ValidateRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ValidateResponseMessage2;
import com.chinarewards.qqgbvpn.main.protocol.filter.LoginFilter;
import com.chinarewards.qqgbvpn.main.vo.ValidationVO;
import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingValidateResultVO;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * 这个Handler2是为了处理重复打印多次小票这个问题新加的一个指令
 * 因为旧的的POS机使用的是Handler
 * 所以新的POS机使用的是Handler2
 * @author Harry
 *	2012-02-24
 */
public class ValidateCommandHandler2 implements ServiceHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	private final int ERROR_CODE_THER = 999; 
	
	private final int SUCCESS_CODE = 0; 
	
	private final int SIGNEDINTMAXVALUE  = 32767; 
	
	@Inject
	public Provider<GroupBuyingManager> gbm;
	
	@Inject
	protected Configuration configuration;
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {
		
		ValidateRequestMessage bodyMessage = (ValidateRequestMessage) request.getParameter();
		ServiceSession session = request.getSession();

		log.debug("ValidateCommandHandler2 bodyMessage=: {}"+bodyMessage);
		ValidateRequestMessage validateRequestMessage = (ValidateRequestMessage) bodyMessage;
		ValidateResponseMessage2 validateResponseMessage = new ValidateResponseMessage2();

		log.debug("ValidateCommandHandler2 posId=: {}"+String.valueOf(session.getAttribute(LoginFilter.POS_ID)));
		
		HashMap<String, String> postParams = new HashMap<String, String>();
		postParams.put("posId", String.valueOf(session.getAttribute(LoginFilter.POS_ID)));
		postParams.put("grouponId", validateRequestMessage.getGrouponId());
		postParams.put("token", validateRequestMessage.getGrouponVCode());
		postParams.put("key", configuration.getString("txserver.key"));
		log.debug("ValidateCommandHandler2 key=: {}"+configuration.getString("txserver.key"));

		
		try {
			//验证首先检查是否通过本地验证（就是验证结果表GroupBuyingValidateResult里面是否还存在同一个团购同一个验证码的数据）
			GroupBuyingValidateResultVO groupBuyingValidateResultVO = gbm.get().groupBuyingValidateLocal(validateRequestMessage.getGrouponId(), validateRequestMessage.getGrouponVCode());
			//如果存在一条需要验证的数据说明，上一次验证失败（失败的原因有：可能是验证请求回复POS机失败，或者验证后的ACK请求失败）
			if(groupBuyingValidateResultVO != null){
				log.debug("validate to dup.....");
				//从validate表里面获取上一次验证和第一次验证这个团购的数据然后回复给POS机
				ValidationVO lastValidationVo = gbm.get().getValidationByPcodeVcodeLastTs(validateRequestMessage.getGrouponId(), validateRequestMessage.getGrouponVCode());
				ValidationVO firstValidationVo = gbm.get().getValidationByPcodeVcodeFirstTs(validateRequestMessage.getGrouponId(), validateRequestMessage.getGrouponVCode());
				//查询在该次验证前验证了多少次
				int validateCount = gbm.get().getValidationCountByPcodeVcode(validateRequestMessage.getGrouponId(), validateRequestMessage.getGrouponVCode());
				//因为要得到当前的验证次数，所以要加上1
				int currCount = validateCount + 1;
				log.debug("validate count:{}",currCount);
				if( currCount > SIGNEDINTMAXVALUE){
					currCount = SIGNEDINTMAXVALUE;
				}
				validateResponseMessage.setValidate_count(currCount);
				validateResponseMessage.setPrev_posId(lastValidationVo.getPosId());
				validateResponseMessage.setFirst_posId(firstValidationVo.getPosId());
				validateResponseMessage.setQqws_resultcode(0);
				validateResponseMessage.setQqvalidate_resultstatus(Long.parseLong(firstValidationVo.getResultStatus()));
				validateResponseMessage.setResultName(groupBuyingValidateResultVO.getResultName());
				validateResponseMessage.setCurrentTime(groupBuyingValidateResultVO.getCurrentTime());
				validateResponseMessage.setResultExplain(groupBuyingValidateResultVO.getResultExplain());
				validateResponseMessage.setUseTime(groupBuyingValidateResultVO.getUseTime());
				validateResponseMessage.setValidTime(groupBuyingValidateResultVO.getValidTime());
				validateResponseMessage.setPrev_validate_time(lastValidationVo.getTs());
				validateResponseMessage.setFirst_validate_time(firstValidationVo.getTs());
				
				//每一次验证都要插入一条验证的数据，作为历史记录
				gbm.get().createValidation(String.valueOf(session.getAttribute(LoginFilter.POS_ID)), lastValidationVo);
			}
			//这个团购这个验证码第一次验证，请求腾讯服务器验证。
			else{
				log.debug("ValidateCommandHandler to tx...");
				HashMap<String, Object> result =  gbm.get().groupBuyingValidate(postParams);
				int resultCode = Integer.valueOf((String) result.get("resultCode"));
				log.debug("resultCode: {}", resultCode);
				if (resultCode == SUCCESS_CODE ) {
					List<GroupBuyingValidateResultVO> items = (List<GroupBuyingValidateResultVO>) result
							.get("items");
					
					validateResponseMessage.setQqws_resultcode(0);
					validateResponseMessage.setQqvalidate_resultstatus(0);
					
					for (GroupBuyingValidateResultVO item : items) {
						log.debug("item.getResultStatus(): [{}]", item.getResultStatus());
						//腾讯那边说resultStatus非0是验证通过
						if (!"0".equals(item.getResultStatus())) {
							validateResponseMessage.setQqvalidate_resultstatus(Long.parseLong(item.getResultStatus()));
						}else{
							//创建本地验证记录用来备份，如果这次验证失败可以第二次验证时不用访问腾讯服务器，这条记录会在某一次验证成功后的ACK请求中删除
							log.debug("ValidateCommandHandler create groupBuyingValidateResult");
							gbm.get().createValidateResultLocal(validateRequestMessage.getGrouponId(), validateRequestMessage.getGrouponVCode(), item);
						}
						
						ValidationVO firstValidationVo = gbm.get().getValidationByPcodeVcodeFirstTs(validateRequestMessage.getGrouponId(), validateRequestMessage.getGrouponVCode());
						
						validateResponseMessage.setValidate_count(1);
						validateResponseMessage.setResultName(item.getResultName());
						validateResponseMessage.setCurrentTime(item.getCurrentTime());
						validateResponseMessage.setResultExplain(item.getResultExplain());
						validateResponseMessage.setUseTime(item.getUseTime());
						validateResponseMessage.setValidTime(item.getValidTime());
						validateResponseMessage.setPrev_posId(null);
						validateResponseMessage.setFirst_posId(String.valueOf(session.getAttribute(LoginFilter.POS_ID)));
						validateResponseMessage.setPrev_validate_time(null);
						validateResponseMessage.setFirst_validate_time(firstValidationVo.getTs());
					}
				} else {
					switch (resultCode) {
					case -1:
						log.debug("error=========:服务器繁忙");
						validateResponseMessage.setQqws_resultcode(resultCode);
						break;
					case -2:
						log.debug("error=========:md5校验失败");
						validateResponseMessage.setQqws_resultcode(resultCode);
						break;
					case -3:
						log.debug("error=========:没有权限");
						validateResponseMessage.setQqws_resultcode(resultCode);
						break;
					default:
						log.debug("error=========:未知错误");
						validateResponseMessage.setQqws_resultcode(resultCode);
						break;
					}
				}
			}
			
		} catch (JsonGenerationException e) {
			log.error("error=========:生成JSON对象出错", e);
			validateResponseMessage.setQqws_resultcode(ERROR_CODE_THER);
		} catch (MD5Exception e) {
			log.error("error=========:生成MD5校验位出错", e);
			validateResponseMessage.setQqws_resultcode(ERROR_CODE_THER);
		} catch (ParseXMLException e) {
			log.error("error=========:解析XML出错", e);
			validateResponseMessage.setQqws_resultcode(ERROR_CODE_THER);
		} catch (SendPostTimeOutException e) {
			log.error("error=========:POST连接出错", e);
			validateResponseMessage.setQqws_resultcode(ERROR_CODE_THER);
		} catch (SaveDBException e) {
			log.error("error=========:后台保存数据库出错 ", e);
			validateResponseMessage.setQqws_resultcode(ERROR_CODE_THER);
		}catch (Throwable e) {
			log.error("error=========:", e);
			validateResponseMessage.setQqws_resultcode(ERROR_CODE_THER);
		}
		
		validateResponseMessage.setCmdId(CmdConstant.VALIDATE_2_CMD_ID_RESPONSE);

		log.debug("ValidateCommandHandler2 end:"+validateResponseMessage.getCmdId());
		
		response.writeResponse(validateResponseMessage);
	}

}
