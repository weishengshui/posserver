package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

import org.apache.commons.configuration.Configuration;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.protocol.CmdCodecFactory;

public class MessageCoderFactory implements ProtocolCodecFactory {

	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * default Charset is gb2312
	 */
	private static final Charset charsetDefault = Charset.forName("gb2312");

	private Charset charset;

	private ProtocolEncoder encoder;

	private ProtocolDecoder decoder;

	protected final CmdCodecFactory cmdCodecFactory;
	
	protected final Configuration configuration;

	public MessageCoderFactory(CmdCodecFactory cmdCodecFactory, Configuration configuration) {
		this(charsetDefault, cmdCodecFactory, configuration);
	}

	public MessageCoderFactory(Charset charset, CmdCodecFactory cmdCodecFactory, Configuration configuration) {
		this.charset = charset;
		this.cmdCodecFactory = cmdCodecFactory;
		this.configuration = configuration;
		encoder = new MessageEncoder(charset, this.cmdCodecFactory);
		decoder = new MessageDecoder(charset, this.cmdCodecFactory, this.configuration);
		
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		log.debug("getDecoder invoked");
		return decoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		log.debug("getEncoder invoked");
		return encoder;
	}

	// ---------------------------------------------------//
	public Charset getCharset() {
		return charset;
	}

}
