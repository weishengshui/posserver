/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.cmd.init;

/**
 * @author cream
 * 
 */
public class InitRequest {

	/**
	 * Pos 机流水号
	 */
	private long serial;

	/**
	 * Pos 机编号
	 */
	private String posId;

	public long getSerial() {
		return serial;
	}

	public void setSerial(long serial) {
		this.serial = serial;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

}
