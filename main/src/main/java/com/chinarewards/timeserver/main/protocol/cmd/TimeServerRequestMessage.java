package com.chinarewards.timeserver.main.protocol.cmd;

import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;

public class TimeServerRequestMessage implements ICommand {
	
	public static final long TIME_SERVER_CMD_ID=110L;
	private long cmdId;

	@Override
	public long getCmdId() {
		return cmdId;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	}
	
	

}
