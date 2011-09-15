package com.chinarewards.qqgbvpn.main.logic.firmware.impl;

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.status.PosOperationStatus;
import com.chinarewards.qqgbvpn.main.dao.qqapi.PosDao;
import com.chinarewards.qqgbvpn.main.logic.firmware.FirmwareManager;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpDoneRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpDoneResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpgradeRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpgradeRequestResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.firmware.FirmwareUpDoneResult;
import com.chinarewards.qqgbvpn.main.protocol.cmd.init.InitResult;
import com.chinarewards.utils.StringUtil;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Require configuration: firmware.location pointing the directory for the
 * firmware files.
 * 
 * @author kmtong
 * 
 */
public class FirmwareManagerImpl implements FirmwareManager {
	
	Logger logger = LoggerFactory.getLogger(getClass());

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

				String path = config.getString("firmware.location");

				File firmwareFile = new File(path, filename);
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

	@Override
	public FirmwareUpDoneResponseMessage upDoneRequest(
			FirmwareUpDoneRequestMessage req) {
		logger.debug("upDoneRequest() invoke");

		FirmwareUpDoneResponseMessage resp = new FirmwareUpDoneResponseMessage();

		if (StringUtil.isEmptyString(req.getPosId())) {
			throw new IllegalArgumentException("POS ID is missing!");
		}
		if (CmdConstant.FIRMWARE_UP_DONE_CMD_ID != req.getCmdId()) {
			throw new IllegalArgumentException("cmdId error!    cmdId != "+
					CmdConstant.FIRMWARE_UP_DONE_CMD_ID+",cmdId = "+req.getCmdId()+"");
		}
		
		Pos pos = null;
		FirmwareUpDoneResult result = null;
		try {
			pos = posDao.get().fetchPos(req.getPosId(), null, null,
					PosOperationStatus.ALLOWED);
			pos.setUpgradeRequired(false);
			
			posDao.get().merge(pos);
			
			result = FirmwareUpDoneResult.SUCCESS;
		}catch(Throwable e){
			logger.error(e.getMessage(), e);
			result = FirmwareUpDoneResult.PROCESS_ERROR;
		}
		resp.setResult(result.getPosCode());
		
		return resp;
	}

}
