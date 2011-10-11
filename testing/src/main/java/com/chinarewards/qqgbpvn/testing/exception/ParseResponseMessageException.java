package com.chinarewards.qqgbpvn.testing.exception;

/**
 * description：解析响应消息 异常
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-23   下午03:25:50
 * @author Seek
 */
public class ParseResponseMessageException extends Exception {
	
	private static final long serialVersionUID = 5846884553952195061L;

	public ParseResponseMessageException() {
		super();
	}

	public ParseResponseMessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParseResponseMessageException(String message) {
		super(message);
	}

	public ParseResponseMessageException(Throwable cause) {
		super(cause);
	}
	
}
