package com.chinarewards.qqadidas.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqadidas.main.protocol.cmd.ReceiveGiftRequestMessage;
import com.chinarewards.qqadidas.main.protocol.cmd.WeixinSignInRequestMessage;
import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * weixin sign in message coder
 * 
 * @author weishengshui
 * 
 */
public class WeixinSignInMessageCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		log.debug("weixin sign in request message decode");
		WeixinSignInRequestMessage message = new WeixinSignInRequestMessage();
		log.debug("in.remaining()={}", in.remaining());
		if (in.remaining() < ProtocolLengths.COMMAND
				+ ProtocolLengths.POSNETSTRLEN) {
			throw new PackageException(
					"weixin sign in message body error, body message is: " + in);
		}

		long cmdId = in.getUnsignedInt();
		int weixinNoLength = in.getUnsignedShort();
		byte[] weixinNo = new byte[weixinNoLength];
		in.get(weixinNo);

		// reconstruct message
		message.setCmdId(cmdId);
		message.setWeixinNoLength(weixinNoLength);
		message.setWeixinNo(Tools.byteToString(weixinNo, charset));

		return message;
	}

	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {
		log.debug("weixin sign in request message encode");
		WeixinSignInRequestMessage message = (WeixinSignInRequestMessage) bodyMessage;
		long cmdId = message.getCmdId();
		int weixinNoLength = message.getWeixinNoLength();
		String weixinNo = message.getWeixinNo();

		byte[] resultByte = new byte[ProtocolLengths.COMMAND
				+ ProtocolLengths.POSNETSTRLEN + weixinNoLength];

		Tools.putUnsignedInt(resultByte, cmdId, 0);
		Tools.putUnsignedShort(resultByte, weixinNoLength,
				ProtocolLengths.COMMAND);
		if(weixinNo!=null){
			Tools.putBytes(resultByte, weixinNo.getBytes(charset),
					ProtocolLengths.COMMAND + ProtocolLengths.POSNETSTRLEN);
		}
		
		return resultByte;
	}

}
