/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.impl;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class ServiceDispatcherException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -818769558177065122L;

	/**
	 * 
	 */
	public ServiceDispatcherException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServiceDispatcherException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param arg0
	 */
	public ServiceDispatcherException(String message) {
		super(message);
	}

	/**
	 * @param arg0
	 */
	public ServiceDispatcherException(Throwable cause) {
		super(cause);
	}
}
