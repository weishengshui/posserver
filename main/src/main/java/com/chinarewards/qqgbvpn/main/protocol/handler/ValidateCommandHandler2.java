package com.chinarewards.qqgbvpn.main.protocol.handler;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.codehaus.jackson.JsonGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.domain.status.ValidationStatus;
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
import com.chinarewards.qqgbvpn.main.vo.ValidateResponseMessageVO;
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


	
	@Inject
	public Provider<GroupBuyingManager> gbm;
	
	@Inject
	protected Configuration configuration;
	
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
			ValidateResponseMessageVO vo = gbm.get().qqgbvValidationCommand(postParams);
			
			validateResponseMessage.setValidate_count(vo.getValidate_count());
			validateResponseMessage.setPrev_posId(vo.getPrev_posId());
			validateResponseMessage.setFirst_posId(vo.getFirst_posId());
			validateResponseMessage.setQqws_resultcode(vo.getQqws_resultcode());
			validateResponseMessage.setQqvalidate_resultstatus(vo.getQqvalidate_resultstatus());
			validateResponseMessage.setResultName(vo.getResultName());
			validateResponseMessage.setCurrentTime(vo.getCurrentTime());
			validateResponseMessage.setResultExplain(vo.getResultExplain());
			validateResponseMessage.setUseTime(vo.getUseTime());
			validateResponseMessage.setValidTime(vo.getValidTime());
			validateResponseMessage.setPrev_validate_time(vo.getPrev_validate_time());
			validateResponseMessage.setFirst_validate_time(vo.getFirst_validate_time());
			validateResponseMessage.setCmdId(CmdConstant.VALIDATE_2_CMD_ID_RESPONSE);
			
		} catch (JsonGenerationException e) {
			log.error("JsonGenerationException:", e);
		} catch (SaveDBException e) {
			log.error("SaveDBException:", e);
		}

		log.debug("ValidateCommandHandler2 end:"+validateResponseMessage.getCmdId());
		
		response.writeResponse(validateResponseMessage);
	}

}
