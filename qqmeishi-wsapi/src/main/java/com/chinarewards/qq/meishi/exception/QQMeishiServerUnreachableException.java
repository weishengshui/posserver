package com.chinarewards.qq.meishi.exception;

/**
 * description：QQ美食服务器不可达
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-7   上午10:55:20
 * @author Seek
 */
public class QQMeishiServerUnreachableException extends Exception {
	
	private static final long serialVersionUID = 9183573830594381338L;
	
	public QQMeishiServerUnreachableException() {
		super();
	}

	public QQMeishiServerUnreachableException(String message, Throwable cause) {
		super(message, cause);
	}

	public QQMeishiServerUnreachableException(String message) {
		super(message);
	}

	public QQMeishiServerUnreachableException(Throwable cause) {
		super(cause);
	}
	
}
