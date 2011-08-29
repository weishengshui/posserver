/**
 * 
 */
package com.chinarewards.qqgbvpn.main;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class PosServerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3615846307986562540L;

	/**
	 * 
	 */
	public PosServerException() {
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public PosServerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public PosServerException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public PosServerException(Throwable arg0) {
		super(arg0);
	}

}
