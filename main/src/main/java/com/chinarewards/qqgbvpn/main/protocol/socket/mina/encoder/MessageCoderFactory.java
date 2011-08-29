package com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	public MessageCoderFactory(Injector injector){
		this(charsetDefault,injector);
	}
	
	public MessageCoderFactory(Charset charset,Injector injector){
		this.charset = charset;
		this.injector = injector;
		encoder = new MessageEncoder(charset,this.injector);
		decoder = new MessageDecoder(charset,this.injector);
	}
	
	
	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		log.debug("getDecoder invoked");
		return decoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		log.debug("getDecoder invoked");
		return encoder;
	}

	//---------------------------------------------------//
	public Charset getCharset() {
		return charset;
	}

	
}
