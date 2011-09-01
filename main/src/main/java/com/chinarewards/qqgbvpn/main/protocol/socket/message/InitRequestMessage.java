package com.chinarewards.qqgbvpn.main.protocol.socket.message;

import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;

/**
 * init request message
 * 
 * @author huangwei
 *
 */
public class InitRequestMessage implements ICommand {

	private long cmdId;
	
	private String posid;
	
	public long getCmdId() {
		return cmdId;
	}

	public String getPosId() {
		return posid;
	}

	public void setPosid(String posid) {
		this.posid = posid;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	}

}
