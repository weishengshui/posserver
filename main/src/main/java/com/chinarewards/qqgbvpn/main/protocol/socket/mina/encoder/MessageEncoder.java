package com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
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

public class MessageEncoder implements ProtocolEncoder {

	private Logger log = LoggerFactory.getLogger(getClass());

	private Charset charset;

	private Injector injector;

	public MessageEncoder(Charset charset, Injector injector) {
		this.charset = charset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.mina.filter.codec.ProtocolEncoder#dispose(org.apache.mina.
	 * core.session.IoSession)
	 */
	@Override
	public void dispose(IoSession session) throws Exception {
		// TODO Auto-generated method stub

		System.out.println("dispose()");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.mina.filter.codec.ProtocolEncoder#encode(org.apache.mina.core
	 * .session.IoSession, java.lang.Object,
	 * org.apache.mina.filter.codec.ProtocolEncoderOutput)
	 */
	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		log.debug("encode message start");
		
		log.debug("message========:"+message.getClass());
		Message msg = (Message) message;
		HeadMessage headMessage = msg.getHeadMessage();
		IBodyMessage bodyMessage = msg.getBodyMessage();

		long cmdId = bodyMessage.getCmdId();
		byte[] bodyByte = null;
		log.debug("cmdId is ()", cmdId);
		
		String cmdName = injector.getInstance(CmdProperties.class).getCmdNameById(cmdId);
		if(cmdName == null || cmdName.length() == 0){
			throw new PackgeException("cmd id is not exits,cmdId is :"+cmdId);
		}
		//Dispatcher
		IBodyMessageCoder bodyMessageCoder = injector.getInstance(Key.get(IBodyMessageCoder.class, Names.named(cmdName)));
		
		bodyByte = bodyMessageCoder.encode(bodyMessage, charset);

		byte[] headByte = new byte[ProtocolLengths.HEAD];

		Tools.putUnsignedInt(headByte, headMessage.getSeq(), 0);
		Tools.putUnsignedInt(headByte, headMessage.getAck(), 4);
		Tools.putUnsignedShort(headByte, headMessage.getFlags(), 8);
		// TODO set checksum
		Tools.putUnsignedShort(headByte, headMessage.getChecksum(), 10);
		Tools.putUnsignedInt(headByte, ProtocolLengths.HEAD + bodyByte.length,
				12);

		byte[] result = new byte[ProtocolLengths.HEAD + bodyByte.length];

		Tools.putBytes(result, headByte, 0);
		Tools.putBytes(result, bodyByte, ProtocolLengths.HEAD);
		out.write(result);
		log.debug("encode message end");
	}

}
