package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.protocol.CmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ErrorBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.HeadMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.Message;
import com.chinarewards.qqgbvpn.main.protocol.exception.FormatHeadContentException;
import com.chinarewards.qqgbvpn.main.protocol.exception.FormatPackageContentException;
import com.chinarewards.qqgbvpn.main.protocol.exception.ParseResponseContentException;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

/**
 * description：package foramt/parse
 * @copyright binfen.cc
 * @projectName main
 * @time 2011-9-29   下午03:01:20
 * @author Seek
 */
public final class PackageUtil {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final int flags = 0x0000;
	private static final int ack = 0x20000004;
	
	private CmdCodecFactory cmdCodecFactory;
	
	private PackageHeadCodec packageHeadCodec;
	
	public PackageUtil(PackageHeadCodec packageHeadCodec, CmdCodecFactory cmdCodecFactory){
		this.packageHeadCodec = packageHeadCodec;
		this.cmdCodecFactory = cmdCodecFactory;
	}
	
	/**
	 * description：build package head bytes
	 * @param bodySize
	 * @return
	 * @time 2011-9-23   下午02:22:19
	 * @author Seek
	 */
	public final byte[] formatHeadContent(long currentSequence, byte[] bodys) 
				throws FormatHeadContentException {
		try{
			HeadMessage headMessage = new HeadMessage();
			headMessage.setAck(ack);
			headMessage.setChecksum(0);
			headMessage.setFlags(flags);
			headMessage.setMessageSize(ProtocolLengths.HEAD + bodys.length);
			headMessage.setSeq( currentSequence);
			
			logger.debug("buildHeadMessage():");
			logger.debug("messageSize:"+headMessage.getMessageSize());
			logger.debug("sequence:"+headMessage.getSeq());
			
			byte[] heads = packageHeadCodec.encode(headMessage);
			
			logger.debug("heads bytes:"+Arrays.toString(heads) + ", length="+heads.length);
			logger.debug("bodys bytes:"+Arrays.toString(bodys) + ", length="+bodys.length);
			
			byte[] packageBytes = new byte[heads.length + bodys.length];
			System.arraycopy(heads, 0, packageBytes, 0, heads.length);
			System.arraycopy(bodys, 0, packageBytes, heads.length, bodys.length);
			
			//replace checkSum
			logger.debug("src requestBytes:"+Arrays.toString(packageBytes) + 
					", length=" + packageBytes.length);
			int checkSumVal = Tools.checkSum(packageBytes, packageBytes.length);
			logger.debug("request calculate Checksum=" + checkSumVal);
			Tools.putUnsignedShort(packageBytes, checkSumVal, 10);
			logger.debug("add checkSum requestBytes:"+Arrays.toString(packageBytes) +
					", length="+packageBytes.length);
			
			
			byte[] result = new byte[ProtocolLengths.HEAD];
			System.arraycopy(packageBytes, 0, result, 0, result.length);
			
			return result;
		}catch(Throwable e){
			throw new FormatHeadContentException(e);
		}
	}
	
	/**
	 * description：build package bytes
	 * @param bodySize
	 * @return
	 * @time 2011-9-23   下午02:22:19
	 * @author Seek
	 */
	public final byte[] formatPackageContent(long currentSequence, byte[] bodys) 
				throws FormatPackageContentException {
		try{
			byte[] heads = this.formatHeadContent(currentSequence, bodys);
			byte[] packagebytes = new byte[heads.length + bodys.length];
			System.arraycopy(heads, 0, packagebytes, 0, heads.length);
			System.arraycopy(bodys, 0, packagebytes, heads.length, bodys.length);
			return packagebytes;
		}catch(Throwable e){
			throw new FormatPackageContentException(e);
		}
	}
	
	/**
	 * description：解析接收内容
	 * @param responseBytes 响应内容
	 * @return
	 * @throws ParseResponseMessageException
	 * @time 2011-9-23   下午03:25:23
	 * @author Seek
	 */
	public final Message parseResponseContent(byte[] responseBytes, Charset charset) 
				throws ParseResponseContentException {
		try{
			logger.debug("parseResponseMessage run...");
			
			Message message = new Message();
			IoBuffer ioBuff = IoBuffer.wrap(responseBytes);
			//读取头部
			logger.debug("decode packageHead...");
			HeadMessage headMessage = packageHeadCodec.decode(ioBuff);
			
			//check package checksum
			int checksum = headMessage.getChecksum();
			logger.debug("get checksum="+checksum);
			
			Tools.putUnsignedShort(responseBytes, 0, 10);
			
			int checkSumTmp = Tools.checkSum(responseBytes, responseBytes.length);
			logger.debug("calculate Checksum="+checkSumTmp);
			if(checksum != checkSumTmp){
				logger.debug("server checksum != native checksum");
				throw new ParseResponseContentException(
						"response checksum error!  receive Checksum="+checksum+
						", calculate Checksum="+checkSumTmp);
			}
			
			// read commandId
			ioBuff.position(ProtocolLengths.HEAD);		//skip message head
			long commandId  = ioBuff.getUnsignedInt();	//读取commandId
			ICommand bodyMessage = null;
			logger.debug("read cmdId="+commandId);
			if(CmdConstant.ERROR_CMD_ID == commandId){
				logger.debug("cmdId is CmdConstant.ERROR_CMD_ID!");
				long errorMessageCode = ioBuff.getUnsignedInt();		//读取errorCode
				logger.debug("error message code:"+errorMessageCode);
				
				ErrorBodyMessage errorBodyMessage = new ErrorBodyMessage();
				errorBodyMessage.setErrorCode(errorMessageCode);
				bodyMessage = errorBodyMessage;
			}else {
				ICommandCodec codec = cmdCodecFactory.getCodec(commandId);
				
				ioBuff.position(ProtocolLengths.HEAD);		//skip message head
				bodyMessage = codec.decode(ioBuff, charset);
			}
			
			message.setHeadMessage(headMessage);
			message.setBodyMessage(bodyMessage);
			
			logger.debug("headMessage:" + message.getHeadMessage());
			logger.debug("bodyMessage:" + message.getBodyMessage());
			return message;
		}catch(Throwable e){
			throw new ParseResponseContentException(e);
		}
	}
	
}
