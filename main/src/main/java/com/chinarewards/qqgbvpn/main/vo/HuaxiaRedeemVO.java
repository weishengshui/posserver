package com.chinarewards.qqgbvpn.main.vo;

public class HuaxiaRedeemVO {
	
	/**
	 * 兑换结果：成功
	 */
	public static final int REDEEM_RESULT_SUCCESS = 0;
	
	/**
	 * 兑换结果：失败，没有可用兑换机会
	 */
	public static final int REDEEM_RESULT_NONE = 1;
	
	/**
	 * 兑换结果：失败，系统异常
	 */
	public static final int REDEEM_RESULT_FAIL = 2;
	
	/**
	 * 兑换结果：失败，兑换异常,pos or agent not found
	 */
	public static final int REDEEM_RESULT_FAIL_POS_NONE = 3;
	
	/**
	 * 兑换结果：失败，兑换保存数据库异常
	 */
	public static final int REDEEM_RESULT_FAIL_SAVE_EXCEPTION = 4;
	
	/**
	 * 兑换结果：Already acked (same POS) 
	 */
	public static final int REDEEM_RESULT_ALREADY_ACKED = 5;
	
	/**
	 * 限制兑换的最大次数
	 */
	public static final int REDEEM_MAXCOUNT = 65535;
	

	/**
	 * 可用次数
	 */
	private Integer redeemCount;
	
	/**
	 * 卡号
	 */
	private String cardNum;
	
	/**
	 * 兑换结果
	 */
	private Integer result;
	
	private String txDate;
	
	/**
	 * POS ID
	 */
	private String posId;
	
	private String chanceId;
	
	private String ackId;
	

	public String getChanceId() {
		return chanceId;
	}

	public void setChanceId(String chanceId) {
		this.chanceId = chanceId;
	}

	public String getAckId() {
		return ackId;
	}

	public void setAckId(String ackId) {
		this.ackId = ackId;
	}

	public String getTxDate() {
		return txDate;
	}

	public void setTxDate(String txDate) {
		this.txDate = txDate;
	}

	public Integer getRedeemCount() {
		return redeemCount;
	}

	public void setRedeemCount(Integer redeemCount) {
		this.redeemCount = redeemCount;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}
	
	
}
