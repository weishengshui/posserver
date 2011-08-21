package com.chinarewards.qqgbvpn.qqapi.vo;

public class GroupBuyingValidateResultVO {

	/**
	 * 验证结果名称-小票上显示
	 */
	private String resultName;
	
	/**
	 * 验证结果标题-屏幕上显示，包含换行符号
	 */
	private String resultExplain;
	
	/**
	 * 当前操作的时间，例如2011-08-10 20:10:00
	 */
	private String currentTime;
	
	/**
	 * 验证的时间，例如2011-08-10 20:10:00，如果为空则不打印这时间
	 */
	private String useTime;
	
	/**
	 * 验证码有效期，例如2011-08-10，如果为空则不打印这时间
	 */
	private String validTime;
	
	/**
	 * 退款的时间，例如2011-08-10 20:10:00，如果为空则不打印这时间
	 */
	private String refundTime;

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

	public String getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}

	
}
