package com.chinarewards.qqgbvpn.domain;

import java.io.Serializable;
import java.util.List;

public class PageInfo<T> implements Serializable {

	private static final long serialVersionUID = 4959472402595748283L;

	private int pageSize = 4; // 每页显示4行

	private int recordCount; // 总行数

	private int pageCount; // 总页数

	private int pageId; // 当前页

	private int startIndex; // 当前页的开始行数

	private int endIndex; // 当前页的结束行数
	
	private List<T> items;

	public PageInfo() {

	}

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
