package com.chinarewards.timeserver.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;
import com.chinarewards.timeserver.main.protocol.cmd.TimeServerRequestMessage;

public class TimeServerMessageCodec implements ICommandCodec {

	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		TimeServerRequestMessage message = new TimeServerRequestMessage();
		if(in.remaining()< ProtocolLengths.COMMAND){
			throw new PackageException("time server message body error, body message is : "+in);
		}
		long cmdId = in.getUnsignedInt();
		message.setCmdId(cmdId);
		return message;
	}

	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {
		TimeServerRequestMessage requestMessage = (TimeServerRequestMessage)bodyMessage;
		long cmdId = requestMessage.getCmdId();
		byte[] resultBytes = new byte[ProtocolLengths.COMMAND];
		Tools.putUnsignedInt(resultBytes, cmdId, 0);
		return resultBytes;
	}

}
