package com.chinarewards.qq.meishi.test.qqmeishi.support.convertqqmi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Ignore;
import org.mortbay.jetty.servlet.ServletHolder;

import com.chinarewards.qq.meishi.main.HardCodedServlet;
import com.chinarewards.qq.meishi.main.ServletHolderFactory;
import com.chinarewards.qq.meishi.util.IoUtil;
import com.chinarewards.qq.meishi.util.json.JsonUtil;
import com.chinarewards.qq.meishi.vo.QQMeishiConvertQQMiRespVO;
import com.chinarewards.qq.meishi.vo.common.QQMeishiResp;

/**
 * description：QQ美食兑换Q米  本地测试数据准备
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-7   下午06:32:48
 * @author Seek
 */
@Ignore
public final class ConvertQQMiPrepNativeData {
	
	private static ConvertQQMiPrepNativeData thiz = new ConvertQQMiPrepNativeData();
	
	private Map<String, QQMeishiResp<QQMeishiConvertQQMiRespVO>> resultsMap = Collections
			.synchronizedMap(new HashMap<String, QQMeishiResp<QQMeishiConvertQQMiRespVO>>());
	
	private static final Boolean isSerialize = false;	//close serialize to resouce file
	
	/* native monitor port */
	private static final Integer MONITOR_PORT = 9001;	//localhost:9011  for monitor
	
	private static final String CHARSET = "UTF-8";
	
	private static final String MOCK_PATH = "mock_convert_qq_mi";
	
	private ConvertQQMiPrepNativeData(){
	}
	public static ConvertQQMiPrepNativeData getInstance(){
		return thiz;
	}
	
	public String buildURL(String targetName){
		String url = "http://localhost:" + MONITOR_PORT + "/" + targetName;
		return url;
	}
	
	public Map<String, QQMeishiResp<QQMeishiConvertQQMiRespVO>> getResMap(){
		return resultsMap;
	}
	
	
	private void serializeToTarget(String filename, byte[] bytes)
			throws Throwable {
		if(isSerialize) {
			FileOutputStream out = new FileOutputStream(filename, false);
			out.write(bytes);
			out.flush();
			out.close();
		}
	}
	
	/******************************* prepare data *****************************/
	public ServletHolder getAccumulate(String targetName) throws Throwable {
		final String resourcename = MOCK_PATH + "/" + targetName;
		
		QQMeishiResp<QQMeishiConvertQQMiRespVO> expectedVO = new QQMeishiResp<QQMeishiConvertQQMiRespVO>(
				new QQMeishiConvertQQMiRespVO());
		expectedVO.setErrCode(0);
		expectedVO.setErrMessage("成功!");
		
		expectedVO.getResult().setValidCode(0);
		expectedVO.getResult().setHasPassword(false);
		expectedVO.getResult().setTradeTime("20120131T234058+0800");
		expectedVO.getResult().setTitle("QQ美食极品客联名%会员");
		expectedVO.getResult().setTip("会员1234在极品客实际消费100元。*使用优惠券：[优惠券内容]。%使用积分300积分，抵现金3元。" + 
				"*恭喜你获得300积分/返券：[优惠内容]。10积分即可抵1元使用！*详情访问QQ美食网%用户签名：%%%%" +
				"进入http://meishi.qq.com查看详情。");
		expectedVO.getResult().setPassword(null);
		
		String url = buildURL(targetName);
		resultsMap.put(url, expectedVO);
		
		/* write current content to target file */
		String jsonStr = JsonUtil.formatObject(expectedVO);
		serializeToTarget(Thread.currentThread().getContextClassLoader()
				.getResource(resourcename).getFile(), jsonStr.getBytes(CHARSET));
		
		// load from resource
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(resourcename);
		return ServletHolderFactory.getServletHolder(IoUtil.readStream(is, CHARSET), CHARSET);
	}
	
