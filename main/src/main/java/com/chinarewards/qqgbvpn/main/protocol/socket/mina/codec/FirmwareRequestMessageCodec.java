package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;
import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpgradeRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

/**
 * firmware request codec
 * <p>
 * 
 * XXX should separate into two codec classes.
 * 
 * @author kmtong
 * @since 0.1.0 2011-0915
 */
public class FirmwareRequestMessageCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());

	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		log.debug("FirmwareRequest message decode");
		FirmwareUpgradeRequestMessage message = new FirmwareUpgradeRequestMessage();
		if (in.remaining() < ProtocolLengths.COMMAND + ProtocolLengths.POS_ID) {
			throw new PackageException(
					"FirmwareRequest packge message body error, body message is :"
							+ in);
		}

		// read from buffer
		long cmdId = in.getUnsignedInt();
		byte[] posIdBuf = new byte[ProtocolLengths.POS_ID];
		in.get(posIdBuf);

		// construct message
		message.setPosId(Tools.byteToString(posIdBuf, charset));

		log.debug(
				"FirmwareRequest message request: cmdId is ({}) , posid is ({})",
				new Object[] { cmdId, posIdBuf });
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
		FirmwareUpgradeRequestMessage requestMessage = (FirmwareUpgradeRequestMessage) bodyMessage;
		long cmdId = requestMessage.getCmdId();
		String posId = requestMessage.getPosId();
		
		byte[] resultByte = new byte[ProtocolLengths.COMMAND + ProtocolLengths.POS_ID];
		
		Tools.putUnsignedInt(resultByte, cmdId, 0);
		Tools.putBytes(resultByte, posId.getBytes(), ProtocolLengths.COMMAND);
		
		return resultByte;
	}
	
}
