package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpgradeRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpgradeRequestResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

/**
 * firmware request codec
 * 
 * @author kmtong
 * 
 */
public class FirmwareRequestMessageCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());

	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		log.debug("FirmwareRequest message decode");
		FirmwareUpgradeRequestMessage message = new FirmwareUpgradeRequestMessage();
		if (in.remaining() != ProtocolLengths.COMMAND + ProtocolLengths.POS_ID) {
			throw new PackageException(
					"FirmwareRequest packge message body error, body message is :"
							+ in);
		}

		// read from buffer
		long cmdId = in.getUnsignedInt();
		byte[] posIdBuf = new byte[ProtocolLengths.POS_ID];
		in.get(posIdBuf);

		// construct message
		message.setPosId(Tools.byteToString(posIdBuf, charset));

		log.debug(
				"FirmwareRequest message request:cmdId is ({}) , posid is ({})",
				new Object[] { cmdId, posIdBuf });
		return message;
	}

	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {
		log.debug("FirmwareRequest message encode");

		FirmwareUpgradeRequestResponseMessage responseMessage = (FirmwareUpgradeRequestResponseMessage) bodyMessage;

		IoBuffer buf = IoBuffer.allocate(ProtocolLengths.COMMAND
				+ ProtocolLengths.RESULT + 4);
		buf.setAutoExpand(true);

		buf.putUnsignedInt(responseMessage.getCmdId());
		buf.putShort((short) responseMessage.getResult());
		buf.putUnsignedInt(responseMessage.getSize());

		try {
			StringBuffer sb = new StringBuffer(
					responseMessage.getFirmwareName());
			sb.append(0);
			buf.putString(sb, charset.newEncoder());
			return buf.array();
		} catch (CharacterCodingException e) {
			e.printStackTrace();
			buf.putChar((char) 0);
		}

		return buf.array();
	}

}
