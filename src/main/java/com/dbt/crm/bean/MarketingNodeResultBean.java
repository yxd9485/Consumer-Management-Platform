package com.dbt.crm.bean;

import java.util.List;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 营销节点Bean
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class MarketingNodeResultBean extends BasicProperties {

	private int total;
	private List<MarketingNode> data;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<MarketingNode> getData() {
		return data;
	}
	public void setData(List<MarketingNode> data) {
		this.data = data;
	}
	
}
