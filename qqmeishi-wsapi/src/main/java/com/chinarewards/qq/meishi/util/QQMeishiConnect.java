package com.chinarewards.qq.meishi.util;

import java.util.Map;

import com.chinarewards.qq.meishi.exception.QQMeishiDataParseException;
import com.chinarewards.qq.meishi.exception.QQMeishiInterfaceAccessException;
import com.chinarewards.qq.meishi.exception.QQMeishiReadStreamException;

/**
 * description：QQ Meishi Connect
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-2   下午05:54:29
 * @author Seek
 */
public interface QQMeishiConnect {
 
	public static enum HttpMethod {
		GET,
		POST
	};
	
	/**
	 * description：请求服务器
	 * @param url URL
	 * @param hostAddr host地址
	 * @param httpMethod 请求方式
	 * @param postParams 请求参数<key，value>
	 * @param charset 编码方式
	 * @return 读取到的buffer
	 * @throws QQMeishiInterfaceAccessException QQ美食接口访问异常
	 * @throws QQMeishiDataParseException 数据接口解析异常
	 * @time 2012-3-2   下午05:23:24
	 * @author Seek
	 */
	public String requestServer(String url, String hostAddr,
			HttpMethod httpMethod, Map<String, String> postParams,
			String charset) throws QQMeishiInterfaceAccessException,
			QQMeishiReadStreamException;
			
}
