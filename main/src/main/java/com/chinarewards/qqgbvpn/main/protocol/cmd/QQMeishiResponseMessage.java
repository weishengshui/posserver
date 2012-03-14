package com.chinarewards.qqgbvpn.main.protocol.cmd;

import java.util.Date;

/**
 * @author harry
 *
 */
public class QQMeishiResponseMessage implements ICommand{

	
	public static final long QQMEISHI_CMD_ID_RESPONSE = 102;
	//指令ID 102
	public long cmdId;
	
	//posnet server 异常
	public long serverErrorCode;
	
	//QQ没事服务器error code
	public long qqwsErrorCode;
	
	//0:成功 1: 商家密码错误 2: token码错误 3: 非法用户 4: 金额错误 5: 请输入密码
	public long result;
	
	//是否要提示输入密码，0=是, 1=不是, 2=维持上一次的值 
	public byte forcePwdNextAction;
	
	//交易时间
	public Date xactTime;
	
	//小票上的打印标题
	public String title;
	
	//小票上的打印内容
	public String tip;
	
	//商家输入的交易密码
	public String password;
	
	
	@Override
	public String toString() {
		return " [cmdId=" + cmdId + ", serverErrorCode=" + serverErrorCode
				+ ", qqwsErrorCode=" + qqwsErrorCode + ", result=" + result
				+ ", forcePwdNextAction=" + forcePwdNextAction + ", xactTime="
				+ xactTime + ", title=" + title + ", tip=" + tip
				+ ", password=" + password + "]";
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
	
	public byte getForcePwdNextAction() {
		return forcePwdNextAction;
	}

	public void setForcePwdNextAction(byte forcePwdNextAction) {
		this.forcePwdNextAction = forcePwdNextAction;
	}
	

	public Date getXactTime() {
		return xactTime;
	}

	public void setXactTime(Date xactTime) {
		this.xactTime = xactTime;
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

	public long getServerErrorCode() {
		return serverErrorCode;
	}

	public void setServerErrorCode(long serverErrorCode) {
		this.serverErrorCode = serverErrorCode;
	}

	public long getQqwsErrorCode() {
		return qqwsErrorCode;
	}

	public void setQqwsErrorCode(long qqwsErrorCode) {
		this.qqwsErrorCode = qqwsErrorCode;
	}



}
