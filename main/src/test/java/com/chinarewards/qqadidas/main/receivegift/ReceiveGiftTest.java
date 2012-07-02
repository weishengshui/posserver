package com.chinarewards.qqadidas.main.receivegift;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.configuration.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqadidas.domain.QQActivityHistory;
import com.chinarewards.qqadidas.domain.QQActivityMember;
import com.chinarewards.qqadidas.domain.status.GiftStatus;
import com.chinarewards.qqadidas.domain.status.GiftType;
import com.chinarewards.qqadidas.main.protocol.cmd.PrivilegeExchangeResponseMessage;
import com.chinarewards.qqadidas.main.protocol.cmd.ReceiveGiftResponseMessage;
import com.chinarewards.qqgbpvn.main.CommonTestConfigModule;
import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.core.jpa.JpaPersistModuleBuilder;
import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.PosAssignment;
import com.chinarewards.qqgbvpn.domain.event.Journal;
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
import com.google.inject.persist.Transactional;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.util.Modules;

/**
 * qqadidas领取礼品测试
 * 
 * @author weishengshui
 * 
 */
public class ReceiveGiftTest {

	Logger log = LoggerFactory.getLogger(getClass());
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	PosServer posServer;
	int port;
	Injector injector;
	EntityManager em;
	public static final String TITLE = PrivilegeExchangeResponseMessage.TITLE;

	// cdkey不存在
	@Test
	public void test_recieveGift_cdkey_NOT_EXISTS() throws Exception {
		em.createQuery("delete from QQActivityHistory").executeUpdate();
		assertTrue(socket != null);
		int n;
		int checksum;
		byte[] response = new byte[100];

		// pos机初始化以及登录pos机
		posInitAndLogin();

		// 领取礼品请求包
		String cdkey = "123456";

		byte[] receiveGiftRequest = new byte[ProtocolLengths.HEAD
				+ ProtocolLengths.COMMAND + 2 + cdkey.length()];
		// 设置SEQ
		Tools.putBytes(receiveGiftRequest,
				new byte[] { 0x00, 0x00, 0x00, 0x10 }, 0);
		// 设置MESSAGESIZE
		Tools.putUnsignedInt(receiveGiftRequest, receiveGiftRequest.length, 12);
		// 设置cmd id 领取礼品请求的命令编号：201
		Tools.putUnsignedInt(receiveGiftRequest, 201, 16);
		Tools.putUnsignedShort(receiveGiftRequest, cdkey.length(), 20);
		Tools.putBytes(receiveGiftRequest, cdkey.getBytes(), 22);

		// 计算校验和
		checksum = Tools
				.checkSum(receiveGiftRequest, receiveGiftRequest.length);
		// 设置CHECKSUM
		Tools.putUnsignedShort(receiveGiftRequest, checksum, 10);
		// 发送初始化请求
		os.write(receiveGiftRequest);
		// 得到应答
		response = new byte[100];
		n = is.read(response);
		System.out.println("n:" + n);
		assertTrue(n != -1);
		// 领取礼品应答 CMD ID 是202
		byte[] cmdId = new byte[4];
		Tools.putUnsignedInt(cmdId, 202, 0);
		assertTrue(Arrays.equals(cmdId, Arrays.copyOfRange(response, 16, 20)));
		// result 是2
		assertTrue(Arrays.equals(new byte[] { 0x00, 0x00, 0x00, 0x02 },
				Arrays.copyOfRange(response, 16 + 4, 16 + 4 + 4)));
		// 交易时间
		Calendar cal = Tools.getDate(Arrays.copyOfRange(response, 16 + 4 + 4,
				16 + 4 + 4 + 11));
		System.out.println("交易时间： " + cal.getTime().toString());
		int titleLength = Tools.getUnsignedShort(response, 16 + 4 + 4 + 11);
		int tipLength = Tools.getUnsignedShort(response, 16 + 4 + 4 + 11 + 2
				+ titleLength);
		// 不需要打印小票，所以标题内容与频幕内容都是没有的，即它们的长度都是0
		assertEquals(0, titleLength);
		assertEquals(0, tipLength);

		// check db
		// 确认数据表的内容没有被改变
		List<QQActivityMember> qams = em.createQuery(
				"from QQActivityMember order by cdkey desc").getResultList();
		// // 初始化数据库时插入了两条会员记录
		assertEquals(2, qams.size());
		assertEquals("22222222222", qams.get(0).getCdkey());
		assertEquals(GiftStatus.DONE, qams.get(0).getGiftStatus());
		assertEquals("11111111111", qams.get(1).getCdkey());
		assertEquals(GiftStatus.NEW, qams.get(1).getGiftStatus());
		// 此时的历史记录表QQActivityHistory应该为空
		List<QQActivityHistory> qahs = em.createQuery("from QQActivityHistory")
				.getResultList();
		if (qahs != null) {
			System.out.println("**********:" + qahs.size());
			assertEquals(0, qahs.size());
		}
	}

