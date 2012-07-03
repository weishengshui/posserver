package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.GetFirmwareFragmentRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

/**
 * Implements the codec for the message get firmware fragment.
 * 
 * @author cyril
 * @since 0.1.0
 */
public class GetFirmwareFragmentRequestMessageCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {

		// insufficient bytes give, die.
		if (in.remaining() < ProtocolLengths.COMMAND + ProtocolLengths.POS_ID
				+ 4 + 4) {
			throw new PackageException();
		}
		long cmdId = in.getUnsignedInt();

		// decode data

		// decode POS ID, Pay attention to middle \0
		byte[] posid = new byte[ProtocolLengths.POS_ID];
		in.get(posid);
		long offset = in.getUnsignedInt();
		long length = in.getUnsignedInt();

		// reconstruct message.
		GetFirmwareFragmentRequestMessage message = new GetFirmwareFragmentRequestMessage(
				Tools.byteToString(posid, charset), offset, length);
		
		log.trace("GetFirmwareFragmentRequestMessage:{}", message);
		return message;
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
		//...
		log.debug("get firmware fragment request message encode");
		GetFirmwareFragmentRequestMessage requestMessage = (GetFirmwareFragmentRequestMessage) bodyMessage;
		long cmdId = requestMessage.getCmdId();
		String posId = requestMessage.getPosId();
		long offset = requestMessage.getOffset();
		long length = requestMessage.getLength();
		
		byte[] resultByte = new byte[ProtocolLengths.COMMAND + ProtocolLengths.POS_ID + 
		                         ProtocolLengths.FIRMWARE_OFFSET + ProtocolLengths.FIRMWARE_LENTH];
		
		Tools.putUnsignedInt(resultByte, cmdId, 0);
		Tools.putBytes(resultByte, posId.getBytes(), ProtocolLengths.COMMAND);
		Tools.putUnsignedInt(resultByte, offset, ProtocolLengths.COMMAND + ProtocolLengths.POS_ID);
		Tools.putUnsignedInt(resultByte, length, ProtocolLengths.COMMAND + ProtocolLengths.POS_ID 
				+ ProtocolLengths.FIRMWARE_OFFSET);
		
		log.trace("GetFirmwareFragmentRequestMessage:{}", requestMessage);

		return resultByte;
	}
	
}
