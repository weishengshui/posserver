package com.chinarewards.qqgbvpn.main.util;

import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author iori
 * 消息验证码HMAC-MD5算法，JAVA的实现
 */
public class HMAC_MD5 {

	/**
	 * 获取随机字符串生成密钥
	 * @param length
	 * @return
	 */
	public static byte[] getSecretKey(int length) { 
	    StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"); 
	    StringBuffer sb = new StringBuffer(); 
	    Random r = new Random(); 
	    int range = buffer.length(); 
	    for (int i = 0; i < length; i ++) { 
	        sb.append(buffer.charAt(r.nextInt(range))); 
	    } 
	    return sb.toString().getBytes(); 
	}
	
	/**
	 * 根据密钥和明文生成密文
	 * 密文的长度是： 16 *８
	 * @param secretKey
	 * @param str
	 * @return
	 */
	public static byte[] getSecretContent(byte[] secretKey, String str) {
		try {
			//生成MAC对象
			SecretKeySpec sks = new SecretKeySpec(secretKey, "HMACMD5");
			Mac mac = Mac.getInstance("HMACMD5");
			mac.init(sks);
			//计算验证码
			return mac.doFinal(str.getBytes("UTF-8"));
		} catch (Exception e) {
			return null;
		}
	}
	
}
