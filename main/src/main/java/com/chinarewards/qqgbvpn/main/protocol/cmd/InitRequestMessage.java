package com.chinarewards.qqgbvpn.main.protocol.cmd;

/**
 * init request message
 * 
 * @author huangwei
 * 
 */
public class InitRequestMessage implements ICommand {

	public static final long INIT_CMD_ID = 5;
	
	private long cmdId;

	private String posId;
	
	@Override
	public String toString() {
		return " [cmdId=" + cmdId + ", posId=" + posId + "]";
	}

	public long getCmdId() {
		return cmdId;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	}
}
