package com.dbt.platform.activity.bean;

public class VcodeActivityCrmGroup {
	private int id;
    private String name;
    private String desc;
    private long number;
    /**数据有效期  -1永久**/
    private int valid;
    /**更新周期**/
    private int up_cycle;
    /**过期日期**/
    private String valid_date;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public long getNumber() {
		return number;
	}
	public void setNumber(long number) {
		this.number = number;
	}
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}
	public int getUp_cycle() {
		return up_cycle;
	}
	public void setUp_cycle(int up_cycle) {
		this.up_cycle = up_cycle;
	}
	public String getValid_date() {
		return valid_date;
	}
	public void setValid_date(String valid_date) {
		this.valid_date = valid_date;
	}
    
    
}
    
