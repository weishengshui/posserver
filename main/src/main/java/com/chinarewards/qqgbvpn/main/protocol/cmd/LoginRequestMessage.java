package com.chinarewards.qqgbvpn.main.protocol.cmd;

import java.util.Arrays;


/**
 * login request message body
 * 
 * @author huangwei
 * 
 */
public class LoginRequestMessage implements ICommand {

	/**
	 * 登录请求
	 */
	public static final long LOGIN_CMD_ID = 7;
	
	/**
	 * 绑定请求
	 */
	public static final long BIND_CMD_ID = 9;
	
	private long cmdId;

	private String posId;

	private byte[] challengeResponse;

	@Override
	public String toString() {
		return " [cmdId=" + cmdId + ", posId=" + posId + ", challengeResponse="
				+ Arrays.toString(challengeResponse) + "]";
	}

	public long getCmdId() {
		return cmdId;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;

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
