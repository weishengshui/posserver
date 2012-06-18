package com.chinarewards.timeserver.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;
import com.chinarewards.timeserver.main.protocol.cmd.TimeServerResponseMessage;

public class TimeServerMessageResponseCodec implements ICommandCodec {

	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		TimeServerResponseMessage responseMessage = new TimeServerResponseMessage();
		if (in.remaining() < ProtocolLengths.COMMAND + 8) {
			throw new PackageException(
					"time server message body error, body message is :" + in);
		}

		long cmdId = in.getUnsignedInt();
		long time = in.getLong();

		responseMessage.setCmdId(cmdId);
		responseMessage.setTime(time);
		return responseMessage;
	}

	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {

		TimeServerResponseMessage responseMessage = (TimeServerResponseMessage) bodyMessage;

		long cmdId = responseMessage.getCmdId();
		long time = responseMessage.getTime();

		byte[] resultBytes = new byte[ProtocolLengths.COMMAND + 8];
		Tools.putUnsignedInt(resultBytes, cmdId, 0);
		putUnsignedLong(resultBytes, time, ProtocolLengths.COMMAND);

		return resultBytes;
	}

	private void putUnsignedLong(byte[] bb, long x, int index) {
		bb[index + 0] = (byte) (x >> 56);
		bb[index + 1] = (byte) (x >> 48);
		bb[index + 2] = (byte) (x >> 40);
		bb[index + 3] = (byte) (x >> 32);
		bb[index + 4] = (byte) (x >> 24);
		bb[index + 5] = (byte) (x >> 16);
		bb[index + 6] = (byte) (x >> 8);
		bb[index + 7] = (byte) (x >> 0);
	}

}
