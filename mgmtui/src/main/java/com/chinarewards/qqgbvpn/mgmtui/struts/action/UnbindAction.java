package com.chinarewards.qqgbvpn.mgmtui.struts.action;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;
import org.apache.struts2.StrutsStatics;
import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.core.mail.MailService;
import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.ReturnNote;
import com.chinarewards.qqgbvpn.mgmtui.exception.SaveDBException;
import com.chinarewards.qqgbvpn.mgmtui.exception.UnUseableRNException;
import com.chinarewards.qqgbvpn.mgmtui.logic.GroupBuyingUnbindManager;
import com.chinarewards.qqgbvpn.mgmtui.struts.BaseAction;
import com.chinarewards.qqgbvpn.qqapi.exception.MD5Exception;
import com.chinarewards.qqgbvpn.qqapi.exception.ParseXMLException;
import com.chinarewards.qqgbvpn.qqapi.exception.SendPostTimeOutException;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingUnbindVO;
import com.opensymphony.xwork2.ActionContext;

/**
 * pos unbind action
 * 
 * @author iori
 *
 */
public class UnbindAction extends BaseAction {

	private static final long serialVersionUID = -4872248136823406437L;
	
	private GroupBuyingUnbindManager groupBuyingUnbindMgr;
	
	private MailService mailService;
	
	protected Configuration configuration;

	private Agent agent;
	
	private String agentId;
	
	//private String aname;
	
	private String agentEmail;
	
	private PageInfo pageInfo;
	
	private String posIds;
	
	private String posId;
	
	private String rnId;
	
	private String inviteCode;
	
	private String agentName;
	
	private String posCondition;
	
	private List<Pos> posList;
	
	private String errorMsg;
	
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
	
	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	/*public String getAname() {
		return aname;
	}

	public void setAname(String aname) {
		this.aname = aname;
	}*/

	public String getAgentEmail() {
		return agentEmail;
	}

	public void setAgentEmail(String agentEmail) {
		this.agentEmail = agentEmail;
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
		pageInfo.setPageSize(10);
		return SUCCESS;
	}

	public String search() {
		if (agentName != null && !"".equals(agentName.trim())) {
			
			//EntityManager em = this.getInstance(EntityManager.class);
			//log.debug("em.xtaction.isActive: {}", em.getTransaction().isActive());
			//log.debug("em.xtaction.getRollbackOnly: {}", em.getTransaction().getRollbackOnly());
			
			Agent a = getGroupBuyingUnbindManager().getAgentByName(agentName.trim());
			if (a != null) {
				pageInfo.setPageId(1);
				pageInfo.setPageSize(10);
				pageInfo = getGroupBuyingUnbindManager().getPosByAgentId(pageInfo, a.getId());
				this.setAgentId(a.getId());
				this.setAgent(a);
				/*this.setAgentId(a.getId());
				this.setAname(a.getName());
				this.setAgentEmail(a.getEmail());*/
			} else {
				//这里应该报找不到的提示
				this.errorMsg = "第三方信息找不到!";
			}
		}
		return SUCCESS;
	}
	
	public String request() {
		if (inviteCode != null && !"".equals(inviteCode.trim())) {
			Agent a = getGroupBuyingUnbindManager().getAgentByInviteCode(inviteCode);
			if (a != null) {
				pageInfo = new PageInfo();
				pageInfo.setPageId(1);
				pageInfo.setPageSize(10);
				pageInfo = getGroupBuyingUnbindManager().getPosByAgentId(pageInfo, a.getId());
				this.setAgentId(a.getId());
				this.setAgentName(a.getName());
				this.setAgent(a);
				/*this.setAgentId(a.getId());
				this.setAname(a.getName());
				this.setAgentEmail(a.getEmail());*/
			} else {
				//这里应该报找不到的提示
				this.errorMsg = "无可用邀请!";
			}
		}
		return SUCCESS;
	}
	
	public String createInvite() throws JsonGenerationException
		, SaveDBException, UnsupportedEncodingException
		, MessagingException, javax.mail.MessagingException{
		if (this.getAgentId() != null && !"".equals(this.getAgentId().trim())) {
			//生成邀请单
			String inviteCode = getGroupBuyingUnbindManager().createInviteCode(this.getAgentId().trim());
			if (inviteCode != null) {
				//发送邮件
				String path = getEmailPath(inviteCode);
				String[] toAdds = {this.getAgentEmail()};
				String subject = "测试邮件";
				String content = "<html><body><br><a href='" + path + "'>请点击此链接进行回收POS机，谢谢</a></body></html>";
				getMailService().sendMail(toAdds, null, subject, content, null);
				return SUCCESS;
			}
		}
		//这里应该报第三方不能为空的提示
		this.errorMsg = "第三方信息找不到!";
		return SUCCESS;
	}
	
	public String confirmRnNumber() throws SaveDBException {
		if (posIds != null && !"".equals(posIds.trim())) {
			try {
				ReturnNote rn = getGroupBuyingUnbindManager().confirmReturnNote(
						this.getAgentId(), inviteCode, splitPosIds(posIds));
			} catch (UnUseableRNException e) {
				//TODO 这里到时改为不提示错误
				this.errorMsg = "回收单已使用!";
			}
		} else {
			// 这里应该报POS机不能为空的提示
			this.errorMsg = "POS机信息找不到!";
		}
		return SUCCESS;
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
			params.put("posId", new String[] { posId });
			params.put("key", getConfiguration().getString("txserver.key"));
			try {
				HashMap<String, Object> result = getGroupBuyingUnbindManager().groupBuyingUnbind(params);
				String resultCode = (String) result.get("resultCode");
				System.out.println("resultCode->" + resultCode);
				if ("0".equals(resultCode)) {
					List<GroupBuyingUnbindVO> items = (List<GroupBuyingUnbindVO>) result
							.get("items");
					for (GroupBuyingUnbindVO item : items) {
						System.out.println(item.getPosId());
						System.out.println(item.getResultStatus());
					}
				} else {
					switch (Integer.valueOf(resultCode)) {
					case -1:
						this.errorMsg = "服务器繁忙!";
						break;
					case -2:
						this.errorMsg = "md5校验失败!";
						break;
					case -3:
						this.errorMsg = "没有权限!";
						break;
					default:
						this.errorMsg = "未知错误!";
						break;
					}
				}
			} catch (JsonGenerationException e) {
				this.errorMsg = "生成JSON对象出错!";
				e.printStackTrace();
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
		if (agentName != null && !"".equals(agentName.trim())) {
			Agent a = getGroupBuyingUnbindManager().getAgentByName(agentName.trim());
			if (a != null) {
				this.setAgentId(a.getId());
				this.setAgentEmail(a.getEmail());
				this.setAgent(a);
				/*this.setAgentId(a.getId());
				this.setAname(a.getName());
				this.setAgentEmail(a.getEmail());*/
			} else {
				//这里应该报找不到的提示
				this.errorMsg = "第三方机信息找不到!";
			}
		}
		return SUCCESS;
	}
	
	protected List<String> splitPosIds(String ids) {
		return Arrays.asList(ids.split(","));
	}
	
	private String getEmailPath(String inviteCode) {
		HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		String path = request.getRequestURL().toString();
		String ctx = request.getContextPath();
		path = path.substring(0, path.indexOf(ctx)) + ctx + "/returnnote/request?inviteCode=" + inviteCode;
		return path;
	}

}
