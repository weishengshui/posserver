/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.filter;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.login.LoginResult;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.IBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.LoginRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.LoginResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.Message;

/**
 * Login filter.
 * 
 * @author cream
 * @since 1.0.0 2011-08-29
 */
public class LoginFilter extends IoFilterAdapter {

	private final static String IS_LOGIN = "is_login";
	private final static String POS_ID = "pos_id";

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception {
		Boolean isLogin = (Boolean) session.getAttribute(IS_LOGIN);
//		String posId = (String) session.getAttribute(POS_ID);

//		if (StringUtil.isEmptyString(posId)) {
//			throw new IllegalArgumentException("Pos Id not existed!");
//		}
		// Check whether the command ID is LOGIN
		IBodyMessage msg = ((Message) message).getBodyMessage();
		long cmdId = msg.getCmdId();
		if (cmdId == CmdConstant.INIT_CMD_ID) {
			// TODO get POS ID
			// LoginRequestMessage lm = (LoginRequestMessage) message;
			// session.setAttribute(POS_ID, lm.getPosid());
		} else if (cmdId == CmdConstant.LOGIN_CMD_ID) {
			// get POS ID
			LoginRequestMessage lm = (LoginRequestMessage) msg;
			session.setAttribute(POS_ID, lm.getPosid());

		} else if (isLogin == null || !isLogin) {
			// TODO use another Exception.
			throw new IllegalArgumentException("Not login yet!");
		}
		// pass the chain when
		// case 1: cmdId is INIT or LOGIN.
		// case 2: had login
		nextFilter.messageReceived(session, message);
	}

	@Override
	public void messageSent(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception {
		IBodyMessage msg = ((Message) writeRequest.getMessage()).getBodyMessage();
		long cmdId = msg.getCmdId();
		if (cmdId == CmdConstant.LOGIN_CMD_ID) {
			session.setAttribute(IS_LOGIN, false);

			LoginResponseMessage lm = (LoginResponseMessage) msg;
			int result = lm.getResult();
			if (LoginResult.SUCCESS.getPosCode() == result) {
				session.setAttribute(IS_LOGIN, true);
			}
		}
	}

}
