package com.chinarewards.qqgbvpn.main.protocol.socket.message;

/**
 * init response message
 * 
 * @author huangwei
 *
 */
public class InitResponseMessage implements IBodyMessage {

	private int cmdId;
	
	private int result;
	
	private byte[] challeuge;

	
	//-------------------------------------------------//
	public int getCmdId() {
		return cmdId;
	}

	public void setCmdId(int cmdId) {
		this.cmdId = cmdId;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public byte[] getChalleuge() {
		return challeuge;
	}

	public void setChalleuge(byte[] challeuge) {
		this.challeuge = challeuge;
	} 
	

}
