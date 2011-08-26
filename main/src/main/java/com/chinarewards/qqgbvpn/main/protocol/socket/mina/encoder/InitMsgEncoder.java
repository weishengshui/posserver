/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.chinarewards.qqgbvpn.main.protocol.socket.InitMsgResult;
import com.chinarewards.qqgbvpn.main.protocol.socket.MsgV2;

/**
 * @author Cyril
 * 
 */
public class InitMsgEncoder implements ProtocolEncoder {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.mina.filter.codec.ProtocolEncoder#dispose(org.apache.mina.
	 * core.session.IoSession)
	 */
	@Override
	public void dispose(IoSession session) throws Exception {
		// TODO Auto-generated method stub

		System.out.println("dispose()");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.mina.filter.codec.ProtocolEncoder#encode(org.apache.mina.core
	 * .session.IoSession, java.lang.Object,
	 * org.apache.mina.filter.codec.ProtocolEncoderOutput)
	 */
	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {

		InitMsgResult msg = (InitMsgResult) message;

		int msgLength = (int) MsgV2.LENGTH + 2 + 8;

		IoBuffer buf = IoBuffer.allocate(msgLength);
		// SEQ
		buf.putUnsignedInt(2);
		// ACK
		buf.putUnsignedInt(0);
		// FLAGS - not used now
		buf.putUnsignedShort(0);
		// Checksum
		buf.putUnsignedShort(0x1234);
		// message length
		buf.putUnsignedInt(msgLength);
		// the result
		buf.putUnsignedInt(msgLength);
		// the challenge
		buf.put(msg.getChallenge());

		// a must (from tutorial)
		buf.flip();
		out.write(buf);

	}

}
