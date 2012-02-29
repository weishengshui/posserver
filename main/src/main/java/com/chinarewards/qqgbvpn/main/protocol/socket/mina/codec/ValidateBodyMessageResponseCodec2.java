package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;
import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ValidateResponseMessage2;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

public class ValidateBodyMessageResponseCodec2 implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * description：mock pos test use it!
	 * @param bodyMessage
	 * @param charset
	 * @return
	 * @time 2011-9-22   下午07:23:35
	 * @author Seek
	 */
	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		log.debug("validate response message encode2");
		ValidateResponseMessage2 message = new ValidateResponseMessage2();
		
		long cmdId = in.getUnsignedInt();
		
		long qqws_resultcode = in.getUnsignedInt();
		long qqvalidate_resultstatus = in.getUnsignedInt();
		int validate_count = in.getUnsignedShort();
		String first_posId = null;
		String prev_posId = null;
		
		String resultName = null;
		String resultExplain = null;
		String currentTime = null;
		String useTime = null;
		String validTime = null;
		
		Date first_validate_time = null;
		Date prev_validate_time = null;
		
		//获取
		byte remains[] = new byte[in.capacity()-in.position()];
		in.get(remains);
		String remainsStr = new String(remains);
		
		int begin = 0;
		int end = 0;
		
		if( (end = remainsStr.indexOf(CmdConstant.SEPARATOR, begin)) != -1){
			first_posId = remainsStr.substring(begin, end);
		}
		begin = end+1;
		if( (end = remainsStr.indexOf(CmdConstant.SEPARATOR, begin)) != -1){
			prev_posId = remainsStr.substring(begin, end);
		}
		begin = end+1;
		if( (end = remainsStr.indexOf(CmdConstant.SEPARATOR, begin)) != -1){
			resultName = remainsStr.substring(begin, end);
		}
		begin = end+1;
		
		if( (end = remainsStr.indexOf(CmdConstant.SEPARATOR, begin)) != -1){
			resultExplain = remainsStr.substring(begin, end);
		}
		begin = end+1;
		
		if( (end = remainsStr.indexOf(CmdConstant.SEPARATOR, begin)) != -1){
			currentTime = remainsStr.substring(begin, end);
		}
		begin = end+1;
		
		if( (end = remainsStr.indexOf(CmdConstant.SEPARATOR, begin)) != -1){
			useTime = remainsStr.substring(begin, end);
		}
		begin = end+1;
		
		if( (end = remainsStr.indexOf(CmdConstant.SEPARATOR, begin)) != -1){
			validTime = remainsStr.substring(begin, end);
		}
		begin = end+1;
		
		message.setCmdId(cmdId);
		message.setQqvalidate_resultstatus(qqvalidate_resultstatus);
		message.setQqws_resultcode(qqws_resultcode);
		message.setValidate_count(validate_count);
		message.setPrev_posId(prev_posId);
		message.setFirst_posId(first_posId);
		message.setResultName(resultName);
		message.setResultExplain(resultExplain);
		message.setCurrentTime(currentTime);
		message.setUseTime(useTime);
		message.setValidTime(validTime);
		message.setFirst_validate_time(first_validate_time);
		message.setPrev_validate_time(prev_validate_time);
		
		log.trace("ValidateResponseMessage:{}", message);
		return message;
	}

	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {
		log.debug("validate response message encode");
		ValidateResponseMessage2 responseMessage = (ValidateResponseMessage2) bodyMessage;

		long cmdId = responseMessage.getCmdId();
		int validateCount = responseMessage.getValidate_count();
		long qqvalidate_resultstatus = responseMessage.getQqvalidate_resultstatus();
		long qqws_resultcode = responseMessage.getQqws_resultcode();
		
		String posId = "";

		StringBuffer buffer = new StringBuffer();
		buffer.append(responseMessage.getResultName()==null?"":responseMessage.getResultName()).append(
				CmdConstant.SEPARATOR);
		buffer.append(responseMessage.getResultExplain()==null?"":responseMessage.getResultExplain()).append(
				CmdConstant.SEPARATOR);
		buffer.append(responseMessage.getCurrentTime()==null?"":responseMessage.getCurrentTime()).append(
				CmdConstant.SEPARATOR);
		buffer.append(responseMessage.getUseTime()==null?"":responseMessage.getUseTime()).append(
				CmdConstant.SEPARATOR);
		buffer.append(responseMessage.getValidTime()==null?"":responseMessage.getValidTime()).append(
				CmdConstant.SEPARATOR);
		
		posId = responseMessage.getFirst_posId()==null?"":responseMessage.getFirst_posId();
		buffer.append(posId);
		//这里定义了如果POSID不够12个字节用'\0'补充
		for(int i = posId.length(); i < 12 ; i++){
			buffer.append(CmdConstant.SEPARATOR);
		}

		posId = responseMessage.getPrev_posId() == null ? "" : responseMessage.getPrev_posId();
		buffer.append(posId);
		//这里定义了如果POSID不够12个字节用'\0'补充
		for (int i = posId.length(); i < 12; i++) {
			buffer.append(CmdConstant.SEPARATOR);
		}
		
		Date first_validate_time = responseMessage.getFirst_validate_time();
		Date prev_validate_time = responseMessage.getPrev_validate_time();

		String tmpStr = buffer.toString();
		log.debug("validate result buffer is ({})",buffer);
		tmpStr = tmpStr.replaceAll("\\\\r\\\\n", String.valueOf(CmdConstant.ENTER));
		byte[] tmp = tmpStr.getBytes(charset);
		log.debug("validate src is ({})",tmpStr);

		byte[] resultByte = new byte[ProtocolLengths.COMMAND
				+ ProtocolLengths.VALIDATE_COUNT
				+ ProtocolLengths.QQWS_RESULTCODE
				+ ProtocolLengths.QQVALIDATE_RESULTSTATUS + tmp.length
				+ ProtocolLengths.FIRST_VALIDATE_TIME
				+ ProtocolLengths.PREV_VALIDATE_TIME];
		
		Tools.putUnsignedInt(resultByte, cmdId, 0);
		Tools.putUnsignedShort(resultByte, validateCount,
				ProtocolLengths.COMMAND);
		Tools.putUnsignedInt(resultByte, qqws_resultcode,
				ProtocolLengths.COMMAND + ProtocolLengths.VALIDATE_COUNT);
		Tools.putUnsignedInt(resultByte, qqvalidate_resultstatus,
				ProtocolLengths.COMMAND + ProtocolLengths.VALIDATE_COUNT
						+ ProtocolLengths.QQWS_RESULTCODE);

		int index = ProtocolLengths.COMMAND + ProtocolLengths.VALIDATE_COUNT
				+ ProtocolLengths.QQWS_RESULTCODE
				+ ProtocolLengths.QQVALIDATE_RESULTSTATUS;
		Tools.putBytes(resultByte, tmp, index);

		index += tmp.length;
		if (first_validate_time != null) {
			Tools.putDate(resultByte, first_validate_time, index);
		} else {
			for (int i = 0; i < ProtocolLengths.PREV_VALIDATE_TIME; i++) {
				resultByte[index + i] = 0;
			}
		}
		index += ProtocolLengths.FIRST_VALIDATE_TIME;
		if (validateCount > 1 && prev_validate_time != null) {
			Tools.putDate(resultByte, prev_validate_time, index);
		} else {
			for (int i = 0; i < ProtocolLengths.PREV_VALIDATE_TIME; i++) {
				resultByte[index + i] = 0;
			}
		}

		log.trace("ValidateResponseMessage2:{}", responseMessage);

		return resultByte;
	}

}
