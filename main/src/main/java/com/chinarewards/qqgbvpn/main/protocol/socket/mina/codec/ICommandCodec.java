package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;

import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;

/**
 * Defines the interface of a command message coder/decoder.
 * 
 * @author huangwei
 * @since 0.1.0
 */
public interface ICommandCodec {

	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException;

	public byte[] encode(ICommand bodyMessage, Charset charset);

}
