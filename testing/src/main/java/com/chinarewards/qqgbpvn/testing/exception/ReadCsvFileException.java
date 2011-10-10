package com.chinarewards.qqgbpvn.testing.exception;

/**
 * description：read csv data file Exception
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-26   上午10:25:50
 * @author Seek
 */
public class ReadCsvFileException extends Exception {
	
	private static final long serialVersionUID = 1204132445987525758L;
	
	public ReadCsvFileException() {
		super();
	}

	public ReadCsvFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReadCsvFileException(String message) {
		super(message);
	}

	public ReadCsvFileException(Throwable cause) {
		super(cause);
	}

}
