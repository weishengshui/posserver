/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol;

import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * 
 * 
 * @author Cyril
 * 
 */
public interface CmdCodecFactory {

	public ICommandCodec getCodec(long commandId);

}
