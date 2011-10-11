/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

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
		
		private final long oweLength;

		private final Object message;
		
		private final HeadMessage header;

		/**
		 * 
		 * @param moreDataRequired
		 * @param oweLength
		 * @param headMessage
		 * @param message
		 */
		public Result(boolean moreDataRequired, long oweLength,
				HeadMessage headMessage, Object message) {
			this.moreDataRequired = moreDataRequired;
			this.oweLength = oweLength;
			this.message = message;
			this.header = headMessage;
		}

		/**
		 * Returns parsed header, if parsed, no matter the header is correct
		 * or not.
		 * 
		 * @return
		 */
		public HeadMessage getHeader() {
			return header;
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
		 * Returns the expected length. The value is valid only if 
		 * {@link #isMoreDataRequired()} returns <code>true</code>.
		 * 
		 * @return the expectedLength
		 */
		public long getOweLength() {
			return oweLength;
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
	public Result decode(IoBuffer in, Charset charset) {

		log.trace("IoBuffer remaining bytes: {}, current position: {}",
				in.remaining(), in.position());

		// check length, it must greater than head length
		if (in.remaining() >= ProtocolLengths.HEAD) {
			
			int start = in.position();
			
			// parse message header
			log.trace("Parsing message header..");
			PackageHeadCodec headCodec = new PackageHeadCodec();
			HeadMessage header = headCodec.decode(in);	// parse
			log.trace("- message header: {}", header);
			log.trace("- IoBuffer remaining (for body): {}", in.remaining());

			// check length
			// XXX suspicious code, may lead to unwanted error code 3: invalid
			// size
			// XXX should wait until the whole package is received.
//			if (messageSize != ProtocolLengths.HEAD + in.remaining()) {
//				ErrorBodyMessage bodyMessage = new ErrorBodyMessage();
//				bodyMessage.setErrorCode(CmdConstant.ERROR_MESSAGE_SIZE_CODE);
//				return new Result(true, null);
//			}
			if (ProtocolLengths.HEAD + in.remaining() < header.getMessageSize()) {
				// more data is required.
				long owe = header.getMessageSize() - ProtocolLengths.HEAD + in.remaining();
				log.trace("More bytes are required to parse the complete message (still need {} bytes)",
						header.getMessageSize() - ProtocolLengths.HEAD + in.remaining());
				in.position(start);	// restore the original position.
				return new Result(true, owe, header, null);
			}

			//
			// validate the checksum.
			//
			
			// byteTmp: raw bytes
			int calculatedChecksum = 0;
			{
				// reset to original position;
				in.position(start);
				byte[] byteTmp = new byte[(int)header.getMessageSize()];	// XXX possible truncation
				// important! just consume the required number of bytes
				in.get(byteTmp);
//				CodecUtil.debugRaw(log, byteTmp);	// some debug output
				Tools.putUnsignedShort(byteTmp, 0, 10);

				// calculate the checksum
				calculatedChecksum = Tools.checkSum(byteTmp, byteTmp.length);
			}

			// validate the checksum. if not correct, return an error response.
			if (calculatedChecksum != header.getChecksum()) {
				ErrorBodyMessage bodyMessage = new ErrorBodyMessage();
				bodyMessage.setErrorCode(CmdConstant.ERROR_CHECKSUM_CODE);
				Message message = new Message(header, bodyMessage);
				log.trace(
						"Received message has invalid checksum (calc: 0x{}, actual: 0x{})",
						Integer.toHexString(calculatedChecksum),
						Integer.toHexString(header.getChecksum()));
				return new Result(false, 0, header, message);
			}

			// really decode the command message.
			in.position(start + ProtocolLengths.HEAD);
			ICommand bodyMessage = null;
			try {
				bodyMessage = this.decodeMessageBody(in, charset);
				// a message is completely decoded.
				Message message = new Message(header, bodyMessage);
				return new Result(false, 0, header, message);
			} catch (Throwable e) {
				log.trace("Unexpected error when decoding message", e);
				ErrorBodyMessage errorBodyMessage = new ErrorBodyMessage();
				errorBodyMessage.setErrorCode(CmdConstant.ERROR_MESSAGE_CODE);
				Message message = new Message(header, errorBodyMessage);
				return new Result(false, 0, header, message);
			}

		} else {
			
			long owe = ProtocolLengths.HEAD - in.remaining();
			log.trace("More bytes are required to parse the header message (still need {} bytes)",
					owe);
			return new Result(true, owe, null, null);
			
		}

	}

	protected ICommand decodeMessageBody(IoBuffer in, Charset charset)
			throws PackageException {

		// get cmdId and process it
		int position = in.position();
		long cmdId = in.getUnsignedInt();
		in.position(position);
		log.debug("cmdId: {}", cmdId);

		// get the message codec for this command ID.
		ICommandCodec bodyMessageCoder = cmdCodecFactory.getCodec(cmdId);
		log.trace("Command codec for command ID {}: {}", cmdId,
				bodyMessageCoder);

		return bodyMessageCoder.decode(in, charset);
	}

}
