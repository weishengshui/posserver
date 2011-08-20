package com.chinarewards.qqgbvpn.qqapi.vo;

public class TuanUnbindVO {

	/**
	 * POS机编号
	 */
	private String posId;
	
	/**
	 * 取消状态，非0代表成功
	 */
	private String resultStatus;

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public String getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}
	
	
}
