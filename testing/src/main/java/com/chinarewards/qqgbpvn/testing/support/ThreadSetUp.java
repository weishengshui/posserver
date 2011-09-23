package com.chinarewards.qqgbpvn.testing.support;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.chinarewards.qqgbpvn.testing.context.TestContext;

public class ThreadSetUp extends AbstractJavaSamplerClient {
	
	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		try {
			TestContext.initBasePosConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new SampleResult();
	}

}
