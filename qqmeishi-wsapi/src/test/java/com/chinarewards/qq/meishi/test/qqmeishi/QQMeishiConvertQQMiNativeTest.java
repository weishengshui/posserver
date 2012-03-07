package com.chinarewards.qq.meishi.test.qqmeishi;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;

import com.chinarewards.qq.meishi.exception.QQMeishiReadRespStreamException;
import com.chinarewards.qq.meishi.exception.QQMeishiReqDataDigestException;
import com.chinarewards.qq.meishi.exception.QQMeishiRespDataParseException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerLinkNotFoundException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerRespException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerUnreachableException;
import com.chinarewards.qq.meishi.guice.TestAppModule;
import com.chinarewards.qq.meishi.main.ServletHolderFactory;
import com.chinarewards.qq.meishi.main.TestConfigModule;
import com.chinarewards.qq.meishi.service.QQMeishiService;
import com.chinarewards.qq.meishi.test.GuiceTest;
import com.chinarewards.qq.meishi.util.IoUtil;
import com.chinarewards.qq.meishi.vo.QQMeishiConvertQQMiReqVO;
import com.chinarewards.qq.meishi.vo.QQMeishiConvertQQMiRespVO;
import com.chinarewards.qq.meishi.vo.common.QQMeishiResp;
import com.google.inject.Module;

/**
 * description：QQ美食兑换Q米本地测试
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-5   下午06:05:07
 * @author Seek
 */
public class QQMeishiConvertQQMiNativeTest extends GuiceTest {
	
	private static final String QQ_MEISHI_CONVERT_URL_KEY  = "qq.meishi.url.convertQQMi";
	
	private static final String CHARSET = "UTF-8";
	
	private static final Integer JETTY_SERVER_PORT = 8085;
	
	/* json string filename and jetty servlet path */
	private static final String ACCUMULATE = "accumulate";
	private static final String ACCUMULATE_BADTOKEN = "accumulate-badtoken";
	private static final String ACCUMULATE_BADXACTPWD = "accumulate-badxactpwd";
	private static final String ACCUMULATE_HASPWD = "accumulate-haspwd";
	private static final String ACCUMULATE_ILLEGALUSER = "accumulate-illegaluser";
	private static final String ACCUMULATE_INVALIDAMOUNT = "accumulate-invalidamount";
	
	
	
	static private Map<String, QQMeishiConvertQQMiRespVO> resultsMap = null;
	static {
		resultsMap = Collections.synchronizedMap(new HashMap<String, QQMeishiConvertQQMiRespVO>());
	}
	
	private Server server = new Server(0);

	@Override
	protected Module[] getModules() {
		TestAppModule appModules = new TestAppModule();
		appModules.getList().add(createTestConfigModule());
		
		return new Module[] { appModules };
	}

	private Module createTestConfigModule() {

		Configuration conf = new BaseConfiguration();
		// hard-coded config
		conf.setProperty("server.port", 0);
		
		// URL for QQ meishi
		conf.setProperty("qq.meishi.host", "localhost:"+JETTY_SERVER_PORT);
//		conf.setProperty("qq.meishi.url.convertQQMi",			TODO 不用了，在每个test中设置
//				"http://localhost:8085/");

		TestConfigModule confModule = new TestConfigModule(conf);
		return confModule;
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		initTestServer();
	}

	/* (non-Javadoc)
	 * @see com.chinarewards.qqgbpvn.main.test.JpaGuiceTest#tearDown()
	 */
	@Override
	public void tearDown() throws Exception {
		// stop server
		if (server.isStarted()) {
			server.stop();
		}
		
		super.tearDown();
	}

