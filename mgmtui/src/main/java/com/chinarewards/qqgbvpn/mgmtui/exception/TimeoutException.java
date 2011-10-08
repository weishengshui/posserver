package com.chinarewards.qqgbvpn.mgmtui.exception;

public class TimeoutException extends RuntimeException {
	
	private static final long serialVersionUID = -4287057597145221675L;

	public TimeoutException(String errMessage) {
		super(errMessage);
	 }
}
