package com.chinarewards.qqgbpvn.testing.lab.business;

import java.util.Arrays;

import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.chinarewards.qqgbpvn.testing.context.TestContext;
import com.chinarewards.qqgbpvn.testing.exception.BuildBodyMessageException;
import com.chinarewards.qqgbpvn.testing.exception.RunTaskException;
import com.chinarewards.qqgbpvn.testing.lab.PosTask;
import com.chinarewards.qqgbvpn.main.protocol.SimpleCmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ErrorBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.InitResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.LoginRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.Message;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;
import com.chinarewards.qqgbvpn.main.util.HMAC_MD5;

/**
 * description：POS机Login
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-22   下午06:02:53
 * @author Seek
 */
public final class PosLoginTask extends PosTask {

	@Override
	protected SampleResult runTask(JavaSamplerContext context)
			throws RunTaskException {
		SampleResult res = new SampleResult();
		
		res.sampleStart();	//开始任务
		try{
			byte[] bodys =  buildBodyMessage(context);
			Message message = super.sendMessage(context, bodys);
			
			if(message.getBodyMessage() instanceof ErrorBodyMessage){
				res.setSuccessful(false);
			}else{
				res.setSuccessful(true);
			}
		}catch(Throwable e){
			res.setSuccessful(false);
			throw new RunTaskException(e);
		}finally{
			res.sampleEnd();	//结束任务
		}
		return res;
	}

	@Override
	protected byte[] buildBodyMessage(JavaSamplerContext context)
			throws BuildBodyMessageException {
		try{
			logger.debug("PosLoginTask buildBodyMessage() run...");
			LoginRequestMessage bodyMessage = new LoginRequestMessage();
			bodyMessage.setCmdId(CmdConstant.LOGIN_CMD_ID);
			bodyMessage.setPosId(TestContext.getBasePosConfig().getPosId());
			
			
			//get init response challenge value
			InitResponseMessage initResponseMessage = (InitResponseMessage) TestContext.
							getBasePosConfig().getLastResponseBodyMessage();
			
			//build challengeResponse by challenge and secret
			logger.debug("challenge="+Arrays.toString(initResponseMessage.getChallenge())+
						", length="+initResponseMessage.getChallenge().length);
			logger.debug("secret="+TestContext.getBasePosConfig().getSecret());
			byte[] challengeResponse = HMAC_MD5.getSecretContent(initResponseMessage.getChallenge(), 
							TestContext.getBasePosConfig().getSecret());
			logger.debug("challengeResponse="+Arrays.toString(challengeResponse)+
					", length="+challengeResponse.length);
			
			bodyMessage.setChallengeResponse(challengeResponse);
			
			SimpleCmdCodecFactory cmdCodecFactory = TestContext.getCmdCodecFactory();
			ICommandCodec codec = cmdCodecFactory.getCodec(bodyMessage.getCmdId());
			
			byte[] bodys = codec.encode(bodyMessage, TestContext.getCharset());
			return bodys;
		}catch(Throwable e){
			throw new BuildBodyMessageException(e);
		}
	}
	
}
