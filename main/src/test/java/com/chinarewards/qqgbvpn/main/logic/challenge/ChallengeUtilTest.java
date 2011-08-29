/**
 * 
 */
package com.chinarewards.qqgbvpn.main.logic.challenge;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

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
		File tmpFile = File.createTempFile("test", "txt");
		String serial = ChallengeUtil.generatePosSecret(tmpFile);
		assertNotNull(serial);
		assertNotSame("", serial.trim());
		assertEquals("000001", serial);

		String serial2 = ChallengeUtil.generatePosSecret(tmpFile);
		assertNotNull(serial2);
		assertNotSame("", serial2.trim());
		assertEquals("000002", serial2);
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
