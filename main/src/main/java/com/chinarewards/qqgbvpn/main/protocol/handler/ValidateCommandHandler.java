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
import com.chinarewards.qqgbvpn.main.protocol.cmd.ValidateResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.filter.LoginFilter;
import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingValidateResultVO;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ValidateCommandHandler implements ServiceHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	private final int ERROR_CODE_THER = 999; 
	
	private final int SUCCESS_CODE = 0; 
	
	@Inject
	public Provider<GroupBuyingManager> gbm;
	
	@Inject
	protected Configuration configuration;
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {
		
		ValidateRequestMessage bodyMessage = (ValidateRequestMessage) request.getParameter();
		ServiceSession session = request.getSession();

		log.debug("ValidateCommandHandler======execute==bodyMessage=: {}"+bodyMessage);
		ValidateRequestMessage validateRequestMessage = (ValidateRequestMessage) bodyMessage;
		ValidateResponseMessage validateResponseMessage = new ValidateResponseMessage();

		log.debug("ValidateCommandHandler======execute==posId=: {}"+String.valueOf(session.getAttribute(LoginFilter.POS_ID)));
		
		HashMap<String, String> postParams = new HashMap<String, String>();
		postParams.put("posId", String.valueOf(session.getAttribute(LoginFilter.POS_ID)));
		postParams.put("grouponId", validateRequestMessage.getGrouponId());
		postParams.put("token", validateRequestMessage.getGrouponVCode());
		postParams.put("key", configuration.getString("txserver.key"));
		log.debug("ValidateCommandHandler======execute==key=: {}"+configuration.getString("txserver.key"));

		
		try {
			HashMap<String, Object> result =  gbm.get().groupBuyingValidate(postParams);
			int resultCode = Integer.valueOf((String) result.get("resultCode"));
			log.debug("resultCode=========: {}"+resultCode);
			if (resultCode == SUCCESS_CODE ) {
				List<GroupBuyingValidateResultVO> items = (List<GroupBuyingValidateResultVO>) result
						.get("items");
				validateResponseMessage.setResult(SUCCESS_CODE);
				for (GroupBuyingValidateResultVO item : items) {
					log.debug("item.getResultStatus()=============:"+item.getResultStatus());
					if (!"0".equals(item.getResultStatus())) {
						validateResponseMessage.setResult(1);
					}
					validateResponseMessage.setResultName(item.getResultName());
					validateResponseMessage.setCurrentTime(item.getCurrentTime());
					validateResponseMessage.setResultExplain(item.getResultExplain());
					validateResponseMessage.setUseTime(item.getUseTime());
					validateResponseMessage.setValidTime(item.getValidTime());
				}
			} else {
				switch (resultCode) {
				case -1:
					log.debug("error=========:服务器繁忙");
					validateResponseMessage.setResult(resultCode);
					break;
				case -2:
					log.debug("error=========:md5校验失败");
					validateResponseMessage.setResult(resultCode);
					break;
				case -3:
					log.debug("error=========:没有权限");
					validateResponseMessage.setResult(resultCode);
					break;
				default:
					log.debug("error=========:未知错误");
					validateResponseMessage.setResult(resultCode);
					break;
				}
			}
		} catch (JsonGenerationException e) {
			log.error("error=========:生成JSON对象出错");
			validateResponseMessage.setResult(ERROR_CODE_THER);
		} catch (MD5Exception e) {
			log.error("error=========:生成MD5校验位出错");
			validateResponseMessage.setResult(ERROR_CODE_THER);
		} catch (ParseXMLException e) {
			log.error("error=========:解析XML出错");
			validateResponseMessage.setResult(ERROR_CODE_THER);
		} catch (SendPostTimeOutException e) {
			log.error("error=========:POST连接出错");
			validateResponseMessage.setResult(ERROR_CODE_THER);
		} catch (SaveDBException e) {
			log.error("error=========:后台保存数据库出错 "+e.getMessage());
			validateResponseMessage.setResult(ERROR_CODE_THER);
		}catch (Throwable e) {
			log.error("error========= "+e.getMessage());
			validateResponseMessage.setResult(ERROR_CODE_THER);
		}
		
		validateResponseMessage.setCmdId(CmdConstant.VALIDATE_CMD_ID_RESPONSE);

		log.debug("ValidateCommandHandler======execute==end=:"+validateResponseMessage.getCmdId());
		
		response.writeResponse(validateResponseMessage);
	}

}
