package com.chinarewards.qqgbvpn.main.protocol.socket.message;

/**
 * login request message body
 * 
 * @author huangwei
 *
 */
public class LoginResponseMessage implements IBodyMessage {

	private int cmdId;

	private long serial;
	
	private int result;
	
	private byte[] challeuge; 

	
	public int getCmdId() {
		return cmdId;
	}

	
	public long getSerial() {
		return serial;
	}

	public void setCmdId(int cmdId) {
		this.cmdId = cmdId;

	}

	
	public void setSerial(long serial) {
		this.serial = serial;

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
