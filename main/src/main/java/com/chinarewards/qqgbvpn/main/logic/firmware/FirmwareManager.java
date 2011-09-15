package com.chinarewards.qqgbvpn.main.logic.firmware;

import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpDoneRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpDoneResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpgradeRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.FirmwareUpgradeRequestResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.GetFirmwareFragmentRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.GetFirmwareFragmentResponseMessage;

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
	 * Retrieve the firmware fragment.
	 * 
	 * @param request
	 * @return
	 */
	public GetFirmwareFragmentResponseMessage getFirmwareFragment(
			GetFirmwareFragmentRequestMessage request);

	/**
	 * <ul>
	 * <li>Check POS ID first</li>
	 * <li>modify POS upgradeRequired.</li>
	 * </ul>
	 */
	public FirmwareUpDoneResponseMessage ackUpgradeCompleted(
			FirmwareUpDoneRequestMessage req);

}
