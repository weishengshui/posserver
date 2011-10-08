package com.chinarewards.qqgbpvn.testing.lab.business.message.impl;

import java.util.Arrays;
import java.util.Map;

import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbpvn.testing.context.TestContext;
import com.chinarewards.qqgbpvn.testing.exception.BuildBodyMessageException;
import com.chinarewards.qqgbpvn.testing.lab.business.message.BuildMessage;
import com.chinarewards.qqgbvpn.main.protocol.SimpleCmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.InitResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.LoginRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;
import com.chinarewards.qqgbvpn.main.util.HMAC_MD5;

/**
 * description：pos login process
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-30   下午05:53:51
 * @author Seek
 */
public class PosLoginBuildMessage implements BuildMessage {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public byte[] buildBodyMessage(Map<String,String> context)
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
