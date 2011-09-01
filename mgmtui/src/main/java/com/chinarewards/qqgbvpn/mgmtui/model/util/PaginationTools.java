package com.chinarewards.qqgbvpn.mgmtui.model.util;


public class PaginationTools {
	// 默认每页显示条数
	public static int DEFAULT_COUNT_ON_EACH_PAGE = 10;

	// 当前页记录起始位置
	private int startIndex;
 
	// 设置每页显示条数
	private int countOnEachPage; 

	public PaginationTools(int page,int pagesize) {
		this.setCountOnEachPage(pagesize);
		this.setPage(page);
	}
	
	private void setPage(int page) {
		if (page < 1) {
			startIndex = 0;
		} else {
			startIndex = (page - 1) * countOnEachPage;
		}
	}

	/**
	 * 默认实例方法，设置默认页显示条数
	 * 
	 */
	public PaginationTools() {
		startIndex = 0;
		countOnEachPage = DEFAULT_COUNT_ON_EACH_PAGE;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getCountOnEachPage() {
		return countOnEachPage;
	}

	public void setCountOnEachPage(int countOnEachPage) {
		this.countOnEachPage = countOnEachPage;
	}

	

}