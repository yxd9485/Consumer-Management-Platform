package com.dbt.crm;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dbt.crm.bean.MarketingNode;
import com.dbt.crm.bean.MarketingNodeResultBean;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.EncryptUtil;
import com.dbt.framework.util.HttpReq;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.activity.bean.VcodeActivityCrmGroup;

/**
 * 活动规则模板主表Service
 */
@Service
public class CRMServiceServiceImpl {
	Logger log = Logger.getLogger(this.getClass());
	
	/** 获取用户群组秘钥 **/
	private static final String S4RNWPS4SK = "S4RnWpS4sk";
	/** 获取营销节点秘钥 **/
	private static final String BBRCJVCPGX = "bBRCjvCpGX";
    
	/**
	 * 获取群组下拉框
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public List<VcodeActivityCrmGroup> queryVcodeActivityCrmGroup() throws NoSuchAlgorithmException {
		String startValidDate = DateUtil.getDateTime(DateUtil.addDays(0), "yyyy-MM-dd");		
		ServerInfo serverInfo = ((Map<String, ServerInfo>) RedisApiUtil.getInstance().getObject(false,
				CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO)).get(DbContextHolder.getDBType());		
		String token = EncryptUtil.encrypt(S4RNWPS4SK + DateUtil.getDateTime(DateUtil.addDays(0), "yyyy-MM-dd"));
		String itemValue = serverInfo.getItemValue();
//		if (PropertiesUtil.getPropertyValue("run_env").equals("TEST")) {
//			itemValue = "TEST";
//		}
		String interfaceUrl = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_HTTP_URL,
				DatadicKey.filterHttpUrl.CRM_INTERFACE_URL) + "crm/inside/group/@simple?start_valid_date="
				+ startValidDate  +"&province=" + itemValue + "&token="+token; 
		JSONObject revokeJson = HttpReq.handerHttpReq(interfaceUrl, null);
		if (revokeJson == null || !revokeJson.containsKey("result")) {
			return null;
		}
		List<VcodeActivityCrmGroup> result = JSON.parseArray(StringUtils
		        .defaultIfBlank(revokeJson.getString("result"), "[]"), VcodeActivityCrmGroup.class);
		return result;
	}

	/**
	 * 根据id查询详情
	 * @param groupId
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public VcodeActivityCrmGroup findGroupInfo(String groupId) throws NoSuchAlgorithmException {
		String interfaceUrl = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_HTTP_URL,
				DatadicKey.filterHttpUrl.CRM_INTERFACE_URL) + "crm/inside/group/ele/" + groupId + "?token="
				+ EncryptUtil.encrypt(S4RNWPS4SK + groupId);
		JSONObject revokeJson = HttpReq.handerHttpReq(interfaceUrl, null);
		if (revokeJson == null || !revokeJson.containsKey("result")) {
			throw new BusinessException("暂未找到对应群组,请核对群组是否存在!");
		}
		VcodeActivityCrmGroup activityCrmGroup = JSON.parseObject(revokeJson.getString("result"), VcodeActivityCrmGroup.class);
		return activityCrmGroup;
	}
	
	/**
	 * 获取营销列表
	 * @param pageInfo 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public MarketingNodeResultBean queryMarketingNode(MarketingNode queryBean, PageOrderInfo pageInfo) throws NoSuchAlgorithmException {
		ServerInfo serverInfo = ((Map<String, ServerInfo>) RedisApiUtil.getInstance().getObject(false,
				CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO)).get(DbContextHolder.getDBType());
		
		// 测试数据url
//		String interfaceUrl = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_HTTP_URL,
//				DatadicKey.filterHttpUrl.CRM_INTERFACE_URL) + "crm/inside/marketing/canvas/node?token=C47A5D7D475F7E13B3C679BEDB006F9D&item_value=D";
		
		String token = EncryptUtil.encrypt(BBRCJVCPGX + serverInfo.getItemValue());
		// 正式数据url
		String interfaceUrl = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_HTTP_URL,
				DatadicKey.filterHttpUrl.CRM_INTERFACE_URL) + "crm/inside/marketing/canvas/node?token=" + token + "&item_value=" + serverInfo.getItemValue();
		
		// 初始参数
		if(StringUtils.isBlank(queryBean.getPlan_name())) {
			queryBean.setPlan_name(null);
		}
		if(StringUtils.isBlank(queryBean.getTask_name())) {
			queryBean.setTask_name(null);
		}
		if(StringUtils.isBlank(queryBean.getData_value())) {
			queryBean.setData_value(null);
		}
		queryBean.setDeleteFlag(null);
		queryBean.setPage_no(pageInfo.getCurrentPage());
		queryBean.setPage_size(pageInfo.getPagePerCount());
		
		JSONObject revokeJson = HttpReq.handerHttpReq(interfaceUrl,JSON.toJSONString(queryBean));
		log.error(interfaceUrl);
		log.error(JSON.toJSONString(queryBean));
		log.error(revokeJson);
		if (revokeJson == null || !revokeJson.containsKey("result")) {
			return null;
		}
		MarketingNodeResultBean result = JSON.parseObject(revokeJson.getString("result"), MarketingNodeResultBean.class);
		return result;
	}
	
	/**
	 * 更新状态
	 * @param nodeId 节点ID
	 * @param state 状态
	 * @throws NoSuchAlgorithmException 
	 */
	@SuppressWarnings("unchecked")
	public String doMarketingNodeEdit(String nodeId, String state) throws NoSuchAlgorithmException {
		String message = "失败";
		
		ServerInfo serverInfo = ((Map<String, ServerInfo>) RedisApiUtil.getInstance().getObject(false,
			CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO)).get(DbContextHolder.getDBType());
	
		// 测试数据url
//		String interfaceUrl = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_HTTP_URL,
//			DatadicKey.filterHttpUrl.CRM_INTERFACE_URL) + "crm/inside/marketing/canvas/node/"+nodeId+"?"
//					+ "token="+EncryptUtil.encrypt(BBRCJVCPGX +  state + "D")+"&item_value=D&state="+state;
	
		String token = EncryptUtil.encrypt(BBRCJVCPGX + state + serverInfo.getItemValue());
		
		// 正式数据url
		String interfaceUrl = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_HTTP_URL,
			DatadicKey.filterHttpUrl.CRM_INTERFACE_URL) + "crm/inside/marketing/canvas/node/"+nodeId+"?token=" + token + "&item_value=" + serverInfo.getItemValue() + "&state="+state;;
		
		JSONObject revokeJson = HttpReq.handerHttpReq(interfaceUrl, null);
		log.error(interfaceUrl);
		log.error(revokeJson);
		if (revokeJson.containsKey("code")) {
			message = revokeJson.getString("message");
		}
		return message;
	}
}