	// 测试未领取过得礼品
	@Test
	public void test_recieveGift_NEW() throws Exception {
		String cdkey = "11111111111";
		assertTrue(socket != null);
		int n;
		int checksum;
		byte[] response = new byte[100];

		// pos机初始化以及登录pos机
		posInitAndLogin();

		// 领取礼品请求包
		byte[] receiveGiftRequest = new byte[ProtocolLengths.HEAD
				+ ProtocolLengths.COMMAND + ProtocolLengths.POSNETSTRLEN
				+ cdkey.length()];
		// 设置SEQ
		Tools.putBytes(receiveGiftRequest,
				new byte[] { 0x00, 0x00, 0x00, 0x10 }, 0);
		// 设置MESSAGESIZE
		Tools.putUnsignedInt(receiveGiftRequest, receiveGiftRequest.length, 12);
		// 设置cmd id 领取礼品请求的命令编号：201
		Tools.putUnsignedInt(receiveGiftRequest, 201, 16);
		Tools.putUnsignedShort(receiveGiftRequest, cdkey.length(), 20);
		Tools.putBytes(receiveGiftRequest, cdkey.getBytes(), 22);

		// 计算校验和
		checksum = Tools
				.checkSum(receiveGiftRequest, receiveGiftRequest.length);
		// 设置CHECKSUM
		Tools.putUnsignedShort(receiveGiftRequest, checksum, 10);
		// 发送领取礼品请求
		os.write(receiveGiftRequest);
		// 得到应答
		response = new byte[100];
		n = is.read(response);
		System.out.println("n:" + n);
		assertTrue(n != -1);
		// 领取礼品应答 CMD ID 是202
		byte[] cmdId = new byte[4];
		Tools.putUnsignedInt(cmdId, 202, 0);
		assertTrue(Arrays.equals(cmdId, Arrays.copyOfRange(response, 16, 20)));
		// result 是0表示交易成功
		assertTrue(Arrays.equals(new byte[] { 0x00, 0x00, 0x00, 0x00 },
				Arrays.copyOfRange(response, 16 + 4, 16 + 4 + 4)));
		// 交易时间
		Calendar cal = Tools.getDate(Arrays.copyOfRange(response, 16 + 4 + 4,
				16 + 4 + 4 + 11));
		System.out.println("交易时间： " + cal.getTime().toString());
		int titleLength = Tools.getUnsignedShort(response, 16 + 4 + 4 + 11);
		// 标题title
		assertTrue(ReceiveGiftResponseMessage.TITLE.equals(new String(Arrays
				.copyOfRange(response, 16 + 4 + 4 + 11 + 2, 16 + 4 + 4 + 11 + 2
						+ titleLength), "gbk")));
		// 屏幕内容tip
		int tipLength = Tools.getUnsignedShort(response, 16 + 4 + 4 + 11 + 2
				+ titleLength);
		assertTrue(ReceiveGiftResponseMessage.TIP.equals(new String(Arrays
				.copyOfRange(response, 16 + 4 + 4 + 11 + 2 + titleLength + 2,
						16 + 4 + 4 + 11 + 2 + titleLength + 2 + tipLength),
				"gbk")));
		// check db
		// 确认数据表的内容
		em.clear();
		List<QQActivityMember> qams = em.createQuery(
				"select qam from QQActivityMember qam order by qam.cdkey asc")
				.getResultList();
		// 初始化数据库时插入了两条会员记录
		assertEquals(2, qams.size());
		assertEquals("11111111111", qams.get(0).getCdkey());
		// 领取礼品的状态应该改变了
		assertEquals(GiftStatus.DONE, qams.get(0).getGiftStatus());
		assertEquals("22222222222", qams.get(1).getCdkey());
		assertEquals(GiftStatus.DONE, qams.get(1).getGiftStatus());
		// 此时的历史记录表QQActivityHistory应该有一条记录
		List<QQActivityHistory> qahs = em.createQuery("from QQActivityHistory")
				.getResultList();
		assertEquals(1, qahs.size());
		assertEquals("11111111111", qahs.get(0).getCdkey());
		assertEquals(GiftType.GIFT, qahs.get(0).getAType());
		List<Journal> journals = em
				.createQuery(
						"select j from Journal j where j.entity='QQACTIVITYMEMBER' and j.entityId='11111111111'")
				.getResultList();
		assertEquals(1, journals.size());
		Journal journal = journals.get(0);
		System.out.println("Journal[" + journal.getEntity() + ", "
				+ journal.getEntityId() + ", " + journal.getEvent() + ", "
				+ journal.getEventDetail() + ", " + journal.getTs());

		// 领取礼品之后，此时将会返回"已领取过"信息，发送领取礼品请求
		os.write(receiveGiftRequest);
		// 得到应答
		response = new byte[100];
		n = is.read(response);
		assertEquals(39, n);
		// 领取礼品应答 CMD ID 是202
		cmdId = new byte[4];
		Tools.putUnsignedInt(cmdId, 202, 0);
		assertTrue(Arrays.equals(cmdId, Arrays.copyOfRange(response, 16, 20)));
		// result 是1表示已领取过
		assertTrue(Arrays.equals(new byte[] { 0x00, 0x00, 0x00, 0x01 },
				Arrays.copyOfRange(response, 16 + 4, 16 + 4 + 4)));
		// 交易时间
		cal = Tools.getDate(Arrays.copyOfRange(response, 16 + 4 + 4,
				16 + 4 + 4 + 11));
		System.out.println("交易时间： " + cal.getTime().toString());
		titleLength = Tools.getUnsignedShort(response, 16 + 4 + 4 + 11);
		tipLength = Tools.getUnsignedShort(response, 16 + 4 + 4 + 11 + 2
				+ titleLength);
		// 不需要打印小票，所以标题内容与频幕内容都是没有的，即它们的长度都是0
		assertEquals(0, titleLength);
		assertEquals(0, tipLength);

		// check db，数据库的内容应该没有变
		qams = em.createQuery(
				"select qam from QQActivityMember qam order by qam.cdkey asc")
				.getResultList();
		// 初始化数据库时插入了两条会员记录
		assertEquals(2, qams.size());
		assertEquals("11111111111", qams.get(0).getCdkey());
		assertEquals(GiftStatus.DONE, qams.get(0).getGiftStatus());
		assertEquals("22222222222", qams.get(1).getCdkey());
		assertEquals(GiftStatus.DONE, qams.get(1).getGiftStatus());
		// 此时的历史记录表QQActivityHistory应该有一条记录
		qahs = em.createQuery("from QQActivityHistory").getResultList();
		assertEquals(1, qahs.size());
		assertEquals("11111111111", qahs.get(0).getCdkey());
		assertEquals(GiftType.GIFT, qahs.get(0).getAType());

	}

