package com.chinarewards.timeserver.main.protocol.cmd;

import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;

public class TimeServerResponseMessage implements ICommand {
	public static final long TIME_SERVER_CMD_ID_RESPONSE = 111L;
	private long cmdId;
	private long time;

	@Override
	public long getCmdId() {
		return cmdId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	}
	

}
