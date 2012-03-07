package com.chinarewards.qq.meishi.exception;

import com.chinarewards.qq.meishi.exception.base.IHttpStatusCode;
import com.chinarewards.qq.meishi.exception.base.IRawContent;

/**
 * description：QQ美食响应数据解析异常
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-2   下午04:56:04
 * @author Seek
 */
public class QQMeishiRespDataParseException extends Exception implements
		IRawContent, IHttpStatusCode {
	
	private static final long serialVersionUID = -7541114094086452957L;
	
	private String rawContent;
	
	private int httpStatusCode;
	
	public QQMeishiRespDataParseException() {
		super();
	}

	public QQMeishiRespDataParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public QQMeishiRespDataParseException(String message) {
		super(message);
	}

	public QQMeishiRespDataParseException(Throwable cause) {
		super(cause);
	}

	@Override
	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	@Override
	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	@Override
	public String getRawContent() {
		return rawContent;
	}

	@Override
	public void setRawContent(String rawContent) {
		this.rawContent = rawContent;
	}
	
}
