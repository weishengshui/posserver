package com.chinarewards.qq.meishi.util.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qq.meishi.exception.QQMeishiDataParseException;
import com.chinarewards.qq.meishi.exception.QQMeishiInterfaceAccessException;
import com.chinarewards.qq.meishi.exception.QQMeishiReadStreamException;
import com.chinarewards.qq.meishi.util.IoUtil;
import com.chinarewards.qq.meishi.util.QQMeishiConnect;

/**
 * description：httpclient implements
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-5   下午03:12:22
 * @author Seek
 */
public class QQMeishiConnectImpl implements QQMeishiConnect {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * 创建线程安全的HtttpClient
	 */
	private static DefaultHttpClient getThreadSafeClient() {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		ClientConnectionManager mgr = httpClient.getConnectionManager();
		HttpParams params = httpClient.getParams();

		httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(mgr.getSchemeRegistry()), params);
		return httpClient;
	}
	
	/**
	 * 发送请求
	 */
	private HttpResponse sendRequest(String url, String hostAddr,
			HttpMethod httpMethod, Map<String, String> postParams,
			String charset) throws QQMeishiInterfaceAccessException {
		HttpRequestBase httpReqest = null;
		charset = charset.trim();
		try {
			HttpClient client = getThreadSafeClient();
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			
			if(HttpMethod.POST.equals(httpMethod)){
				httpReqest = new HttpPost(url);
			}else {
				httpReqest = new HttpGet(url);
			}
			
			//设置处理请求的字符集
			if (charset != null && !"".equals(charset)) {
				httpReqest.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset);
			}
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			//设置post参数
			if (postParams != null) {
				for (String key : postParams.keySet()) {
					Object v = postParams.get(key);
					if (v instanceof java.util.Collection) {
						ArrayList<String> list = (ArrayList<String>) v;
						for (String s : list) {
							s = s==null?null: URLEncoder.encode((String) s, charset);
							params.add(new BasicNameValuePair(key, s));
						}
					} else {
						v = v==null?null: URLEncoder.encode((String) v, charset);
						params.add(new BasicNameValuePair(key, (String) v));
					}
				}
			}
			httpReqest.addHeader("host", hostAddr);
			
			HttpResponse httpResponse = client.execute(httpReqest);
			//请求成功
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return httpResponse;
			}
			throw new Exception("request error, status code: " + httpResponse.
					getStatusLine().getStatusCode());
		} catch (Throwable e) {
			if (httpReqest != null && !httpReqest.isAborted()) {
				httpReqest.abort();
			}
			throw new QQMeishiInterfaceAccessException(e);
		}
	}
	
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
			QQMeishiReadStreamException {
		HttpResponse httpResponse = sendRequest(url, hostAddr, httpMethod, 
				postParams, charset);
		
		String str = null;
		try {
			String contentCharset = httpResponse.getParams().getParameter(
					CoreProtocolPNames.HTTP_CONTENT_CHARSET).toString();
			str = IoUtil.readStream(httpResponse.getEntity().getContent(), contentCharset);
		} catch (Throwable e) {
			throw new QQMeishiReadStreamException(e);
		}
		return str;
	}
	
}
