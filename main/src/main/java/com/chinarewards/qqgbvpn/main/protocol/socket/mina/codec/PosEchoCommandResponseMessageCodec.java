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
 * description：Implements the codec for the message pos echo command response.
 * @copyright binfen.cc
 * @projectName main
 * @time 2011-10-20   下午06:53:53
 * @author Seek
 */
public class PosEchoCommandResponseMessageCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {

		// insufficient bytes give, die.
		if (in.remaining() < ProtocolLengths.COMMAND) {
			throw new PackageException();
		}
		long cmdId = in.getUnsignedInt();
		
		short result = in.getUnsigned();
		
		// decode data 
		//获取
		byte data[] = new byte[in.remaining()];	//TODO in.remaining()  ?
		
		if(data != null && data.length != 0){
			in.get(data);
		}
		
		// reconstruct message.
		PosEchoCommandResponseMessage responseMessage = new PosEchoCommandResponseMessage();
		responseMessage.setResult(result);
		responseMessage.setData(data);
		
		return responseMessage;
	}
	
	/**
	 * implements encoding function.
	 */
	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {

		PosEchoCommandResponseMessage msg = (PosEchoCommandResponseMessage) bodyMessage;

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
		// result
		buf.putUnsigned(msg.getResult());
		// data - optional
		if (msg.getData() != null && bufLength > 0) {
			buf.put(msg.getData());
		}

		// return result
		return buf.array();
	}
	
}
