package com.chinarewards.qq.meishi.exception;

/**
 * description：QQ美食数据解析异常
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-2   下午04:56:04
 * @author Seek
 */
public class QQMeishiDataParseException extends Exception {
	
	private static final long serialVersionUID = -7541114094086452957L;

	public QQMeishiDataParseException() {
		super();
	}

	public QQMeishiDataParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public QQMeishiDataParseException(String message) {
		super(message);
	}

	public QQMeishiDataParseException(Throwable cause) {
		super(cause);
	}
	
}
