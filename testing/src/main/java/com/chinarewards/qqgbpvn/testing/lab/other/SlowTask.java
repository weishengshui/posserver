package com.chinarewards.qqgbpvn.testing.lab.other;

import java.util.Arrays;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.chinarewards.qqgbpvn.testing.context.TestContext;
import com.chinarewards.qqgbpvn.testing.exception.BuildBodyMessageException;
import com.chinarewards.qqgbpvn.testing.exception.RunTaskException;
import com.chinarewards.qqgbpvn.testing.exception.SendMessageException;
import com.chinarewards.qqgbpvn.testing.lab.PosTask;
import com.chinarewards.qqgbpvn.testing.util.SocketUtil;
import com.chinarewards.qqgbvpn.main.protocol.SimpleCmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.InitRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.Message;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * description：分次发的的package
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-29   上午10:30:53
 * @author Seek
 */
public class SlowTask extends PosTask {
	
	private static final String SEND_TIMESTAMP = "SEND_TIMESTAMP(unit of time:Second)";
	
	private static final Integer TAIL_BYTES_LEN = 2;
	
	@Override
	public Arguments getDefaultParameters() {
		Arguments arguments = new Arguments();
		arguments.addArgument(SEND_TIMESTAMP, "5");
		return arguments;
	}

	@Override
	protected SampleResult runTask(JavaSamplerContext context)
			throws RunTaskException {
		SampleResult res = new SampleResult();
		res.sampleStart();	//开始任务
		
		try{
			byte[] bodys =  buildBodyMessage(context);
			sendMessage(context, bodys);
			
			res.setSuccessful(true);
		}catch(Throwable e){
			res.setSuccessful(false);
			throw new RunTaskException(e);
		}finally{
			res.sampleEnd();	//结束任务
		}
		return res;
	}
	
	/**
	 * description：only do init request
	 * @param context
	 * @return
	 * @throws BuildBodyMessageException
	 * @time 2011-9-29   上午10:55:31
	 * @author Seek
	 */
	@Override
	protected byte[] buildBodyMessage(JavaSamplerContext context)
			throws BuildBodyMessageException {
		try{
			InitRequestMessage bodyMessage = new InitRequestMessage();
			bodyMessage.setCmdId(CmdConstant.INIT_CMD_ID);
			bodyMessage.setPosId(TestContext.getBasePosConfig().getPosId());
			
			SimpleCmdCodecFactory cmdCodecFactory = TestContext.getCmdCodecFactory();
			ICommandCodec codec = cmdCodecFactory.getCodec(bodyMessage.getCmdId());
			
			byte[] bodys = codec.encode(bodyMessage, TestContext.getCharset());
			return bodys;
		}catch(Throwable e){
			throw new BuildBodyMessageException(e);
		}
	}
	
	@Override
	protected Message sendMessage(JavaSamplerContext context, byte[] bodys)
			throws SendMessageException {
		try{
			logger.debug("sendMessage run...");
			
			byte[] requestBytes = formatPackageContent(context, bodys);
			
			/***************************************************************************/
			byte[] foreBytes = new byte[requestBytes.length - TAIL_BYTES_LEN];
			byte[] tailBytes = new byte[TAIL_BYTES_LEN];
			
			System.arraycopy(requestBytes, 0, foreBytes, 0, foreBytes.length);
			System.arraycopy(requestBytes, foreBytes.length, tailBytes, 0, tailBytes.length);
			
			//发送包到pos server
			logger.debug("send fore bytes...");
			SocketUtil.writeFromOutputStream(TestContext.getBasePosConfig().getSocket().
					getOutputStream(), foreBytes);
			
			Long sendTimestamp = Long.parseLong(context.getParameter(SEND_TIMESTAMP));
			logger.debug("sleep "+sendTimestamp+" Second...");
			Thread.sleep(sendTimestamp);
			
			logger.debug("send tail bytes...");
			SocketUtil.writeFromOutputStream(TestContext.getBasePosConfig().getSocket().
					getOutputStream(), tailBytes);
			
			byte[] responseBytes = SocketUtil.readFromInputStream(TestContext.
					getBasePosConfig().getSocket().getInputStream());
			/***************************************************************************/
			
			logger.debug("responseBytes:"+Arrays.toString(responseBytes) +
					", length="+responseBytes.length);
			//将package解析并封装成Message
			Message message = super.parseResponseContent(context, responseBytes);
			
			//将当前的bodyMessage存入线程   以便下次请求使用
			TestContext.getBasePosConfig().setLastResponseBodyMessage(message.getBodyMessage());
			return message;
		}catch(Throwable e){
			throw new SendMessageException(e);
		}
	}
	
}
