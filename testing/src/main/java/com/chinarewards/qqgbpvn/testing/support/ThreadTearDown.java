package com.chinarewards.qqgbpvn.testing.support;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbpvn.testing.context.TestContext;

/**
 * description：tear down a thread
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-26   上午10:32:13
 * @author Seek
 */
public class ThreadTearDown extends AbstractJavaSamplerClient {
	
	private Logger logger = LoggerFactory.getLogger(ThreadTearDown.class);
	
	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		try{
			TestContext.clearBasePosConfig();
		}catch(Throwable e){
			logger.error(e.getMessage(), e);
		}
		
		return new SampleResult();
	}

}
