/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.impl;

import java.util.HashMap;
import java.util.Map;

import com.chinarewards.qqgbvpn.main.protocol.ServiceHandler;
import com.chinarewards.qqgbvpn.main.protocol.ServiceMapping;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class SimpleServiceMapping implements ServiceMapping {

	Map<Long, Class<? extends ServiceHandler>> mapping;

	public SimpleServiceMapping() {
		mapping = new HashMap<Long, Class<? extends ServiceHandler>>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chinarewards.qqgbvpn.main.protocol.ServiceMapping#addMapping(long,
	 * java.lang.Class)
	 */
	@Override
	public void addMapping(long commandId, Class<? extends ServiceHandler> clazz) {
		mapping.put(commandId, clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chinarewards.qqgbvpn.main.protocol.ServiceMapping#getMapping(long)
	 */
	@Override
	public Object getMapping(long commandId) {
		return mapping.get(commandId);
	}

}
