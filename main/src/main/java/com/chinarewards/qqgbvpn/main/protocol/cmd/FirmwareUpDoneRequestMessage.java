package com.chinarewards.qqgbvpn.main.protocol.cmd;

/**
 * update firmware done request message
 * 
 * @author Seek
 * 
 */
public class FirmwareUpDoneRequestMessage implements ICommand {

	public static final long FIRMWARE_UP_DONE_CMD_ID = 17;
	
	private long cmdId;

	private String posId;

	@Override
	public String toString() {
		return " [cmdId=" + cmdId + ", posId=" + posId + "]";
	}

	public long getCmdId() {
		return cmdId;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	}

}
