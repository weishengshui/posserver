package com.chinarewards.qqgbvpn.main.protocol.cmd;

/**
 * Firmware upgrade request message: to obtain the sizing information about the
 * data.
 * 
 * @author kmtong
 * @see FirmwareUpgradeRequestResponseMessage
 */
public class FirmwareUpgradeRequestMessage implements ICommand {

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
