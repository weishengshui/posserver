package com.chinarewards.qqgpvn.main.qqapi;

import java.util.Arrays;

import org.junit.Test;

import com.chinarewards.qqgbvpn.main.util.HMAC_MD5;

public class HMACMD5Test {

	@Test
	public void testHMACMD5() {
		//明文
		String str = "abcde";
		//密钥
		byte[] secretKey = HMAC_MD5.getSecretKey(8);
		
		System.out.println("secretKey length: " + secretKey.length);
		
		//根据密钥和明文生成密文
		byte[] secretContent = HMAC_MD5.getSecretContent(secretKey, str);
		
		//根据相同密钥和明文再次生成密文
		byte[] secretContent2 = HMAC_MD5.getSecretContent(secretKey, str);
		
		System.out.println("secretContent length: " + secretContent.length);
		
		//转16进制字符串输出
		for (byte b : secretContent) {
			System.out.print(Integer.toHexString(b & 0XFF));
			System.out.print(" ");
		}
		System.out.println();
		//判断密文是否相同
		System.out.println(Arrays.equals(secretContent, secretContent2));
	}
}
