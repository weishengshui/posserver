package com.chinarewards.qq.meishi.vo.common;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

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
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof QQMeishiResp))
			return false;
		@SuppressWarnings("unchecked")
		QQMeishiResp<T> respVO = (QQMeishiResp<T>) obj;
		return new EqualsBuilder().append(this.errCode, respVO.errCode)
				.append(this.errMessage, respVO.errMessage)
				.append(this.result, respVO.result).isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.errCode)
				.append(this.errMessage).append(this.result)
				.toHashCode();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("errCode", this.errCode)
				.append("errMessage", this.errMessage)
				.append("result", this.result).toString();
	}
	
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
