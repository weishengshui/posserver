package com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;

import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;

/**
 * body message coder
 * 
 * @author huangwei
 * @since 0.1.0
 */
public interface IBodyMessageCoder {

	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException;

	public byte[] encode(ICommand bodyMessage, Charset charset);

}
