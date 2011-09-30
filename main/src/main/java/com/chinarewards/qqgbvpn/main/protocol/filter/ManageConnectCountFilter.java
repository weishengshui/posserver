package com.chinarewards.qqgbvpn.main.protocol.filter;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.MXBeans.ManageConnectMXBean;

/**
 * 用来监控连接的状态
 * @author dengrenwen
 *
 */
@SuppressWarnings("rawtypes")
public class ManageConnectCountFilter extends IoFilterAdapter implements ManageConnectMXBean {

	Logger log = LoggerFactory.getLogger(getClass());
	
	//存储记录所有的连接及状态
	private Hashtable<String , String> sessionCollector = new Hashtable<String , String>();
	private String IS_IDLE= "idle";
	private String IS_ACTIVE= "active";
	
	@Override
	public long getOpenConnectCount() {
		
		return sessionCollector.keySet().size();
	}

	@Override
	public long getActivityConnectCount() {
		long count=0;
		for(Iterator iterator=sessionCollector.keySet().iterator();iterator.hasNext();){
			String sessionId=iterator.next().toString();
			if(sessionCollector.get(sessionId).equals(IS_ACTIVE)){
				count++;
			}
		}
		return count;
	}

	@Override
	public long getIdleConnectCount() {
		long count=0;
		for(Iterator iterator=sessionCollector.keySet().iterator();iterator.hasNext();){
			String sessionId=iterator.next().toString();
			if(sessionCollector.get(sessionId).equals(IS_IDLE)){
				count++;
			}
		}
		return count;
	}

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception {
		updateIoSessionConnectState(session,IS_ACTIVE);
		nextFilter.messageReceived(session, message);
	}

	@Override
	public void messageSent(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception {
		updateIoSessionConnectState(session,IS_ACTIVE);
		
		nextFilter.messageSent(session, writeRequest);
	}

	@Override
	public void sessionClosed(NextFilter nextFilter, IoSession session)
			throws Exception {
		for(Iterator iterator=sessionCollector.keySet().iterator();iterator.hasNext();){
			String sessionId=iterator.next().toString();
			if(sessionId.equals(String.valueOf(session.getId()))){
				sessionCollector.remove(sessionId);
				break;
			}
		}
		nextFilter.sessionClosed(session);
	}

	@Override
	public void sessionIdle(NextFilter nextFilter, IoSession session,
			IdleStatus status) throws Exception {
		updateIoSessionConnectState(session,IS_IDLE);
		nextFilter.sessionIdle(session, status);
	}

	@Override
	public void sessionOpened(NextFilter nextFilter, IoSession session)
			throws Exception {
		String sessionId=String.valueOf(session.getId());
		sessionCollector.put(sessionId, IS_ACTIVE);
		nextFilter.sessionOpened(session);
	}
	
	
	public void updateIoSessionConnectState(IoSession session,String state){
		for(Iterator iterator=sessionCollector.keySet().iterator();iterator.hasNext();){
			String sessionId=iterator.next().toString();
			if(sessionId.equals(String.valueOf(session.getId()))){
				sessionCollector.remove(sessionId);
				sessionCollector.put(sessionId, state);
				break;
			}
		}
	}
	
	

}
