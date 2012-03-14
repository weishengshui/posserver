package com.chinarewards.qq.meishi.service;

import com.chinarewards.qq.meishi.exception.QQMeishiReqDataDigestException;
import com.chinarewards.qq.meishi.exception.QQMeishiRespDataParseException;
import com.chinarewards.qq.meishi.exception.QQMeishiReadRespStreamException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerLinkNotFoundException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerRespException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerUnreachableException;
import com.chinarewards.qq.meishi.vo.QQMeishiConvertQQMiReqVO;
import com.chinarewards.qq.meishi.vo.QQMeishiConvertQQMiRespVO;
import com.chinarewards.qq.meishi.vo.common.QQMeishiResp;

/**
 * description：QQ美食 service interface
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-2   下午05:55:42
 * @author Seek
 */
public interface QQMeishiService {

	/**
	 * description：兑换QQ米
	 * @param meishiConvertQQMiReqVO 兑换QQ米，业务请求对象
	 * @return QQMeishiResp<QQMeishiConvertQQMiRespVO> 兑换QQ米业务响应对象 
	 * @throws QQMeishiServerUnreachableException QQ美食服务器不可达
	 * @throws QQMeishiServerLinkNotFoundException QQ美食服务器链接不存在
	 * @throws QQMeishiServerRespException QQ美食服务器响应异常
	 * @throws QQMeishiRespDataParseException QQ美食响应数据解析异常
	 * @throws QQMeishiReadRespStreamException QQ美食读取响应流异常
	 * @throws QQMeishiReqDataDigestException QQ美食请求数据签名异常
	 * @time 2012-3-7   上午11:15:17
	 * @author Seek
	 */
	public QQMeishiResp<QQMeishiConvertQQMiRespVO> convertQQMi(
			QQMeishiConvertQQMiReqVO meishiConvertQQMiReqVO)
			throws QQMeishiServerUnreachableException,
			QQMeishiServerLinkNotFoundException, QQMeishiServerRespException,
			QQMeishiRespDataParseException, QQMeishiReadRespStreamException,
			QQMeishiReqDataDigestException;
	
}
