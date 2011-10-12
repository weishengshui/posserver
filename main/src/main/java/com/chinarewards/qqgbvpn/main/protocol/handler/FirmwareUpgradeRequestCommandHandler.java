package com.chinarewards.qqgbvpn.main.protocol.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.logic.firmware.FirmwareManager;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;
import com.chinarewards.qqgbvpn.main.protocol.ServiceResponse;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpgradeRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpgradeRequestResponseMessage;
import com.google.inject.Inject;

public class FirmwareUpgradeRequestCommandHandler implements ServiceHandler {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
	protected FirmwareManager firmwareManager;
	
	
	/* (non-Javadoc)
	 * @see com.chinarewards.qqgbvpn.main.protocol.ServiceHandler#execute(com.chinarewards.qqgbvpn.main.protocol.ServiceRequest, com.chinarewards.qqgbvpn.main.protocol.ServiceResponse)
	 */
	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {

		FirmwareUpgradeRequestMessage bodyMessage = (FirmwareUpgradeRequestMessage) request
				.getParameter();

		FirmwareUpgradeRequestResponseMessage fwResponseMessage = null;
		try {
			// 创建一个新的challenge
			fwResponseMessage = firmwareManager.upgradeRequest(bodyMessage);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		response.writeResponse(fwResponseMessage);
	}

}
