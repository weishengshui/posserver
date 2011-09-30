package com.chinarewards.qqgbpvn.testing.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description：a digest util
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-30   上午09:49:47
 * @author Seek
 */
public final class DigestUtil {
	
	private static Logger logger = LoggerFactory.getLogger(SocketUtil.class);
	
	public static final String MD5 = "MD5";
	public static final String SHA1 = "SHA-1";
	
	/**
	 * description：compare byte1 and byte2
	 * @param b1 bytes1
	 * @param b2 bytes2
	 * @param algorithm digest algorithm
	 * @return boolean {true success, false fail}
	 * @time 2011-9-30   上午09:48:15
	 * @author Seek
	 */
	public final static boolean compareDigest(byte[] b1, byte[] b2, String algorithm) {
		boolean result = false;
		try {
			MessageDigest digest1 = MessageDigest.getInstance(algorithm);
			digest1.update(b1);
			byte[] digest1Bytes = digest1.digest();
			
			
			MessageDigest digest2 = MessageDigest.getInstance(algorithm);
			digest2.update(b2);

			result = MessageDigest.isEqual(digest1Bytes, digest2.digest());
			
			logger.debug("bytes1 and bytes2 compareDigest = " + result);
		} catch (NoSuchAlgorithmException e) {
			logger.error("algorithm is ERROR!", e);
		}
		return result;
	}
	
}
