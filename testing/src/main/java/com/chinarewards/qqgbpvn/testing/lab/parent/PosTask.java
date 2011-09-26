package com.chinarewards.qqgbpvn.testing.lab.parent;

import java.util.Arrays;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbpvn.testing.context.TestContext;
import com.chinarewards.qqgbpvn.testing.exception.BuildBodyMessageException;
import com.chinarewards.qqgbpvn.testing.exception.BuildHeadMessageException;
import com.chinarewards.qqgbpvn.testing.exception.ParseResponseMessageException;
import com.chinarewards.qqgbpvn.testing.exception.RunTaskException;
import com.chinarewards.qqgbpvn.testing.exception.SendMessageException;
import com.chinarewards.qqgbpvn.testing.util.SocketUtils;
import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.protocol.SimpleCmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ErrorBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.HeadMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.Message;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * description：pos 任务
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-23   下午02:06:46
 * @author Seek
 */
public abstract class PosTask extends AbstractJavaSamplerClient {
	
	private static final int flags = 0x0000;
	private static final int ack = 0x20000004;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * description：build package head bytes
	 * @param bodySize
	 * @return
	 * @time 2011-9-23   下午02:22:19
	 * @author Seek
	 */
	protected byte[] buildHeadMessage(JavaSamplerContext context, int bodySize) 
				throws BuildHeadMessageException {
		try{
			HeadMessage headMessage = new HeadMessage();
			headMessage.setAck(ack);
			headMessage.setChecksum(0);
			headMessage.setFlags(flags);
			headMessage.setMessageSize(ProtocolLengths.HEAD + bodySize);
			headMessage.setSeq(TestContext.getBasePosConfig().getSequence());
			
			logger.debug("buildHeadMessage():");
			logger.debug("messageSize:"+headMessage.getMessageSize());
			logger.debug("sequence:"+headMessage.getSeq());
			
			byte[] heads = TestContext.getPackageHeadCodec().encode(headMessage);
			return heads;
		}catch(Throwable e){
			throw new BuildHeadMessageException(e);
		}	
	}
	
	/**
	 * description：run a jmeter test	{is not allow overwrite}
	 * @param context
	 * @return
	 * @time 2011-9-23   下午02:23:00
	 * @author Seek
	 */
	@Override
	public final SampleResult runTest(JavaSamplerContext context) {
		SampleResult sampleResult = null;
		try{
			//如果没有init basePosConfig,那么不做
			if(TestContext.getBasePosConfig() == null){
				return null;
			}
			sampleResult = runTask(context);
		}catch(Throwable e){
			logger.error(e.getMessage(), e);
		}finally {
			//自增流水号
			TestContext.incrementSequence();
			logger.debug("the sequence auto +1");
		}
		return sampleResult;
	}
	
	/**
	 * description：发送消息到pos server
	 * @param context 
	 * @param bodys BodyMessage content
	 * @return pos server Response Message include{HeadMessage and BodyMessage}
	 * @throws SendMessageException
	 * @time 2011-9-23   下午05:54:59
	 * @author Seek
	 */
	protected final Message sendMessage(JavaSamplerContext context, byte[] bodys) 
				throws SendMessageException {
		try{
			logger.debug("sendMessage run...");

			byte[] heads = buildHeadMessage(context, bodys.length);
			logger.debug("heads bytes:"+Arrays.toString(heads) + ", length="+heads.length);
			logger.debug("bodys bytes:"+Arrays.toString(bodys) + ", length="+bodys.length);
			
			byte[] requestBytes = new byte[heads.length + bodys.length];
			System.arraycopy(heads, 0, requestBytes, 0, heads.length);
			System.arraycopy(bodys, 0, requestBytes, heads.length, bodys.length);
			
			//replace checkSum
			logger.debug("src requestBytes:"+Arrays.toString(requestBytes) + 
					", length=" + requestBytes.length);
			int checkSumVal = Tools.checkSum(requestBytes, requestBytes.length);
			logger.debug("request calculate Checksum=" + checkSumVal);
			Tools.putUnsignedShort(requestBytes, checkSumVal, 10);
			logger.debug("add checkSum requestBytes:"+Arrays.toString(requestBytes) +
					", length="+requestBytes.length);
			
			//发送包到pos server
			byte[] responseBytes = SocketUtils.sendPackageToServer(requestBytes);
			logger.debug("responseBytes:"+Arrays.toString(responseBytes) +
					", length="+responseBytes.length);
			//将package解析并封装成Message
			Message message = parseResponseMessage(responseBytes);
			
			//将当前的bodyMessage存入线程   以便下次请求使用
			TestContext.getBasePosConfig().setLastResponseBodyMessage(message.getBodyMessage());
			return message;
		}catch(Throwable e){
			throw new SendMessageException(e);
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
	private Message parseResponseMessage(byte[] responseBytes) 
				throws ParseResponseMessageException {
		try{
			logger.debug("parseResponseMessage run...");
			
			Message message = new Message();
			IoBuffer ioBuff = IoBuffer.wrap(responseBytes);
			//读取头部
			logger.debug("decode packageHead...");
			HeadMessage headMessage = TestContext.getPackageHeadCodec().decode(ioBuff);
			
			//check package checksum
			int checksum = headMessage.getChecksum();
			logger.debug("get checksum="+checksum);
			
			Tools.putUnsignedShort(responseBytes, 0, 10);
			
			int checkSumTmp = Tools.checkSum(responseBytes, responseBytes.length);
			logger.debug("calculate Checksum="+checkSumTmp);
			if(checksum != checkSumTmp){
				logger.debug("server checksum != native checksum");
				throw new ParseResponseMessageException(
						"response checksum error!  receive Checksum="+checksum+", calculate Checksum="+checkSumTmp);
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
				SimpleCmdCodecFactory cmdCodecFactory = TestContext.getCmdCodecFactory();
				ICommandCodec codec = cmdCodecFactory.getCodec(commandId);
				
				ioBuff.position(ProtocolLengths.HEAD);		//skip message head
				bodyMessage = codec.decode(ioBuff, TestContext.getCharset());
			}
			
			message.setHeadMessage(headMessage);
			message.setBodyMessage(bodyMessage);
			
			logger.debug("headMessage:" + message.getHeadMessage());
			logger.debug("bodyMessage:" + message.getBodyMessage());
			return message;
		}catch(Throwable e){
			throw new ParseResponseMessageException(e);
		}
	}
	
	/**
	 * description：执行任务
	 * @param context
	 * @return
	 * @time 2011-9-23   下午02:18:14
	 * @author Seek
	 */
	protected abstract SampleResult runTask(JavaSamplerContext context) throws RunTaskException;
	
	/**
	 * description：build package body bytes
	 * @return
	 * @time 2011-9-23   下午02:22:37
	 * @author Seek
	 */
	protected abstract byte[] buildBodyMessage(JavaSamplerContext context) throws BuildBodyMessageException;
	
}
