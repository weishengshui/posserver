package com.chinarewards.qqgbvpn.main.protocol.cmd;

import com.chinarewards.qqgbvpn.common.Tools;

/**
 * head message
 * 
 * @author huangwei
 * @since 0.1.0
 */
public class HeadMessage {

	private long seq;

	private long ack;

	private int flags;

	private int checksum;

	private long messageSize;

	// -----------------------------------------------//
	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

	public long getAck() {
		return ack;
	}

	public void setAck(long ack) {
		this.ack = ack;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public int getChecksum() {
		return checksum;
	}

	public void setChecksum(int checksum) {
		this.checksum = checksum;
	}

	public long getMessageSize() {
		return messageSize;
	}

	public void setMessageSize(long messageSize) {
		this.messageSize = messageSize;
	}

	@Override
	public String toString() {
		StringBuffer sbr = new StringBuffer();
		sbr.append("seq:{0x").append(Long.toHexString(seq)).append("} ack:{0x")
				.append(Long.toHexString(ack)).append("} flags:{0x")
				.append(Integer.toHexString(flags)).append("} checksum:{0x")
				.append(Integer.toHexString(checksum))
				.append("} messageSize:{0x").append(Long.toHexString(messageSize)).append("}");
		return sbr.toString();
	}

}
