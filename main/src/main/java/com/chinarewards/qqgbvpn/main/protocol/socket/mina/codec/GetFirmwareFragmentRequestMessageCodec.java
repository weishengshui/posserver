package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.GetFirmwareFragmentRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

/**
 * Implements the codec for the message get firmware fragment.
 * 
 * @author cyril
 * @since 0.1.0
 */
public class GetFirmwareFragmentRequestMessageCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {

		// insufficient bytes give, die.
		if (in.remaining() < ProtocolLengths.COMMAND + ProtocolLengths.POS_ID
				+ 4 + 4) {
			throw new PackageException();
		}
		long cmdId = in.getUnsignedInt();

		// decode data

		// decode POS ID, Pay attention to middle \0
		byte[] posid = new byte[ProtocolLengths.POS_ID];
		in.get(posid);
		long offset = in.getUnsignedInt();
		long length = in.getUnsignedInt();

		// reconstruct message.
		GetFirmwareFragmentRequestMessage message = new GetFirmwareFragmentRequestMessage(
				Tools.byteToString(posid, charset), offset, length);

		return message;
	}

	/**
	 * TODO implements encoding function.
	 */
	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {
		throw new UnsupportedOperationException(
				"Message encoding not supported");
	}
}
