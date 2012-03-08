package com.chinarewards.qq.meishi.conn.vo;

import java.io.Serializable;

import com.chinarewards.qq.meishi.conn.vo.base.IHttpStatusCode;
import com.chinarewards.qq.meishi.conn.vo.base.IRawContent;

/**
 * description：QQ美食兑换QQ米连线响应VO
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-8   上午10:35:13
 * @author Seek
 */
public class QQMeishiConnRespVO implements Serializable, IHttpStatusCode,
		IRawContent {

	private static final long serialVersionUID = -6798314944134892673L;
	
	private String rawContent;
	
	private int httpStatusCode;
	
	@Override
	public String getRawContent() {
		return rawContent;
	}
	
	@Override
	public void setRawContent(String rawContent) {
		this.rawContent = rawContent;
	}
	
	@Override
	public int getHttpStatusCode() {
		return httpStatusCode;
	}
	
	@Override
	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

}
