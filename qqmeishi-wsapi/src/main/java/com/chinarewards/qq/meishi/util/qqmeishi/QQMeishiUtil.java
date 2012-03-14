package com.chinarewards.qq.meishi.util.qqmeishi;

import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qq.meishi.util.DigestUtil;

/**
 * description：QQ meishi util
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-7   下午04:29:05
 * @author Seek
 */
public final class QQMeishiUtil {
	
	static Logger log = LoggerFactory.getLogger(QQMeishiUtil.class);
	
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
		String urlPart = null;
		
		StringBuffer buff = new StringBuffer("");
		Set<String> keys = otherReqParams.keySet();
		for(String key:keys){
			String value = otherReqParams.get(key) == null ? "" : encoder(
					otherReqParams.get(key), charset);
			key = encoder(key, charset);
			
			String batch = "&" + key + "=" + value;
			buff.append(batch);
		}
		
		if(buff.length() > 0){
			urlPart = buff.delete(0, 1).toString();
		}
		
		return buildSig(urlPart, commSecretKey);
	}
	
	/**
	 * description：生成Sig
	 * @param urlPart 其他的所有请求参数URL,&key=value,需要字符编码后的
	 * @param commSecretKey 通讯密钥
	 * @return sig value
	 * @throws Throwable data digest Exception
	 * @time 2012-3-7   下午04:27:04
	 * @author Seek
	 */
	public static String buildSig(String urlPart, String commSecretKey)
			throws Throwable {
		try {
			String seed = urlPart + commSecretKey;
			
			log.debug("buildSig seed:"+seed);
			return DigestUtil.digestData(seed.getBytes(), DigestUtil.MD5);
		} catch (Throwable e) {
			throw new Throwable(e);
		}
	}
	
	/**
	 * description：返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的秒数。 
	 * @return unit of time:second
	 * @time 2012-3-7   下午04:33:23
	 * @author Seek
	 */
	public static long qqMeishiGetTime(){
		return new Date().getTime() / 1000;
	}
	
	/**
	 * description：字符串编码
	 * @param content 内容
	 * @param charset 编码方式
	 * @return 编码后的内容
	 * @throws Throwable
	 * @time 2012-3-8   上午10:05:22
	 * @author Seek
	 */
	public static String encoder(String content, String charset)
			throws Throwable {
		return URLEncoder.encode(content, charset);
	}
	
}
