package com.chinarewards.qqgbvpn.mgmtui.struts.action.finance;

import java.util.ArrayList;

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

	@Override
	public String execute() {
		FinanceReportSearchVO searchVO = new FinanceReportSearchVO();
		pageInfo = new PageInfo();
		pageInfo.setPageId(1);
		pageInfo.setPageSize(INITPAGESIZE);
//		pageInfo = getFinanceManager().searchFinanceReport(searchVO, pageInfo);
		pageInfo.setItems(new ArrayList<FinanceReportVO>());
		System.out.println("size : " + pageInfo.getItems().size());
		return SUCCESS;
	}


}
