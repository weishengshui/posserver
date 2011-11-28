package com.chinarewards.qqgbvpn.main.protocol.filter;

import java.util.Hashtable;
import java.util.Iterator;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.chinarewards.qqgbvpn.main.management.PosConnectionMXBean;
import com.chinarewards.qqgbvpn.main.util.IoSessionMessageManage;

/**
 * 用来监控连接的状态
 * 
 * @author harry
 * 
 */
@SuppressWarnings("rawtypes")
public class MonitorConnectManageFilter extends IoFilterAdapter implements
		PosConnectionMXBean {

	Logger log = LoggerFactory.getLogger(getClass());

	// 存储所有的连接及状态
	private Hashtable<Long, IoSessionMessageManage> sessionCollector = new Hashtable<Long, IoSessionMessageManage>();
	//用来标识连接的状态，闲置和活动
	private String IS_IDLE = "idle";
	private String IS_ACTIVE = "active";

	//当前打开的连接
	private long openConnectCount;
	//当前活动的连接
	private long activityConnectCount;
	//当前闲置的连接
	private long idleConnectCount;

	//总共接收了多少字节
	private long receiveBytesCount;
	//总共发送了多少字节
	private long sendBytesCount;
	
	//记录已经关闭连接的接收字节数
	private long closedReceiveBytes;
	//记录已经关闭连接的发送字节数
	private long closedSendBytes;

	/**
	 * 得到打开的连接数
	 */
	@Override
	public long getConnectionCount() {
		openConnectCount = sessionCollector.keySet().size();
		return openConnectCount;
	}

	/**
	 * 得到活跃的连接数
	 */
	@Override
	public long getActive() {
		activityConnectCount = getConnectCountByState(IS_ACTIVE);
		return activityConnectCount;
	}

	/**
	 * 得到闲置的连接数
	 */
	@Override
	public long getIdle() {
		idleConnectCount = getConnectCountByState(IS_IDLE);
		return idleConnectCount;
	}

	/**
	 * 接收消息时触发事件 在这个事件里面我们改变一个会话的状态为活跃的
	 */
	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception {
		
		updateIoSessionConnectMessage(session, IS_ACTIVE);
//		log.debug(" id = {} ,session.getReadBytes()={}",new Object[]{session.getId(),session.getReadBytes()});
		
		nextFilter.messageReceived(session, message);
	}

	/**
	 * 这个是发送消息时触发的事件 在这个事件里面我们改变一个会话的状态为活跃的
	 */
	@Override
	public void messageSent(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception {
//		log.debug(" id = {} ,session.getWrittenBytes()={}",new Object[]{session.getId(),session.getWrittenBytes()});
		updateIoSessionConnectMessage(session, IS_ACTIVE);
		nextFilter.messageSent(session, writeRequest);
	}

	/**
	 * 这个是关闭一个会话时触发的事件 我们在这个事件里移除一个连接数
	 */
	@Override
	public void sessionClosed(NextFilter nextFilter, IoSession session)
			throws Exception {
		//记录即将要关闭的连接，接收和发送的字节数
		this.closedReceiveBytes += sessionCollector.get(session.getId()).getReceiveBytes();
		this.closedSendBytes += sessionCollector.get(session.getId()).getSendBytes();
		//移除。
		sessionCollector.remove(session.getId());

		nextFilter.sessionClosed(session);
	}

	/**
	 * 这个是根据设置的闲置时间来确定连接是否闲着，闲置时就会触发这个事件 这个事件我们用了改变连接的状态为闲置
	 */
	@Override
	public void sessionIdle(NextFilter nextFilter, IoSession session,
			IdleStatus status) throws Exception {
		//改变当前连接的状态为闲置
		updateIoSessionConnectMessage(session, IS_IDLE);
		
		nextFilter.sessionIdle(session, status);
	}

	/**
	 * 打开一个连接时我们就保存一个连接的session id 及状态。初始都是活跃状态
	 */
	@Override
	public void sessionOpened(NextFilter nextFilter, IoSession session)
			throws Exception {		
		
		addElementToSessionCollector(session,IS_ACTIVE);
		
		nextFilter.sessionOpened(session);
	}
	/**
	 * 当sessionCollector里面没有元素时，用来添加元素。
	 * @param session
	 */
	private void addElementToSessionCollector(IoSession session , String state){
		IoSessionMessageManage sessionMess = new IoSessionMessageManage();
		//在当前这个连接第一次出发闲置的事件时，记录闲置的开始时间
		if (state.equals(IS_IDLE)) {
			// 得到当前系统时间戳（毫秒计算）
			sessionMess.setIdleTime(System.currentTimeMillis());
		}
		sessionMess.setState(state);
		sessionMess.setSendBytes(session.getWrittenBytes());
		sessionMess.setReceiveBytes(session.getReadBytes());
		sessionMess.setSession(session);

		sessionCollector.put(session.getId(), sessionMess);
	}

	/**
	 * 用来改变连接的状态
	 * 
	 * @param session
	 *            : IoSession
	 * @param state
	 *            : active or idle
	 */
	private void updateIoSessionConnectMessage(IoSession session, String state) {
		
//		log.trace("session.getId = {}, state={}",
//				new Object[] { session.getId(), state });
		
		IoSessionMessageManage sessionMess = sessionCollector.get(session.getId());
		
		if (sessionMess != null) {
			//在当前这个连接第一次出发闲置的事件时，记录闲置的开始时间
			if (sessionMess.getState().equals(IS_ACTIVE) && state.equals(IS_IDLE)) {
				// 得到当前系统时间戳（毫秒计算）
//				log.debug("()={}", System.currentTimeMillis());
				sessionMess.setIdleTime(System.currentTimeMillis());
			}
			sessionMess.setState(state);
			sessionMess.setSendBytes(session.getWrittenBytes());
			sessionMess.setReceiveBytes(session.getReadBytes());
//			log.debug("session_id={}, state={}", new Object[] {
//					session.getId(), state });
			sessionMess.setSession(session);
		} else {
			//如果因为情况所有的统计数据而去掉了连接管理，但是这个连接并没有关闭，当这个连接发送或者接收数据时，做添加连接管理处理
			addElementToSessionCollector(session , state);
		}
	}

	/**
	 * 根据状态得到不同状态下的连接数
	 * 
	 * @param state
	 *            ： 状态
	 * @return
	 */
	private long getConnectCountByState(String state) {
		long count = 0;
		for (Iterator iterator = sessionCollector.keySet().iterator(); iterator.hasNext();) {
			long sessionId = Long.valueOf(iterator.next().toString());
			if (sessionCollector.get(sessionId).getState().equals(state)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 得到所有连接的总接收字节数
	 */
	@Override
	public long getBytesReceived() {
		//初始化为那些已关闭连接所接收过的字节数
		receiveBytesCount = this.closedReceiveBytes;
//		log.debug(" receiveBytesCount={} ",receiveBytesCount);
		//得到正在管理的连接的所接收的字节数
		for (Iterator iterator = sessionCollector.keySet().iterator(); iterator.hasNext();) {
			IoSessionMessageManage sessionMess = sessionCollector.get(iterator.next());
			receiveBytesCount += sessionMess.getReceiveBytes();
		}
//		log.debug(" receiveBytesCount={} ",receiveBytesCount);
		return receiveBytesCount;
	}

	/**
	 * 得到所有连接的总发送字节数
	 */
	@Override
	public long getBytesSent() {
		//初始化为那些已关闭连接所发送过的字节数
		sendBytesCount = this.closedSendBytes;
//		log.debug(" sendBytesCount={} ",sendBytesCount);
		//得到正在管理的连接的所发送的字节数
		for (Iterator iterator = sessionCollector.keySet().iterator(); iterator.hasNext();) {
			IoSessionMessageManage sessionMess = sessionCollector.get(iterator.next());
			sendBytesCount += sessionMess.getSendBytes();
		}
//		log.debug(" sendBytesCount={} ",sendBytesCount);
		return sendBytesCount;
	}

	/**
	 * 关闭所有闲置的连接
	 */
	@Override
	public void closeIdleConnections() {
		if (sessionCollector != null && !sessionCollector.isEmpty()) {
			for (Iterator iterator = sessionCollector.keySet().iterator(); iterator.hasNext();) {
				IoSessionMessageManage sessionMess = sessionCollector.get(iterator.next());
				// 关闭所有闲置的连接
				if (sessionMess.getState().equals(IS_IDLE)) {
					sessionMess.getSession().close(true);
				}
			}
		}
	}

	/**
	 * 清空所有统计数据
	 */
	@Override
	public void resetStatistics() {
		
		this.closedReceiveBytes = 0;
		this.closedSendBytes = 0;
		for (Iterator iterator = sessionCollector.keySet().iterator(); iterator.hasNext();) {
			IoSessionMessageManage sessionMess = sessionCollector.get(iterator.next());
			sessionMess.setSendBytes(0);
			sessionMess.setReceiveBytes(0);
		}

	}

	/**
	 * 
	 * 根据闲置的时间，关闭闲置的连接
	 */
	@Override
	public void closeIdleConnections(long second) {
		//把分钟转换成毫秒数
		long idleMilliSecond = second * 1000;
		//得到应该可以关闭的闲置时间
		long startIdleMilliSecond = System.currentTimeMillis() - idleMilliSecond;
//		log.debug("startIdleMilliSecond={}", startIdleMilliSecond);
		
		if (sessionCollector != null && !sessionCollector.isEmpty()) {
			for (Iterator iterator = sessionCollector.keySet().iterator(); iterator.hasNext();) {
				IoSessionMessageManage sessionMess = sessionCollector.get(iterator.next());
				// 按照闲置的时间关闭所有闲置的连接
				if (sessionMess.getState().equals(IS_IDLE) && sessionMess.getIdleTime() <= startIdleMilliSecond) {
//					log.debug("sessionMess.getIdleTime()={}",sessionMess.getIdleTime());
					sessionMess.getSession().close(true);
				}
			}
		}

	}
}
