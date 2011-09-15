package com.chinarewards.qqgbvpn.domain.status;

/**
 * Defines the list of POS firmware upgrade status.
 * 
 * @author Cyril
 * @since 0.1.0 2011-09-14
 */
public enum PosFirmwareUpgradeStatus {

	/**
	 * No firmware upgrade is allowed.
	 */
	NOT_ALLOWED,

	/**
	 * State that firmware upgrade should be carried upon POS initialization. It
	 * is expected that once this flag is set, the firmware source should be
	 * available.
	 */
	UPGRADE_ON_INIT;

}
