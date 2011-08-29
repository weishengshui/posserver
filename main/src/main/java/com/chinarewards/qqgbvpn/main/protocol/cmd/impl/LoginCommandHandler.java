package com.chinarewards.qqgbvpn.main.protocol.cmd.impl;

import org.apache.mina.core.session.IoSession;

import com.chinarewards.qqgbvpn.main.protocol.cmd.CommandHandler;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.IBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.LoginRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.LoginResponseMessage;

public class LoginCommandHandler implements CommandHandler {

	@Override
	public IBodyMessage execute(IoSession session, IBodyMessage bodyMessage) {
		// TODO Auto-generated method stub
		LoginRequestMessage message = (LoginRequestMessage) bodyMessage;
		LoginResponseMessage loginResponseMessage = new LoginResponseMessage();
		loginResponseMessage.setCmdId(message.getCmdId() + 1);
		return loginResponseMessage;
	}

}
