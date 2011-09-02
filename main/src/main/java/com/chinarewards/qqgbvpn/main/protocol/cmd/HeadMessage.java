package com.chinarewards.qqgbvpn.main.protocol.cmd;

/**
 * head message
 * 
 * @author huangwei
 * 
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
		sbr.append("seq:{").append(seq).append("} ack:{").append(ack).append(
				"} flags:{").append(flags).append("} checksum:{").append(
				checksum).append("} messageSize:{").append(messageSize).append(
				"}");
		return sbr.toString();
	}

}
