/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.cmd.login;

/**
 * See the WIKI <a href=
 * "http://wiki.dev.jifen.cc/index.php/Pos%E6%9C%BA%E6%8A%A5%E6%96%87%E5%AE%9A%E4%B9%89"
 * >Pos机报文定义</a> for detail.
 * 
 * @author cream
 * @since 1.0.0 2011-08-26
 */
public class LoginResponse {
	// 流水号
	private long serial;

	// 交易结果
	private LoginResult result;

	// 随机安全码
	private byte[] challenge;

	public long getSerial() {
		return serial;
	}

	public void setSerial(long serial) {
		this.serial = serial;
	}

	public LoginResult getResult() {
		return result;
	}

	public void setResult(LoginResult result) {
		this.result = result;
	}

	public byte[] getChallenge() {
		return challenge;
	}

	public void setChallenge(byte[] challenge) {
		this.challenge = challenge;
	}

}
