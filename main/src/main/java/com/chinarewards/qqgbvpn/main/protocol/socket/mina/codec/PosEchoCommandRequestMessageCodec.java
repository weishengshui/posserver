package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.PosEchoCommandRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.PosEchoCommandResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

/**
 * description：Implements the codec for the message pos echo command.
 * @copyright binfen.cc
 * @projectName main
 * @time 2011-10-20   下午06:41:36
 * @author Seek
 */
public class PosEchoCommandRequestMessageCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {

		// insufficient bytes give, die.
		if (in.remaining() < ProtocolLengths.COMMAND) {
			throw new PackageException();
		}
		long cmdId = in.getUnsignedInt();
		
		// decode data 	
		byte data[] = new byte[in.remaining()];		//TODO in.capacity()-in.position() != in.remaining()
		if(data != null && data.length != 0){
			in.get(data);
		}
		
		// reconstruct message.
		PosEchoCommandRequestMessage requestMessage = new PosEchoCommandRequestMessage();
		requestMessage.setData(data);
		
		
		log.trace("PosEchoCommandRequestMessage:"+requestMessage);
		return requestMessage;
	}
	
	/**
	 * description：mock pos test use it!
	 * @param bodyMessage
	 * @param charset
	 * @return
	 * @time 2011-9-22   下午07:23:35
	 * @author Seek
	 */
	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {

		PosEchoCommandRequestMessage msg = (PosEchoCommandRequestMessage) bodyMessage;

		// prepare buffer
		int bufLength = 0;
		if (msg.getData() != null) {
			bufLength += msg.getData().length;
		}
		
		IoBuffer buf = IoBuffer.allocate(ProtocolLengths.COMMAND
				+ ProtocolLengths.ECHO_COMMAND_RESULT + bufLength);

		// encode data
		// command ID
		buf.putUnsignedInt(msg.getCmdId());
		// data - optional
		if (msg.getData() != null && bufLength > 0) {
			buf.put(msg.getData());
		}
		
		log.trace("PosEchoCommandRequestMessage:", msg);
		// return result
		return buf.array();
	}
	
}
