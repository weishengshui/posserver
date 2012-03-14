package com.chinarewards.qqgbvpn.main.logic.qqapi;

import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.QQMeishiResponseMessage;


public interface QQMeishiManager {
	
	/**
	 * QQ美食POS机奖励Q米
	 * @param postParams
	 * @return
	 * @throws SaveDBException
	 * @throws JsonGenerationException
	 */
	public QQMeishiResponseMessage qqmeishiCommand(HashMap<String, String> postParams)throws SaveDBException,JsonGenerationException;
}
