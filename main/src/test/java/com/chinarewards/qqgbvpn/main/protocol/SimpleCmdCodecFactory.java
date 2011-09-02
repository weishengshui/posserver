/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol;

import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class SimpleCmdCodecFactory implements CmdCodecFactory {

	private final CmdMapping mapping;

	public SimpleCmdCodecFactory(CmdMapping cmdMapping) {
		this.mapping = cmdMapping;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chinarewards.qqgbvpn.main.protocol.CmdCodecFactory#getCodec(long)
	 */
	@Override
	public ICommandCodec getCodec(long commandId) {

		Class<ICommandCodec> codecClazz = (Class<ICommandCodec>) mapping
				.getMapping(commandId);

		if (codecClazz == null)
			return null;

		// return an new instance.
		ICommandCodec codec;
		try {
			codec = codecClazz.newInstance();
			return codec;
		} catch (InstantiationException e) {
			// XXX throws better exception
			throw new RuntimeException("Error creating message codec", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Error creating message codec", e);
		}

	}

}
