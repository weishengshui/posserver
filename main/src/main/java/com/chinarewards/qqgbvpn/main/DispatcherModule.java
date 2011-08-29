package com.chinarewards.qqgbvpn.main;

import com.chinarewards.qqgbvpn.main.protocol.cmd.CommandHandler;
import com.chinarewards.qqgbvpn.main.protocol.cmd.impl.InitCommandHandler;
import com.chinarewards.qqgbvpn.main.protocol.cmd.impl.LoginCommandHandler;
import com.chinarewards.qqgbvpn.main.protocol.cmd.impl.SearchCommandHandler;
import com.chinarewards.qqgbvpn.main.protocol.cmd.impl.ValidateCommandHandler;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder.IBodyMessageCoder;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder.InitMessageCoder;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder.LoginMessageCoder;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder.SearchBodyMessageCoder;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder.ValidateBodyMessageCoder;
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
		// coder
		bind(IBodyMessageCoder.class).annotatedWith(Names.named("login")).to(
				LoginMessageCoder.class).in(Singleton.class);
		bind(IBodyMessageCoder.class).annotatedWith(Names.named("init")).to(
				InitMessageCoder.class).in(Singleton.class);
		bind(IBodyMessageCoder.class).annotatedWith(Names.named("search")).to(
				SearchBodyMessageCoder.class).in(Singleton.class);
		bind(IBodyMessageCoder.class).annotatedWith(Names.named("validate")).to(
				ValidateBodyMessageCoder.class).in(Singleton.class);
		
		// command hander
		bind(CommandHandler.class).annotatedWith(Names.named("login")).to(
				LoginCommandHandler.class).in(Singleton.class);
		bind(CommandHandler.class).annotatedWith(Names.named("init")).to(
				InitCommandHandler.class).in(Singleton.class);
		bind(CommandHandler.class).annotatedWith(Names.named("search")).to(
				SearchCommandHandler.class).in(Singleton.class);
		bind(CommandHandler.class).annotatedWith(Names.named("validate"))
				.to(ValidateCommandHandler.class).in(Singleton.class);

	}

}
