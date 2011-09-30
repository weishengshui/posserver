package com.chinarewards.qqgbvpn.mgmtui.struts.action.finance;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.logic.finance.FinanceManager;
import com.chinarewards.qqgbvpn.mgmtui.struts.BaseAction;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportVO;

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
	
	private PageInfo pageInfo;

	private Map<String,String> agent = new LinkedHashMap<String,String>();
	
	private FinanceReportSearchVO searchVO = new FinanceReportSearchVO();

	public void validate(){
		
	}
	
	private void preparedata(){
		agent.put("","无");
		agent.put("40e6bda2-d37b-11e0-b051-f0095cb162d9","代理商A");
		agent.put("40eec751-d37b-11e0-b051-f0095cb162d9","代理商B");
		agent.put("40f37641-d37b-11e0-b051-f0095cb162d9","代理商C");
	}
	
	@Override
	public String execute() {
		pageInfo = new PageInfo();
		pageInfo.setPageId(1);
		pageInfo.setPageSize(INITPAGESIZE);
		pageInfo = getFinanceManager().searchFinanceReport(searchVO, pageInfo);
		
		preparedata();
		return SUCCESS;
	}

	public String searchBill(){
		pageInfo = getFinanceManager().searchFinanceReport(searchVO, pageInfo);
		preparedata();
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
