/**
 * 
 */
package com.chinarewards.qqgbvpn.main.logic.login.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.junit.Test;

import com.chinarewards.qqgbpvn.main.TestConfigModule;
import com.chinarewards.qqgbpvn.main.test.JpaGuiceTest;
import com.chinarewards.qqgbvpn.config.DatabaseProperties;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.status.PosDeliveryStatus;
import com.chinarewards.qqgbvpn.domain.status.PosInitializationStatus;
import com.chinarewards.qqgbvpn.domain.status.PosOperationStatus;
import com.chinarewards.qqgbvpn.main.dao.qqapi.PosDao;
import com.chinarewards.qqgbvpn.main.guice.AppModule;
import com.chinarewards.qqgbvpn.main.logic.login.LoginManager;
import com.chinarewards.qqgbvpn.main.protocol.cmd.InitRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.InitResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.LoginRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.LoginResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.init.InitResult;
import com.chinarewards.qqgbvpn.main.protocol.cmd.login.LoginResult;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;

/**
 * @author cream
 * 
 */
public class LoginManagerImplTest extends JpaGuiceTest {

	PosDao posDao;

	@Override
	protected Module[] getModules() {
		return new Module[] {
				new AppModule(),
				new JpaPersistModule("posnet")
						.properties(new DatabaseProperties().getProperties()), buildTestConfigModule() };
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
		conf.setProperty("db.hibernate.dialect",
				"org.hibernate.dialect.HSQLDialect");
		conf.setProperty("db.hibernate.show_sql", true);
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


	@Test
	public void testInit() throws IOException {

		posDao = getInjector().getInstance(PosDao.class);

		// prepared data
		Pos pos = new Pos();
		pos.setPosId("pos-0001");
		pos.setDstatus(PosDeliveryStatus.DELIVERED);
		pos.setIstatus(PosInitializationStatus.INITED);
		pos.setOstatus(PosOperationStatus.ALLOWED);
		getEm().persist(pos);
		getEm().flush();

		InitRequestMessage request = new InitRequestMessage();
		request.setPosid("pos-0001");

		InitResponseMessage response = getManager().init(request);
		assertNotNull(response.getChallenge());
		assertEquals(InitResult.INIT.getPosCode(), response.getResult());

		Pos record = getEm().find(Pos.class, pos.getId());
		assertNotNull(record);
		assertNotNull(record.getChallenge());
		assertNotNull(record.getSecret());
	}

	@Test
	public void testLogin() {

		// prepared data
		Pos pos = new Pos();
		pos.setPosId("pos-0002");
		pos.setSecret("000001");
		pos.setDstatus(PosDeliveryStatus.DELIVERED);
		pos.setIstatus(PosInitializationStatus.INITED);
		pos.setOstatus(PosOperationStatus.ALLOWED);

		byte[] challenge = new byte[] { 120, 66, 116, 82, 89, 97, 80, 82 };
		pos.setChallenge(challenge);
		getEm().persist(pos);
		getEm().flush();

		LoginRequestMessage req = new LoginRequestMessage();
		req.setPosId("pos-0002");
		byte[] challengeResponse = new byte[] { -64, 39, 8, -126, -57, -34,
				102, -117, -68, -60, -126, 39, 109, -110, 36, 64 };
		req.setChallengeResponse(challengeResponse);

		LoginResponseMessage resp = getManager().login(req);

		assertNotNull(resp.getChallenge());
		assertEquals(LoginResult.SUCCESS.getPosCode(), resp.getResult());

		Pos record = getEm().find(Pos.class, pos.getId());
		assertNotNull(record);
		assertNotNull(record.getChallenge());
	}

	@Test
	public void testLoginUninited() {

		// prepared data
		Pos pos = new Pos();
		pos.setPosId("pos-0002");
		pos.setSecret("000001");
		pos.setDstatus(PosDeliveryStatus.DELIVERED);
		pos.setIstatus(PosInitializationStatus.UNINITED);
		pos.setOstatus(PosOperationStatus.ALLOWED);

		byte[] challenge = new byte[] { 120, 66, 116, 82, 89, 97, 80, 82 };
		pos.setChallenge(challenge);
		getEm().persist(pos);
		getEm().flush();

		LoginRequestMessage req = new LoginRequestMessage();
		req.setPosId("pos-0002");
		byte[] challengeResponse = new byte[] { -64, 39, 8, -126, -57, -34,
				102, -117, -68, -60, -126, 39, 109, -110, 36, 64 };
		req.setChallengeResponse(challengeResponse);

		LoginResponseMessage resp = getManager().login(req);

		assertNotNull(resp.getChallenge());
		assertEquals(LoginResult.OTHERS.getPosCode(), resp.getResult());

		Pos record = getEm().find(Pos.class, pos.getId());
		assertNotNull(record);
		assertNotNull(record.getChallenge());
	}

	@Test
	public void testBind() {

		// prepared data
		Pos pos = new Pos();
		pos.setPosId("pos-0002");
		pos.setSecret("000001");
		pos.setDstatus(PosDeliveryStatus.DELIVERED);
		pos.setIstatus(PosInitializationStatus.UNINITED);
		pos.setOstatus(PosOperationStatus.ALLOWED);

		byte[] challenge = new byte[] { 120, 66, 116, 82, 89, 97, 80, 82 };
		pos.setChallenge(challenge);
		getEm().persist(pos);
		getEm().flush();

		LoginRequestMessage req = new LoginRequestMessage();
		req.setPosId("pos-0002");
		byte[] challengeResponse = new byte[] { -64, 39, 8, -126, -57, -34,
				102, -117, -68, -60, -126, 39, 109, -110, 36, 64 };
		req.setChallengeResponse(challengeResponse);

		LoginResponseMessage resp = getManager().bind(req);

		assertNotNull(resp.getChallenge());
		assertEquals(LoginResult.SUCCESS.getPosCode(), resp.getResult());

		Pos record = getEm().find(Pos.class, pos.getId());
		assertNotNull(record);
		assertNotNull(record.getChallenge());
	}

	@Test
	public void testBindInited() {

		// prepared data
		Pos pos = new Pos();
		pos.setPosId("pos-0002");
		pos.setSecret("000001");
		pos.setDstatus(PosDeliveryStatus.DELIVERED);
		pos.setIstatus(PosInitializationStatus.INITED);
		pos.setOstatus(PosOperationStatus.ALLOWED);

		byte[] challenge = new byte[] { 120, 66, 116, 82, 89, 97, 80, 82 };
		pos.setChallenge(challenge);
		getEm().persist(pos);
		getEm().flush();

		LoginRequestMessage req = new LoginRequestMessage();
		req.setPosId("pos-0002");
		byte[] challengeResponse = new byte[] { -64, 39, 8, -126, -57, -34,
				102, -117, -68, -60, -126, 39, 109, -110, 36, 64 };
		req.setChallengeResponse(challengeResponse);

		LoginResponseMessage resp = getManager().bind(req);

		assertNotNull(resp.getChallenge());
		assertEquals(LoginResult.OTHERS.getPosCode(), resp.getResult());

		Pos record = getEm().find(Pos.class, pos.getId());
		assertNotNull(record);
		assertNotNull(record.getChallenge());
	}

	private LoginManager getManager() {
		return getInjector().getInstance(LoginManager.class);
	}
}
