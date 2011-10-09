package com.chinarewards.qqgbvpn.main.protocol.filter;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.MXBeans.ManageConnectMXBean;
import com.chinarewards.qqgbvpn.main.util.IoSessionMessageManage;

/**
 * 用来监控连接的状态
 * 
 * @author harry
 * 
 */
@SuppressWarnings("rawtypes")
public class ManageConnectCountFilter extends IoFilterAdapter implements
		ManageConnectMXBean {

	Logger log = LoggerFactory.getLogger(getClass());

	// 存储记录所有的连接及状态
	private Hashtable<Long, IoSessionMessageManage> sessionCollector = new Hashtable<Long, IoSessionMessageManage>();
	private String IS_IDLE = "idle";
	private String IS_ACTIVE = "active";

	private long openConnectCount;
	private long activityConnectCount;
	private long idleConnectCount;

	private long receiveBytesCount;
	private long sendBytesCount;

	/**
	 * 得到打开的连接数
	 */
	@Override
	public long getOpenConnectCount() {
		openConnectCount = sessionCollector.keySet().size();
		return openConnectCount;
	}

	/**
	 * 得到活跃的连接数
	 */
	@Override
	public long getActivityConnectCount() {
		activityConnectCount = getConnectCountByState(IS_ACTIVE);
		return activityConnectCount;
	}

	/**
	 * 得到闲置的连接数
	 */
	@Override
	public long getIdleConnectCount() {
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
		nextFilter.messageReceived(session, message);
	}

	/**
	 * 这个是发送消息时触发的事件 在这个事件里面我们改变一个会话的状态为活跃的
	 */
	@Override
	public void messageSent(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception {
		updateIoSessionConnectMessage(session, IS_ACTIVE);
		nextFilter.messageSent(session, writeRequest);
	}

	/**
	 * 这个是关闭一个会话时触发的事件 我们在这个事件里移除一个连接数
	 */
	@Override
	public void sessionClosed(NextFilter nextFilter, IoSession session)
			throws Exception {
		for (Iterator iterator = sessionCollector.keySet().iterator(); iterator.hasNext();) {
			long sessionId = Long.valueOf(iterator.next().toString());
			if (sessionId==session.getId()) {
				sessionCollector.remove(sessionId);
				break;
			}
		}
		nextFilter.sessionClosed(session);
	}

	/**
	 * 这个是根据设置的闲置时间来确定连接是否闲着，闲置时就会触发这个事件 这个事件我们用了改变连接的状态为闲置
	 */
	@Override
	public void sessionIdle(NextFilter nextFilter, IoSession session,
			IdleStatus status) throws Exception {
		updateIoSessionConnectMessage(session, IS_IDLE);
		nextFilter.sessionIdle(session, status);
	}

	/**
	 * 打开一个连接时我们就保存一个连接的session id 及状态。初始都是活跃状态
	 */
	@Override
	public void sessionOpened(NextFilter nextFilter, IoSession session)
			throws Exception {
		IoSessionMessageManage sessionMess = new IoSessionMessageManage();
		sessionMess.setState(IS_ACTIVE);
		sessionMess.setSession(session);
		sessionCollector.put(session.getId(), sessionMess);
		nextFilter.sessionOpened(session);
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
		log.debug("session.getId = {}，state={}",new Object[]{session.getId(),state});
		IoSessionMessageManage sessionMess = sessionCollector.get(session.getId());
		log.debug("sessionMess={}",sessionMess);
		if (sessionMess != null) {
			if (sessionMess.getState().equals(IS_ACTIVE)&&state.equals(IS_IDLE)) {
				// 得到当前系统时间戳（毫秒计算）
				log.debug("()={}",System.currentTimeMillis());
				sessionMess.setIdleTime(System.currentTimeMillis());
			}
			sessionMess.setState(state);
			sessionMess.setSession(session);

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
			long sessionId =Long.valueOf(iterator.next().toString()) ;
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
	public long getReceiveBytesCount() {
		receiveBytesCount = 0;
		for (Iterator iterator = sessionCollector.keySet().iterator(); iterator.hasNext();) {
			IoSessionMessageManage sessionMess = sessionCollector.get(iterator.next());
			receiveBytesCount += sessionMess.getSession().getReadBytes();
		}
		return receiveBytesCount;
	}

	/**
	 * 得到所有连接的总发送字节数
	 */
	@Override
	public long getSendBytesCount() {
		sendBytesCount = 0;
		for (Iterator iterator = sessionCollector.keySet().iterator(); iterator.hasNext();) {
			IoSessionMessageManage sessionMess = sessionCollector.get(iterator.next());
			sendBytesCount += sessionMess.getSession().getWrittenBytes();
		}
		return sendBytesCount;
	}

	/**
	 * 关闭所有闲置的连接
	 */
	@Override
	public void closeAllIdleConnect() {
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
	public void clearAllStatistic() {
		if (sessionCollector != null && !sessionCollector.isEmpty()) {
			sessionCollector.clear();
			this.openConnectCount = 0;
			this.activityConnectCount = 0;
			this.idleConnectCount = 0;
			this.receiveBytesCount = 0;
			this.sendBytesCount = 0;
		}
	}

	/**
	 * 根据闲置的时间，关闭闲置的连接
	 */
	@Override
	public void closeIdleConnect(long minute) {
		long idleMilliSecond = minute * 60 * 1000;
		long startIdleMilliSecond = System.currentTimeMillis() - idleMilliSecond;
		log.debug("startIdleMilliSecond={}",startIdleMilliSecond);
		if (sessionCollector != null && !sessionCollector.isEmpty()) {
			for (Iterator iterator = sessionCollector.keySet().iterator(); iterator.hasNext();) {
				IoSessionMessageManage sessionMess = sessionCollector.get(iterator.next());
				 // 按照闲置的时间关闭所有闲置的连接
				 if(sessionMess.getState().equals(IS_IDLE) && sessionMess.getIdleTime() <= startIdleMilliSecond){
					 log.debug("sessionMess.getIdleTime()={}",sessionMess.getIdleTime());
					 sessionMess.getSession().close(true);
				 }
			}
		}

	}
}
