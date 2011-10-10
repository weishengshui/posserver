/**
 * 
 */
package com.chinarewards.qqgbvpn.main.impl;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.configuration.Configuration;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.ConfigKey;
import com.chinarewards.qqgbvpn.main.PosServer;
import com.chinarewards.qqgbvpn.main.PosServerException;
import com.chinarewards.qqgbvpn.main.protocol.CmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.CmdMapping;
import com.chinarewards.qqgbvpn.main.protocol.CodecMappingConfigBuilder;
import com.chinarewards.qqgbvpn.main.protocol.ServiceDispatcher;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandlerObjectFactory;
import com.chinarewards.qqgbvpn.main.protocol.ServiceMapping;
import com.chinarewards.qqgbvpn.main.protocol.SimpleCmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.filter.BodyMessageFilter;
import com.chinarewards.qqgbvpn.main.protocol.filter.ErrorConnectionKillerFilter;
import com.chinarewards.qqgbvpn.main.protocol.filter.IdleConnectionKillerFilter;
import com.chinarewards.qqgbvpn.main.protocol.filter.LoginFilter;
import com.chinarewards.qqgbvpn.main.protocol.filter.MonitorCommandManageFilter;
import com.chinarewards.qqgbvpn.main.protocol.filter.MonitorConnectManageFilter;
import com.chinarewards.qqgbvpn.main.protocol.handler.ServerSessionHandler;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.MessageCoderFactory;
import com.chinarewards.qqgbvpn.main.rmi.RMIRegistry;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;