	private void initTestServer() throws Exception {
		// build test server start
		if (!server.isStarted()) {
			ServletHandler scHandler = new ServletHandler();
			
			scHandler.addServletWithMapping(
					getAccumulate(), "/"+ACCUMULATE);
			scHandler.addServletWithMapping(
					getAccumulateBadtoken(), "/"+ACCUMULATE_BADTOKEN);
			scHandler.addServletWithMapping(
					getAccumulateBadxactpwd(), "/"+ACCUMULATE_BADXACTPWD);
			scHandler.addServletWithMapping(
					getAccumulateHaspwd(), "/"+ACCUMULATE_HASPWD);
			scHandler.addServletWithMapping(
					getAccumulateIllegaluser(), "/"+ACCUMULATE_ILLEGALUSER);
			scHandler.addServletWithMapping(
					getAccumulateInvalidamount(), "/"+ACCUMULATE_INVALIDAMOUNT);
			
			// add handler to server
			server.addHandler(scHandler);
			server.getConnectors()[0].setPort(JETTY_SERVER_PORT);
			server.start();
		}
		// build test server end
	}
	
	/******************************* prepare data *****************************/
	private ServletHolder getAccumulate() throws Exception {
		final String filename = "mock_server" + "/" + ACCUMULATE;
		
		QQMeishiConvertQQMiRespVO expectedVO = new QQMeishiConvertQQMiRespVO();
		expectedVO.setValidCode(0);
		expectedVO.setHasPassword(false);
		expectedVO.setTradeTime("20120131T234058+0800");
		expectedVO.setTitle("QQ美食极品客联名%会员");
		expectedVO.setTip("会员1234在极品客实际消费100元。*使用优惠券：[优惠券内容]。%使用积分300积分，抵现金3元。" + 
				"*恭喜你获得300积分/返券：[优惠内容]。10积分即可抵1元使用！*详情访问QQ美食网%用户签名：%%%%" +
				"进入http://meishi.qq.com查看详情。");
		expectedVO.setPassword(null);
		
		String url = "http://localhost:" + 10000 + "/" + ACCUMULATE;
		resultsMap.put(url, expectedVO);
		
		
		// load from resource
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(filename);
		return ServletHolderFactory.getServletHolder(IoUtil.readStream(is, CHARSET), CHARSET);
	}
	
	private ServletHolder getAccumulateBadtoken() throws Exception {
		final String filename = "mock_server" + "/" + ACCUMULATE_BADTOKEN;
		
		QQMeishiConvertQQMiRespVO expectedVO = new QQMeishiConvertQQMiRespVO();
		expectedVO.setValidCode(2);
		expectedVO.setHasPassword(false);
		expectedVO.setTradeTime("20120131T234058+0800");
		expectedVO.setTitle("QQ美食极品客联名%会员");
		expectedVO.setTip("会员1234在极品客实际消费100元。*使用优惠券：[优惠券内容]。%使用积分300积分，抵现金3元。" +
				"*恭喜你获得300积分/返券：[优惠内容]。10积分即可抵1元使用！*详情访问QQ美食网%用户签名：%%%%" +
				"进入http://meishi.qq.com查看详情。");
		expectedVO.setPassword(null);
		
		String url = "http://localhost:" + 10000 + "/" + ACCUMULATE_BADTOKEN;
		resultsMap.put(url, expectedVO);
		
		
		// load from resource
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(filename);
		return ServletHolderFactory.getServletHolder(IoUtil.readStream(is, CHARSET), CHARSET);
	}
	
	private ServletHolder getAccumulateBadxactpwd() throws Exception {
		final String filename = "mock_server" + "/" + ACCUMULATE_BADXACTPWD;
		
		QQMeishiConvertQQMiRespVO expectedVO = new QQMeishiConvertQQMiRespVO();
		expectedVO.setValidCode(0);
		expectedVO.setHasPassword(false);
		expectedVO.setTradeTime("20120131T234058+0800");
		expectedVO.setTitle("QQ美食极品客联名%会员");
		expectedVO.setTip("会员1234在极品客实际消费100元。*使用优惠券：[优惠券内容]。%使用积分300积分，抵现金3元。" +
				"*恭喜你获得300积分/返券：[优惠内容]。10积分即可抵1元使用！*详情访问QQ美食网%用户签名：%%%%" +
				"进入http://meishi.qq.com查看详情。");
		expectedVO.setPassword("");
		
		String url = "http://localhost:" + 10000 + "/" + ACCUMULATE_BADXACTPWD;
		resultsMap.put(url, expectedVO);
		
		
		// load from resource
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(filename);
		return ServletHolderFactory.getServletHolder(IoUtil.readStream(is, CHARSET), CHARSET);
	}
	
