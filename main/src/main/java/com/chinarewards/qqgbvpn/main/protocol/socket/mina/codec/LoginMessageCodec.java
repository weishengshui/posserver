package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.LoginRequestMessage;
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
		
		log.trace("LoginRequestMessage:{}", message);
		return message;
	}

	/**
	 * description：mock pos test use it!
	 * @param bodyMessage
	 * @param charset
	 * @return
	 * @time 2011-9-22   下午07:23:35
	 * @author Seek
	 */
	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {
		log.debug("login/bind message encode");

		LoginRequestMessage requestMessage = (LoginRequestMessage) bodyMessage;
		long cmdId = requestMessage.getCmdId();
		String posId = requestMessage.getPosId();
		byte[] challengeResponse = requestMessage.getChallengeResponse();

		byte[] resultByte = new byte[ProtocolLengths.COMMAND + ProtocolLengths.POS_ID
		             	+ ProtocolLengths.CHALLENGE_RESPONSE];

		Tools.putUnsignedInt(resultByte, cmdId, 0);
		Tools.putBytes(resultByte, posId.getBytes(), ProtocolLengths.COMMAND);
		Tools.putBytes(resultByte, challengeResponse, ProtocolLengths.COMMAND + ProtocolLengths.POS_ID);
		
		log.trace("LoginRequestMessage:{}", requestMessage);

		return resultByte;
	}

}
