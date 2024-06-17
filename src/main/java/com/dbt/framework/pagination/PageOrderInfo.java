package com.dbt.framework.pagination;

import java.io.Serializable;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;


public class PageOrderInfo implements Serializable{
	private static final long serialVersionUID = 5079656427437986387L;
	private int startCount;
	private int pagePerCount;
	private int currentPage;
	private String orderCol;
	private String orderType;
	
	public static final int DEFAULT_START = 0;
	public static final int DEFAULT_PER = 15;
	public static final int DEFAULT_CUR = 1;
	public static final String ORDER_ASC = "asc";
	public static final String ORDER_DESC = "desc";
	PageOrderInfo(){
		this.startCount = DEFAULT_START;
		this.pagePerCount = DEFAULT_PER;
		this.currentPage = DEFAULT_CUR;
	}
	public PageOrderInfo(int startCount, int pagePerCount) {
		super();
		this.startCount = startCount;
		this.pagePerCount = pagePerCount;
	}
	public PageOrderInfo(String pageParam) {
		if(StringUtils.isEmpty(pageParam)){
			this.startCount = DEFAULT_START;
			this.pagePerCount = DEFAULT_PER;
			this.currentPage = DEFAULT_CUR;
		} else {
			String[] values = pageParam.split(",");
			this.startCount = values.length > 0 && StringUtils.isNotEmpty(values[0]) ? Integer.parseInt(values[0]) : DEFAULT_START;
			this.pagePerCount = values.length > 1 && StringUtils.isNotEmpty(values[1]) ? Integer.parseInt(values[1]) : DEFAULT_PER;
			this.currentPage = values.length > 2 && StringUtils.isNotEmpty(values[2]) ? Integer.parseInt(values[2]) : DEFAULT_CUR;
			this.orderCol = values.length > 3 && StringUtils.isNotEmpty(values[3]) ? values[3] : "";
			this.orderType = values.length > 4 && StringUtils.isNotEmpty(values[4]) ? values[4] : "";
		}
	}
	public IPage initPage(){
		IPage page = new Page();
		page.setSize(this.pagePerCount);
		page.setPages(this.startCount);
		page.setCurrent(this.currentPage);
		return page;
	}

	public int getStartCount() {
		return startCount;
	}
	public void setStartCount(int startCount) {
		this.startCount = startCount;
	}
	public int getPagePerCount() {
		return pagePerCount;
	}
	public void setPagePerCount(int pagePerCount) {
		this.pagePerCount = pagePerCount;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public String getOrderCol() {
		return orderCol;
	}
	public void setOrderCol(String orderCol) {
		this.orderCol = orderCol;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	
}
