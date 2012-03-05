package com.chinarewards.qq.meishi.util;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * description：json字符串工具类
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-5   上午11:15:01
 * @author Seek
 */
public final class JsonUtil {
	
	/**
	 * description：json String convert Object
	 * @param jsonString
	 * @param beanClass
	 * @return object
	 * @time 2012-3-5   上午11:14:58
	 * @author Seek
	 */
	public static Object parseObject(String jsonString,
			Class<?> beanClass) throws Throwable {
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		
		Object obj = mapper.readValue(jsonString, beanClass);
		return obj;
	}
	
}
