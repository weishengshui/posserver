package com.chinarewards.qqgbvpn.mgmtui.exception;

public class UnUseableRNException extends Exception {

	private static final long serialVersionUID = -2706835778805254758L;

	public UnUseableRNException(Exception e) {
		super(e);
	}

	public UnUseableRNException(String msg) {
		super(msg);
	}

	public UnUseableRNException() {
		super();
	}

	public UnUseableRNException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnUseableRNException(Throwable cause) {
		super(cause);
	}

}