	public ServletHolder getAccumulateBadtoken(String targetName) throws Throwable {
		final String resourcename = MOCK_PATH + "/" + targetName;
		
		QQMeishiResp<QQMeishiConvertQQMiRespVO> expectedVO = new QQMeishiResp<QQMeishiConvertQQMiRespVO>(
				new QQMeishiConvertQQMiRespVO());
		expectedVO.setErrCode(0);
		expectedVO.setErrMessage("成功!");
		
		expectedVO.getResult().setValidCode(2);
		expectedVO.getResult().setHasPassword(false);
		expectedVO.getResult().setTradeTime("20120131T234058+0800");
		expectedVO.getResult().setTitle("QQ美食极品客联名%会员");
		expectedVO.getResult().setTip("会员1234在极品客实际消费100元。*使用优惠券：[优惠券内容]。%使用积分300积分，抵现金3元。" +
				"*恭喜你获得300积分/返券：[优惠内容]。10积分即可抵1元使用！*详情访问QQ美食网%用户签名：%%%%" +
				"进入http://meishi.qq.com查看详情。");
		expectedVO.getResult().setPassword(null);
		
		String url = buildURL(targetName);
		resultsMap.put(url, expectedVO);
		
		/* write current content to target file */
		String jsonStr = JsonUtil.formatObject(expectedVO);
		serializeToTarget(Thread.currentThread().getContextClassLoader()
				.getResource(resourcename).getFile(), jsonStr.getBytes(CHARSET));
		
		// load from resource
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(resourcename);
		return ServletHolderFactory.getServletHolder(IoUtil.readStream(is, CHARSET), CHARSET);
	}
	
	public ServletHolder getAccumulateBadxactpwd(String targetName) throws Throwable {
		final String resourcename = MOCK_PATH + "/" + targetName;
		
		QQMeishiResp<QQMeishiConvertQQMiRespVO> expectedVO = new QQMeishiResp<QQMeishiConvertQQMiRespVO>(
				new QQMeishiConvertQQMiRespVO());
		expectedVO.setErrCode(0);
		expectedVO.setErrMessage("成功!");
		
		expectedVO.getResult().setValidCode(0);
		expectedVO.getResult().setHasPassword(false);
		expectedVO.getResult().setTradeTime("20120131T234058+0800");
		expectedVO.getResult().setTitle("QQ美食极品客联名%会员");
		expectedVO.getResult().setTip("会员1234在极品客实际消费100元。*使用优惠券：[优惠券内容]。%使用积分300积分，抵现金3元。" +
				"*恭喜你获得300积分/返券：[优惠内容]。10积分即可抵1元使用！*详情访问QQ美食网%用户签名：%%%%" +
				"进入http://meishi.qq.com查看详情。");
		expectedVO.getResult().setPassword("");
		
		String url = buildURL(targetName);
		resultsMap.put(url, expectedVO);
		
		/* write current content to target file */
		String jsonStr = JsonUtil.formatObject(expectedVO);
		serializeToTarget(Thread.currentThread().getContextClassLoader()
				.getResource(resourcename).getFile(), jsonStr.getBytes(CHARSET));
		
		// load from resource
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(resourcename);
		return ServletHolderFactory.getServletHolder(IoUtil.readStream(is, CHARSET), CHARSET);
	}
	
	public ServletHolder getAccumulateHaspwd(String targetName) throws Throwable {
		final String resourcename = MOCK_PATH + "/" + targetName;
		
		QQMeishiResp<QQMeishiConvertQQMiRespVO> expectedVO = new QQMeishiResp<QQMeishiConvertQQMiRespVO>(
				new QQMeishiConvertQQMiRespVO());
		expectedVO.setErrCode(0);
		expectedVO.setErrMessage("成功!");
		
		expectedVO.getResult().setValidCode(0);
		expectedVO.getResult().setHasPassword(true);
		expectedVO.getResult().setTradeTime("20120131T234058+0800");
		expectedVO.getResult().setTitle("QQ美食极品客联名%会员");
		expectedVO.getResult().setTip("会员1234在极品客实际消费100元。*使用优惠券：[优惠券内容]。%使用积分300积分，抵现金3元。" +
				"*恭喜你获得300积分/返券：[优惠内容]。10积分即可抵1元使用！*详情访问QQ美食网%用户签名：%%%%" +
				"进入http://meishi.qq.com查看详情。");
		expectedVO.getResult().setPassword("123456789");
		
		String url = buildURL(targetName);
		resultsMap.put(url, expectedVO);
		
		/* write current content to target file */
		String jsonStr = JsonUtil.formatObject(expectedVO);
		serializeToTarget(Thread.currentThread().getContextClassLoader()
				.getResource(resourcename).getFile(), jsonStr.getBytes(CHARSET));
		
		// load from resource
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(resourcename);
		return ServletHolderFactory.getServletHolder(IoUtil.readStream(is, CHARSET), CHARSET);
	}
	
