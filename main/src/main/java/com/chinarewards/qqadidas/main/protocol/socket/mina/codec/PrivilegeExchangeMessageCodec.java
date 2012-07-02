package com.chinarewards.qqadidas.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqadidas.main.protocol.cmd.PrivilegeExchangeRequestMessage;
import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * privilege exchange message coder
 * 
 * @author weishengshui
 * 
 */
public class PrivilegeExchangeMessageCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		log.debug("privilege exchange request message decode");
		PrivilegeExchangeRequestMessage message = new PrivilegeExchangeRequestMessage();
		log.debug("in.remaining()={}", in.remaining());
		if (in.remaining() < ProtocolLengths.COMMAND + ProtocolLengths.POSNETSTRLEN + ProtocolLengths.POSNETSTRLEN) {
			throw new PackageException(
					"privilege exchange message body error, body message is: " + in);
		}

		long cmdId = in.getUnsignedInt();
		int cdkeyLength = in.getUnsignedShort();
		
		byte[] cdkey = new byte[cdkeyLength];
		in.get(cdkey);
		int amountLength = in.getUnsignedShort();
		byte[] amount = new byte[amountLength];
		in.get(amount);
		// reconstruct message
		message.setCmdId(cmdId);
		message.setCdkeyLength(cdkeyLength);
		message.setCdkey(Tools.byteToString(cdkey, charset));
		message.setAmountLength(amountLength);
		message.setAmount(Tools.byteToString(amount, charset));

		return message;
	}

	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {
		log.debug("privilege exchange request message encode");
		PrivilegeExchangeRequestMessage message = (PrivilegeExchangeRequestMessage)bodyMessage;
		
		long cmdId = message.getCmdId();
		int cdkeyLength = message.getCdkeyLength();
		String cdkey = message.getCdkey();
		int amountLength = message.getAmountLength();
		String amount = message.getAmount();
		
		byte[] resultByte = new byte[ProtocolLengths.COMMAND+ProtocolLengths.POSNETSTRLEN*2+cdkeyLength+amountLength];
		
		Tools.putUnsignedInt(resultByte, cmdId, 0);
		Tools.putUnsignedShort(resultByte, cdkeyLength, ProtocolLengths.COMMAND);
		Tools.putBytes(resultByte, cdkey.getBytes(charset), ProtocolLengths.COMMAND+ProtocolLengths.POSNETSTRLEN);
		Tools.putUnsignedShort(resultByte, amountLength, ProtocolLengths.COMMAND+ProtocolLengths.POSNETSTRLEN+cdkeyLength);
		Tools.putBytes(resultByte, amount.getBytes(charset), ProtocolLengths.COMMAND+ProtocolLengths.POSNETSTRLEN+cdkeyLength+ProtocolLengths.POSNETSTRLEN);
		
		return resultByte;
	}

}
