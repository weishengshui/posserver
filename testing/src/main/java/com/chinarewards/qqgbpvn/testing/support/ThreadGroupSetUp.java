package com.chinarewards.qqgbpvn.testing.support;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.Map;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import com.chinarewards.qqgbpvn.testing.context.TestContext;
import com.chinarewards.qqgbpvn.testing.model.BasePosConfig;
import com.chinarewards.qqgbvpn.common.HomeDirLocator;
import com.chinarewards.qqgbvpn.config.ConfigReader;
import com.chinarewards.qqgbvpn.main.protocol.CmdMapping;
import com.chinarewards.qqgbvpn.main.protocol.CodecMappingConfigBuilder;
import com.chinarewards.qqgbvpn.main.protocol.SimpleCmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.PackageHeadCodec;

public class ThreadGroupSetUp extends AbstractJavaSamplerClient {
	
	private static final String CSV_FILE = "CSV_FILE";
	private static final String POSNET_HOME = "POSNET_HOME";
	
	private static final String POS_SERVER_IP = "POS_SERVER_IP";
	private static final String POS_SERVER_PORT = "POS_SERVER_PORT";
	
	private static final String SEPARATOR = ",";
	
	@Override
	public Arguments getDefaultParameters() {
		Arguments arguments = new Arguments();
		arguments.addArgument(CSV_FILE, "D:\\pos_data.csv");
		arguments.addArgument(POSNET_HOME, "D:\\posnetv2\\conf");
		arguments.addArgument(POS_SERVER_IP, "127.0.0.1");
		arguments.addArgument(POS_SERVER_PORT, "1234");
		return arguments;
	}
	
	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		System.out.println("ThreadGroupSetUp...");
		
		String csvFileName = context.getParameter(CSV_FILE);
		String posServerIp = context.getParameter(POS_SERVER_IP);
		String posServerPort = context.getParameter(POS_SERVER_PORT);
		String posnetHome = context.getParameter(POSNET_HOME);
		
		System.out.println("csvFileName:"+csvFileName);
		System.out.println("posServerIp:"+posServerIp);
		System.out.println("posServerPort:"+posServerPort);
		System.out.println("posnetHome:"+posnetHome);
		try {
			//setup posServer ip and port
			TestContext.setPosServerIp(posServerIp);
			TestContext.setPosServerPort(posServerPort);
			
			//setup charset
			TestContext.setCharset(Charset.forName("GB2312"));
			
			//setup {message head} and {message body}  Codec
			buildSimpleCmdCodecFactory(posnetHome);
			TestContext.setPackageHeadCodec(new PackageHeadCodec());
			
			//read csv data file to memory
			readCsvToMap(csvFileName, TestContext.getPosMap());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new SampleResult();
	}
	
	/**
	 * description：读取CSV到Map中
	 * @param fileName
	 * @param map
	 * @throws Exception
	 * @time 2011-9-22   下午02:28:57
	 * @author Seek
	 */
	private void readCsvToMap(String fileName, Map<String, BasePosConfig> map) throws Exception{
		System.out.println("begin readCsvToMap");
		BufferedReader buffRead = null;
		try{
			String tempLine;
			buffRead = new BufferedReader(new FileReader(fileName));
			while((tempLine = buffRead.readLine()) != null){
				String arr[] = tempLine.split(SEPARATOR);
				
				if(arr != null && arr[0] != null){
					System.out.println("put("+arr[0]+","+arr[1]+")");
					
					BasePosConfig basePosConfig = new BasePosConfig();
					basePosConfig.setPosId(arr[1].trim());
					basePosConfig.setSecret(arr[2].trim());
					
					map.put(arr[0].trim(), basePosConfig);
				}
			}
		}catch(Throwable e){
			throw new Exception(e);
		}finally{
			buffRead.close();
		}
		System.out.println("end readCsvToMap");
	}
	
	/**
	 * description：创建一个SimpleCmdCodecFactory
	 * @param posnetHome
	 * @time 2011-9-23   上午11:46:45
	 * @author Seek
	 */
	private void buildSimpleCmdCodecFactory(String posnetHome){
		// read POSNet server's configuration file.
		Configuration configuration = null;
		ConfigReader reader = new ConfigReader(new HomeDirLocator(posnetHome));
		try {
			configuration = reader.read("posnet.ini");
		} catch (ConfigurationException e) {
			throw new RuntimeException("Failed to read posnet.ini", e);
		}
		
		// prepare the codec mapping
		CodecMappingConfigBuilder builder = new CodecMappingConfigBuilder();
		CmdMapping cmdMapping = builder.buildMapping(configuration);
		// and then the factory
		SimpleCmdCodecFactory cmdCodecFactory = new SimpleCmdCodecFactory(cmdMapping);
		
		TestContext.setCmdCodecFactory(cmdCodecFactory);
	}

}
