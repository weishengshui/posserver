package com.chinarewards.qqgbvpn.main.protocol.cmd;

/**
 * Defines the message of a POS client firmware update request.
 * 
 * @author cyril
 * @since 0.1.0
 */
public class PosRequestFirmwareUpdateResponse implements ICommand {

	/**
	 * Firmware upgrade is available.
	 */
	public static final int RESULT_ALLOWED = 0;

	/**
	 * No firmware upgrade is available for this POS machine.
	 */
	public static final int RESULT_NO_FIRMWARE_UPGRADE = 1;

	/**
	 * Firmware upgrade is allowed, but an error has occurred such that the
	 * source of the firmware is not found.
	 */
	public static final int RESULT_FIRMWARE_NOT_AVAILABLE = 2;

	private final int result;

	private final long firmwareSize;

	private final String firmwareName;

	public PosRequestFirmwareUpdateResponse(int result, long firmwareSize,
			String firmwareName) {
		this.result = result;
		this.firmwareName = firmwareName;
		this.firmwareSize = firmwareSize;
	}

	/**
	 * Current implementation always return
	 * {@link CmdConstant#POS_REQUEST_FIRMWARE_RESPONSE}
	 */
	public long getCmdId() {
		return CmdConstant.POS_REQUEST_FIRMWARE_RESPONSE;
	}

	/**
	 * Refers to the constants started with prefix <code>RESULT_</code>.
	 * 
	 * @return the result
	 */
	public int getResult() {
		return result;
	}

	/**
	 * @return the firmwareSize
	 */
	public long getFirmwareSize() {
		return firmwareSize;
	}

	/**
	 * @return the firmwareName
	 */
	public String getFirmwareName() {
		return firmwareName;
	}

}
