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

public class HuaxiaSearchBodyMessageResponseCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {
		log.debug("HuaxiaSearch response message encode");
		HuaxiaResponseMessage responseMessage = (HuaxiaResponseMessage) bodyMessage;

		long cmdId = responseMessage.getCmdId();
		int result = responseMessage.getResult();
		//兑换次数
		int redeemCount = responseMessage.getRedeemCount();

		byte[] resultByte = new byte[ProtocolLengths.COMMAND + ProtocolLengths.RESULT + ProtocolLengths.REDEEM_COUNT];
		Tools.putUnsignedInt(resultByte, cmdId, 0);
		Tools.putUnsignedShort(resultByte, result, ProtocolLengths.COMMAND);
		Tools.putUnsignedShort(resultByte, redeemCount, (ProtocolLengths.COMMAND + ProtocolLengths.RESULT));
		
		log.debug("HuaxiaSearch message encode end ,result byte is ({})",Arrays.toString(resultByte));
		return resultByte;
	}

}
