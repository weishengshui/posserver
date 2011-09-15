package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpDoneResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

/**
 * description：POS固件升级成功 response
 * @copyright binfen.cc
 * @projectName main
 * @time 2011-9-15   上午11:15:04
 * @author Seek
 */
public class FirmwareUpDoneResponseCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		return null;
	}

	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {
		log.debug("FirmwareUpDoneResponse message encode");
		FirmwareUpDoneResponseMessage msg = (FirmwareUpDoneResponseMessage) bodyMessage;
		
		// prepare buffer
		IoBuffer buf = IoBuffer.allocate(ProtocolLengths.COMMAND
				+ ProtocolLengths.RESULT);
		
		buf.position(0);

		// encode data
		// command ID
		buf.putUnsignedInt(msg.getCmdId());
		// result
		buf.putShort(msg.getResult());

		// return result
		return buf.array();

	}

}
