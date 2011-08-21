/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.socket;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class LoggedInMessage extends CmdBasedMessage {

	public static final int SESSION_KEY_LENGTH = 8;

	private byte[] sessionKey = new byte[SESSION_KEY_LENGTH];

	public LoggedInMessage(long length, long sequence, int commandId,
			byte[] sessionKey) {
		super(length, sequence, commandId);
		this.sessionKey = sessionKey;
	}

	/**
	 * @return the sessionKey
	 */
	public byte[] getSessionKey() {
		return sessionKey;
	}

}
