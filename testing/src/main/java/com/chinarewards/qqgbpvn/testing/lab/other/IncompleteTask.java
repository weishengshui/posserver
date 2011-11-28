package com.chinarewards.qqgbpvn.testing.lab.other;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.chinarewards.qqgbpvn.testing.context.TestContext;
import com.chinarewards.qqgbpvn.testing.exception.BuildBodyMessageException;
import com.chinarewards.qqgbpvn.testing.exception.RunTaskException;
import com.chinarewards.qqgbpvn.testing.exception.SendMessageException;
import com.chinarewards.qqgbpvn.testing.lab.PosTask;
import com.chinarewards.qqgbpvn.testing.lab.business.message.BuildMessage;
import com.chinarewards.qqgbpvn.testing.lab.business.message.BusinessType;
import com.chinarewards.qqgbpvn.testing.lab.business.message.MessageFactory;
import com.chinarewards.qqgbvpn.main.protocol.cmd.Message;

/**
 * description：发送不完整的package
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-29   上午10:30:53
 * @author Seek
 */
public class IncompleteTask extends PosTask {

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
		Map<String, String> map = new HashMap<String, String>();
		BuildMessage buildMessage = MessageFactory.getBuildMessage(BusinessType.PosInit);
		return buildMessage.buildBodyMessage(map);

	}
	
	@Override
	protected Message sendMessage(JavaSamplerContext context, byte[] bodys)
			throws SendMessageException {
		try{
			logger.debug("sendMessage run...");
			
			byte[] requestBytes = formatPackageContent(context, bodys);
			
			/***************************************************************************/
			byte[] porxyRequestBytes = new byte[requestBytes.length - 2];
			System.arraycopy(requestBytes, 0, porxyRequestBytes, 0, porxyRequestBytes.length);
			/***************************************************************************/
			
			//发送包到pos server
			byte[] responseBytes = super.sendPackageToServer(
					TestContext.getBasePosConfig().getSocket(), porxyRequestBytes);
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
