package com.chinarewards.qqgbvpn.main.protocol.cmd;



public class HuaxiaResponseMessage implements ICommand {

	private long cmdId;
	
	private int result;
	
	private String txDate;
	
	public String chanceId;
	
	public String ackId;
	
	public int redeemCount;

	@Override
	public String toString() {
		return "HuaxiaResponseMessage [cmdId=" + cmdId + ", result=" + result
				+ ", txDate=" + txDate + "]";
	}

	//---------------------------------------//
	public long getCmdId() {
		return cmdId;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getTxDate() {
		return txDate;
	}

	public void setTxDate(String txDate) {
		this.txDate = txDate;
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

	public int getRedeemCount() {
		return redeemCount;
	}

	public void setRedeemCount(int redeemCount) {
		this.redeemCount = redeemCount;
	}

	
}
