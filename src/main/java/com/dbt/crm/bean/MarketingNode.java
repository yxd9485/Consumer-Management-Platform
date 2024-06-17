package com.dbt.crm.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 营销节点Bean
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class MarketingNode extends BasicProperties {

	/** 运营画布计划唯一标识 **/
	private String id;
	/** 计划名称**/
	private String plan_name;
	/** 流程名称**/
	private String task_name;
	/** 节点类型，{1:触达节点 2:活动节点 3:自定义节点} **/
	private String type;
	/** 数据类型，类型为触达时{1:短信 2:公众号 3:企业微信 4:AI语音}，类型为活动时{1:扫点得 2:万能签到 ...} **/
	private String data_type;
	/** 数据值，类型为触达时为触达任务id，类型为活动时为活动名称，类型为自定义时为标题 **/
	private String data_value;
	/** 备注 **/
	private String remark;
	/** 开始时间 **/
	private String date_start;
	/** 结束时间 **/
	private String date_end;
	/** 节点状态，{0:待删除 1:无效节点 2:未开始 3:进行中 4:完成且成功 5:完成且失败 99:已删除} **/
	private String state;
	
	private int page_no;
	private int page_size;
	
    public MarketingNode() {
        super();
    }

    public MarketingNode(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.plan_name = paramAry.length > 0 ? paramAry[0] : "";
        this.task_name = paramAry.length > 1 ? paramAry[1] : "";
        this.data_value = paramAry.length > 2 ? paramAry[2] : "";
        this.state = paramAry.length > 3 ? paramAry[3] : "";
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public String getData_value() {
		return data_value;
	}

	public void setData_value(String data_value) {
		this.data_value = data_value;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDate_start() {
		return date_start;
	}

	public void setDate_start(String date_start) {
		this.date_start = date_start;
	}

	public String getDate_end() {
		return date_end;
	}

	public void setDate_end(String date_end) {
		this.date_end = date_end;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPlan_name() {
		return plan_name;
	}

	public void setPlan_name(String plan_name) {
		this.plan_name = plan_name;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public int getPage_no() {
		return page_no;
	}

	public void setPage_no(int page_no) {
		this.page_no = page_no;
	}

	public int getPage_size() {
		return page_size;
	}

	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}
}
