package com.chinarewards.qqgbvpn.main.encoder;

/**
 * This interface provides methods to transfer between uuid and byte array.
 * 
 * As we know, uuid has 32 chars.If you use it as string, you need prepare
 * 32*2*8 bit for it. Otherwise, if you convert it to byte array. You just need
 * 32*1*8 bit. If every char just contains 0-9&a-f(just need 4 bit), it means
 * you can use one byte (8 bit) to contain two chars. So the result is you just
 * need 16*1*8 bit memory space.
 * 
 * @author yanxin
 * @since 2011-11-08
 */
public interface IUUIDEncoder {

	/**
	 * Encoding specified uuid to byte array.
	 * 
	 * @param uuid
	 * @return
	 * @throws UUIDPatternException
	 *             The char of specified uuid should between 0-9 & a-f, if not
	 *             would throw it.
	 */
	public byte[] encode(String uuid) throws UUIDPatternException;

	/**
	 * Decoding specified byte array to a uuid string.
	 * 
	 * @param array
	 * @return
	 * @throws UUIDPatternException
	 *             If char after decoding was not between 0-9&a-f.
	 */
	public String decode(byte[] array) throws UUIDPatternException;
}
