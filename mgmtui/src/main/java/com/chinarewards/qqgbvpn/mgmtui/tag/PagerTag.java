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
		out.print("<div style='margin-top: 15px;'  class='page'>");
		if (pageInfo.getPageId() > 1) {
			out.print("<a href='javascript:goPage(1);' class='home'>首页</a>&nbsp;&nbsp;");
			out.print("<a href='javascript:goPage(" + (pageInfo.getPageId() - 1) + ");' class='up'> << 上一页</a>&nbsp;&nbsp;");
		} else {
			out.print("<span>首页</span>&nbsp;&nbsp;");
			out.print("<span> << 上一页</span>&nbsp;&nbsp;");
		}
		if (pageInfo.getPageId() < pageInfo.getPageCount()) {
			out.print("<a href='javascript:goPage(" + (pageInfo.getPageId() + 1) + ");' class='home'>下一页 >> </a>&nbsp;&nbsp;");
			out.print("<a href='javascript:goPage(" + pageInfo.getPageCount() + ");' class='up'>末页</a>&nbsp;&nbsp;");
		} else {
			out.print("<span>下一页 >> </span>&nbsp;&nbsp;");
			out.print("<span>末页</span>&nbsp;&nbsp;");
		}
		out.print("页&nbsp;" + pageInfo.getPageId() + "/" + pageInfo.getPageCount() + "&nbsp;&nbsp;");
		out.print("共&nbsp;" + pageInfo.getRecordCount() + "&nbsp;&nbsp;条&nbsp;&nbsp;");
		out.print("</div>");
	}
}
