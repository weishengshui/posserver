/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.exception;

/**
 * This Exception throw when Login failed.
 * 
 * @author cream
 * @since 1.0.0 2011-08-29
 */
public class PermissionDeniedException extends Exception {

	private static final long serialVersionUID = 8987891536664269340L;

	public PermissionDeniedException() {
		super();
	}

	public PermissionDeniedException(String message, Throwable cause) {
		super(message, cause);
	}

	public PermissionDeniedException(String message) {
		super(message);
	}

	public PermissionDeniedException(Throwable cause) {
		super(cause);
	}

}