/**
 * Concrete implementation of <code>PosServer</code>.
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class DefaultPosServer implements PosServer {
	
	/**
	 * Default timeout, in seconds, which the server will disconnect a client.
	 */
	public static final long DEFAULT_SERVER_CLIENTMAXIDLETIME = 1800;
	
	/**
	 * Default monitor port
	 */
	public static final int DEFAULT_SERVER_MONITORPORT = 9999;

	protected final Configuration configuration;

	protected final Injector injector;

	protected Logger log = LoggerFactory.getLogger(PosServer.class);

	protected ServiceMapping mapping;

	protected CmdMapping cmdMapping;

	protected CmdCodecFactory cmdCodecFactory;

	protected final ServiceHandlerObjectFactory serviceHandlerObjectFactory;

	protected final ServiceDispatcher serviceDispatcher;

	protected JMXConnectorServer cs;
	
	private MonitorConnectManageFilter monitorConnectManageFilter;
	private MonitorCommandManageFilter monitorCommandManageFilter;
	
	/**
	 * socket server address
	 */
	InetSocketAddress serverAddr;

	/**
	 * The configured port to use.
	 */
	protected int port;

	/**
	 * The configured jmx moniter server port to use.
	 */
	protected int jmxMoniterPort;

	/**
	 * Whether the PersistService of Guice has been initialized, i.e. the
	 * .start() method has been called. We need to remember this state since it
	 * cannot be called twice (strange!).
	 */
	protected boolean isPersistServiceInited = false;

	/**
	 * acceptor
	 */
	NioSocketAcceptor acceptor;

	boolean persistenceServiceInited = false;

	@Inject
	public DefaultPosServer(Configuration configuration, Injector injector,
			ServiceDispatcher serviceDispatcher,
			ServiceHandlerObjectFactory serviceHandlerObjectFactory) {
		this.configuration = configuration;
		this.injector = injector;
		this.serviceDispatcher = serviceDispatcher;
		this.serviceHandlerObjectFactory = serviceHandlerObjectFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbvpn.main.PosServer#start()
	 */
	@Override
	public void start() throws PosServerException,
			InstanceAlreadyExistsException, MBeanRegistrationException,
			NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException, IOException {

		buildCodecMapping();

		// start the JPA persistence service
		startPersistenceService();

		// setup Apache Mina server.
		startMinaService();


		log.info("Server running, listening on {}", getLocalPort());

	}

	protected void buildCodecMapping() {

		CodecMappingConfigBuilder builder = new CodecMappingConfigBuilder();
		CmdMapping cmdMapping = builder.buildMapping(configuration);

		// and then the factory
		this.cmdCodecFactory = new SimpleCmdCodecFactory(cmdMapping);
		this.cmdMapping = cmdMapping;

	}

	/**
	 * Start the Apache Mina service.
	 * 
	 * @throws PosServerException
	 * @throws NullPointerException
	 * @throws MalformedObjectNameException
	 * @throws NotCompliantMBeanException
	 * @throws MBeanRegistrationException
	 * @throws InstanceAlreadyExistsException
	 * @throws IOException
	 */
	protected void startMinaService() throws PosServerException,
			InstanceAlreadyExistsException, MBeanRegistrationException,
			NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException, IOException {

		
		// default 1800 seconds
		long idleTime = configuration.getLong(ConfigKey.SERVER_CLIENTMAXIDLETIME,
				DEFAULT_SERVER_CLIENTMAXIDLETIME);

		log.debug("idleTime={}",idleTime);

		port = configuration.getInt("server.port");
		serverAddr = new InetSocketAddress(port);

		// =============== server side ===================

		// Programmers: You MUST consult your team before rearranging the 
		// order of the following Mina filters, as the ordering may affects
		// the behaviour of the application which may lead to unpredictable
		// result.
		
		acceptor = new NioSocketAcceptor();

		// ManageIoSessionConnect filter if idle server will not close any idle IoSession
		acceptor.getFilterChain().addLast("ManageIoSessionConnect",
				new IdleConnectionKillerFilter(idleTime));
		
		// add jmx monitor
		addMonitor();
		
		// monitor manage connect count filter ------> jmx
		acceptor.getFilterChain().addLast("monitorConnectManageFilter",
				this.monitorConnectManageFilter);

		// our logging filter		
		acceptor.getFilterChain()
				.addLast(
						"cr-logger",
						new com.chinarewards.qqgbvpn.main.protocol.filter.LoggingFilter());
		
		acceptor.getFilterChain().addLast("logger", buildLoggingFilter());

		// decode message
		// TODO config MessageCoderFactory to allow setting the maximum message size 
		acceptor.getFilterChain().addLast(
				"codec", new ProtocolCodecFilter(new MessageCoderFactory(cmdCodecFactory)));

		// kills error connection if too many.
		acceptor.getFilterChain().addLast("errorConnectionKiller",
				new ErrorConnectionKillerFilter());
		
		// monitor manage command filter ------> jmx
		acceptor.getFilterChain().addLast("monitorCommandManageFilter",
				this.monitorCommandManageFilter);
		
		// bodyMessage filter - short-circuit if error message is received.
		acceptor.getFilterChain().addLast("bodyMessage",
				new BodyMessageFilter());
		
		// Login filter.
		acceptor.getFilterChain().addLast("login",
				injector.getInstance(LoginFilter.class));

		// the handler class
		acceptor.setHandler(new ServerSessionHandler(serviceDispatcher,
				mapping, configuration));
		
		// additional configuration
		acceptor.setCloseOnDeactivation(true);
		acceptor.setReuseAddress(true);

		// acceptor.getSessionConfig().setReadBufferSize(2048);
		if (idleTime > 0) {
			log.info("Client idle timeout set to {} seconds", idleTime);
		} else {
			log.info("Client idle timeout set to {} seconds, will be disabled",
					idleTime);
		}
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		
		// start the acceptor and listen to incomming connection!
		try {
			acceptor.bind(serverAddr);
		} catch (IOException e) {
			throw new PosServerException("Error binding server port", e);
		}
	}
	
	/**
	 * Build an new instance of LoggingFilter with sane logging level. The
	 * principle is to hide unnecessary logging under INFO level.
	 * 
	 * @return
	 */
	protected LoggingFilter buildLoggingFilter() {
		LoggingFilter loggingFilter = new LoggingFilter();
		loggingFilter.setMessageReceivedLogLevel(LogLevel.DEBUG);
		loggingFilter.setMessageSentLogLevel(LogLevel.DEBUG);
		loggingFilter.setSessionIdleLogLevel(LogLevel.TRACE);
		return loggingFilter;
	}

	protected void startPersistenceService() {

		// the guice-persist's PersistService can only be started once.
		if (persistenceServiceInited) {
			return;
		}

		// start the PersistService.
		PersistService ps = injector.getInstance(PersistService.class);
		ps.start();
		persistenceServiceInited = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbvpn.main.PosServer#stop()
	 */
	@Override
	public void stop() throws IOException {
		acceptor.unbind(serverAddr);
		acceptor.dispose();
		if (cs != null && cs.isActive())
			cs.stop();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbvpn.main.PosServer#shutdown()
	 */
	@Override
	public void shutdown() {
		try {
			PersistService ps = injector.getInstance(PersistService.class);
			ps.stop();
		} catch (Throwable t) {
			// mute
			log.warn("An error occurred when stopping persistence service", t);
		}
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

	public void addMonitor() throws PosServerException,
			InstanceAlreadyExistsException, MBeanRegistrationException,
			NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException, IOException {
		jmxMoniterPort = configuration.getInt(ConfigKey.SERVER_MONITORPORT, DEFAULT_SERVER_MONITORPORT);
		log.debug(" monitor port ={}", jmxMoniterPort);
		// jmx----------------------code start--------------------------
		// jmx 服务器
		MBeanServer mbs = MBeanServerFactory.createMBeanServer();
		// 管理连接状态数目工具Filter
		this.monitorConnectManageFilter = new MonitorConnectManageFilter();
		this.monitorCommandManageFilter = new MonitorCommandManageFilter();
		// 注册需要被管理的MBean
		mbs.registerMBean(this.monitorConnectManageFilter, new ObjectName(
				"monitorConnectManage:name=monitorConnectManage"));
		
		mbs.registerMBean(this.monitorCommandManageFilter, new ObjectName(
		"MonitorCommandManage:name=MonitorCommandManage"));

		String jmxServiceURL = "service:jmx:rmi:///jndi/rmi://localhost:"
				+ jmxMoniterPort + "/jmxrmi";
		// Create an RMI connector and start it
		JMXServiceURL url = new JMXServiceURL(jmxServiceURL);

		log.debug(" JMXServiceURL ={}", jmxServiceURL);

		cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
		// jmx----------------------code end--------------------------
	}

	@Override
	public void setMonitorEnable(boolean isMonitorEnable) throws IOException {
		if (cs != null && isMonitorEnable) {
			RMIRegistry.RegistryRMI(jmxMoniterPort);
			cs.start();
		} else {
			if (cs != null && cs.isActive()&& !isMonitorEnable){
				cs.stop();
			}
		}
	}

}
