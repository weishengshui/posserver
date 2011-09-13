package com.chinarewards.qqgbvpn.mgmtui.struts.action;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;
import org.apache.struts2.StrutsStatics;

import com.chinarewards.qqgbvpn.core.mail.MailService;
import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.ReturnNote;
import com.chinarewards.qqgbvpn.mgmtui.exception.SaveDBException;
import com.chinarewards.qqgbvpn.mgmtui.exception.UnUseableRNException;
import com.chinarewards.qqgbvpn.mgmtui.logic.GroupBuyingUnbindManager;
import com.chinarewards.qqgbvpn.mgmtui.struts.BaseAction;
import com.chinarewards.qqgbvpn.mgmtui.vo.ReturnNoteInfo;
import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;
import com.chinarewards.utils.StringUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * pos unbind action
 * 
 * @author iori
 *
 */
public class UnbindAction extends BaseAction {

	private static final long serialVersionUID = -4872248136823406437L;
	
	private static final int initPageSize = 10;
	
	private GroupBuyingUnbindManager groupBuyingUnbindMgr;
	
	private MailService mailService;
	
	protected Configuration configuration;
	
	private HttpServletRequest request;

	private Agent agent;
	
	private String agentId;
	
	private String agentEmail;
	
	private PageInfo pageInfo;
	
	private String posIds;
	
	private String posId;
	
	private String rnId;
	
	private String rnNum;
	
	private Date rnTime;
	
	private Integer posCount;
	
	private String inviteCode;
	
	private String agentName;
	
	private String posCondition;
	
	private List<Pos> posList;
	
	private List<Agent> agentList;
	
	private String isAgent;
	
	private Date sendTime;
	
	private String passTime;
	
	private ReturnNoteInfo rnInfo;
	
	private String errorMsg;
	
	private String successMsg;
	
	private GroupBuyingUnbindManager getGroupBuyingUnbindManager() {
		groupBuyingUnbindMgr = super.getInstance(GroupBuyingUnbindManager.class);
		return groupBuyingUnbindMgr;
	}
	
	private MailService getMailService() {
		mailService = super.getInstance(MailService.class);
		return mailService;
	}
	
	private Configuration getConfiguration() {
		configuration = super.getInstance(Configuration.class);
		return configuration;
	}
	
	public String getPassTime() {
		return passTime;
	}

	public void setPassTime(String passTime) {
		this.passTime = passTime;
	}

	public Integer getPosCount() {
		return posCount;
	}

	public void setPosCount(Integer posCount) {
		this.posCount = posCount;
	}

	public String getIsAgent() {
		return isAgent;
	}

	public void setIsAgent(String isAgent) {
		this.isAgent = isAgent;
	}

	public Date getRnTime() {
		return rnTime;
	}

	public void setRnTime(Date rnTime) {
		this.rnTime = rnTime;
	}

	public ReturnNoteInfo getRnInfo() {
		return rnInfo;
	}

