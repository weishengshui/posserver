package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.InitRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.InitResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

/**
 * init message coder
 * 
 * @author huangwei
 *
 */
public class InitMessageCodec implements ICommandCodec {
	
	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		log.debug("init request message decode");
		InitRequestMessage message = new InitRequestMessage();
		log.debug("in.remaining()={}", in.remaining());
		if (in.remaining() != ProtocolLengths.COMMAND + ProtocolLengths.POS_ID) {
			throw new PackageException(
					"login packge message body error, body message is :" + in);
		}
		long cmdId = in.getUnsignedInt();
		
		// decode POS ID, Pay attention to middle \0
		byte[] posid = new byte[ProtocolLengths.POS_ID];
		in.get(posid);
		
		// reconstruct message.
		message.setCmdId(cmdId);
		message.setPosId(Tools.byteToString(posid, charset));
		
		log.debug("init message request:cmdId is ({}) , posid is ({})",new Object[]{cmdId,posid});
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
		log.debug("init request message encode");
		InitRequestMessage initRequestMessage = (InitRequestMessage) bodyMessage;
		long cmdId = initRequestMessage.getCmdId();
		String posId = initRequestMessage.getPosId();

		byte[] resultByte = new byte[ProtocolLengths.COMMAND + ProtocolLengths.POS_ID];
		
		Tools.putUnsignedInt(resultByte, cmdId, 0);
		Tools.putBytes(resultByte, posId.getBytes(), ProtocolLengths.COMMAND);
		
		return resultByte;
	}

}
