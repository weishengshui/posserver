package com.chinarewards.qqgbpvn.testing.lab;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbpvn.testing.context.TestContext;
import com.chinarewards.qqgbpvn.testing.exception.BuildBodyMessageException;
import com.chinarewards.qqgbpvn.testing.exception.ParseResponseMessageException;
import com.chinarewards.qqgbpvn.testing.exception.RunTaskException;
import com.chinarewards.qqgbpvn.testing.exception.SendMessageException;
import com.chinarewards.qqgbpvn.testing.exception.SocketProcessException;
import com.chinarewards.qqgbpvn.testing.util.SocketUtil;
import com.chinarewards.qqgbvpn.main.protocol.cmd.Message;
import com.chinarewards.qqgbvpn.main.protocol.exception.FormatPackageContentException;
import com.chinarewards.qqgbvpn.main.protocol.exception.ParseResponseContentException;

/**
 * description：pos 任务
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-23   下午02:06:46
 * @author Seek
 */
public abstract class PosTask extends AbstractJavaSamplerClient {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * description：run a jmeter test	{is not allow override}
	 * @param context
	 * @return
	 * @time 2011-9-23   下午02:23:00
	 * @author Seek
	 */
	@Override
	public final SampleResult runTest(JavaSamplerContext context) {
		SampleResult sampleResult = null;
		try{
			//如果没有init basePosConfig,那么不做
			if(TestContext.getBasePosConfig() == null){
				logger.warn("this thread haven't BasePosConfig!");
				return null;
			}
			
			//sleep
			long randomTime = (long)(Math.random() * TestContext.getTimestampRange());
			Thread.sleep(randomTime * 60 * 1000);
			
			sampleResult = runTask(context);
		}catch(Throwable e){
			logger.error(e.getMessage(), e);
		}finally {
			//自增流水号
			TestContext.incrementSequence();
			logger.debug("the sequence auto +1");
		}
		return sampleResult;
	}
	
	/**
	 * description：发送消息到pos server
	 * @param context 
	 * @param bodys BodyMessage content
	 * @return pos server Response Message include{HeadMessage and BodyMessage}
	 * @throws SendMessageException
	 * @time 2011-9-23   下午05:54:59
	 * @author Seek
	 */
	protected Message sendMessage(JavaSamplerContext context, byte[] bodys) 
				throws SendMessageException {
		try{
			logger.debug("sendMessage run...");
			
			//生成发送的包
			byte[] requestBytes = formatPackageContent(context, bodys);
			
			//发送包到pos server
			byte[] responseBytes = sendPackageToServer(
					TestContext.getBasePosConfig().getSocket(), requestBytes);
			logger.debug("responseBytes:"+Arrays.toString(responseBytes) +
					", length="+responseBytes.length);
			
			//将package解析并封装成Message
			Message message = parseResponseContent(context, responseBytes);
			
			//将当前的bodyMessage存入线程   以便下次请求使用
			TestContext.getBasePosConfig().setLastResponseBodyMessage(message.getBodyMessage());
			return message;
		}catch(Throwable e){
			throw new SendMessageException(e);
		}
	}
	
	/**
	 * description：format request package content
	 * @param context
	 * @param bodys
	 * @return
	 * @throws BuildBodyMessageException
	 * @time 2011-9-29   下午03:41:34
	 * @author Seek
	 */
	protected byte[] formatPackageContent(JavaSamplerContext context, byte[] bodys) 
				throws BuildBodyMessageException {
		try {
			byte[] requestBytes = TestContext.getPackageUtil().formatPackageContent(
					TestContext.getBasePosConfig().getSequence(), bodys);
			return requestBytes;
		} catch (FormatPackageContentException e) {
			throw new BuildBodyMessageException("build package bytes Error!", e);
		}
	}
	
	/**
	 * description：parse response package content
	 * @param context
	 * @param responseBytes
	 * @return
	 * @throws ParseResponseMessageException
	 * @time 2011-9-29   下午03:41:55
	 * @author Seek
	 */
	protected Message parseResponseContent(JavaSamplerContext context, byte[] responseBytes) 
				throws ParseResponseMessageException {
		try {
			Message message = TestContext.getPackageUtil().parseResponseContent(
					responseBytes, TestContext.getCharset());
			return message;
		} catch (ParseResponseContentException e) {
			throw new ParseResponseMessageException("parse package bytes Error!", e);
		}
	}
	
	/**
	 * description：发送一个package，同时接收一个package
	 * @param sendPackage
	 * @return
	 * @time 2011-9-22   下午05:59:41
	 * @author Seek
	 */
	protected byte[] sendPackageToServer(Socket outerSocket, byte[] sendPackage) 
				throws SocketProcessException {
		logger.debug("sendPackageToServer() run...");
		Socket socket = null;
		OutputStream os = null;
		InputStream is = null;
		
		byte[] responseBytes = null;
		try{
			socket = outerSocket;
			os = socket.getOutputStream();
			is = socket.getInputStream();
			
			//write bytes to socket
			SocketUtil.writeFromOutputStream(os, sendPackage);
			//read bytes from socket
			responseBytes = SocketUtil.readFromInputStream(is);
		}catch(Throwable e) {
			throw new SocketProcessException(e);
		}finally{
			//socket.close();
		}
		logger.debug("socket I/O end!");
		return responseBytes;
	}
	
	/**
	 * description：build package body bytes
	 * @return
	 * @time 2011-9-23   下午02:22:37
	 * @author Seek
	 */
	protected byte[] buildBodyMessage(JavaSamplerContext context) 
			throws BuildBodyMessageException {
		return null;
	}
	
	/**
	 * description：执行任务
	 * @param context
	 * @return
	 * @time 2011-9-23   下午02:18:14
	 * @author Seek
	 */
	protected abstract SampleResult runTask(JavaSamplerContext context) throws RunTaskException;
	
}
