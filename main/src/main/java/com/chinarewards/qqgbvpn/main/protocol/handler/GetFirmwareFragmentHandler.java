package com.chinarewards.qqgbvpn.main.protocol.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.logic.firmware.FirmwareManager;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;
import com.chinarewards.qqgbvpn.main.protocol.ServiceResponse;
import com.chinarewards.qqgbvpn.main.protocol.cmd.GetFirmwareFragmentRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.GetFirmwareFragmentResponseMessage;
import com.google.inject.Inject;

/**
 * Implements the command handling for firmware fragment retrieval.
 * 
 * @author Cyril
 * @since 0.1.0 2011-09-15
 */
public class GetFirmwareFragmentHandler implements ServiceHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	protected FirmwareManager fwMgr;

	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {

		// get request
		GetFirmwareFragmentRequestMessage msg = (GetFirmwareFragmentRequestMessage) request
				.getParameter();

		log.debug("execute(): request={}", msg);

		GetFirmwareFragmentResponseMessage out = null;

		// call the main logic.
		try {
			out = fwMgr.getFirmwareFragment(msg);
		} catch (Throwable t) {
			log.error("An unexpected error has occurred", t);
			out = new GetFirmwareFragmentResponseMessage(
					GetFirmwareFragmentResponseMessage.RESULT_OTHER_ERROR, null);
		}

		response.writeResponse(out);
	}

}