	// 测试礼品已领取过的情况
	@Test
	public void test_recieveGift_DONE() throws Exception {
		em.createQuery("delete from QQActivityHistory").executeUpdate();
		String cdkey = "22222222222";
		assertTrue(socket != null);
		int n;
		int checksum;
		byte[] response = new byte[100];

		// pos机初始化以及登录pos机
		posInitAndLogin();

		// 领取礼品请求包
		byte[] receiveGiftRequest = new byte[ProtocolLengths.HEAD
				+ ProtocolLengths.COMMAND + 2 + cdkey.length()];
		// 设置SEQ
		Tools.putBytes(receiveGiftRequest,
				new byte[] { 0x00, 0x00, 0x00, 0x10 }, 0);
		// 设置MESSAGESIZE
		Tools.putUnsignedInt(receiveGiftRequest, receiveGiftRequest.length, 12);
		// 设置cmd id 领取礼品请求的命令编号：201
		Tools.putUnsignedInt(receiveGiftRequest, 201, 16);
		Tools.putUnsignedShort(receiveGiftRequest, cdkey.length(), 20);
		Tools.putBytes(receiveGiftRequest, cdkey.getBytes(), 22);

		// 计算校验和
		checksum = Tools
				.checkSum(receiveGiftRequest, receiveGiftRequest.length);
		// 设置CHECKSUM
		Tools.putUnsignedShort(receiveGiftRequest, checksum, 10);
		// 发送领取礼品请求
		os.write(receiveGiftRequest);
		// 得到应答
		response = new byte[100];
		n = is.read(response);
		// 预计接收包的长度是39
		assertEquals(39, n);
		assertTrue(n != -1);
		// 领取礼品应答 CMD ID 是202
		byte[] cmdId = new byte[4];
		Tools.putUnsignedInt(cmdId, 202, 0);
		assertTrue(Arrays.equals(cmdId, Arrays.copyOfRange(response, 16, 20)));
		// result 是1表示礼品已领取过
		assertTrue(Arrays.equals(new byte[] { 0x00, 0x00, 0x00, 0x01 },
				Arrays.copyOfRange(response, 16 + 4, 16 + 4 + 4)));
		// 交易时间
		Calendar cal = Tools.getDate(Arrays.copyOfRange(response, 16 + 4 + 4,
				16 + 4 + 4 + 11));
		System.out.println("交易时间： " + cal.getTime().toString());
		int titleLength = Tools.getUnsignedShort(response, 16 + 4 + 4 + 11);
		int tipLength = Tools.getUnsignedShort(response, 16 + 4 + 4 + 11 + 2
				+ titleLength);
		// 不需要打印小票，所以标题内容与频幕内容都是没有的，即它们的长度都是0
		assertEquals(0, titleLength);
		assertEquals(0, tipLength);

		// check db
		em.clear();
		List<QQActivityMember> qams = em.createQuery(
				"select qam from QQActivityMember qam order by qam.cdkey asc")
				.getResultList();
		// 初始化数据库时插入了两条会员记录
		assertEquals(2, qams.size());
		assertEquals("11111111111", qams.get(0).getCdkey());
		// 领取礼品的状态应该改变了
		assertEquals(GiftStatus.NEW, qams.get(0).getGiftStatus());
		assertEquals("22222222222", qams.get(1).getCdkey());
		assertEquals(GiftStatus.DONE, qams.get(1).getGiftStatus());
		// 历史记录表应该是空的
		List<QQActivityHistory> qahs = em.createQuery("from QQActivityHistory")
				.getResultList();
		assertEquals(0, qahs.size());
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
		em.persist(pos);
		Agent agent = new Agent();
		agent.setName(agentName);
		em.persist(agent);
		PosAssignment pa = new PosAssignment();
		pa.setAgent(agent);
		pa.setPos(pos);
		em.persist(pa);

		String cdkey = "11111111111";
		QQActivityMember qm = new QQActivityMember();
		qm.setCdkey(cdkey);
		qm.setGiftStatus(GiftStatus.NEW);
		em.persist(qm);
		cdkey = "22222222222";
		QQActivityMember qm2 = new QQActivityMember();
		qm2.setCdkey(cdkey);
		qm2.setGiftStatus(GiftStatus.DONE);
		em.persist(qm2);

		em.flush();
	}

	private EntityManager getEm() {
		return em;
	}

	@Before
	public void setUp() throws Exception {

		createInjector();

		try {
			port = startServer();
			socket = new Socket("localhost", port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("setUp error message: " + e.getMessage());
		}
	}

	protected Module[] getModules() {

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

	@After
	public void tearDown() throws Exception {

		try {
			is.close();
			os.close();
			socket.close();
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			clearDb();
			stopServer();
		} catch (Exception e) {
			System.out.println("tearDown error message: " + e.getMessage());
			e.printStackTrace();
		}

	}

	private void clearDb() {
		em.getTransaction().begin();
		em.createQuery("delete QQActivityMember").executeUpdate();
		em.createQuery("delete QQActivityHistory").executeUpdate();
		em.createQuery("delete Journal").executeUpdate();
		em.getTransaction().commit();
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

		initDB();

		// make sure it is started, and port is correct
		assertFalse(posServer.isStopped());
		System.out.println("pos server start!");
		assertEquals(runningPort, posServer.getLocalPort());
		return runningPort;
		// sleep for a while...

	}

	private Injector getInjector() {
		return injector;
	}

	private void createInjector() {
		injector = Guice.createInjector(getModules());
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
