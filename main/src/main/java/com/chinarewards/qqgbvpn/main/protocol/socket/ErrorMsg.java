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
public class ErrorMsg implements ICommandId {

	public static final long COMMAND_ID = 4294967295L;

	private int errorCode;

	public ErrorMsg(int result) {
		this.errorCode = result;
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
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

}
