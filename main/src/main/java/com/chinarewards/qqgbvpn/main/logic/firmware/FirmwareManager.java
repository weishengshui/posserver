package com.chinarewards.qqgbvpn.main.logic.firmware;

import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpDoneRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpDoneResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpgradeRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpgradeRequestResponseMessage;

public interface FirmwareManager {

	/**
	 * <ul>
	 * <li>Check POS ID first</li>
	 * <li>Check POS firmware upgrade status.</li>
	 * </ul>
	 */
	public FirmwareUpgradeRequestResponseMessage upgradeRequest(
			FirmwareUpgradeRequestMessage req);
	
	
	/**
	 * <ul>
	 * <li>Check POS ID first</li>
	 * <li>modify POS upgradeRequired.</li>
	 * </ul>
	 */
	public FirmwareUpDoneResponseMessage upDoneRequest(
			 FirmwareUpDoneRequestMessage req);
	
}
