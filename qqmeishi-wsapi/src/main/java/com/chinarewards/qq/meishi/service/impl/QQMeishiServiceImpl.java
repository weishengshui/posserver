package com.chinarewards.qq.meishi.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qq.meishi.exception.QQMeishiReqDataDigestException;
import com.chinarewards.qq.meishi.exception.QQMeishiRespDataParseException;
import com.chinarewards.qq.meishi.exception.QQMeishiReadRespStreamException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerLinkNotFoundException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerRespException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerUnreachableException;
import com.chinarewards.qq.meishi.service.QQMeishiService;
import com.chinarewards.qq.meishi.util.QQMeishiConnect;
import com.chinarewards.qq.meishi.util.json.JacksonTypeReference;
import com.chinarewards.qq.meishi.util.json.JsonUtil;
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
		
		Map<String, String> reqParams = new HashMap<String, String>();
		reqParams.put("verifyCode", meishiConvertQQMiReqVO.getVerifyCode());
		reqParams.put("posid", meishiConvertQQMiReqVO.getPosid());
		reqParams.put("consume", String.valueOf(meishiConvertQQMiReqVO.getConsume()));
		reqParams.put("password", meishiConvertQQMiReqVO.getPassword());
		log.info("QQ meishi reqParams:" + reqParams.toString());
		System.out.println("QQ meishi reqParams:" + reqParams.toString());
		
		String respContent = qqMeishiConnect.requestServer(qqMeishiConvert, qqMeishiHostAddress, 
				QQMeishiConnect.HttpMethod.POST, reqParams, CHARSET);
		log.info("QQ Meishi respContent:" + respContent);
		System.out.println("QQ Meishi respContent:" + respContent);
		try {
			respVO = JsonUtil.parseObject(
					respContent, new JacksonTypeReference<QQMeishiResp<QQMeishiConvertQQMiRespVO>>() {
					});
		} catch (Throwable e) {
			throw new QQMeishiRespDataParseException(e);
		}
		return respVO;
	}
	
}
