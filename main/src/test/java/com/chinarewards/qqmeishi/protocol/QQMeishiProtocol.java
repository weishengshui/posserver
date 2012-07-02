package com.chinarewards.qqmeishi.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.configuration.Configuration;
import org.apache.mina.core.buffer.IoBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;

import com.chinarewards.qqgbpvn.main.CommonTestConfigModule;
import com.chinarewards.qqgbpvn.main.test.GuiceTest;
import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.core.jpa.JpaPersistModuleBuilder;
import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.PosAssignment;
import com.chinarewards.qqgbvpn.domain.status.PosDeliveryStatus;
import com.chinarewards.qqgbvpn.domain.status.PosInitializationStatus;
import com.chinarewards.qqgbvpn.domain.status.PosOperationStatus;
import com.chinarewards.qqgbvpn.main.ApplicationModule;
import com.chinarewards.qqgbvpn.main.PosServer;
import com.chinarewards.qqgbvpn.main.ServerModule;
import com.chinarewards.qqgbvpn.main.guice.AppModule;
import com.chinarewards.qqgbvpn.main.impl.DefaultPosServer;
import com.chinarewards.qqgbvpn.main.logic.qqapi.impl.HardCodedServlet;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandlerModule;
import com.chinarewards.qqgbvpn.main.protocol.ServiceMapping;
import com.chinarewards.qqgbvpn.main.protocol.ServiceMappingConfigBuilder;
import com.chinarewards.qqgbvpn.main.protocol.cmd.QQMeishiResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.guice.ServiceHandlerGuiceModule;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.QQMeishiBodyMessageResponseCodec;
import com.chinarewards.qqgbvpn.main.util.HMAC_MD5;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.util.Modules;


/** 
 *  FIXME restore this as QQMeishiProtocolTest
 * @author harry
 * @since 0.1.0
 */
public class QQMeishiProtocol extends GuiceTest {

	EntityManager em;
	long runForSeconds;
	PosServer posServer;
	int port;
	private static final String CHARSET = "UTF-8";
	private static final int QQMEISHIPORT = 8084;
	
	private static final String SUCCESS = "SUCCESS";
	private static final String ERRORCODE = "ERRORCODE";
	
	private Server server = new Server(0);
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		// if (!server.isStarted()) {
		
		// }
		port = startServer();
		runForSeconds = 1;

	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		if (em != null && em.getTransaction().isActive()) {
			em.getTransaction().rollback();
		}
		
