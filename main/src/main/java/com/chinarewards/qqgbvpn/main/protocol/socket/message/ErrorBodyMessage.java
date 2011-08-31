package com.chinarewards.qqgbvpn.main.protocol.socket.message;

import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;

/**
 * error body message
 * 
 * @author huangwei
 *
 */
public class ErrorBodyMessage implements IBodyMessage {

	private long cmdId = CmdConstant.ERROR_CMD_ID;
	
	private long errorCode;
	
	//-------------------------------//
	@Override
	public long getCmdId() {
		return cmdId;
	}

	public long getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(long errorCode) {
		this.errorCode = errorCode;
	}

}