/**
 * 
 */
package com.chinarewards.qqgbvpn.main;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.chinarewards.qqgbpvn.main.test.BaseTest;
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

	/**
	 * Test MINA Filter.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testInitLogin() throws Exception {

		BootThread t = new BootThread();

		t.run();

		byte[] challenge = new byte[8];
		{
			Socket socket = new Socket("localhost", 1234);

			OutputStream os = socket.getOutputStream();

			byte[] initMsg = new byte[] {
					// SEQ
					0, 0, 0, 24,
					// ACK
					0, 0, 0, 0,
					// flags
					0, 0,
					// checksum
					0, 2,
					// message length
					0, 0, 0, 32,
					// command ID
					0, 0, 0, 5,
					// POS ID
					'P', 'O', 'S', '-', '5', '6', '7', '8', '9', '0', '1', '2' };
			System.out.println("Packet size: " + initMsg.length);
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
			System.out.println("challenge:" + Arrays.toString(challenge));

			os.close();
			socket.close();
		}

		{
			Socket socket = new Socket("localhost", 1234);

			OutputStream os = socket.getOutputStream();

			byte[] loginMsg = new byte[] {
					// SEQ
					0, 0, 0, 24,
					// ACK
					0, 0, 0, 0,
					// flags
					0, 0,
					// checksum
					0, 2,
					// message length
					0, 0, 0, 48,
					// command ID
					0, 0, 0, 7,
					// POS ID
					'P', 'O', 'S', '-', '5', '6', '7', '8', '9', '0', '1', '2',
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			System.out.println("Packet size: " + loginMsg.length);

			byte[] challengeResponse = HMAC_MD5.getSecretContent(challenge,
					"012345");
			System.arraycopy(challengeResponse, 0, loginMsg, 32, 16);

			os.write(loginMsg);
			os.flush();

			InputStream in = socket.getInputStream();
			byte[] returnValue = new byte[30];
			in.read(returnValue);
			System.out.println("loginResponse:" + Arrays.toString(returnValue));

			assertEquals(30, returnValue.length);

			byte[] comId = new byte[4];
			System.arraycopy(returnValue, 16, comId, 0, 4);

			assertEquals(Arrays.toString(new byte[] { 0, 0, 0, 8 }),
					Arrays.toString(comId));

			byte[] result = new byte[2];
			System.arraycopy(returnValue, 20, result, 0, 2);
			assertEquals(Arrays.toString(new byte[] { 0, 0 }),
					Arrays.toString(result));

			os.close();
			socket.close();
		}

	}
}

class BootThread extends Thread {

	BootStrap boot;

	@Override
	public void run() {
		boot = new BootStrap((String[]) null);
		try {
			boot.run();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Provider<EntityManager> em = boot.getInjector().getProvider(
				EntityManager.class);

		// prepared data
		em.get().getTransaction().begin();
		Pos pos = new Pos();
		pos.setPosId("POS-56789012");
		pos.setDstatus(PosDeliveryStatus.DELIVERED);
		pos.setSecret("012345");
		pos.setIstatus(PosInitializationStatus.INITED);
		pos.setOstatus(PosOperationStatus.ALLOWED);
		em.get().persist(pos);
		em.get().flush();
		em.get().getTransaction().commit();
	}
}