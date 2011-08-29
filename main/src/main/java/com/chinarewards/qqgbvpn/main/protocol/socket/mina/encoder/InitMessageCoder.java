package com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackgeException;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.IBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.InitRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.InitResponseMessage;

/**
 * init message coder
 * 
 * @author huangwei
 *
 */
public class InitMessageCoder implements IBodyMessageCoder {
	
	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public IBodyMessage decode(IoBuffer in, Charset charset)
			throws PackgeException {
		log.debug("init message decode");
		InitRequestMessage message = new InitRequestMessage();
		if (in.remaining() != ProtocolLengths.COMMAND + ProtocolLengths.POS_ID) {
			throw new PackgeException(
					"login packge message body error, body message is :" + in);
		}
		int cmdId = in.getUnsignedShort();
		byte[] posid = new byte[ProtocolLengths.POS_ID];
		in.get(posid);
		message.setCmdId(cmdId);
		message.setPosid(new String(posid, charset));
		return message;
	}

	@Override
	public byte[] encode(IBodyMessage bodyMessage, Charset charset) {
		log.debug("init message encode");
		InitResponseMessage responseMessage = (InitResponseMessage) bodyMessage;
		int cmdId = responseMessage.getCmdId();
		int result = responseMessage.getResult();
		byte[] challeuge = responseMessage.getChalleuge();

		byte[] resultByte = new byte[ProtocolLengths.COMMAND+ProtocolLengths.RESULT+ProtocolLengths.CHALLEUGE];
		
		Tools.putUnsignedShort(resultByte, cmdId, 0);
		Tools.putUnsignedShort(resultByte, result, ProtocolLengths.COMMAND);
		Tools.putBytes(resultByte, challeuge, ProtocolLengths.COMMAND+ProtocolLengths.RESULT);
		
		return resultByte;
	}

}
