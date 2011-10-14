package com.chinarewards.qqgbvpn.main.protocol.cmd;

/**
 * Defines the message of a POS client firmware update request.
 * 
 * @author cyril
 * @since 0.1.0
 */
public class GetFirmwareFragmentRequestMessage implements ICommand {

	private final String posId;

	private final long offset;

	private final long length;
	
	@Override
	public String toString() {
		return "cmdId=" + getCmdId()+ ",posId=" + posId
				+ ", offset=" + offset + ", length=" + length;
	}

	public GetFirmwareFragmentRequestMessage(String posId, long offset,
			long length) {
		this.posId = posId;
		this.offset = offset;
		this.length = length;
	}

	/**
	 * Current implementation always return
	 * {@link CmdConstant#GET_FIRMWARE_FRAGMENT_CMD_ID}
	 */
	public long getCmdId() {
		return CmdConstant.GET_FIRMWARE_FRAGMENT_CMD_ID;
	}

	/**
	 * Returns the POS ID.
	 * 
	 * @return the POS ID.
	 */
	public String getPosId() {
		return posId;
	}

	/**
	 * @return the offset
	 */
	public long getOffset() {
		return offset;
	}

	/**
	 * @return the length
	 */
	public long getLength() {
		return length;
	}
	
}
