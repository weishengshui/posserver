package com.chinarewards.qqgbvpn.main.protocol.cmd;



public class ValidateResponseMessage implements ICommand {

	private long cmdId;
	
	private int result;
	
	private String resultName;

	private String resultExplain;
	
	private String currentTime;
	
	private String useTime;
	
	private String validTime;
	
	@Override
	public String toString() {
		return " [cmdId=" + cmdId + ", result=" + result + ", resultName="
				+ resultName + ", resultExplain=" + resultExplain
				+ ", currentTime=" + currentTime + ", useTime=" + useTime
				+ ", validTime=" + validTime + "]";
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

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public String getUseTime() {
		return useTime;
	}

	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}

	public String getValidTime() {
		return validTime;
	}

	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}
	
}
