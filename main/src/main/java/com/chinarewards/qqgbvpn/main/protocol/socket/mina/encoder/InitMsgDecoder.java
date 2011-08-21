/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.protocol.socket.InitMsg;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.TransportMessage;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class InitMsgDecoder extends CumulativeProtocolDecoder {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {

		byte[] head = new byte[(int) TransportMessage.HEADER_LENGTH];

		System.out.println("InitMsgDecoder.doDecode invoked");
		System.out.println("in.remaining() = " + in.remaining());

		if (in.remaining() >= 24) {

			// read header
			in.get(head);	// 2 byte

			// packet length
			long length = in.getLong(); // XXX	// 4 byte

			// serial number
			long serial = in.getLong();	// 4 byte

			// command ID
			long cmdId = in.getUnsignedInt();	// 2 byte

			// pos ID
			byte[] posIdRaw = new byte[ProtocolLengths.POS_ID];
			in.get(posIdRaw);
			String posId = new String(posIdRaw);

			log.debug("length={}, serial={}, cmdId={}, posIdRaw={}, posId={}", new Object[] {
					length, serial, cmdId, posIdRaw, posId });
			
			InitMsg m = new InitMsg(24, serial, posId);
			out.write(m);

			return true;

		} else {
			// not yet done
			return false;
		}

	}

}
