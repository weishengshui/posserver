/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import org.apache.mina.core.buffer.IoBuffer;

import com.chinarewards.qqgbvpn.main.session.CodecException;
import com.chinarewards.qqgbvpn.main.session.ISessionKey;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public interface SessionKeyDecoder {

	/**
	 * Decode any session key from the buffer. Caller should make sure the given
	 * IoBuffer contains valid session key.
	 * 
	 * @param in
	 * @return
	 */
	public ISessionKey decode(IoBuffer in) throws CodecException;

}
