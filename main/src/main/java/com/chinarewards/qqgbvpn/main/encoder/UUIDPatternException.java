package com.chinarewards.qqgbvpn.main.encoder;
/**
 * Throw it when UUID is not correct.
 * 
 * @author yanxin
 * @since 2011-11-08
 */
public class UUIDPatternException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5612015880281157147L;

	public UUIDPatternException() {

	}

	public UUIDPatternException(String msg) {
		super(msg);
	}

	public UUIDPatternException(String msg, Throwable e) {
		super(msg, e);
	}
}
