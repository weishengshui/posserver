package com.chinarewards.qqgbvpn.main.protocol.cmd;

/**
 * 
 * 
 * @author huangwei
 *
 */
public class ValCallbackRequestMessage implements ICommand{

	public long cmdId;
	
	public String grouponId;
	
	public String grouponVCode;
	
	//--------------------------------//
	@Override
	public long getCmdId() {
		return cmdId;
	}

	public String getGrouponId() {
		return grouponId;
	}

	public void setGrouponId(String grouponId) {
		this.grouponId = grouponId;
	}

	public String getGrouponVCode() {
		return grouponVCode;
	}

	public void setGrouponVCode(String grouponVCode) {
		this.grouponVCode = grouponVCode;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	} 

}
