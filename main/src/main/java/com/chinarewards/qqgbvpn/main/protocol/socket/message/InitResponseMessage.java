package com.chinarewards.qqgbvpn.main.protocol.socket.message;

/**
 * init response message
 * 
 * @author huangwei
 *
 */
public class InitResponseMessage implements IBodyMessage {

	private long cmdId;
	
	private int result;
	
	private byte[] challenge;

	
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