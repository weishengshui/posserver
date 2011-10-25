package com.chinarewards.qqgbvpn.main.protocol.cmd;


public class HuaxiaRequestMessage implements ICommand {

	public long cmdId;
	
	public String cardNum;
	
	public String chanceId;
	
	public String ackId;
	
	@Override
	public String toString() {
		return " [cmdId=" + cmdId + ", cardNum=" + cardNum + ", chanceId="
				+ chanceId + ", ackId=" + ackId + "]";
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

	public String getChanceId() {
		return chanceId;
	}

	public void setChanceId(String chanceId) {
		this.chanceId = chanceId;
	}

	public String getAckId() {
		return ackId;
	}

	public void setAckId(String ackId) {
		this.ackId = ackId;
	} 
	
	

}
