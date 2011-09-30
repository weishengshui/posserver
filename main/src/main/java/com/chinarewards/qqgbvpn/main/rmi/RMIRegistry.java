package com.chinarewards.qqgbvpn.main.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.PosServer;

/**
 * 启动rmi服务
 * @author dengrenwen
 *
 */
public class RMIRegistry {

	static Logger log = LoggerFactory.getLogger(RMIRegistry.class);
	
	public static int RegistryRMI(int port) {
		log.debug("start listener rmi server...,port=" + port);
		int p = port;
		// Object obj = null;
		try {
			Registry registry = LocateRegistry.getRegistry(p);
			registry.list();
		} catch (RemoteException remoteexception) {
			log.debug("listener rmi server error...");
			Registry registry = null;
			do {
				if (registry != null)
					break;
				try {
					registry = LocateRegistry.createRegistry(p);
					registry.list();
					log.debug("server rmi create success...");
				} catch (RemoteException remoteexception1) {
					remoteexception1.printStackTrace();
					// throw remoteexception1;
				}
			} while (true);
		}
		log.debug("end listener rmi server...");
		return p;
	}
}
