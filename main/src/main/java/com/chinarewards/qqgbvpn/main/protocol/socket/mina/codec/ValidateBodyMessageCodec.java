package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ValidateRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ValidateResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

public class ValidateBodyMessageCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		log.debug("validate request message decode");
		ValidateRequestMessage message = new ValidateRequestMessage();
		if (in.remaining() < ProtocolLengths.COMMAND + 2) {
			throw new PackageException(
					"validate packge message body error, body message is :"
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
					"validate packge message body error, body message");
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
		log
				.debug(
						"validate message request:cmdId is ({}) , grouponId is ({}), grouponVCode is ({})",
						new Object[] { cmdId, grouponId, grouponVCode });
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
		log.debug("validate request message encode");
		ValidateRequestMessage requestMessage = (ValidateRequestMessage) bodyMessage;

		long cmdId = requestMessage.getCmdId();
		String grouponId = requestMessage.getGrouponId();
		String grouponVCode = requestMessage.getGrouponVCode();
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(grouponId==null?"":grouponId).append(
				CmdConstant.SEPARATOR);
		buffer.append(grouponVCode==null?"":grouponVCode).append(
				CmdConstant.SEPARATOR);

		String tmpStr = buffer.toString();
		log.debug("validate result buffer is ({})",buffer);
		tmpStr = tmpStr.replaceAll("\\\\r\\\\n", String.valueOf(CmdConstant.ENTER));
		byte[] tmp = tmpStr.getBytes(charset);
		log.debug("validate src is ({})",tmpStr);

		byte[] resultByte = new byte[ProtocolLengths.COMMAND + tmp.length];
		Tools.putUnsignedInt(resultByte, cmdId, 0);
		Tools.putBytes(resultByte, tmp, ProtocolLengths.COMMAND);
		
		log.debug("validate message encode end ,result byte is ({})",Arrays.toString(resultByte));
		return resultByte;
	}

}
