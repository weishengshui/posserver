package com.chinarewards.qqgbpvn.testing.lab.firmware;

import java.util.HashMap;
import java.util.Map;

import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.chinarewards.qqgbpvn.testing.context.TestContext;
import com.chinarewards.qqgbpvn.testing.exception.BuildBodyMessageException;
import com.chinarewards.qqgbpvn.testing.exception.RunTaskException;
import com.chinarewards.qqgbpvn.testing.lab.PosTask;
import com.chinarewards.qqgbpvn.testing.lab.business.message.BuildMessage;
import com.chinarewards.qqgbpvn.testing.lab.business.message.BusinessType;
import com.chinarewards.qqgbpvn.testing.lab.business.message.MessageFactory;
import com.chinarewards.qqgbvpn.main.protocol.SimpleCmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ErrorBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpgradeRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpgradeRequestResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.Message;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * description：POS机请求升级
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-29   下午05:36:57
 * @author Seek
 */
public final class PosRequestUpgradeTask extends PosTask {
	
	@Override
	public SampleResult runTask(JavaSamplerContext context) throws RunTaskException {
		SampleResult res = new SampleResult();
		res.sampleStart();	//开始任务
		
		try{
			byte[] bodys =  buildBodyMessage(context);
			Message message = super.sendMessage(context, bodys);
			
			if(message.getBodyMessage() instanceof ErrorBodyMessage){
				res.setSuccessful(false);
			}else{
				FirmwareUpgradeRequestResponseMessage bodyMessage = (FirmwareUpgradeRequestResponseMessage) message.getBodyMessage();
				TestContext.getBasePosConfig().setFirmwareSize(bodyMessage.getSize());
				TestContext.getBasePosConfig().setFirmwareName(bodyMessage.getFirmwareName());
				
				logger.debug("save firmware size:"+bodyMessage.getSize());
				logger.debug("save firmware name:"+bodyMessage.getFirmwareName());
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
		BuildMessage buildMessage = MessageFactory.getBuildMessage(BusinessType.PosRequestUpgrade);
		return buildMessage.buildBodyMessage(map);
	}
	
}
