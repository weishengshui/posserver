package com.chinarewards.qqgbpvn.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.configuration.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;

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
import com.chinarewards.qqgbvpn.main.protocol.guice.ServiceHandlerGuiceModule;
import com.chinarewards.qqgbvpn.main.util.HMAC_MD5;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.util.Modules;

/**
 * FIXME restore this as SessionKeyProtocol2Test
 * 
 * @author Cyril
 * @author harry
 * @since 0.1.0
 */
public class SessionKeyProtocol2 extends GuiceTest {

	EntityManager em;
	long runForSeconds;
	private Server server = new Server(0);
	PosServer posServer;
	int port;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		// if (!server.isStarted()) {
		startTXServer();
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
		stopTXServer();
		stopServer();
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
		ServiceMapping mapping = mappingBuilder.buildMapping(confModule
				.getConfiguration());

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

	private void startTXServer() throws Exception {
		// build test server start
		if (!server.isStarted()) {
			ServletHandler scHandler = new ServletHandler();
			scHandler.addServletWithMapping(getInitGrouponCacheServletHolder(),
					"/initGrouponCache");
			scHandler.addServletWithMapping(
					getGroupBuyingValidateServletHolder(),
					"/groupBuyingValidate");
			// add handler to server
			server.addHandler(scHandler);
			server.getConnectors()[0].setPort(8787);
			server.start();
		}
		// build test server end
	}

	private void stopTXServer() throws Exception {
		log.debug("tx server stop...");
		if (server.isStarted()) {

			try {
				server.stop();
			} catch (Throwable t) {

			}
		}
	}

	private ServletHolder getInitGrouponCacheServletHolder() throws Exception {
		HardCodedServlet s = new HardCodedServlet();
		s.init();
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\" ?>");
		sb.append("<tuan>");
		sb.append("<resultCode>0</resultCode>");
		sb.append("<groupon>");
		sb.append("<item>");
		sb.append("<grouponId>132127</grouponId>");
		sb.append("<grouponName>400.01元套餐POST</grouponName>");
		sb.append("<mercName>三人行骨头王ggggg</mercName>");
		sb.append("<listName>40元套餐\r\n        (132127)</listName>");
		sb.append("<detailName>1111111111111140元套餐(132127)</detailName>");
		sb.append("</item>");
		sb.append("<item>");
		sb.append("<grouponId>132123</grouponId>");
		sb.append("<grouponName>400.01元套餐</grouponName>");
		sb.append("<mercName>三人行骨头王</mercName>");
		sb.append("<listName>400.01元套餐\r\n        (132123)</listName>");
		sb.append("<detailName>400.01元套餐</detailName>");
		sb.append("</item>");
		sb.append("<item>");
		sb.append("<grouponId>132154</grouponId>");
		sb.append("<grouponName>测试商品</grouponName>");
		sb.append("<mercName>测试商品王</mercName>");
		sb.append("<listName>测试商品套餐\r\n        (132154)</listName>");
		sb.append("<detailName>测试商品套餐</detailName>");
		sb.append("</item>");
		sb.append("</groupon>");
		sb.append("</tuan>");
		s.setResponse(new String(sb.toString().getBytes("gbk"), "iso-8859-1"));

		ServletHolder h = new ServletHolder();
		h.setServlet(s);

