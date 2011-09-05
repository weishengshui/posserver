package com.chinarewards.qqgbvpn.mgmtui.struts.action;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;
import org.codehaus.jackson.JsonGenerationException;

import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.ReturnNote;
import com.chinarewards.qqgbvpn.mgmtui.exception.SaveDBException;
import com.chinarewards.qqgbvpn.mgmtui.logic.GroupBuyingUnbindManager;
import com.chinarewards.qqgbvpn.mgmtui.service.MailService;
import com.chinarewards.qqgbvpn.mgmtui.struts.BaseAction;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.opensymphony.xwork2.ActionContext;

/**
 * pos unbind action
 * 
 * @author iori
 *
 */
public class UnbindAction extends BaseAction {

	private static final long serialVersionUID = -4872248136823406437L;
	
	@Inject
	private Provider<GroupBuyingUnbindManager> groupBuyingUnbindMgr;
	
	@Inject
	private Provider<MailService> mailService;

	private Agent agent;
	
	private PageInfo pageInfo;
	
	private String posIds;
	
	private String posId;
	
	private String rnId;
	
	private String agentName;
	
	private String posCondition;
	
	private List<Pos> posList;
	
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
			Agent a = groupBuyingUnbindMgr.get().getAgentByName(agentName.trim());
			if (a != null) {
				pageInfo.setPageId(1);
				pageInfo.setPageSize(10);
				pageInfo = groupBuyingUnbindMgr.get().getPosByAgentId(pageInfo, agent.getId());
				this.setAgent(a);
			} else {
				//这里应该报找不到的提示
				System.out.println("!!!!!!!!!!!agent 为空!!");
			}
		}
		return SUCCESS;
	}
	
	public String searchByAgent() {
		if (rnId != null && !"".equals(rnId.trim())) {
			Agent a = groupBuyingUnbindMgr.get().getAgentByRnId(rnId);
			if (a != null) {
				pageInfo = new PageInfo();
				pageInfo.setPageId(1);
				pageInfo.setPageSize(10);
				pageInfo = groupBuyingUnbindMgr.get().getPosByAgentId(pageInfo, a.getId());
				this.setAgent(a);
			} else {
				//这里应该报找不到的提示
				System.out.println("!!!!!!!!!!!agent 为空!!");
			}
		}
		return SUCCESS;
	}
	
	public String createRnNumber() throws JsonGenerationException
		, SaveDBException, UnsupportedEncodingException
		, MessagingException, javax.mail.MessagingException{
		if (agent.getId() != null && !"".equals(agent.getId().trim())) {
			//生成回收单
			ReturnNote rn = groupBuyingUnbindMgr.get().createReturnNoteByAgentId(agent.getId());
			//发送邮件
			HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
			String path = request.getRequestURL().toString();
			path = path.substring(0, path.lastIndexOf("/")) + "/searchByAgent?rnId=" + rn.getId();
			String[] toAdds = {agent.getEmail()};
			String subject = "测试邮件";
			String content = "<html><body><br><a href='" + path + "'>请点击此链接进行回收POS机，谢谢</a></body></html>";
			mailService.get().sendMail(toAdds, null, subject, content, null);
			return SUCCESS;
		} else {
			//这里应该报第三方不能为空的提示
			System.out.println("!!!!!!!!!!!agent.getId(): 为空!!");
		}
		return SUCCESS;
	}
	
	public String confirmRnNumber() throws JsonGenerationException, SaveDBException{
		if (posIds != null && !"".equals(posIds.trim())) {
			System.out.println("!!!!!!!!!!!posIds: " + posIds);
			ReturnNote rn = groupBuyingUnbindMgr.get().confirmReturnNote(agent.getId(), rnId, posIds);
		} else {
			//这里应该报POS机不能为空的提示
			System.out.println("!!!!!!!!!!!posIds: 为空!!");
		}
		return SUCCESS;
	}
	
	public String posSearch() {
		if (posCondition != null && !"".equals(posCondition.trim())) {
			posList = groupBuyingUnbindMgr.get().getPosByPosInfo(posCondition.trim());
		}
		return SUCCESS;
	}
	
	public String unbind(){
		if (posId != null && !"".equals(posId.trim())) {
			System.out.println("!!!!!!!!!!!!!posId: " + posId);
			/*HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("posId", new String[] { posId });
			params.put("key", new PosNetworkProperties().getTxServerKey());
			HashMap<String, Object> map = groupBuyingUnbindMgr.get().groupBuyingUnbind(params);*/
		}
		return SUCCESS;
	}
	
	public String sendURL() {
		if (agentName != null && !"".equals(agentName.trim())) {
			Agent a = groupBuyingUnbindMgr.get().getAgentByName(agentName.trim());
			if (a != null) {
				this.setAgent(a);
			} else {
				//这里应该报找不到的提示
				System.out.println("!!!!!!!!!!!agent 为空!!");
			}
		}
		return SUCCESS;
	}

}
