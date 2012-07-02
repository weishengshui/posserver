package com.chinarewards.qqadidas.main.privilegeexchange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.configuration.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.chinarewards.qqadidas.domain.QQActivityHistory;
import com.chinarewards.qqadidas.domain.QQActivityMember;
import com.chinarewards.qqadidas.domain.status.PrivilegeStatus;
import com.chinarewards.qqadidas.main.protocol.cmd.PrivilegeExchangeResponseMessage;
import com.chinarewards.qqgbpvn.main.CommonTestConfigModule;
import com.chinarewards.qqgbpvn.main.test.JpaGuiceTest;
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
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.util.HMAC_MD5;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.util.Modules;

/**
 * qqadidas 权益兑换服务端协议测试
 * 
 * @author weishengshui
 * 
 */
public class PrivilegeExchangeProtocolTest extends JpaGuiceTest {

	private Socket socket;
	private InputStream is;
	private OutputStream os;
	EntityManager em;
	PosServer posServer;
	int port;
	public static final String TITLE = PrivilegeExchangeResponseMessage.TITLE;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		try {
			port = startServer();
			socket = new Socket("localhost", port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
		} catch (Exception e) {
			System.out.println("setUp error message: " + e.getMessage());
		}
	}

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

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		try {
			is.close();
			os.close();
			socket.close();
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			stopServer();
		} catch (Exception e) {
			System.out.println("tearDown error message: " + e.getMessage());
		}
		
	}

	// cdkey正确，会员之前未消费过，本次消费金额为600元及以上，返回"交易完成，返回100元"
	@Test
	public void test_NOTSPENDING_AMOUNT_MORE_THAN_600() throws Exception {
		assertTrue(socket != null);
		// POS初始化以及登录POS机
		posInitAndLogin();

		String cdkey = "11111111111";
		// 设置消费金额
		String amount = "602";
		// result 0 表示交易成功
		byte[] result = new byte[] { 0x00, 0x00, 0x00, 0x00 };
		String tip = cdkey + "本次消费" + amount + "元，即刻享受100元折扣优惠，折扣后实际支付为"
				+ (Double.parseDouble(amount) - 100)
				+ "元。本券打印后仅限当次使用。即日起至6月19日止。";
		test_PRIVILEGE(cdkey, amount, result, TITLE, tip);
		// check db
		// //check QQActivityMember table
		Query q = em
				.createQuery("select qm.privilegeStatus from QQActivityMember qm where qm.cdkey=?1");
		q.setParameter(1, cdkey);
		PrivilegeStatus pstatus = (PrivilegeStatus) q.getSingleResult();
		// 会员已经获取全部优惠
		assertTrue(pstatus.equals(PrivilegeStatus.DONE));
		// //check QQActivityHistory table
		q = em.createQuery("select qh from QQActivityHistory qh where qh.cdkey=?1");
		q.setParameter(1, cdkey);
		List<QQActivityHistory> qhs = q.getResultList();
		// 1条历史记录
		assertEquals(1, qhs.size());
		QQActivityHistory qh = qhs.get(0);
		assertEquals("pos-0002", qh.getPosId());
	}

	/**
	 * 包含5个测试用例
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_PRIVILEGE_EXCHANGE() throws Exception {
		assertTrue(socket != null);
		// POS初始化以及登录POS机
		posInitAndLogin();
		// 验证码
		String cdkey = "33333333333";
		// 消费金额
		String amount;
		byte[] result;

		// case 1：CDKEY正确，本次的消费金额为 0 - 299元： pos机显示 "消费**元,未能获得优惠"
		cdkey = "33333333333";
		// 设置消费金额
		amount = "262";
		// result 03表示消费金额不足
		result = new byte[] { 0x00, 0x00, 0x00, 0x03 };
		test_PRIVILEGE(cdkey, amount, result, null, null);
		// check db
		// //check QQActivityMember table
		Query q = em
				.createQuery("select qm.privilegeStatus from QQActivityMember qm where qm.cdkey=?1");
		q.setParameter(1, cdkey);
		PrivilegeStatus pstatus = (PrivilegeStatus) q.getSingleResult();
		// 会员还是没有消费过
		assertTrue(pstatus.equals(PrivilegeStatus.NEW));
		// //check QQActivityHistory table
		q = em.createQuery("select qh from QQActivityHistory qh where qh.cdkey=?1");
		q.setParameter(1, cdkey);
		List<QQActivityHistory> qhs = q.getResultList();
		// 0条历史记录
		assertEquals(0, qhs.size());

		// case 2：CDKEY正确，会员之前已经返还过50元， 本次的消费金额为 300元及以上，返回"交易完成，返回50元"
		cdkey = "33333333333";
		// 设置消费金额
		amount = "370";
		// result 0 表示交易成功
		result = new byte[] { 0x00, 0x00, 0x00, 0x00 };
		String tip = cdkey + "本次消费" + amount + "元，即刻享受50元折扣优惠，折扣后实际支付为"
				+ (Double.parseDouble(amount) - 50)
				+ "元。您尚有一次满额折抵50元优惠可用。本券打印后仅限当次使用。即日起至6月19日止。";
		test_PRIVILEGE(cdkey, amount, result, TITLE, tip);
		// check db
		// //check QQActivityMember table
		q = em.createQuery("select qm.privilegeStatus from QQActivityMember qm where qm.cdkey=?1");
		q.setParameter(1, cdkey);
		pstatus = (PrivilegeStatus) q.getSingleResult();
		// 已经返回给会员50元
		assertTrue(pstatus.equals(PrivilegeStatus.HALF));
		// //check QQActivityHistory table
		q = em.createQuery("select qh from QQActivityHistory qh where qh.cdkey=?1");
		q.setParameter(1, cdkey);
		qhs = q.getResultList();
		// 1条历史记录
		assertEquals(1, qhs.size());

		// case 3：CDKEY正确，会员之前已经返还过50元， 本次的消费金额为 300元及以上： 1. pos机显示 "交易完成" 2.
		// 打印小票
		// [显示返还50元]
		cdkey = "33333333333";
		// 设置消费金额
		amount = "602";
		// result 0 表示交易成功
		result = new byte[] { 0x00, 0x00, 0x00, 0x00 };
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		tip = cdkey + "在" + format.format(new Date()) + "获得50元折扣优惠。本次消费 "
				+ amount + "元，即刻享受50元折扣优惠，折扣后实际支付额为"
				+ (Double.parseDouble(amount) - 50)
				+ "元。本券打印后仅限当次使用。即日起至6月19日止。";
		test_PRIVILEGE(cdkey, amount, result, TITLE, tip);
		// check db
		// //check QQActivityMember table
		q = em.createQuery("select qm.privilegeStatus from QQActivityMember qm where qm.cdkey=?1");
		q.setParameter(1, cdkey);
		pstatus = (PrivilegeStatus) q.getSingleResult();
		// 已经返回给会员100元
		assertTrue(pstatus.equals(PrivilegeStatus.DONE));
		// //check QQActivityHistory table
		q = em.createQuery("select qh from QQActivityHistory qh where qh.cdkey=?1");
		q.setParameter(1, cdkey);
		qhs = q.getResultList();
		// 2条历史记录
		assertEquals(2, qhs.size());

		// case 4：CDKEY正确，会员之前已经返还过100元，pos机显示 "优惠已全部使用"
		cdkey = "33333333333";
		// 设置消费金额
		amount = "352";
		// result 1表示优惠已全部使用
		result = new byte[] { 0x00, 0x00, 0x00, 0x01 };
		test_PRIVILEGE(cdkey, amount, result, null, null);
		// check db
		// //check QQActivityMember table
		q = em.createQuery("select qm.privilegeStatus from QQActivityMember qm where qm.cdkey=?1");
		q.setParameter(1, cdkey);
		pstatus = (PrivilegeStatus) q.getSingleResult();
		// 已经返回给会员100元
		assertTrue(pstatus.equals(PrivilegeStatus.DONE));
		// //check QQActivityHistory table
		q = em.createQuery("select qh from QQActivityHistory qh where qh.cdkey=?1");
		q.setParameter(1, cdkey);
		qhs = q.getResultList();
		// 2条历史记录
		assertEquals(2, qhs.size());

		// case 5：CDKEY错误， pos机显示 "CDKEY无效"
		String cdkey2 = "123456";
		// 设置消费金额
		amount = "100";
		// result 02表示验证码无效
		result = new byte[] { 0x00, 0x00, 0x00, 0x02 };
		test_PRIVILEGE(cdkey2, amount, result, null, null);
		// check db
		// //check QQActivityMember table
		q = em.createQuery("select qm.privilegeStatus from QQActivityMember qm where qm.cdkey=?1");
		q.setParameter(1, cdkey);
		pstatus = (PrivilegeStatus) q.getSingleResult();
		// 已经返回给会员100元
		assertTrue(pstatus.equals(PrivilegeStatus.DONE));
		// //check QQActivityHistory table
		q = em.createQuery("select qh from QQActivityHistory qh where qh.cdkey=?1");
		q.setParameter(1, cdkey);
		qhs = q.getResultList();
		// 2条历史记录
		assertEquals(2, qhs.size());

	}

	private void test_PRIVILEGE(String cdkey, String amount, byte[] result,
			String title, String tip) throws Exception {
		int n;
		int checksum;
		byte[] response = new byte[100];
		// 权益兑换请求包
		// 验证码
		int cdkeyLength = cdkey.length();
		int titleLength;
		int tipLength;
		if (title == null && tip == null) {
			titleLength = 0;
			tipLength = 0;
		} else {
			titleLength = title.getBytes(Charset.forName("GBK")).length;
			tipLength = tip.getBytes(Charset.forName("GBK")).length;
		}
		// 设置消费金额
		int amountLength = amount.length();
		// 预期接收包的长度
		int exceptedResponseLength = ProtocolLengths.HEAD
				+ ProtocolLengths.COMMAND
				+ ProtocolLengths.QQADIDAS_RESULT_LENGTH
				+ ProtocolLengths.CR_DATE_LENGTH + ProtocolLengths.POSNETSTRLEN
				+ titleLength + ProtocolLengths.POSNETSTRLEN + tipLength;
		// POS初始化以及登录POS机
		posInitAndLogin();

		byte[] PrivilegeExchangeRequest = new byte[ProtocolLengths.HEAD
				+ ProtocolLengths.COMMAND + ProtocolLengths.POSNETSTRLEN
				+ cdkeyLength + ProtocolLengths.POSNETSTRLEN + amountLength];
		// 设置SEQ
		Tools.putBytes(PrivilegeExchangeRequest, new byte[] { 0x00, 0x00, 0x00,
				0x10 }, 0);
		// 设置MESSAGESIZE
		Tools.putUnsignedInt(PrivilegeExchangeRequest,
				PrivilegeExchangeRequest.length, 12);
		// 设置cmd id 权益兑换请求的命令编号：203
		Tools.putUnsignedInt(PrivilegeExchangeRequest, 203,
				ProtocolLengths.HEAD);
		Tools.putUnsignedShort(PrivilegeExchangeRequest, cdkeyLength,
				ProtocolLengths.HEAD + ProtocolLengths.COMMAND);
		Tools.putBytes(PrivilegeExchangeRequest, cdkey.getBytes(),
				ProtocolLengths.HEAD + ProtocolLengths.COMMAND
						+ ProtocolLengths.POSNETSTRLEN);
		Tools.putUnsignedShort(PrivilegeExchangeRequest, amountLength,
				ProtocolLengths.HEAD + ProtocolLengths.COMMAND
						+ ProtocolLengths.POSNETSTRLEN + cdkeyLength);
		Tools.putBytes(PrivilegeExchangeRequest, amount.getBytes(),
				ProtocolLengths.HEAD + ProtocolLengths.COMMAND
						+ ProtocolLengths.POSNETSTRLEN + cdkeyLength
						+ ProtocolLengths.POSNETSTRLEN);
		// 计算校验和
		checksum = Tools.checkSum(PrivilegeExchangeRequest,
				PrivilegeExchangeRequest.length);
		// 设置CHECKSUM
		Tools.putUnsignedShort(PrivilegeExchangeRequest, checksum, 10);
		// 发送初始化请求
		os.write(PrivilegeExchangeRequest);
		// 得到应答
		response = new byte[500];
		n = is.read(response);
		assertEquals(n, exceptedResponseLength);
		// 权益兑换 CMD ID 是204
		byte[] cmdId = new byte[4];
		Tools.putUnsignedInt(cmdId, 204, 0);
		assertTrue(Arrays.equals(cmdId, Arrays.copyOfRange(response,
				ProtocolLengths.HEAD, ProtocolLengths.HEAD
						+ ProtocolLengths.COMMAND)));
		// 比较result
		assertTrue(Arrays.equals(
				result,
				Arrays.copyOfRange(response, ProtocolLengths.HEAD
						+ ProtocolLengths.COMMAND, ProtocolLengths.HEAD
						+ ProtocolLengths.COMMAND
						+ ProtocolLengths.QQADIDAS_RESULT_LENGTH)));
		// 交易时间
		Calendar cal = Tools.getDate(Arrays.copyOfRange(response,
				ProtocolLengths.HEAD + ProtocolLengths.COMMAND
						+ ProtocolLengths.QQADIDAS_RESULT_LENGTH,
				ProtocolLengths.HEAD + ProtocolLengths.COMMAND
						+ ProtocolLengths.QQADIDAS_RESULT_LENGTH
						+ ProtocolLengths.CR_DATE_LENGTH));
		System.out.println("交易时间： " + cal.getTime().toString());
		// 接收的titleLength
		int receivetitleLength = Tools.getUnsignedShort(response,
				ProtocolLengths.HEAD + ProtocolLengths.COMMAND
						+ ProtocolLengths.QQADIDAS_RESULT_LENGTH
						+ ProtocolLengths.CR_DATE_LENGTH);
		// 比较titleLength
		assertEquals(titleLength, receivetitleLength);
		// 比较title
		if (title == null) {
			title = "";
		}
		assertTrue(title.equals(new String(Arrays.copyOfRange(response,
				ProtocolLengths.HEAD + ProtocolLengths.COMMAND
						+ ProtocolLengths.QQADIDAS_RESULT_LENGTH
						+ ProtocolLengths.CR_DATE_LENGTH
						+ ProtocolLengths.POSNETSTRLEN, ProtocolLengths.HEAD
						+ ProtocolLengths.COMMAND
						+ ProtocolLengths.QQADIDAS_RESULT_LENGTH
						+ ProtocolLengths.CR_DATE_LENGTH
						+ ProtocolLengths.POSNETSTRLEN + receivetitleLength),
				"gbk")));
		// 接收的tipLength
		int receivetipLength = Tools.getUnsignedShort(response,
				ProtocolLengths.HEAD + ProtocolLengths.COMMAND
						+ ProtocolLengths.QQADIDAS_RESULT_LENGTH
						+ ProtocolLengths.CR_DATE_LENGTH
						+ ProtocolLengths.POSNETSTRLEN + receivetitleLength);
		// 比较tipLength
		assertEquals(tipLength, receivetipLength);
		// 比较tip
		if (tip == null) {
			tip = "";
		}
		System.out.println("tip:"
				+ new String(Arrays.copyOfRange(response, ProtocolLengths.HEAD
						+ ProtocolLengths.COMMAND
						+ ProtocolLengths.QQADIDAS_RESULT_LENGTH
						+ ProtocolLengths.CR_DATE_LENGTH
						+ ProtocolLengths.POSNETSTRLEN + receivetitleLength
						+ ProtocolLengths.POSNETSTRLEN, ProtocolLengths.HEAD
						+ ProtocolLengths.COMMAND
						+ ProtocolLengths.QQADIDAS_RESULT_LENGTH
						+ ProtocolLengths.CR_DATE_LENGTH
						+ ProtocolLengths.POSNETSTRLEN + receivetitleLength
						+ ProtocolLengths.POSNETSTRLEN + receivetipLength),
						"gbk"));
		// assertTrue(tip.equals(new String(Arrays.copyOfRange(response,
		// ProtocolLengths.HEAD + ProtocolLengths.COMMAND
		// + ProtocolLengths.QQADIDAS_RESULT_LENGTH
		// + ProtocolLengths.CR_DATE_LENGTH
		// + ProtocolLengths.POSNETSTRLEN + receivetitleLength
		// + ProtocolLengths.POSNETSTRLEN, ProtocolLengths.HEAD
		// + ProtocolLengths.COMMAND
		// + ProtocolLengths.QQADIDAS_RESULT_LENGTH
		// + ProtocolLengths.CR_DATE_LENGTH
		// + ProtocolLengths.POSNETSTRLEN + receivetitleLength
		// + ProtocolLengths.POSNETSTRLEN + receivetipLength),
		// "gbk")));
		// 打印接收包的16进制码
		for (int i = 0; i < n; i++) {
			String str = Integer.toHexString((byte) response[i]);
			if (str.length() < 2) {
				str = "0" + str;
			}
			if (str.length() > 2) {
				str = str.substring(str.length() - 2);
			}
			System.out.print(str);
			if ((i + 1) % 4 == 0) {
				System.out.println();
			}
		}
	}

	private int startServer() throws Exception {
		// force changing of configuration
		createInjector();
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

		// XXX we insert data here
		{
			DefaultPosServer dserver = (DefaultPosServer) posServer;
			Injector injector = dserver.getInjector();
			em = injector.getInstance(EntityManager.class);
			em.getTransaction().begin();
			initDB(em);
		}

		// make sure it is started, and port is correct
		assertFalse(posServer.isStopped());
		System.out.println("pos server start!");
		assertEquals(runningPort, posServer.getLocalPort());
		return runningPort;
		// sleep for a while...

	}

	private void initDB(EntityManager em) {
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

		List<Pos> pp = em.createQuery("select p from Pos p").getResultList();
		log.debug("pp={}", pp);
		if (pp != null) {
			for (Pos p : pp) {
				log.debug("getPosId : {}", p.getPosId());
			}
		}

		// 消费者还没有得到过优惠
		String cdkey = "11111111111";
		QQActivityMember qm = new QQActivityMember();
		qm.setCdkey(cdkey);
		qm.setPrivilegeStatus(PrivilegeStatus.NEW);
		em.persist(qm);

		// 消费者已经得到100元的优惠了
		cdkey = "22222222222";
		qm = new QQActivityMember();
		qm.setCdkey(cdkey);
		qm.setPrivilegeStatus(PrivilegeStatus.DONE);
		em.persist(qm);
		// 消费者之前未消费过
		cdkey = "33333333333";
		qm = new QQActivityMember();
		qm.setCdkey(cdkey);
		qm.setPrivilegeStatus(PrivilegeStatus.NEW);
		em.persist(qm);

		// 消费者之前未消费过
		cdkey = "44444444444";
		qm = new QQActivityMember();
		qm.setCdkey(cdkey);
		qm.setPrivilegeStatus(PrivilegeStatus.NEW);
		em.persist(qm);

		List<QQActivityMember> qms = em.createQuery("from QQActivityMember")
				.getResultList();
		log.debug("qms={}", qms);
		if (qms != null) {
			for (QQActivityMember qqm : qms) {
				log.debug("getCdkey: {}", qqm.getCdkey());
			}
		}
	}

	private void stopServer() throws Exception {
		// stop it, and make sure it is stopped.
		posServer.stop();
		assertTrue(posServer.isStopped());
		log.info("posServer stopped");
	}

	protected Module buildPersistModule(Configuration config) {

		JpaPersistModule jpaModule = new JpaPersistModule("posnet");
		// config it.

		JpaPersistModuleBuilder b = new JpaPersistModuleBuilder();
		b.configModule(jpaModule, config, "db");

		return jpaModule;
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
