package com.chinarewards.qqgbvpn.mgmtui.struts.action.finance;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.exception.ServiceException;
import com.chinarewards.qqgbvpn.mgmtui.logic.agent.AgentLogic;
import com.chinarewards.qqgbvpn.mgmtui.logic.finance.FinanceManager;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentVO;
import com.chinarewards.qqgbvpn.mgmtui.struts.BaseAction;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportSearchVO;

/**
 * finance action
 * 
 * @author iori
 *
 */
public class FinanceAction extends BaseAction {

	private static final long serialVersionUID = -8740228698739172297L;
	
	private static final int INITPAGESIZE = 10;
	
	private FinanceManager financeManager;
	
	private AgentLogic agentLogic;
	
	private PageInfo pageInfo;

	private Map<String,String> agent = new LinkedHashMap<String,String>();
	
	private FinanceReportSearchVO searchVO = new FinanceReportSearchVO();

	public void validate(){
		
	}
	
	private void prepareData(){
		agent.put("","无");
		try {
			List<AgentVO> list = getAgentLogic().findAllAgent();
			if (list != null) {
				for(AgentVO v:list){
					agent.put(v.getId(), v.getName());
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String execute() {
		pageInfo = new PageInfo();
		pageInfo.setPageId(1);
		pageInfo.setPageSize(INITPAGESIZE);
		pageInfo = getFinanceManager().searchFinanceReport(searchVO, pageInfo);
		
		prepareData();
		return SUCCESS;
	}

	public String searchBill(){
		pageInfo = getFinanceManager().searchFinanceReport(searchVO, pageInfo);
		prepareData();
		return SUCCESS;
	}
	
	public String generateExel(){
		return SUCCESS;
	}
	
	public PageInfo getPageInfo() {
		return pageInfo;
	}
	
	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

	public FinanceManager getFinanceManager() {
		financeManager = super.getInstance(FinanceManager.class);
		return financeManager;
	}

	public AgentLogic getAgentLogic() {
		agentLogic = super.getInstance(AgentLogic.class);
		return agentLogic;
	}

	public Map<String, String> getAgent() {
		return agent;
	}

	public void setAgent(Map<String, String> agent) {
		this.agent = agent;
	}

	public void setSearchVO(FinanceReportSearchVO searchVO) {
		this.searchVO = searchVO;
	}

	public FinanceReportSearchVO getSearchVO() {
		return searchVO;
	}
	
	
}
