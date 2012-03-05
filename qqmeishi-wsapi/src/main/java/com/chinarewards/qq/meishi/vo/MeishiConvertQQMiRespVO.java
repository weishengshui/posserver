package com.chinarewards.qq.meishi.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * description：QQ美食，兑换Q米交易响应VO
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-2   下午04:15:32
 * @author Seek
 */
public class MeishiConvertQQMiRespVO implements Serializable {

	private static final long serialVersionUID = 1626905771653304845L;

	private int validCode;
	
	private Boolean hasPassword;
	
	private String tradeTime;
	
	private String title;
	
	private String tip;
	
	private String password;
	
	public MeishiConvertQQMiRespVO(){
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MeishiConvertQQMiRespVO))
			return false;
		MeishiConvertQQMiRespVO respVO = (MeishiConvertQQMiRespVO) obj;
		return new EqualsBuilder().append(this.validCode, respVO.validCode)
				.append(this.hasPassword, respVO.hasPassword)
				.append(this.tradeTime, respVO.tradeTime)
				.append(this.title, respVO.title).append(this.tip, respVO.tip)
				.append(this.password, respVO.password).isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.validCode)
				.append(this.hasPassword).append(this.tradeTime)
				.append(this.title).append(this.tip).append(this.password)
				.toHashCode();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("validCode", this.validCode)
				.append("hasPassword", this.hasPassword)
				.append("tradeTime", this.tradeTime)
				.append("title", this.title)
				.append("tip", this.tip)
				.append("password", this.password)
				.toString();
	}
	
	/**
	 * 验证结果
	 * 0:成功 1: 商家密码错误 2: token码错误 3: 非法用户 4: 金额错误 5: 请输入密码 
	 * URL:
	 * http://wiki.dev.jifen.cc/QQ%E7%BE%8E%E9%A3%9FPOS%E6%9C%BA%E5%A5%96%E5%8A%B1Q%E7%B1%B3
	 */
	public int getValidCode() {
		return validCode;
	}

	public void setValidCode(int validCode) {
		this.validCode = validCode;
	}
	
	/**
	 * 商家下一次是否需要输入交易密码
	 * true代表下一次累积Q米时强制POS客户端需要输入交易密码。 
	 */
	public Boolean getHasPassword() {
		return hasPassword;
	}

	public void setHasPassword(Boolean hasPassword) {
		this.hasPassword = hasPassword;
	}
	
	/**
	 * 交易时间
	 * 暂时只支持 YYYYMMDDThhmmss±hhmm. 
	 * 例 19850412T231530+0400 指1985年4月12日下午11时15分30秒，时区为+4小时0分 
	 */
	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}
	
	/**
	 * 小票上的标题
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * 小票上的打印内容
	 */
	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}
	
	/**
	 * 返回的交易密码
	 * 仅validCode =1时返回交易密码,其他情况为空. 如果不为空(NULL或空字符串)，
	 * 则POS客户端下一次要检查用户输入的交易密码是否正确时，应要检查这个值是否匹配，
	 * 才发请求到QQ美食服务器
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
