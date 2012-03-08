package com.chinarewards.qq.meishi.test.qqmeishi;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;

import com.chinarewards.qq.meishi.exception.QQMeishiRespDataParseException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerLinkNotFoundException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerRespException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerUnreachableException;
import com.chinarewards.qq.meishi.guice.TestAppModule;
import com.chinarewards.qq.meishi.main.TestConfigModule;
import com.chinarewards.qq.meishi.service.QQMeishiService;
import com.chinarewards.qq.meishi.test.GuiceTest;
import com.chinarewards.qq.meishi.test.qqmeishi.support.convertqqmi.ConvertQQMiPrepNativeData;
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
	
	private static final Integer JETTY_SERVER_PORT = 9001;
	
	/* json string filename and jetty servlet path */
	private static final String ACCUMULATE = "accumulate";
	private static final String ACCUMULATE_BADTOKEN = "accumulate-badtoken";
	private static final String ACCUMULATE_BADXACTPWD = "accumulate-badxactpwd";
	private static final String ACCUMULATE_HASPWD = "accumulate-haspwd";
	private static final String ACCUMULATE_ILLEGALUSER = "accumulate-illegaluser";
	private static final String ACCUMULATE_INVALIDAMOUNT = "accumulate-invalidamount";
	
	/* exception case */
	private static final String ERR_SERVER_UNREACHABLE = "err_server-unreachable";	/* 不需要任何内容 */
	private static final String ERR_PAGE_NOT_FOUND = "err_page-not-found";	/* 不需要任何内容 */
	private static final String ERR_SERVER_RESP_EXCEPTION = "err_server-resp-exception";	/* 动态生成的servlet */
	private static final String ERR_JSON_ERROR = "err_json-error";
	
	private Server server = new Server(0);
	
	private ConvertQQMiPrepNativeData prepData = ConvertQQMiPrepNativeData.getInstance();

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
//		conf.setProperty("qq.meishi.url.convertQQMi",			TODO 不用了，在每个test中动态设置
//				"http://localhost:9001/");

		TestConfigModule confModule = new TestConfigModule(conf);
		return confModule;
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		try {
			initTestServer();
		} catch (Throwable e) {
			throw new Exception(e);
		}
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

	private void initTestServer() throws Throwable {
		// build test server start
		if (!server.isStarted()) {
			ServletHandler scHandler = new ServletHandler();
			
			scHandler.addServletWithMapping(prepData.getAccumulate(ACCUMULATE),
					"/" + ACCUMULATE);
			scHandler.addServletWithMapping(
					prepData.getAccumulateBadtoken(ACCUMULATE_BADTOKEN), 
					"/" + ACCUMULATE_BADTOKEN);
			scHandler.addServletWithMapping(
					prepData.getAccumulateBadxactpwd(ACCUMULATE_BADXACTPWD),
					"/" + ACCUMULATE_BADXACTPWD);
			scHandler.addServletWithMapping(
					prepData.getAccumulateHaspwd(ACCUMULATE_HASPWD), 
					"/" + ACCUMULATE_HASPWD);
			scHandler.addServletWithMapping(
					prepData.getAccumulateIllegaluser(ACCUMULATE_ILLEGALUSER),
					"/" + ACCUMULATE_ILLEGALUSER);
			scHandler.addServletWithMapping(prepData
					.getAccumulateInvalidamount(ACCUMULATE_INVALIDAMOUNT), 
					"/" + ACCUMULATE_INVALIDAMOUNT);
			
			scHandler.addServletWithMapping(prepData
					.getErrServerRespException(ERR_SERVER_RESP_EXCEPTION), 
					"/" + ERR_SERVER_RESP_EXCEPTION);
			scHandler.addServletWithMapping(prepData
					.getErrJsonError(ERR_JSON_ERROR), 
					"/" + ERR_JSON_ERROR);
			
			// add handler to server
			server.addHandler(scHandler);
			server.getConnectors()[0].setPort(JETTY_SERVER_PORT);
			server.start();
		}
		// build test server end
	}
	
	
	/******************************** Test Case *******************************/
	private QQMeishiResp<QQMeishiConvertQQMiRespVO> baseTest(final String url)
			throws Throwable {
		Configuration configuration = getInjector().getInstance(
				Configuration.class);

		configuration.clearProperty(QQ_MEISHI_CONVERT_URL_KEY);
		configuration.addProperty(QQ_MEISHI_CONVERT_URL_KEY, url);

		QQMeishiService qqMeishiService = getInjector().getInstance(
				QQMeishiService.class);

		QQMeishiResp<QQMeishiConvertQQMiRespVO> actualVO = null;
		QQMeishiConvertQQMiReqVO meishiConvertQQMiReqVO = new QQMeishiConvertQQMiReqVO();
		actualVO = qqMeishiService.convertQQMi(meishiConvertQQMiReqVO);
		
		Assert.assertEquals(prepData.getResMap().get(url), actualVO);
		return actualVO;
	}
	
	
	@Test
	public void testAccumulate() throws Throwable {
		final String url = prepData.buildURL(ACCUMULATE);

		QQMeishiResp<QQMeishiConvertQQMiRespVO> actualVO = baseTest(url);
		System.out.println("testAccumulate() actual:"+actualVO);
	}
	
	@Test
	public void testAccumulateBadtoken() throws Throwable {
		final String url = prepData.buildURL(ACCUMULATE_BADTOKEN);
		
		QQMeishiResp<QQMeishiConvertQQMiRespVO> actualVO = baseTest(url);
		System.out.println("testAccumulateBadtoken() actual:"+actualVO);
	}
	
	@Test
	public void testAccumulateBadxactpwd() throws Throwable {
		final String url = prepData.buildURL(ACCUMULATE_BADXACTPWD);
		
		QQMeishiResp<QQMeishiConvertQQMiRespVO> actualVO = baseTest(url);
		System.out.println("testAccumulateBadxactpwd() actual:"+actualVO);
	}
	
	@Test
	public void testAccumulateHaspwd() throws Throwable {
		final String url = prepData.buildURL(ACCUMULATE_HASPWD);
		
		QQMeishiResp<QQMeishiConvertQQMiRespVO> actualVO = baseTest(url);
		System.out.println("testAccumulateHaspwd() actual:"+actualVO);
	}
	
	@Test
	public void testAccumulateIllegaluser() throws Throwable {
		final String url = prepData.buildURL(ACCUMULATE_ILLEGALUSER);
		
		QQMeishiResp<QQMeishiConvertQQMiRespVO> actualVO = baseTest(url);
		System.out.println("testAccumulateIllegaluser() actual:"+actualVO);
	}
	
	@Test
	public void testAccumulateInvalidamount() throws Throwable {
		final String url = prepData.buildURL(ACCUMULATE_INVALIDAMOUNT);
		
		QQMeishiResp<QQMeishiConvertQQMiRespVO> actualVO = baseTest(url);
		System.out.println("testAccumulateInvalidamount() actual:"+actualVO);
	}
	
	@Test	/* error testcase */
	public void testServerRespException() throws Throwable {
		final String url = prepData.buildURL(ERR_SERVER_RESP_EXCEPTION);
		
		try{
			baseTest(url);
		}catch(QQMeishiServerRespException e){
			System.out.println("testServerRespException():"+e.getHttpStatusCode());
			System.out.println("testServerRespException():"+e.getRawContent());
			Assert.assertTrue(Boolean.TRUE);
			return;
		}
		Assert.assertTrue(Boolean.FALSE);
	}
	
	@Test	/* error testcase */
	public void testJsonError() throws Throwable {
		final String url = prepData.buildURL(ERR_JSON_ERROR);
		
		try{
			baseTest(url);
		}catch(QQMeishiRespDataParseException e){
			System.out.println("testJsonError():"+e.getHttpStatusCode());
			System.out.println("testJsonError():"+e.getRawContent());
			Assert.assertTrue(Boolean.TRUE);
			return;
		}
		Assert.assertTrue(Boolean.FALSE);
	}
	
	@Test	/* error testcase */
	public void testPageNotFound() throws Throwable {
		final String url = prepData.buildURL(ERR_PAGE_NOT_FOUND);
		
		try{
			baseTest(url);
		}catch(QQMeishiServerLinkNotFoundException e){
			System.out.println("testPageNotFound():"+e.getHttpStatusCode());
			System.out.println("testPageNotFound():"+e.getRawContent());
			Assert.assertTrue(Boolean.TRUE);
			return;
		}
		Assert.assertTrue(Boolean.FALSE);
	}
	
	@Test	/* error testcase */
	public void testServerUnreachable() throws Throwable {
		// stop server
		if (server.isStarted()) {
			server.stop();
		}
		
		final String url = prepData.buildURL(ERR_SERVER_UNREACHABLE);
		
		try{
			baseTest(url);
		}catch(QQMeishiServerUnreachableException e){
			Assert.assertTrue(Boolean.TRUE);
			return;
		}
		Assert.assertTrue(Boolean.FALSE);
	}
	/******************************** Test Case *******************************/
	
}
