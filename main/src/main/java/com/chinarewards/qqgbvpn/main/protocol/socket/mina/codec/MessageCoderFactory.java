package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.protocol.CmdCodecFactory;
import com.google.inject.Injector;

public class MessageCoderFactory implements ProtocolCodecFactory {

	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * default Charset is gb2312
	 */
	private static final Charset charsetDefault = Charset.forName("gb2312");

	private Charset charset;

	private ProtocolEncoder encoder;

	private ProtocolDecoder decoder;

	private Injector injector;
	
	protected final CmdCodecFactory cmdCodecFactory;

	public MessageCoderFactory(Injector injector, CmdCodecFactory cmdCodecFactory) {
		this(charsetDefault, injector, cmdCodecFactory);
	}

	public MessageCoderFactory(Charset charset, Injector injector, CmdCodecFactory cmdCodecFactory) {
		this.charset = charset;
		this.injector = injector;
		this.cmdCodecFactory = cmdCodecFactory;
		encoder = new MessageEncoder(charset, this.injector, cmdCodecFactory);
		decoder = new MessageDecoder(charset, this.injector, cmdCodecFactory);
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
