package com.chinarewards.qq.meishi.test.qqmeishi;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.junit.Ignore;
import org.junit.Test;

import com.chinarewards.qq.meishi.exception.QQMeishiReadRespStreamException;
import com.chinarewards.qq.meishi.exception.QQMeishiReqDataDigestException;
import com.chinarewards.qq.meishi.exception.QQMeishiRespDataParseException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerLinkNotFoundException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerRespException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerUnreachableException;
import com.chinarewards.qq.meishi.guice.TestAppModule;
import com.chinarewards.qq.meishi.main.TestConfigModule;
import com.chinarewards.qq.meishi.service.QQMeishiService;
import com.chinarewards.qq.meishi.test.GuiceTest;
import com.chinarewards.qq.meishi.vo.QQMeishiConvertQQMiReqVO;
import com.chinarewards.qq.meishi.vo.QQMeishiConvertQQMiRespVO;
import com.chinarewards.qq.meishi.vo.common.QQMeishiResp;
import com.google.inject.Module;

/**
 * description：QQ美食兑换Q米测试
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-5   下午06:05:07
 * @author Seek
 */
@Ignore
public class QQMeishiConvertQQMiTest extends GuiceTest {
	
	private static final String QQ_MEISHI_HOST_ADDRESS_KEY = "qq.meishi.host";
	private static final String QQ_MEISHI_CONVERT_URL_KEY  = "qq.meishi.url.convertQQMi";
	private static final String QQ_MEISHI_COMM_SECRET_KEY  = "qq.meishi.communication.secretkey";
	
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
		conf.setProperty(QQ_MEISHI_COMM_SECRET_KEY, "NDA2ZTkwOTExZjlkZDY3ZTIxMTU1OTY0NmVlYzVmY2Q=");
		conf.setProperty(QQ_MEISHI_HOST_ADDRESS_KEY, "open.meishi.qq.com");
		conf.setProperty(QQ_MEISHI_CONVERT_URL_KEY, 
				"http://localhost:9001/pos.php");
		
		TestConfigModule confModule = new TestConfigModule(conf);
		return confModule;
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see com.chinarewards.qqgbpvn.main.test.JpaGuiceTest#tearDown()
	 */
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	
	/******************************** Test Case *******************************/

	@Test
	public void testConvertQQmi() {
		QQMeishiService qqMeishiService = getInjector().getInstance(
				QQMeishiService.class);
		
		QQMeishiResp<QQMeishiConvertQQMiRespVO> actualVO = null;
		QQMeishiConvertQQMiReqVO meishiConvertQQMiReqVO = new QQMeishiConvertQQMiReqVO();
		
		meishiConvertQQMiReqVO.setConsume(100.8f);
		meishiConvertQQMiReqVO.setPosid("REWARDS-0001");
		meishiConvertQQMiReqVO.setPassword("123456");
		meishiConvertQQMiReqVO.setVerifyCode("xxx123");
		
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
		
		
		System.out.println("testAccumulate() actual:"+actualVO);
	}
	/******************************** Test Case *******************************/
	
}
