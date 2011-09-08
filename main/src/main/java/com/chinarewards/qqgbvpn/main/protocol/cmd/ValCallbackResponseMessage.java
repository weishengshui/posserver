package com.chinarewards.qqgbvpn.main.protocol.cmd;

/**
 * 
 * 
 * @author huangwei
 *
 */
public class ValCallbackResponseMessage implements ICommand {

	private long cmdId;
	
	private int result;

	//======================//
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
	

}
