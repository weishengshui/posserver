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
		if (in.remaining() < ProtocolLengths.COMMAND + 2) {
			throw new PackgeException(
					"validate packge message body error, body message is :"
							+ in);
		}
		long cmdId = in.getUnsignedInt();
		byte[] requestByte = new byte[in.remaining()];
		in.get(requestByte);

		int grouponIdEnd = -1;
		int grouponVCodeEnd = -1;
		boolean errorFlag = false;
		for(int i=0;i<requestByte.length;i++){
			if(requestByte[i] == 0){
				if(grouponIdEnd == -1){
					grouponIdEnd = i;
				}else if(grouponVCodeEnd == -1){
					grouponVCodeEnd = i;
				}else{
					errorFlag = true;
				}
			}
		}
		if(requestByte[requestByte.length -1] != 0){
			errorFlag = true;
		}
		if(errorFlag){
			throw new PackgeException(
			"validate packge message body error, body message");
		}
		String grouponId = null;
		String grouponVCode = null;
		if(grouponIdEnd != 0){
			byte[] tmp = new byte[grouponIdEnd];
			for(int i=0;i<grouponIdEnd;i++){
				tmp[i] = requestByte[i];
			}
			grouponId = new String(tmp, charset);
		}
		if(grouponVCodeEnd - grouponIdEnd != 1){
			byte[] tmp = new byte[requestByte.length - grouponIdEnd -2];
			for(int i=0;i<requestByte.length - grouponIdEnd -2;i++){
				tmp[i] = requestByte[i+grouponIdEnd+1];
			}
			grouponVCode = new String(tmp, charset);
		}
		
		message.setCmdId(cmdId);
		message.setGrouponId(grouponId);
		message.setGrouponVCode(grouponVCode);
		log.debug("validate message request:cmdId is ({}) , grouponId is ({}), grouponVCode is ({})",new Object[]{cmdId,grouponId,grouponVCode});
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
				CmdConstant.SEPARATOR);
		buffer.append(responseMessage.getResultExplain()).append(
				CmdConstant.SEPARATOR);
		buffer.append(
				Tools.dateToString(DATE_FORMAT, responseMessage
						.getCurrentTime())).append(CmdConstant.SEPARATOR);
		buffer.append(
				Tools.dateToString(DATE_FORMAT, responseMessage.getUseTime()))
				.append(CmdConstant.SEPARATOR);
		buffer
				.append(
						Tools.dateToString(DATE_FORMAT, responseMessage
								.getValidTime())).append(CmdConstant.SEPARATOR);

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
