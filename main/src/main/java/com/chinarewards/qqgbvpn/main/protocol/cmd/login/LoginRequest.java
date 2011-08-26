/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.cmd.login;

/**
 * 
 * 
 * @author cream
 * @since 1.0.0 2011-08-26
 */
public class LoginRequest {

	// POS 机流水号
	private long serial;
	// POS 机编号
	private String posId;
	// 加密结果值
	private byte[] challengeResponse;

	public long getSerial() {
		return serial;
	}

	public void setSerial(long serial) {
		this.serial = serial;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public byte[] getChallengeResponse() {
		return challengeResponse;
	}

	public void setChallengeResponse(byte[] challengeResponse) {
		this.challengeResponse = challengeResponse;
	}
}
