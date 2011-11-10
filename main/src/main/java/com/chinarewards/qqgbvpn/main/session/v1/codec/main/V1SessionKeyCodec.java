/**
 * 
 */
package com.chinarewards.qqgbvpn.main.session.v1.codec.main;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import com.chinarewards.qqgbvpn.main.encoder.IUUIDEncoder;
import com.chinarewards.qqgbvpn.main.encoder.UUIDEncoderImpl;
import com.chinarewards.qqgbvpn.main.encoder.UUIDPatternException;
import com.chinarewards.qqgbvpn.main.session.CodecException;
import com.chinarewards.qqgbvpn.main.session.v1.V1SessionKey;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class V1SessionKeyCodec {

	IUUIDEncoder uuidCode = new UUIDEncoderImpl();
	/**
	 * Encode the session key as byte stream.
	 * 
	 * @param key
	 *            the key to encode
	 * @return the encoded byte stream.
	 * @throws CodecException
	 */
	public byte[] encode(Object key) throws CodecException {

		V1SessionKey k = (V1SessionKey) key;

		try {
			return uuidCode.encode(k.getKey());
//				return k.getKey().getBytes("UTF-8");
//		} catch (UnsupportedEncodingException e) {
//				throw new CodecException(e);
		} catch (UUIDPatternException e) {
			throw new CodecException(e);
		}

	}

	/**
	 * Decode the raw byte streams as session key object.
	 * 
	 * @param bytes
	 * @return
	 * @throws CodecException
	 */
	public V1SessionKey decode(byte[] bytes) throws CodecException {
		String key;
		try {
			key=uuidCode.decode(bytes);
		} catch (UUIDPatternException e) {
			throw new CodecException(e);
		}
//		key = new String(bytes, Charset.forName("UTF-8"));
		return new V1SessionKey(key);

	}

}
