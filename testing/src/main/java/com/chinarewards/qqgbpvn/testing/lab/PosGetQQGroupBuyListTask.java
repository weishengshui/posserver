package com.chinarewards.qqgbpvn.testing.lab;

import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.chinarewards.qqgbpvn.testing.context.TestContext;
import com.chinarewards.qqgbpvn.testing.exception.BuildBodyMessageException;
import com.chinarewards.qqgbpvn.testing.exception.RunTaskException;
import com.chinarewards.qqgbpvn.testing.lab.parent.PosTask;
import com.chinarewards.qqgbvpn.main.protocol.SimpleCmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.Message;
import com.chinarewards.qqgbvpn.main.protocol.cmd.SearchRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.SearchResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * description：POS机    团购商品列表
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-22   下午06:02:53
 * @author Seek
 */
public final class PosGetQQGroupBuyListTask extends PosTask {

	@Override
	protected SampleResult runTask(JavaSamplerContext context)
			throws RunTaskException {
		SampleResult res = new SampleResult();
		
		res.sampleStart();	//开始任务
		try{
			byte[] bodys =  buildBodyMessage(context);
			Message message = super.sendMessage(context, bodys);
			
			res.setSuccessful(true);
		}catch(Throwable e){
			res.setSuccessful(false);
			e.printStackTrace();
		}finally{
			res.sampleEnd();	//结束任务
		}
		return res;
	}
	
	@Override
	protected byte[] buildBodyMessage(JavaSamplerContext context)
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
				//page random scope {1 - totalPage}		default page = 1;
				page = (int)(Math.random() * searchResponseMessage.getTotalpage()) + 1;	
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
