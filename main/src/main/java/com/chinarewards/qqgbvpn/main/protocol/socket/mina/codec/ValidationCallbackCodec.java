package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ValCallbackRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ValCallbackResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

public class ValidationCallbackCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		log.debug("validationCallback message decode");
		ValCallbackRequestMessage message = new ValCallbackRequestMessage();
		if (in.remaining() < ProtocolLengths.COMMAND + 2) {
			throw new PackageException(
					"validationCallback packge message body error, body message is :"
							+ in);
		}
		long cmdId = in.getUnsignedInt();
		byte[] requestByte = new byte[in.remaining()];
		in.get(requestByte);
		log.debug("requestByte==========:"+Arrays.toString(requestByte));
		int grouponIdEnd = -1;
		int grouponVCodeEnd = -1;
		boolean errorFlag = false;
		for (int i = 0; i < requestByte.length; i++) {
			if (requestByte[i] == 0) {
				if (grouponIdEnd == -1) {
					grouponIdEnd = i;
				} else if (grouponVCodeEnd == -1) {
					grouponVCodeEnd = i;
				} else {
					errorFlag = true;
				}
			}
		}
		if (requestByte[requestByte.length - 1] != 0) {
			errorFlag = true;
		}
		if (errorFlag) {
			throw new PackageException(
					"validationCallback packge message body error, body message");
		}
		String grouponId = null;
		String grouponVCode = null;
		if (grouponIdEnd != 0) {
			byte[] tmp = new byte[grouponIdEnd];
			for (int i = 0; i < grouponIdEnd; i++) {
				tmp[i] = requestByte[i];
			}
			grouponId = new String(tmp, charset);
		}
		if (grouponVCodeEnd - grouponIdEnd != 1) {
			byte[] tmp = new byte[requestByte.length - grouponIdEnd - 2];
			for (int i = 0; i < requestByte.length - grouponIdEnd - 2; i++) {
				tmp[i] = requestByte[i + grouponIdEnd + 1];
			}
			grouponVCode = new String(tmp, charset);
		}

		message.setCmdId(cmdId);
		message.setGrouponId(grouponId);
		message.setGrouponVCode(grouponVCode);
		
		log.trace("ValCallbackRequestMessage:" + message);
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
		throw new UnsupportedOperationException();
	}

}
