package com.chinarewards.qqgbvpn.main.protocol.cmd;

import com.chinarewards.qqgbvpn.main.protocol.PosnetString;

/**
 * @author harry
 *
 */
public class QQmeishiRequestMessage implements ICommand{

	//指令ID 101
	public long cmdId;
	
	//Token码
	public PosnetString userToken;
	
	//消费金额
	public double amount;
	
	//商家输入的交易密码
	public PosnetString password;
	
	@Override
	public String toString() {
		return " [cmdId=" + cmdId + ", userToken=" + userToken
				+ ", amount=" + amount + ", password=" + password + "]";
	}

	//--------------------------------//
	@Override
	public long getCmdId() {
		return cmdId;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	}

	public PosnetString getUserToken() {
		return userToken;
	}

	public void setUserToken(PosnetString userToken) {
		this.userToken = userToken;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public PosnetString getPassword() {
		return password;
	}

	public void setPassword(PosnetString password) {
		this.password = password;
	} 

}
