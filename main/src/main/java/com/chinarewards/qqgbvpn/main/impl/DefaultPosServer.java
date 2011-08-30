/**
 * 
 */
package com.chinarewards.qqgbvpn.main.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Iterator;

import org.apache.commons.configuration.Configuration;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.PosServer;
import com.chinarewards.qqgbvpn.main.PosServerException;
import com.chinarewards.qqgbvpn.main.protocol.filter.LoginFilter;
import com.chinarewards.qqgbvpn.main.protocol.hander.ServerSessionHandler;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder.MessageCoderFactory;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class DefaultPosServer implements PosServer {

	protected final Configuration configuration;

	protected final Injector injector;

	protected Logger log = LoggerFactory.getLogger(PosServer.class);

	/**
	 * socket server address
	 */
	InetSocketAddress serverAddr;

	protected int port;

	/**
	 * acceptor
	 */
	IoAcceptor acceptor;

	@Inject
	public DefaultPosServer(Configuration configuration, Injector injector) {
		this.configuration = configuration;
		this.injector = injector;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbvpn.main.PosServer#start()
	 */
	@Override
	public void start() throws PosServerException {

		printConfigValues();

		// setup Apache Mina server.

		port = configuration.getInt("server.port");
		serverAddr = new InetSocketAddress(port);

		acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());

		// not this
		acceptor.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new MessageCoderFactory(injector)));

		// Login filter.
		acceptor.getFilterChain().addLast("login", new LoginFilter());

		acceptor.setHandler(new ServerSessionHandler(injector));
		// TODO make this configurable
		acceptor.setCloseOnDeactivation(true);

		// acceptor.getSessionConfig().setReadBufferSize(2048);
		// TODO make this configurable
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		try {
			acceptor.bind(serverAddr);
		} catch (IOException e) {
			throw new PosServerException("Error binding server address "
					+ serverAddr.getAddress().getHostAddress() + ":"
					+ serverAddr.getPort());
		}

		// XXX get the real port to listen.
		log.info("Server running, listening on {}", getLocalPort());

	}

	/**
	 * Print configuration.
	 */
	private void printConfigValues() {
		// get system configuration
		Iterator iter = configuration.getKeys();
		if (configuration.isEmpty()) {
			log.debug("No configuration values");
		} else {
			log.debug("System configuration:");
			while (iter.hasNext()) {
				String key = (String) iter.next();
				log.debug("- {}: {}", key, configuration.getProperty(key));
			}
		}

		// TODO print command mapping
	}

	protected void startPersistenceService() {
		// PersistService ps = injector.getInstance(PersistService)
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbvpn.main.PosServer#stop()
	 */
	@Override
	public void stop() {

		acceptor.unbind(serverAddr);
		acceptor.dispose();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbvpn.main.PosServer#isStopped()
	 */
	@Override
	public boolean isStopped() {
		if (acceptor == null)
			return true;
		if (!acceptor.isActive() || acceptor.isDisposed())
			return true;
		return false;
	}

	@Override
	public int getLocalPort() {

		if (acceptor != null) {
			InetSocketAddress d = (InetSocketAddress) acceptor
					.getLocalAddress();
			return d.getPort();
		}

		return port;
	}

}
