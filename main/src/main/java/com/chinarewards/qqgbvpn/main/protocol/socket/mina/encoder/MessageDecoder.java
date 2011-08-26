package com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.HeadMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.IBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.LoginMessageCoder;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.Message;

public class MessageDecoder extends CumulativeProtocolDecoder {

	private Logger log = LoggerFactory.getLogger(getClass());

	private Charset charset;
	
	public MessageDecoder(Charset charset){
		this.charset = charset;
	}
	
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {

		log.debug("MessageDecoder.doDecode invoked");
		log.debug("in.remaining is {}",in.remaining());

		//check length, it must greater than head length
		if (in.remaining() > ProtocolLengths.HEAD) {

			log.debug("Do processing");
			HeadMessage headMessage = new HeadMessage();
			// read header
			// read seq
			log.debug("Read head");
			long seq = in.getUnsignedInt();
			headMessage.setSeq(seq);
			log.debug("in.remaining() = " + in.remaining());

			// read ack
			log.debug("read ack");
			long ack = in.getUnsignedInt(); 
			headMessage.setAck(ack);
			log.debug("in.remaining() = " + in.remaining());

			// read flags
			log.debug("read flags");
			int flags = in.getUnsignedShort(); 
			headMessage.setFlags(flags);
			log.debug("in.remaining() = " + in.remaining());

			// read checksum   
			log.debug("read checksum");
			int checksum = in.getUnsignedShort();
			headMessage.setChecksum(checksum);
			log.debug("in.remaining() = " + in.remaining());

			// read message size
			log.debug("read message size");
			long messageSize = in.getUnsignedInt(); 
			headMessage.setMessageSize(messageSize);
			log.debug("in.remaining() = " + in.remaining());
			log.debug("headMessage : {}",
					headMessage.toString());
			//TODO check message by head
			
			//TODO read body and decode to MessageObject
			IBodyMessage bodyMessage = this.decodeMessageBody(in, charset);
			
			Message message = new Message(headMessage,bodyMessage);
			out.write(message);
			return true;

		} else {
			// not yet done
			return false;
		}

	}
	
	private IBodyMessage decodeMessageBody(IoBuffer in,Charset charset){
		//TODO get cmdId and process it
		return new LoginMessageCoder().decode(in, charset);
	}

}