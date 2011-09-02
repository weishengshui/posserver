/**
 * 
 */
package com.chinarewards.qqgbvpn.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbpvn.main.test.BaseTest;
import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.status.PosDeliveryStatus;
import com.chinarewards.qqgbvpn.domain.status.PosInitializationStatus;
import com.chinarewards.qqgbvpn.domain.status.PosOperationStatus;
import com.chinarewards.qqgbvpn.main.util.HMAC_MD5;
import com.google.inject.Provider;

/**
 * @author cream
 * 
 */
public class BootStrapTest extends BaseTest {

	Logger logger = LoggerFactory.getLogger(BootStrapTest.class);

	/**
	 * This is an empty test. It use to let maven test work.
	 */
	@Test
	public void testEmpty() {
	}

	/**
	 * Test MINA Filter. Open it when you need to test. <br/>
	 * <strong>Please DO NOT commit it. Not a good idea to run Multithreading
	 * test at hudson build.<strong>
	 * 
	 * @throws Exception
	 */
	// @Test
	public void testInitLogin() throws Exception {

		BootThread t = new BootThread();

		t.run();

		testMethod("POS-00000000");

		t.interrupt();
	}

	/**
	 * Open it when you need to test. <br/>
	 * <strong>Please DO NOT commit it. Not a good idea to run Multithreading
	 * test at hudson build.<strong>
	 * 
	 * @throws Exception
	 */
	 @Test
	public void testBatchInitLogin() throws Exception {

		int threadNum = 30;
		int loopsToRun = 50;

		// BootThread t = new BootThread(threadNum);
		//
		// t.run();

		for (int j = 0; j < loopsToRun; j++) {
			for (int i = 0; i < threadNum; i++) {
				final String posId = "POS-" + String.format("%08d", i);

				logger.debug("====creating thread {}, PosID:{}", new Object[] {
						i, posId });
				testMethod(posId);
			}
		}

		// t.interrupt();
	}

