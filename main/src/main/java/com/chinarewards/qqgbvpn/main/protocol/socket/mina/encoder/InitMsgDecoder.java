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

import com.chinarewards.qqgbvpn.main.protocol.socket.InitMsg2;
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

		if (in.remaining() >= 32) {

			System.out.println("Do processing");
			
			// SEQ: Sequence number
			System.out.println("Read sequence");
			long seq = in.getUnsignedInt();	// 4 byte
			System.out.println("in.remaining() = " + in.remaining());

			// ACK: Sequence number
			System.out.println("Read ACK");
			long ack = in.getUnsignedInt();	// 4 byte
			System.out.println("in.remaining() = " + in.remaining());

			// Flags
			System.out.println("Read Flags");
			int flags = in.getUnsignedShort();	// 2 byte
			System.out.println("in.remaining() = " + in.remaining());

			// Checksum
			System.out.println("Read Checksum");
			int checksum = in.getUnsignedShort();	// 2 byte
			System.out.println("in.remaining() = " + in.remaining());

			// message size
			System.out.println("Read message size");
			long length = in.getUnsignedInt(); // XXX	// 4 byte
			System.out.println("in.remaining() = " + in.remaining());

			// command ID
			System.out.println("Read command ID");
			long cmdId = in.getUnsignedInt();	// 4 byte
			System.out.println("in.remaining() = " + in.remaining());

			// POS ID
			byte[] posIdRaw = new byte[ProtocolLengths.POS_ID];
			in.get(posIdRaw);
			String posId = new String(posIdRaw);
			System.out.println("in.remaining() = " + in.remaining());

			
			log.info("SEQ={}, ACK={}, Flags={}, Checksum={}, MsgLength={}, CmdId={}, PosID={}",
					new Object[] { seq, ack, flags, checksum, length, cmdId, posId } );
			
			InitMsg2 m = new InitMsg2(seq, ack, flags, checksum, length, posId);
			out.write(m);

			return true;

		} else {
			// not yet done
			return false;
		}

	}

}
