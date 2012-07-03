package com.chinarewards.qqadidas.main.privilegeexchange;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.configuration.Configuration;
import org.junit.Test;

import com.chinarewards.qqadidas.domain.QQActivityMember;
import com.chinarewards.qqadidas.domain.status.PrivilegeStatus;
import com.chinarewards.qqadidas.main.logic.QQAdidasManager;
import com.chinarewards.qqadidas.main.logic.impl.QQAdidasManagerImpl;
import com.chinarewards.qqadidas.main.protocol.cmd.PrivilegeExchangeResponseMessage;
import com.chinarewards.qqgbpvn.main.CommonTestConfigModule;
import com.chinarewards.qqgbpvn.main.test.JpaGuiceTest;
import com.chinarewards.qqgbvpn.core.jpa.JpaPersistModuleBuilder;
import com.chinarewards.qqgbvpn.main.ApplicationModule;
import com.chinarewards.qqgbvpn.main.PosServer;
import com.chinarewards.qqgbvpn.main.ServerModule;
import com.chinarewards.qqgbvpn.main.guice.AppModule;
import com.chinarewards.qqgbvpn.main.impl.DefaultPosServer;
import com.chinarewards.qqgbvpn.main.protocol.ServiceHandlerModule;
import com.chinarewards.qqgbvpn.main.protocol.ServiceMapping;
import com.chinarewards.qqgbvpn.main.protocol.ServiceMappingConfigBuilder;
import com.chinarewards.qqgbvpn.main.protocol.guice.ServiceHandlerGuiceModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.util.Modules;

/**
 * qqadidas 权益兑换逻辑测试
 * 
 * @author weishengshui
 * 
 */
public class PrivilegeExchangeLogicTest extends JpaGuiceTest {
	EntityManager em;
	PosServer posServer;
	QQAdidasManager logic;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		startServer();
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		try {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			stopServer();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	//权益兑换逻辑测试
	@Test
	public void test() {

		String cdkey;// 验证码
		String amount;// 消费额

		// case 1: 无效的cdkey
		cdkey = "123456789001234";
		amount = "0.01";
		assertEquals(PrivilegeExchangeResponseMessage.CDKEY_NOT_EXISTS,
				logic.getResultStatusByCdkeyAmount(cdkey, amount));

		// case 2: cdkey正确111111111111234，消费额30
		cdkey = "111111111111234";
		amount = "30";
		assertEquals(PrivilegeExchangeResponseMessage.AMOUNT_INVALID,
				logic.getResultStatusByCdkeyAmount(cdkey, amount));

		// case 3: cdkey正确111111111111234，消费额299
		amount = "299";
		assertEquals(PrivilegeExchangeResponseMessage.AMOUNT_INVALID,
				logic.getResultStatusByCdkeyAmount(cdkey, amount));

		// case 4: cdkey正确111111111111234，消费额300
		amount = "300";
		assertEquals(PrivilegeExchangeResponseMessage.PRIVILEGE_FIRST_HALF,
				logic.getResultStatusByCdkeyAmount(cdkey, amount));

		// case 5: cdkey正确111111111111234，消费额599
		amount = "599";
		assertEquals(PrivilegeExchangeResponseMessage.PRIVILEGE_SECOND_HALF,
				logic.getResultStatusByCdkeyAmount(cdkey, amount));

		// case 6: cdkey正确111111111111234，消费额600
		amount = "600";
		assertEquals(PrivilegeExchangeResponseMessage.PRIVILEGE_DONE,
				logic.getResultStatusByCdkeyAmount(cdkey, amount));

		// case 7: cdkey正确222222222221234，消费额600
		cdkey = "222222222221234";
		amount = "600";
		assertEquals(PrivilegeExchangeResponseMessage.PRIVILEGE_ALL,
				logic.getResultStatusByCdkeyAmount(cdkey, amount));

		// case 8: cdkey正确222222222221234，消费额352
		cdkey = "222222222221234";
		amount = "352";
		assertEquals(PrivilegeExchangeResponseMessage.PRIVILEGE_DONE,
				logic.getResultStatusByCdkeyAmount(cdkey, amount));
		
		// case 9: cdkey正确222222222221234，消费额625
		cdkey = "222222222221234";
		amount = "625";
		assertEquals(PrivilegeExchangeResponseMessage.PRIVILEGE_DONE,
				logic.getResultStatusByCdkeyAmount(cdkey, amount));
		
		// case 10: cdkey正确333333333331234，消费额310
		cdkey = "333333333331234";
		amount = "310";
		assertEquals(PrivilegeExchangeResponseMessage.PRIVILEGE_FIRST_HALF,
				logic.getResultStatusByCdkeyAmount(cdkey, amount));
		
		// case 11: cdkey正确333333333331234，消费额600
		cdkey = "333333333331234";
		amount = "600";
		assertEquals(PrivilegeExchangeResponseMessage.PRIVILEGE_SECOND_HALF,
				logic.getResultStatusByCdkeyAmount(cdkey, amount));
		
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

	protected Module buildPersistModule(Configuration config) {

		JpaPersistModule jpaModule = new JpaPersistModule("posnet");
		// config it.

		JpaPersistModuleBuilder b = new JpaPersistModuleBuilder();
		b.configModule(jpaModule, config, "db");

		return jpaModule;
	}

	protected void startServer() throws Exception {
		createInjector();
		Configuration conf = getInjector().getInstance(Configuration.class);
		conf.setProperty("server.port", 0);

		posServer = getInjector().getInstance(PosServer.class);

		posServer.start();

		int runningPort = posServer.getLocalPort();

		{
			DefaultPosServer dserver = (DefaultPosServer) posServer;
			Injector injector = dserver.getInjector();
			logic = injector.getInstance(QQAdidasManagerImpl.class);
			em = injector.getInstance(EntityManager.class);
			em.getTransaction().begin();
			initDb(em);
		}
		assertEquals(runningPort, posServer.getLocalPort());

	}

	protected void stopServer() throws Exception {
		posServer.stop();
	}

	protected void initDb(EntityManager em) {
		String cdkey1 = "111111111111234";
		String cdkey2 = "222222222221234";
		String cdkey3 = "333333333331234";

		QQActivityMember qm = new QQActivityMember();
		qm.setCdkey(cdkey1);
		qm.setPrivilegeStatus(PrivilegeStatus.NEW);
		em.persist(qm);
		qm = new QQActivityMember();
		qm.setCdkey(cdkey2);
		qm.setPrivilegeStatus(PrivilegeStatus.NEW);
		em.persist(qm);
		qm = new QQActivityMember();
		qm.setCdkey(cdkey3);
		qm.setPrivilegeStatus(PrivilegeStatus.NEW);
		em.persist(qm);

		List<QQActivityMember> qms = em.createQuery(
				"select q from QQActivityMember q").getResultList();
		log.debug("QQActivityMember list={}", qms);
		for (QQActivityMember qam : qms) {
			log.debug("QQActivityMember getCdkey()={}", qam.getCdkey());
		}
	}

}
