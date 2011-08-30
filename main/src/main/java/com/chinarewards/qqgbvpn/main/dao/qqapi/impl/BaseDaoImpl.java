package com.chinarewards.qqgbvpn.main.dao.qqapi.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class BaseDaoImpl {
	
	protected Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	protected Provider<EntityManager> em;
	
	/**
	 * 分布查询
	 * @author iori
	 * @param sql
	 * @param params 参数
	 * @param pageInfo
	 * @return
	 */
	protected PageInfo findPageInfo(String sql, List<Object> params, PageInfo pageInfo) {
		Query query = em.get().createQuery(sql);
		if (params != null && params.size() > 0) {
			//JPA参数下标从一开始
			for (int i = 1; i <= params.size(); i++) {
				query.setParameter(i, params.get(i-1));
			}
		}
		//记录总数
		int count = getCount(query);
		int pageId = pageInfo.getPageId();
        int pageSize = pageInfo.getPageSize();
        //总页数
        int pages = (count - 1) / pageSize + 1;
        int newPageId = pageId > pages ? pages : pageId;
        newPageId = newPageId < 1 ? 1 : newPageId;
        //记录开始行数
        int sIndex = (newPageId - 1) * pageSize + 1;
        sIndex = sIndex > count ? count : sIndex;
        //记录结束行数
        int eIndex = sIndex + pageSize -1;
        eIndex = eIndex > count ? count : eIndex;

        PageInfo newPageInfo = new PageInfo();
        newPageInfo.setPageId(newPageId);
        newPageInfo.setStartIndex(sIndex);
        newPageInfo.setEndIndex(eIndex);
        newPageInfo.setPageCount(pages);
        newPageInfo.setPageSize(pageSize);
        newPageInfo.setRecordCount(count);
        
        if (count > 0) {
        	query.setFirstResult(sIndex-1);
        	query.setMaxResults(pageSize);
        	//分布查询结果
        	List items = query.getResultList();
        	newPageInfo.setItems(items);
        }
        
		return newPageInfo;
	}
	
	/**
	 * 取记录总数
	 * @author iori
	 * @param query
	 * @return
	 */
	private int getCount(Query query) {
		int count = 0;
		List list = query.getResultList();
		if (list != null && !list.isEmpty()) {
			count = list.size();
		}
		return count;
	}
	
}
