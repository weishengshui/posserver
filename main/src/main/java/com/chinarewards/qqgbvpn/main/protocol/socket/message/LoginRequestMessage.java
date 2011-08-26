package com.chinarewards.qqgbvpn.main.protocol.socket.message;

/**
 * login request message body
 * 
 * @author huangwei
 *
 */
public class LoginRequestMessage implements IBodyMessage {

	private int cmdId;

	private long serial;
	
	private String posid;
	
	private byte[] challeugeresponse; 

	
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

	public String getPosid() {
		return posid;
	}

	public void setPosid(String posid) {
		this.posid = posid;
	}

	public byte[] getChalleugeresponse() {
		return challeugeresponse;
	}

	public void setChalleugeresponse(byte[] challeugeresponse) {
		this.challeugeresponse = challeugeresponse;
	}
}
