/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class MessageHeaderCodecException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -651642153976694636L;

	/**
	 * 
	 */
	public MessageHeaderCodecException() {
	}

	/**
	 * @param cause
	 */
	public MessageHeaderCodecException(String message) {
		super(message);
	}

	/**
	 * @param arg0
	 */
	public MessageHeaderCodecException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MessageHeaderCodecException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
