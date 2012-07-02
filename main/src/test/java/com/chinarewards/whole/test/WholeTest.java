package com.chinarewards.whole.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.configuration.Configuration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbpvn.main.CommonTestConfigModule;
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
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandlerModule;
import com.chinarewards.qqgbvpn.main.protocol.ServiceMapping;
import com.chinarewards.qqgbvpn.main.protocol.ServiceMappingConfigBuilder;
import com.chinarewards.qqgbvpn.main.protocol.guice.ServiceHandlerGuiceModule;
import com.chinarewards.qqgbvpn.main.util.HMAC_MD5;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.util.Modules;

public class WholeTest {
	Logger log = LoggerFactory.getLogger(getClass());
	byte[] challenge = new byte[8];

	@Test
	public void testWholeServerLifecyle() throws Exception {

		// start server
		Injector injector = getInjector();
		Configuration conf = injector.getInstance(Configuration.class);
		conf.setProperty("server.port", 0);

		PosServer posServer = injector.getInstance(PosServer.class);
		assertTrue(posServer.isStopped());

		posServer.start();
		assertFalse(posServer.isStopped());
		int runningPort = posServer.getLocalPort();

		posServer.stop();
		assertTrue(posServer.isStopped());
		// // init db
		DefaultPosServer dserver = (DefaultPosServer) posServer;
		Injector injector2 = dserver.getInjector();
		EntityManager em = injector.getInstance(EntityManager.class);
		em.getTransaction().begin();
		initDb(em);

		conf.setProperty("server.port", runningPort);

		assertTrue(posServer.isStopped());

		// // server.start();
		posServer.start();
		assertFalse(posServer.isStopped());
		assertEquals(runningPort, posServer.getLocalPort());
		// communication with server
		Socket socket = new Socket("localhost", runningPort);
		assertTrue(socket != null);
		InputStream is = socket.getInputStream();
		OutputStream os = socket.getOutputStream();
		// // assemble byte array, header + body

		// ////pos init
		posInit(is, os);
		// ////pos login
		posLogin(is, os);

		// check response byte
		// // expected xxxx...

		// check server db
		// //How to get entitymanager

		// communication 2
		// clear db

		// stop server

		em.getTransaction().rollback();
		posServer.stop();
	}

	private Injector getInjector() {

		Injector injector = Guice.createInjector(getModules());
		return injector;
	}

	private Module[] getModules() {
		CommonTestConfigModule configModule = new CommonTestConfigModule();
		ServiceMappingConfigBuilder mappingConfigBuilder = new ServiceMappingConfigBuilder();
		ServiceMapping mapping = mappingConfigBuilder.buildMapping(configModule
				.getConfiguration());

		Module[] modules = new Module[] {
				new ApplicationModule(),
				new CommonTestConfigModule(),
				buildPersistModule(configModule.getConfiguration()),
				new ServerModule(),
				new AppModule(),
				Modules.override(
						new ServiceHandlerModule(configModule
								.getConfiguration())).with(
						new ServiceHandlerGuiceModule(mapping)) };
		return modules;
	}

	private Module buildPersistModule(Configuration configuration) {
		JpaPersistModule jpaPersistModule = new JpaPersistModule("posnet");
		JpaPersistModuleBuilder b = new JpaPersistModuleBuilder();
		b.configModule(jpaPersistModule, configuration, "db");

		return jpaPersistModule;
	}

	private void initDb(EntityManager em) {

		String posId1 = "REWARDS-0003";
		String posId2 = "pos-0002";
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

		List<Pos> pp = em.createQuery("select p from Pos p").getResultList();
		log.debug("pp={}", pp);
		System.out.println("pp size:" + pp.size());
		if (pp != null) {
			for (Pos p : pp) {
				log.debug("getPosId : {}", p.getPosId());
			}
		}
	}

