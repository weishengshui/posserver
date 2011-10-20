package com.chinarewards.qqgbvpn.main.protocol.cmd;

import java.util.Arrays;


/**
 * description：pos response echo command
 * @copyright binfen.cc
 * @projectName main
 * @time 2011-10-20   下午06:02:22
 * @author Seek
 */
public class PosEchoCommandResponseMessage implements ICommand {
	
	public static final short RESULT_OK = 0;

	/**
	 * request data to long
	 */
	public static final short RESULT_DATA_TOO_LONG = 1;

	/**
	 * pos server other error
	 */
	public static final short RESULT_OTHER_ERROR = 2;
	
	
	private short result;
	
	private byte[] data;
	
	@Override
	public String toString() {
		return "cmdId=" + getCmdId() + ", result="
				+ result + ", data=" + Arrays.toString(data);
	}

	//-------------------------------------------------//
	public long getCmdId() {
		return CmdConstant.ECHO_CMD_ID_RESPONSE;
	}
	
	public short getResult() {
		return result;
	}

	public void setResult(short result) {
		this.result = result;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
}
