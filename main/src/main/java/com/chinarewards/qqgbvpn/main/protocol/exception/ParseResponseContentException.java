package com.chinarewards.qqgbvpn.main.protocol.exception;

/**
 * description：format response content Exception
 * @copyright binfen.cc
 * @projectName main
 * @time 2011-9-29   下午03:02:41
 * @author Seek
 */
public class ParseResponseContentException extends Exception {
	
	private static final long serialVersionUID = -5710460677241445627L;

	public ParseResponseContentException() {
		super();
	}

	public ParseResponseContentException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParseResponseContentException(String message) {
		super(message);
	}

	public ParseResponseContentException(Throwable cause) {
		super(cause);
	}
	
}
