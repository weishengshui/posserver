package com.chinarewards.qqadidas.main.weixinsignin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.configuration.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqadidas.domain.QQWeixinSignIn;
import com.chinarewards.qqadidas.main.protocol.cmd.WeixinSignInResponseMessage;
import com.chinarewards.qqgbpvn.main.CommonTestConfigModule;
import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.core.jpa.JpaPersistModuleBuilder;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.status.PosDeliveryStatus;
import com.chinarewards.qqgbvpn.domain.status.PosInitializationStatus;
import com.chinarewards.qqgbvpn.domain.status.PosOperationStatus;
import com.chinarewards.qqgbvpn.main.ApplicationModule;
import com.chinarewards.qqgbvpn.main.PosServer;
import com.chinarewards.qqgbvpn.main.ServerModule;
import com.chinarewards.qqgbvpn.main.guice.AppModule;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandlerModule;
import com.chinarewards.qqgbvpn.main.protocol.ServiceMapping;
import com.chinarewards.qqgbvpn.main.protocol.ServiceMappingConfigBuilder;
import com.chinarewards.qqgbvpn.main.protocol.guice.ServiceHandlerGuiceModule;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.util.HMAC_MD5;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.util.Modules;

public class WeixinSignInProtocol {

	Logger log = LoggerFactory.getLogger(getClass());
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private PosServer posServer;
	private int port;
	private Injector injector;
	private EntityManager em;

	@Before
	public void setUp() throws Exception {
		createInjector();
		port = startServer();
		socket = new Socket("localhost", port);
		is = socket.getInputStream();
		os = socket.getOutputStream();
	}

	@After
	public void tearDown() throws Exception {
		if (em != null && em.getTransaction().isActive()) {
			em.getTransaction().rollback();
		}
		stopServer();
	}

	@Test
	public void test() throws Exception {

		assertNotNull(socket);
		// pos init and login
		posInitAndLogin();

		String[] weixinNos = new String[] { 
				"1",//length is 1
				"1111111111",//length is 10
				"1111111111111111",//length is 16
				};
		int index = 0;
		String posId = "pos-0002";
		// weixin signIn request cmdId is 205
		long requestCmdId = 205;
		// weixin signIn response cmdId is 206
		long responseCmdId = 206;
		String weixinNo;
		// case 1: weixinNo invalid(weixinNo's length is 0)
		weixinNo = "";
		weixinTest(requestCmdId, weixinNo, responseCmdId,
				WeixinSignInResponseMessage.RESULT_ERROR);
		// check db
		List<QQWeixinSignIn> qqweixins = getEm().createQuery(
				" from QQWeixinSignIn").getResultList();
		assertEquals(0, qqweixins.size());

		// case 2: weixinNo valid(weixinNo's length is 1)
		weixinNo = "1";
		weixinTest(requestCmdId, weixinNo, responseCmdId,
				WeixinSignInResponseMessage.RESULT_SUCCESS);
		// check db
		qqweixins = getEm().createQuery(
				" from QQWeixinSignIn qws order by qws.weixinNo asc").getResultList();
		// 此时，一条微信签到记录
		assertEquals(1, qqweixins.size());
		index = 0;
		for(QQWeixinSignIn qqweixin :qqweixins){
			assertEquals(posId, qqweixin.getPosId());
			assertEquals(weixinNos[index], qqweixin.getWeixinNo());
			index++;
		}

		// case 3: weixinNo valid(weixinNo's length is 10)
		weixinNo = "1111111111";
		weixinTest(requestCmdId, weixinNo, responseCmdId,
				WeixinSignInResponseMessage.RESULT_SUCCESS);
		// check db
		qqweixins = getEm().createQuery(
				" from QQWeixinSignIn qws order by qws.weixinNo asc").getResultList();
		// 此时，两条微信签到记录
		assertEquals(2, qqweixins.size());
		index = 0;
		for(QQWeixinSignIn qqweixin :qqweixins){
			assertEquals(posId, qqweixin.getPosId());
			assertEquals(weixinNos[index], qqweixin.getWeixinNo());
			index++;
		}

		// case 4: weixinNo valid(weixinNo's length is 16)
		weixinNo = "1111111111111111";
		weixinTest(requestCmdId, weixinNo, responseCmdId,
				WeixinSignInResponseMessage.RESULT_SUCCESS);
		// check db
		qqweixins = getEm().createQuery(
				" from QQWeixinSignIn qws order by qws.weixinNo asc").getResultList();
		// 此时，两条微信签到记录
		assertEquals(3, qqweixins.size());
		index = 0;
		for(QQWeixinSignIn qqweixin :qqweixins){
			assertEquals(posId, qqweixin.getPosId());
			assertEquals(weixinNos[index], qqweixin.getWeixinNo());
			index++;
		}

		// case 5: weixinNo invalid(weixinNo's length is 17)
		weixinNo = "11111111111111111";
		weixinTest(requestCmdId, weixinNo, responseCmdId,
				WeixinSignInResponseMessage.RESULT_ERROR);
		// check db
		qqweixins = getEm().createQuery(
				" from QQWeixinSignIn qws order by qws.weixinNo asc").getResultList();
		// 此时，两条微信签到记录
		assertEquals(3, qqweixins.size());
		index = 0;
		for(QQWeixinSignIn qqweixin :qqweixins){
			assertEquals(posId, qqweixin.getPosId());
			assertEquals(weixinNos[index], qqweixin.getWeixinNo());
			index++;
		}

	}

