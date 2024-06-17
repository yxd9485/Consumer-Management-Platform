package com.dbt.platform.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dbt.framework.base.action.reply.BaseResult;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.HttpReq;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.activate.bean.VpsVcodeBatchActivateCog;

@Service("batchActivateCogJob")
public class BatchActivateCogJob {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    private String BATCH_ACTIVATE_URL = "http://59.110.53.118:9008/DBTMainEntStats/";
    
    @SuppressWarnings("unchecked")
	public void updateBatchActivateCog() {
    	log.warn("激活批次用户刷新Job开始执行...");
    	// 获取job执行的省区
       	Set<String> nameList = ((Map<String, ServerInfo>) RedisApiUtil.getInstance()
				.getObject(false, CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO)).keySet();
    	
    	StringBuffer buffer = null;
    	Map<String, Object> params = new HashMap<>();
    	VpsVcodeBatchActivateCog queryBean = new VpsVcodeBatchActivateCog();
       	
       	// 循环执行任务
       	for (String projectServerName : nameList) {
       		buffer = new StringBuffer();
       		buffer.append("处理省区：").append(projectServerName).append(",");
       		
       		// 查询省区下的激活人员信息
       		queryBean.setServerName(projectServerName);
       		params.clear();
            params.put("queryBean", queryBean);
            params.put("pageOrderInfo", new PageOrderInfo(0, 100));
            JSONObject myDrinkingJson = HttpReq.handerHttpReq(BATCH_ACTIVATE_URL + "batchActivate/showActivateCogInfoList.do", JSON.toJSONString(params));
            if(null == myDrinkingJson) {
            	buffer.append("返回结果：查询异常；");
            	continue;	
            }

            BaseResult<Map<String, Object>> result = myDrinkingJson.getObject("result", BaseResult.class);
            if(null != result
            		&& Constant.ResultCode.SUCCESS.equals(result.getResult().getCode())
            		&& Constant.ResultCode.SUCCESS.equals(result.getResult().getBusinessCode())){
            	
            	List<VpsVcodeBatchActivateCog> userList = JSON.parseArray(JSON.toJSONString(result.getReply().get("resultList")), VpsVcodeBatchActivateCog.class);
            	if(CollectionUtils.isEmpty(userList)) {
            		buffer.append("返回结果：查询激活人员0；");
            		continue;
            	}
            	
            	buffer.append("返回结果：查询激活人员").append(userList.size()).append(";");
            	// 更新激活人员信息
            	for (VpsVcodeBatchActivateCog activateCog : userList) {
            		params.clear();
                    params.put("activateCog", activateCog);
                    myDrinkingJson = HttpReq.handerHttpReq(BATCH_ACTIVATE_URL + "batchActivate/doActivateCogEdit.do", JSON.toJSONString(params));
                    result = JSONObject.toJavaObject(myDrinkingJson, BaseResult.class);
                    if(null != result
                    		&& Constant.ResultCode.SUCCESS.equals(result.getResult().getCode())
                    		&& Constant.ResultCode.SUCCESS.equals(result.getResult().getBusinessCode())){
                    	buffer.append("处理用户：").append(activateCog.getPhoneNum()).append("成功;");
                    }else{
                    	buffer.append("处理用户：").append(activateCog.getPhoneNum()).append("失败;");
                    }
				}
            }else {
            	buffer.append("返回结果：查询异常2；");
            }
            log.error(buffer.toString());
    	}
       	log.warn("激活批次用户刷新Job执行结束...");
    }
    
    public static void main(String[] args) {
    	VpsVcodeBatchActivateCog item = new VpsVcodeBatchActivateCog();
    	List<VpsVcodeBatchActivateCog> resultList = new ArrayList<VpsVcodeBatchActivateCog>();
    	resultList.add(item);
    	Map<String, Object> resultMap = new HashMap<>();
    	resultMap.put("resultList", resultList);
    	
    	List<VpsVcodeBatchActivateCog> userList = JSON.parseArray(JSON.toJSONString(resultMap.get("resultList")), VpsVcodeBatchActivateCog.class);
    	System.out.println(JSON.toJSONString(userList));
	}

}
