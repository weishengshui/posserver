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

public class HuaxiaAckBodyMessageCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		log.debug("HuaxiaAck request message decode");
		HuaxiaRequestMessage message = new HuaxiaRequestMessage();
		if (in.remaining() < ProtocolLengths.COMMAND) {
			throw new PackageException(
					"HuaxiaAck packge message body error, body message is :"
							+ in);
		}
		long cmdId = in.getUnsignedInt();
		byte[] requestByte = new byte[in.remaining()];
		in.get(requestByte);
		log.debug("requestByte :"+Arrays.toString(requestByte));
		int cardNumEnd = -1;
		int chanceIdEnd = -1;
		int ackIdEnd = -1;
		boolean errorFlag = false;
		for (int i = 0; i < requestByte.length; i++) {
			if (requestByte[i] == 0) {
				if (cardNumEnd == -1) {
					cardNumEnd = i;
				} else if (chanceIdEnd == -1) {
					chanceIdEnd = i;
				} else if (ackIdEnd == -1) {
					ackIdEnd = i;
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
					"HuaxiaAck packge message body error, body message");
		}
		
		log.debug("HuaxiaAck message request:cmdId is ({}) , cardNumEnd is ({}) , chanceIdEnd is ({}) , ackIdEnd is ({})"
				, new Object[] { cmdId, cardNumEnd ,chanceIdEnd, ackIdEnd});
		
		String cardNum = null;
		String chanceId = null;
		String ackId = null;
		
		if (cardNumEnd != 0) {
			byte[] tmp = new byte[cardNumEnd];
			for (int i = 0; i < cardNumEnd; i++) {
				tmp[i] = requestByte[i];
			}
			cardNum = new String(tmp, charset);
		}
		
		if (chanceIdEnd - cardNumEnd != 1) {
			byte[] tmp = new byte[chanceIdEnd - cardNumEnd - 1];
			for (int i = 0; i < chanceIdEnd - cardNumEnd - 1; i++) {
				tmp[i] = requestByte[i + cardNumEnd + 1];
			}
			chanceId = new String(tmp, charset);
		}
		
		if (ackIdEnd - chanceIdEnd != 1) {
			byte[] tmp = new byte[requestByte.length - chanceIdEnd - 2];
			for (int i = 0; i < requestByte.length - chanceIdEnd - 2; i++) {
				tmp[i] = requestByte[i + chanceIdEnd + 1];
			}
			ackId = new String(tmp, charset);
		}

		message.setCmdId(cmdId);
		message.setCardNum(cardNum);
		message.setChanceId(chanceId);
		message.setAckId(ackId);

		log.trace("HuaxiaRequestMessage:{}", message);
		return message;
	}
	
	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {
		throw new UnsupportedOperationException();
	}

}
