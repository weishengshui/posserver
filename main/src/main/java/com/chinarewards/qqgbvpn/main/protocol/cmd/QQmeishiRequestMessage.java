package com.chinarewards.qqgbvpn.main.protocol.cmd;


/**
 * @author harry
 *
 */
public class QQmeishiRequestMessage implements ICommand{

	//指令ID 101
	public long cmdId;
	
	//Token码
	public String userToken;
	
	//消费金额
	public double amount;
	
	//商家输入的交易密码
	public String password;
	
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

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	} 

}
