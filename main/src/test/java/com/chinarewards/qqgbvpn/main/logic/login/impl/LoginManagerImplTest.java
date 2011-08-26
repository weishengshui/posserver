/**
 * 
 */
package com.chinarewards.qqgbvpn.main.logic.login.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

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
import com.chinarewards.qqgpvn.main.test.JpaGuiceTest;
import com.google.inject.Module;

/**
 * @author cream
 * 
 */
public class LoginManagerImplTest extends JpaGuiceTest {

	PosDao posDao;

	@Override
	protected Module[] getModules() {
		return new Module[] { new QQApiModule() };
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
		em.persist(pos);
		em.flush();

		InitRequest request = new InitRequest();
		request.setSerial(1l);
		request.setPosId("pos-0001");

		InitResponse response = getManager().init(request);
		assertEquals(1l, response.getSerial());
		assertNotNull(response.getChallenge());
		assertEquals(InitResult.INIT, response.getResult());

	}

	// @Test Not finished yet!
	public void testLogin() {

		// prepared data
		Pos pos = new Pos();
		pos.setPosId("pos-0002");
		pos.setDstatus(PosDeliveryStatus.DELIVERED);
		pos.setIstatus(PosInitializationStatus.INITED);
		pos.setOstatus(PosOperationStatus.ALLOWED);
		em.persist(pos);
		em.flush();

		LoginRequest req = new LoginRequest();
		req.setSerial(2l);
		req.setPosId("pos-0002");
		byte[] b = new byte[] {};
		req.setChallengeResponse(b);

		LoginResponse resp = getManager().login(req);

		assertEquals(2l, resp.getSerial());
		assertNotNull(resp.getChallenge());
		assertEquals(LoginResult.SUCCESS, resp.getResult());

		Pos record = em.find(Pos.class, "pos-0002");
		assertNotNull(record.getChallenge());
		assertNotNull(record.getSecret());
	}

	private LoginManager getManager() {
		return getInjector().getInstance(LoginManager.class);
	}
}