	private ServletHolder getAccumulateHaspwd() throws Exception {
		final String filename = "mock_server" + "/" + ACCUMULATE_HASPWD;
		
		QQMeishiConvertQQMiRespVO expectedVO = new QQMeishiConvertQQMiRespVO();
		expectedVO.setValidCode(0);
		expectedVO.setHasPassword(true);
		expectedVO.setTradeTime("20120131T234058+0800");
		expectedVO.setTitle("QQ美食极品客联名%会员");
		expectedVO.setTip("会员1234在极品客实际消费100元。*使用优惠券：[优惠券内容]。%使用积分300积分，抵现金3元。" +
				"*恭喜你获得300积分/返券：[优惠内容]。10积分即可抵1元使用！*详情访问QQ美食网%用户签名：%%%%" +
				"进入http://meishi.qq.com查看详情。");
		expectedVO.setPassword("123456789");
		
		String url = "http://localhost:" + 10000 + "/" + ACCUMULATE_HASPWD;
		resultsMap.put(url, expectedVO);
		
		
		// load from resource
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(filename);
		return ServletHolderFactory.getServletHolder(IoUtil.readStream(is, CHARSET), CHARSET);
	}
	
	private ServletHolder getAccumulateIllegaluser() throws Exception {
		final String filename = "mock_server" + "/" + ACCUMULATE_ILLEGALUSER;
		
		QQMeishiConvertQQMiRespVO expectedVO = new QQMeishiConvertQQMiRespVO();
		expectedVO.setValidCode(3);
		expectedVO.setHasPassword(false);
		expectedVO.setTradeTime("20120131T234058+0800");
		expectedVO.setTitle("QQ美食极品客联名%会员");
		expectedVO.setTip("会员1234在极品客实际消费100元。*使用优惠券：[优惠券内容]。%使用积分300积分，抵现金3元。" +
				"*恭喜你获得300积分/返券：[优惠内容]。10积分即可抵1元使用！*详情访问QQ美食网%用户签名：%%%%" +
				"进入http://meishi.qq.com查看详情。");
		expectedVO.setPassword(null);
		
		String url = "http://localhost:" + 10000 + "/" + ACCUMULATE_ILLEGALUSER;
		resultsMap.put(url, expectedVO);
		
		
		// load from resource
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(filename);
		return ServletHolderFactory.getServletHolder(IoUtil.readStream(is, CHARSET), CHARSET);
	}
	
	private ServletHolder getAccumulateInvalidamount() throws Exception {
		final String filename = "mock_server" + "/" + ACCUMULATE_INVALIDAMOUNT;
		
		QQMeishiConvertQQMiRespVO expectedVO = new QQMeishiConvertQQMiRespVO();
		expectedVO.setValidCode(4);
		expectedVO.setHasPassword(false);
		expectedVO.setTradeTime("20120131T234058+0800");
		expectedVO.setTitle("QQ美食极品客联名%会员");
		expectedVO.setTip("会员1234在极品客实际消费100元。*使用优惠券：[优惠券内容]。%使用积分300积分，抵现金3元。" +
				"*恭喜你获得300积分/返券：[优惠内容]。10积分即可抵1元使用！*详情访问QQ美食网%用户签名：%%%%" +
				"进入http://meishi.qq.com查看详情。");
		expectedVO.setPassword(null);
		
		String url = "http://localhost:" + 10000 + "/" + ACCUMULATE_INVALIDAMOUNT;
		resultsMap.put(url, expectedVO);
		
		
		// load from resource
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(filename);
		return ServletHolderFactory.getServletHolder(IoUtil.readStream(is, CHARSET), CHARSET);
	}
	/******************************* prepare data *****************************/
	
	
	
	
	
