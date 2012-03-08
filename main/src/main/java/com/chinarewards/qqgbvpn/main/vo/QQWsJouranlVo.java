package com.chinarewards.qqgbvpn.main.vo;

import com.chinarewards.qq.meishi.vo.QQMeishiConvertQQMiRespVO;
import com.chinarewards.qq.meishi.vo.common.QQMeishiResp;

public class QQWsJouranlVo {

	//http请求状态码
	int httpStatusCode;
	
	//请求的原始内容（这个只有在请求不正常情况下才会有，正常情况下内容和content一样的）
	String rawContent;
	
	//请求的有效数据
	QQMeishiResp<QQMeishiConvertQQMiRespVO> content;
	
	//异常情况下的堆栈信息
	String stacktrace;

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public String getRawContent() {
		return rawContent;
	}

	public void setRawContent(String rawContent) {
		this.rawContent = rawContent;
	}

	public QQMeishiResp<QQMeishiConvertQQMiRespVO> getContent() {
		return content;
	}

	public void setContent(QQMeishiResp<QQMeishiConvertQQMiRespVO> content) {
		this.content = content;
	}

	public String getStacktrace() {
		return stacktrace;
	}

	public void setStacktrace(String stacktrace) {
		this.stacktrace = stacktrace;
	}

	
}
