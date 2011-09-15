package com.chinarewards.qqgbvpn.main.protocol.cmd;

/**
 * Defines the message of a POS client firmware update request.
 * 
 * @author cyril
 * @since 0.1.0
 */
public class PosRequestFirmwareUpdate implements ICommand {

	private final String posId;

	public PosRequestFirmwareUpdate(String posId) {
		this.posId = posId;
	}

	/**
	 * Current implementation always return
	 * {@link CmdConstant#POS_REQUEST_FIRMWARE_UPDATE}
	 */
	public long getCmdId() {
		return CmdConstant.POS_REQUEST_FIRMWARE_UPDATE;
	}

	/**
	 * Returns the POS ID.
	 * 
	 * @return the POS ID.
	 */
	public String getPosId() {
		return posId;
	}

}
