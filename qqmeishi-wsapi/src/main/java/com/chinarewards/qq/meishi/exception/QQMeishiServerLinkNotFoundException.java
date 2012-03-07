package com.chinarewards.qq.meishi.exception;

import com.chinarewards.qq.meishi.exception.base.IHttpStatusCode;
import com.chinarewards.qq.meishi.exception.base.IRawContent;

/**
 * description：QQ美食服务器连接不存在
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-7   上午10:59:33
 * @author Seek
 */
public class QQMeishiServerLinkNotFoundException extends Exception implements
		IRawContent, IHttpStatusCode {
	
	private static final long serialVersionUID = -2084636127626326797L;
	
	private String rawContent;
	
	private int httpStatusCode;

	public QQMeishiServerLinkNotFoundException() {
		super();
	}

	public QQMeishiServerLinkNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public QQMeishiServerLinkNotFoundException(String message) {
		super(message);
	}

	public QQMeishiServerLinkNotFoundException(Throwable cause) {
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
