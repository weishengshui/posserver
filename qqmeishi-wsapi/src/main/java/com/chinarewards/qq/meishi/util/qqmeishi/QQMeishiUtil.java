package com.chinarewards.qq.meishi.util.qqmeishi;

import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.chinarewards.qq.meishi.exception.QQMeishiReqDataDigestException;
import com.chinarewards.qq.meishi.util.DigestUtil;

/**
 * description：QQ meishi util
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-7   下午04:29:05
 * @author Seek
 */
public final class QQMeishiUtil {
	
	/**
	 * description：append argsMap to requestParams,for example: args[verifycode]=837539
	 * @param reqParams		all request parameters
	 * @param serviceParams service request parameters
	 * @return request parameters
	 * @time 2012-3-7   下午04:02:10
	 * @author Seek
	 */
	public static Map<String, String> appendArgsMap(Map<String, String> reqParams,
			Map<String, String> serviceParams) {
		Set<String> keys = serviceParams.keySet();
		for(String key:keys){
			reqParams.put("args"+"["+key+"]", serviceParams.get(key));
		}
		return reqParams;
	}
	
	/**
	 * description：生成Sig
	 * @param otherReqParams 其他的所有请求参数Map
	 * @param commSecretKey 通讯密钥
	 * @param charset 编码方式
	 * @return sig value
	 * @throws Throwable data digest Exception
	 * @time 2012-3-7   下午04:27:04
	 * @author Seek
	 */
	public static String buildSig(Map<String, String> otherReqParams,
			String commSecretKey, String charset)
			throws Throwable {
		String otherParamList = null;
		
		StringBuffer buff = new StringBuffer("");
		Set<String> keys = otherReqParams.keySet();
		for(String key:keys){
			String value = otherReqParams.get(key)==null?"":URLEncoder.encode(otherReqParams.get(key), charset);
			key = URLEncoder.encode(key, charset);
			
			String batch = "&" + key + "=" + value;
			buff.append(batch);
		}
		otherParamList = buff.delete(0, 1).toString();
		
		return buildSig(otherParamList, commSecretKey, charset);
	}
	
	/**
	 * description：生成Sig
	 * @param otherParamList 其他的所有请求参数URL
	 * @param commSecretKey 通讯密钥
	 * @param charset 编码方式
	 * @return sig value
	 * @throws Throwable data digest Exception
	 * @time 2012-3-7   下午04:27:04
	 * @author Seek
	 */
	public static String buildSig(String otherParamList, String commSecretKey,
			String charset) throws Throwable {
		try {
			String seed = otherParamList + commSecretKey;
			
			System.out.println(seed);
			return DigestUtil.digestData(seed.getBytes(), DigestUtil.MD5);
		} catch (Throwable e) {
			throw new QQMeishiReqDataDigestException(e);
		}
	}
	
	/**
	 * description：返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的秒数。 
	 * @return second
	 * @time 2012-3-7   下午04:33:23
	 * @author Seek
	 */
	public static long qqMeishiGetTime(){
		return new Date().getTime() / 1000;
	}
	
}
