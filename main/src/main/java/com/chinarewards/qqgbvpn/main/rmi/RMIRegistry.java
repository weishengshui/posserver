package com.chinarewards.qqgbvpn.main.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 启动rmi服务
 * 
 * @author dengrenwen
 * 
 */
public class RMIRegistry {

	static Logger log = LoggerFactory.getLogger(RMIRegistry.class);

	public static int createRegistry(int port) {
		log.info("Start listener rmi server, port={}", port);
		int p = port;
		// Object obj = null;
		try {
			Registry registry = LocateRegistry.getRegistry(p);
			registry.list();
			log.info("using existing RMI registry found on port {}", port);
		} catch (RemoteException remoteexception) {
			log.info("no existing RMI registry found on port {}, creating...",
					port);
			Registry registry = null;
			do {
				if (registry != null)
					break;
				try {
					registry = LocateRegistry.createRegistry(p);
					registry.list();
					log.info("RMI server create success.");
				} catch (RemoteException remoteexception1) {
					log.error("Error creating RMI registry server",
							remoteexception1);
				}
			} while (true);
		}
		return p;
	}
}
