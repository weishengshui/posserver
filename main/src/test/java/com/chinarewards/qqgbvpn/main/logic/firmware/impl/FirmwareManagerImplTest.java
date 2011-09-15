package com.chinarewards.qqgbvpn.main.logic.firmware.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.commons.configuration.Configuration;
import org.apache.mina.core.buffer.IoBuffer;
import org.junit.Test;

import com.chinarewards.qqgbpvn.main.CommonTestConfigModule;
import com.chinarewards.qqgbpvn.main.test.JpaGuiceTest;
import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.core.jpa.JpaPersistModuleBuilder;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.status.PosDeliveryStatus;
import com.chinarewards.qqgbvpn.domain.status.PosInitializationStatus;
import com.chinarewards.qqgbvpn.domain.status.PosOperationStatus;
import com.chinarewards.qqgbvpn.main.dao.qqapi.PosDao;
import com.chinarewards.qqgbvpn.main.guice.AppModule;
import com.chinarewards.qqgbvpn.main.logic.firmware.FirmwareManager;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpDoneRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpDoneResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.GetFirmwareFragmentRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.GetFirmwareFragmentResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.firmware.FirmwareUpDoneResult;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.FirmwareUpDoneRequestCodec;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.FirmwareUpDoneResponseCodec;
import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;
import com.google.common.io.ByteStreams;
import com.google.common.io.CountingInputStream;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;


/**
 * test firmwareManager
 */
public class FirmwareManagerImplTest extends JpaGuiceTest {
	
	PosDao posDao;
	
	private FirmwareManager getManager() {
		return getInjector().getInstance(FirmwareManager.class);
	}

