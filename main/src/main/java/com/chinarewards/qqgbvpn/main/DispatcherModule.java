package com.chinarewards.qqgbvpn.main;

import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CommandHandler;
import com.chinarewards.qqgbvpn.main.protocol.cmd.impl.LoginCommandHandler;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.IBodyMessageCoder;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.LoginMessageCoder;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

/**
 * cmd hander module
 * 
 * @author huangwei
 *
 */
public class DispatcherModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CommandHandler.class).annotatedWith(Names.named(CmdConstant.LOGIN_CMD_NAME)).to(
				LoginCommandHandler.class).in(Singleton.class);
		bind(IBodyMessageCoder.class).annotatedWith(Names.named(CmdConstant.LOGIN_CMD_NAME)).to(
				LoginMessageCoder.class).in(Singleton.class);

	}

}
