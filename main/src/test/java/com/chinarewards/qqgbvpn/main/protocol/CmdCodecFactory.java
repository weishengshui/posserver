/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol;

import com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder.IBodyMessageCoder;

/**
 * 
 * 
 * @author Cyril
 * 
 */
public interface CmdCodecFactory {

	public IBodyMessageCoder getCodec(long commandId);

}
