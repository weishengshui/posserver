package com.chinarewards.qqgbvpn.main.protocol.cmd;

/**
 * update firmware done request message
 * 
 * @author Seek
 * 
 */
public class FirmwareUpDoneRequestMessage implements ICommand {

	private long cmdId;

	private String posId;

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
