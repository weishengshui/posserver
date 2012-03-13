package com.chinarewards.qq.meishi.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * description：QQ美食，兑换Q米交易请求VO
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-2   下午04:03:20
 * @author Seek
 */
public class QQMeishiConvertQQMiReqVO implements Serializable {

	private static final long serialVersionUID = -8347534846162331211L;
	
	private String verifyCode;
	
	private String posid;
	
	private double  consume;
	
	private String password;
	
	public QQMeishiConvertQQMiReqVO(){
		
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("verifyCode", this.verifyCode)
				.append("posid", this.posid)
				.append("consume", this.consume)
				.append("password", this.password)
				.toString();
	}
	
	public String getVerifyCode() {
		return verifyCode;
	}

	/**
	 * token码
	 * QQ会员的唯一标识
	 */
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	
	public String getPosid() {
		return posid;
	}
	
	/**
	 * POS-ID
	 */
	public void setPosid(String posid) {
		this.posid = posid;
	}
	
	public double getConsume() {
		return consume;
	}
	
	/**
	 * 消费金额
	 * 最大和最小值应为Java的限制. 精确到小数点后2位 
	 */
	public void setConsume(double consume) {
		this.consume = consume;
	}
	
	public String getPassword() {
		return password;
	}
	
	/**
	 * 设置一个密码.
	 * 商家输入的交易密码.
	 * 内容一般是数字, 但请勿在程序中假定有这个限制.
	 * 如果POS不显示"请输入交易密码"界面的情况,只能为空. 
	 * 如果POS机显示了"请输入交易密码"界面,无论pos机操作者有没有输入值,都不能为空.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
}