		return h;
	}

	private ServletHolder getGroupBuyingValidateServletHolder()
			throws Exception {
		HardCodedServlet s = new HardCodedServlet();
		s.init();
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\" ?>");
		sb.append("<tuan>");
		sb.append("<resultCode>0</resultCode>");
		sb.append("<groupon>");
		sb.append("<resultStatus>0</resultStatus>");
		sb.append("<resultName>验证已成功</resultName>");
		sb.append("<resultExplain>验证成功于\r\n08.03 11:10:23</resultExplain>");
		sb.append("<currentTime>2011-08-03 11:10:23</currentTime>");
		sb.append("<useTime>2011-08-03 11:10:23</useTime>");
		sb.append("<validTime>2011-08-10</validTime>");
		sb.append("</groupon>");
		sb.append("</tuan>");
		s.setResponse(new String(sb.toString().getBytes("gbk"), "iso-8859-1"));

		ServletHolder h = new ServletHolder();
		h.setServlet(s);

		/*
		 * String servletPath = "/groupBuyingValidate"; ServletHandler scHandler
		 * = new ServletHandler(); scHandler.addServletWithMapping(h,
		 * servletPath);
		 */
		return h;
	}

	protected Module buildPersistModule(Configuration config) {

		JpaPersistModule jpaModule = new JpaPersistModule("posnet");
		// config it.

		JpaPersistModuleBuilder b = new JpaPersistModuleBuilder();
		b.configModule(jpaModule, config, "db");

		return jpaModule;
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

	private void ackRequest(OutputStream os, InputStream is)
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
				0, 0, 0, 33,
				// command ID
				0, 0, 0, 11,
				// grouponId
				49, 51, 50, 49, 50, 51, 0,
				// grouponVCode
				49, 49, 49, 49, 50, 50, 0 };

		int checksum = Tools.checkSum(msg, msg.length);
		Tools.putUnsignedShort(msg, checksum, 10);

		// write response
		log.info("ack...");

		// send both message at once
		byte[] outBuf = new byte[msg.length];
		System.arraycopy(msg, 0, outBuf, 0, msg.length);

		os.write(outBuf);
		// ----------

		Thread.sleep(runForSeconds * 1000);
		// read
		log.info("Read response");
		byte[] response = new byte[161];
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
	}

	private void oldPosValidate(OutputStream os, InputStream is, int validateCount)
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
				0, 0, 0, 33,
				// command ID
				0, 0, 0, 27,
				// grouponId
				49, 51, 50, 49, 50, 51, 0,
				// grouponVCode
				49, 49, 49, 49, 50, 50, 0 };
		
	

		int checksum = Tools.checkSum(msg, msg.length);
		Tools.putUnsignedShort(msg, checksum, 10);

		// write response
		log.info(" list Send request to server");

		// send both message at once
		byte[] outBuf = new byte[msg.length];
		System.arraycopy(msg, 0, outBuf, 0, msg.length);

		os.write(outBuf);
		// ----------

		Thread.sleep(runForSeconds * 1000);
		// read
		log.info("Read response");
		byte[] response = new byte[161];
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
		assertEquals(validateCount, response[21]);
		assertEquals(0, response[22]);
		assertEquals(0, response[23]);
		assertEquals(0, response[24]);
		assertEquals(0, response[25]);
		assertEquals(0, response[26]);
		assertEquals(0, response[27]);
		assertEquals(0, response[28]);
		assertEquals(0, response[29]);
	}
	
	
	private void checkResultCount(int count){
		
		Query result = em.createQuery("select count(r.id) from GroupBuyValidateResult r");
		int resultCount = Integer.parseInt(result.getSingleResult().toString());
		log.debug("group buy validate resultCount={}",resultCount);
		assertEquals(count, resultCount);
	}
	
	private void checkValidateCount(int count){
		
		Query query = em.createQuery("select count(v.id) from Validation v where v.pcode = :pcode and v.vcode = :vcode");
		query.setParameter("pcode", "132123");
		query.setParameter("vcode", "111122");
		int resultCount = Integer.parseInt(query.getSingleResult().toString());
		log.debug("validation resultCount={}",resultCount);
		assertEquals(count, resultCount);
		
		
	}
	
	private void deleteValidationData(int count){
		Query query = em.createQuery("delete Validation");
		int deleteCount = query.executeUpdate();
		assertEquals(count, deleteCount);
	}
	
	
	@Test
	public void testThreeValidate() throws Exception {

		Thread.sleep(500); 
		Socket socket = new Socket("localhost", port);
		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();
		byte[] challenge = new byte[8];
		byte[] sessionId = new byte[16];
		int validateCount = 1;

		log.debug("start init 1......");
		newPosInit(os, is, challenge, sessionId);// new client add session key

		log.debug("start login 1......");
		oldPosLogin(os, is, challenge);

		log.debug("start list ......");
		oldPosSearchList(os, is);

		System.out.println("");

		log.debug("start one validate ......");
		SendValidationCheckValidateCount(os, is, sessionId, validateCount);

		log.debug("start two validate ......");
		validateCount = 2;
		SendValidationCheckValidateCount(os, is, sessionId, validateCount);
		
		log.debug("ACK request......");
		ackRequest(os, is);
		
		//检查验证结果表里面是不是只有0条数据
		checkResultCount(0);
		//检查验证表里面是不是只有两条数据
		checkValidateCount(2);
		
		//为了不影响下一次这里清空validation的数据
		deleteValidationData(2);

		//如果我再来验证这个团购这个验证码应该是第一次，因为这个时候会去请求腾讯服务器
		validateCount = 1;
		SendValidationCheckValidateCount(os, is, sessionId, validateCount);

		os.close();
		socket.close();
		
		//断线后再次验证
		Socket socket2 = new Socket("localhost", port);
		OutputStream os2 = socket2.getOutputStream();
		InputStream is2 = socket2.getInputStream();

		challenge = new byte[8];

		this.oldPosInit(os2, is2, challenge);// old client

		log.debug("start login 2......");
		oldPosLogin(os2, is2, challenge);

		log.debug("start list 2 ......");
		oldPosSearchList(os2, is2);

		log.debug("start validate 3......");
		
		//确定验证的次数数第二次
		validateCount = 2;
		oldPosValidate(os2, is2, validateCount);

		os2.close();
		socket2.close();
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
				'R', 'E', 'W', 'A', 'R', 'D', 'S', '-', '0', '0', '0', '2',
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

	private void oldPosSearchList(OutputStream os, InputStream is)
			throws Exception {
		byte[] msg = new byte[] {
				// SEQ
				0, 0, 0, 26,
				// ACK
				0, 0, 0, 0,
				// flags
				0, 0,
				// checksum
				0, 0,
				// message length
				0, 0, 0, 24,
				// command ID
				0, 0, 0, 1,
				// page
				0, 1,
				// size
				0, 6 };

		int checksum = Tools.checkSum(msg, msg.length);
		Tools.putUnsignedShort(msg, checksum, 10);

		// write response
		log.info(" list Send request to server");

		// send both message at once
		byte[] outBuf = new byte[msg.length];
		System.arraycopy(msg, 0, outBuf, 0, msg.length);

		os.write(outBuf);
		// ----------

		Thread.sleep(runForSeconds * 1000);
		// read
		log.info("Read response");
		byte[] response = new byte[364];
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
		assertEquals(0, response[8]);
		assertEquals(0, response[20]);
		assertEquals(0, response[21]);

	}

	private void newPosInit(OutputStream os, InputStream is, byte[] challenge,
			byte[] sessionId) throws Exception {
		byte[] msg = new byte[] {
				// SEQ
				0, 0, 0, 24,
				// ACK
				0, 0, 0, 0,
				// flags
				(byte) 128, 0,
				// checksum
				0, 0,
				// message length
				0, 0, 0, 36,
				// session description
				1, 0, 0, 0,
				// command ID
				0, 0, 0, 5,
				// POS ID
				'R', 'E', 'W', 'A', 'R', 'D', 'S', '-', '0', '0', '0', '2' };

		// calculate checksum
		int checksum = Tools.checkSum(msg, msg.length);
		Tools.putUnsignedShort(msg, checksum, 10);

		// send both message at once
		// int rubbishLength = 4;
		byte[] outBuf = new byte[msg.length];
		System.arraycopy(msg, 0, outBuf, 0, msg.length);

		os.write(outBuf);

		// ----------

		// session.write("Client First Message");
		Thread.sleep(runForSeconds * 1000);
		// read
		log.info("Read response");
		byte[] response = new byte[50];
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
		System.arraycopy(response, 42, challenge, 0, 8);
		System.arraycopy(response, 20, sessionId, 0, 16);
		assertEquals(0, response[40]);
		assertEquals(0, response[41]);
	}

	private void SendValidationCheckValidateCount(OutputStream os,
			InputStream is, byte[] sessionId, int validateCount)
			throws Exception {
		byte[] msg = new byte[] {
				// SEQ
				0, 0, 0, 27,
				// ACK
				0, 0, 0, 0,
				// flags
				(byte) 128, 0,
				// checksum
				0, 0,
				// message length
				0, 0, 0, 54,
				// session key dscription
				1, 0, 0, 16,
				// session key
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				// command ID
				0, 0, 0, 27,
				// grouponId
				49, 51, 50, 49, 50, 51, 0,
				// grouponVCode
				49, 49, 49, 49, 50, 50, 0 };

		Tools.putBytes(msg, sessionId, 20);

		int checksum = Tools.checkSum(msg, msg.length);
		Tools.putUnsignedShort(msg, checksum, 10);

		// write response
		log.info(" list Send request to server");

		// send both message at once
		byte[] outBuf = new byte[msg.length];
		System.arraycopy(msg, 0, outBuf, 0, msg.length);

		os.write(outBuf);
		// ----------

		Thread.sleep(runForSeconds * 1000);
		// read
		log.info("Read response");
		byte[] response = new byte[183];
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
		assertEquals(validateCount, response[41]);
		assertEquals(0, response[42]);
		assertEquals(0, response[43]);
		assertEquals(0, response[44]);
		assertEquals(0, response[45]);
		assertEquals(0, response[46]);
		assertEquals(0, response[47]);
		assertEquals(0, response[48]);
		assertEquals(0, response[49]);
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
