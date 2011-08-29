/**
 * 
 */
package com.chinarewards.qqgbvpn.main.logic.login.impl;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.status.PosDeliveryStatus;
import com.chinarewards.qqgbvpn.domain.status.PosOperationStatus;
import com.chinarewards.qqgbvpn.main.dao.qqapi.PosDao;
import com.chinarewards.qqgbvpn.main.logic.challenge.ChallengeUtil;
import com.chinarewards.qqgbvpn.main.logic.login.LoginManager;
import com.chinarewards.qqgbvpn.main.protocol.cmd.init.InitRequest;
import com.chinarewards.qqgbvpn.main.protocol.cmd.init.InitResponse;
import com.chinarewards.qqgbvpn.main.protocol.cmd.init.InitResult;
import com.chinarewards.qqgbvpn.main.protocol.cmd.login.LoginRequest;
import com.chinarewards.qqgbvpn.main.protocol.cmd.login.LoginResponse;
import com.chinarewards.qqgbvpn.main.protocol.cmd.login.LoginResult;
import com.chinarewards.utils.StringUtil;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author cream
 * @since 1.0.0 2011-08-26
 */
public class LoginManagerImpl implements LoginManager {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	Provider<PosDao> posDao;

	/**
	 * <ul>
	 * <li>Check POS ID first</li>
	 * <li>Check POS status.</li>
	 * <li>Check POS secret code. If not existed, create it.</li>
	 * </ul>
	 */
	@Override
	public InitResponse init(InitRequest req) {
		logger.debug("InitResponse() invoke");

		InitResponse resp = new InitResponse();

		if (StringUtil.isEmptyString(req.getPosId())) {
			throw new IllegalArgumentException("POS ID is missing!");
		}

		resp.setSerial(req.getSerial());

		Pos pos = null;
		InitResult result = null;

		byte[] challenge = ChallengeUtil.generateChallenge();

		try {
			pos = posDao.get().fetchPos(req.getPosId(),
					PosDeliveryStatus.DELIVERED, null,
					PosOperationStatus.ALLOWED);

			// check pos.secret. When not existed, generate one.
			if (StringUtil.isEmptyString(pos.getSecret())) {
				pos.setSecret(ChallengeUtil.generatePosSecret());
			}

			pos.setChallenge(challenge);

			switch (pos.getIstatus()) {
			case INITED:
				result = InitResult.INIT;
				break;
			case UNINITED:
				result = InitResult.UNINIT;
				break;
			default:
				result = InitResult.OTHERS;
				break;
			}
		} catch (NoResultException e) {
			logger.warn("NO result fetch.");
			result = InitResult.OTHERS;
		}
		resp.setChallenge(challenge);
		resp.setResult(result);

		return resp;
	}

	@Override
	public LoginResponse login(LoginRequest req) {
		LoginResponse resp = new LoginResponse();

		resp.setSerial(req.getSerial());

		LoginResult result = null;
		byte[] challenge = ChallengeUtil.generateChallenge();
		try {
			Pos pos = posDao.get().fetchPos(req.getPosId(), null, null, null);

			logger.debug("challenge:{}", challenge);
			pos.setChallenge(challenge);

			boolean check = ChallengeUtil.checkChallenge(
					req.getChallengeResponse(), pos.getSecret(),
					pos.getChallenge());

			if (check) {
				result = LoginResult.SUCCESS;
			} else {
				result = LoginResult.VALIDATE_FAILED;
			}
		} catch (NoResultException e) {
			logger.warn("Pos ID not found in DB. PosId={}", req.getPosId());
			result = LoginResult.POSID_NOT_EXIST;
		}
		resp.setChallenge(challenge);
		resp.setResult(result);

		return resp;
	}

}
