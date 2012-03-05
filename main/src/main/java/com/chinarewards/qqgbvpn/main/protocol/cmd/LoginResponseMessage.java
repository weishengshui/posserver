package com.chinarewards.qqgbvpn.main.protocol.cmd;

import java.util.Arrays;


/**
 * login request message body
 * 
 * @author huangwei
 *
 */
public class LoginResponseMessage implements ICommand {

	/**
	 * 登录回复
	 */
	public static final long LOGIN_CMD_ID_RESPONSE = 8;
	
	/**
	 * 绑定回复
	 */
	public static final long BIND_CMD_ID_RESPONSE = 10;
	
	private long cmdId;
	
	private int result;
	
	private byte[] challenge; 
	
	@Override
	public String toString() {
		return " [cmdId=" + cmdId + ", result=" + result + ", challenge="
				+ Arrays.toString(challenge) + "]";
	}


	public long getCmdId() {
		return cmdId;
	}


	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;

	}



	public int getResult() {
		return result;
	}


	public void setResult(int result) {
		this.result = result;
	}


	public byte[] getChallenge() {
		return challenge;
	}

	public void setChallenge(byte[] challeuge) {
		this.challenge = challeuge;
	}

}
