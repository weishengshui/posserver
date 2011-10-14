/**
 * 
 */
package com.chinarewards.qqgbvpn.main.util;

import java.util.UUID;

/**
 * 
 * 
 * @author Cyril
 * @since 1.0.0
 */
public class Util {

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// mute this exception
			System.err.println("Util.sleep() interrupted");
			e.printStackTrace();
		}
	}

	public static void sleepWithException(long millis)
			throws InterruptedException {
		Thread.sleep(millis);
	}
	
	public static String getUUID(boolean needBar) {
		String uuid = UUID.randomUUID().toString();
		if (!needBar) {
			uuid = uuid.replaceAll("-", "");
		}
		return uuid;
	}

}
