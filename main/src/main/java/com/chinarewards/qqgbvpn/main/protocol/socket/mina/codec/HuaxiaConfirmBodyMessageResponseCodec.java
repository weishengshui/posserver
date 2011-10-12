package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.HuaxiaResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

public class HuaxiaConfirmBodyMessageResponseCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {
		log.debug("HuaxiaConfirm response message encode");
		HuaxiaResponseMessage responseMessage = (HuaxiaResponseMessage) bodyMessage;

		long cmdId = responseMessage.getCmdId();
		int result = responseMessage.getResult();
		String txDate = responseMessage.getTxDate();
		
		byte[] tmp = txDate.getBytes(charset);

		byte[] resultByte = new byte[ProtocolLengths.COMMAND + ProtocolLengths.RESULT +  + tmp.length];
		
		Tools.putUnsignedInt(resultByte, cmdId, 0);
		Tools.putUnsignedShort(resultByte, result, ProtocolLengths.COMMAND);
		Tools.putBytes(resultByte, tmp, ProtocolLengths.COMMAND + ProtocolLengths.RESULT);
		
		log.debug("HuaxiaConfirm message encode end ,result byte is ({})",Arrays.toString(resultByte));
		return resultByte;
	}

}
