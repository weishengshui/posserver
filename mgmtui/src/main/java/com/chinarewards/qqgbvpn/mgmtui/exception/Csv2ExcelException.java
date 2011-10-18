package com.chinarewards.qqgbvpn.mgmtui.exception;

public class Csv2ExcelException extends Exception {

	private static final long serialVersionUID = -2727037783849180220L;

	public Csv2ExcelException(Exception e) {
		super(e);
	}

	public Csv2ExcelException(String msg) {
		super(msg);
	}

	public Csv2ExcelException() {
		super();
	}

	public Csv2ExcelException(String message, Throwable cause) {
		super(message, cause);
	}

	public Csv2ExcelException(Throwable cause) {
		super(cause);
	}

}
