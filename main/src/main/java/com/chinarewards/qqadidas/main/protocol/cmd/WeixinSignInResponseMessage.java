package com.chinarewards.qqadidas.main.protocol.cmd;

import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;

/**
 * 
 * @author weishengshui
 * 
 */
public class WeixinSignInResponseMessage implements ICommand {
	/**
	 * response result code
	 */
	public static final int RESULT_SUCCESS = 0;
	public static final int RESULT_ERROR = 1;
	public static final long WEIXIN_SIGN_IN_CMD_ID_RESPONSE = 206;
	private long cmdId;
	// 应答结果类型
	private int result;

	@Override
	public long getCmdId() {
		return cmdId;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	}

}
