/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.cmd.login;

/**
 * @author cream
 * 
 */
public enum LoginResult {
	SUCCESS(0), VALIDATE_FAILED(1), POSID_NOT_EXIST(2), OTHERS(3);

	private int posCode;

	LoginResult(int posCode) {
		this.posCode = posCode;
	}

	public int getPosCode() {
		return posCode;
	}
}
