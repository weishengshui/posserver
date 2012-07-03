package com.chinarewards.qqgbvpn.main.protocol.cmd;


/**
 * update firmware done response message
 * 
 * @author Seek
 *
 */
public class FirmwareUpDoneResponseMessage implements ICommand {

	public static final long FIRMWARE_UP_DONE_CMD_ID_RESPONSE = 18;
	
	private long cmdId;
	
	private short result;
	
	@Override
	public String toString() {
		return " [cmdId=" + cmdId + ", result=" + result + "]";

	}

	//-------------------------------------------------//
	public long getCmdId() {
		return cmdId;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	}

	public short getResult() {
		return result;
	}

	public void setResult(short result) {
		this.result = result;
	}
	
}
