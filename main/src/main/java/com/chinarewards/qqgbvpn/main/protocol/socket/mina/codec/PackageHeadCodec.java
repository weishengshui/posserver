package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.protocol.cmd.HeadMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

public class PackageHeadCodec {
	
	private Logger log = LoggerFactory.getLogger(getClass());
		
	/**
	 * description：encode package head message
	 * @param headMessage
	 * @return
	 * @time 2011-9-22   下午08:09:04
	 * @author Seek
	 */
	public byte[] encode(HeadMessage headMessage) {
		byte[] headByte = new byte[ProtocolLengths.HEAD];

		Tools.putUnsignedInt(headByte, headMessage.getSeq(), 0);
		Tools.putUnsignedInt(headByte, headMessage.getAck(), 4);
		Tools.putUnsignedShort(headByte, headMessage.getFlags(), 8);
		Tools.putUnsignedShort(headByte, 0, 10);	//checksum
		Tools.putUnsignedInt(headByte, headMessage.getMessageSize(),
				12);
		return headByte;
	}
	
	public HeadMessage decode(IoBuffer in){
		log.trace("Do processing");
		HeadMessage headMessage = new HeadMessage();
		// read header
		// read seq
		log.trace("Read message head");
		long seq = in.getUnsignedInt();
		headMessage.setSeq(seq);

		// read ack
		long ack = in.getUnsignedInt();
		headMessage.setAck(ack);

		// read flags
		int flags = in.getUnsignedShort();
		headMessage.setFlags(flags);
		log.trace("in.remaining() = " + in.remaining());

		// read checksum
		log.trace("read checksum");
		int checksum = in.getUnsignedShort();
		log.trace("checksum========:" + checksum);
		headMessage.setChecksum(checksum);
		log.trace("in.remaining() = " + in.remaining());

		// read message size
		log.trace("read message size");
		long messageSize = in.getUnsignedInt();
		headMessage.setMessageSize(messageSize);
		log.trace("in.remaining() = " + in.remaining());
		log.trace("headMessage ====: {}", headMessage.toString());
		
		return headMessage;
	}
	
}