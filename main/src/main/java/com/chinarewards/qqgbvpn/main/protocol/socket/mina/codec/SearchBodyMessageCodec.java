package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.SearchRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

/**
 * Search BodyMessage Coder
 * 
 * @author huangwei
 *
 */
public class SearchBodyMessageCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		log.debug("search message decode");
		SearchRequestMessage message = new SearchRequestMessage();
		if (in.remaining() < ProtocolLengths.COMMAND + ProtocolLengths.PAGE
				+ ProtocolLengths.SIZE) {
			throw new PackageException(
					"search packge message body error, body message is :" + in);
		}
		long cmdId = in.getUnsignedInt();
		int page = in.getUnsignedShort();
		int size = in.getUnsignedShort();
		
		message.setCmdId(cmdId);
		message.setPage(page);
		message.setSize(size);
		
		log.trace("SearchRequestMessage:{}", message);
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
		log.debug("search message encode");

		SearchRequestMessage requestMessage = (SearchRequestMessage) bodyMessage;
		long cmdId = requestMessage.getCmdId();
		int  page = requestMessage.getPage();
		int  size = requestMessage.getSize();

		byte[] resultByte = new byte[ProtocolLengths.COMMAND + ProtocolLengths.PAGE
		             		+ ProtocolLengths.SIZE];

		Tools.putUnsignedInt(resultByte, cmdId, 0);
		Tools.putUnsignedShort(resultByte, page, ProtocolLengths.COMMAND);
		Tools.putUnsignedShort(resultByte, size, ProtocolLengths.COMMAND + ProtocolLengths.PAGE);
		
		log.trace("SearchRequestMessage:{}", requestMessage);
		return resultByte;
	}

}
