/**
 * 
 */
package com.chinarewards.qqgbvpn.main.protocol.filter;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.login.LoginResult;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.ErrorBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.IBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.InitRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.LoginRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.LoginResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.Message;
import com.chinarewards.utils.StringUtil;

/**
 * Login filter.
 * 
 * @author cream
 * @since 1.0.0 2011-08-29
 */
public class LoginFilter extends IoFilterAdapter {

	public final static String IS_LOGIN = "is_login";
	public final static String POS_ID = "pos_id";

	Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception {
		Boolean isLogin = (Boolean) session.getAttribute(IS_LOGIN);

		// Check whether the command ID is LOGIN
		Message messageTmp = (Message)message;
		IBodyMessage msg = messageTmp.getBodyMessage();
		long cmdId = msg.getCmdId();
		if (cmdId == CmdConstant.INIT_CMD_ID) {
			// get POS ID
			InitRequestMessage im = (InitRequestMessage) msg;
			session.setAttribute(POS_ID, im.getPosid());
		} else if (cmdId == CmdConstant.LOGIN_CMD_ID) {
			// get POS ID
			LoginRequestMessage lm = (LoginRequestMessage) msg;
			session.setAttribute(POS_ID, lm.getPosid());

		} else if (isLogin == null || !isLogin) {
			ErrorBodyMessage bodyMessage = new ErrorBodyMessage();
			bodyMessage.setErrorCode(CmdConstant.ERROR_NO_LOGIN_CODE);
			messageTmp.setBodyMessage(bodyMessage);
			session.write(messageTmp);
			log.debug("not login....");
			return;
		}

		// Check POS ID for other connection(NOT init or login).
		String posId = (String) session.getAttribute(POS_ID);

		if (StringUtil.isEmptyString(posId)) {
			throw new IllegalArgumentException("Pos Id not existed!");
		}

		// pass the chain when
		// case 1: cmdId is INIT or LOGIN.
		// case 2: had login
		nextFilter.messageReceived(session, messageTmp);
	}

	@Override
	public void messageSent(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception {
		IBodyMessage msg = ((Message) writeRequest.getMessage())
				.getBodyMessage();
		long cmdId = msg.getCmdId();
		if (cmdId == CmdConstant.LOGIN_CMD_ID_RESPONSE) {
			session.setAttribute(IS_LOGIN, false);

			LoginResponseMessage lm = (LoginResponseMessage) msg;
			int result = lm.getResult();
			if (LoginResult.SUCCESS.getPosCode() == result) {
				session.setAttribute(IS_LOGIN, true);
			}
		}
	}

}
