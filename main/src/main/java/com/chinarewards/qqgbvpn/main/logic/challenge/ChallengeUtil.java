/**
 * 
 */
package com.chinarewards.qqgbvpn.main.logic.challenge;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.util.HMAC_MD5;

/**
 * This class will handler the challenge logic.
 * 
 * @author cream
 * @since 1.0.0 2011-08-26
 */
public class ChallengeUtil {

	static Logger logger = LoggerFactory.getLogger(ChallengeUtil.class);

	/**
	 * This method will generate 8 byte of challenge code.
	 * 
	 * @return
	 */
	public static byte[] generateChallenge() {
		return HMAC_MD5.getSecretKey(8);
	}

	public static String generatePosSecret() {
		String result = null;
		int serial = (int) (Math.random() * 1000000);
		result = String.format("%06d", serial);
		return result;
	}

	public static boolean checkChallenge(byte[] challengeResponse,
			String posSecret, byte[] random) {
		logger.trace("checking challenge:response:{}, posKey:{}, random:{}",
				new Object[] { challengeResponse, posSecret, random });
		byte[] content = HMAC_MD5.getSecretContent(random, posSecret);
		return Arrays.equals(challengeResponse, content);
	}
}
