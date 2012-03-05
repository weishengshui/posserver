package com.chinarewards.qq.meishi.exception;

/**
 * description：QQ美食接口访问异常
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-2   下午04:54:24
 * @author Seek
 */
public class QQMeishiInterfaceAccessException extends Exception {
	
	private static final long serialVersionUID = -8800295446800218893L;

	public QQMeishiInterfaceAccessException() {
		super();
	}

	public QQMeishiInterfaceAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public QQMeishiInterfaceAccessException(String message) {
		super(message);
	}

	public QQMeishiInterfaceAccessException(Throwable cause) {
		super(cause);
	}

}
