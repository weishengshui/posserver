package com.chinarewards.qqgbvpn.main.protocol.socket.message;

import java.util.Date;

public class ValidateResponseMessage implements IBodyMessage {

	private long cmdId;
	
	private int result;
	
	private String resultName;

	private String resultExplain;
	
	private Date currentTime;
	
	private Date useTime;
	
	private Date validTime;

	
	
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

	public String getResultName() {
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public String getResultExplain() {
		return resultExplain;
	}

	public void setResultExplain(String resultExplain) {
		this.resultExplain = resultExplain;
	}

	public Date getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Date currentTime) {
		this.currentTime = currentTime;
	}

	public Date getUseTime() {
		return useTime;
	}

	public void setUseTime(Date useTime) {
		this.useTime = useTime;
	}

	public Date getValidTime() {
		return validTime;
	}

	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}
	

	
	
}
