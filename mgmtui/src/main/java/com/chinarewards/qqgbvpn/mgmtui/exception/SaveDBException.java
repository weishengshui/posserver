package com.chinarewards.qqgbvpn.mgmtui.exception;

public class SaveDBException extends Exception {

	private static final long serialVersionUID = -6698548271734411731L;

	public SaveDBException(Exception e) {
		super(e);
	}
	
	public SaveDBException(String msg) {
		super(msg);
	}
}
