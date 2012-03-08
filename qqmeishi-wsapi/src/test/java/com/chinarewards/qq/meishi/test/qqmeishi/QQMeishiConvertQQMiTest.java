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
import com.chinarewards.qq.meishi.util.DigestUtil;
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
		conf.setProperty(QQ_MEISHI_COMM_SECRET_KEY,
				"NDA2ZTkwOTExZjlkZDY3ZTIxMTU1OTY0NmVlYzVmY2Q=");
		conf.setProperty(QQ_MEISHI_HOST_ADDRESS_KEY, "open.meishi.qq.com");
		conf.setProperty(QQ_MEISHI_CONVERT_URL_KEY,
				"http://open.meishi.qq.com/pos.php");	//localhost:9001  for monitor
		
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
	/**
	 	腾讯测试环境数据：
	 	
	   	结果					verifyCode(token码)	posid(pos机id)	consume(金额)	password(密码)
		posid错误			随便6位数			pos333333		33.3			随便
		金额错误				随便6位数			REWARDS-0001	大于5万			随便
		商家密码错误			随便6位数			pos345			33.3			随便
		无密码请求正确		255416				REWARDS-0001	33.3			空
		有密码请求正确		828572				REWARDS-0001	33.3			123456
	 */
	@Test
	public void testConvertQQmi() {
		QQMeishiService qqMeishiService = getInjector().getInstance(
				QQMeishiService.class);
		
		QQMeishiResp<QQMeishiConvertQQMiRespVO> actualVO = null;
		QQMeishiConvertQQMiReqVO meishiConvertQQMiReqVO = new QQMeishiConvertQQMiReqVO();
		
		meishiConvertQQMiReqVO.setConsume(33.3f);
		meishiConvertQQMiReqVO.setPosid("REWARDS-0001");
		try {
			meishiConvertQQMiReqVO.setPassword(DigestUtil.digestData("123456", DigestUtil.MD5));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		meishiConvertQQMiReqVO.setVerifyCode("828572");
		
		try {
			actualVO = qqMeishiService.convertQQMi(meishiConvertQQMiReqVO);
		} catch (QQMeishiServerUnreachableException e) {
			e.printStackTrace();
		} catch (QQMeishiServerLinkNotFoundException e) {
			System.out.println(e.getHttpStatusCode());
			System.out.println(e.getRawContent());
			e.printStackTrace();
			
		} catch (QQMeishiServerRespException e) {
			System.out.println(e.getHttpStatusCode());
			System.out.println(e.getRawContent());
			
			e.printStackTrace();
		} catch (QQMeishiRespDataParseException e) {
			System.out.println(e.getHttpStatusCode());
			System.out.println(e.getRawContent());
			
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
