package com.chinarewards.qqgbvpn.main.protocol.socket.message;

/**
 * init request message
 * 
 * @author huangwei
 *
 */
public class InitRequestMessage implements IBodyMessage {

	private int cmdId;
	
	private String posid;
	
	public int getCmdId() {
		return cmdId;
	}

	public String getPosid() {
		return posid;
	}

	public void setPosid(String posid) {
		this.posid = posid;
	}

	public void setCmdId(int cmdId) {
		this.cmdId = cmdId;
	}

}
