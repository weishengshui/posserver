package com.chinarewards.qq.meishi.exception;

import com.chinarewards.qq.meishi.exception.base.IHttpStatusCode;
import com.chinarewards.qq.meishi.exception.base.IRawContent;

/**
 * description：QQ美食服务器响应异常
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-7   上午11:05:36
 * @author Seek
 */
public class QQMeishiServerRespException extends Exception implements
		IRawContent, IHttpStatusCode {

	private static final long serialVersionUID = -5954520940582970450L;
	
	private String rawContent;
	
	private int httpStatusCode;
	
	public QQMeishiServerRespException() {
		super();
	}

	public QQMeishiServerRespException(String message, Throwable cause) {
		super(message, cause);
	}

	public QQMeishiServerRespException(String message) {
		super(message);
	}

	public QQMeishiServerRespException(Throwable cause) {
		super(cause);
	}
	
	@Override
	public String toString() {
		return "{\"rawContent\":"+rawContent+",\"httpStatusCode\":"+httpStatusCode+"}";
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
