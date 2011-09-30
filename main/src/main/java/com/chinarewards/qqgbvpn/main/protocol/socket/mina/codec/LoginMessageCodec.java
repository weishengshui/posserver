package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.LoginRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.LoginResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

/**
 * login coder/bind coder.
 * 
 * @author huangwei
 * 
 */
public class LoginMessageCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		log.debug("login/bind message decode");
		LoginRequestMessage message = new LoginRequestMessage();
		if (in.remaining() < ProtocolLengths.COMMAND + ProtocolLengths.POS_ID
				+ ProtocolLengths.CHALLENGE_RESPONSE) {
			throw new PackageException(
					"login packge message body error, body message is :" + in);
		}
		
		// read from buffer
		long cmdId = in.getUnsignedInt();
		byte[] posIdBuf = new byte[ProtocolLengths.POS_ID];
		byte[] challeugeresponse = new byte[ProtocolLengths.CHALLENGE_RESPONSE];
		in.get(posIdBuf);
		in.get(challeugeresponse);
		
		// construct message
		message.setCmdId(cmdId);
		message.setChallengeResponse(challeugeresponse);
		message.setPosId(Tools.byteToString(posIdBuf, charset));
		
		log.debug("login/bind message request:cmdId is ({}) , posid is ({}) ,challengeResponse is ({})",new Object[]{cmdId,posIdBuf,challeugeresponse});
		return message;
	}

	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {
		log.debug("login/bind message encode");

		LoginResponseMessage responseMessage = (LoginResponseMessage) bodyMessage;
		long cmdId = responseMessage.getCmdId();
		int result = responseMessage.getResult();
		byte[] challeuge = responseMessage.getChallenge();

		byte[] resultByte = new byte[ProtocolLengths.COMMAND + ProtocolLengths.RESULT
				+ ProtocolLengths.CHALLENGE];

		Tools.putUnsignedInt(resultByte, cmdId, 0);
		Tools.putUnsignedShort(resultByte, result, ProtocolLengths.COMMAND);
		Tools.putBytes(resultByte, challeuge, ProtocolLengths.COMMAND + ProtocolLengths.RESULT);

		return resultByte;
	}

}
