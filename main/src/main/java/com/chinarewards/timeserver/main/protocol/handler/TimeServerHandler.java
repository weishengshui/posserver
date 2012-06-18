package com.chinarewards.timeserver.main.protocol.handler;

import java.util.Date;

import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceRequest;
import com.chinarewards.qqgbvpn.main.protocol.ServiceResponse;
import com.chinarewards.timeserver.main.protocol.cmd.TimeServerRequestMessage;
import com.chinarewards.timeserver.main.protocol.cmd.TimeServerResponseMessage;

public class TimeServerHandler implements ServiceHandler {

	@Override
	public void execute(ServiceRequest request, ServiceResponse response) {
		System.out.println("TimeServerHandler*****************************************");
		Date now = new Date();
		TimeServerRequestMessage bodyMessage = (TimeServerRequestMessage)request.getParameter();
		System.out.println("TimeServerHandler======execute==bodyMessage=:"
				+ bodyMessage);
		TimeServerResponseMessage responseMessage = new TimeServerResponseMessage();
		responseMessage.setCmdId(TimeServerResponseMessage.TIME_SERVER_CMD_ID_RESPONSE);
		responseMessage.setTime(now.getTime());
		
		response.writeResponse(responseMessage);
	}

}
