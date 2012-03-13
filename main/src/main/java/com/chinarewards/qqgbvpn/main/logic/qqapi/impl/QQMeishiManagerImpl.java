package com.chinarewards.qqgbvpn.main.logic.qqapi.impl;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qq.meishi.domain.QQMeishiXaction;
import com.chinarewards.qq.meishi.domain.status.QQMeishiXactionErrorCode;
import com.chinarewards.qq.meishi.domain.status.QQMeishiXactionPosnetErrorCode;
import com.chinarewards.qq.meishi.exception.QQMeishiReadRespStreamException;
import com.chinarewards.qq.meishi.exception.QQMeishiReqDataDigestException;
import com.chinarewards.qq.meishi.exception.QQMeishiRespDataParseException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerLinkNotFoundException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerRespException;
import com.chinarewards.qq.meishi.exception.QQMeishiServerUnreachableException;
import com.chinarewards.qq.meishi.service.QQMeishiService;
import com.chinarewards.qq.meishi.vo.QQMeishiConvertQQMiReqVO;
import com.chinarewards.qq.meishi.vo.QQMeishiConvertQQMiRespVO;
import com.chinarewards.qq.meishi.vo.common.QQMeishiResp;
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
import com.chinarewards.qqgbvpn.main.vo.QQWsJouranlVo;
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

			qqmeishiXaction.setAgentId(agent.getId());
			qqmeishiXaction.setAgentName(agent.getName());
			qqmeishiXaction.setConsumeAmount(Double.parseDouble(postParams.get("amount")));
			qqmeishiXaction.setForcePwdOnNextAction((responseMessage.getForcePwdNextAction() == 0) ? true : false);
			qqmeishiXaction.setPosId(posId);
			qqmeishiXaction.setPosModel(pos.getModel());
			qqmeishiXaction.setPosSimPhoneNo(pos.getSimPhoneNo());
			qqmeishiXaction.setQqUserToken(postParams.get("userToken"));
			qqmeishiXaction.setReceiptTip(responseMessage.getTip());
			qqmeishiXaction.setReceiptTitle(responseMessage.getTitle());
			qqmeishiXaction.setRemoteXactDate(responseMessage.getXactTime());
			qqmeishiXaction.setRemoteXactPwd(responseMessage.getPassword());
			qqmeishiXaction.setTs(date);
			qqmeishiXaction.setXactPwd(postParams.get("password"));
			qqmeishiXaction.setXactResultCode(Integer.parseInt(Long.toString(responseMessage.getResult())));

			// 保存交易数据
			qqmeishiDao.get().saveQQMeishiXaction(qqmeishiXaction);
				
		}else{
			log.error("qqmeishi get pos or agent error");
			log.error("pos or agent not found by posId : " + posId);
			throw new SaveDBException("qqmeishi transaction error,pos or agent not found by posId : " + posId);
		}
	}
	
	/**
	 * 得到异常的堆栈信息
	 * @param e
	 * @return
	 */
	private String getExceptionStackTrace(Exception e){		
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		e.printStackTrace(new PrintWriter(buf, true));
		
		return buf.toString();
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
		QQMeishiConvertQQMiReqVO serverRequestVo = new QQMeishiConvertQQMiReqVO();
		serverRequestVo.setConsume(Float.parseFloat(postParams.get("amount")));
		serverRequestVo.setPassword(postParams.get("password"));
		serverRequestVo.setPosid(posId);
		serverRequestVo.setVerifyCode(postParams.get("userToken"));
		
		//实例化响应对象
		QQMeishiResponseMessage responseMessage = new QQMeishiResponseMessage();
		responseMessage.setServerErrorCode(QQMeishiXactionPosnetErrorCode.POSSEV_SUCCESS);
		//默认不需要输入密码
		responseMessage.setForcePwdNextAction((byte)1);
		
		//记录日志准备
		QQWsJouranlVo jouranlVo = new QQWsJouranlVo();
		ObjectMapper mapper = new ObjectMapper();
		String eventDetail = "";
		String event = "";
		
		try {
			// 访问腾讯
			QQMeishiResp<QQMeishiConvertQQMiRespVO> serverResponseVo = service.get()
					.convertQQMi(serverRequestVo);

			jouranlVo.setContent(serverResponseVo);
			
			
			//判读腾讯返回的结果
			/**
			 * 0 成功
			 * 1 必需参数未传递
			 * 2 方法内部执行错误，如系统繁忙
			 * 3 参数无效，请求方法的参数不合要求
			 * 4 sig 校验出错
			 * 5 没有权限操作
			 * 6 逻辑性错误
			 * 1001 非法的 client （尚未申请）
			 */
			int errorCode = serverResponseVo.getErrCode();
			
			// qqws error code
			responseMessage.setQqwsErrorCode(errorCode);
			
			QQMeishiConvertQQMiRespVO qqmeishiResponseVo = serverResponseVo.getResult();
			
			if(errorCode == QQMeishiXactionErrorCode.OK && qqmeishiResponseVo != null){ //QQ美食返回正确的结果
				//日志事件
				event = DomainEvent.QQMEISHI_QMI_XACTION_OK.toString();
				
				//处理返回结果				
				//result
				responseMessage.setResult(qqmeishiResponseVo.getValidCode());
				
				//forcePwdNextAction
				if(qqmeishiResponseVo.getHasPassword().booleanValue()){
					responseMessage.setForcePwdNextAction((byte)0);
				}else{
					responseMessage.setForcePwdNextAction((byte)1);
				}
				
				//xactTime
				String xactTime = qqmeishiResponseVo.getTradeTime();
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				try {
					responseMessage.setXactTime(format.parse(xactTime));
				} catch (ParseException e) {
					log.error("error==:交易时间格式错误："+xactTime, e);
				}
				
				//title
				responseMessage.setTitle(qqmeishiResponseVo.getTitle());
				
				//tip
				responseMessage.setTip(qqmeishiResponseVo.getTip());
				
				//password
				responseMessage.setPassword(qqmeishiResponseVo.getPassword());
				
				//记录腾讯返回的数据到表：QQMeishiXaction
				this.createQQMeishiXaction(postParams, responseMessage);
			
			}
			
		} catch (QQMeishiServerUnreachableException e) {
			jouranlVo.setStacktrace(getExceptionStackTrace(e));
			responseMessage.setServerErrorCode(QQMeishiXactionPosnetErrorCode.POSSEV_ERROR_QQWS_UNREACHABLE);
			log.error("error==:QQ美食服务器不可达", e);
		} catch (QQMeishiServerLinkNotFoundException e) {
			jouranlVo.setHttpStatusCode(e.getHttpStatusCode());
			jouranlVo.setRawContent(e.getRawContent());
			jouranlVo.setStacktrace(getExceptionStackTrace(e));
			responseMessage.setServerErrorCode(QQMeishiXactionPosnetErrorCode.POSSEV_ERROR_QQWS_NOCONNECT);
			log.error("error==:QQ美食服务器链接不存在", e);
		} catch (QQMeishiServerRespException e) {
			jouranlVo.setHttpStatusCode(e.getHttpStatusCode());
			jouranlVo.setRawContent(e.getRawContent());
			jouranlVo.setStacktrace(getExceptionStackTrace(e));
			responseMessage.setServerErrorCode(QQMeishiXactionPosnetErrorCode.POSSEV_ERROR_QQWS_RESPERROR);
			log.error("error==:QQ美食服务器响应异常", e);
		} catch (QQMeishiRespDataParseException e) {
			jouranlVo.setHttpStatusCode(e.getHttpStatusCode());
			jouranlVo.setRawContent(e.getRawContent());
			jouranlVo.setStacktrace(getExceptionStackTrace(e));
			responseMessage.setServerErrorCode(QQMeishiXactionPosnetErrorCode.POSSEV_ERROR_QQWS_DATAPARSE);
			log.error("error==:QQ美食响应数据解析异常", e);
		} catch (QQMeishiReadRespStreamException e) {
			jouranlVo.setHttpStatusCode(e.getHttpStatusCode());
			jouranlVo.setStacktrace(getExceptionStackTrace(e));
			responseMessage.setServerErrorCode(QQMeishiXactionPosnetErrorCode.POSSEV_ERROR_QQWS_IO);
			log.error("error==:QQ美食读取响应流异常", e);
		} catch (QQMeishiReqDataDigestException e) {
			jouranlVo.setStacktrace(getExceptionStackTrace(e));
			responseMessage.setServerErrorCode(QQMeishiXactionPosnetErrorCode.POSSEV_ERROR_QQWS_SIG);
			log.error("error==:QQ美食请求数据签名异常", e);
		}
		
		// 记录日志
		try {
			if (!(DomainEvent.QQMEISHI_QMI_XACTION_OK.toString().equals(event)))
				event = DomainEvent.QQMEISHI_QMI_XACTION_FAILED.toString();

			eventDetail = mapper.writeValueAsString(jouranlVo);

			journalLogic.logEvent(event, DomainEntity.QQMEISHIXACTION.toString(),
							posId, eventDetail);
		} catch (Exception e) {
			log.error("qqmeishi transaction save journal error");
			log.error("posId: " + posId);
			log.error("httpStatusCode: " + jouranlVo.getHttpStatusCode());
			log.error("rawcontent:" + jouranlVo.getRawContent());
			log.error("stackTrace: " + jouranlVo.getStacktrace());
			log.error("content: " + jouranlVo.getContent().toString());
			log.error("event: "	+ event);
			log.error("eventDetail: " + eventDetail);
			throw new SaveDBException(e);
		}
		
		return responseMessage;
	}
	
}
