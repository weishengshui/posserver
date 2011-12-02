package com.chinarewards.qqgbvpn.main.protocol.cmd;

import java.util.Arrays;


/**
 * init response message
 * 
 * @author huangwei
 *
 */
public class InitResponseMessage implements ICommand {

	private long cmdId;
	
	private int result;
	
	private byte[] challenge;
	
	@Override
	public String toString() {
		return " [cmdId=" + cmdId + ", result=" + result + ", challenge="
				+ Arrays.toString(challenge) + "]";
	}

	//-------------------------------------------------//
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
