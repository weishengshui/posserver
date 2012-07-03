package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.QQMeishiResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

public class QQMeishiBodyMessageResponseCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * description：mock pos test use it!
	 * @param bodyMessage
	 * @param charset
	 * @return
	 * @time 2012-3-2
	 * @author Harry
	 */
	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		log.debug("QQmeishi response message encode");
		QQMeishiResponseMessage message = new QQMeishiResponseMessage();
		
		long cmdId = in.getUnsignedInt();
		long serverErrorCode = in.getUnsignedInt();
		long qqwsErrorCode = in.getUnsignedInt();
		long result = in.getUnsignedInt();
		byte forcePwdNextAction = in.get();
		Date xactTime = null;
		String title = null;
		String tip = null;
		String password = null;
		
		byte[] xactTimeBytes = new byte[ProtocolLengths.CR_DATE_LENGTH];
		in.get(xactTimeBytes);
		
		int titleLen = in.getUnsignedShort();
		if(titleLen > 0){
			byte[] titleBytes = new byte[titleLen];
			in.get(titleBytes);
			title = new String(titleBytes, charset);
		}
		
		int tipLen = in.getUnsignedShort();
		if(tipLen > 0){
			byte[] tipBytes = new byte[tipLen];
			in.get(tipBytes);
			tip = new String(tipBytes, charset);
		}
		
		int passwordLen = in.getUnsignedShort();
		if(passwordLen > 0){
			byte[] passwordBytes = new byte[passwordLen];
			in.get(passwordBytes);
			password = new String(passwordBytes, charset);
		}
		
		message.setCmdId(cmdId);
		message.setServerErrorCode(serverErrorCode);
		message.setQqwsErrorCode(qqwsErrorCode);
		message.setResult(result);
		message.setForcePwdNextAction(forcePwdNextAction);
		message.setPassword(password);
		message.setTitle(title);
		message.setTip(tip);
		message.setXactTime(xactTime);
		
		log.trace("QQmeishiResponseMessage:{}", message);
		return message;
	}

	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {
		log.debug("QQmeishi response message encode");
		QQMeishiResponseMessage responseMessage = (QQMeishiResponseMessage) bodyMessage;

		long cmdId = responseMessage.getCmdId();
		long serverErrorCode = responseMessage.getServerErrorCode();
		long qqwsErrorCode = responseMessage.getQqwsErrorCode();
		long result = responseMessage.getResult();
		byte forcePwdNextAction = responseMessage.getForcePwdNextAction();
		Date xactTime = responseMessage.getXactTime();
		String title = responseMessage.getTitle();
		String tip = responseMessage.getTip();
		String password = responseMessage.getPassword();
		
		int byteLen = ProtocolLengths.COMMAND
				+ ProtocolLengths.QQMEISHI_SERVER_ERROR
				+ ProtocolLengths.QQMEISHI_QQWS_ERROR
				+ ProtocolLengths.QQMEISHI_RESULT
				+ ProtocolLengths.FORCE_PWD_NEXT_ACTION
				+ ProtocolLengths.CR_DATE_LENGTH + ProtocolLengths.POSNETSTRLEN;
		// title
		int titleLen = (title == null) ? 0 : title.length();
		byte[] titleBytes = null;
		if(titleLen > 0){
			titleBytes = title.getBytes(charset);
			titleLen = titleBytes.length;
		}
		byteLen += titleLen;
		//tip
		byteLen += ProtocolLengths.POSNETSTRLEN;
		int tipLen = (tip == null) ? 0 : tip.length();
		byte[] tipBytes = null;
		if(tipLen > 0){
			
			tipBytes = tip.getBytes(charset);
			tipLen = tipBytes.length;
		}
		byteLen += tipLen;
		//password
		byteLen += ProtocolLengths.POSNETSTRLEN;
		int passwordLen = (password == null) ? 0 : password.length();
		byte[] passwordBytes = null;
		if(passwordLen > 0){
			passwordBytes = password.getBytes(charset);
			passwordLen = passwordBytes.length;
		}
		byteLen += passwordLen;
		//to result bytes
		byte[] resultByte = new byte[byteLen];
		int index = 0;
		Tools.putUnsignedInt(resultByte, cmdId, index);
		index += ProtocolLengths.COMMAND;
		
		//serverErrorCode
		Tools.putUnsignedInt(resultByte, serverErrorCode, index);
		index += ProtocolLengths.QQMEISHI_SERVER_ERROR;
		
		//qqwsErrorCode
		Tools.putUnsignedInt(resultByte, qqwsErrorCode, index);
		index += ProtocolLengths.QQMEISHI_QQWS_ERROR;
		
		//result
		Tools.putUnsignedInt(resultByte, result, index);
		index += ProtocolLengths.QQMEISHI_RESULT;
		
		//forcePwdNextAction
		resultByte[index] = forcePwdNextAction;
		index += ProtocolLengths.FORCE_PWD_NEXT_ACTION;
		
		// xact time
		if(xactTime != null){
			Calendar ca = Calendar.getInstance();
			ca.setTime(xactTime);
			Tools.putDate(resultByte, ca, index);
		}else{
			for (int i = 0; i < ProtocolLengths.CR_DATE_LENGTH; i++){
				resultByte[index + i] = 0;
			}
		}
		index += ProtocolLengths.CR_DATE_LENGTH;
		
		//title
		Tools.putUnsignedShort(resultByte, titleLen, index);
		index += ProtocolLengths.POSNETSTRLEN;
		if(titleLen > 0){
			Tools.putBytes(resultByte, titleBytes, index);
			index += titleBytes.length;
		}
		
		//tip
		Tools.putUnsignedShort(resultByte, tipLen, index);
		index += ProtocolLengths.POSNETSTRLEN;
		if(tipLen > 0){
			log.debug("tipBytes.length="+tipBytes.length);
			log.debug("index="+index);
			Tools.putBytes(resultByte, tipBytes, index);
			index += tipBytes.length;
		}
		
		//password
		Tools.putUnsignedShort(resultByte, passwordLen, index);
		index += ProtocolLengths.POSNETSTRLEN;
		if(passwordLen > 0){
			Tools.putBytes(resultByte, passwordBytes, index);
			index += passwordBytes.length;
		}

		log.trace("QQmeishiResponseMessage:{}", responseMessage);

		return resultByte;
	}

}
