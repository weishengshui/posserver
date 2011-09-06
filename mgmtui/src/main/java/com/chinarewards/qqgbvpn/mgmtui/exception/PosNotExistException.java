/**
 * 
 */
package com.chinarewards.qqgbvpn.mgmtui.exception;

/**
 * @author cream
 * 
 */
public class PosNotExistException extends Exception {

	private static final long serialVersionUID = -62752790863062368L;

	/**
	 * 
	 */
	public PosNotExistException() {
	}

	/**
	 * @param message
	 */
	public PosNotExistException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public PosNotExistException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PosNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

}