	private void posInit(InputStream is, OutputStream os) throws Exception {
		int n;
		int checksum;
		byte[] response = new byte[100];

		// init 初始化POS机
		byte[] initContents = new byte[] {
				// SEQ 0
				0x00, 0x00, 0x00, 0x01,
				// ACK 4
				0x00, 0x00, 0x00, 0x00,
				// FLAGS 8
				0x00, 0x00,
				// CHECKSUM 10
				0x00, 0x00,
				// MESSAGESIZE 12
				0x00, 0x00, 0x00, 0x20,
				// CMD ID 16 请求的CMD ID是5
				0x00, 0x00, 0x00, 0x05,
				// posId 20
				'p', 'o', 's', '-', '0', '0', '0', '2', '\0', '\0', '\0', '\0', };
		// 计算校验和
		checksum = Tools.checkSum(initContents, initContents.length);
		Tools.putUnsignedShort(initContents, checksum, 10);
		// 发送初始化请求
		os.write(initContents);
		// 得到应答
		n = is.read(response);
		assertTrue(n != -1);
		// 应答包的SEQ
		assertTrue(Arrays.equals(new byte[] { 0, 0, 0, 1 },
				Arrays.copyOfRange(response, 0, 4)));
		// 应答包的ACK
		assertTrue(Arrays.equals(new byte[] { 0, 0, 0, 0 },
				Arrays.copyOfRange(response, 4, 8)));
		// 应答包的MESSAGE SIZE是30
		assertTrue(Arrays.equals(new byte[] { 0, 0, 0, 30 },
				Arrays.copyOfRange(response, 12, 16)));
		// POS INIT 应答 CMD ID 是6
		assertTrue(Arrays.equals(new byte[] { 0, 0, 0, 6 },
				Arrays.copyOfRange(response, 16, 20)));
		// POS机已经初始化
		assertTrue(Arrays.equals(new byte[] { 0, 0 },
				Arrays.copyOfRange(response, 20, 22)));
		// challenge随机安全码（初始化POS机时生成），获取随机安全码用于登录

		System.arraycopy(response, 22, challenge, 0, 8);
	}

	private void posLogin(InputStream is, OutputStream os) throws Exception {

		int n;
		int checksum;
		byte[] response = new byte[100];
		// login 登录POS机
		byte[] loginContents = new byte[] {
				// SEQ
				0x00, 0x00, 0x00, 0x01,
				// ACK
				0x00, 0x00, 0x00, 0x00,
				// FLAGS
				0x00, 0x00,
				// CHECKSUM
				0x00, 0x00,
				// MESSAGESIZE
				0x00, 0x00, 0x00, 0x30,
				// CMD ID 登录POS的CMD ID 是7
				0x00, 0x00, 0x00, 0x07,
				// posId
				'p', 'o', 's', '-', '0', '0', '0', '2', '\0', '\0', '\0', '\0',
				// challengeResponse 由随机安全码challenge与POS的secret两者加密得到
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		String pos_secret = "000001";
		// 将challenge于pos_secret两者加密得到challengeResponse
		byte[] content2 = HMAC_MD5.getSecretContent(challenge, pos_secret);
		// 将加密后的结果复制过去
		System.arraycopy(content2, 0, loginContents, 32, content2.length);
		// 计算校验和
		checksum = Tools.checkSum(loginContents, loginContents.length);
		Tools.putUnsignedShort(loginContents, checksum, 10);
		// 发送登录请求
		os.write(loginContents);
		response = new byte[100];
		// 得到应答
		n = is.read(response);
		// 应答包的SEQ
		assertTrue(Arrays.equals(new byte[] { 0, 0, 0, 1 },
				Arrays.copyOfRange(response, 0, 4)));
		// 应答包的ACK
		assertTrue(Arrays.equals(new byte[] { 0, 0, 0, 0 },
				Arrays.copyOfRange(response, 4, 8)));
		// 应答包的MESSAGE SIZE是30
		assertTrue(Arrays.equals(new byte[] { 0, 0, 0, 30 },
				Arrays.copyOfRange(response, 12, 16)));
		// POS LOGIN应答 CMD ID 是8
		assertTrue(Arrays.equals(new byte[] { 0, 0, 0, 8 },
				Arrays.copyOfRange(response, 16, 20)));
		// 登陆成功
		assertTrue(Arrays.equals(new byte[] { 0, 0 },
				Arrays.copyOfRange(response, 20, 22)));
		// 登录成功后将得到一个新的challenge随机安全码，暂时还不知道为什么登录后要新获取一个challenge，断线重连？
		System.arraycopy(response, 22, challenge, 0, challenge.length);

	}
}
