/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.socket;

import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommandId;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class InitMsgResult implements ICommandId {

	public static final int COMMAND_ID = 6;

	private byte[] challenge = new byte[8];

	public InitMsgResult(byte[] challenge) {
		this.challenge = challenge;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbvpn.main.protocol.cmd.ICommandId#getCmdId()
	 */
	@Override
	public long getCmdId() {
		return COMMAND_ID;
	}

	/**
	 * @return the challenge
	 */
	public byte[] getChallenge() {
		return challenge;
	}

}
