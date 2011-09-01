package com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;

import com.chinarewards.qqgbvpn.main.exception.PackgeException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;

/**
 * body message coder
 * 
 * @author huangwei
 *
 */
public interface IBodyMessageCoder {

	public ICommand decode(IoBuffer in,Charset charset) throws PackgeException;
	
	public byte[] encode(ICommand bodyMessage,Charset charset);
}
