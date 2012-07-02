package com.chinarewards.qqadidas.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqadidas.main.protocol.cmd.ReceiveGiftRequestMessage;
import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * receive gift message coder
 * 
 * @author weishengshui
 * 
 */
public class ReceiveGiftMessageCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		log.debug("receive gift request message decode");
		ReceiveGiftRequestMessage message = new ReceiveGiftRequestMessage();
		log.debug("in.remaining()={}", in.remaining());
		if (in.remaining() < ProtocolLengths.COMMAND + ProtocolLengths.POSNETSTRLEN) {
			throw new PackageException(
					"receive gift message body error, body message is: " + in);
		}

		long cmdId = in.getUnsignedInt();
		int cdkeyLength = in.getUnsignedShort();
		byte[] cdkey = new byte[cdkeyLength];
		in.get(cdkey);

		// reconstruct message
		message.setCmdId(cmdId);
		message.setCdkeyLength(cdkeyLength);
		message.setCdkey(Tools.byteToString(cdkey, charset));

		return message;
	}

	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {
		log.debug("receive gift request message encode");
		ReceiveGiftRequestMessage message = (ReceiveGiftRequestMessage)bodyMessage;
		long cmdId = message.getCmdId();
		int cdkeyLength = message.getCdkeyLength();
		String cdkey = message.getCdkey();
		
		byte[] resultByte = new byte[ProtocolLengths.COMMAND+2+cdkeyLength];
		
		Tools.putUnsignedInt(resultByte, cmdId, 0);
		Tools.putUnsignedShort(resultByte, cdkeyLength, ProtocolLengths.COMMAND);
		Tools.putBytes(resultByte, cdkey.getBytes(), ProtocolLengths.COMMAND+2);
		
		return resultByte;
	}

}