		stopServer();
	}
	

	private void startTXServer(String source) throws Exception {
		// build test server start
		if (!server.isStarted()) {
			ServletHandler scHandler = new ServletHandler();
			if(source.equals(SUCCESS)){
				scHandler.addServletWithMapping(getSuccess(),
				"/qqmeishitransaction");
			}else if(source.equals(ERRORCODE)){
				scHandler.addServletWithMapping(getErrorCode(),
				"/qqmeishitransaction");
			}
			
			// add handler to server
			server.addHandler(scHandler);
			server.getConnectors()[0].setPort(QQMEISHIPORT);
			server.start();
			System.out.println("qqmeishi server start!");
		}
		// build test server end
	}

	private void stopTXServer() throws Exception {
		log.debug("qqmeishi server stop");
		if (server.isStarted()) {

			try {
				server.stop();
			} catch (Throwable t) {

			}
		}
	}

	private ServletHolder getErrorCode() throws Exception {
		HardCodedServlet s = new HardCodedServlet();
		s.init();
		StringBuffer sb = new StringBuffer();
		sb.append("{\"result\":\"\",\"errCode\":1,\"errMessage\":\"失败了!\"}");
		s.setResponse(new String(sb.toString().getBytes(CHARSET), CHARSET));
		ServletHolder h = new ServletHolder();
		h.setServlet(s);

		return h;
	}
	
	private ServletHolder getSuccess() throws Exception {
		HardCodedServlet s = new HardCodedServlet();
		s.init();
		StringBuffer sb = new StringBuffer();
		sb.append("{'result':{'password':'123456789','validCode':0,'hasPassword':true,'tradeTime':'2012-01-31 23:40:58','title':'gfedcba','tip':'abcdefg'},'errCode':0,'errMessage':'成功了!'}");
		s.setResponse(new String(sb.toString().getBytes(CHARSET), CHARSET));

		ServletHolder h = new ServletHolder();
		h.setServlet(s);

		return h;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgpvn.main.test.GuiceTest#getModules()
	 */
	@Override
	protected Module[] getModules() {

		CommonTestConfigModule confModule = new CommonTestConfigModule();
		ServiceMappingConfigBuilder mappingBuilder = new ServiceMappingConfigBuilder();
		Configuration configuration = confModule.getConfiguration();
		ServiceMapping mapping = mappingBuilder.buildMapping(configuration);
	
		// build the Guice modules.
		Module[] modules = new Module[] {
				new ApplicationModule(),
				new CommonTestConfigModule(),
				buildPersistModule(confModule.getConfiguration()),
				new ServerModule(),
				new AppModule(),
				Modules.override(
						new ServiceHandlerModule(confModule.getConfiguration()))
						.with(new ServiceHandlerGuiceModule(mapping)) };

		return modules;
	}

	protected Module buildPersistModule(Configuration config) {

		JpaPersistModule jpaModule = new JpaPersistModule("posnet");
		// config it.

		JpaPersistModuleBuilder b = new JpaPersistModuleBuilder();
		b.configModule(jpaModule, config, "db");

		return jpaModule;
	}

	private void stopServer() throws Exception {
		// stop it, and make sure it is stopped.
		posServer.stop();
		assertTrue(posServer.isStopped());
		log.info("posServer stopped");
	}


	private int startServer() throws Exception {

		// force changing of configuration
		Configuration conf = getInjector().getInstance(Configuration.class);
		conf.setProperty("server.port", 0);

		// get an new instance of PosServer
		posServer = getInjector().getInstance(PosServer.class);
		// make sure it is started, and port is correct
		assertTrue(posServer.isStopped());
		//
		// start it!
		posServer.start();
		int runningPort = posServer.getLocalPort();
		// stop it.
		posServer.stop();
		assertTrue(posServer.isStopped());

		// XXX we insert data here
		{
			DefaultPosServer dserver = (DefaultPosServer) posServer;
			Injector injector = dserver.getInjector();

			em = injector.getInstance(EntityManager.class);
			em.getTransaction().begin();
			initDB(em);
		}

		// get an new instance of PosServer
		conf.setProperty("server.port", runningPort);

		// make sure it is stopped
		assertTrue(posServer.isStopped());

		// start it!
		posServer.start();

		// make sure it is started, and port is correct
		assertFalse(posServer.isStopped());
		assertEquals(runningPort, posServer.getLocalPort());
		return posServer.getLocalPort();
		// sleep for a while...
	}
	
	 private QQMeishiResponseMessage getResponseQQMeishiMessage(byte[] responseBytes)throws Exception{
		byte[] responseBody = new byte[responseBytes.length - 16];
		System.arraycopy(responseBytes, 16, responseBody, 0, responseBody.length);
		IoBuffer in2 = IoBuffer.allocate(responseBody.length);
		in2.put(responseBody);
		in2.position(0);
		Charset charset = Charset.forName("GB2312");
		QQMeishiBodyMessageResponseCodec responseCodec = new QQMeishiBodyMessageResponseCodec();
		
//		System.out.println(CodecUtil.hexDumpAsString(responseBody));
		
		QQMeishiResponseMessage reponseMessage = (QQMeishiResponseMessage)responseCodec.decode(in2, charset);

		return reponseMessage;
		
	}
	 	
	@Ignore
	@Test
	public void testQQMeishiSuccess() throws Exception {
		System.out.println("success!");
		//启动腾讯服务器
		startTXServer(SUCCESS);
		/**
		 * 启动QQ美食服务器
		 * 返回正确的参数
		 */	
		Socket socket = new Socket("localhost", port);
		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();

		byte[] challenge = new byte[8];
		
		log.debug("pos init");
		this.oldPosInit(os, is, challenge);// old client

		log.debug("pos login");
		oldPosLogin(os, is, challenge);

		byte[] responseBytes = qqMeishiRequest(os, is);

		System.out.println("");
		os.close();
		socket.close();
		
		//关闭腾讯服务器
		stopTXServer();
		
		QQMeishiResponseMessage reponseMessage = getResponseQQMeishiMessage(responseBytes);
			
		assertEquals(102, reponseMessage.getCmdId());
		assertEquals(0, reponseMessage.getServerErrorCode());
		assertEquals(0, reponseMessage.getQqwsErrorCode());
		assertEquals(0, reponseMessage.getResult());
		assertEquals(0, reponseMessage.getForcePwdNextAction());

		assertEquals("gfedcba", reponseMessage.getTitle());
		assertEquals("abcdefg", reponseMessage.getTip());		
		assertEquals("123456789", reponseMessage.getPassword());
		
		
	}
	
	@Ignore
	@Test
	public void testQQMeishiErrorCode() throws Exception {
		System.out.println("error code!");
		//启动腾讯服务器
		startTXServer(ERRORCODE);
		/**
		 * 启动QQMeishi server
		 * 返回errorCode 不等于0
		 */
		Socket socket = new Socket("localhost", port);
		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();

		byte[] challenge = new byte[8];
		
		log.debug("pos init");
		this.oldPosInit(os, is, challenge);// old client

		log.debug("pos login");
		oldPosLogin(os, is, challenge);

		byte[] responseBytes = qqMeishiRequest(os, is);

		System.out.println("");
		os.close();
		socket.close();
		
		//关闭腾讯服务器
		stopTXServer();
		
		QQMeishiResponseMessage reponseMessage = getResponseQQMeishiMessage(responseBytes);
			
		assertEquals(102, reponseMessage.getCmdId());
		assertEquals(0, reponseMessage.getServerErrorCode());
		assertEquals(1, reponseMessage.getQqwsErrorCode());
		assertEquals(0, reponseMessage.getResult());
		assertEquals(0, reponseMessage.getForcePwdNextAction());
		assertEquals(null, reponseMessage.getTitle());
		assertEquals(null, reponseMessage.getTip());		
		assertEquals(null, reponseMessage.getPassword());
	}
	@Ignore
	@Test
	public void testQQMeishiNOConnect() throws Exception {
		System.out.println("no qqmeishi server!");
		/**
		 * 不启动QQ美食服务器
		 */
		Socket socket = new Socket("localhost", port);
		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();

		byte[] challenge = new byte[8];
		
		log.debug("pos init");
		this.oldPosInit(os, is, challenge);// old client

		log.debug("pos login");
		oldPosLogin(os, is, challenge);

		byte[] responseBytes = qqMeishiRequest(os, is);

		System.out.println("");
		os.close();
		socket.close();
		
		QQMeishiResponseMessage reponseMessage = getResponseQQMeishiMessage(responseBytes);
			
		assertEquals(102, reponseMessage.getCmdId());
		assertEquals(1, reponseMessage.getServerErrorCode());
		assertEquals(0, reponseMessage.getQqwsErrorCode());
		assertEquals(0, reponseMessage.getResult());
		assertEquals(0, reponseMessage.getForcePwdNextAction());
		assertEquals(null, reponseMessage.getTitle());
		assertEquals(null, reponseMessage.getTip());		
		assertEquals(null, reponseMessage.getPassword());
	}

	private void oldPosInit(OutputStream os, InputStream is, byte[] challenge)
			throws Exception {
		byte[] msg = new byte[] {
				// SEQ
				0, 0, 0, 24,
				// ACK
				0, 0, 0, 0,
				// flags
				0, 0,
				// checksum
				0, 0,
				// message length
				0, 0, 0, 32,
				// command ID
				0, 0, 0, 5,
				// POS ID
				'R', 'E', 'W', 'A', 'R', 'D', 'S', '-', '0', '0', '0', '3' };

		// calculate checksum
		int checksum = Tools.checkSum(msg, msg.length);
		Tools.putUnsignedShort(msg, checksum, 10);

		// send both message at once
		byte[] outBuf = new byte[msg.length];
		System.arraycopy(msg, 0, outBuf, 0, msg.length);

		// write response
		log.info(" Init Send request to server");
		os.write(outBuf);

		// ----------

		// session.write("Client First Message");
		Thread.sleep(runForSeconds * 1000);
		// read
		log.info("Read response");
		byte[] response = new byte[30];
		int n = is.read(response);
		System.out.println("Number of bytes init read: " + n);
		for (int i = 0; i < n; i++) {
			String s = Integer.toHexString((byte) response[i]);
			if (s.length() < 2)
				s = "0" + s;
			if (s.length() > 2)
				s = s.substring(s.length() - 2);
			System.out.print(s + " ");
			if ((i + 1) % 8 == 0)
				System.out.println("");
		}
		System.arraycopy(response, 22, challenge, 0, 8);
		assertEquals(0, response[20]);
		assertEquals(0, response[21]);
	}

	private void oldPosLogin(OutputStream os, InputStream is, byte[] challenge)
			throws Exception {
		byte[] msg = new byte[] {
				// SEQ
				0, 0, 0, 25,
				// ACK
				0, 0, 0, 0,
				// flags
				0, 0,
				// checksum
				0, 0,
				// message length
				0, 0, 0, 48,
				// command ID
				0, 0, 0, 7,
				// POS ID
				'R', 'E', 'W', 'A', 'R', 'D', 'S', '-', '0', '0', '0', '3',
				// challengeResponse
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		byte[] content2 = HMAC_MD5.getSecretContent(challenge, "000001");
		Tools.putBytes(msg, content2, 32);
		int checksum = Tools.checkSum(msg, msg.length);
		Tools.putUnsignedShort(msg, checksum, 10);
		byte[] outBuf = new byte[msg.length];
		System.arraycopy(msg, 0, outBuf, 0, msg.length);

		log.info(" Login Send request to server");
		os.write(outBuf);
		// ----------

		Thread.sleep(runForSeconds * 1000);
		// read
		log.info("Read response");
		byte[] response = new byte[30];
		int n = is.read(response);
		System.out.println("Number of bytes login read2: " + n);
		for (int i = 0; i < n; i++) {
			String s = Integer.toHexString((byte) response[i]);
			if (s.length() < 2)
				s = "0" + s;
			if (s.length() > 2)
				s = s.substring(s.length() - 2);
			System.out.print(s + " ");
			if ((i + 1) % 8 == 0)
				System.out.println("");
		}
		assertEquals(0, response[20]);
		assertEquals(0, response[21]);

	}

	
	private byte[] qqMeishiRequest(OutputStream os, InputStream is)
			throws Exception {
		byte[] msg = new byte[] {
				// SEQ
				0, 0, 0, 27,
				// ACK
				0, 0, 0, 0,
				// flags
				0, 0,
				// checksum
				0, 0,
				// message length
				0, 0, 0, 48,
				// command ID
				0, 0, 0, 101,
				// userToken abcd123
				0, 7, 97, 98, 99, 100, 49, 50, 51,
				//amount 1.11
				(byte)0xC3, (byte)0xF5, 0x28, 0x5C, (byte)0x8F, (byte)0xC2, (byte)0xF1, (byte)0x3F,
				// password 123456789
				0, 9, 49, 50, 51, 52, 53, 54, 55, 56, 57 };

		int checksum = Tools.checkSum(msg, msg.length);
		Tools.putUnsignedShort(msg, checksum, 10);

		// send both message at once
		byte[] outBuf = new byte[msg.length];
		System.arraycopy(msg, 0, outBuf, 0, msg.length);

		os.write(outBuf);
		// ----------

		Thread.sleep(runForSeconds * 1000);
		// read
		log.info("QQMeishi Read response");
		byte[] response = new byte[200];
		int n = is.read(response);
		System.out.println("Number of bytes login read: " + n);
		for (int i = 0; i < n; i++) {
			String s = Integer.toHexString((byte) response[i]);
			if (s.length() < 2)
				s = "0" + s;
			if (s.length() > 2)
				s = s.substring(s.length() - 2);
			System.out.print(s + " ");
			if ((i + 1) % 8 == 0)
				System.out.println("");
		}
		

		return response;
	}

	private void initDB(EntityManager em) {
		String posId1 = "REWARDS-0003";
		String posId2 = "REWARDS-0002";
		String agentName = "agentName";

		Pos pos = new Pos();
		pos.setPosId(posId1);
		pos.setDstatus(PosDeliveryStatus.DELIVERED);
		pos.setSecret("000001");
		pos.setIstatus(PosInitializationStatus.INITED);
		pos.setOstatus(PosOperationStatus.ALLOWED);
		em.persist(pos);
		Pos pos2 = new Pos();
		pos2.setPosId(posId2);
		pos2.setDstatus(PosDeliveryStatus.DELIVERED);
		pos2.setSecret("000001");
		pos2.setIstatus(PosInitializationStatus.INITED);
		pos2.setOstatus(PosOperationStatus.ALLOWED);
		em.persist(pos2);
		Agent agent = new Agent();
		agent.setName(agentName);
		em.persist(agent);
		PosAssignment pa = new PosAssignment();
		pa.setAgent(agent);
		pa.setPos(pos);
		em.persist(pa);
		PosAssignment pa2 = new PosAssignment();
		pa2.setAgent(agent);
		pa2.setPos(pos2);
		em.persist(pa2);

		List<Pos> pp = em.createQuery(" from Pos").getResultList();
		log.debug("pp={}", pp);
		if (pp != null) {
			for (Pos p : pp) {
				log.debug("getPosId : {}", p.getPosId());
			}
		}
	}

}
