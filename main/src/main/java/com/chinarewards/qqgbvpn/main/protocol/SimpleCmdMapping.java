/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol;

import java.util.HashMap;
import java.util.Map;

import com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec.ICommandCodec;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public class SimpleCmdMapping implements CmdMapping {

	Map<Long, Object> mapping = new HashMap<Long, Object>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbvpn.main.protocol.CmdMapping#addMapping(long,
	 * java.lang.Class)
	 */
	@Override
	public void addMapping(long commandId,
			Class<? extends ICommandCodec> clazz) {
		mapping.put(commandId, clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbvpn.main.protocol.CmdMapping#getMapping(long)
	 */
	@Override
	public Object getMapping(long commandId) {
		return mapping.get(commandId);
	}

}
