package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ValCallbackRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ValCallbackResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

public class ValidationCallbackResponseCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * description：mock pos test use it!
	 * @param bodyMessage
	 * @param charset
	 * @return
	 * @time 2011-9-22   下午07:23:35
	 * @author Seek
	 */
	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {
		log.debug("validationCallback message encode");
		ValCallbackResponseMessage responseMessage = (ValCallbackResponseMessage) bodyMessage;

		long cmdId = responseMessage.getCmdId();
		int result = responseMessage.getResult();

		byte[] resultByte = new byte[ProtocolLengths.COMMAND
				+ ProtocolLengths.RESULT];
		Tools.putUnsignedInt(resultByte, cmdId, 0);
		Tools.putUnsignedShort(resultByte, result, ProtocolLengths.COMMAND);
		
		log.trace("ValCallbackResponseMessage:", responseMessage);
		return resultByte;
	}

}
