package com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.commons.io.HexDump;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.config.CmdProperties;
import com.chinarewards.qqgbvpn.main.exception.PackgeException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.ErrorBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.HeadMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.IBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.Message;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

public class MessageDecoder extends CumulativeProtocolDecoder {

	private Logger log = LoggerFactory.getLogger(getClass());

	private Charset charset;

	private Injector injector;

	public MessageDecoder(Charset charset, Injector injector) {
		this.charset = charset;
		this.injector = injector;
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {

		log.debug("MessageDecoder.doDecode invoked");
		log.debug("in.remaining is {}", in.remaining());

		// check length, it must greater than head length
		if (in.remaining() > ProtocolLengths.HEAD) {

			log.debug("Do processing");
			HeadMessage headMessage = new HeadMessage();
			// read header
			// read seq
			log.debug("Read head");
			long seq = in.getUnsignedInt();
			headMessage.setSeq(seq);

			// read ack
			long ack = in.getUnsignedInt();
			headMessage.setAck(ack);

			// read flags
			int flags = in.getUnsignedShort();
			headMessage.setFlags(flags);
			log.debug("in.remaining() = " + in.remaining());

			// read checksum
			log.debug("read checksum");
			int checksum = in.getUnsignedShort();
			log.debug("checksum========:" + checksum);
			headMessage.setChecksum(checksum);
			log.debug("in.remaining() = " + in.remaining());

			// read message size
			log.debug("read message size");
			long messageSize = in.getUnsignedInt();
			headMessage.setMessageSize(messageSize);
			log.debug("in.remaining() = " + in.remaining());
			log.debug("headMessage ====: {}", headMessage.toString());

			// check length
			if (messageSize != ProtocolLengths.HEAD + in.remaining()) {
				ErrorBodyMessage bodyMessage = new ErrorBodyMessage();
				bodyMessage.setErrorCode(CmdConstant.ERROR_MESSAGE_SIZE_CODE);
				Message message = new Message(headMessage, bodyMessage);
				out.write(message);
				return true;
			}
			int position = in.position();

			in.position(0);
			// byteTmp: raw bytes
			byte[] byteTmp = new byte[in.remaining()];
			in.get(byteTmp);
			CodecUtil.debugRaw(log, byteTmp);
			Tools.putUnsignedShort(byteTmp, 0, 10);
			log.debug("byteTmp==msg==:" + Arrays.toString(byteTmp));

			int checkSumTmp = Tools.checkSum(byteTmp, byteTmp.length);
			log.debug("checkSumTmp========:" + checkSumTmp);

			// FIXME re-enable checksum
			
//			if (checkSumTmp != checksum) {
//				ErrorBodyMessage bodyMessage = new ErrorBodyMessage();
//				bodyMessage.setErrorCode(CmdConstant.ERROR_CHECKSUM_CODE);
//				Message message = new Message(headMessage, bodyMessage);
//				out.write(message);
//				return true;
//			}

			in.position(position);
			IBodyMessage bodyMessage = null;
			try {
				bodyMessage = this.decodeMessageBody(in, charset);
			} catch (Exception e) {
				ErrorBodyMessage errorBodyMessage = new ErrorBodyMessage();
				errorBodyMessage.setErrorCode(CmdConstant.ERROR_MESSAGE_CODE);
				Message message = new Message(headMessage, errorBodyMessage);
				out.write(message);
				return true;
			}
			Message message = new Message(headMessage, bodyMessage);
			out.write(message);
			return true;

		} else {
			HeadMessage headMessage = new HeadMessage();
			ErrorBodyMessage errorBodyMessage = new ErrorBodyMessage();
			errorBodyMessage.setErrorCode(CmdConstant.ERROR_MESSAGE_CODE);
			Message message = new Message(headMessage, errorBodyMessage);
			out.write(message);
			byte[] tmp = new byte[in.remaining()];
			in.get(tmp);
			return true;
		}

	}

	private IBodyMessage decodeMessageBody(IoBuffer in, Charset charset)
			throws PackgeException {
		// get cmdId and process it
		int position = in.position();
		long cmdId = in.getUnsignedInt();
		log.debug("position========:" + position);
		in.position(position);
		log.debug("cmdId========:" + cmdId);
		log.debug("injector========:" + (injector == null));
		injector.getInstance(CmdProperties.class);
		String cmdName = injector.getInstance(CmdProperties.class)
				.getCmdNameById(cmdId);
		log.debug("cmdName========:" + cmdName);
		if (cmdName == null || cmdName.length() == 0) {
			throw new PackgeException("cmd id is not exits,cmdId is :" + cmdId);
		}
		// Dispatcher
		IBodyMessageCoder bodyMessageCoder = injector.getInstance(Key.get(
				IBodyMessageCoder.class, Names.named(cmdName)));

		return bodyMessageCoder.decode(in, charset);
	}

}