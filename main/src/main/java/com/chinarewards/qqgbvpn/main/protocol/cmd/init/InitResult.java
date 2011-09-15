/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.cmd.init;

/**
 * @author cream
 * 
 */
public enum InitResult {
	INIT(0), UNINIT(1), FIRMWARE_UPGRADE_REQUIRED(2), OTHERS(3);

	private int posCode;

	InitResult(int posCode) {
		this.posCode = posCode;
	}

	public int getPosCode() {
		return posCode;
	}
}
