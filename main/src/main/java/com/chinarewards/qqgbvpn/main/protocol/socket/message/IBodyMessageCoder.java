package com.chinarewards.qqgbvpn.main.protocol.socket.message;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;

import com.chinarewards.qqgbvpn.main.exception.PackgeException;

/**
 * body message coder
 * 
 * @author huangwei
 *
 */
public interface IBodyMessageCoder {

	public IBodyMessage decode(IoBuffer in,Charset charset) throws PackgeException;
	
	public byte[] encode(IBodyMessage bodyMessage,Charset charset);
}
