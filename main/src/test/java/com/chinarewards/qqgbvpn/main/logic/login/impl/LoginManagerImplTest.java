/**
 * 
 */
package com.chinarewards.qqgbvpn.main.logic.login.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.chinarewards.qqgbvpn.config.DatabaseProperties;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.status.PosDeliveryStatus;
import com.chinarewards.qqgbvpn.domain.status.PosInitializationStatus;
import com.chinarewards.qqgbvpn.domain.status.PosOperationStatus;
import com.chinarewards.qqgbvpn.main.QQApiModule;
import com.chinarewards.qqgbvpn.main.dao.qqapi.PosDao;
import com.chinarewards.qqgbvpn.main.logic.login.LoginManager;
import com.chinarewards.qqgbvpn.main.protocol.cmd.init.InitRequest;
import com.chinarewards.qqgbvpn.main.protocol.cmd.init.InitResponse;
import com.chinarewards.qqgbvpn.main.protocol.cmd.init.InitResult;
import com.chinarewards.qqgbvpn.main.protocol.cmd.login.LoginRequest;
import com.chinarewards.qqgbvpn.main.protocol.cmd.login.LoginResponse;
import com.chinarewards.qqgbvpn.main.protocol.cmd.login.LoginResult;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.InitRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.InitResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.LoginRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.LoginResponseMessage;
import com.chinarewards.qqgpvn.main.test.JpaGuiceTest;
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
				new QQApiModule(),
				new JpaPersistModule("posnet")
						.properties(new DatabaseProperties().getProperties()) };
	}

	@Test
	public void testInit() {

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
		pos.setDstatus(PosDeliveryStatus.DELIVERED);
		pos.setIstatus(PosInitializationStatus.INITED);
		pos.setOstatus(PosOperationStatus.ALLOWED);
		getEm().persist(pos);
		getEm().flush();

		LoginRequestMessage req = new LoginRequestMessage();
		req.setPosid("pos-0002");
		byte[] b = new byte[] {};
		req.setChallengeResponse(b);

		LoginResponseMessage resp = getManager().login(req);

		assertNotNull(resp.getChallenge());
		assertEquals(LoginResult.SUCCESS.getPosCode(), resp.getResult());

		Pos record = getEm().find(Pos.class, pos.getId());
		assertNotNull(record);
		assertNotNull(record.getChallenge());
	}

	private LoginManager getManager() {
		return getInjector().getInstance(LoginManager.class);
	}
}
