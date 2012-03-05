package com.chinarewards.qq.meishi.service;

import com.chinarewards.qq.meishi.exception.QQMeishiDataParseException;
import com.chinarewards.qq.meishi.exception.QQMeishiInterfaceAccessException;
import com.chinarewards.qq.meishi.exception.QQMeishiReadStreamException;
import com.chinarewards.qq.meishi.vo.MeishiConvertQQMiReqVO;
import com.chinarewards.qq.meishi.vo.MeishiConvertQQMiRespVO;

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
	 * @param meishiConvertQQMiReqVO
	 * @return
	 * @time 2012-3-2   下午05:58:08
	 * @author Seek
	 */
	public MeishiConvertQQMiRespVO convertQQMi(MeishiConvertQQMiReqVO 
			meishiConvertQQMiReqVO) throws QQMeishiInterfaceAccessException,
			QQMeishiReadStreamException, QQMeishiDataParseException;
	
}
