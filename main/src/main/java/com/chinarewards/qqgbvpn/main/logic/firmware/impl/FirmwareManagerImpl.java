package com.chinarewards.qqgbvpn.main.logic.firmware.impl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import javax.persistence.NoResultException;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.status.PosOperationStatus;
import com.chinarewards.qqgbvpn.main.dao.qqapi.PosDao;
import com.chinarewards.qqgbvpn.main.logic.firmware.FirmwareManager;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpDoneRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpDoneResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpgradeRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpgradeRequestResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.GetFirmwareFragmentRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.GetFirmwareFragmentResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.firmware.FirmwareUpDoneResult;
import com.chinarewards.utils.StringUtil;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Require configuration: firmware.location pointing the directory for the
 * firmware files.
 * 
 * @author kmtong
 * @author cyril
 * @since 0.1.0 2011-09-15
 */
public class FirmwareManagerImpl implements FirmwareManager {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
	Provider<PosDao> posDao;

	@Inject
	Configuration config;

	public FirmwareUpgradeRequestResponseMessage upgradeRequest(
			FirmwareUpgradeRequestMessage req) {
		FirmwareUpgradeRequestResponseMessage resp = new FirmwareUpgradeRequestResponseMessage();
		try {
			Pos pos = posDao.get().fetchPos(req.getPosId(), null, null,
					PosOperationStatus.ALLOWED);
			String filename = pos.getFirmware();
			if (pos.getUpgradeRequired() != null
					&& pos.getUpgradeRequired().booleanValue()) {

				File firmwareFile = this.getFirmwareAbsPath(filename);
				
				if (firmwareFile.isFile() && firmwareFile.canRead()) {
					resp.setFirmwareName(filename);
					resp.setSize(firmwareFile.length());
					resp.setResult(0);
				} else {
					resp.setResult(2);
				}

			} else {
				resp.setResult(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.setResult(3);
		}
		return resp;
	}

	/* (non-Javadoc)
	 * @see com.chinarewards.qqgbvpn.main.logic.firmware.FirmwareManager#getFirmwareFragment(com.chinarewards.qqgbvpn.main.protocol.cmd.GetFirmwareFragmentRequestMessage)
	 */
	@Override
	public GetFirmwareFragmentResponseMessage getFirmwareFragment(
			GetFirmwareFragmentRequestMessage req) {
		
		Pos pos = null;
		// response data to be sent.
		byte[] fragment = null;
		short result = 0;
		
		try {
			// locate the POS machine record.
			try {
				pos = posDao.get().fetchPos(req.getPosId(), null, null,
						PosOperationStatus.ALLOWED);
			} catch (NoResultException e) {
				// no POS is found.
			}
			// POS machine is not found, return error
			if (pos == null) {
				result = GetFirmwareFragmentResponseMessage.RESULT_POS_NOT_FOUND;
				return new GetFirmwareFragmentResponseMessage(result, null);
			}
			
			// make sure firmware upgrade is available.
			if (pos.getUpgradeRequired() != null
					&& pos.getUpgradeRequired().booleanValue()) {
				
				// upgrade is allowed.

				// make sure firmware is available for reading.
				String filename = pos.getFirmware();
				File firmwareFile = getFirmwareAbsPath(filename);
				log.debug("Firmware for POS ID {} is {}", req.getPosId(), firmwareFile);

				// make sure the file does exist.
				if (!firmwareFile.exists()) {
					return new GetFirmwareFragmentResponseMessage(GetFirmwareFragmentResponseMessage.RESULT_FIRMWARE_NOT_FOUND, null);
				}
				
				// make sure it is a file and can be read.
				if (!firmwareFile.isFile()) {
					log.warn("Firmware '{}' for POS ID {} is not a file", firmwareFile, req.getPosId());
					return new GetFirmwareFragmentResponseMessage(GetFirmwareFragmentResponseMessage.RESULT_FIRMWARE_IO_ERROR, null);
				} else if (!firmwareFile.canRead()) {
					log.warn("Firmware '{}' for POS ID {} cannot be read", firmwareFile, req.getPosId());
					return new GetFirmwareFragmentResponseMessage(GetFirmwareFragmentResponseMessage.RESULT_FIRMWARE_IO_ERROR, null);
				}
				
				// Note: After this line, we assume the file is ready to be 
				// read.
				
				// make sure the specified offset does not exceed the filesize,
				// i.e. < firmware file size.
				long firmwareFilesize = firmwareFile.length();
				if (req.getOffset() >= firmwareFilesize) {
					if (log.isDebugEnabled()) {
						log.debug("POS ID {} request a invalid firmware offset {}, firmware '{}' has a size of {} bytes only.", 
								new Object[] { req.getPosId(), req.getOffset(), firmwareFile.getAbsoluteFile(), firmwareFilesize });
					}
					return new GetFirmwareFragmentResponseMessage(GetFirmwareFragmentResponseMessage.RESULT_FIRMWARE_IO_ERROR, null);
				}
				
				// read the whole fragment.
				try {
					
					fragment = this.readFile(firmwareFile, req.getOffset(), req.getLength());
					
				} catch (IOException e) {
					// strange error has occurred when reading firmware.
					if (log.isDebugEnabled()) {
						log.debug("An unexpected error has occurred when reading firmware '{}' for POS ID {}.", 
								new Object[] { firmwareFile.getAbsoluteFile(), req.getPosId() });
					}
					return new GetFirmwareFragmentResponseMessage(GetFirmwareFragmentResponseMessage.RESULT_FIRMWARE_IO_ERROR, null);
				}
				
			} else {
				// POS is not marked to allow firmware upgrade.
				return new GetFirmwareFragmentResponseMessage(GetFirmwareFragmentResponseMessage.RESULT_NO_FIRMWARE_UPGRADE, null);
			}
			
		} catch (Exception e) {
			// any sort of unexpected error.
			log.error(
					"Unexpected error when handling request of retrieving firmware fragment for POS ID "
							+ req.getPosId(), e);
			return new GetFirmwareFragmentResponseMessage(GetFirmwareFragmentResponseMessage.RESULT_FIRMWARE_IO_ERROR, null);
		}

		
		// construct the response message.
		return new GetFirmwareFragmentResponseMessage(
				GetFirmwareFragmentResponseMessage.RESULT_OK, fragment);
	}
	
	/**
	 * 
	 * 
	 * 
	 * @param file
	 * @param offset zero based position.
	 * @param length
	 * @return
	 * @throws IOException 
	 */
	protected byte[] readFile(File file, long offset, long length)
			throws IOException {

		// open for reading only.
		RandomAccessFile f = new RandomAccessFile(file, "r");
		
		// seek to the desired position.
		f.seek(offset);
		
		// read now!
		byte[] buf = new byte[(int)length];	// XXX truncated!?
		long remaining = length;
		int buf_pos = 0;
		int actualReadLength = 0;
		//
		int defaultBlockSize = 4096;	// default block size to read.

		// the read size shall be the same as defaultBlockSize, until
		// the last block is reached or the required read length is exceed.
		int read_size = (defaultBlockSize > length) ? defaultBlockSize : (int)length;
		
		// read until the whole expected length is reached.
		while (actualReadLength < length) {
			
			// don't read outside the specified length from method argument.
			if (length - actualReadLength < read_size) {
				read_size = (int)length - actualReadLength;
			}
			
			// read the stream!
			int actual = f.read(buf, buf_pos, read_size);
			
			// stop condition: end of file reached.
			if (actual == -1) {
				break;
			}
			
			// increment the actual totally read bytes
			actualReadLength += actual;
			
		}
		
		return Arrays.copyOf(buf, actualReadLength);
		
	}
	

	/**
	 * Returns the absolute path of the firmware. The filename can be either
	 * absolute or relative and this method will resolve them to absolute path.
	 * 
	 * @param filename the path of the firmware.
	 * @return
	 */
	protected File getFirmwareAbsPath(String filename) {
		File f = new File(filename);

		// if it is an absolute path, ignore it.
		if (f.isAbsolute()) {
			return f;
		}
		
		// if it is an relative path, append it.
		String path = config.getString("firmware.location");
		File firmwareFile = new File(path, filename);
		return firmwareFile;
	}
	
	@Override
	public FirmwareUpDoneResponseMessage ackUpgradeCompleted(
			FirmwareUpDoneRequestMessage req) {
		log.debug("upDoneRequest() invoke");

		FirmwareUpDoneResponseMessage resp = new FirmwareUpDoneResponseMessage();

		if (StringUtil.isEmptyString(req.getPosId())) {
			throw new IllegalArgumentException("POS ID is missing!");
		}
		if (FirmwareUpDoneRequestMessage.FIRMWARE_UP_DONE_CMD_ID != req.getCmdId()) {
			throw new IllegalArgumentException("cmdId error!    cmdId != "+
					FirmwareUpDoneRequestMessage.FIRMWARE_UP_DONE_CMD_ID+",cmdId = "+req.getCmdId()+"");
		}
		
		Pos pos = null;
		FirmwareUpDoneResult result = null;
		try {
			pos = posDao.get().fetchPos(req.getPosId(), null, null,
					PosOperationStatus.ALLOWED);
			pos.setUpgradeRequired(false);

			posDao.get().merge(pos);

			result = FirmwareUpDoneResult.SUCCESS;
		} catch (Throwable e) {
			log.warn(
					"An error has occurred when acknowleding POS (ID="
							+ req.getPosId() + ") firmware upgrade", e);
			result = FirmwareUpDoneResult.PROCESS_ERROR;
		}
		resp.setResult(result.getPosCode());
		
		return resp;
	}

}
