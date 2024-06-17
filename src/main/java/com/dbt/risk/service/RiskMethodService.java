package com.dbt.risk.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSONObject;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.HttpReq;
import com.dbt.framework.util.UUIDTools;
import com.dbt.risk.bean.MethodBean;
import com.dbt.risk.bean.MethodGlueBean;
import com.dbt.risk.dao.RiskMethodDao;

@Service
public class RiskMethodService {
	@Autowired
	private RiskMethodDao methodDao;
	/**
	 * 查询接口列表
	 * @param info
	 * @param bean
	 * @return
	 */
	public List<MethodBean> getMethodList(PageOrderInfo info,MethodBean bean){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("param", info);
		map.put("bean", bean);
		return methodDao.getMethodList(map);
	}
	/**
	 * 接口数量
	 * @param info
	 * @param bean
	 * @return
	 */
	public int getMethodCount(PageOrderInfo info,MethodBean bean) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("param", info);
		map.put("bean", bean);
		return methodDao.getMethodCount(map);
	}
	/**
	 * 接口详情
	 * @param info
	 * @param bean
	 * @param model
	 */
	public void getMethodDetail(PageOrderInfo info,MethodBean bean,Model model){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("param", info);
		map.put("bean", bean);
		List<MethodBean> list=methodDao.getMethodList(map);
		if(list!=null&&list.size()>0) {
			model.addAttribute("bean", list.get(0));
		}
	}
	/**
	 * 新增接口
	 * @param bean
	 */
	public void addMethod(MethodBean bean) {
		methodDao.addMethod(bean);
	}
	/**
	 * 更新接口
	 * @param bean
	 */
	public void updateMethod(MethodBean bean) {
		methodDao.updateMethod(bean);
	}
	/**
	 * 删除接口
	 * @param infoKey
	 */
	public void delMethod(String infoKey) {
		methodDao.delMethod(infoKey);
	}
	/**
	 * 校验接口名称
	 * @param methodName
	 * @param infoKey
	 * @return
	 */
	public int checkName(String methodName,String infoKey) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("methodName", methodName);
		map.put("infoKey", infoKey);
		return methodDao.checkName(map);
	}
	/**
	 * 服务器分发
	 * @return
	 */
	public String syncData() {
		List<String> ipList=methodDao.getLocalHostList();
		String failMsg="";
		int successCount=0;
		for (String localhost : ipList) {
			try {
				String url=localhost+"web/riskData/initCache";
				System.out.println(url);
				JSONObject json=HttpReq.httpGet(url);
//				JSONObject json=HttpsUtil.doPostForJson(url, null);
				if(json!=null&&json.containsKey("result")) {
					successCount++;
				}else {
					failMsg+=","+url;
					methodDao.delLocalhost(localhost);
				}
			} catch (Exception e) {
				failMsg+=","+localhost;
				methodDao.delLocalhost(localhost);
			}
		}
		if(StringUtils.isNotEmpty(failMsg)) {
			failMsg=failMsg.substring(1);
			failMsg+=" 部署失败";
		}
		String result="部署成功"+successCount+"台 "+failMsg;
		return result;
	}
	public MethodGlueBean getMethodGlue(String methodInfoKey) {
		return methodDao.getMethodGlue(methodInfoKey);
	}
	/**
	 * 新增接口
	 * @param bean
	 */
	public void addMethodGlue(MethodGlueBean bean) {
		String id=UUIDTools.getInstance().getUUID();
		bean.setGlueId(id);
		methodDao.addMethodGlue(bean);
		String methodInfoKey=bean.getMethodInfoKey();
		Map<String,String> map=new HashMap<String,String>();
		map.put("infoKey", methodInfoKey);
		map.put("glueId", id);
		methodDao.updateMethodGlue(map);
		methodDao.changeMethodGlueId(map);
	}
}
