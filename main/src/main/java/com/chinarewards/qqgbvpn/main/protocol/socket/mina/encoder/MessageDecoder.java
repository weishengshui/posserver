package com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.config.CmdProperties;
import com.chinarewards.qqgbvpn.main.exception.PackgeException;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.HeadMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.IBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.Message;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

public class MessageDecoder extends CumulativeProtocolDecoder {

	private Logger log = LoggerFactory.getLogger(getClass());

	private Charset charset;
	
	private Injector injector;
	
	public MessageDecoder(Charset charset,Injector injector){
		this.charset = charset;
		this.injector = injector;
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
			log.debug("checksum========:"+checksum);
			headMessage.setChecksum(checksum);
			log.debug("in.remaining() = " + in.remaining());

			// read message size
			log.debug("read message size");
			long messageSize = in.getUnsignedInt(); 
			headMessage.setMessageSize(messageSize);
			log.debug("in.remaining() = " + in.remaining());
			log.debug("headMessage ====: {}",
					headMessage.toString());
			
			//check length
			if(messageSize != ProtocolLengths.HEAD + in.remaining()){
				throw new PackgeException("packge message error");
			}
			int position = in.position();

			in.position(0);
			byte[] byteTmp = new byte[in.remaining()];
			in.get(byteTmp);
			Tools.putUnsignedShort(byteTmp, 0, 10);
			log.debug("byteTmp==msg==:"+Arrays.toString(byteTmp));
			int checkSumTmp = Tools.checkSum(byteTmp, byteTmp.length);
			log.debug("checkSumTmp========:"+checkSumTmp);
			
			if(checkSumTmp != checksum){
				throw new PackgeException("packge message error:checksum error");
			}
			in.position(position);
			IBodyMessage bodyMessage = this.decodeMessageBody(in, charset);
			
			Message message = new Message(headMessage,bodyMessage);
			out.write(message);
			return true;

		} else {
			// not yet done
			return false;
		}

	}
	
	private IBodyMessage decodeMessageBody(IoBuffer in,Charset charset) throws PackgeException{
		//get cmdId and process it
		int position = in.position();
		long cmdId = in.getUnsignedInt();
		log.debug("position========:"+position);
		in.position(position);
		log.debug("cmdId========:"+cmdId);
		log.debug("injector========:"+(injector==null));
		injector.getInstance(CmdProperties.class);
		String cmdName = injector.getInstance(CmdProperties.class).getCmdNameById(cmdId);
		log.debug("cmdName========:"+cmdName);
		if(cmdName == null || cmdName.length() == 0){
			throw new PackgeException("cmd id is not exits,cmdId is :"+cmdId);
		}
		//Dispatcher
		IBodyMessageCoder bodyMessageCoder = injector.getInstance(Key.get(IBodyMessageCoder.class, Names.named(cmdName)));
		
		return bodyMessageCoder.decode(in, charset);
	}

}