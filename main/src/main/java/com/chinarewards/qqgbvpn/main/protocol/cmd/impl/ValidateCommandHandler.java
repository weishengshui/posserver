package com.chinarewards.qqgbvpn.main.protocol.cmd.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.codehaus.jackson.JsonGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.config.PosNetworkProperties;
import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.main.logic.qqapi.GroupBuyingManager;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CommandHandler;
import com.chinarewards.qqgbvpn.main.protocol.filter.LoginFilter;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.IBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.ValidateRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.ValidateResponseMessage;
import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingValidateResultVO;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ValidateCommandHandler implements CommandHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	private final int ERROR_CODE_THER = 1; 
	
	private final int SUCCESS_CODE = 0; 
	
	@Inject
	public Provider<GroupBuyingManager> gbm;
	
	@SuppressWarnings("unchecked")
	@Override
	public IBodyMessage execute(IoSession session, IBodyMessage bodyMessage) {
		
		log.debug("ValidateCommandHandler======execute==bodyMessage=:"+bodyMessage);
		ValidateRequestMessage validateRequestMessage = (ValidateRequestMessage) bodyMessage;
		ValidateResponseMessage validateResponseMessage = new ValidateResponseMessage();

		log.debug("ValidateCommandHandler======execute==posId=:"+String.valueOf(session.getAttribute(LoginFilter.POS_ID)));
		
		HashMap<String, String> postParams = new HashMap<String, String>();
		postParams.put("posId", String.valueOf(session.getAttribute(LoginFilter.POS_ID)));
		postParams.put("grouponId", validateRequestMessage.getGrouponId());
		postParams.put("token", validateRequestMessage.getGrouponVCode());
		postParams.put("key", new PosNetworkProperties().getTxServerKey());
		log.debug("ValidateCommandHandler======execute==key=:"+new PosNetworkProperties().getTxServerKey());

		
		try {
			HashMap<String, Object> result =  gbm.get().groupBuyingValidate(postParams);
			int resultCode = Integer.valueOf((String) result.get("resultCode"));
			log.debug("resultCode=========:"+resultCode);
			if (resultCode == SUCCESS_CODE ) {
				List<GroupBuyingValidateResultVO> items = (List<GroupBuyingValidateResultVO>) result
						.get("items");
				for (GroupBuyingValidateResultVO item : items) {
					validateResponseMessage.setResultName(item.getResultName());
					validateResponseMessage.setCurrentTime(item.getCurrentTime());
					validateResponseMessage.setResultExplain(item.getResultExplain());
					validateResponseMessage.setUseTime(item.getUseTime());
					validateResponseMessage.setValidTime(item.getValidTime());
				}

				validateResponseMessage.setResult(SUCCESS_CODE);
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
			log.debug("error=========:生成JSON对象出错");
			validateResponseMessage.setResult(ERROR_CODE_THER);
		} catch (MD5Exception e) {
			log.debug("error=========:生成MD5校验位出错");
			validateResponseMessage.setResult(ERROR_CODE_THER);
		} catch (ParseXMLException e) {
			log.debug("error=========:解析XML出错");
			validateResponseMessage.setResult(ERROR_CODE_THER);
		} catch (SendPostTimeOutException e) {
			log.debug("error=========:POST连接出错");
			validateResponseMessage.setResult(ERROR_CODE_THER);
		} catch (SaveDBException e) {
			log.debug("error=========:后台保存数据库出错 "+e.getMessage());
			validateResponseMessage.setResult(ERROR_CODE_THER);
		}catch (Throwable e) {
			log.debug("error========= "+e.getMessage());
			validateResponseMessage.setResult(ERROR_CODE_THER);
		}
		
		validateResponseMessage.setCmdId(CmdConstant.VALIDATE_CMD_ID_RESPONSE);

		log.debug("ValidateCommandHandler======execute==end=:"+validateResponseMessage.getCmdId());
		return validateResponseMessage;
	}

}
