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
public class InitMsg extends CmdBasedMessage {

	public static final int COMMAND_ID = 5;

	private String posId;

	public InitMsg(long length, long sequence, String posId) {
		super(length, sequence, COMMAND_ID);
		this.posId = posId;
	}

	/**
	 * @return the posId
	 */
	public String getPosId() {
		return posId;
	}

}
