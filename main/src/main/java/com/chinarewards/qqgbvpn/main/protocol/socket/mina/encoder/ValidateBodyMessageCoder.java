package com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackgeException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.IBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.ValidateRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.ValidateResponseMessage;

public class ValidateBodyMessageCoder implements IBodyMessageCoder {

	private Logger log = LoggerFactory.getLogger(getClass());

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Override
	public IBodyMessage decode(IoBuffer in, Charset charset)
			throws PackgeException {
		log.debug("validate message decode");
		ValidateRequestMessage message = new ValidateRequestMessage();
		if (in.remaining() < ProtocolLengths.COMMAND + 4) {
			throw new PackgeException(
					"validate packge message body error, body message is :"
							+ in);
		}
		long cmdId = in.getUnsignedInt();
		byte[] requestByte = new byte[in.remaining()];
		in.get(requestByte);

		String src = new String(requestByte, charset);
		int postion = src.indexOf(CmdConstant.END_PRIEX);
		if (postion == -1) {
			throw new PackgeException(
					"validate packge message body error, body message");
		}
		String grouponId = src.substring(0, postion);
		src = src.substring(postion + CmdConstant.END_PRIEX.length());
		if (src.indexOf(CmdConstant.END_PRIEX) == -1
				|| src.indexOf(CmdConstant.END_PRIEX) != src.length()
						- CmdConstant.END_PRIEX.length()) {
			throw new PackgeException(
					"validate packge message body error, body message");
		}
		String grouponVCode = src.substring(0, src.length()
				- CmdConstant.END_PRIEX.length());
		message.setCmdId(cmdId);
		message.setGrouponId(grouponId);
		message.setGrouponVCode(grouponVCode);
		return message;
	}

	@Override
	public byte[] encode(IBodyMessage bodyMessage, Charset charset) {
		log.debug("validate message encode");
		ValidateResponseMessage responseMessage = (ValidateResponseMessage) bodyMessage;

		long cmdId = responseMessage.getCmdId();
		int result = responseMessage.getResult();

		StringBuffer buffer = new StringBuffer();
		buffer.append(responseMessage.getResultName()).append(
				CmdConstant.END_PRIEX);
		buffer.append(responseMessage.getResultExplain()).append(
				CmdConstant.END_PRIEX);
		buffer.append(
				Tools.dateToString(DATE_FORMAT, responseMessage
						.getCurrentTime())).append(CmdConstant.END_PRIEX);
		buffer.append(
				Tools.dateToString(DATE_FORMAT, responseMessage.getUseTime()))
				.append(CmdConstant.END_PRIEX);
		buffer
				.append(
						Tools.dateToString(DATE_FORMAT, responseMessage
								.getValidTime())).append(CmdConstant.END_PRIEX);

		byte[] tmp = buffer.toString().getBytes(charset);

		byte[] resultByte = new byte[ProtocolLengths.COMMAND
				+ ProtocolLengths.RESULT + tmp.length];
		Tools.putUnsignedInt(resultByte, cmdId, 0);
		Tools.putUnsignedShort(resultByte, result, ProtocolLengths.COMMAND);
		Tools.putBytes(resultByte, tmp, ProtocolLengths.COMMAND
				+ ProtocolLengths.RESULT);

		return resultByte;
	}

}
