package com.chinarewards.qq.meishi.vo.common;

import com.chinarewards.qq.meishi.vo.base.IQQMeishiRespBase;

/**
 * description：QQ meishi response commmon head
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-7   上午10:03:59
 * @author Seek
 */
public class QQMeishiResp <T extends IQQMeishiRespBase> {
	
	private int errCode;
	
	private String errMessage;
	
	private T result;
	
	public QQMeishiResp() {
	}
	
	public QQMeishiResp(T meishiResp){
		this.result = meishiResp;
	}
	
	//TODO toString, equals, hashcode
	
	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}
	
}
