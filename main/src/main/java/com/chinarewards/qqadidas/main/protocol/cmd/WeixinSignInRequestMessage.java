package com.chinarewards.qqadidas.main.protocol.cmd;

import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;

/**
 * 
 * @author weishengshui
 * 
 */
public class WeixinSignInRequestMessage implements ICommand {
	public static final long WEIXIN_SIGN_IN_CMD_ID = 205;
	private long cmdId;
	// POSNET字符串
	private int weixinNoLength;
	private String weixinNo;

	@Override
	public long getCmdId() {
		return cmdId;
	}

	public int getWeixinNoLength() {
		return weixinNoLength;
	}

	public void setWeixinNoLength(int weixinNoLength) {
		this.weixinNoLength = weixinNoLength;
	}

	public String getWeixinNo() {
		return weixinNo;
	}

	public void setWeixinNo(String weixinNo) {
		this.weixinNo = weixinNo;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	}

}