	@Override
	protected Module[] getModules() {
		
		CommonTestConfigModule confModule = new CommonTestConfigModule();
		Configuration configuration = confModule.getConfiguration();

		JpaPersistModule jpaModule = new JpaPersistModule("posnet");
		JpaPersistModuleBuilder builder = new JpaPersistModuleBuilder();
		builder.configModule(jpaModule,  configuration, "db");
		
		return new Module[] {
				new AppModule(), jpaModule, confModule };
	}
	
	
	@Test
	public void testAckUpgradeCompleted() throws IOException {
		posDao = getInjector().getInstance(PosDao.class);

		// prepared data
		Pos pos = new Pos();
		pos.setPosId("pos-0001");
		pos.setDstatus(PosDeliveryStatus.DELIVERED);
		pos.setIstatus(PosInitializationStatus.INITED);
		pos.setOstatus(PosOperationStatus.ALLOWED);
		pos.setUpgradeRequired(true);
		getEm().persist(pos);
		getEm().flush();

		FirmwareUpDoneRequestMessage request = new FirmwareUpDoneRequestMessage();
		request.setPosId("pos-0001");
		request.setCmdId(CmdConstant.FIRMWARE_UP_DONE_CMD_ID);
		
		FirmwareUpDoneResponseMessage response = getManager().ackUpgradeCompleted(request);
		
		assertEquals(response.getResult(), FirmwareUpDoneResult.SUCCESS.getPosCode());
		
		Pos record = getEm().find(Pos.class, pos.getId());
		assertNotNull(record);
		assertFalse(record.getUpgradeRequired());
	}
	
	
	
	
	/**
	 * Test the most normal case: POS machine ready, firmware prepared.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetFirmwareFragment_OK() throws Exception {
		
		String firmwarePath = "test/firmware/50k.bin";
		long fragmentOffset = 0;
		long fragmentLength = 1000;
		
		// ----
		
		posDao = getInjector().getInstance(PosDao.class);

		// create the firmware directory and fake firmware.
		File firmwareDir = this.createTmpDir();
		File firmware = this.reserveEmtpyTmpFile(firmwareDir);
		log.debug("Fake firmware will be placed at {}", firmware);
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(firmwarePath);
		this.copyToFile(is, firmware);
		is.close();
		
		// prepared data
		Pos pos = new Pos();
		pos.setPosId("POS-1");
		pos.setDstatus(PosDeliveryStatus.DELIVERED);
		pos.setIstatus(PosInitializationStatus.INITED);
		pos.setOstatus(PosOperationStatus.ALLOWED);	// must be allowed
		pos.setUpgradeRequired(true);	// trigger allowed upgrade
		pos.setFirmware(firmware.getName());	// the fake firmware file
		getEm().persist(pos);
		getEm().flush();
		
		
		// config the firmware directory.
		Configuration conf = getInjector().getInstance(Configuration.class);
		conf.setProperty("firmware.location", firmwareDir.getAbsoluteFile().toString());
		
		
		// now test our API.
		FirmwareManager api = getManager();
		
		// get the first 1000 bytes
		GetFirmwareFragmentRequestMessage req = new GetFirmwareFragmentRequestMessage(
				"POS-1", fragmentOffset, fragmentLength);
		GetFirmwareFragmentResponseMessage resp = api.getFirmwareFragment(req);
		
		//
		// Lots of validation
		//
		assertNotNull(resp);
		assertEquals("Command ID not correct",
				CmdConstant.GET_FIRMWARE_FRAGMENT_CMD_ID_RESPONSE,
				resp.getCmdId());
		assertEquals("Unexpected result code",
				GetFirmwareFragmentResponseMessage.RESULT_OK,
				resp.getResult());
		assertEquals("The returned fragment size is not correct",
				fragmentLength, resp.getContent().length);
		// compare the byte content
		is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(firmwarePath);
		byte[] expectedFragment = new byte[(int)fragmentLength];
		ByteStreams.readFully(is, expectedFragment, (int)fragmentOffset, (int)fragmentLength);
		is.close();
		// make sure they are equals
		assertTrue(
				"The byte content returned by API is not equal to the origin file!",
				Arrays.equals(expectedFragment, resp.getContent()));		

	}
	
	/**
	 * Test the case a read is performed on the last fragment of firmware which
	 * has a size smaller than the requested length.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetFirmwareFragment_ReadLastNotCompleteFirmwareBlock() throws Exception {
		
		String firmwarePath = "test/firmware/50k.bin";	// 50 * 1024
		long fragmentOffset = 50 * 1024 - 3;	// last 3 byte
		long fragmentLength = 1000;
		
		// ----
		
		posDao = getInjector().getInstance(PosDao.class);

		// create the firmware directory and fake firmware.
		File firmwareDir = this.createTmpDir();
		File firmware = this.reserveEmtpyTmpFile(firmwareDir);
		log.debug("Fake firmware will be placed at {}", firmware);
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(firmwarePath);
		this.copyToFile(is, firmware);
		is.close();
		
		// prepared POS data
		Pos pos = new Pos();
		pos.setPosId("POS-1");
		pos.setDstatus(PosDeliveryStatus.DELIVERED);
		pos.setIstatus(PosInitializationStatus.INITED);
		pos.setOstatus(PosOperationStatus.ALLOWED);	// must be allowed
		pos.setUpgradeRequired(true);	// trigger allowed upgrade
		pos.setFirmware(firmware.getName());	// the fake firmware file
		getEm().persist(pos);
		getEm().flush();
		
		
		// config the firmware directory.
		Configuration conf = getInjector().getInstance(Configuration.class);
		conf.setProperty("firmware.location", firmwareDir.getAbsoluteFile().toString());
		
		
		// now test our API.
		FirmwareManager api = getManager();
		
		// get the first 1000 bytes
		GetFirmwareFragmentRequestMessage req = new GetFirmwareFragmentRequestMessage(
				"POS-1", fragmentOffset, fragmentLength);
		GetFirmwareFragmentResponseMessage resp = api.getFirmwareFragment(req);
		
		//
		// Lots of validation
		//
		assertNotNull(resp);
		assertEquals("Command ID not correct",
				CmdConstant.GET_FIRMWARE_FRAGMENT_CMD_ID_RESPONSE,
				resp.getCmdId());
		assertEquals("Unexpected result code",
				GetFirmwareFragmentResponseMessage.RESULT_OK,
				resp.getResult());
		assertEquals("The returned fragment size is not correct",
				3, resp.getContent().length);	// must be 3
		// compare the byte content
		is = new CountingInputStream(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(firmwarePath));
		is.skip(fragmentOffset);
		byte[] expectedFragment = new byte[3];
		ByteStreams.readFully(is, expectedFragment, 0, 3);
		is.close();
		// make sure they are equals
		assertTrue(
				"The byte content returned by API is not equal to the origin file!",
				Arrays.equals(expectedFragment, resp.getContent()));		

	}
	
	@Test
	public void testFirmwareUpDoneRequestCodec() throws Exception{
		//charset
		Charset charset = Charset.forName("gbk");
		
		final long cmdId = CmdConstant.FIRMWARE_UP_DONE_CMD_ID;
		final byte[] posId = new String("pos-0001a123").getBytes(charset);
			
		// prepare buffer
		IoBuffer buf = IoBuffer.allocate(ProtocolLengths.COMMAND
				+ ProtocolLengths.POS_ID);
		
		buf.putUnsignedInt(cmdId);// encode data   // command ID
		buf.put(posId);	// result
		
		buf.position(0);
		ICommandCodec iCommandCodec = new FirmwareUpDoneRequestCodec();
		FirmwareUpDoneRequestMessage message = (FirmwareUpDoneRequestMessage) iCommandCodec.decode(buf, charset);
		
		assertEquals(message.getCmdId(), cmdId);
		assertEquals(message.getPosId(), Tools.byteToString(posId, charset));
	}
	
	@Test
	public void testFirmwareUpDoneResponseCodec(){
		//charset
		Charset charset = Charset.forName("gbk");
		final long cmdId = CmdConstant.FIRMWARE_UP_DONE_CMD_ID_RESPONSE;
		final short result = FirmwareUpDoneResult.SUCCESS.getPosCode();
		
		ICommandCodec iCommandCodec = new FirmwareUpDoneResponseCodec();
		FirmwareUpDoneResponseMessage msg = new FirmwareUpDoneResponseMessage();
		msg.setCmdId(cmdId);
		msg.setResult(result);
		byte[] b = iCommandCodec.encode(msg, charset);
		
		assertEquals(b.length, ProtocolLengths.COMMAND + ProtocolLengths.RESULT);
		
		byte[] codIdBytes = new byte[ProtocolLengths.COMMAND];
		System.arraycopy(b, 0, codIdBytes, 0, ProtocolLengths.COMMAND);
		long tempCmdId = Tools.byteToUnsignedInt(codIdBytes);
		
		byte[] resultBytes = new byte[ProtocolLengths.RESULT];
		System.arraycopy(b, ProtocolLengths.COMMAND, resultBytes, 0, ProtocolLengths.RESULT);
		int tempResult = Tools.byteToUnsignedShort(resultBytes);
		
		assertEquals(cmdId, tempCmdId);
		
		assertEquals(result, (short)tempResult);
	}
	
}
