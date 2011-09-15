package com.chinarewards.qqgbvpn.main.protocol.cmd;

/**
 * Defines the message of a firmware fragment retrieval response.
 * 
 * @author cyril
 * @since 0.1.0
 */
public class GetFirmwareFragmentResponseMessage implements ICommand {

	public static final short RESULT_OK = 0;

	/**
	 * Offset value exceed the actual firmware size (out-of-bound). For example,
	 * if the firmware has a length of 10 bytes, an offset value of 10 is
	 * invalid but 9 is valid.
	 */
	public static final short RESULT_OFFSET_OOB = 1;

	/**
	 * The firmware does not exist on the server. It is an error.
	 */
	public static final short RESULT_FIRMWARE_NOT_FOUND = 2;

	/**
	 * An error has occurred when retrieving firmware on the server. For
	 * example, the firmware is suddenly not available for reading.
	 */
	public static final short RESULT_FIRMWARE_IO_ERROR = 3;

	/**
	 * Invalid argument is passed in the request.
	 */
	public static final short RESULT_INVARG = 4;

	/**
	 * Unexpected error.
	 */
	public static final short RESULT_OTHER_ERROR = 5;

	/**
	 * The POS ID is not found.
	 */
	public static final short RESULT_POS_NOT_FOUND = 6;
	
	/**
	 * Firmware upgrade is not available for this POS machine.
	 */
	public static final short RESULT_NO_FIRMWARE_UPGRADE = 7;
	

	private final short result;

	private byte[] content;

	public GetFirmwareFragmentResponseMessage(short result, byte[] content) {
		this.result = result;
		this.content = content;
	}

	/**
	 * Current implementation always return
	 * {@link CmdConstant#GET_FIRMWARE_FRAGMENT_CMD_ID_RESPONSE}
	 */
	public long getCmdId() {
		return CmdConstant.GET_FIRMWARE_FRAGMENT_CMD_ID_RESPONSE;
	}

	/**
	 * @return the result
	 */
	public int getResult() {
		return result;
	}

	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}

}
