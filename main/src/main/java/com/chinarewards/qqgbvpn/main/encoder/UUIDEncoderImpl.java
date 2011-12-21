package com.chinarewards.qqgbvpn.main.encoder;

/**
 * The implementation to {@link IUUIDEncoder}
 * 
 * @author yanxin
 * @since 2011-11-08
 */
public class UUIDEncoderImpl implements IUUIDEncoder {

	@Override
	public byte[] encode(String uuid) throws UUIDPatternException {
		// To lower
		uuid = uuid.toLowerCase();
		// validate pattern
		char[] chars = uuid.toCharArray();
		byte[] result = new byte[16];
		int length = chars.length;
		if (length != 32) {
			throw new UUIDPatternException("The length of uuid is not 32!");
		}
		for (int i = 0; i < length; i = i + 2) {
			byte first = getByteFromChar(chars[i]);
			// Change 4 bit to left.
			first = (byte) (first << 4);
			byte second = getByteFromChar(chars[i + 1]);
			byte sum = (byte) (first | second);
			result[i / 2] = sum;
		}

		return result;
	}

	@Override
	public String decode(byte[] array) throws UUIDPatternException {
		StringBuffer uuid = new StringBuffer();
		for (byte b : array) {
			// split a byte to 2 bytes and then convert to 2 chars.
			byte first = (byte) ((b >> 4) & 0x0f);
			uuid.append(getCharFromByte(first));
			byte second = (byte) (b & 0x0f);
			uuid.append(getCharFromByte(second));
		}
		return uuid.toString();
	}

	private byte getByteFromChar(char c) throws UUIDPatternException {
		byte b = 0;
		if (c >= 48 && c <= 57) {
			b = (byte) (c - 48);
		} else if (c >= 97 && c <= 102) {
			b = (byte) (c - 87);
		} else {
			throw new UUIDPatternException(
					"UUID string contains illegal char when encoding!");
		}
		return b;
	}

	private char getCharFromByte(byte b) throws UUIDPatternException {
		char c = 0;
		if (b >= 0 && b <= 9) {
			c = (char) (b + 48);
		} else if (b >= 10 && b <= 15) {
			c = (char) (b + 87);
		} else {
			throw new UUIDPatternException(
					"UUID string contains illegal char when decoding!");
		}

		return c;
	}

}
