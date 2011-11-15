package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.protocol.CmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.cmd.HeadMessage;

public class MessageDecoder extends CumulativeProtocolDecoder {

	private Logger log = LoggerFactory.getLogger(getClass());

	private Charset charset;

	protected CmdCodecFactory cmdCodecFactory;
	
	/**
	 * The maximum allowed message size (the whole message)
	 */
	private long maxMessageSize = 1024 * 100;

	public MessageDecoder(Charset charset, CmdCodecFactory cmdCodecFactory) {
		this.charset = charset;
		this.cmdCodecFactory = cmdCodecFactory;
	}
	
	
	/**
	 * @return the maxMessageSize
	 */
	public long getMaxMessageSize() {
		return maxMessageSize;
	}

	/**
	 * @param maxMessageSize the maxMessageSize to set
	 */
	public void setMaxMessageSize(long maxMessageSize) {
		if (maxMessageSize <=0) {
			throw new IllegalArgumentException("Maximum message should be larger than zero");
		}
		this.maxMessageSize = maxMessageSize;
	}



	/**
	 * 
	 * @see http 
	 *      ://mina.apache.org/report/trunk/apidocs/org/apache/mina/filter/codec
	 *      /CumulativeProtocolDecoder.html for the usage.
	 */
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		log.debug("MessageDecoder.doDecode() invoked");
		log.trace("Mina session ID: " + session.getId());
		log.debug("buffer.remaining() ={}",in.remaining());
		// TODO make the '96' be configurable.
		CodecUtil.hexDumpForLogging(log, in, 96);

		ProtocolMessageDecoder decoder = new ProtocolMessageDecoder(
				cmdCodecFactory);
		ProtocolMessageDecoder.Result result = decoder.decode(in, charset);

		// close the session if message to big.
		boolean sessionClosed = killTooBigMessage(session, in, result);
		// return false since we don't need data as the session is closed
		// by killTooBigMessage()
		if (sessionClosed) return false;
		
		// act according to the result.
		if (!result.isMoreDataRequired()) {
			Object parsedMessage = result.getMessage();
			log.trace(
					"Number of bytes remained in buffer after parsing one message: {}",
					in.remaining());
			if (parsedMessage == null) {
				log.warn(
						"Internal error: No message is returned by {} even it reports a message is parsed",
						decoder.getClass());
				return true;
			} else {
				log.debug("Message processed completed. Message: {}",
						parsedMessage);
				// write the data.
				out.write(parsedMessage);
				return true;
			}
		} else {
			log.trace("More raw data is required to decode");
			// we do not have sufficient bytes to parse the message, return
			// 'false' to request Mina to do more.
			return false;
		}

	}

	/**
	 * Closes the session if message size exceed the threshold.
	 * 
	 * @param session
	 * @param result
	 * @return return <code>true</code> if this function has closed the 
	 * session, <code>false</code> otherwise.
	 */
	protected boolean killTooBigMessage(IoSession session, IoBuffer in,
			ProtocolMessageDecoder.Result result) {
		
		// there is nothing we can do if the header is unknown
		if (result.getHeader() == null) {
			return false;
		}
		
		HeadMessage header = result.getHeader();
		if (header.getMessageSize() > getMaxMessageSize()) {
			log.debug(
					"Received header indicates a message size of {} bytes, which exceed the threshold {}, session will be closed",
					header.getMessageSize(), getMaxMessageSize());
			session.close(true);	// close immediately
			return true;
		}
		
		return false;

	}

}