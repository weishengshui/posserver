package com.chinarewards.qqgbvpn.main.protocol.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;
import com.chinarewards.qqgbvpn.main.protocol.ServiceResponse;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpDoneResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.PosEchoCommandRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.PosEchoCommandResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.firmware.FirmwareUpDoneResult;

/**
 * description：POS echo request
 * @copyright binfen.cc
 * @projectName main
 * @time 2011-10-20   下午05:57:56
 * @author Seek
 */
public class PosEchoCommandHandler implements ServiceHandler {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	/* (non-Javadoc)
	 * @see com.chinarewards.qqgbvpn.main.protocol.ServiceHandler#execute(com.chinarewards.qqgbvpn.main.protocol.ServiceRequest, com.chinarewards.qqgbvpn.main.protocol.ServiceResponse)
	 */
	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {
		
		PosEchoCommandRequestMessage bodyMessage = (PosEchoCommandRequestMessage)request.getParameter();
		
		log.debug("PosEchoCommandRequestMessage======execute==bodyMessage=: {}", bodyMessage);
		
		PosEchoCommandResponseMessage posEchoCommandResponseMessage = null;
		try {
			posEchoCommandResponseMessage = new PosEchoCommandResponseMessage();
			posEchoCommandResponseMessage.setData(bodyMessage.getData());
			posEchoCommandResponseMessage.setResult(PosEchoCommandResponseMessage.RESULT_OK);
			
			//request data too long
			if(bodyMessage.getData().length > 63335){
				posEchoCommandResponseMessage.setResult(PosEchoCommandResponseMessage.RESULT_DATA_TOO_LONG);
				posEchoCommandResponseMessage.setData(null);
			}
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			posEchoCommandResponseMessage.setResult(PosEchoCommandResponseMessage.RESULT_OTHER_ERROR);
		}
		response.writeResponse(posEchoCommandResponseMessage);
	}
	
}
