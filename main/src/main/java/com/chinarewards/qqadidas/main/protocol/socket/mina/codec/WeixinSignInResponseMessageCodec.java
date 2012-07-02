package com.chinarewards.qqadidas.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqadidas.main.protocol.cmd.WeixinSignInResponseMessage;
import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * weixin sign in response message coder
 * 
 * @author weishengshui
 * 
 */
public class WeixinSignInResponseMessageCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {

		log.debug("weixin sign in response message decode");
		WeixinSignInResponseMessage message = new WeixinSignInResponseMessage();

		log.debug("in.remainning()={}", in.remaining());

		if (in.remaining() < ProtocolLengths.COMMAND + ProtocolLengths.QQADIDAS_RESULT_LENGTH) {
			throw new PackageException(
					"weixin sign in response message body error, body message is: "
							+ in);
		}
		long cmdId = in.getUnsignedInt();
		int result = in.getInt();
		
		// reconstruct message
		message.setCmdId(cmdId);
		message.setResult(result);
		
		return message;
	}

	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {

		log.debug("weixin sign in response message encode");

		WeixinSignInResponseMessage message = (WeixinSignInResponseMessage)bodyMessage;

		long cmdId = message.getCmdId();
		int result = message.getResult();
		
		byte[] resultByte = new byte[ProtocolLengths.COMMAND+ProtocolLengths.QQADIDAS_RESULT_LENGTH];
		
		Tools.putUnsignedInt(resultByte, cmdId, 0);
		Tools.putInt(resultByte, result, ProtocolLengths.COMMAND);

		return resultByte;
	}

}
