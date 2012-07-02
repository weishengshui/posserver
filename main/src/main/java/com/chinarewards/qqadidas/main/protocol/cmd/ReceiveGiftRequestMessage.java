package com.chinarewards.qqadidas.main.protocol.cmd;

import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;

/**
 * 
 * @author weishengshui
 * 
 */
public class ReceiveGiftRequestMessage implements ICommand {

	public static final long RECEIVE_GIFT_CMD_ID = 201;

	private long cmdId;

	// cdkey的内容长度
	private int cdkeyLength;
	private String cdkey;

	public long getCmdId() {
		return cmdId;
	}

	public String getCdkey() {
		return cdkey;
	}

	public void setCdkey(String cdkey) {
		this.cdkey = cdkey;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	}

	public int getCdkeyLength() {
		return cdkeyLength;
	}

	public void setCdkeyLength(int cdkeyLength) {
		this.cdkeyLength = cdkeyLength;
	}

}
