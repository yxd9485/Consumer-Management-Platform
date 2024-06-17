package com.dbt.framework.datadic.bean;

import org.springframework.util.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
* 文件名: Datadic
* 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. 
* 描述: 字典数据bean
* 修改人: HaoQi
* 修改时间：2014-08-19 13:40:45
* 修改内容：新增
*/
public class SysDataDic extends BasicProperties {

	private static final long serialVersionUID = 517813512235753633L;

	private String dataDicKey;

	private String categoryKey;
	
	private String dataId;
	
	private String dataValue;
	
	private String dataAlias;
	
	private String dataExplain;
	
	private String categoryName;
	
	private String categoryCode;
	
	private String dicType;
	
	private int version;
	

	public SysDataDic() {
	}

	public SysDataDic(String params) {
		if(!StringUtils.isEmpty(params)){
			String[] values = params.split(",");
			this.categoryKey = values.length > 0 ? (values[0]): null;
			this.dataAlias = values.length > 1 ? values[1] : null;
			
		}
	}

	public String getDataDicKey() {
		return dataDicKey;
	}

	public void setDataDicKey(String dataDicKey) {
		this.dataDicKey = dataDicKey;
	}

	public String getCategoryKey() {
		return categoryKey;
	}

	public void setCategoryKey(String categoryKey) {
		this.categoryKey = categoryKey;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public String getDataAlias() {
		return dataAlias;
	}

	public void setDataAlias(String dataAlias) {
		this.dataAlias = dataAlias;
	}

	public String getDataExplain() {
		return dataExplain;
	}

	public void setDataExplain(String dataExplain) {
		this.dataExplain = dataExplain;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getDicType() {
		return dicType;
	}

	public void setDicType(String dicType) {
		this.dicType = dicType;
	}

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
	
}