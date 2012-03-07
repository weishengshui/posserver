package com.chinarewards.qq.meishi.util;

import java.util.Map;

import com.chinarewards.qq.meishi.exception.QQMeishiReadRespStreamException;
import com.chinarewards.qq.meishi.exception.QQMeishiRespDataParseException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerLinkNotFoundException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerRespException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerUnreachableException;

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
	
	public static enum ContentFormat {
		Form,
		Json
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
	 * @throws QQMeishiRespDataParseException 数据接口解析异常
	 * @time 2012-3-2   下午05:23:24
	 * @author Seek
	 */
	public String requestServer(String url, String hostAddr,
			HttpMethod httpMethod, Map<String, String> postParams,
			String charset) throws QQMeishiServerUnreachableException,
			QQMeishiServerLinkNotFoundException, QQMeishiServerRespException,
			QQMeishiReadRespStreamException;
			
}
