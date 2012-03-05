/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.Calendar;

import org.apache.mina.core.buffer.IoBuffer;
import org.junit.Test;

import com.chinarewards.qqgbpvn.main.test.GuiceTest;
import com.chinarewards.qqgbvpn.main.protocol.cmd.QQmeishiRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.QQmeishiResponseMessage;

/**
 * 
 * @author harry
 * @since 0.1.0
 */
public class QQmeishiProtocolMessageCoderTest extends GuiceTest {

	@Test
	public void testDoDecode_TwoPartsHalfHeader() throws Exception {

		Charset charset = Charset.forName("GB2312");
		
		//request
		QQmeishiBodyMessageCodec codec = new QQmeishiBodyMessageCodec();
		QQmeishiRequestMessage requestMessage1 = new QQmeishiRequestMessage();
		requestMessage1.setCmdId(101);
		requestMessage1.setAmount(1.11);
		requestMessage1.setPassword("123456789");
		requestMessage1.setUserToken("abcd123");
		
		byte[] bodyMsg = codec.encode(requestMessage1, charset);
		IoBuffer in1 = IoBuffer.allocate(bodyMsg.length);
		in1.put(bodyMsg);
		in1.position(0);
		
//		System.out.println(CodecUtil.hexDumpAsString(bodyMsg));
		
		QQmeishiRequestMessage requestMessage2 = (QQmeishiRequestMessage)codec.decode(in1, charset);
		
		assertEquals(requestMessage1.getCmdId(), requestMessage2.getCmdId());
		assertEquals(requestMessage1.getUserToken(), requestMessage2.getUserToken());
		assertTrue(requestMessage1.getAmount() == requestMessage2.getAmount());
		assertEquals(requestMessage1.getPassword(), requestMessage2.getPassword());
		
		
		// reponse
		Calendar ca = Calendar.getInstance();
		ca.set(2012, Calendar.JANUARY, 31, 23, 58, 59);
		ca.set(Calendar.MILLISECOND, 987);

		QQmeishiResponseMessage reponseMessage1 = new QQmeishiResponseMessage();
		reponseMessage1.setCmdId(102);
		reponseMessage1.setForce_pwd_next_action((byte)1);
		reponseMessage1.setResult(2);
		reponseMessage1.setPassword("123456789");
		reponseMessage1.setTip("abcdefg");
		reponseMessage1.setTitle("gfedcba");
		reponseMessage1.setXact_time(ca.getTime());
		
		QQmeishiBodyMessageResponseCodec responseCodec = new QQmeishiBodyMessageResponseCodec();
		
		byte[] responseBytes = responseCodec .encode(reponseMessage1, charset);
		
		IoBuffer in2 = IoBuffer.allocate(responseBytes.length);
		in2.put(responseBytes);
		in2.position(0);
		QQmeishiResponseMessage reponseMessage2 = (QQmeishiResponseMessage)responseCodec.decode(in2, charset);
		
//		System.out.println(CodecUtil.hexDumpAsString(responseBytes));
		
		assertEquals(reponseMessage1.getCmdId(), reponseMessage2.getCmdId());
		assertEquals(reponseMessage1.getForce_pwd_next_action(), reponseMessage2.getForce_pwd_next_action());
		assertEquals(reponseMessage1.getPassword(), reponseMessage2.getPassword());
		assertEquals(reponseMessage1.getResult(), reponseMessage2.getResult());
		assertEquals(reponseMessage1.getTip(), reponseMessage2.getTip());
		assertEquals(reponseMessage1.getTitle(), reponseMessage2.getTitle());	

	}
	
}
