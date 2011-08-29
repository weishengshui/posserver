/**
 * 
 */
package com.chinarewards.qqgbvpn.main.logic.login.impl;

import java.io.File;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.status.PosDeliveryStatus;
import com.chinarewards.qqgbvpn.domain.status.PosOperationStatus;
import com.chinarewards.qqgbvpn.main.dao.qqapi.PosDao;
import com.chinarewards.qqgbvpn.main.logic.challenge.ChallengeUtil;
import com.chinarewards.qqgbvpn.main.logic.login.LoginManager;
import com.chinarewards.qqgbvpn.main.protocol.cmd.init.InitResult;
import com.chinarewards.qqgbvpn.main.protocol.cmd.login.LoginResult;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.InitRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.InitResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.LoginRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.LoginResponseMessage;
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

	@Override
	public InitResponseMessage init(InitRequestMessage req, File secretFile) {
		logger.debug("InitResponse() invoke");

		InitResponseMessage resp = new InitResponseMessage();

		if (StringUtil.isEmptyString(req.getPosid())) {
			throw new IllegalArgumentException("POS ID is missing!");
		}

		Pos pos = null;
		InitResult result = null;

		byte[] challenge = ChallengeUtil.generateChallenge();

		try {
			pos = posDao.get().fetchPos(req.getPosid(),
					PosDeliveryStatus.DELIVERED, null,
					PosOperationStatus.ALLOWED);

			// check pos.secret. When not existed, generate one.
			if (StringUtil.isEmptyString(pos.getSecret())) {
				if (secretFile != null) {
					pos.setSecret(ChallengeUtil.generatePosSecret(secretFile));
				} else {
					pos.setSecret(ChallengeUtil.generatePosSecret());
				}
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
		resp.setResult(result.getPosCode());

		return resp;
	}

	@Override
	public InitResponseMessage init(InitRequestMessage req) {
		return init(req, null);
	}

	@Override
	public LoginResponseMessage login(LoginRequestMessage req) {
		LoginResponseMessage resp = new LoginResponseMessage();

		LoginResult result = null;
		byte[] challenge = ChallengeUtil.generateChallenge();
		try {
			Pos pos = posDao.get().fetchPos(req.getPosid(), null, null, null);
			logger.trace(
					"pos.posId:{}, pos.secret:{}, pos.challenge:{}",
					new Object[] { pos.getPosId(), pos.getSecret(),
							pos.getChallenge() });
			boolean check = ChallengeUtil.checkChallenge(
					req.getChallengeResponse(), pos.getSecret(),
					pos.getChallenge());

			logger.debug("new challenge:{}", challenge);
			pos.setChallenge(challenge);

			if (check) {
				result = LoginResult.SUCCESS;
			} else {
				result = LoginResult.VALIDATE_FAILED;
			}
		} catch (NoResultException e) {
			logger.warn("Pos ID not found in DB. PosId={}", req.getPosid());
			result = LoginResult.POSID_NOT_EXIST;
		}
		resp.setChallenge(challenge);
		resp.setResult(result.getPosCode());

		return resp;
	}

}
