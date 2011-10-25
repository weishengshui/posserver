package com.chinarewards.qqgbvpn.main.protocol.cmd;

import java.util.List;


/**
 * Search Response Message
 * 
 * @author huangwei
 *
 */
public class SearchResponseMessage implements ICommand {
	
	public long cmdId;
	
	public int result;
	
	public int totalnum;
	
	public int curnum;
	
	public int curpage;
	
	public int totalpage;
	
	public List<SearchResponseDetail> detail;
	
	//------------------------------------------------//
	
	@Override
	public String toString() {
		return " [cmdId=" + cmdId + ", result=" + result + ", totalnum="
				+ totalnum + ", curnum=" + curnum + ", curpage=" + curpage
				+ ", totalpage=" + totalpage + ", detail=" + detail + "]";
	}

	@Override
	public long getCmdId() {
		return cmdId;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getTotalnum() {
		return totalnum;
	}

	public void setTotalnum(int totalnum) {
		this.totalnum = totalnum;
	}

	public int getCurnum() {
		return curnum;
	}

	public void setCurnum(int curnum) {
		this.curnum = curnum;
	}

	public int getCurpage() {
		return curpage;
	}

	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}

	public int getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}

	public List<SearchResponseDetail> getDetail() {
		return detail;
	}

	public void setDetail(List<SearchResponseDetail> detail) {
		this.detail = detail;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	}
	
	

}
