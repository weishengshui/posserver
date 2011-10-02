package com.chinarewards.qqgbpvn.testing.exception;

/**
 * description：发送Message 异常
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-23   下午05:54:21
 * @author Seek
 */
public class SendMessageException extends Exception {
	
	private static final long serialVersionUID = -2517308864919707596L;

	public SendMessageException() {
		super();
	}

	public SendMessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public SendMessageException(String message) {
		super(message);
	}

	public SendMessageException(Throwable cause) {
		super(cause);
	}
	
}
