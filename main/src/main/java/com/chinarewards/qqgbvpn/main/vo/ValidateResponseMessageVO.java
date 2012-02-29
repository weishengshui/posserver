package com.chinarewards.qqgbvpn.main.vo;

import java.util.Date;



public class ValidateResponseMessageVO {

	private long cmdId;
	
	private String resultName;

	private String resultExplain;
	
	private String currentTime;
	
	private String useTime;
	
	private String validTime;
	
	/**
	 * 下面四个字段是新的验证协议添加的
	 */
	private long qqws_resultcode;
	
	private long qqvalidate_resultstatus;
	
	private int validate_count;
	
	private String first_posId;
	
	private String prev_posId;
	
	private Date first_validate_time;
	
	private Date prev_validate_time;
	
	//---------------------------------------//
	public long getCmdId() {
		return cmdId;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
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
	
	public long getQqws_resultcode() {
		return qqws_resultcode;
	}

	public void setQqws_resultcode(long qqws_resultcode) {
		this.qqws_resultcode = qqws_resultcode;
	}

	public long getQqvalidate_resultstatus() {
		return qqvalidate_resultstatus;
	}

	public void setQqvalidate_resultstatus(long qqvalidate_resultstatus) {
		this.qqvalidate_resultstatus = qqvalidate_resultstatus;
	}

	public int getValidate_count() {
		return validate_count;
	}

	public void setValidate_count(int validate_count) {
		this.validate_count = validate_count;
	}

	public String getFirst_posId() {
		return first_posId;
	}

	public void setFirst_posId(String first_posId) {
		this.first_posId = first_posId;
	}

	public String getPrev_posId() {
		return prev_posId;
	}

	public void setPrev_posId(String prev_posId) {
		this.prev_posId = prev_posId;
	}

	public Date getFirst_validate_time() {
		return first_validate_time;
	}

	public void setFirst_validate_time(Date first_validate_time) {
		this.first_validate_time = first_validate_time;
	}

	public Date getPrev_validate_time() {
		return prev_validate_time;
	}

	public void setPrev_validate_time(Date prev_validate_time) {
		this.prev_validate_time = prev_validate_time;
	}


	
}
