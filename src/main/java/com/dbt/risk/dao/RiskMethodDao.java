package com.dbt.risk.dao;

import java.util.List;
import java.util.Map;

import com.dbt.risk.bean.MethodBean;
import com.dbt.risk.bean.MethodGlueBean;

public interface RiskMethodDao {
	public List<MethodBean> getMethodList(Map<String,Object> map);
	public int getMethodCount(Map<String,Object> map);
	public void addMethod(MethodBean bean);
	public void updateMethod(MethodBean bean);
	public void delMethod(String infoKey);
	public int checkName(Map<String,Object> map);
	/**
	 * 服务器部署相关
	 * @return
	 */
	public List<String> getLocalHostList();
	public void delLocalhost(String ip);
	/**
	 * method GLUE相关
	 * @param methodInfoKey
	 * @return
	 */
	public MethodGlueBean getMethodGlue(String methodInfoKey);
	public void addMethodGlue(MethodGlueBean bean);
	public void updateMethodGlue(Map<String,String> map);
	public void changeMethodGlueId(Map<String,String> map);
}
