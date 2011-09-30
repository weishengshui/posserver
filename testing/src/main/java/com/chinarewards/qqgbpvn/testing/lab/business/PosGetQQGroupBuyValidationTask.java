package com.chinarewards.qqgbpvn.testing.lab.business;

import java.util.List;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.chinarewards.qqgbpvn.testing.context.TestContext;
import com.chinarewards.qqgbpvn.testing.exception.BuildBodyMessageException;
import com.chinarewards.qqgbpvn.testing.exception.RunTaskException;
import com.chinarewards.qqgbpvn.testing.lab.PosTask;
import com.chinarewards.qqgbvpn.main.protocol.SimpleCmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ErrorBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.Message;
import com.chinarewards.qqgbvpn.main.protocol.cmd.SearchResponseDetail;
import com.chinarewards.qqgbvpn.main.protocol.cmd.SearchResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ValidateRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * description：POS机  团购验证
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-22   下午06:02:53
 * @author Seek
 */
public final class PosGetQQGroupBuyValidationTask extends PosTask {
	
	private static final String GROUPON_VCODE = "grouponVCode";
	
	@Override
	public Arguments getDefaultParameters() {
		Arguments arguments = new Arguments();
		arguments.addArgument(GROUPON_VCODE, "12345678");
		return arguments;
	}
	
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
			logger.debug("PosGetQQGroupBuyValidationTest buildBodyMessage() run...");
			
			String grouponVCode = context.getParameter(GROUPON_VCODE);
			logger.debug("grouponVCode="+grouponVCode);
			
			ValidateRequestMessage bodyMessage = new ValidateRequestMessage();
			bodyMessage.setCmdId(CmdConstant.VALIDATE_CMD_ID);
			
			
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
