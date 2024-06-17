package com.dbt.framework.pagination;

import java.util.List;

public class PageResult<T> {

	/* 数据结果集 */
	private List<T> resultList;
	/* 数据结果集条数 */
	private long countAll;
	/* 分页数据 */
	private PageOrderInfo pageOrderInfo;
	
	public PageResult(List<T> resultList, long countAll, PageOrderInfo pageOrderInfo){
		this.resultList = resultList;
		this.countAll = countAll;
		this.pageOrderInfo = pageOrderInfo;
	}
	
	public List<T> getResultList() {
		return resultList;
	}
	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}
	public long getCountAll() {
		return countAll;
	}
	public void setCountAll(long countAll) {
		this.countAll = countAll;
	}
	public PageOrderInfo getPageOrderInfo() {
		return pageOrderInfo;
	}
	public void setPageOrderInfo(PageOrderInfo pageOrderInfo) {
		this.pageOrderInfo = pageOrderInfo;
	}
	
	
}
