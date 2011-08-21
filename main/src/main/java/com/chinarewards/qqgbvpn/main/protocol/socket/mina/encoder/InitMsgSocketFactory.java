/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;


/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class InitMsgSocketFactory implements ProtocolCodecFactory {

	private ProtocolEncoder encoder;

	private ProtocolDecoder decoder;

	public InitMsgSocketFactory(boolean client) {
		if (client) {
			encoder = new InitMsgEncoder();
			decoder = new InitMsgDecoder();
			// decoder = new ImageResponseDecoder();
		} else {
			// encoder = new ImageResponseEncoder();
			encoder = new InitMsgEncoder();
			decoder = new InitMsgDecoder();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.mina.filter.codec.ProtocolCodecFactory#getDecoder(org.apache
	 * .mina.core.session.IoSession)
	 */
	@Override
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		System.out.println("getDecoder invoked");
		return decoder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.mina.filter.codec.ProtocolCodecFactory#getEncoder(org.apache
	 * .mina.core.session.IoSession)
	 */
	@Override
	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		System.out.println("getEncoder invoked");
		return encoder;
	}

}
