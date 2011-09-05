package com.chinarewards.qqgbvpn.mgmtui.exception;

/**
 * description：service Exception
 * @copyright binfen.cc
 * @projectName mgmtui
 * @time 2011-9-5   下午12:03:28
 * @author Seek
 */
public class ServiceException extends Exception {
	
	private static final long serialVersionUID = 8060814960814385234L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

}
