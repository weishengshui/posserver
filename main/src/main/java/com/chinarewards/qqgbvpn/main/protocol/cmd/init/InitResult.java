/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.cmd.init;

/**
 * @author cream
 * 
 */
public enum InitResult {
	INIT(0), UNINIT(1), OTHERS(2);

	private int posCode;

	InitResult(int posCode) {
		this.posCode = posCode;
	}

	public int getPosCode() {
		return posCode;
	}
}
