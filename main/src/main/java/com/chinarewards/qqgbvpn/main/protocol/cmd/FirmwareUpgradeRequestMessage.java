package com.chinarewards.qqgbvpn.main.protocol.cmd;

/**
 * Firmware upgrade request message: to obtain the sizing information about the
 * data.
 * 
 * @author kmtong
 * @see FirmwareUpgradeRequestResponseMessage
 */
public class FirmwareUpgradeRequestMessage implements ICommand {
	
	public static final long FIRMWARE_UPGRADE_CMD_ID = 13;
	
	private long cmdId = FIRMWARE_UPGRADE_CMD_ID;
	
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

}
