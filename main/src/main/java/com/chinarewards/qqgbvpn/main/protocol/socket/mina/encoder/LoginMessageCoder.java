package com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackgeException;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.IBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.LoginRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.LoginResponseMessage;

/**
 * login coder/bind coder.
 * 
 * @author huangwei
 * 
 */
public class LoginMessageCoder implements IBodyMessageCoder {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public IBodyMessage decode(IoBuffer in, Charset charset)
			throws PackgeException {
		log.debug("login/bind message decode");
		LoginRequestMessage message = new LoginRequestMessage();
		if (in.remaining() != ProtocolLengths.COMMAND + ProtocolLengths.POS_ID
				+ ProtocolLengths.CHALLEUGERESPONSE) {
			throw new PackgeException(
					"login packge message body error, body message is :" + in);
		}
		long cmdId = in.getUnsignedInt();
		byte[] posid = new byte[ProtocolLengths.POS_ID];
		byte[] challeugeresponse = new byte[ProtocolLengths.CHALLEUGERESPONSE];
		in.get(posid);
		in.get(challeugeresponse);
		message.setCmdId(cmdId);
		message.setChallengeResponse(challeugeresponse);
		message.setPosid(new String(posid, charset));
		log.debug("login/bind message request:cmdId is ({}) , posid is ({}) ,challengeResponse is ({})",new Object[]{cmdId,posid,challeugeresponse});
		return message;
	}

	@Override
	public byte[] encode(IBodyMessage bodyMessage, Charset charset) {
		log.debug("login/bind message encode");

		LoginResponseMessage responseMessage = (LoginResponseMessage) bodyMessage;
		long cmdId = responseMessage.getCmdId();
		int result = responseMessage.getResult();
		byte[] challeuge = responseMessage.getChallenge();

		byte[] resultByte = new byte[ProtocolLengths.COMMAND + ProtocolLengths.RESULT
				+ ProtocolLengths.CHALLEUGE];

		Tools.putUnsignedInt(resultByte, cmdId, 0);
		Tools.putUnsignedShort(resultByte, result, ProtocolLengths.COMMAND);
		Tools.putBytes(resultByte, challeuge, ProtocolLengths.COMMAND + ProtocolLengths.RESULT);

		return resultByte;
	}

}
