package com.chinarewards.qq.meishi.exception;

/**
 * description：读取网络流异常
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-2   下午05:40:33
 * @author Seek
 */
public class QQMeishiReadStreamException extends Exception {
	
	private static final long serialVersionUID = 7126868581450582850L;

	public QQMeishiReadStreamException() {
		super();
	}

	public QQMeishiReadStreamException(String message, Throwable cause) {
		super(message, cause);
	}

	public QQMeishiReadStreamException(String message) {
		super(message);
	}

	public QQMeishiReadStreamException(Throwable cause) {
		super(cause);
	}
	
}
