package com.chinarewards.qqadidas.main.protocol.cmd;

import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;

public class PrivilegeExchangeRequestMessage implements ICommand {
	public static final long RIGHT_FOR_CMD_ID = 203;

	private long cmdId;
	// 验证码
	private int cdkeyLength;
	private String cdkey;
	// 消费金额
	private int amountLength;
	private String amount;

	@Override
	public long getCmdId() {
		return this.cmdId;
	}

	public int getCdkeyLength() {
		return cdkeyLength;
	}

	public void setCdkeyLength(int cdkeyLength) {
		this.cdkeyLength = cdkeyLength;
	}

	public String getCdkey() {
		return cdkey;
	}

	public void setCdkey(String cdkey) {
		this.cdkey = cdkey;
	}

	public int getAmountLength() {
		return amountLength;
	}

	public void setAmountLength(int amountLength) {
		this.amountLength = amountLength;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	}

}
