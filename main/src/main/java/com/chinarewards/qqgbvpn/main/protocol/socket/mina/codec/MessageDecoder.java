package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.protocol.CmdCodecFactory;

public class MessageDecoder extends CumulativeProtocolDecoder {

	private Logger log = LoggerFactory.getLogger(getClass());

	private Charset charset;

	protected CmdCodecFactory cmdCodecFactory;

	public MessageDecoder(Charset charset, CmdCodecFactory cmdCodecFactory) {
		this.charset = charset;
		this.cmdCodecFactory = cmdCodecFactory;
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

		// TODO make the '96' be configurable.
		CodecUtil.hexDumpForLogging(log, in, 96);
		
		ProtocolMessageDecoder decoder = new ProtocolMessageDecoder(
				cmdCodecFactory);
		ProtocolMessageDecoder.Result result = decoder.decode(in, charset);

		// act according to the result.
		if (!result.isMoreDataRequired()) {
			Object parsedMessage = result.getMessage();
			log.trace("Number of bytes remained in buffer after parsing one message: {}", in.remaining());
			if (parsedMessage == null) {
				log.warn(
						"Internal error: No message is returned by {} even it reports a message is parsed",
						decoder.getClass());
				return true;
			} else {
				log.debug("Message decoded successful. Message: {}", parsedMessage);
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

}