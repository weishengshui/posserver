package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.HuaxiaRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

public class HuaxiaSearchBodyMessageCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		log.debug("HuaxiaSearch request message decode");
		HuaxiaRequestMessage message = new HuaxiaRequestMessage();
		if (in.remaining() < ProtocolLengths.COMMAND) {
			throw new PackageException(
					"HuaxiaSearch packge message body error, body message is :"
							+ in);
		}
		long cmdId = in.getUnsignedInt();
		byte[] requestByte = new byte[in.remaining()];
		in.get(requestByte);
		log.debug("requestByte :"+Arrays.toString(requestByte));
		int cardNumEnd = -1;
		boolean errorFlag = false;
		for (int i = 0; i < requestByte.length; i++) {
			if (requestByte[i] == 0) {
				if (cardNumEnd == -1) {
					cardNumEnd = i;
				} else {
					errorFlag = true;
				}
			}
		}
		if (requestByte[requestByte.length - 1] != 0) {
			errorFlag = true;
		}
		if (errorFlag) {
			throw new PackageException(
					"HuaxiaSearch packge message body error, body message");
		}
		String cardNum = null;
		if (cardNumEnd != 0) {
			byte[] tmp = new byte[cardNumEnd];
			for (int i = 0; i < cardNumEnd; i++) {
				tmp[i] = requestByte[i];
			}
			cardNum = new String(tmp, charset);
		}

		message.setCmdId(cmdId);
		message.setCardNum(cardNum);
		
		log.trace("HuaxiaRequestMessage:", message);
		return message;
	}
	
	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {
		throw new UnsupportedOperationException();
	}

}
