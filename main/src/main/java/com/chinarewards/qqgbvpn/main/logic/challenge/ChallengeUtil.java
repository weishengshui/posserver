/**
 * 
 */
package com.chinarewards.qqgbvpn.main.logic.challenge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.config.PosNetworkProperties;
import com.chinarewards.qqgbvpn.main.util.HMAC_MD5;
import com.chinarewards.utils.StringUtil;

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

	public static String generatePosSecret(File secretFile) {
		String result = null;
		synchronized (secretFile) {

			FileReader rf = null;
			BufferedReader br = null;
			FileWriter fw = null;
			String str = null;
			int serial = 0;
			try {
				rf = new FileReader(secretFile);
				br = new BufferedReader(rf);

				if (br.ready()) {
					str = br.readLine();
				}

				fw = new FileWriter(secretFile);

				if (StringUtil.isEmptyString(str)) {
					serial = 1;
				} else {
					serial = Integer.valueOf(str);
				}

				result = String.format("%06d", serial);

				serial++;
				fw.write(serial + "");
			} catch (FileNotFoundException e) {
				logger.error("File (file=" + secretFile + ") not found yet!", e);
			} catch (IOException e) {
				logger.error("Reading file error.", e);
			} finally {
				try {
					if (br != null) {
						br.close();
					}
					if (rf != null) {
						rf.close();
					}
					if (fw != null) {
						fw.close();
					}
				} catch (IOException e) {
					logger.error("unknow exception!", e);
				}
			}
			return result;
		}
	}

	/**
	 * Get serial number from special file. The increment it and restore serial
	 * number.
	 * 
	 * @return
	 */
	public static String generatePosSecret() {

		String filePath = new PosNetworkProperties().getSecretFilePaht();
		File secretFile = new File(filePath);

		return generatePosSecret(secretFile);
	}

	public static boolean checkChallenge(byte[] challengeResponse,
			String posSecret, byte[] random) {
		logger.trace("checking challenge:response:{}, posKey:{}, random:{}",
				new Object[] { challengeResponse, posSecret, random });
		byte[] content = HMAC_MD5.getSecretContent(random, posSecret);
		return Arrays.equals(challengeResponse, content);
	}
}
