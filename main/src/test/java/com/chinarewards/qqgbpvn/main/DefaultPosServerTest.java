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
		Thread.sleep(500); // 0.5 seconds

		createSocket(server.getLocalPort());

		// stop it, and make sure it is stopped.
		server.stop();
		assertTrue(server.isStopped());

		log.info("Server stopped");

	}

	private void createSocket(int port) throws Exception {

		Socket socket = new Socket("localhost", port);

		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();

		// byte[] msg = new byte[] {
		// // SEQ
		// 0, 0, 0, 24,
		// // ACK
		// 0, 0, 0, 0,
		// // flags
		// 0, 0,
		// // checksum
		// 0, 0,
		// // message length
		// 0, 0, 0, 32,
		// // command ID
		// 0, 0, 0, 5,
		// // POS ID
		// 'R', 'E', 'W', 'A', 'R', 'D', 'S', '-', '0', '0', '0', '3' };
		// byte[] msg2 = new byte[] {
		// // SEQ
		// 0, 0, 0, 25,
		// // ACK
		// 0, 0, 0, 0,
		// // flags
		// 0, 0,
		// // checksum
		// 0, 0,
		// // message length
		// 0, 0, 0, 32,
		// // command ID
		// 0, 0, 0, 5,
		// // POS ID
		// 'R', 'E', 'W', 'A', 'R', 'D', 'S', '-', '0', '0', '0', '2' };
		//
		// // calculate checksum
		// int checksum = Tools.checkSum(msg, msg.length);
		// Tools.putUnsignedShort(msg, checksum, 10);
		// // calculate checksum
		// int checksum2 = Tools.checkSum(msg2, msg2.length);
		// Tools.putUnsignedShort(msg2, checksum2, 10);
		//
		//
		// long runForSeconds = 60;
		// // write response
		// log.info("Send request to server");
		//
		// // send both message at once
		// int rubbishLength = 4;
		// byte[] outBuf = new byte[msg.length + msg2.length + rubbishLength];
		// System.arraycopy(msg, 0, outBuf, 0, msg.length);
		// System.arraycopy(msg2, 0, outBuf, msg.length, msg2.length);
		//
		// os.write(outBuf);
		//
		// // ----------
		//
		// // session.write("Client First Message");
		// Thread.sleep(runForSeconds * 1000);
		// // read
		// log.info("Read response");
		// byte[] response = new byte[30];
		// int n = is.read(response);
		// System.out.println("Number of bytes read: " + n);
		// for (int i = 0; i < n; i++) {
		// String s = Integer.toHexString((byte) response[i]);
		// if (s.length() < 2)
		// s = "0" + s;
		// if (s.length() > 2)
		// s = s.substring(s.length() - 2);
		// System.out.print(s + " ");
		// if ((i + 1) % 8 == 0)
		// System.out.println("");
		// }
//		this.posinit(os, is);
		log.debug("pos---------------2--------------------start--------------");
		this.posinit2(os, is);
		System.out.println("");
		os.close();
		socket.close();

	}



	private void posinit(OutputStream os, InputStream is) throws Exception {
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
//		byte[] msg2 = new byte[] {
//				// SEQ
//				0, 0, 0, 25,
//				// ACK
//				0, 0, 0, 0,
//				// flags
//				0, 0,
//				// checksum
//				0, 0,
//				// message length
//				0, 0, 0, 32,
//				// command ID
//				0, 0, 0, 5,
//				// POS ID
//				'R', 'E', 'W', 'A', 'R', 'D', 'S', '-', '0', '0', '0', '2' };

		// calculate checksum
		int checksum = Tools.checkSum(msg, msg.length);
		Tools.putUnsignedShort(msg, checksum, 10);
		// calculate checksum
//		int checksum2 = Tools.checkSum(msg2, msg2.length);
//		Tools.putUnsignedShort(msg2, checksum2, 10);

		long runForSeconds = 3;
		// write response
		log.info(" Init Send request to server");

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
	
		byte[] challenge2 = new byte[8];
		System.arraycopy(response, 22, challenge2, 0, 8);

		log.debug("start login ......");
		posLogin(os, is, challenge2);

	}

	private void posLogin(OutputStream os, InputStream is, byte[] challenge2)
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

		byte[] content2 = HMAC_MD5.getSecretContent(challenge2, "000001");
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
		log.debug("end login .....");
		
		log.debug("start list ......");
		posSearchList(os, is);

	}

	private void posSearchList(OutputStream os, InputStream is)
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
		log.debug("end list .....");
		
		log.debug("start validate ......");
		posValidate(os, is);
	}
	
	private void posValidate(OutputStream os, InputStream is)
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
				1, 2, 3, 4, 5, 6, 0 };

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
		log.debug("end validate .....");
	}
	
	
	private void posinit2(OutputStream os, InputStream is) throws Exception {
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

		long runForSeconds = 3;
		// write response
		log.info(" Init Send request to server");

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
	
		byte[] challenge2 = new byte[8];
		System.arraycopy(response, 22, challenge2, 0, 8);

		log.debug("start login ......");
//		posLogin2(os, is, challenge2);

	}

	private void posLogin2(OutputStream os, InputStream is, byte[] challenge2)
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

		byte[] content2 = HMAC_MD5.getSecretContent(challenge2, "000001");
		Tools.putBytes(msg, content2, 32);
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
		log.debug("end login .....");
		
		log.debug("start list ......");
		posSearchList2(os, is);

	}

	private void posSearchList2(OutputStream os, InputStream is)
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
		log.debug("end list .....");
		
		log.debug("start validate ......");
		posValidate2(os, is);
	}
	
	private void posValidate2(OutputStream os, InputStream is)
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
				1, 2, 3, 4, 5, 6, 0 };

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
		log.debug("end validate .....");
	}

}
