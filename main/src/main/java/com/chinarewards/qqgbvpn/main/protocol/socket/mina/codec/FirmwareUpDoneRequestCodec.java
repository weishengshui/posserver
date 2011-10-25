package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpDoneRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

/**
 * description：POS固件升级请求解码
 * @copyright binfen.cc
 * @projectName main
 * @time 2011-9-15   上午11:11:28
 * @author Seek
 */
public class FirmwareUpDoneRequestCodec implements ICommandCodec {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		
		log.debug("FirmwareUpDone message decode");
		FirmwareUpDoneRequestMessage message = new FirmwareUpDoneRequestMessage();
		
		if (in.remaining() < ProtocolLengths.COMMAND + ProtocolLengths.POS_ID) {
			throw new PackageException(
					"login packge message body error, body message is :" + in);
		}
		
		// read from buffer
		long cmdId = in.getUnsignedInt();
		byte[] posIdBuf = new byte[ProtocolLengths.POS_ID];
		in.get(posIdBuf);
		
		// construct message
		message.setCmdId(cmdId);
		message.setPosId(Tools.byteToString(posIdBuf, charset));
		
		log.trace("FirmwareUpDoneRequestMessage:{}", message);
		return message;
	}
	
	/**
	 * description：mock pos test use it!
	 * @param bodyMessage
	 * @param charset
	 * @return
	 * @time 2011-9-29   下午12:56:18
	 * @author Seek
	 */
	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {
		//...
		log.debug("FirmwareUpDone message encode");
		FirmwareUpDoneRequestMessage requestMessage = (FirmwareUpDoneRequestMessage) bodyMessage;
		long cmdId = requestMessage.getCmdId();
		String posId = requestMessage.getPosId();

		byte[] resultByte = new byte[ProtocolLengths.COMMAND + ProtocolLengths.POS_ID];
		
		Tools.putUnsignedInt(resultByte, cmdId, 0);
		Tools.putBytes(resultByte, posId.getBytes(), ProtocolLengths.COMMAND);
		
		log.trace("FirmwareUpDoneRequestMessage:{}", requestMessage);
		return resultByte;
	}

}
