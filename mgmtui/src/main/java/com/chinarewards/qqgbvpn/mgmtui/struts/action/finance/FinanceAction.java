package com.chinarewards.qqgbvpn.mgmtui.struts.action.finance;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinarewards.qqgbvpn.domain.FinanceReportHistory;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.exception.ServiceException;
import com.chinarewards.qqgbvpn.mgmtui.logic.agent.AgentLogic;
import com.chinarewards.qqgbvpn.mgmtui.logic.finance.FinanceManager;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentVO;
import com.chinarewards.qqgbvpn.mgmtui.struts.BaseAction;
import com.chinarewards.qqgbvpn.mgmtui.thread.CreateFinanceReport;
import com.chinarewards.qqgbvpn.mgmtui.util.Tools;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportSearchVO;
import com.chinarewards.utils.StringUtil;

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

	private String fileName;
	
	private String reportId;
	
	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

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
				pageInfo = new PageInfo();
				pageInfo.setPageId(1);
				pageInfo.setPageSize(INITPAGESIZE);
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
		return SUCCESS;
	}
	
	public String searchExcel(){
		if(pageInfo == null || pageInfo.getPageId()==0){
			pageInfo = new PageInfo();
			pageInfo.setPageId(1);
			pageInfo.setPageSize(INITPAGESIZE);
		}
		System.out.println("getFinanceReportHistoryStatus:" + searchVO.getFinanceReportHistoryStatus());
		pageInfo = getFinanceManager().searchFinanceReportHistory(searchVO, pageInfo);
		if (pageInfo != null) {
			List<FinanceReportHistory> list = pageInfo.getItems();
			if (list != null && list.size() > 0) {
				List<FinanceReportHistory> tempList = new ArrayList<FinanceReportHistory>();
				for (FinanceReportHistory history : list) {
					if (!StringUtil.isEmptyString(history.getReportDetail())) {
						history.setReportDetail(Tools.charConversion(history.getReportDetail()));
					}
					tempList.add(history);
				}
				pageInfo.setItems(tempList);
			}
		}
		prepareAgent();
		prepareStatus();
		return SUCCESS;
	}
	
	public String downLoadExcel(){
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

	public String getFileName() {
		String tempName= "";
		try {
			tempName = new String(fileName.getBytes(),"ISO8859-1"); //把file转换成ISO8859-1编码格式
		} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
		}
		return tempName; 
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public InputStream getInputStream() throws FileNotFoundException, UnsupportedEncodingException {
		FinanceReportHistory report = getFinanceManager().getFinanceReportHistoryById(reportId);
		System.out.println("report.getReportDetail():" + report.getReportDetail());
		//return new StringBufferInputStream(report.getReportDetail());
		return new ByteArrayInputStream(report.getReportDetail().getBytes("gb2312"));
		} 
}