	public ServletHolder getAccumulateIllegaluser(String targetName) throws Throwable {
		final String resourcename = MOCK_PATH + "/" + targetName;
		
		QQMeishiResp<QQMeishiConvertQQMiRespVO> expectedVO = new QQMeishiResp<QQMeishiConvertQQMiRespVO>(
				new QQMeishiConvertQQMiRespVO());
		expectedVO.setErrCode(0);
		expectedVO.setErrMessage("成功!");
		
		expectedVO.getResult().setValidCode(3);
		expectedVO.getResult().setHasPassword(false);
		expectedVO.getResult().setTradeTime("20120131T234058+0800");
		expectedVO.getResult().setTitle("QQ美食极品客联名%会员");
		expectedVO.getResult().setTip("会员1234在极品客实际消费100元。*使用优惠券：[优惠券内容]。%使用积分300积分，抵现金3元。" +
				"*恭喜你获得300积分/返券：[优惠内容]。10积分即可抵1元使用！*详情访问QQ美食网%用户签名：%%%%" +
				"进入http://meishi.qq.com查看详情。");
		expectedVO.getResult().setPassword(null);
		
		String url = buildURL(targetName);
		resultsMap.put(url, expectedVO);
		
		/* write current content to target file */
		String jsonStr = JsonUtil.formatObject(expectedVO);
		serializeToTarget(Thread.currentThread().getContextClassLoader()
				.getResource(resourcename).getFile(), jsonStr.getBytes(CHARSET));
		
		// load from resource
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(resourcename);
		return ServletHolderFactory.getServletHolder(IoUtil.readStream(is, CHARSET), CHARSET);
	}
	
	public ServletHolder getAccumulateInvalidamount(String targetName) throws Throwable {
		final String resourcename = MOCK_PATH + "/" + targetName;
		
		QQMeishiResp<QQMeishiConvertQQMiRespVO> expectedVO = new QQMeishiResp<QQMeishiConvertQQMiRespVO>(
				new QQMeishiConvertQQMiRespVO());
		expectedVO.setErrCode(0);
		expectedVO.setErrMessage("成功!");
		
		expectedVO.getResult().setValidCode(4);
		expectedVO.getResult().setHasPassword(false);
		expectedVO.getResult().setTradeTime("20120131T234058+0800");
		expectedVO.getResult().setTitle("QQ美食极品客联名%会员");
		expectedVO.getResult().setTip("会员1234在极品客实际消费100元。*使用优惠券：[优惠券内容]。%使用积分300积分，抵现金3元。" +
				"*恭喜你获得300积分/返券：[优惠内容]。10积分即可抵1元使用！*详情访问QQ美食网%用户签名：%%%%" +
				"进入http://meishi.qq.com查看详情。");
		expectedVO.getResult().setPassword(null);
		
		String url = buildURL(targetName);
		resultsMap.put(url, expectedVO);
		
		/* write current content to target file */
		String jsonStr = JsonUtil.formatObject(expectedVO);
		serializeToTarget(Thread.currentThread().getContextClassLoader()
				.getResource(resourcename).getFile(), jsonStr.getBytes(CHARSET));
		
		// load from resource
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(resourcename);
		return ServletHolderFactory.getServletHolder(IoUtil.readStream(is, CHARSET), CHARSET);
	}
	
	public ServletHolder getErrServerRespException(String targetName) throws Throwable {
		ServletHolder servletHolder = new ServletHolder();
		servletHolder.setServlet(new HardCodedServlet(){
			
			private static final long serialVersionUID = -3653028984873164395L;

			@Override
			protected void service(HttpServletRequest arg0,
					HttpServletResponse arg1) throws ServletException,
					IOException {
				super.service(arg0, arg1);
				
				if(true){
					throw new ServletException("a surprised!");
				}
			}
		});
		
		return servletHolder;
	}
	
	public ServletHolder getErrJsonError(String targetName) throws Throwable {
		final String resourcename = MOCK_PATH + "/" + targetName;
		
		StringBuffer sbuff = new StringBuffer();
		sbuff.append("error json!");
		
		/* write current content to target file */
		serializeToTarget(Thread.currentThread().getContextClassLoader()
				.getResource(resourcename).getFile(), sbuff.toString().getBytes(CHARSET));
		
		// load from resource
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(resourcename);
		return ServletHolderFactory.getServletHolder(IoUtil.readStream(is, CHARSET), CHARSET);
	}
	/******************************* prepare data *****************************/
	
}
