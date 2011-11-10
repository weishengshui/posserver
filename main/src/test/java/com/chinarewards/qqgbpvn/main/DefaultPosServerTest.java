package com.chinarewards.qqgbpvn.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.chinarewards.qqgbpvn.main.test.GuiceTest;
import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.core.jpa.JpaPersistModuleBuilder;
import com.chinarewards.qqgbvpn.main.ApplicationModule;
import com.chinarewards.qqgbvpn.main.PosServer;
import com.chinarewards.qqgbvpn.main.ServerModule;
import com.chinarewards.qqgbvpn.main.guice.AppModule;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandlerModule;
import com.chinarewards.qqgbvpn.main.protocol.ServiceMapping;
import com.chinarewards.qqgbvpn.main.protocol.ServiceMappingConfigBuilder;
import com.chinarewards.qqgbvpn.main.protocol.guice.ServiceHandlerGuiceModule;
import com.chinarewards.qqgbvpn.main.util.HMAC_MD5;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.util.Modules;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class DefaultPosServerTest extends GuiceTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
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

	protected Module buildTestConfigModule() {

		Configuration conf = new BaseConfiguration();
		// hard-coded config
		conf.setProperty("server.port", 0);
		// persistence
		conf.setProperty("db.user", "sa");
		conf.setProperty("db.password", "");
		conf.setProperty("db.driver", "org.hsqldb.jdbcDriver");
		conf.setProperty("db.url", "jdbc:hsqldb:.");
		// additional Hibernate properties
		conf.setProperty("db.ext.hibernate.dialect",
				"org.hibernate.dialect.HSQLDialect");
		conf.setProperty("db.ext.hibernate.show_sql", true);
		// URL for QQ
		conf.setProperty("qq.groupbuy.url.groupBuyingSearchGroupon",
				"http://localhost:8086/qqapi");
		conf.setProperty("qq.groupbuy.url.groupBuyingValidationUrl",
				"http://localhost:8086/qqapi");
		conf.setProperty("qq.groupbuy.url.groupBuyingUnbindPosUrl",
				"http://localhost:8086/qqapi");

		TestConfigModule confModule = new TestConfigModule(conf);
		return confModule;
	}

	protected Module buildPersistModule(Configuration config) {

		JpaPersistModule jpaModule = new JpaPersistModule("posnet");
		// config it.

		JpaPersistModuleBuilder b = new JpaPersistModuleBuilder();
		b.configModule(jpaModule, config, "db");

		return jpaModule;
	}
	
	public void testStart_RandomPort() throws Exception {

		// force changing of configuration
		Configuration conf = getInjector().getInstance(Configuration.class);
		conf.setProperty("server.port", 0);

		// get an new instance of PosServer
		PosServer server = getInjector().getInstance(PosServer.class);

		// make sure it is stopped
		assertTrue(server.isStopped());

		// start it!
		server.start();

		// make sure it is started, and port is correct
		assertFalse(server.isStopped());
		assertTrue(0 != server.getLocalPort());
		assertTrue(server.getLocalPort() > 0);

		// sleep for a while...
		Thread.sleep(500); // 0.5 seconds

		// stop it, and make sure it is stopped.
		server.stop();
		assertTrue(server.isStopped());

		log.info("Server stopped");

	}

	@Test
	public void testStart() throws Exception {

		// force changing of configuration
		Configuration conf = getInjector().getInstance(Configuration.class);
		conf.setProperty("server.port", 0);

		// get an new instance of PosServer
		PosServer server = getInjector().getInstance(PosServer.class);
		// make sure it is started, and port is correct
		assertTrue(server.isStopped());
		//
		// start it!
		server.start();
		int runningPort = server.getLocalPort();
		// stop it.
		server.stop();
		assertTrue(server.isStopped());

		//
		// Now we know which free port to use.
		//
		// XXX it is a bit risky since the port maybe in use by another
		// process.
		//

		// get an new instance of PosServer
		conf.setProperty("server.port", runningPort);

		// make sure it is stopped
		assertTrue(server.isStopped());

		// start it!
		server.start();

		// make sure it is started, and port is correct
		assertFalse(server.isStopped());
		assertEquals(runningPort, server.getLocalPort());

		// sleep for a while...
//		Thread.sleep(500); // 0.5 seconds
//		log.trace("one socket...");
//		//测试旧的pos机正常流程
//		createSocket1(server.getLocalPort());
//		
//		Thread.sleep(1000); // 1 seconds
//		log.trace("two socket...");
//		//测试新的pos机正常流程
//		createSocket2(server.getLocalPort());
//		
//		Thread.sleep(1000); // 1 seconds
//		log.trace("three socket.......");
//		//测试断线，后重连
//		createSocket3(server.getLocalPort());

		
		// stop it, and make sure it is stopped.
		server.stop();
		assertTrue(server.isStopped());

		log.info("Server stopped");

	}
	
	private void createSocket3(int port) throws Exception {
		Socket socket = new Socket("localhost", port);
		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();
		byte[] challenge = new byte[8];
		byte[] sessionId = new byte[16];
		
		log.debug("start init ......");
		this.newPosInit(os, is , challenge ,sessionId);// new client add session key protocol
		
		log.debug("start login ......");
		newPosLogin(os, is, challenge);
		
		log.debug("start list ......");
		newPosSearchList(os, is);
		
		System.out.println("");
		os.close();
		socket.close();
		
		log.debug("send session id socket........");
		Socket socket2 = new Socket("localhost", port);
		OutputStream os2 = socket2.getOutputStream();
		InputStream is2 = socket2.getInputStream();
		
		log.debug("start validate ......");
		newPosValidateSendSessionKey(os2, is2 , sessionId);
		
		log.debug("start list ......");
		newPosSearchList(os2, is2);
		
		System.out.println("");
		os2.close();
		socket2.close();
		
		
	}
	
	
	private void createSocket2(int port) throws Exception {
		Socket socket = new Socket("localhost", port);
		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();
		byte[] challenge = new byte[8];
		byte[] sessionId = new byte[16];
		
		log.debug("start init ......");
		this.newPosInit(os, is , challenge, sessionId);// new client add session key protocol
		
		log.debug("start login ......");
		newPosLogin(os, is, challenge);
		
		log.debug("start list ......");
		newPosSearchList(os, is);
		
		log.debug("start validate ......");
		newPosValidate(os, is);
		
		System.out.println("");
		os.close();
		socket.close();
	}

	private void createSocket1(int port) throws Exception {
		Socket socket = new Socket("localhost", port);
		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();
		
		byte[] challenge = new byte[8];
		
		this.oldPosInit(os, is, challenge);// old  client
		
		log.debug("start login ......");
		oldPosLogin(os, is, challenge);
		
		log.debug("start list ......");
		oldPosSearchList(os, is);
		
		log.debug("start validate ......");
		oldPosValidate(os, is);
		
		System.out.println("");
		os.close();
		socket.close();

	}

	private void oldPosInit(OutputStream os, InputStream is, byte[] challenge) throws Exception {
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

		long runForSeconds = 3;

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
//		System.out.println("--------------------");
//		for (int i = 0; i < msg.length; i++) {
//			String s = Integer.toHexString((byte) msg[i]);
//			if (s.length() < 2)
//				s = "0" + s;
//			if (s.length() > 2)
//				s = s.substring(s.length() - 2);
//			System.out.print(s + " ");
//			if ((i + 1) % 8 == 0)
//				System.out.println("");
//		}
//		System.out.println("--------------------");
		long runForSeconds = 1;
		// send both message at once
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
				0,1,
				//size
				0,6				
				};

		int checksum = Tools.checkSum(msg, msg.length);
		Tools.putUnsignedShort(msg, checksum, 10);

		long runForSeconds = 3;
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
	
	}
	
	private void oldPosValidate(OutputStream os, InputStream is)
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
				0, 0, 0, 3,
				// grouponId
				49, 51, 50, 49, 50, 51,0,
				// grouponVCode
				49, 49, 49, 49, 49, 49, 0 };

		int checksum = Tools.checkSum(msg, msg.length);
		Tools.putUnsignedShort(msg, checksum, 10);

		long runForSeconds = 3;
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
		byte[] response = new byte[110];
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
		assertEquals(response[21],0);
	}
	
	
	private void newPosInit(OutputStream os, InputStream is, byte[] challenge, byte[] sessionId) throws Exception {
		byte[] msg = new byte[] {
				// SEQ
				0, 0, 0, 24,
				// ACK
				0, 0, 0, 0,
				 // flags
				(byte)128, 0,
				// checksum
				0, 0,
				// message length
				0, 0, 0, 36,
				//session description
				1,0,0,0,
				// command ID
				0, 0, 0, 5,
				// POS ID
				'R', 'E', 'W', 'A', 'R', 'D', 'S', '-', '0', '0', '0', '2' };

		// calculate checksum
		int checksum = Tools.checkSum(msg, msg.length);
		Tools.putUnsignedShort(msg, checksum, 10);

		long runForSeconds = 3;

		// send both message at once
//		int rubbishLength = 4;
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
	}

	private void newPosLogin(OutputStream os, InputStream is, byte[] challenge)
			throws Exception {
		byte[] msg = new byte[] {
				// SEQ
				0, 0, 0, 25,
				// ACK
				0, 0, 0, 0,
				// flags
				(byte)128, 0,
				// checksum
				0, 0,
				// message length
				0, 0, 0, 52,
				//session description
				1,0,0,0,
				// command ID
				0, 0, 0, 7,
				// POS ID
				'R', 'E', 'W', 'A', 'R', 'D', 'S', '-', '0', '0', '0', '2',
				// challengeResponse
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		byte[] content2 = HMAC_MD5.getSecretContent(challenge, "000001");
		Tools.putBytes(msg, content2, 36);
		int checksum = Tools.checkSum(msg, msg.length);
		Tools.putUnsignedShort(msg, checksum, 10);

		long runForSeconds = 3;
		// write response
		log.info(" Login Send request to server");

		// send both message at once
		byte[] outBuf = new byte[msg.length];
		System.arraycopy(msg, 0, outBuf, 0, msg.length);

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

	}

	private void newPosSearchList(OutputStream os, InputStream is)
			throws Exception {
		byte[] msg = new byte[] {
				// SEQ
				0, 0, 0, 26,
				// ACK
				0, 0, 0, 0,
				// flags
				(byte)128, 0,
				// checksum
				0, 0,
				// message length
				0, 0, 0, 28,
				//session key description
				1, 0, 0, 0,
				// command ID
				0, 0, 0, 1,
				// page
				0,1,
				//size
				0,6				
				};

		int checksum = Tools.checkSum(msg, msg.length);
		Tools.putUnsignedShort(msg, checksum, 10);

		long runForSeconds = 3;
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
		
	}
	
	private void newPosValidate(OutputStream os, InputStream is)
			throws Exception {
		byte[] msg = new byte[] {
				// SEQ
				0, 0, 0, 27,
				// ACK
				0, 0, 0, 0,
				// flags
				(byte)128, 0,
				// checksum
				0, 0,
				// message length
				0, 0, 0, 38,
				//session key dscription
				1,0,0,0,
				// command ID
				0, 0, 0, 3,
				// grouponId
				49, 51, 50, 49, 50, 51,0,
				// grouponVCode
				49, 49, 49, 49, 50, 50, 0 };

		int checksum = Tools.checkSum(msg, msg.length);
		Tools.putUnsignedShort(msg, checksum, 10);

		long runForSeconds = 3;
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
		byte[] response = new byte[110];
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
		assertEquals(response[21],0);
	}
	
	private void newPosValidateSendSessionKey(OutputStream os, InputStream is , byte[] sessionId)
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
				//session key
				0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,
				// command ID
				0, 0, 0, 3,
				// grouponId
				49, 51, 50, 49, 50, 51, 0,
				// grouponVCode
				49, 49, 49, 49, 50, 50, 0 };

		Tools.putBytes(msg, sessionId, 20);
		
		int checksum = Tools.checkSum(msg, msg.length);
		Tools.putUnsignedShort(msg, checksum, 10);
		
		long runForSeconds = 3;
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
		byte[] response = new byte[136];
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
		assertEquals(response[41], 0);
	}

}
