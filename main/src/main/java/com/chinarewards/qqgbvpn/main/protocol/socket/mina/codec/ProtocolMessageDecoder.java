/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.CmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ErrorBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.HeadMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.Message;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

/**
 * 
 * 
 * @author cyril
 * @since 0.1.0
 */
public class ProtocolMessageDecoder {

	private static final Logger log = LoggerFactory
			.getLogger(ProtocolMessageDecoder.class);

	/**
	 * Stores the result of message decoding.
	 * 
	 * @author cyril
	 * @since 0.1.0
	 */
	public static class Result {

		private final boolean moreDataRequired;

		private final Object message;

		/**
		 * 
		 * @param parsed
		 * @param message
		 */
		public Result(boolean parsed, Object message) {
			this.moreDataRequired = parsed;
			this.message = message;
		}

		/**
		 * Returns whether more data is required in order to parse the raw
		 * stream.
		 * 
		 * @return <code>true</code> if more input is required,
		 *         <code>false</code> otherwise.
		 */
		public boolean isMoreDataRequired() {
			return moreDataRequired;
		}

		/**
		 * The parsed message. This method will return a value if and only if
		 * {@link #isMoreDataRequired()} returns <code>true</code>. Otherwise
		 * this method will return <code>false</code>.
		 * 
		 * @return the message
		 */
		public Object getMessage() {
			return message;
		}

	}

	private CmdCodecFactory cmdCodecFactory;

	/**
	 * Creates an instance of <code>ProtocolMessageDecoder</code>.
	 * 
	 * @param cmdCodecFactory
	 */
	public ProtocolMessageDecoder(CmdCodecFactory cmdCodecFactory) {
		this.cmdCodecFactory = cmdCodecFactory;
	}

	/**
	 * 
	 * @param in
	 * @param charset
	 */
	public Result decode(IoBuffer in, Charset charset) throws Exception {

		log.trace("IoBuffer.remaining() = {}", in.remaining());

		// check length, it must greater than head length
		if (in.remaining() > ProtocolLengths.HEAD) {

			log.trace("Do message processing");
			HeadMessage headMessage = new HeadMessage();
			// read header
			// read seq
			log.trace("Read message header");
			long seq = in.getUnsignedInt();
			headMessage.setSeq(seq);

			// read ack
			long ack = in.getUnsignedInt();
			headMessage.setAck(ack);

			// read flags
			int flags = in.getUnsignedShort();
			headMessage.setFlags(flags);
			log.trace("- IoBuffer.remaining(): " + in.remaining());

			// read checksum
			log.trace("Read message checksum");
			int checksum = in.getUnsignedShort();
			log.trace("- Message checksum: {}", checksum);
			headMessage.setChecksum(checksum);
			log.trace("- IoBuffer.remaining(): " + in.remaining());

			// read message size
			log.trace("read message size");
			long messageSize = in.getUnsignedInt();
			headMessage.setMessageSize(messageSize);
			log.trace("- IoBuffer.remaining(): " + in.remaining());
			log.trace("- message header: {}", headMessage.toString());

			// check length
			// XXX suspicious code, may lead to unwanted error code 3: invalid
			// size
			// XXX should wait until the whole package is received.
			if (messageSize != ProtocolLengths.HEAD + in.remaining()) {
				ErrorBodyMessage bodyMessage = new ErrorBodyMessage();
				bodyMessage.setErrorCode(CmdConstant.ERROR_MESSAGE_SIZE_CODE);
//				Message message = new Message(headMessage, bodyMessage);
				return new Result(true, null);
			}
			int position = in.position();

			// byteTmp: raw bytes
			in.position(0);
			byte[] byteTmp = new byte[in.remaining()];
			in.get(byteTmp);
			CodecUtil.debugRaw(log, byteTmp);
			Tools.putUnsignedShort(byteTmp, 0, 10);
			log.trace("- byteTmp==msg==:" + Arrays.toString(byteTmp));

			int calculatedChceksum = Tools.checkSum(byteTmp, byteTmp.length);
			log.trace("- calculated checksum: "+ calculatedChceksum);

			// validate the checksum. if not correct, return an error response.
			if (calculatedChceksum != checksum) {
				ErrorBodyMessage bodyMessage = new ErrorBodyMessage();
				bodyMessage.setErrorCode(CmdConstant.ERROR_CHECKSUM_CODE);
				Message message = new Message(headMessage, bodyMessage);
				return new Result(false, message);
			}

			// really decode the command message.
			in.position(position);
			ICommand bodyMessage = null;
			try {
				bodyMessage = this.decodeMessageBody(in, charset);
			} catch (Throwable e) {
				ErrorBodyMessage errorBodyMessage = new ErrorBodyMessage();
				errorBodyMessage.setErrorCode(CmdConstant.ERROR_MESSAGE_CODE);
				Message message = new Message(headMessage, errorBodyMessage);
				return new Result(false, message);
			}
			Message message = new Message(headMessage, bodyMessage);
			return new Result(false, message);

		} else {
			
			return new Result(true, null);
			
//			HeadMessage headMessage = new HeadMessage();
//			ErrorBodyMessage errorBodyMessage = new ErrorBodyMessage();
//			errorBodyMessage.setErrorCode(CmdConstant.ERROR_MESSAGE_CODE);
//			Message message = new Message(headMessage, errorBodyMessage);
//			byte[] tmp = new byte[in.remaining()];
//			in.get(tmp);
//
//			return new Result(false, message);
		}

	}

	protected ICommand decodeMessageBody(IoBuffer in, Charset charset)
			throws PackageException {

		// get cmdId and process it
		int position = in.position();
		long cmdId = in.getUnsignedInt();
		log.debug("IoBuffer.position()========:" + position);
		in.position(position);
		log.debug("cmdId========:" + cmdId);

		// get the message codec for this command ID.
		ICommandCodec bodyMessageCoder = cmdCodecFactory.getCodec(cmdId);
		log.trace("Command codec for command ID {}: {}", cmdId,
				bodyMessageCoder);

		return bodyMessageCoder.decode(in, charset);
	}

}
