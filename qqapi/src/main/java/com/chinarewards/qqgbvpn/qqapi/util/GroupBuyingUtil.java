package com.chinarewards.qqgbvpn.qqapi.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;

public class GroupBuyingUtil {
	
	//private final static HttpClient client = new DefaultHttpClient();
	
	
	/**
	 * MD5加密字符串
	 * @author iori
	 * @param s
	 * @return
	 * @throws Exception 
	 */
	public static String MD5(String s) throws MD5Exception {
		try {
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(s.getBytes());
			byte[] md = mdInst.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < md.length; i++) {
				int val = ((int) md[i]) & 0xff;
				if (val < 16) {
					sb.append("0");
				}
				sb.append(Integer.toHexString(val));
			}
			return sb.toString();
		} catch (Exception e) {
			throw new MD5Exception(e);
		}
	}
	
	/**
	 * 创建线程安全的HtttpClient
	 * @return
	 */
	public static DefaultHttpClient getThreadSafeClient() {

		DefaultHttpClient httpClient = new DefaultHttpClient();
		ClientConnectionManager mgr = httpClient.getConnectionManager();
		HttpParams params = httpClient.getParams();

		httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(
				params, mgr.getSchemeRegistry()), params);

		return httpClient;
	}
	
	/**
	 * 发送POST请求到相应URL
	 * @author iori
	 * @param url
	 * @param params POST参数
	 * @return
	 */
	public static InputStream sendPost(String url, HashMap<String,Object> postParams) throws SendPostTimeOutException {
		try {
			HttpClient client = getThreadSafeClient();
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			HttpPost post = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			//设置post参数
			if (postParams != null) {
				for (String key : postParams.keySet()) {
					Object v = postParams.get(key);
					if (v instanceof java.util.Collection) {
						ArrayList<String> list = (ArrayList<String>) v;
						for (String s : list) {
							params.add(new BasicNameValuePair(key, URLEncoder.encode((String) s,"UTF-8")));
						}
					} else {
						params.add(new BasicNameValuePair(key, URLEncoder.encode((String) v,"UTF-8")));
					}
				}
			}
			HttpEntity httpentity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			post.setEntity(httpentity);
			HttpResponse httpResponse = client.execute(post);
			//请求成功
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					return entity.getContent();
				}
			}
			throw new Exception("send post error, status code: " + httpResponse.getStatusLine().getStatusCode());
		} catch (Exception e) {
			throw new SendPostTimeOutException(e);
		}
	}

	/**
	 * 解析QQ返回的XML流，返回解析结果
	 * @author iori
	 * @param in      XML输入流
	 * @param nodeDir 子元素的目录
	 * @param bean 	     返回对象Class   
	 * @return
	 * @throws ParseXMLException
	 */
	public static HashMap<String,Object> parseXML(InputStream in, String nodeDir, Class bean) throws ParseXMLException {
		try {
			HashMap<String,Object> parseResult = new HashMap<String,Object>();
			SAXReader reader = new SAXReader(); 
			Document xmlDoc = reader.read(new InputStreamReader(in, "UTF-8"));
			Element root = xmlDoc.getRootElement();
			String resultCode = root.elementText("resultCode");
			parseResult.put("resultCode", resultCode);
			//0才有item
			if ("0".equals(resultCode)) {
				List<Element> listRowSet = xmlDoc.selectNodes(nodeDir);
				List itemList = new ArrayList();
				for (Element ele : listRowSet) {
					Object item = copyProperties(bean,ele);
					itemList.add(item);
				}
				parseResult.put("items", itemList);
			}
			return parseResult;
		} catch (Exception e) {
			throw new ParseXMLException(e);
		}
	}
	
	/**
	 * 动态将XML的值设置给对象
	 * @author iori
	 * @param bean
	 * @param ele
	 * @return
	 * @throws Exception
	 */
	private static Object copyProperties(Class bean, Element ele) throws Exception {
		Object obj = bean.newInstance();
		//遍历第个item的子元素
		for(Iterator<Element> it = ele.elementIterator();it.hasNext();){
			Element element = (Element) it.next();
			//遍历对象的所有方法
			for (Method ms : bean.getMethods()) {
				String name = ms.getName();
				//是相应属性的set方法
				if (name.startsWith("set") && name.substring(3).equalsIgnoreCase(element.getName())) {
					//属性值
					String param = element.getText();
					if (param != null) {
						//调用set方法
						ms.invoke(obj, new Object[] { param });
					}
					//保存一个值只设置一次就OK
					break;
				}
			}
		}
		return obj;
	}
	
}
