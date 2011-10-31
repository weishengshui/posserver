/**
 * 
 */
package com.chinarewards.qqgbvpn.main.logic.challenge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.HexDump;
import org.junit.Test;

import com.chinarewards.qqgbvpn.main.util.HMAC_MD5;

/**
 * @author cream
 * 
 */
public class ChallengeUtilTest {

	@Test
	public void testGenerateChallenge() {
		byte[] challenge = ChallengeUtil.generateChallenge();
		assertNotNull(challenge);
	}

	@Test
	public void testGeneratePosSecret() throws IOException {
		String serial = ChallengeUtil.generatePosSecret();
		assertNotNull(serial);
		assertNotSame("", serial.trim());
		assertEquals(6, serial.length());
		System.out.println(serial);
	}

	@Test
	public void testCheckChallenge() {
		String posKey = "000001";
		// random byte value: 120 66 116 82 89 97 80 82
		// challenge response byte value:
		// -64 39 8 -126 -57 -34 102 -117 -68 -60 -126 39 109 -110 36 64
		byte[] challengeResponse = new byte[] { -64, 39, 8, -126, -57, -34,
				102, -117, -68, -60, -126, 39, 109, -110, 36, 64 };
		byte[] random = new byte[] { 120, 66, 116, 82, 89, 97, 80, 82 };
		assertTrue(ChallengeUtil.checkChallenge(challengeResponse, posKey,
				random));
	}


}
