package com.chinarewards.qqgbvpn.main.protocol.socket.message;

/**
 * init request message
 * 
 * @author huangwei
 *
 */
public class InitRequestMessage implements IBodyMessage {

	private long cmdId;
	
	private String posid;
	
	public long getCmdId() {
		return cmdId;
	}

	public String getPosid() {
		return posid;
	}

	public void setPosid(String posid) {
		this.posid = posid;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	}

}
