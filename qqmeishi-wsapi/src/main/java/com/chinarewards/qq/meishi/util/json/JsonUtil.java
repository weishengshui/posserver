package com.chinarewards.qq.meishi.util.json;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description：json字符串工具类
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-5   上午11:15:01
 * @author Seek
 */
public final class JsonUtil {
	
	static Logger log = LoggerFactory.getLogger(JsonUtil.class);
	
	/**
	 * description：json String convert Object
	 * @param jsonString
	 * @param beanClass
	 * @return object
	 * @time 2012-3-5   上午11:14:58
	 * @author Seek
	 */
	public static <T> T parseObject(String jsonString,
			TypeReference<T> typeReference) throws Throwable {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		
		@SuppressWarnings("unchecked")		//TODO maven不能编译，必须强制转换<T>，JDK可以，原因不明
		T obj = (T) mapper.readValue(jsonString, typeReference);
		return obj;
	}
	
	/**
	 * description：format Object to Json String
	 * @param obj any object 
	 * @return Json String
	 * @throws Throwable
	 * @time 2012-3-7   上午09:51:00
	 * @author Seek
	 */
	public static String formatObject(Object obj) throws Throwable {
		ObjectMapper mapper = new ObjectMapper();

		return mapper.writeValueAsString(obj);
	}
	
}
