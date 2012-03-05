package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.QQmeishiResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ValidateResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

public class QQmeishiBodyMessageResponseCodec implements ICommandCodec {

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
		QQmeishiResponseMessage message = new QQmeishiResponseMessage();
		
		long cmdId = in.getUnsignedInt();
		long result = in.getUnsignedInt();
		byte forcePwdNextAction = in.get();
		Date xactTime = null;
		String title = null;
		String tip = null;
		String password = null;
		
		//FIXME decode crdate
		byte[] xactTimeBytes = new byte[ProtocolLengths.CR_DATE];
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
		message.setResult(result);
		message.setForce_pwd_next_action(forcePwdNextAction);
		message.setPassword(password);
		message.setTitle(title);
		message.setTip(tip);
		message.setXact_time(xactTime);
		
		log.trace("QQmeishiResponseMessage:{}", message);
		return message;
	}

	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {
		log.debug("QQmeishi response message encode");
		QQmeishiResponseMessage responseMessage = (QQmeishiResponseMessage) bodyMessage;

		long cmdId = responseMessage.getCmdId();
		long result = responseMessage.getResult();
		byte forcePwdNextAction = responseMessage.getForce_pwd_next_action();
		Date xactTime = responseMessage.getXact_time();
		String title = responseMessage.getTitle();
		String tip = responseMessage.getTip();
		String password = responseMessage.getPassword();
		
		//FIXME 11
		int byteLen = ProtocolLengths.COMMAND + ProtocolLengths.QQMEISHI_RESULT
				+ ProtocolLengths.FORCE_PWD_NEXT_ACTION
				+ ProtocolLengths.CR_DATE + ProtocolLengths.POSNETSTRLEN;

		// title
		int titleLen = (title == null) ? 0 : title.length();
		byte[] titleBytes = null;
		if(titleLen > 0){
			title = title.replaceAll("\\\\r\\\\n",
					String.valueOf(CmdConstant.ENTER));
			byteLen += title.length();
			titleBytes = title.getBytes(charset);
		}
		
		//tip
		byteLen += ProtocolLengths.POSNETSTRLEN;
		int tipLen = (tip == null) ? 0 : tip.length();
		byte[] tipBytes = null;
		if(tipLen > 0){
			tip = tip.replaceAll("\\\\r\\\\n",
					String.valueOf(CmdConstant.ENTER));
			byteLen += tip.length();
			tipBytes = tip.getBytes(charset);
		}
		
		//password
		byteLen += ProtocolLengths.POSNETSTRLEN;
		int passwordLen = (password == null) ? 0 : password.length();
		byte[] passwordBytes = null;
		if(passwordLen > 0){
			password = password.replaceAll("\\\\r\\\\n",
					String.valueOf(CmdConstant.ENTER));
			byteLen += password.length();
			passwordBytes = password.getBytes(charset);
		}
		
		byte[] resultByte = new byte[byteLen];
		int index = 0;
		Tools.putUnsignedInt(resultByte, cmdId, index);
		index += ProtocolLengths.COMMAND;
		
		Tools.putUnsignedInt(resultByte, result, index);
		index += ProtocolLengths.QQMEISHI_RESULT;
		
		resultByte[index] = forcePwdNextAction;
		index += ProtocolLengths.FORCE_PWD_NEXT_ACTION;
		
		if(xactTime != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(xactTime);
			Tools.putDate(resultByte, calendar, index);
			
		}else{
			for (int i = 0; i < ProtocolLengths.CR_DATE; i++){
				resultByte[index + i] = 0;
			}
		}
		index += ProtocolLengths.CR_DATE;
		
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
			Tools.putBytes(resultByte, tipBytes, index);
			index += tipBytes.length;
		}
		
		//password
		Tools.putUnsignedShort(resultByte, passwordLen, index);
		index += ProtocolLengths.POSNETSTRLEN;
		if(passwordLen > 0){
			Tools.putBytes(titleBytes, passwordBytes, index);
			index += passwordBytes.length;
		}

		log.trace("QQmeishiResponseMessage:{}", responseMessage);

		return resultByte;
	}

}
