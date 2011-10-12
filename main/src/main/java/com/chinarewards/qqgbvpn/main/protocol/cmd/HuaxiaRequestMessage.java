package com.chinarewards.qqgbvpn.main.protocol.cmd;


public class HuaxiaRequestMessage implements ICommand {

	public long cmdId;
	
	public String cardNum;
	
	@Override
	public String toString() {
		return "HuaxiaRequestMessage [cmdId=" + cmdId + ", cardNum="
				+ cardNum + "]";
	}

	//--------------------------------//
	@Override
	public long getCmdId() {
		return cmdId;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	} 
	
	

}
