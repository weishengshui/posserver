package com.chinarewards.qqgbpvn.testing.lab.business.message.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbpvn.testing.context.TestContext;
import com.chinarewards.qqgbpvn.testing.exception.BuildBodyMessageException;
import com.chinarewards.qqgbpvn.testing.lab.business.message.BuildMessage;
import com.chinarewards.qqgbvpn.main.protocol.SimpleCmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.SearchRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.SearchResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * description：pos get qq group buy list process
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-30   下午05:53:51
 * @author Seek
 */
public class PosGetQQGroupBuyListBuildMessage implements BuildMessage {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public byte[] buildBodyMessage(Map<String,String> context)
			throws BuildBodyMessageException {
		try{
			logger.debug("PosGetQQGroupBuyListTask buildBodyMessage() run...");
			SearchRequestMessage bodyMessage = new SearchRequestMessage();
			bodyMessage.setCmdId(CmdConstant.SEARCH_CMD_ID);
			
			int page = 1;
			final int size = 6;
			ICommand iCommand = TestContext.getBasePosConfig().getLastResponseBodyMessage();
			if(iCommand instanceof SearchResponseMessage){
				logger.debug("getLastResponseBodyMessage() is SearchResponseMessage!");
				SearchResponseMessage searchResponseMessage = (SearchResponseMessage)iCommand;
				
				//go on, then ++ , the next page
				if(page < searchResponseMessage.getTotalpage()){
					page = searchResponseMessage.getCurpage() + 1;
				}else {
					page=1;
				}
			}
			
			logger.debug("page="+page);
			logger.debug("size="+size);
			bodyMessage.setPage(page);
			bodyMessage.setSize(size);
			
			SimpleCmdCodecFactory cmdCodecFactory = TestContext.getCmdCodecFactory();
			ICommandCodec codec = cmdCodecFactory.getCodec(bodyMessage.getCmdId());
			
			byte[] bodys = codec.encode(bodyMessage, TestContext.getCharset());
			return bodys;
		}catch(Throwable e){
			throw new BuildBodyMessageException(e);
		}
	}

}
