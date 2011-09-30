package com.chinarewards.qqgbpvn.testing.lab.support;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbpvn.testing.context.TestContext;
import com.chinarewards.qqgbpvn.testing.exception.ReadCsvFileException;
import com.chinarewards.qqgbpvn.testing.model.BasePosConfig;
import com.chinarewards.qqgbvpn.common.HomeDirLocator;
import com.chinarewards.qqgbvpn.config.ConfigReader;
import com.chinarewards.qqgbvpn.main.protocol.CmdMapping;
import com.chinarewards.qqgbvpn.main.protocol.CodecMappingConfigBuilder;
import com.chinarewards.qqgbvpn.main.protocol.SimpleCmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.PackageHeadCodec;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.PackageUtil;

/**
 * description：threadGroup setUp()
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-26   上午10:19:30
 * @author Seek
 */
public final class ThreadGroupSetUp extends AbstractJavaSamplerClient {
	
	private Logger logger = LoggerFactory.getLogger(ThreadGroupSetUp.class);
	
	private static final String CSV_FILE = "CSV_FILE";
	private static final String POSNET_HOME = "POSNET_HOME";
	
	private static final String POS_SERVER_IP = "POS_SERVER_IP";
	private static final String POS_SERVER_PORT = "POS_SERVER_PORT";
	
	private static final String CSV_SEPARATOR = "CSV_SEPARATOR";
	private static final String TIMESTAMP_RANGE = "TIMESTAMP_RANGE(unit of time:minute)";
	
	@Override
	public Arguments getDefaultParameters() {
		Arguments arguments = new Arguments();
		arguments.addArgument(POSNET_HOME, "D:\\posnetv2");
		arguments.addArgument(CSV_FILE, "D:\\pos_data.csv");
		arguments.addArgument(CSV_SEPARATOR, ",");
		
		arguments.addArgument(POS_SERVER_IP, "127.0.0.1");
		arguments.addArgument(POS_SERVER_PORT, "1234");
		
		arguments.addArgument(TIMESTAMP_RANGE, "0");
		return arguments;
	}
	
	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		logger.debug("threadGroup setUp...");
		SampleResult result = new SampleResult();
		result.sampleStart();
		
		String csvFileName = context.getParameter(CSV_FILE);
		String posServerIp = context.getParameter(POS_SERVER_IP);
		Integer posServerPort = Integer.parseInt(context.getParameter(POS_SERVER_PORT));
		String posnetHome = context.getParameter(POSNET_HOME);
		String csvSeparator = context.getParameter(CSV_SEPARATOR);
		Long timestampRange = Long.parseLong(context.getParameter(TIMESTAMP_RANGE));
		
		logger.debug("csvFileName:"+csvFileName);
		logger.debug("posServerIp:"+posServerIp);
		logger.debug("posServerPort:"+posServerPort);
		logger.debug("posnetHome:"+posnetHome);
		logger.debug("csvSeparator:"+csvSeparator);
		try {
			//setup posServer ip and port
			TestContext.setPosServerIp(posServerIp);
			TestContext.setPosServerPort(posServerPort);
			
			//setup charset  and  csvSeparator
			TestContext.setCharset(Charset.forName("GB2312"));
			TestContext.setCsvSeparator(csvSeparator);
			
			//setup {message head} and {message body}  Codec
			buildSimpleCmdCodecFactory(posnetHome);
			logger.debug("build a SimpleCmdCodecFactory!");
			TestContext.setPackageHeadCodec(new PackageHeadCodec());
			
			//read csv data file to memory
			readCsvToMap(csvFileName, TestContext.getPosMap());
			
			//set max pos
			TestContext.setMaxPos((long)TestContext.getPosMap().size());
			
			//set thread timestamp
			TestContext.setTimestampRange(timestampRange);
			
			//set a packageUtil
			TestContext.setPackageUtil(new PackageUtil(TestContext.getPackageHeadCodec(), 
					TestContext.getCmdCodecFactory()));
			
			result.setSuccessful(true);
		} catch (Exception e) {
			result.setSuccessful(false);
			logger.error(e.getMessage(), e);
		} finally {
			result.sampleEnd();
		}
		return result;
	}
	
	/**
	 * description：read data from csv File to map memory
	 * @param fileName
	 * @param map
	 * @throws Exception
	 * @time 2011-9-22   下午02:28:57
	 * @author Seek
	 */
	private void readCsvToMap(String fileName, Map<String, BasePosConfig> map) throws Exception {
		logger.debug("ThreadGroupSetUp readCsvToMap() run...");
		BufferedReader buffRead = null;
		try{
			String tempLine;
			buffRead = new BufferedReader(new FileReader(fileName));
			while((tempLine = buffRead.readLine()) != null){
				String arr[] = tempLine.split(TestContext.getCsvSeparator());
				
				if(arr != null && arr[0] != null){
					BasePosConfig basePosConfig = new BasePosConfig();
					basePosConfig.setPosId(arr[1].trim());
					basePosConfig.setSecret(arr[2].trim());
					
					logger.debug("put("+arr[0]+","+basePosConfig+")");
					map.put(arr[0].trim(), basePosConfig);
				}
			}
		}catch(Throwable e){
			throw new ReadCsvFileException(e.getMessage(), e);
		}finally{
			buffRead.close();
		}
		logger.debug("ThreadGroupSetUp run readCsvToMap() is over!");
	}
	
	/**
	 * description：build a SimpleCmdCodecFactory
	 * @param posnetHome
	 * @time 2011-9-23   上午11:46:45
	 * @author Seek
	 */
	private void buildSimpleCmdCodecFactory(String posnetHome) {
		// read POSNet server's configuration file.
		Configuration configuration = null;
		ConfigReader reader = new ConfigReader(new HomeDirLocator(posnetHome));
		try {
			configuration = reader.read("posnet.ini");
		} catch (ConfigurationException e) {
			throw new RuntimeException("Failed to read posnet.ini", e);
		}
		debugPrintConfig(configuration);
		
		// prepare the codec mapping
		CodecMappingConfigBuilder builder = new CodecMappingConfigBuilder();
		CmdMapping cmdMapping = builder.buildMapping(configuration);
		logger.debug("cmdMapping:"+cmdMapping);
		// and then the factory
		SimpleCmdCodecFactory cmdCodecFactory = new SimpleCmdCodecFactory(cmdMapping);
		
		TestContext.setCmdCodecFactory(cmdCodecFactory);
	}
	
	@SuppressWarnings("rawtypes")
	protected void debugPrintConfig(Configuration configuration) {
		Iterator i = configuration.getKeys();
		while (i.hasNext()) {
			String key = (String)i.next();
			Object value = configuration.getProperty(key);
			logger.debug("- " + key + ": " + value);
		}
	}
	
}
