package com.chinarewards.qqadidas.main.protocol.cmd;

import java.util.Calendar;

import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;

public class PrivilegeExchangeResponseMessage implements ICommand {
	public static final long RECEIVE_GIFT_CMD_ID_RESPONSE = 204;
	/**
	 * response result code
	 * 
	 */
	public static final int RESPONSE_RESULT_SUCCESS = 0;
	public static final int RESPONSE_RESULT_ALL_USE = 1;
	public static final int RESPONSE_RESULT_CDKEY_INVALID = 2;
	public static final int RESPONSE_RESULT_AMOUNT_INVALID = 3;
	public static final int RESPONSE_RESULT_ERROR_OTHERS = 4;
	public static final String TITLE = "adidas NEO Label    打造你的新鲜范";

	// cdkey错误
	public static final int CDKEY_NOT_EXISTS = -1;
	// 消费金额不足
	public static final int AMOUNT_INVALID = 0;
	// 会员之前已经返还100元
	public static final int PRIVILEGE_DONE = 1;
	// 会员之前未消费过，本次消费金额为300-599，返回50元优惠
	public static final int PRIVILEGE_FIRST_HALF = 2;
	// 会员之前已经返还过50元，本次消费金额为300及以上
	public static final int PRIVILEGE_SECOND_HALF = 3;
	// 会员之前未消费过，本次消费金额为600及以上，返回50元优惠
	public static final int PRIVILEGE_ALL = 4;
	// 其它错误
	public static final int ERROR_CODE_OTHERS = 5;
	private long cmdId;

	private int result;
	// 交易时间
	private Calendar xact_time;
	// 标题
	private int titleLength;
	private String title;
	// 屏幕内容
	private int tipLength;
	private String tip;

	public long getCmdId() {
		return this.cmdId;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getTitleLength() {
		return titleLength;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTitleLength(int titleLength) {
		this.titleLength = titleLength;
	}

	public int getTipLength() {
		return tipLength;
	}

	public void setTipLength(int tipLength) {
		this.tipLength = tipLength;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public Calendar getXact_time() {
		return xact_time;
	}

	public void setXact_time(Calendar xact_time) {
		this.xact_time = xact_time;
	}

}
