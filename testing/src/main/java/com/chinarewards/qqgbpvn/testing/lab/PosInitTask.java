package com.chinarewards.qqgbpvn.testing.lab;

import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.chinarewards.qqgbpvn.testing.context.TestContext;
import com.chinarewards.qqgbpvn.testing.exception.BuildBodyMessageException;
import com.chinarewards.qqgbpvn.testing.exception.RunTaskException;
import com.chinarewards.qqgbpvn.testing.lab.parent.PosTask;
import com.chinarewards.qqgbvpn.main.protocol.SimpleCmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.InitRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.Message;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * description：POS机Init
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-22   下午06:02:53
 * @author Seek
 */
public final class PosInitTask extends PosTask {
	
	@Override
	public SampleResult runTask(JavaSamplerContext context) throws RunTaskException {
		logger.debug("log is ok~");
		
		SampleResult res = new SampleResult();
		res.sampleStart();	//开始任务
		
		try{
			byte[] bodys =  buildBodyMessage(context);
			Message message = super.sendMessage(context, bodys);
			
			res.setSuccessful(true);
		}catch(Throwable e){
			res.setSuccessful(false);
			throw new RunTaskException(e);
		}finally{
			res.sampleEnd();	//结束任务
		}
		return res;
	}
	
	@Override
	protected byte[] buildBodyMessage(JavaSamplerContext context) throws BuildBodyMessageException {
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
	
}
