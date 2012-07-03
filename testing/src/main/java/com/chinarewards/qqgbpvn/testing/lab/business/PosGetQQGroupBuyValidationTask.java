package com.chinarewards.qqgbpvn.testing.lab.business;

import java.util.HashMap;
import java.util.Map;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.chinarewards.qqgbpvn.testing.exception.BuildBodyMessageException;
import com.chinarewards.qqgbpvn.testing.exception.RunTaskException;
import com.chinarewards.qqgbpvn.testing.lab.PosTask;
import com.chinarewards.qqgbpvn.testing.lab.business.message.BuildMessage;
import com.chinarewards.qqgbpvn.testing.lab.business.message.BusinessType;
import com.chinarewards.qqgbpvn.testing.lab.business.message.MessageFactory;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ErrorBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.Message;

/**
 * description：POS机  团购验证
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-22   下午06:02:53
 * @author Seek
 */
public final class PosGetQQGroupBuyValidationTask extends PosTask {
	
	public static final String GROUPON_VCODE = "grouponVCode";
	
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
		Map<String, String> map = new HashMap<String, String>();
		map.put(GROUPON_VCODE, context.getParameter(GROUPON_VCODE));
		
		BuildMessage buildMessage = MessageFactory.getBuildMessage(BusinessType.PosGetQQGroupBuyValidation);
		return buildMessage.buildBodyMessage(map);
	}
	
}
