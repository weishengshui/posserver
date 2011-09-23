package com.chinarewards.qqgbpvn.testing.lab.parent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.mina.core.buffer.IoBuffer;

import com.chinarewards.qqgbpvn.testing.context.TestContext;
import com.chinarewards.qqgbpvn.testing.exception.BuildBodyMessageException;
import com.chinarewards.qqgbpvn.testing.exception.BuildHeadMessageException;
import com.chinarewards.qqgbpvn.testing.exception.ParseResponseMessageException;
import com.chinarewards.qqgbpvn.testing.exception.RunTaskException;
import com.chinarewards.qqgbpvn.testing.exception.SendMessageException;
import com.chinarewards.qqgbpvn.testing.util.SocketTools;
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
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * description：build package head bytes
	 * @param bodySize
	 * @return
	 * @time 2011-9-23   下午02:22:19
	 * @author Seek
	 */
	protected byte[] buildHeadMessage(JavaSamplerContext context, int bodySize) throws BuildHeadMessageException {
		HeadMessage headMessage = new HeadMessage();
		headMessage.setAck(ack);
		headMessage.setChecksum(0);
		headMessage.setFlags(flags);
		headMessage.setMessageSize(ProtocolLengths.HEAD + bodySize);
		headMessage.setSeq(TestContext.getBasePosConfig().getSequence());
		
		byte[] heads = TestContext.getPackageHeadCodec().encode(headMessage);
		return heads;
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
			sampleResult = runTask(context);
		}catch(Throwable e){
			e.printStackTrace();
		}finally {
			//自增流水号
			TestContext.incrementSequence();
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
	protected final Message sendMessage(JavaSamplerContext context, byte[] bodys) throws SendMessageException {
		try{
			StringBuffer packageContent = new StringBuffer();
			byte[] heads = buildHeadMessage(context, bodys.length);
			packageContent.append(heads);
			packageContent.append(bodys);
			
			byte[] packageBytes = packageContent.toString().getBytes(TestContext.getCharset());
			
			//replace checkSum
			int checkSumVal = Tools.checkSum(packageBytes, packageBytes.length);
			Tools.putUnsignedShort(packageBytes, checkSumVal, 10);
			System.out.println("request calculate Checksum=" + checkSumVal);
			
			//发送包到pos server
			byte[] posInitResponseBytes = SocketTools.sendPackageToServer(packageBytes);
			
			//将package解析并封装成Message
			Message message = parseResponseMessage(posInitResponseBytes);
			
			//将当前的bodyMessage存入线程   以便下次请求使用
			TestContext.getBasePosConfig().setLastResponseBodyMessage(message.getBodyMessage());
			return message;
		}catch(Throwable e){
			throw new SendMessageException(e);
		}
	}
	
	/**
	 * description：解析接收内容
	 * @param posInitResponseBytes 响应内容
	 * @return
	 * @throws ParseResponseMessageException
	 * @time 2011-9-23   下午03:25:23
	 * @author Seek
	 */
	private Message parseResponseMessage(byte[] posInitResponseBytes) 
				throws ParseResponseMessageException {
		try{
			Message message = new Message();
			IoBuffer ioBuff = IoBuffer.wrap(posInitResponseBytes);
			//读取头部
			HeadMessage headMessage = TestContext.getPackageHeadCodec().decode(ioBuff);
			
			//check package checksum
			int checksum = headMessage.getChecksum();
			Tools.putUnsignedShort(posInitResponseBytes, 0, 10);
			int checkSumTmp = Tools.checkSum(posInitResponseBytes, posInitResponseBytes.length);
			if(checksum != checkSumTmp){
				throw new ParseResponseMessageException(
						"response checksum error!  receive Checksum="+checksum+", calculate Checksum="+checkSumTmp);
			}
			
			// read commandId
			ioBuff.position(ProtocolLengths.HEAD);		//skip message head
			long commandId  = ioBuff.getUnsignedInt();	//读取commandId
			ICommand bodyMessage = null;
			if(CmdConstant.ERROR_CMD_ID == commandId){
				long errorMessageCode = ioBuff.getUnsignedInt();		//读取errorCode
				
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
			
			System.out.println("headMessage:" + message.getHeadMessage());
			System.out.println("bodyMessage:" + message.getBodyMessage());
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
