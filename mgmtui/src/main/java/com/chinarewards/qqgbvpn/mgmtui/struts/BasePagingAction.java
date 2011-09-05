package com.chinarewards.qqgbvpn.mgmtui.struts;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author huangwei
 *
 */
public class BasePagingAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1746696581124894977L;

	/**
	 * 当前页
	 */
	protected int page = 1;
	
	/**
	 * 总页数
	 */
	protected int pageCount;
	
	/**
	 * 首页，上一页是否显示
	 */
	protected boolean startFlag = false;
	
	/**
	 * 下一页，末页是否显示
	 */
	protected boolean endFlag = false;
	
	/**
	 * 分页列表
	 */
	protected List<String[]> pageList = new ArrayList<String[]>();
	
	/**
	 * 每页显示记录条数
	 */
	protected  int countEachPage = 15;
	
	/**
	 * 分页显示参数，每页显示的页数
	 */
	private  final int COUNT_NUMBER_PAGE = 5;
	
	protected void insertPageList(int countTotal,int page){
		if(countEachPage < 1){
			countEachPage = 15;
		}
		pageCount = countTotal/countEachPage + (countTotal%countEachPage==0?0:1);
		int start = page - COUNT_NUMBER_PAGE/2;
		int end = page + COUNT_NUMBER_PAGE/2;
		if(start < 1){
			end = end + 1 - start;
			start = 1;
		}
		if(end > pageCount){
			end = pageCount;
		}
		for(int i = start; i <= end;i++){
			String[] pageListStr = new String[2];
			pageListStr[0] = ""+i;
			if(i == page){
				pageListStr[1] = "true";
			}else{
				pageListStr[1] = "false";
			}
			pageList.add(pageListStr);
		}
		if(pageList != null && pageList.size() == 1){
			pageList.clear();
		}
		if(page > 1 && pageCount > 0){
			this.startFlag = true;
		}
		if(page < pageCount && pageCount > 0){
			this.endFlag = true;
		}
		
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public boolean isStartFlag() {
		return startFlag;
	}

	public void setStartFlag(boolean startFlag) {
		this.startFlag = startFlag;
	}

	public boolean isEndFlag() {
		return endFlag;
	}

	public void setEndFlag(boolean endFlag) {
		this.endFlag = endFlag;
	}

	public List<String[]> getPageList() {
		return pageList;
	}

	public void setPageList(List<String[]> pageList) {
		this.pageList = pageList;
	}

	public int getCountEachPage() {
		return countEachPage;
	}

	public void setCountEachPage(int countEachPage) {
		this.countEachPage = countEachPage;
	}
	
	
	
	
}
