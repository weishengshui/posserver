package com.chinarewards.qq.meishi.service.impl;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qq.meishi.conn.QQMeishiConnect;
import com.chinarewards.qq.meishi.conn.vo.QQMeishiConnRespVO;
import com.chinarewards.qq.meishi.exception.QQMeishiReadRespStreamException;
import com.chinarewards.qq.meishi.exception.QQMeishiReqDataDigestException;
import com.chinarewards.qq.meishi.exception.QQMeishiRespDataParseException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerLinkNotFoundException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerRespException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerUnreachableException;
import com.chinarewards.qq.meishi.service.QQMeishiService;
import com.chinarewards.qq.meishi.util.json.JacksonTypeReference;
import com.chinarewards.qq.meishi.util.json.JsonUtil;
import com.chinarewards.qq.meishi.util.qqmeishi.QQMeishiUtil;
import com.chinarewards.qq.meishi.vo.QQMeishiConvertQQMiReqVO;
import com.chinarewards.qq.meishi.vo.QQMeishiConvertQQMiRespVO;
import com.chinarewards.qq.meishi.vo.common.QQMeishiResp;
import com.google.inject.Inject;

/**
 * description：QQ美食 service implements
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-2   下午05:56:35
 * @author Seek
 */
public class QQMeishiServiceImpl implements QQMeishiService {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	private static final String QQ_MEISHI_HOST_ADDRESS_KEY = "qq.meishi.host";
	private static final String QQ_MEISHI_CONVERT_URL_KEY  = "qq.meishi.url.convertQQMi";
	private static final String QQ_MEISHI_COMM_SECRET_KEY  = "qq.meishi.communication.secretkey";
	
	private static final String CHARSET = "UTF-8";
	
	@Inject
	protected QQMeishiConnect qqMeishiConnect;
	
	@Inject
	protected Configuration configuration;
	
	@Override
	public QQMeishiResp<QQMeishiConvertQQMiRespVO> convertQQMi(
			QQMeishiConvertQQMiReqVO meishiConvertQQMiReqVO)
			throws QQMeishiServerUnreachableException,
			QQMeishiServerLinkNotFoundException, QQMeishiServerRespException,
			QQMeishiRespDataParseException, QQMeishiReadRespStreamException,
			QQMeishiReqDataDigestException {
		QQMeishiResp<QQMeishiConvertQQMiRespVO> respVO = null;
		
		String qqMeishiConvert = configuration.getString(QQ_MEISHI_CONVERT_URL_KEY);
		String qqMeishiHostAddress = configuration.getString(QQ_MEISHI_HOST_ADDRESS_KEY);
		
		/* TODO 由于sig算法是对key=value的先后是有顺序的，所以使用TreeMap，采用自然排序 */
		Map<String, String> serviceParams = new TreeMap<String, String>();
		serviceParams.put("verifyCode", meishiConvertQQMiReqVO.getVerifyCode());
		serviceParams.put("posid", meishiConvertQQMiReqVO.getPosid());
		serviceParams.put("consume", String.valueOf(meishiConvertQQMiReqVO.getConsume()));
		serviceParams.put("password", meishiConvertQQMiReqVO.getPassword());
		
		Map<String, String> reqParams = new TreeMap<String, String>();
		reqParams.put("method", "pos.addLog");
		reqParams.put("ts", String.valueOf(QQMeishiUtil.qqMeishiGetTime()));
		reqParams.put("client", "pos");
		reqParams.put("format", "JSON");
		QQMeishiUtil.appendArgsMap(reqParams, serviceParams);
		reqParams.put("uin", null);
		reqParams.put("skey", null);
		reqParams.put("cid", null);
		reqParams.put("ip", null);
		reqParams.put("ver", "1.0");
		
		try {
			String commSecretKey = configuration.getString(QQ_MEISHI_COMM_SECRET_KEY);
			String sig = QQMeishiUtil.buildSig(reqParams, commSecretKey, CHARSET);
			log.debug("sig:"+sig);
			reqParams.put("sig", sig);
		} catch (Throwable e) {
			throw new QQMeishiReqDataDigestException(e);
		}
		
		log.debug("QQ meishi reqParams:" + reqParams.toString());
		QQMeishiConnRespVO qqMeishiConnRespVO = qqMeishiConnect.requestServer(
				qqMeishiConvert, qqMeishiHostAddress,
				QQMeishiConnect.HttpMethod.POST,
				QQMeishiConnect.ContentFormat.Form, reqParams, CHARSET);
		
		log.debug("QQ Meishi respContent:" + qqMeishiConnRespVO.getRawContent());
		try {
			String packageRespContent = qqMeishiConnRespVO.getRawContent()
					.replace("\"result\":\"\"", "\"result\":null");
			respVO = JsonUtil.parseObject(packageRespContent, 
						new JacksonTypeReference<QQMeishiResp<QQMeishiConvertQQMiRespVO>>() {}
					 );
		} catch (Throwable e) {
			QQMeishiRespDataParseException dataParseException = new QQMeishiRespDataParseException(e);
			dataParseException.setHttpStatusCode(qqMeishiConnRespVO.getHttpStatusCode());
			dataParseException.setRawContent(qqMeishiConnRespVO.getRawContent());
			
			throw dataParseException;
		}
		return respVO;
	}
	
}
