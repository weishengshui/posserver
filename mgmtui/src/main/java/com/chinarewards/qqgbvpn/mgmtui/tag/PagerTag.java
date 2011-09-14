package com.chinarewards.qqgbvpn.mgmtui.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.chinarewards.qqgbvpn.domain.PageInfo;

public class PagerTag extends SimpleTagSupport {

	private PageInfo pageInfo;
	
	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

	@Override
	public void doTag() throws JspException, IOException {
		super.doTag();
		JspWriter out  = this.getJspContext().getOut();
		out.print("页&nbsp;" + pageInfo.getPageId() + "/" + pageInfo.getPageCount() + "&nbsp;&nbsp;");
		out.print("共&nbsp;" + pageInfo.getRecordCount() + "&nbsp;&nbsp;条&nbsp;&nbsp;");
		if (pageInfo.getPageId() > 1) {
			out.print("<a href='javascript:goPage(" + (pageInfo.getPageId() - 1) + ");'>上一页</a>&nbsp;&nbsp;");
		}
		if (pageInfo.getPageId() < pageInfo.getPageCount()) {
			out.print("<a href='javascript:goPage(" + (pageInfo.getPageId() + 1) + ");'>下一页</a>");
		}
	}
}
