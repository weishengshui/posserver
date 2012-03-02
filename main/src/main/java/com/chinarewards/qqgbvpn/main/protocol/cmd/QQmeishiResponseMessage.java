package com.chinarewards.qqgbvpn.main.protocol.cmd;

import java.util.Date;

/**
 * @author harry
 *
 */
public class QQmeishiResponseMessage implements ICommand{

	//指令ID 102
	public long cmdId;
	
	//0:成功 1: 商家密码错误 2: token码错误 3: 非法用户 4: 金额错误 5: 请输入密码
	public long result;
	
	//是否要提示输入密码，0=是, 1=不是, 2=维持上一次的值 
	public byte force_pwd_next_action;
	
	//交易时间
	public Date xact_time;
	
	//小票上的打印标题
	public String title;
	
	//小票上的打印内容
	public String tip;
	
	//商家输入的交易密码
	public String password;
	
	
	@Override
	public String toString() {
		return " [cmdId=" + cmdId + ", result=" + result
				+ ", force_pwd_next_action=" + force_pwd_next_action
				+ ", xact_time=" + xact_time + ", title=" + title
				+ ", tip=" + tip + ", password=" + password + "]";
	}

	//--------------------------------//
	@Override
	public long getCmdId() {
		return cmdId;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	}

	public long getResult() {
		return result;
	}

	public void setResult(long result) {
		this.result = result;
	}

	public byte getForce_pwd_next_action() {
		return force_pwd_next_action;
	}

	public void setForce_pwd_next_action(byte force_pwd_next_action) {
		this.force_pwd_next_action = force_pwd_next_action;
	}

	public Date getXact_time() {
		return xact_time;
	}

	public void setXact_time(Date xact_time) {
		this.xact_time = xact_time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}



}
