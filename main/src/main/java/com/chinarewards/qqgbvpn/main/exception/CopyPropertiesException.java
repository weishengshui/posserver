package com.chinarewards.qqgbvpn.main.exception;

public class CopyPropertiesException extends Exception {

	private static final long serialVersionUID = -5097408849748964199L;

	public CopyPropertiesException(Exception e) {
		super(e);
	}
	
	public CopyPropertiesException(String msg) {
		super(msg);
	}
}
