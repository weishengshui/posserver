package com.chinarewards.qqgbvpn.main.protocol.filter;

import java.util.Hashtable;
import java.util.Iterator;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.mxbeans.MonitorCommandManageMXBean;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.Message;

/**
 * 用来监控连接的状态
 * 
 * @author harry
 * 
 */
@SuppressWarnings("rawtypes")
public class MonitorCommandManageFilter extends IoFilterAdapter implements	MonitorCommandManageMXBean {

	Logger log = LoggerFactory.getLogger(getClass());

	// 存储所有指令的使用情况
	private Hashtable<Long, Long> commandCollector = new Hashtable<Long, Long>();
	

	/**
	 * 接收消息时触发事件 在这个事件里面我们改变一个会话的状态为活跃的
	 */
	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception {
		// 得到指令信息
		ICommand msg = ((Message) message).getBodyMessage();
		log.debug(" msg = {}", msg);
		if (commandCollector != null) {
			// 如果collector里面还没有这个指令的记录就添加
			log.debug(" msg.getCmdId() = {}", msg.getCmdId());
			if (commandCollector.get(msg.getCmdId()) == null) {
				commandCollector.put(msg.getCmdId(), 1L);
			} else {
				long count = commandCollector.get(msg.getCmdId());
				commandCollector.put(msg.getCmdId(), ++count);
			}
		}
		nextFilter.messageReceived(session, message);
	}

	/**
	 * 清空所有统计数据
	 */
	@Override
	public void clearAllStatistic() {
		
		//清空指令管理池
		if (commandCollector != null && !commandCollector.isEmpty()) {
			commandCollector.clear();
		}
	}



	/**
	 * 得到指令的信息
	 */
	@Override
	public String getAllCommandReceiveMessage() {
		String mes = "";
		if (commandCollector != null) {
			mes += "receive_command_type_count (" + commandCollector.size()	+ ") : ";
			for (Iterator ite = commandCollector.keySet().iterator(); ite.hasNext();) {
				long command = Long.valueOf(ite.next().toString());
				long count = commandCollector.get(command);
				log.debug("command ={},count={}===={}", new Object[] { command,	count });
				mes += command + " (" + count + ") | ";
			}

		}
		return mes;
	}
}
