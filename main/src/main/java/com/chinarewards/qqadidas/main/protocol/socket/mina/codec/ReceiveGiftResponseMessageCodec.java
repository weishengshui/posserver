package com.chinarewards.qqadidas.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;
import java.util.Calendar;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqadidas.main.protocol.cmd.ReceiveGiftResponseMessage;
import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * receive gift response message coder
 * 
 * @author weishengshui
 * 
 */
public class ReceiveGiftResponseMessageCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {

		log.debug("receive gift response message decode");

		ReceiveGiftResponseMessage message = new ReceiveGiftResponseMessage();

		log.debug("in.remainning()={}", in.remaining());

		if (in.remaining() < ProtocolLengths.COMMAND
				+ ProtocolLengths.QQADIDAS_RESULT_LENGTH
				+ ProtocolLengths.CR_DATE_LENGTH + ProtocolLengths.POSNETSTRLEN
				* 2) {
			throw new PackageException(
					"receive gift response message body error, body message is: "
							+ in);
		}
		long cmdId = in.getUnsignedInt();
		int result = in.getInt();
		byte[] xact_time = new byte[11];
		in.get(xact_time);
		Calendar xact = Tools.getDate(xact_time);

		int titleLength = in.getUnsignedShort();
		byte[] title = new byte[titleLength];
		in.get(title);
		int tipLength = in.getUnsignedShort();
		byte[] tip = new byte[tipLength];
		in.get(tip);

		message.setCmdId(cmdId);
		message.setResult(result);
		message.setXact_time(xact);
		message.setTitleLength(titleLength);
		message.setTitle(Tools.byteToString(title, charset));
		message.setTipLength(tipLength);
		message.setTip(Tools.byteToString(tip, charset));
		return message;
	}

	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {

		log.debug("receive gift response message encode");

		ReceiveGiftResponseMessage message = (ReceiveGiftResponseMessage) bodyMessage;

		long cmdId = message.getCmdId();
		int result = message.getResult();
		Calendar xact_time = message.getXact_time();
		int titleLength = message.getTitleLength();
		String title = message.getTitle();
		int tipLength = message.getTipLength();
		String tip = message.getTip();

		byte[] resultByte = new byte[ProtocolLengths.COMMAND
				+ ProtocolLengths.QQADIDAS_RESULT_LENGTH
				+ ProtocolLengths.CR_DATE_LENGTH + ProtocolLengths.POSNETSTRLEN
				+ titleLength + ProtocolLengths.POSNETSTRLEN + tipLength];

		Tools.putUnsignedInt(resultByte, cmdId, 0);
		Tools.putInt(resultByte, result, ProtocolLengths.COMMAND);
		Tools.putDate(resultByte, xact_time, ProtocolLengths.COMMAND
				+ ProtocolLengths.QQADIDAS_RESULT_LENGTH);
		Tools.putUnsignedShort(resultByte, titleLength, ProtocolLengths.COMMAND
				+ ProtocolLengths.QQADIDAS_RESULT_LENGTH
				+ ProtocolLengths.CR_DATE_LENGTH);

		Tools.putUnsignedShort(resultByte, tipLength, ProtocolLengths.COMMAND
				+ 4 + 11 + 2 + titleLength);
		if (titleLength != 0) {
			Tools.putBytes(resultByte, title.getBytes(charset),
					ProtocolLengths.COMMAND
							+ ProtocolLengths.QQADIDAS_RESULT_LENGTH
							+ ProtocolLengths.CR_DATE_LENGTH
							+ ProtocolLengths.POSNETSTRLEN);

		}
		if (tipLength != 0) {
			Tools.putBytes(resultByte, tip.getBytes(charset),
					ProtocolLengths.COMMAND + 4 + 11 + 2 + titleLength + 2);
			Tools.putBytes(resultByte, title.getBytes(charset),
					ProtocolLengths.COMMAND
							+ ProtocolLengths.QQADIDAS_RESULT_LENGTH
							+ ProtocolLengths.CR_DATE_LENGTH
							+ ProtocolLengths.POSNETSTRLEN);
		}
		
		return resultByte;
	}

}
