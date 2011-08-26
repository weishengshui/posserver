package com.chinarewards.qqgbvpn.main.protocol.socket.message;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * body message coder
 * 
 * @author huangwei
 *
 */
public interface IBodyMessageCoder {

	public IBodyMessage decode(IoBuffer in,Charset charset);
	
	public byte[] encode(IBodyMessage bodyMessage,Charset charset);
}
