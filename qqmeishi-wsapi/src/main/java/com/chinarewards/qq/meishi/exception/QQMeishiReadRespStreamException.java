package com.chinarewards.qq.meishi.exception;

import com.chinarewards.qq.meishi.exception.base.IHttpStatusCode;
import com.chinarewards.qq.meishi.exception.base.IRawContent;

/**
 * description：读取网络流异常
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-2   下午05:40:33
 * @author Seek
 */
public class QQMeishiReadRespStreamException extends Exception implements
		IRawContent, IHttpStatusCode {
	
	private static final long serialVersionUID = 7126868581450582850L;
	
	private String rawContent;
	
	private int httpStatusCode;
	
	public QQMeishiReadRespStreamException() {
		super();
	}

	public QQMeishiReadRespStreamException(String message, Throwable cause) {
		super(message, cause);
	}

	public QQMeishiReadRespStreamException(String message) {
		super(message);
	}

	public QQMeishiReadRespStreamException(Throwable cause) {
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