	public void setRnInfo(ReturnNoteInfo rnInfo) {
		this.rnInfo = rnInfo;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getRnNum() {
		return rnNum;
	}

	public void setRnNum(String rnNum) {
		this.rnNum = rnNum;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public List<Agent> getAgentList() {
		return agentList;
	}

	public void setAgentList(List<Agent> agentList) {
		this.agentList = agentList;
	}

	public String getAgentEmail() {
		return agentEmail;
	}

	public void setAgentEmail(String agentEmail) {
		this.agentEmail = agentEmail;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public List<Pos> getPosList() {
		return posList;
	}

	public void setPosList(List<Pos> posList) {
		this.posList = posList;
	}

	public String getPosCondition() {
		return posCondition;
	}

	public void setPosCondition(String posCondition) {
		this.posCondition = posCondition;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getRnId() {
		return rnId;
	}

	public void setRnId(String rnId) {
		this.rnId = rnId;
	}

	public String getPosIds() {
		return posIds;
	}

	public void setPosIds(String posIds) {
		this.posIds = posIds;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	
	@Override
	public String execute() {
		agent = new Agent();
		pageInfo = new PageInfo();
		pageInfo.setPageId(1);
		pageInfo.setPageSize(initPageSize);
		return SUCCESS;
	}

	public String search() {
		posIds = "";
		if (agentName != null && !"".equals(agentName.trim())) {
			Agent a = getGroupBuyingUnbindManager().getAgentByName(agentName.trim());
			if (a != null) {
				pageInfo = new PageInfo();
				pageInfo.setPageId(1);
				pageInfo.setPageSize(initPageSize);
				pageInfo = getGroupBuyingUnbindManager().getPosByAgentId(pageInfo, a.getId());
				this.setAgentId(a.getId());
				this.setAgent(a);
			} else {
				//这里应该报找不到的提示
				this.errorMsg = "第三方信息找不到!";
			}
		}
		return SUCCESS;
	}
	
	public String request() {
		posIds = "";
		if (inviteCode != null && !"".equals(inviteCode.trim())) {
			Agent a = getGroupBuyingUnbindManager().getAgentByInviteCode(inviteCode.trim());
			if (a != null) {
				log.debug("a.getId() : {}",a.getId());
				pageInfo = new PageInfo();
				pageInfo.setPageId(1);
				pageInfo.setPageSize(initPageSize);
				pageInfo = getGroupBuyingUnbindManager().getPosByAgentId(pageInfo, a.getId());
				List<Pos> posList = pageInfo.getItems();
				for (Pos p : posList) {
					log.debug("p.getDstatus() : {}",p.getDstatus());
				}
				this.setAgentId(a.getId());
				this.setAgentName(a.getName());
				this.setAgent(a);
			} else {
				//这里应该报找不到的提示
				this.errorMsg = "无可用邀请!";
			}
		}
		return SUCCESS;
	}
	
	public String goPage() {
		if (pageInfo == null) {
			pageInfo = new PageInfo();
			pageInfo.setPageId(1);
			pageInfo.setPageSize(initPageSize);
		}
		pageInfo = getGroupBuyingUnbindManager().getPosByAgentId(pageInfo, this.getAgentId().trim());
		return SUCCESS;
	}
	
	public String createInvite() throws UnsupportedEncodingException, MessagingException{
		if (this.getAgentId() != null && !"".equals(this.getAgentId().trim())) {
			//生成邀请单
			String inviteCode = getGroupBuyingUnbindManager().createInviteCode(this.getAgentId().trim());
			if (inviteCode != null) {
				//发送邮件
				String path = getInviteEmailPath(inviteCode);
				String[] toAdds = {this.getAgentEmail()};
				String subject = "邀请填写申请表";
				String content = "<html><body><br><a href='" + path + "'>请点击此链接填写申请表，谢谢。</a></body></html>";
				getMailService().sendMail(toAdds, null, subject, content, null);
				this.setAgentName(this.getAgentName());
				//this.setSendTime(new Date());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				this.setPassTime(sdf.format(new Date()));
				return SUCCESS;
			}
		}
		//这里应该报第三方不能为空的提示
		this.errorMsg = "第三方信息找不到!";
		return ERROR;
	}
	
	public String confirmRnNumber() throws SaveDBException {
		if (inviteCode == null) {
			inviteCode = "";
		}
		if (posIds != null && !"".equals(posIds.trim())) {
			List<String> posList = splitPosIds(posIds.trim());
			ReturnNote rn = null;
			String errInfo = "";
			try {
				rn = getGroupBuyingUnbindManager().confirmReturnNote(
						this.getAgentId(), inviteCode.trim(), posList);
			} catch (UnUseableRNException e1) {
				errInfo = e1.getMessage();
			}
			if (rn != null) {
				//受邀者填写完后发邮件给我方
				if (!StringUtil.isEmptyString(inviteCode)) {
					String[] toAdds = {getConfiguration().getString("company.email")};
					String subject = "第三方成功填写申请表";
					String content = "<html><body><br>" + this.getAgentName() + "已成功填写申请表，共申请回收" + posList.size() + "台POS机。</body></html>";
					try {
						getMailService().sendMail(toAdds, null, subject, content, null);
					} catch (Throwable e) {
						
					}
					this.setIsAgent("true");
				}
				this.setPosCount(splitPosIds(posIds.trim()).size());
				this.setRnId(rn.getId());
				this.setRnNum(rn.getRnNumber());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				this.setPassTime(sdf.format(rn.getCreateDate()));
				//this.setRnTime(rn.getCreateDate());
				return SUCCESS;
			}else {
				this.errorMsg = "第三方信息找不到!";
			}
		} else {
			// 这里应该报POS机不能为空的提示
			this.errorMsg = "POS机信息找不到!";
		}
		return ERROR;
	}
	
	public String confirmSuccess() {
		return SUCCESS;
	}
	
	public String unbindSuccess() {
		return SUCCESS;
	}
	
	public String confirmAllRnNumber() throws SaveDBException {
		if (!StringUtil.isEmptyString(agentId)) {
			ReturnNoteInfo rnInfo = getGroupBuyingUnbindManager().confirmAllReturnNote(agentId.trim());
			if (rnInfo != null) {
				this.setPosCount(rnInfo.getPosList() != null ? rnInfo.getPosList().size() : 0);
				this.setRnId(rnInfo.getRn().getId());
				this.setRnNum(rnInfo.getRn().getRnNumber());
				//this.setRnTime(rnInfo.getRn().getCreateDate());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				this.setPassTime(sdf.format(rnInfo.getRn().getCreateDate()));
				return SUCCESS;
			}
		}
		this.errorMsg = "第三方信息找不到!";
		return ERROR;
	}
	
	public String posSearch() {
		if (posCondition != null && !"".equals(posCondition.trim())) {
			posList = getGroupBuyingUnbindManager().getPosByPosInfo(posCondition.trim());
			if (posList == null || posList.size() == 0) {
				this.errorMsg = "POS机信息找不到!";
			}
		}
		return SUCCESS;
	}
	
	public String unbind(){
		if (posId != null && !"".equals(posId.trim())) {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("posId", new String[] { posId.trim() });
			params.put("key", getConfiguration().getString("txserver.key"));
			try {
				HashMap<String, Object> result = getGroupBuyingUnbindManager().groupBuyingUnbind(params);
				String resultCode = (String) result.get("resultCode");
				if ("0".equals(resultCode)) {
					this.successMsg = posId + "解绑成功!";
				} else {
					switch (Integer.valueOf(resultCode)) {
					case -1:
						this.errorMsg = "服务器繁忙!";
						break;
					case -2:
						this.errorMsg = "MD5校验失败!";
						break;
					case -3:
						this.errorMsg = "没有权限!";
						break;
					default:
						this.errorMsg = "未知错误!";
						break;
					}
				}
			} catch (MD5Exception e) {
				this.errorMsg = "生成MD5校验位出错!";
				e.printStackTrace();
			} catch (ParseXMLException e) {
				this.errorMsg = "解析XML出错!";
				e.printStackTrace();
			} catch (SendPostTimeOutException e) {
				this.errorMsg = "POST连接出错!";
				e.printStackTrace();
			} catch (SaveDBException e) {
				this.errorMsg = "后台保存数据库出错!";
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	public String sendURL() {
		if (agentName == null) {
			agentName = "";
		}
		List<Agent> list = getGroupBuyingUnbindManager().getAgentLikeName(agentName.trim());
		if (list != null && list.size() > 0) {
			this.setAgentList(list);
		} else {
			//这里应该报找不到的提示
			this.errorMsg = "第三方机信息找不到!";
		}
		return SUCCESS;
	}
	
	public String sendURLSuccess() {
		return SUCCESS;
	}
	
	public String getReturnNoteList() {
		if (rnNum == null) {
			rnNum = "";
		}
		pageInfo = new PageInfo();
		pageInfo.setPageId(1);
		pageInfo.setPageSize(initPageSize);
		pageInfo = getGroupBuyingUnbindManager().getReturnNoteLikeRnNumber(rnNum.trim(), pageInfo);
		return SUCCESS;
	}
	
	public String goPageForRnList() {
		if (rnNum == null) {
			rnNum = "";
		}
		if (pageInfo == null) {
			pageInfo = new PageInfo();
			pageInfo.setPageId(1);
			pageInfo.setPageSize(initPageSize);
		}
		pageInfo = getGroupBuyingUnbindManager().getReturnNoteLikeRnNumber(rnNum.trim(), pageInfo);
		return SUCCESS;
	}
	
	public String getReturnNoteInfo() {
		if (!StringUtil.isEmptyString(rnId)) {
			rnInfo = getGroupBuyingUnbindManager().getReturnNoteInfoByRnId(rnId.trim());
		}
		return SUCCESS;
	}
	
	protected List<String> splitPosIds(String ids) {
		return Arrays.asList(ids.split(","));
	}
	
	private String getInviteEmailPath(String inviteCode) {
		String path = getRequest().getRequestURL().toString();
		String ctx = getRequest().getContextPath();
		path = path.substring(0, path.indexOf(ctx)) + ctx + "/returnnote/request?inviteCode=" + inviteCode;
		return path;
	}
	
	private HttpServletRequest getRequest() {
		if (request == null) {
			request = (HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		}
		return request;
	}

}
