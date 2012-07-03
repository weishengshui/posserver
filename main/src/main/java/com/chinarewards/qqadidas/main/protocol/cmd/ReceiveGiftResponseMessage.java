package com.chinarewards.qqadidas.main.protocol.cmd;

import java.util.Calendar;

import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;

/**
 * 
 * @author weishengshui
 * 
 */
public class ReceiveGiftResponseMessage implements ICommand {

	public static final long RECEIVE_GIFT_CMD_ID_RESPONSE = 202;
	public static final String TITLE = "adidas NEO Label    打造你的新鲜范";
	public static final String TIP = "交易成功";

	/**
	 * response result code
	 */
	public static final int SUCCESS_CODE = 0;
	public static final int ALREADY_GET = 1;
	public static final int CDKEY_INVALID = 2;
	public static final int ERROR_CODE_OTHER = 3;

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
