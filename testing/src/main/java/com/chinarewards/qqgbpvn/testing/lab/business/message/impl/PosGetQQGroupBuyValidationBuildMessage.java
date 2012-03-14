package com.chinarewards.qqgbpvn.testing.lab.business.message.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbpvn.testing.context.TestContext;
import com.chinarewards.qqgbpvn.testing.exception.BuildBodyMessageException;
import com.chinarewards.qqgbpvn.testing.lab.business.PosGetQQGroupBuyValidationTask;
import com.chinarewards.qqgbpvn.testing.lab.business.message.BuildMessage;
import com.chinarewards.qqgbvpn.main.protocol.SimpleCmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.SearchResponseDetail;
import com.chinarewards.qqgbvpn.main.protocol.cmd.SearchResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ValidateRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * description：pos get qq group buy validation process
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-30   下午05:53:51
 * @author Seek
 */
public class PosGetQQGroupBuyValidationBuildMessage implements BuildMessage {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public byte[] buildBodyMessage(Map<String,String> context)
			throws BuildBodyMessageException {
		try{
			logger.debug("PosGetQQGroupBuyValidationTest buildBodyMessage() run...");
			
			String grouponVCode = context.get(PosGetQQGroupBuyValidationTask.GROUPON_VCODE);
			logger.debug("grouponVCode="+grouponVCode);
			
			ValidateRequestMessage bodyMessage = new ValidateRequestMessage();
			bodyMessage.setCmdId(ValidateRequestMessage.VALIDATE_CMD_ID);
			
			
			String grouponId = null;
			ICommand iCommand = TestContext.getBasePosConfig().getLastResponseBodyMessage();
			
			//if the last request is get list, random get grouponId from list.  and set to threadLocal
			//else from threadLocal
			if(iCommand instanceof SearchResponseMessage){
				logger.debug("getLastResponseBodyMessage() is SearchResponseMessage!");
				SearchResponseMessage searchResponseMessage = (SearchResponseMessage)iCommand;
				List<SearchResponseDetail> searchResponseDetailList = searchResponseMessage.getDetail();
				
				int randomIndex = (int)(Math.random()*searchResponseDetailList.size());
				grouponId = searchResponseDetailList.get(randomIndex).getGrouponId();
				logger.debug("random from list get grouponId = "+grouponId);
				
				TestContext.getBasePosConfig().setGrouponId(grouponId);
			}else {
				grouponId = TestContext.getBasePosConfig().getGrouponId();
				logger.debug("grouponId from getBasePosConfig = "+grouponId);
			}
			
			bodyMessage.setGrouponId(grouponId);
			bodyMessage.setGrouponVCode(grouponVCode);
			
			SimpleCmdCodecFactory cmdCodecFactory = TestContext.getCmdCodecFactory();
			ICommandCodec codec = cmdCodecFactory.getCodec(bodyMessage.getCmdId());
			
			byte[] bodys = codec.encode(bodyMessage, TestContext.getCharset());
			return bodys;
		}catch(Throwable e){
			throw new BuildBodyMessageException(e);
		}
	}

}
