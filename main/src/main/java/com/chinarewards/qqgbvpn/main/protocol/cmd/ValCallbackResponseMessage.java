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

	@Override
	public String toString() {
		return "cmdId=" + cmdId + ", result="
				+ result;
	}

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
