package com.chinarewards.qqgbvpn.main.logic.firmware;

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

}
