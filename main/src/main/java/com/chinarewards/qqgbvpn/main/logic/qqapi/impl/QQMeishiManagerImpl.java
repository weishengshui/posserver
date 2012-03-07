package com.chinarewards.qqgbvpn.main.logic.qqapi.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qq.meishi.domain.QQMeishiXaction;
import com.chinarewards.qq.meishi.exception.QQMeishiDataParseException;
import com.chinarewards.qq.meishi.exception.QQMeishiInterfaceAccessException;
import com.chinarewards.qq.meishi.exception.QQMeishiReadStreamException;
import com.chinarewards.qq.meishi.service.QQMeishiService;
import com.chinarewards.qq.meishi.vo.MeishiConvertQQMiReqVO;
import com.chinarewards.qq.meishi.vo.MeishiConvertQQMiRespVO;
import com.chinarewards.qqgbvpn.common.DateTimeProvider;
import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.event.DomainEntity;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.logic.journal.JournalLogic;
import com.chinarewards.qqgbvpn.main.dao.qqapi.GroupBuyingDao;
import com.chinarewards.qqgbvpn.main.dao.qqapi.QQMeishiDao;
import com.chinarewards.qqgbvpn.main.exception.SaveDBException;
import com.chinarewards.qqgbvpn.main.logic.qqapi.QQMeishiManager;
import com.chinarewards.qqgbvpn.main.protocol.cmd.QQMeishiResponseMessage;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class QQMeishiManagerImpl implements QQMeishiManager {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject   
	private Provider<QQMeishiService> service;
	
	@Inject
	private Provider<QQMeishiDao> qqmeishiDao;
	
	@Inject
	private Provider<GroupBuyingDao> qqgbvDao;
	
	@Inject
	private JournalLogic journalLogic;
	
	@Inject
	private DateTimeProvider dtProvider;
	
	/**
	 * 包存交易记录到QQMeishiXaction表
	 * 以及记录交易日志
	 * @param postParams
	 * @param responseMessage
	 * @throws SaveDBException
	 */
	private void createQQMeishiXaction(HashMap<String, String> postParams, QQMeishiResponseMessage responseMessage)throws SaveDBException{
		
		String posId = postParams.get("posId");
		
		Pos pos = qqgbvDao.get().getPosByPosId(posId);		
		Agent agent = qqgbvDao.get().getAgentByPosId(posId);
		
		if (pos != null && agent != null) {
			Date date = dtProvider.getTime();
			QQMeishiXaction qqmeishiXaction = new QQMeishiXaction();
			String eventDetail = "";
			try{
				qqmeishiXaction.setAgentId(agent.getId());
				qqmeishiXaction.setAgentName(agent.getName());
				qqmeishiXaction.setConsumeAmount(Double.parseDouble(postParams.get("amount")));
				qqmeishiXaction.setForcePwdOnNextAction((responseMessage.getForcePwdNextAction() == 0) ? true :false);
				qqmeishiXaction.setPosId(posId);
				qqmeishiXaction.setPosModel(pos.getModel());
				qqmeishiXaction.setPosSimPhoneNo(pos.getSimPhoneNo());
				qqmeishiXaction.setQqUserToken(postParams.get("userToken"));
				qqmeishiXaction.setReceiptTip(responseMessage.getTip());
				qqmeishiXaction.setReceiptTitle(responseMessage.getTitle());
				qqmeishiXaction.setRemoteXactDate(responseMessage.getXactTime().getTime());
				qqmeishiXaction.setRemoteXactPwd(responseMessage.getPassword());
				qqmeishiXaction.setTs(date);
				qqmeishiXaction.setXactPwd(postParams.get("password"));
				qqmeishiXaction.setXactResultCode(Integer.parseInt(Long.toString(responseMessage.getResult())));
				
				//保存交易数据
				qqmeishiDao.get().saveQQMeishiXaction(qqmeishiXaction);
				
				ObjectMapper mapper = new ObjectMapper();
				eventDetail = mapper.writeValueAsString(qqmeishiXaction);
	
				//记录保存日志
				journalLogic.logEvent(DomainEvent.QQMEISHI_QMI_XACTION_OK.toString(), DomainEntity.QQMEISHIXACTION.toString(), posId, eventDetail);
			}catch(Exception e){
				log.error("qqmeishi transaction save error");
				log.error("posId: " + posId);
				log.error("ts: " + date);
				log.error("userToken: " + qqmeishiXaction.getQqUserToken());
				log.error("title" + qqmeishiXaction.getReceiptTitle());
				log.error("tip" + qqmeishiXaction.getReceiptTip());
				log.error("entity: " + DomainEntity.QQMEISHIXACTION.toString());
				log.error("entityId: " + qqmeishiXaction.getId());
				log.error("event: " + DomainEvent.QQMEISHI_QMI_XACTION_OK.toString());
				log.error("eventDetail: " + eventDetail);
				throw new SaveDBException(e);
			}
		}else{
			log.error("qqmeishi get pos or agent error");
			log.error("pos or agent not found by posId : " + posId);
			throw new SaveDBException("qqmeishi transaction error,pos or agent not found by posId : " + posId);
		}
		
		
		
	}

	/**
	 * QQ美食交易记录日子分四类
	 * 1.就是OK  记录reponseMessage, original content  event:QQMEISHI_QMI_XACTION_OK
	 * 2.qqmeishi error code 记录original content  event:QQMEISHI_QMI_XACTION_FAILED
	 * 3.qqmeishi 404  记录error code(404) ,original content event:QQMEISHI_QMI_XACTION_FAILED
	 * 4.connect qqmeishi server error 记录 StackTrace message event:QQMEISHI_QMI_XACTION_FAILED
	 */
	@Override
	public QQMeishiResponseMessage qqmeishiCommand(
			HashMap<String, String> postParams) throws SaveDBException,
			JsonGenerationException {
		
		String posId = postParams.get("posId");
		//封装VO
		MeishiConvertQQMiReqVO serverRequestVo = new MeishiConvertQQMiReqVO();
		serverRequestVo.setConsume(Float.parseFloat(postParams.get("amount")));
		serverRequestVo.setPassword(postParams.get("password"));
		serverRequestVo.setPosid(posId);
		serverRequestVo.setVerifyCode(postParams.get("userToken"));
		
		QQMeishiResponseMessage responseMessage = new QQMeishiResponseMessage();
		
		responseMessage.setServerErrorCode(0);
		String stackTraceMessage = "";
		int errorCode = 0;
		boolean isFailedJournalLogic = false;
		
		try {
			//访问腾讯
			MeishiConvertQQMiRespVO serverResponseVo = service.get().convertQQMi(serverRequestVo);

			//处理返回结果
 

			//XXX  to vo value ,check qqws error code
			responseMessage.setQqwsErrorCode(0);
			//result
			responseMessage.setResult(serverResponseVo.getValidCode());
			
			//forcePwdNextAction
			if(serverResponseVo.getHasPassword()){
				responseMessage.setForcePwdNextAction((byte)0);
			}else{
				responseMessage.setForcePwdNextAction((byte)1);
			}
			
			//xactTime
			String xactTime = serverResponseVo.getTradeTime();
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd'T'hhmmss");
			try {
				Calendar ca=Calendar.getInstance();
				ca.setTimeZone(TimeZone.getTimeZone("GMT"+xactTime.substring(xactTime.length()-5)));
				ca.setTime(format.parse(xactTime));
				responseMessage.setXactTime(ca);
			} catch (ParseException e) {
				log.error("error==:交易时间格式错误："+xactTime, e);
			}
			
			//title
			responseMessage.setTitle(serverResponseVo.getTitle());
			
			//tip
			responseMessage.setTip(serverResponseVo.getTip());
			
			//password
			responseMessage.setPassword(serverResponseVo.getPassword());
			
			//记录腾讯返回的数据到表：QQMeishiXaction
			this.createQQMeishiXaction(postParams, responseMessage);
			
		} catch (QQMeishiInterfaceAccessException e) {
			isFailedJournalLogic = true;
			log.error("error==:QQ美食接口访问异常", e);
		} catch (QQMeishiReadStreamException e) {
			e.getStackTrace();
			isFailedJournalLogic = true;
			log.error("error==:读取网络流异常", e);
		} catch (QQMeishiDataParseException e) {
			isFailedJournalLogic = true;
			log.error("error==:QQ美食数据解析异常", e);
		}

		//如果请求腾讯异常，记录日志
		if(isFailedJournalLogic){
			String eventDetail = "";

			ObjectMapper mapper = new ObjectMapper();
			try {
				eventDetail = mapper.writeValueAsString(serverRequestVo);
				//记录保存日志
				journalLogic.logEvent(DomainEvent.QQMEISHI_QMI_XACTION_FAILED.toString(), DomainEntity.QQMEISHIXACTION.toString(), posId, eventDetail);
			}catch(Exception e) {
				log.error("qqmeishi transaction save failed journal error");
				log.error("posId: " + posId);
				log.error("amount: " + serverRequestVo.getConsume());
				log.error("password" + serverRequestVo.getPassword());
				log.error("entity: " + DomainEntity.QQMEISHIXACTION.toString());
				log.error("userToken: " + serverRequestVo.getVerifyCode());
				log.error("event: " + DomainEvent.QQMEISHI_QMI_XACTION_FAILED.toString());
				log.error("eventDetail: " + eventDetail);
				throw new SaveDBException(e);
			}
		}
		
		return responseMessage;
	}
	
}
