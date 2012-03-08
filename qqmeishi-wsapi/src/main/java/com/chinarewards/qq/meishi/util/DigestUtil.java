package com.chinarewards.qq.meishi.util;

import java.security.MessageDigest;

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
	
	private static Logger logger = LoggerFactory.getLogger(DigestUtil.class);
	
	public static final String MD5 = "MD5";
	public static final String SHA1 = "SHA-1";
	
	
	/**
	 * description：digest a bytes
	 * @param source 	 data source
	 * @param algorithm  MD5, SHA1...
	 * @return  bytes digest result
	 * @time 2012-3-6   下午03:27:34
	 * @author Seek
	 */
	public static String digestData(String source, String algorithm)
			throws Throwable {
		return digestData(source.getBytes(), algorithm);
	}
	
	/**
	 * description：digest a bytes
	 * @param source 	 data source
	 * @param algorithm  MD5, SHA1...
	 * @return  bytes digest result
	 * @time 2012-3-6   下午03:27:34
	 * @author Seek
	 */
	public static String digestData(byte[] source, String algorithm)
			throws Throwable {
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance(algorithm);
			md.update(source);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
										// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
											// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
											// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
															// >>>
															// 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串

		} catch (Throwable e) {
			throw new Exception("data digest is ERROR!", e);
		}
		return s;
	}
	
	/**
	 * description：compare byte1 and byte2
	 * @param b1 bytes1
	 * @param b2 bytes2
	 * @param algorithm digest algorithm
	 * @return boolean {true success, false fail}
	 * @time 2011-9-30   上午09:48:15
	 * @author Seek
	 */
	public final static boolean compareDigest(byte[] b1, byte[] b2,
			String algorithm) throws Throwable {
		boolean result = false;
		try {
			MessageDigest digest1 = MessageDigest.getInstance(algorithm);
			digest1.update(b1);
			byte[] digest1Bytes = digest1.digest();
			
			MessageDigest digest2 = MessageDigest.getInstance(algorithm);
			digest2.update(b2);

			result = MessageDigest.isEqual(digest1Bytes, digest2.digest());
			
			logger.debug("bytes1 and bytes2 compareDigest = " + result);
		} catch (Throwable e) {
			throw new Exception("compare digest is ERROR!", e);
		}
		return result;
	}
	
}
