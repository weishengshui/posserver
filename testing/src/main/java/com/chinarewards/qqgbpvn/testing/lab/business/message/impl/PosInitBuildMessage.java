package com.chinarewards.qqgbpvn.testing.lab.business.message.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbpvn.testing.context.TestContext;
import com.chinarewards.qqgbpvn.testing.exception.BuildBodyMessageException;
import com.chinarewards.qqgbpvn.testing.lab.business.message.BuildMessage;
import com.chinarewards.qqgbvpn.main.protocol.SimpleCmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.cmd.InitRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * description：pos init process
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-30   下午05:53:51
 * @author Seek
 */
public class PosInitBuildMessage implements BuildMessage {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public byte[] buildBodyMessage(Map<String,String> context) throws BuildBodyMessageException {
		logger.debug("PosInitTask buildBodyMessage() run...");
		try{
			InitRequestMessage bodyMessage = new InitRequestMessage();
			bodyMessage.setCmdId(InitRequestMessage.INIT_CMD_ID);
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
