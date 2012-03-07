package com.chinarewards.qq.meishi.domain.status;

public class QQMeishiXactionPosnetErrorCode {

	/**
	 * 成功
	 */
	public static final int POSSEV_SUCCESS				            = 0;
	/**
	 * QQ美食服务器不可达
	 */
	public static final int POSSEV_ERROR_QQWS_UNREACHABLE			= 1;
	/**
	 * QQ美食服务器响应异常
	 */
	public static final int POSSEV_ERROR_QQWS_RESPERROR		    	= 2;
	/**
	 * QQ美食服务器链接不存在
	 */
	public static final int POSSEV_ERROR_QQWS_NOCONNECT				= 3;
	/**
	 * QQ美食响应数据解析异常
	 */
	public static final int POSSEV_ERROR_QQWS_DATAPARSE				= 4;
	/**
	 * QQ美食读取响应流异常
	 */
	public static final int POSSEV_ERROR_QQWS_IO					= 5;
	/**
	 * QQ美食请求数据签名异常
	 */
	public static final int POSSEV_ERROR_QQWS_SIG					= 6;
}
