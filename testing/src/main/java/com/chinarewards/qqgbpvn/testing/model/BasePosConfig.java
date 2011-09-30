package com.chinarewards.qqgbpvn.testing.model;

import java.net.Socket;

import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;

public final class BasePosConfig {
	
	private Long number;
	
	private String posId;

	/**
	 * 流水号
	 */
	private Integer sequence = 1;
	
	private String secret;	//pos UI  secret key
	
	private ICommand lastResponseBodyMessage;	//last response Message
	
	private String grouponId;	//为了解决选择一次， 重复验证的问题。
	
	private Socket socket;
	
	private long firmwareSize;		//文件大小
	
	private String firmwareName;	//文件名
	
	private long firmwareOffset = 0;	//文件偏移量
	
	@Override
	public String toString() {
		return "BasePosConfig [number=" + number + ", posId=" + posId
				+ ", sequence=" + sequence + ", secret=" + secret
				+ ", lastResponseBodyMessage=" + lastResponseBodyMessage
				+ ", grouponId=" + grouponId + ", socket=" + socket
				+ ", firmwareSize=" + firmwareSize + ", firmwareName="
				+ firmwareName + ", firmwareOffset=" + firmwareOffset + "]";
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}
	
	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	
	public ICommand getLastResponseBodyMessage() {
		return lastResponseBodyMessage;
	}

	public void setLastResponseBodyMessage(ICommand lastResponseBodyMessage) {
		this.lastResponseBodyMessage = lastResponseBodyMessage;
	}
	
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	public String getGrouponId() {
		return grouponId;
	}

	public void setGrouponId(String grouponId) {
		this.grouponId = grouponId;
	}
	
	public long getFirmwareSize() {
		return firmwareSize;
	}

	public void setFirmwareSize(long firmwareSize) {
		this.firmwareSize = firmwareSize;
	}

	public String getFirmwareName() {
		return firmwareName;
	}

	public void setFirmwareName(String firmwareName) {
		this.firmwareName = firmwareName;
	}

	public long getFirmwareOffset() {
		return firmwareOffset;
	}

	public void setFirmwareOffset(long firmwareOffset) {
		this.firmwareOffset = firmwareOffset;
	}
	
}
