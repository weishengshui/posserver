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
public class InitMsg2 extends MsgV2 {

	public static final int COMMAND_ID = 5;

	private String posId;

	public InitMsg2(long seq, long ack, int flags, int checksum, long size, String posId) {
		super(seq, ack, flags, checksum, size);
		this.posId = posId;
	}

	/**
	 * @return the posId
	 */
	public String getPosId() {
		return posId;
	}

}