	public void testInsert() {
		URL url = BootThread.class.getResource("/");
		String filePath = url.getPath();
		String[] args = new String[] { "-d", filePath };
		BootStrap boot = new BootStrap(args);
		try {
			boot.run();

			PosServer server = boot.getInjector().getInstance(PosServer.class);
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Provider<EntityManager> em = boot.getInjector().getProvider(
				EntityManager.class);

		for (int i = 0; i < 1000; i++) {
			// prepared data
			em.get().getTransaction().begin();

			String posId = "POS-" + String.format("%08d", i);

			System.out.println("*************************PosId:" + posId);

			@SuppressWarnings("unchecked")
			List<Pos> result = em.get()
					.createQuery("FROM Pos WHERE posId=:posId")
					.setParameter("posId", posId).getResultList();
			if (result == null || result.isEmpty()) {
				Pos pos = new Pos();
				pos.setPosId(posId);
				pos.setDstatus(PosDeliveryStatus.DELIVERED);
				pos.setSecret("012345");
				pos.setIstatus(PosInitializationStatus.INITED);
				pos.setOstatus(PosOperationStatus.ALLOWED);
				em.get().persist(pos);
				em.get().flush();
			}
			em.get().getTransaction().commit();
		}
	}

	private void testMethod(String posId) {

		try {
			byte[] challenge = new byte[8];
			{
				Socket socket = new Socket("localhost", 1235);

				OutputStream os = socket.getOutputStream();

				byte[] initMsg = new byte[] {
						// SEQ
						0, 0, 0, 24,
						// ACK
						0, 0, 0, 0,
						// flags
						0, 0,
						// checksum
						0, 0,
						// message length
						0, 0, 0, 32,
						// command ID
						0, 0, 0, 5,
						// POS ID
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
				char[] posIdChar = posId.toCharArray();
				byte[] posIdByte = new byte[posIdChar.length];
				for (int i = 0; i < posIdChar.length; i++) {
					posIdByte[i] = (byte) posIdChar[i];
				}
				System.arraycopy(posIdByte, 0, initMsg, 20, 12);
				int checkSum = Tools.checkSum(initMsg, initMsg.length);
				Tools.putUnsignedShort(initMsg, checkSum, 10);
				logger.debug("Init Packet size: {}", initMsg.length);
				os.write(initMsg);
				os.flush();

				InputStream in = socket.getInputStream();
				byte[] returnValue = new byte[100];
				int length = in.read(returnValue);

				assertEquals(30, length);

				byte[] comId = new byte[4];
				System.arraycopy(returnValue, 16, comId, 0, 4);

				assertEquals(Arrays.toString(new byte[] { 0, 0, 0, 6 }),
						Arrays.toString(comId));

				byte[] result = new byte[2];
				System.arraycopy(returnValue, 20, result, 0, 2);
				assertEquals(Arrays.toString(new byte[] { 0, 0 }),
						Arrays.toString(result));

				System.arraycopy(returnValue, 22, challenge, 0, 8);
				logger.debug("init challenge:{}", Arrays.toString(challenge));

				os.close();
				socket.close();
			}
			// Thread.sleep(5);
			{
				Socket socket = new Socket("localhost", 1235);

				OutputStream os = socket.getOutputStream();

				byte[] loginMsg = new byte[] {
						// SEQ
						0, 0, 0, 24,
						// ACK
						0, 0, 0, 0,
						// flags
						0, 0,
						// checksum
						0, 0,
						// message length
						0, 0, 0, 48,
						// command ID
						0, 0, 0, 7,
						// POS ID
						'P', 'O', 'S', '-', '5', '6', '7', '8', '9', '0', '1',
						'2', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
				char[] posIdChar = posId.toCharArray();
				byte[] posIdByte = new byte[posIdChar.length];
				for (int i = 0; i < posIdChar.length; i++) {
					posIdByte[i] = (byte) posIdChar[i];
				}
				System.arraycopy(posIdByte, 0, loginMsg, 20, 12);
				logger.debug("login Packet size: {}", loginMsg.length);

				byte[] challengeResponse = HMAC_MD5.getSecretContent(challenge,
						"012345");
				logger.debug("login challenge:{}", challenge);
				logger.debug("challenge resp:{}", challengeResponse);
				System.arraycopy(challengeResponse, 0, loginMsg, 32, 16);

				int checkSum = Tools.checkSum(loginMsg, loginMsg.length);
				Tools.putUnsignedShort(loginMsg, checkSum, 10);

				os.write(loginMsg);
				os.flush();

				InputStream in = socket.getInputStream();
				byte[] returnValue = new byte[30];
				in.read(returnValue);
				logger.debug("loginResponse:{}", Arrays.toString(returnValue));

				// test return value.
				assertEquals(30, returnValue.length);

				byte[] comId = new byte[4];
				System.arraycopy(returnValue, 16, comId, 0, 4);

				assertEquals(Arrays.toString(new byte[] { 0, 0, 0, 8 }),
						Arrays.toString(comId));

				byte[] result = new byte[2];
				System.arraycopy(returnValue, 20, result, 0, 2);
				assertEquals("POS ID " + posId + "'s result not correct",
						Arrays.toString(new byte[] { 0, 0 }),
						Arrays.toString(result));

				os.close();
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("should not reach here.");
		}

	}
}

class BootThread extends Thread {

	BootStrap boot;

	int num;

	BootThread() {
		this(1);
	}

	BootThread(int num) {
		this.num = num;
	}

	@Override
	public void run() {
		URL url = BootThread.class.getResource("/");
		String filePath = url.getPath();
		String[] args = new String[] { "-d", filePath };
		boot = new BootStrap(args);
		try {
			boot.run();

			PosServer server = boot.getInjector().getInstance(PosServer.class);
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Provider<EntityManager> em = boot.getInjector().getProvider(
				EntityManager.class);

		for (int i = 0; i < num; i++) {
			// prepared data
			em.get().getTransaction().begin();

			String posId = "POS-" + String.format("%08d", i);

			System.out.println("*************************PosId:" + posId);

			@SuppressWarnings("unchecked")
			List<Pos> result = em.get()
					.createQuery("FROM Pos WHERE posId=:posId")
					.setParameter("posId", posId).getResultList();
			if (result == null || result.isEmpty()) {
				Pos pos = new Pos();
				pos.setPosId(posId);
				pos.setDstatus(PosDeliveryStatus.DELIVERED);
				pos.setSecret("012345");
				pos.setIstatus(PosInitializationStatus.INITED);
				pos.setOstatus(PosOperationStatus.ALLOWED);
				em.get().persist(pos);
				em.get().flush();
			}
			em.get().getTransaction().commit();
		}
	}
}