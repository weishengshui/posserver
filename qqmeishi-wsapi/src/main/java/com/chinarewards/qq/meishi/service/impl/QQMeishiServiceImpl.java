package com.chinarewards.qq.meishi.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qq.meishi.exception.QQMeishiDataParseException;
import com.chinarewards.qq.meishi.exception.QQMeishiInterfaceAccessException;
import com.chinarewards.qq.meishi.exception.QQMeishiReadStreamException;
import com.chinarewards.qq.meishi.service.QQMeishiService;
import com.chinarewards.qq.meishi.util.JsonUtil;
import com.chinarewards.qq.meishi.util.QQMeishiConnect;
import com.chinarewards.qq.meishi.vo.MeishiConvertQQMiReqVO;
import com.chinarewards.qq.meishi.vo.MeishiConvertQQMiRespVO;
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
	public MeishiConvertQQMiRespVO convertQQMi(MeishiConvertQQMiReqVO 
			meishiConvertQQMiReqVO) throws QQMeishiInterfaceAccessException,
			QQMeishiReadStreamException, QQMeishiDataParseException {
		MeishiConvertQQMiRespVO respVO = null;
		
		String qqMeishiConvert = configuration.getString(QQ_MEISHI_CONVERT_URL_KEY);
		String qqMeishiHostAddress = configuration.getString(QQ_MEISHI_HOST_ADDRESS_KEY);
		
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put("verifyCode", meishiConvertQQMiReqVO.getVerifyCode());
		postParams.put("posid", meishiConvertQQMiReqVO.getPosid());
		postParams.put("consume", String.valueOf(meishiConvertQQMiReqVO.getConsume()));
		postParams.put("password", meishiConvertQQMiReqVO.getPassword());
		
		String respContent = qqMeishiConnect.requestServer(qqMeishiConvert, qqMeishiHostAddress, 
				QQMeishiConnect.HttpMethod.POST, postParams, CHARSET);
		
		try {
			respVO = (MeishiConvertQQMiRespVO) JsonUtil.parseObject(
					respContent, MeishiConvertQQMiRespVO.class);
		} catch (Throwable e) {
			throw new QQMeishiDataParseException(e);
		}
		return respVO;
	}
	
}
