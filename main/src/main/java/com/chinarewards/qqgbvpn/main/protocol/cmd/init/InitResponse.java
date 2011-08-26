/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.cmd.init;


/**
 * @author cream
 * 
 */
public class InitResponse {

	// POS 机流水号
	private long serial;

	// POS 机交易结果
	private InitResult result;

	// 随机安全码
	private byte[] challenge;

	public long getSerial() {
		return serial;
	}

	public void setSerial(long serial) {
		this.serial = serial;
	}

	public InitResult getResult() {
		return result;
	}

	public void setResult(InitResult result) {
		this.result = result;
	}

	public byte[] getChallenge() {
		return challenge;
	}

	public void setChallenge(byte[] challenge) {
		this.challenge = challenge;
	}

}
