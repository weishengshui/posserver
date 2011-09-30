package com.chinarewards.qqgbpvn.testing.lab.firmware;

import java.io.File;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import com.chinarewards.qqgbpvn.testing.context.TestContext;
import com.chinarewards.qqgbpvn.testing.exception.BuildBodyMessageException;
import com.chinarewards.qqgbpvn.testing.exception.RunTaskException;
import com.chinarewards.qqgbpvn.testing.lab.PosTask;
import com.chinarewards.qqgbpvn.testing.util.DigestUtil;
import com.chinarewards.qqgbpvn.testing.util.FileUtil;
import com.chinarewards.qqgbvpn.main.protocol.SimpleCmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ErrorBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.GetFirmwareFragmentRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.GetFirmwareFragmentResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.Message;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * description：POS机下載固件內容
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-29   下午05:36:57
 * @author Seek
 */
public final class PosGetFirmwareFragmentTask extends PosTask {
	
	private static final String GET_FIRMWARE_FRAGMENT_LENGTH = "GET_FIRMWARE_FRAGMENT_LENGTH";
	private static final String PUT_FIRMWARE_PATH = "PUT_FIRMWARE_PATH";
	private static final String NATIVE_FIRMWARE_FILE = "NITIVE_FIRMWARE_FILE";
	
	@Override
	public Arguments getDefaultParameters() {
		Arguments arguments = new Arguments();
		arguments.addArgument(GET_FIRMWARE_FRAGMENT_LENGTH, "100");
		arguments.addArgument(PUT_FIRMWARE_PATH, "D:\\posnetv2\\posnet2-mgmtui\\firmware\\temp\\");
		arguments.addArgument(NATIVE_FIRMWARE_FILE, "D:\\posnetv2\\posnet2-mgmtui\\firmware\\110927.bin");
		return arguments;
	}
	
	@Override
	public SampleResult runTask(JavaSamplerContext context) throws RunTaskException {
		String selfFirmwareName = TestContext.getBasePosConfig().getPosId() + "_firmware.bin";
		
		SampleResult res = new SampleResult();
		res.sampleStart();	//开始任务
		
		try{
			if(TestContext.getBasePosConfig().getFirmwareSize() == 0 || TestContext.getBasePosConfig().getFirmwareName() == null){
				res.setSuccessful(false);
				throw new RuntimeException("this firmware size==0 or firmware name is null");
			}
			
			byte[] bodys =  buildBodyMessage(context);
			Message message = super.sendMessage(context, bodys);
			
			if(message.getBodyMessage() instanceof ErrorBodyMessage){
				res.setSuccessful(false);
			}else {
				res.setSuccessful(true);
				GetFirmwareFragmentResponseMessage responseMessage = 
							(GetFirmwareFragmentResponseMessage) message.getBodyMessage();
				
				byte[] content = responseMessage.getContent();
				
				//write bytes to file
				File firmwareFile = new File(context.getParameter(PUT_FIRMWARE_PATH), selfFirmwareName);
				File currentFile = FileUtil.writeBytesToFile(content, firmwareFile, true);
				logger.debug("write bytes to file done! current temp firmware length:"+currentFile.length());
				
				//offset ending?
				long currentOffset = TestContext.getBasePosConfig().getFirmwareOffset() + content.length;
				TestContext.getBasePosConfig().setFirmwareOffset(currentOffset);
				logger.debug("currentOffset="+currentOffset);
				
				if(currentOffset >= TestContext.getBasePosConfig().getFirmwareSize()){
					//judgment upgrade file whether complete
					File putFirmwareFile = new File(context.getParameter(PUT_FIRMWARE_PATH), selfFirmwareName);
					File nativeFirmwareFile = new File(context.getParameter(NATIVE_FIRMWARE_FILE));
					boolean compareResult = DigestUtil.compareDigest(FileUtil.getBytesFromFile(putFirmwareFile), 
							FileUtil.getBytesFromFile(nativeFirmwareFile), DigestUtil.MD5);
					if(compareResult){
						res.setSuccessful(true);
					}else {
						res.setSuccessful(false);
					}
					
					logger.debug("loop break...");
					res.setStopThread(true);	//TODO thread stop???
				}
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
	protected byte[] buildBodyMessage(JavaSamplerContext context) throws BuildBodyMessageException {
		try{
			String posId = TestContext.getBasePosConfig().getPosId();
			long offset = TestContext.getBasePosConfig().getFirmwareOffset();
			long length = Long.parseLong(context.getParameter(GET_FIRMWARE_FRAGMENT_LENGTH));
			GetFirmwareFragmentRequestMessage bodyMessage = new 
					GetFirmwareFragmentRequestMessage(posId, offset, length);
			
			SimpleCmdCodecFactory cmdCodecFactory = TestContext.getCmdCodecFactory();
			ICommandCodec codec = cmdCodecFactory.getCodec(bodyMessage.getCmdId());
			
			byte[] bodys = codec.encode(bodyMessage, TestContext.getCharset());
			return bodys;
		}catch(Throwable e){
			throw new BuildBodyMessageException(e);
		}
	}
	
}
