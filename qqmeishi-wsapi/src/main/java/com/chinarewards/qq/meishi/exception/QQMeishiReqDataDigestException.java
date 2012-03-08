package com.chinarewards.qq.meishi.exception;

/**
 * description：QQ美食请求数据签名异常
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-6   下午03:20:39
 * @author Seek
 */
public class QQMeishiReqDataDigestException extends Exception {
	
	private static final long serialVersionUID = -2338496846168172741L;

	public QQMeishiReqDataDigestException() {
		super();
	}

	public QQMeishiReqDataDigestException(String message, Throwable cause) {
		super(message, cause);
	}

	public QQMeishiReqDataDigestException(String message) {
		super(message);
	}

	public QQMeishiReqDataDigestException(Throwable cause) {
		super(cause);
	}
	
}
