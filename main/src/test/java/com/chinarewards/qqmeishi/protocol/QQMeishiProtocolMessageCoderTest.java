/**
 * 
 */
package com.chinarewards.qqmeishi.protocol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.Calendar;

import org.apache.mina.core.buffer.IoBuffer;
import org.junit.Test;

import com.chinarewards.qqgbpvn.main.test.GuiceTest;
import com.chinarewards.qqgbvpn.main.protocol.cmd.QQMeishiRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.QQMeishiResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.QQMeishiBodyMessageCodec;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.QQMeishiBodyMessageResponseCodec;

/**
 * 
 * @author harry
 * @since 0.1.0
 */
public class QQMeishiProtocolMessageCoderTest extends GuiceTest {

	@Test
	public void testQQMeishiComdIdCodecTest() throws Exception {

		Charset charset = Charset.forName("GB2312");
		
		//request
		QQMeishiBodyMessageCodec codec = new QQMeishiBodyMessageCodec();
		QQMeishiRequestMessage requestMessage1 = new QQMeishiRequestMessage();
		requestMessage1.setCmdId(101);
		requestMessage1.setAmount(1.11);
		requestMessage1.setPassword("123456789");
		requestMessage1.setUserToken("abcd123");
		
		byte[] bodyMsg = codec.encode(requestMessage1, charset);
		IoBuffer in1 = IoBuffer.allocate(bodyMsg.length);
		in1.put(bodyMsg);
		in1.position(0);
		
//		System.out.println(CodecUtil.hexDumpAsString(bodyMsg));
		
		QQMeishiRequestMessage requestMessage2 = (QQMeishiRequestMessage)codec.decode(in1, charset);
		
		assertEquals(requestMessage1.getCmdId(), requestMessage2.getCmdId());
		assertEquals(requestMessage1.getUserToken(), requestMessage2.getUserToken());
		assertTrue(requestMessage1.getAmount() == requestMessage2.getAmount());
		assertEquals(requestMessage1.getPassword(), requestMessage2.getPassword());
		
		
		// reponse
		Calendar ca = Calendar.getInstance();
		ca.set(2012, Calendar.JANUARY, 31, 23, 58, 59);
		ca.set(Calendar.MILLISECOND, 987);

		QQMeishiResponseMessage responseMessage1 = new QQMeishiResponseMessage();
		responseMessage1.setCmdId(102);
		responseMessage1.setForcePwdNextAction((byte)1);
		responseMessage1.setServerErrorCode(1);
		responseMessage1.setQqwsErrorCode(1);
		responseMessage1.setResult(2);
		responseMessage1.setPassword("123456789");
		responseMessage1.setTip("abcdefg");
		responseMessage1.setTitle("gfedcba");
		responseMessage1.setXactTime(ca);
		
		QQMeishiBodyMessageResponseCodec responseCodec = new QQMeishiBodyMessageResponseCodec();
		
		byte[] responseBytes = responseCodec .encode(responseMessage1, charset);
		
		IoBuffer in2 = IoBuffer.allocate(responseBytes.length);
		in2.put(responseBytes);
		in2.position(0);
	
//		System.out.println(CodecUtil.hexDumpAsString(responseBytes));
		
		QQMeishiResponseMessage reponseMessage2 = (QQMeishiResponseMessage)responseCodec.decode(in2, charset);
			
		assertEquals(responseMessage1.getCmdId(), reponseMessage2.getCmdId());
		assertEquals(responseMessage1.getForcePwdNextAction(), reponseMessage2.getForcePwdNextAction());
		assertEquals(responseMessage1.getPassword(), reponseMessage2.getPassword());
		assertEquals(responseMessage1.getServerErrorCode(), reponseMessage2.getServerErrorCode());
		assertEquals(responseMessage1.getQqwsErrorCode(), reponseMessage2.getQqwsErrorCode());
		assertEquals(responseMessage1.getResult(), reponseMessage2.getResult());
		assertEquals(responseMessage1.getTip(), reponseMessage2.getTip());
		assertEquals(responseMessage1.getTitle(), reponseMessage2.getTitle());	

	}
	
}
