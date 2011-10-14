package com.chinarewards.qqgbvpn.main.protocol.cmd;

/**
 * Firmware upgrade request message: to obtain the sizing information about the
 * data.
 * 
 * @author kmtong
 * @see FirmwareUpgradeRequestResponseMessage
 */
public class FirmwareUpgradeRequestMessage implements ICommand {

	private String posId;

	@Override
	public String toString() {
		return "cmdId=" + getCmdId() + ", posId="
				+ posId;
	}

	public long getCmdId() {
		return CmdConstant.FIRMWARE_UPGRADE_CMD_ID;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

}
