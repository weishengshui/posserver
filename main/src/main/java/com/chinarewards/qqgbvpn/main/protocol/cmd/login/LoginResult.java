/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.cmd.login;

/**
 * @author cream
 * 
 */
public enum LoginResult {
	SUCCESS(0), FAILED(1), OTHERS(2);

	private int posCode;

	LoginResult(int posCode) {
		this.posCode = posCode;
	}

	public int getPosCode() {
		return posCode;
	}
}
