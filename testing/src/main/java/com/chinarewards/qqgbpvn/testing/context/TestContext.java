package com.chinarewards.qqgbpvn.testing.context;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbpvn.testing.model.BasePosConfig;
import com.chinarewards.qqgbvpn.main.protocol.SimpleCmdCodecFactory;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.PackageHeadCodec;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.PackageUtil;

/**
 * description：基础环境配置
 * @copyright binfen.cc
 * @projectName test-qqGBV
 * @time 2011-9-22   下午02:18:14
 * @author Seek
 */
public class TestContext {
	
	private static Logger logger = LoggerFactory.getLogger(TestContext.class);
	
	private static final Map<String, BasePosConfig> POS_MAP = Collections.
								synchronizedMap(new HashMap<String, BasePosConfig>());
	
	private static final ThreadLocal<BasePosConfig> tLocal = 
								new ThreadLocal<BasePosConfig>();
	
	private static Charset charset = null;
	
	private static Long maxPos = 0L;
	
	private static Long num = 0L;
	
	//POS Server IP
	private static String posServerIp;
	
	//POS Server Port
	private static Integer posServerPort;
	
	//csv file Separator
	private static String csvSeparator;

	private static SimpleCmdCodecFactory cmdCodecFactory;	//codec by cmdId
	
	private static PackageHeadCodec packageHeadCodec;
	
	private static PackageUtil packageUtil;
	
	private static long timestampRange;		//thread invoke timestamp range, unit of time: second
	
	/**
	 * description：销毁test系统级的数据
	 * @time 2011-9-22   下午03:48:32
	 * @author Seek
	 */
	public static void testDestroy(){
		maxPos = 0L;
		num = 0L;
		POS_MAP.clear();
		charset = null;
		posServerIp = null;
		posServerPort = null;
		cmdCodecFactory = null;
		packageHeadCodec = null;
		logger.debug("TestContext testDestroy() invoke!");
	}
	
	/**
	 * description：清除自己线程的ThreadLocal
	 * @time 2011-9-22   下午03:48:15
	 * @author Seek
	 */
	public static void clearBasePosConfig(){
		if(tLocal.get() != null){
			try {
				tLocal.get().getSocket().close();	//关闭连接
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			tLocal.remove();
		}
		logger.debug("TestContext clearBasePosConfig() invoke!");
	}
	
	/**
	 * description：初始化一个pos 线程的基础内容		synchronized{because num}
	 * @throws Exception
	 * @time 2011-9-22   下午05:57:42
	 * @author Seek
	 */
	public static final synchronized void initBasePosConfig() throws Exception{
		logger.debug("TestContext initBasePosConfig() invoke!");
		logger.debug("current num="+num);
		if(num == maxPos){
			return;
		}
		
		if(tLocal.get() == null){
			num++;
			
			//get a BasePosConfig from static map, remove it!
			//save to new BasePosConfig for threadLocal.
			BasePosConfig tempBasePosConfig = getPosMap().get(num.toString());
			
			BasePosConfig basePosConfig = new BasePosConfig();
			basePosConfig.setNumber(num);
			basePosConfig.setPosId(tempBasePosConfig.getPosId());
			basePosConfig.setSecret(tempBasePosConfig.getSecret());
			basePosConfig.setSocket(new Socket(getPosServerIp(), getPosServerPort()));
			basePosConfig.setSequence(1);
			tLocal.set(basePosConfig);
			
			getPosMap().remove(num.toString());
		}
	}
	
	/**
	 * description：自增流水号
	 * @time 2011-9-23   下午02:14:14
	 * @author Seek
	 */
	public static final void incrementSequence(){
		logger.debug("TestContext incrementSequence() invoke!");
		logger.debug("current sequence="+tLocal.get().getSequence());
		if(tLocal.get().getSequence() >= 999999){
			tLocal.get().setSequence(0);
		}
		tLocal.get().setSequence(tLocal.get().getSequence() + 1);
	}
	
	public static final BasePosConfig getBasePosConfig(){
		return tLocal.get();
	}
	
	public static Map<String, BasePosConfig> getPosMap() {
		return POS_MAP;
	}
	
	public static Long getMaxPos() {
		return maxPos;
	}

	public static void setMaxPos(Long maxPos) {
		TestContext.maxPos = maxPos;
	}
	
	public static String getPosServerIp() {
		return posServerIp;
	}

	public static void setPosServerIp(String posServerIp) {
		TestContext.posServerIp = posServerIp;
	}

	public static Integer getPosServerPort() {
		return posServerPort;
	}

	public static void setPosServerPort(Integer posServerPort) {
		TestContext.posServerPort = posServerPort;
	}
	
	public static SimpleCmdCodecFactory getCmdCodecFactory() {
		return cmdCodecFactory;
	}

	public static void setCmdCodecFactory(SimpleCmdCodecFactory cmdCodecFactory) {
		TestContext.cmdCodecFactory = cmdCodecFactory;
	}
	
	public static PackageHeadCodec getPackageHeadCodec() {
		return packageHeadCodec;
	}

	public static void setPackageHeadCodec(PackageHeadCodec packageHeadCodec) {
		TestContext.packageHeadCodec = packageHeadCodec;
	}
	
	public static void setCharset(Charset charset) {
		TestContext.charset = charset;
	}

	public static Charset getCharset() {
		return charset;
	}
	
	public static String getCsvSeparator() {
		return csvSeparator;
	}

	public static void setCsvSeparator(String csvSeparator) {
		TestContext.csvSeparator = csvSeparator;
	}
	
	public static long getTimestampRange() {
		return timestampRange;
	}

	public static void setTimestampRange(long timestampRange) {
		TestContext.timestampRange = timestampRange;
	}
	
	public static PackageUtil getPackageUtil() {
		return packageUtil;
	}

	public static void setPackageUtil(PackageUtil packageUtil) {
		TestContext.packageUtil = packageUtil;
	}
	
}