	private void weixinTest(long requestCmdId, String weixinNo,
			long responseCmdId, int result) throws Exception {

		byte[] seq = new byte[] { 0x00, 0x00, 0x00, 0x01 };
		byte[] ack = new byte[] { 0x00, 0x00, 0x00, 0x00 };
		// 预期接收包的长度
		int n = ProtocolLengths.HEAD + ProtocolLengths.COMMAND
				+ ProtocolLengths.QQADIDAS_RESULT_LENGTH;
		byte[] response = new byte[n + 1];
		// 请求包的总长度
		int messageSize = ProtocolLengths.HEAD + ProtocolLengths.COMMAND
				+ ProtocolLengths.POSNETSTRLEN + weixinNo.length();
		// 创建请求包
		byte[] request = new byte[messageSize];

		Tools.putBytes(request, seq, 0);
		Tools.putBytes(request, ack, 4);
		Tools.putUnsignedInt(request, messageSize, 12);
		Tools.putUnsignedInt(request, requestCmdId, 16);
		if (weixinNo == null || weixinNo.trim().length() == 0) {
			Tools.putUnsignedShort(request, 0, 20);
		} else {
			Tools.putUnsignedShort(request, weixinNo.length(), 20);
			Tools.putBytes(request, weixinNo.getBytes(), 22);
		}
		// 校验和
		int checkSum = Tools.checkSum(request, request.length);
		Tools.putUnsignedShort(request, checkSum, 10);

		// 发送请求
		os.write(request);

		// 接收包
		assertEquals(n, is.read(response));

		// 比较messageSize
		assertEquals(n, Tools.getUnsignedInt(response, 12));
		// 比较cmdId
		assertEquals(responseCmdId, Tools.getUnsignedInt(response, 16));
		// 比较result
		assertEquals(result, Tools.getUnsignedInt(response, 20));

	}

	private Module[] getModules() {

		CommonTestConfigModule confModule = new CommonTestConfigModule();
		ServiceMappingConfigBuilder mappingBuilder = new ServiceMappingConfigBuilder();
		Configuration configuration = confModule.getConfiguration();
		ServiceMapping mapping = mappingBuilder.buildMapping(configuration);
		JpaPersistModule jpaModule = new JpaPersistModule("posnet");
		JpaPersistModuleBuilder builder = new JpaPersistModuleBuilder();
		builder.configModule(jpaModule, configuration, "db");

		return new Module[] {
				new ApplicationModule(),
				confModule,
				jpaModule,
				new AppModule(),
				new ServerModule(),
				Modules.override(
						new ServiceHandlerModule(confModule.getConfiguration()))
						.with(new ServiceHandlerGuiceModule(mapping)) };

	}

	private Injector getInjector() {
		return injector;
	}

	private void createInjector() {
		injector = Guice.createInjector(getModules());
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

		em = getInjector().getInstance(EntityManager.class);
		em.getTransaction().begin();

		// we insert data here
		initDB();

		// make sure it is started, and port is correct
		assertFalse(posServer.isStopped());
		System.out.println("pos server start!");
		assertEquals(runningPort, posServer.getLocalPort());
		return runningPort;
		// sleep for a while...

	}

	private void initDB() {
		String posId1 = "pos-0002";
		String agentName = "agentName";

		Pos pos = new Pos();
		pos.setPosId(posId1);
		pos.setDstatus(PosDeliveryStatus.DELIVERED);
		pos.setSecret("000001");
		pos.setIstatus(PosInitializationStatus.INITED);
		pos.setOstatus(PosOperationStatus.ALLOWED);
		getEm().persist(pos);

		getEm().flush();

	}

	private EntityManager getEm() {
		return em;
	}

	private void stopServer() throws Exception {
		// stop it, and make sure it is stopped.
		posServer.stop();
		assertTrue(posServer.isStopped());
		log.info("posServer stopped");
	}

	private void posInitAndLogin() throws Exception {

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
		byte[] challenge = new byte[8];
		System.arraycopy(response, 22, challenge, 0, 8);

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
