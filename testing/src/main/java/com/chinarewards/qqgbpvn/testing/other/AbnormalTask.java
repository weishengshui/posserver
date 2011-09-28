package com.chinarewards.qqgbpvn.testing.other;

import java.net.Socket;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbpvn.testing.exception.BuildBodyMessageException;
import com.chinarewards.qqgbpvn.testing.support.ThreadGroupSetUp;
import com.chinarewards.qqgbpvn.testing.util.SocketUtils;

/**
 * description：非常规的bytes
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-28   下午04:32:54
 * @author Seek
 */
public class AbnormalTask extends AbstractJavaSamplerClient {
	
	private Logger logger = LoggerFactory.getLogger(ThreadGroupSetUp.class);
	
	private static final String POS_SERVER_IP = "POS_SERVER_IP";
	private static final String POS_SERVER_PORT = "POS_SERVER_PORT";
	
	private static final String TIMESTAMP_RANGE = "TIMESTAMP_RANGE";
	
	@Override
	public Arguments getDefaultParameters() {
		Arguments arguments = new Arguments();
		arguments.addArgument(POS_SERVER_IP, "127.0.0.1");
		arguments.addArgument(POS_SERVER_PORT, "1234");
		
		arguments.addArgument(TIMESTAMP_RANGE, "0");
		return arguments;
	}
	
	private byte[] randomBytes(JavaSamplerContext context)
			throws BuildBodyMessageException {
		
		int amt = (int)(Math.random() * 30) + 5;	//随机 5 - 30个byte
		
		byte[] bodys = new byte[amt];
		for(int i=0;i<amt;i++){
			byte randomByte = (byte) ((Math.random() * 255) - 128);
			bodys[i] = randomByte;
		}
		
		return bodys;
	}

	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult res = new SampleResult();
		res.sampleStart();	//开始任务
		
		try{
			//unit of time: second
			long timestampRange = Long.parseLong(context.getParameter(TIMESTAMP_RANGE));
			String posServerIp = context.getParameter(POS_SERVER_IP);
			Integer posServerPort = Integer.parseInt(context.getParameter(POS_SERVER_PORT));
			
			//sleep
			long randomTime = (long)(Math.random() * timestampRange);
			Thread.sleep(randomTime * 1000);
			
			byte[] bodys =  randomBytes(context);
			
			//socket send message
			Socket socket = new Socket(posServerIp, posServerPort);
			SocketUtils.sendPackageToServer(socket, bodys);
			
			res.setSuccessful(true);
		}catch(Throwable e){
			res.setSuccessful(false);
			logger.error(e.getMessage(), e);
		}finally{
			res.sampleEnd();	//结束任务
		}
		return res;
	}
	
}
