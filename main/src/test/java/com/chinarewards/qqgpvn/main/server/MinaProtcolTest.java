package com.chinarewards.qqgpvn.main.server;

import java.io.OutputStream;
import java.net.Socket;

import org.junit.Test;

import com.chinarewards.qqgbvpn.config.DatabaseProperties;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.status.PosDeliveryStatus;
import com.chinarewards.qqgbvpn.domain.status.PosInitializationStatus;
import com.chinarewards.qqgbvpn.domain.status.PosOperationStatus;
import com.chinarewards.qqgbvpn.main.dao.qqapi.PosDao;
import com.chinarewards.qqgbvpn.main.guice.AppModule;
import com.chinarewards.qqgpvn.main.test.JpaGuiceTest;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;

public class MinaProtcolTest extends JpaGuiceTest {

	PosDao posDao;

	@Override
	protected Module[] getModules() {
		return new Module[] {
				new AppModule(),
				new JpaPersistModule("posnet")
						.properties(new DatabaseProperties().getProperties()) };
	}
	
	@Test
	public void testSendViaJavaSocket() throws Exception {
		

		// prepared data
		Pos pos = new Pos();
		pos.setPosId("POS-56789012");
		pos.setDstatus(PosDeliveryStatus.DELIVERED);
		pos.setIstatus(PosInitializationStatus.INITED);
		pos.setOstatus(PosOperationStatus.ALLOWED);
		getEm().persist(pos);
		getEm().flush();
		

		Socket socket = new Socket("localhost", 1234);

		OutputStream os = socket.getOutputStream();

		byte[] msg = new byte[] {
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
				'P', 'O', 'S', '-', '5', '6', '7', '8', '9',
				'0', '1', '2' };
		System.out.println("Packet size: " + msg.length);
		os.write(msg);
		os.close();
		socket.close();

	}

}
