package com.chinarewards.qq.meishi.conn.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qq.meishi.conn.QQMeishiConnect;
import com.chinarewards.qq.meishi.conn.vo.QQMeishiConnRespVO;
import com.chinarewards.qq.meishi.exception.QQMeishiReadRespStreamException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerLinkNotFoundException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerRespException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerUnreachableException;
import com.chinarewards.qq.meishi.util.IoUtil;
import com.chinarewards.qq.meishi.util.json.JsonUtil;
import com.chinarewards.qq.meishi.util.qqmeishi.QQMeishiUtil;

/**
 * description：Httpclient implements
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
			HttpMethod httpMethod, ContentFormat contentFormat,
			Map<String, String> postParams, String charset)
			throws QQMeishiServerUnreachableException {
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
			
			httpReqest.addHeader("host", hostAddr);
			if(HttpMethod.POST.equals(httpMethod)){
				if(ContentFormat.Json.equals(contentFormat)){
					httpReqest.addHeader("Accept", "application/json"); 
					
					String requestJson = JsonUtil.formatObject(postParams);
					StringEntity entity = new StringEntity(requestJson, charset);
					entity.setContentType("application/json; charset="+charset);
					
					//与QQ联调时使用end
					((HttpPost)httpReqest).setEntity(entity);
				}else {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					//设置post参数
					if (postParams != null) {
						for (String key : postParams.keySet()) {
							Object v = postParams.get(key);
							if (v instanceof java.util.Collection) {
								@SuppressWarnings("unchecked")
								ArrayList<String> list = (ArrayList<String>) v;
								for (String s : list) {
									s = s==null?null: QQMeishiUtil.encoder((String) s, charset);
									params.add(new BasicNameValuePair(key, s));
								}
							} else {
								v = v==null?null: QQMeishiUtil.encoder((String) v, charset);
								params.add(new BasicNameValuePair(key, (String) v));
							}
						}
					}
					
					HttpEntity httpentity = new UrlEncodedFormEntity(params, charset);
					//与QQ联调时使用end
					((HttpPost)httpReqest).setEntity(httpentity);
				}
			}else {	//GET
				httpReqest.addHeader("Content-Type", "application/text; charset=" + charset);
			}
			
			HttpResponse httpResponse = client.execute(httpReqest);
			int httpStatusCode = httpResponse.getStatusLine().getStatusCode();
			
			//请求成功
			if (httpStatusCode == HttpStatus.SC_OK	//200
					|| httpStatusCode == HttpStatus.SC_NOT_FOUND	//404
					|| httpStatusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) { // 500
				return httpResponse;
			}
			
			/* other status code~ */
			throw new Exception("request error, status code: " + httpResponse.
					getStatusLine().getStatusCode());
		} catch (Throwable e) {
			if (httpReqest != null && !httpReqest.isAborted()) {
				httpReqest.abort();
			}
			
			QQMeishiServerUnreachableException serverUnreachableException = new QQMeishiServerUnreachableException(e);
			throw serverUnreachableException;
		}
	}
	
	public QQMeishiConnRespVO requestServer(String url, String hostAddr,
			HttpMethod httpMethod, ContentFormat contentFormat,
			Map<String, String> postParams, String charset)
			throws QQMeishiServerUnreachableException,
			QQMeishiServerLinkNotFoundException, QQMeishiServerRespException,
			QQMeishiReadRespStreamException {
		HttpResponse httpResponse = sendRequest(url, hostAddr, httpMethod,
				contentFormat, postParams, charset);
		int httpStatusCode = httpResponse.getStatusLine().getStatusCode();
		
		/* read response stream */
		String rawContent = null;
		try {
			String contentCharset = httpResponse.getParams().getParameter(
					CoreProtocolPNames.HTTP_CONTENT_CHARSET).toString();
			rawContent = IoUtil.readStream(httpResponse.getEntity().getContent(), contentCharset);
		} catch (Throwable e) {
			QQMeishiReadRespStreamException readRespStreamException = new QQMeishiReadRespStreamException(e);
			readRespStreamException.setHttpStatusCode(httpStatusCode);
			throw readRespStreamException;
		}
		
		if(httpStatusCode == HttpStatus.SC_NOT_FOUND) { //404
			QQMeishiServerLinkNotFoundException linkNotFoundException = new QQMeishiServerLinkNotFoundException();
			linkNotFoundException.setHttpStatusCode(httpStatusCode);
			linkNotFoundException.setRawContent(rawContent);
			throw linkNotFoundException; 
		}
		
		if(httpStatusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) { //500
			QQMeishiServerRespException serverRespException = new QQMeishiServerRespException();
			serverRespException.setHttpStatusCode(httpStatusCode);
			serverRespException.setRawContent(rawContent);
			throw serverRespException; 
		}
		
		QQMeishiConnRespVO qqMiConnRespVo = new QQMeishiConnRespVO();
		qqMiConnRespVo.setHttpStatusCode(httpStatusCode);
		qqMiConnRespVo.setRawContent(rawContent);
		return qqMiConnRespVo;
	}
	
}
