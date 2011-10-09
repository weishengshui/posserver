package com.chinarewards.qqgbvpn.mgmtui.struts.action.finance;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.chinarewards.qqgbvpn.domain.FinanceReportHistory;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.exception.ServiceException;
import com.chinarewards.qqgbvpn.mgmtui.logic.agent.AgentLogic;
import com.chinarewards.qqgbvpn.mgmtui.logic.finance.FinanceManager;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentVO;
import com.chinarewards.qqgbvpn.mgmtui.struts.BaseAction;
import com.chinarewards.qqgbvpn.mgmtui.thread.CreateFinanceReport;
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
	
	private AgentLogic agentLogic;
	
	private PageInfo pageInfo;

	private Map<String,String> agent = new LinkedHashMap<String,String>();
	
	private Map<String,String> status = new LinkedHashMap<String,String>();
	
	private FinanceReportSearchVO searchVO = new FinanceReportSearchVO();

	public void validate(){
		
	}
	
	private void prepareAgent(){
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
	
	private void prepareStatus(){
		status.put("", "全部");
		status.put("CREATING", "生成中");
		status.put("COMPLETION", "完成");
		status.put("FAILED", "失败");
	}
	
	@Override
	public String execute() {
		try{
			pageInfo = new PageInfo();
			pageInfo.setPageId(1);
			pageInfo.setPageSize(INITPAGESIZE);
			pageInfo = getFinanceManager().searchFinanceReport(searchVO, pageInfo);
			
			prepareAgent();
		}catch(Exception e){
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}

	public String searchBill(){
		try{
			if(pageInfo == null || pageInfo.getPageId()==0){
				log.debug("pageInfo lost !");
			}
			pageInfo = getFinanceManager().searchFinanceReport(searchVO, pageInfo);
			prepareAgent();
		}catch(Exception e){
			e.printStackTrace();
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	public String generateExcel(){
		try{
			pageInfo = new PageInfo();
			pageInfo.setPageId(1);
			pageInfo.setPageSize(INITPAGESIZE);
			
			/*List<FinanceReportVO> vo = getFinanceManager().searchFinanceReport(searchVO);
			if(vo == null || "".equals(vo) || vo.size() == 0){
				HttpServletRequest request = ServletActionContext.getRequest ();
				request.setAttribute("generate", "false");
				log.debug("can not generate excel because no bill result.");
				return INPUT;
			}*/
			FinanceReportHistory history = getFinanceManager().createFinanceReportHistory(searchVO);
			//超时时间到时从配置文件中获取
			Thread createReport = new CreateFinanceReport(getFinanceManager(),searchVO,history.getId(),60000);
			createReport.start();
			
			pageInfo = getFinanceManager().searchFinanceReportHistory(searchVO, pageInfo);
		}catch(Exception e){
			e.printStackTrace();
			return ERROR;
		}
		/*prepareAgent();
		prepareStatus();*/
		return SUCCESS;
	}
	
	public String searchExcel(){
		pageInfo = new PageInfo();
		pageInfo.setPageId(1);
		pageInfo.setPageSize(INITPAGESIZE);
		pageInfo = getFinanceManager().searchFinanceReportHistory(searchVO, pageInfo);
		prepareAgent();
		prepareStatus();
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

	public Map<String, String> getStatus() {
		return status;
	}

	public void setStatus(Map<String, String> status) {
		this.status = status;
	}
	
}
