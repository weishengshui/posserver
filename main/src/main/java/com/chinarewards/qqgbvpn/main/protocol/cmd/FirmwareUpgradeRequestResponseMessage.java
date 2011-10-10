package com.chinarewards.qqgbvpn.main.protocol.cmd;

/**
 * mainly to return the sizing information of the new firmware
 * 
 * @author kmtong
 * 
 */
public class FirmwareUpgradeRequestResponseMessage implements ICommand {
	
	private int result;

	private long size;

	private String firmwareName;
	
	@Override
	public String toString() {
		return "FirmwareUpgradeRequestResponseMessage [cmdId=" + getCmdId() + ", result=" + result
				+ ", size=" + size + ", firmwareName=" + firmwareName + "]";
	}

	// -------------------------------------------------//
	public long getCmdId() {
		return CmdConstant.FIRMWARE_UPGRADE_CMD_ID_RESPONSE;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getFirmwareName() {
		return firmwareName;
	}

	public void setFirmwareName(String firmwareName) {
		this.firmwareName = firmwareName;
	}

}
