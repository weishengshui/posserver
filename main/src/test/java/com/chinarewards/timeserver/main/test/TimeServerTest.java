package com.chinarewards.timeserver.main.test;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.util.HMAC_MD5;

public class TimeServerTest {
	private Socket socket;
	private static final int PORT = 1234;
	private InputStream is;
	private OutputStream os;

	@Before
	public void setUp() throws Exception {
		try {
			socket = new Socket("localhost", PORT);
		} catch (Exception e) {
			System.out.println("error message: " + e.getMessage());
		}
	}

	@After
	public void tearDown() throws Exception {
		is.close();
		os.close();
		socket.close();
	}

	@Test
	public void testTimeServer() throws Exception {
		assertTrue(socket != null);
		is = socket.getInputStream();
		os = socket.getOutputStream();
		int n;
		int checksum;
		byte[] response = new byte[100];

		// init 初始化POS机
		byte[] initContents = new byte[] {
				// SEQ 0
				0x00, 0x00, 0x00, 0x01,
				// ACK 4
				0x00, 0x00, 0x00, 0x00,
				// FLAGS 8
				0x00, 0x00,
				// CHECKSUM 10
				0x00, 0x00,
				// MESSAGESIZE 12
				0x00, 0x00, 0x00, 0x20,
				// CMD ID 16 请求的CMD ID是5
				0x00, 0x00, 0x00, 0x05,
				// posId 20
				'p', 'o', 's', '-', '0', '0', '0', '2', '\0', '\0', '\0', '\0', };
		// 计算校验和
		checksum = Tools.checkSum(initContents, initContents.length);
		Tools.putUnsignedShort(initContents, checksum, 10);
		// 发送初始化请求
		os.write(initContents);
		// 得到应答
		n = is.read(response);
		// 应答包的SEQ
		assertTrue(Arrays.equals(new byte[] { 0, 0, 0, 1 },
				Arrays.copyOfRange(response, 0, 4)));
		// 应答包的ACK
		assertTrue(Arrays.equals(new byte[] { 0, 0, 0, 0 },
				Arrays.copyOfRange(response, 4, 8)));
		// 应答包的MESSAGE SIZE是30
		assertTrue(Arrays.equals(new byte[] { 0, 0, 0, 30 },
				Arrays.copyOfRange(response, 12, 16)));
		// POS INIT 应答 CMD ID 是6
		assertTrue(Arrays.equals(new byte[] { 0, 0, 0, 6 },
				Arrays.copyOfRange(response, 16, 20)));
		// POS机已经初始化
		assertTrue(Arrays.equals(new byte[] { 0, 0 },
				Arrays.copyOfRange(response, 20, 22)));
		// challenge随机安全码（初始化POS机时生成），获取随机安全码用于登录
		byte[] challenge = new byte[8];
		System.arraycopy(response, 22, challenge, 0, 8);

		// login 登录POS机
		byte[] loginContents = new byte[] {
				// SEQ
				0x00, 0x00, 0x00, 0x01,
				// ACK
				0x00, 0x00, 0x00, 0x00,
				// FLAGS
				0x00, 0x00,
				// CHECKSUM
				0x00, 0x00,
				// MESSAGESIZE
				0x00, 0x00, 0x00, 0x30,
				// CMD ID 登录POS的CMD ID 是7
				0x00, 0x00, 0x00, 0x07,
				// posId
				'p', 'o', 's', '-', '0', '0', '0', '2', '\0', '\0', '\0', '\0',
				// challengeResponse 由随机安全码challenge与POS的secret两者加密得到
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		String pos_secret = "000001";
		// 将challenge于pos_secret两者加密得到challengeResponse
		byte[] content2 = HMAC_MD5.getSecretContent(challenge, pos_secret);
		// 将加密后的结果复制过去
		System.arraycopy(content2, 0, loginContents, 32, content2.length);
		// 计算校验和
		checksum = Tools.checkSum(loginContents, loginContents.length);
		Tools.putUnsignedShort(loginContents, checksum, 10);
		// 发送登录请求
		os.write(loginContents);
		response = new byte[100];
		// 得到应答
		n = is.read(response);
		// 应答包的SEQ
		assertTrue(Arrays.equals(new byte[] { 0, 0, 0, 1 },
				Arrays.copyOfRange(response, 0, 4)));
		// 应答包的ACK
		assertTrue(Arrays.equals(new byte[] { 0, 0, 0, 0 },
				Arrays.copyOfRange(response, 4, 8)));
		// 应答包的MESSAGE SIZE是30
		assertTrue(Arrays.equals(new byte[] { 0, 0, 0, 30 },
				Arrays.copyOfRange(response, 12, 16)));
		// POS LOGIN应答 CMD ID 是8
		assertTrue(Arrays.equals(new byte[] { 0, 0, 0, 8 },
				Arrays.copyOfRange(response, 16, 20)));
		// 登陆成功
		assertTrue(Arrays.equals(new byte[] { 0, 0 },
				Arrays.copyOfRange(response, 20, 22)));
		// 登录成功后将得到一个新的challenge随机安全码，暂时还不知道为什么登录后要新获取一个challenge，断线重连？
		System.arraycopy(response, 22, challenge, 0, challenge.length);

		// 时间请求包
		byte[] timeRequest = new byte[] {
				// SEQ 0
				0x00, 0x00, 0x00, 0x10,
				// ACK 4
				0x00, 0x00, 0x00, 0x00,
				// FLAGS 8
				0x00, 0x00,
				// CHECKSUM 10
				0x00, 0x00,
				// MESSAGESIZE 12
				0x00, 0x00, 0x00, 0x14,
				// CMD ID 16 请求的CMD ID是110
				0x00, 0x00, 0x00, 0x6E };
		// 计算校验和
		checksum = Tools.checkSum(timeRequest, timeRequest.length);
		Tools.putUnsignedShort(timeRequest, checksum, 10);
		// 发送初始化请求
		os.write(timeRequest);
		// 得到应答
		response = new byte[100];
		n = is.read(response);
		System.out.println("n:" + n);
		//读取协议包中的时间
		byte[] b = Arrays.copyOfRange(response, 20, 28);
		System.out.println("The current time is " + (new Date(Tools.bytesToLong(b))).toString());

		for (int i = 0; i < n; i++) {
			String str = Integer.toHexString((byte) response[i]);
			if (str.length() < 2) {
				str = "0" + str;
			}
			if (str.length() > 2) {
				str = str.substring(str.length() - 2);
			}
			System.out.print(str);
			if ((i + 1) % 4 == 0) {
				System.out.println();
			}
		}
	}

}
