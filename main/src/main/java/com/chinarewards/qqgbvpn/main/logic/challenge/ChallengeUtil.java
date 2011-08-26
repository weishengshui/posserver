/**
 * 
 */
package com.chinarewards.qqgbvpn.main.logic.challenge;

import java.util.Date;

/**
 * This class will handler the challenge logic.
 * 
 * @author cream
 * @since 1.0.0 2011-08-26
 */
public class ChallengeUtil {

	/**
	 * This method will generate 8 byte of challenge code.
	 * 
	 * @return
	 */
	public static byte[] generateChallenge() {
		long time = new Date().getTime();

		byte[] result = new byte[8];
		for (int i = 0; i < 8; i++) {
			result[i] = (byte) (time >>> (56 - i * 8));
		}
		return result;
	}

	public static String generatePosSecret() {
		// FIXME Implement me.
		return "00001"; // FOR test.
		// throw new UnsupportedOperationException("Unsupported yet!");
	}

	public static boolean checkChallenge(byte[] chanllengeResponse,
			String posSecret, byte[] random) {
		// FIXME Implement me.
		return true; // For test.
		// throw new UnsupportedOperationException("Unsupported yet!");
	}
}
