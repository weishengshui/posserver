package com.chinarewards.qqgbpvn.testing.exception;

/**
 * description：网络操作 异常
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-23   下午05:54:21
 * @author Seek
 */
public class SocketProcessException extends Exception {
	
	private static final long serialVersionUID = -2517308864919707596L;

	public SocketProcessException() {
		super();
	}

	public SocketProcessException(String message, Throwable cause) {
		super(message, cause);
	}

	public SocketProcessException(String message) {
		super(message);
	}

	public SocketProcessException(Throwable cause) {
		super(cause);
	}
	
}