	/******************************** Test Case *******************************/
	private QQMeishiResp<QQMeishiConvertQQMiRespVO> baseTest(final String url){
		Configuration configuration = getInjector().getInstance(
				Configuration.class);
		
		configuration.clearProperty(QQ_MEISHI_CONVERT_URL_KEY);
		configuration.addProperty(QQ_MEISHI_CONVERT_URL_KEY, url);
		
		QQMeishiService qqMeishiService = getInjector().getInstance(
				QQMeishiService.class);
		
		QQMeishiResp<QQMeishiConvertQQMiRespVO> actualVO = null;
		QQMeishiConvertQQMiReqVO meishiConvertQQMiReqVO = new QQMeishiConvertQQMiReqVO();
		
		try {
			actualVO = qqMeishiService.convertQQMi(meishiConvertQQMiReqVO);
		} catch (QQMeishiServerUnreachableException e) {
			e.printStackTrace();
		} catch (QQMeishiServerLinkNotFoundException e) {
			e.printStackTrace();
		} catch (QQMeishiServerRespException e) {
			e.printStackTrace();
		} catch (QQMeishiRespDataParseException e) {
			e.printStackTrace();
		} catch (QQMeishiReadRespStreamException e) {
			e.printStackTrace();
		} catch (QQMeishiReqDataDigestException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(resultsMap.get(url), actualVO);
		return actualVO;
	}
	
	
	@Test
	public void testAccumulate() {
		final String url = "http://localhost:" + 10000 + "/" + ACCUMULATE;

		QQMeishiResp<QQMeishiConvertQQMiRespVO> actualVO = baseTest(url);
		System.out.println("testAccumulate() actual:"+actualVO);
	}
	
	@Test
	public void testAccumulateBadtoken() {
		final String url = "http://localhost:" + 10000 + "/" + ACCUMULATE_BADTOKEN;
		
		QQMeishiResp<QQMeishiConvertQQMiRespVO> actualVO = baseTest(url);
		System.out.println("testAccumulateBadtoken() actual:"+actualVO);
	}
	
	@Test
	public void testAccumulateBadxactpwd() {
		final String url = "http://localhost:" + 10000 + "/" + ACCUMULATE_BADXACTPWD;
		
		QQMeishiResp<QQMeishiConvertQQMiRespVO> actualVO = baseTest(url);
		System.out.println("testAccumulateBadxactpwd() actual:"+actualVO);
	}
	
	@Test
	public void testAccumulateHaspwd() {
		final String url = "http://localhost:" + 10000 + "/" + ACCUMULATE_HASPWD;
		
		QQMeishiResp<QQMeishiConvertQQMiRespVO> actualVO = baseTest(url);
		System.out.println("testAccumulateHaspwd() actual:"+actualVO);
	}
	
	@Test
	public void testAccumulateIllegaluser() {
		final String url = "http://localhost:" + 10000 + "/" + ACCUMULATE_ILLEGALUSER;
		
		QQMeishiResp<QQMeishiConvertQQMiRespVO> actualVO = baseTest(url);
		System.out.println("testAccumulateIllegaluser() actual:"+actualVO);
	}
	
	@Test
	public void testAccumulateInvalidamount() {
		final String url = "http://localhost:" + 10000 + "/" + ACCUMULATE_INVALIDAMOUNT;
		
		QQMeishiResp<QQMeishiConvertQQMiRespVO> actualVO = baseTest(url);
		System.out.println("testAccumulateInvalidamount() actual:"+actualVO);
	}
	/******************************** Test Case *******************************/
	
}
