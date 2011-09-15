/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.cmd.firmware;

/**
 * @author Seek
 * 
 */
public enum FirmwareUpDoneResult {
	SUCCESS(0), PROCESS_ERROR(1);

	private short posCode;

	FirmwareUpDoneResult(int posCode) {
		this.posCode = (short)posCode;
	}

	public short getPosCode() {
		return posCode;
	}
}
