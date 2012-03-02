//package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;
//
//import java.nio.charset.Charset;
//import java.util.Date;
//
//import org.apache.mina.core.buffer.IoBuffer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.chinarewards.qqgbvpn.common.Tools;
//import com.chinarewards.qqgbvpn.main.exception.PackageException;
//import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
//import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
//import com.chinarewards.qqgbvpn.main.protocol.cmd.QQmeishiResponseMessage;
//import com.chinarewards.qqgbvpn.main.protocol.cmd.ValidateResponseMessage;
//import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
//
//public class QQmeishiBodyMessageResponseCodec implements ICommandCodec {
//
//	private Logger log = LoggerFactory.getLogger(getClass());
//	
//	/**
//	 * description：mock pos test use it!
//	 * @param bodyMessage
//	 * @param charset
//	 * @return
//	 * @time 2012-3-2
//	 * @author Harry
//	 */
//	@Override
//	public ICommand decode(IoBuffer in, Charset charset)
//			throws PackageException {
//		log.debug("QQmeishi response message encode");
//		QQmeishiResponseMessage message = new QQmeishiResponseMessage();
//		
//		long cmdId = in.getUnsignedInt();
//		int result = in.getUnsignedShort();
//		String resultName = null;
//		String resultExplain = null;
//		String currentTime = null;
//		String useTime = null;
//		String validTime = null;
//		
//		//获取
//		byte remains[] = new byte[in.capacity()-in.position()];
//		in.get(remains);
//		String remainsStr = new String(remains);
//		
//		int begin = 0;
//		int end = 0;
//		
//		if( (end = remainsStr.indexOf(CmdConstant.SEPARATOR, begin)) != -1){
//			resultName = remainsStr.substring(begin, end);
//		}
//		begin = end+1;
//		
//		if( (end = remainsStr.indexOf(CmdConstant.SEPARATOR, begin)) != -1){
//			resultExplain = remainsStr.substring(begin, end);
//		}
//		begin = end+1;
//		
//		if( (end = remainsStr.indexOf(CmdConstant.SEPARATOR, begin)) != -1){
//			currentTime = remainsStr.substring(begin, end);
//		}
//		begin = end+1;
//		
//		if( (end = remainsStr.indexOf(CmdConstant.SEPARATOR, begin)) != -1){
//			useTime = remainsStr.substring(begin, end);
//		}
//		begin = end+1;
//		
//		if( (end = remainsStr.indexOf(CmdConstant.SEPARATOR, begin)) != -1){
//			validTime = remainsStr.substring(begin, end);
//		}
//		begin = end+1;
//		
//		message.setCmdId(cmdId);
//		message.setResult(result);
//		message.setResultName(resultName);
//		message.setResultExplain(resultExplain);
//		message.setCurrentTime(currentTime);
//		message.setUseTime(useTime);
//		message.setValidTime(validTime);
//		
//		log.trace("ValidateResponseMessage:{}", message);
//		return message;
//	}
//
//	@Override
//	public byte[] encode(ICommand bodyMessage, Charset charset) {
//		log.debug("QQmeishi response message encode");
//		QQmeishiResponseMessage responseMessage = (QQmeishiResponseMessage) bodyMessage;
//
//		long cmdId = responseMessage.getCmdId();
//		long result = responseMessage.getResult();
//		byte force_pwd_next_action = responseMessage.getForce_pwd_next_action();
//		Date xact_time = responseMessage.getXact_time();
//		String title = responseMessage.getTitle();
//		String tip = responseMessage.getTip();
//		String password = responseMessage.getPassword();
//		
//		int byteLen = ProtocolLengths.COMMAND + ProtocolLengths.QQMEISHI_RESULT
//				+ ProtocolLengths.FORCE_PWD_NEXT_ACTION;
//		
//		byte[] resultByte = new byte[ProtocolLengths.COMMAND
//		             				+ ProtocolLengths.RESULT + tmp.length];
//		
//		StringBuffer buffer = new StringBuffer();
//		buffer.append(title == null ? "" : title).append(CmdConstant.SEPARATOR);
//		buffer.append(tip == null ? "" : tip).append(CmdConstant.SEPARATOR);
//		buffer.append(password == null ? "" : password).append(CmdConstant.SEPARATOR);
//
//		String tmpStr = buffer.toString();
//		log.debug("validate result buffer is ({})",buffer);
//		tmpStr = tmpStr.replaceAll("\\\\r\\\\n", String.valueOf(CmdConstant.ENTER));
//		byte[] tmp = tmpStr.getBytes(charset);
//		log.debug("validate src is ({})",tmpStr);
//
//		byte[] resultByte = new byte[ProtocolLengths.COMMAND
//				+ ProtocolLengths.RESULT + tmp.length];
//		Tools.putUnsignedInt(resultByte, cmdId, 0);
//		Tools.putUnsignedShort(resultByte, result, ProtocolLengths.COMMAND);
//		Tools.putBytes(resultByte, tmp, ProtocolLengths.COMMAND
//				+ ProtocolLengths.RESULT);
//
//
//		log.trace("ValidateResponseMessage:{}", responseMessage);
//
//		return resultByte;
//	}
//
//}
