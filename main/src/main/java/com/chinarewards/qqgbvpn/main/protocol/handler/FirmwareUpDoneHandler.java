package com.chinarewards.qqgbvpn.main.protocol.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.logic.firmware.FirmwareManager;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;
import com.chinarewards.qqgbvpn.main.protocol.ServiceResponse;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpDoneRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpDoneResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.firmware.FirmwareUpDoneResult;
import com.google.inject.Inject;

/**
 * description：POS固件升级成功  请求+响应
 * @copyright binfen.cc
 * @projectName main
 * @time 2011-9-15   上午10:53:30
 * @author Seek
 */
public class FirmwareUpDoneHandler implements ServiceHandler {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
	protected FirmwareManager firmwareManager;
	
	
	/* (non-Javadoc)
	 * @see com.chinarewards.qqgbvpn.main.protocol.ServiceHandler#execute(com.chinarewards.qqgbvpn.main.protocol.ServiceRequest, com.chinarewards.qqgbvpn.main.protocol.ServiceResponse)
	 */
	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {
		
		FirmwareUpDoneRequestMessage bodyMessage = (FirmwareUpDoneRequestMessage)request.getParameter();
		
		log.debug("FirmwareUpDoneHandle======execute==bodyMessage=: {}", bodyMessage);
		
		FirmwareUpDoneResponseMessage firmwareUpDoneResponseMessage = null;
		try {
			firmwareUpDoneResponseMessage = firmwareManager.ackUpgradeCompleted(bodyMessage);
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			firmwareUpDoneResponseMessage = new FirmwareUpDoneResponseMessage();
			firmwareUpDoneResponseMessage.setResult(FirmwareUpDoneResult.SUCCESS.getPosCode());
		}
		firmwareUpDoneResponseMessage.setCmdId(FirmwareUpDoneResponseMessage.FIRMWARE_UP_DONE_CMD_ID_RESPONSE);
		response.writeResponse(firmwareUpDoneResponseMessage);
	}
	
}
