package com.chinarewards.qqadidas.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;
import java.util.Calendar;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqadidas.main.protocol.cmd.PrivilegeExchangeResponseMessage;
import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * right for response message coder
 * 
 * @author weishengshui
 * 
 */
public class PrivilegeExchangeResponseMessageCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {

		log.debug("privilege exchange response message decode");
		PrivilegeExchangeResponseMessage message = new PrivilegeExchangeResponseMessage();

		log.debug("in.remainning()={}", in.remaining());

		if (in.remaining() < ProtocolLengths.COMMAND
				+ ProtocolLengths.QQADIDAS_RESULT_LENGTH
				+ ProtocolLengths.CR_DATE_LENGTH
				+ ProtocolLengths.POSNETSTRLEN * 2) {
			throw new PackageException(
					"privilege exchange response message body error, body message is: "
							+ in);
		}
		long cmdId = in.getUnsignedInt();
		int result = in.getInt();
		byte[] xact_time = new byte[11];
		in.get(xact_time);
		Calendar xact = Tools.getDate(xact_time);

		int titleLength = in.getUnsignedShort();
		int tipLength;
		byte[] title;
		byte[] tip;
		if(titleLength==0){
			tipLength = in.getUnsignedShort();
			message.setTitleLength(0);
			message.setTitle(null);
			message.setTipLength(0);
			message.setTip(null);
		}else{
			title = new byte[titleLength];
			in.get(title);
			tipLength = in.getUnsignedShort();
			tip = new byte[tipLength];
			in.get(tip);
			message.setTitleLength(titleLength);
			message.setTitle(Tools.byteToString(title, charset));
			message.setTipLength(tipLength);
			message.setTip(Tools.byteToString(tip, charset));
		}
		

		message.setCmdId(cmdId);
		message.setResult(result);
		message.setXact_time(xact);
		
		return message;
	}

	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {

		log.debug("privilege exchange response message encode");
		PrivilegeExchangeResponseMessage message = (PrivilegeExchangeResponseMessage) bodyMessage;

		long cmdId = message.getCmdId();
		int result = message.getResult();
		Calendar xact_time = message.getXact_time();
		int titleLength= message.getTitleLength();
		String title = message.getTitle();
		int tipLength = message.getTipLength();
		String tip = message.getTip();

		byte[] resultByte = new byte[ProtocolLengths.COMMAND
				+ ProtocolLengths.QQADIDAS_RESULT_LENGTH
				+ ProtocolLengths.CR_DATE_LENGTH
				+ ProtocolLengths.POSNETSTRLEN * 2 + titleLength + tipLength];
		log.debug("privilege exchange response message encode resultByte length: "
				+ resultByte.length);
		
		//cmdId
		Tools.putUnsignedInt(resultByte, cmdId, 0);
		//result
		Tools.putInt(resultByte, result, ProtocolLengths.COMMAND);
		//xact_time
		Tools.putDate(resultByte, xact_time, ProtocolLengths.COMMAND
				+ ProtocolLengths.QQADIDAS_RESULT_LENGTH);
		//titleLength
		Tools.putUnsignedShort(resultByte, titleLength, ProtocolLengths.COMMAND
				+ ProtocolLengths.QQADIDAS_RESULT_LENGTH
				+ ProtocolLengths.CR_DATE_LENGTH);
		if(titleLength==0){
			Tools.putUnsignedShort(resultByte, tipLength, ProtocolLengths.COMMAND
					+ ProtocolLengths.QQADIDAS_RESULT_LENGTH
					+ ProtocolLengths.CR_DATE_LENGTH
					+ ProtocolLengths.POSNETSTRLEN + titleLength);
		}else{
			//title
			Tools.putBytes(resultByte, title.getBytes(charset),
					ProtocolLengths.COMMAND
							+ ProtocolLengths.QQADIDAS_RESULT_LENGTH
							+ ProtocolLengths.CR_DATE_LENGTH
							+ ProtocolLengths.POSNETSTRLEN);
			//tipLength
			Tools.putUnsignedShort(resultByte, tipLength, ProtocolLengths.COMMAND
					+ ProtocolLengths.QQADIDAS_RESULT_LENGTH
					+ ProtocolLengths.CR_DATE_LENGTH
					+ ProtocolLengths.POSNETSTRLEN + titleLength);
			//tip
			Tools.putBytes(resultByte, tip.getBytes(charset),
					ProtocolLengths.COMMAND
							+ ProtocolLengths.QQADIDAS_RESULT_LENGTH
							+ ProtocolLengths.CR_DATE_LENGTH
							+ ProtocolLengths.POSNETSTRLEN + titleLength
							+ ProtocolLengths.POSNETSTRLEN);
		}
		
		return resultByte;
	}

}
