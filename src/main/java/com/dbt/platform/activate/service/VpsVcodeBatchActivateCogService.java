package com.dbt.platform.activate.service;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.reply.BaseResult;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ExcelUtil;
import com.dbt.framework.util.HttpReq;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.util.SendSMSUtil;
import com.dbt.platform.activate.bean.VpsVcodeBatchActivateCog;

/**
 * 一万一批次激活人员授权配置表Service
 */
@Service
public class VpsVcodeBatchActivateCogService extends BaseService<VpsVcodeBatchActivateCogService> {

    /**
     * 查询激活人员列表
     * @param queryBean </br>
     * @param pageOrderInfo </br>
     * @return Map<String,Object> </br>
     */
	@SuppressWarnings("unchecked")
	public Model queryForList(VpsVcodeBatchActivateCog queryBean, PageOrderInfo pageOrderInfo, Model model) throws Exception{
		String esUrl = DatadicUtil.getDataDicValue(
				DatadicKey.dataDicCategory.FILTER_HTTP_URL,
				DatadicKey.filterHttpUrl.DBTMAINENTSTATS_URL) + "batchActivate/showActivateCogInfoList.do";
		queryBean.setServerName(DbContextHolder.getDBType());

        Map<String, Object> params = new HashMap<>();
        params.put("queryBean", queryBean);
        params.put("pageOrderInfo", pageOrderInfo);
        JSONObject myDrinkingJson = HttpReq.handerHttpReq(esUrl, JSON.toJSONString(params));

        if(null == myDrinkingJson){
        	model.addAttribute("errMsg", "远程服务器无法连接");
        	return model;
        }

        BaseResult<Map<String, Object>> result = myDrinkingJson.getObject("result", BaseResult.class);
        if(null != result
        		&& Constant.ResultCode.SUCCESS.equals(result.getResult().getCode())
        		&& Constant.ResultCode.SUCCESS.equals(result.getResult().getBusinessCode())){
        	model.addAttribute("showCount", result.getReply().get("showCount"));
        	model.addAttribute("resultList", result.getReply().get("resultList"));
        	model.addAttribute("userStatusGro", result.getReply().get("userStatusGro"));
        }

        // 获取该企业下的工厂（使用原生jedis查询，因为key的结尾没有拼serverName）
        String factoryKey = CacheKey.cacheKey.company.KEY_COMPANY_FACTORY_INFO;
		String factoryNames = (String) RedisApiUtil.getInstance().eval("return redis.call('hget', KEYS[1], ARGV[1])", 1, factoryKey, DbContextHolder.getDBType());
        model.addAttribute("factoryNameList", Arrays.asList(StringUtils.defaultIfBlank(factoryNames,"").split(",")));
        model.addAttribute("orderCol", pageOrderInfo.getOrderCol());
        model.addAttribute("orderType", pageOrderInfo.getOrderType());
        /*model.addAttribute("queryParam", queryParam);
        model.addAttribute("pageParam", pageParam);*/
		return model;
	}


	/**
	 * 根据主键获取激活人员信息
	 * @param </br>
	 * @return Model </br>
	 */
	@SuppressWarnings("unchecked")
	public VpsVcodeBatchActivateCog findById(String infoKey, Model model){
		VpsVcodeBatchActivateCog activateCog = null;
		String esUrl = DatadicUtil.getDataDicValue(
				DatadicKey.dataDicCategory.FILTER_HTTP_URL,
				DatadicKey.filterHttpUrl.DBTMAINENTSTATS_URL) + "batchActivate/findById.do";
        Map<String, Object> params = new HashMap<>();
        params.put("infoKey", infoKey);
        JSONObject myDrinkingJson = HttpReq.handerHttpReq(esUrl, JSON.toJSONString(params));
        BaseResult<VpsVcodeBatchActivateCog> result = myDrinkingJson.getObject("result", BaseResult.class);
        if(null != result
        		&& Constant.ResultCode.SUCCESS.equals(result.getResult().getCode())
        		&& Constant.ResultCode.SUCCESS.equals(result.getResult().getBusinessCode())){
        	activateCog = JSON.parseObject(JSON.toJSONString(result.getReply()), VpsVcodeBatchActivateCog.class);
        }
		return activateCog;
	}

	/**
     * 更新激活人员
     *
     * @param activateCog
     * @param model
     */
    @SuppressWarnings("unchecked")
	public Model updateBatchActivateCog(VpsVcodeBatchActivateCog activateCog,
			String isCheck, String oldUserPrivilege, String oldUserStatus, Model model) throws Exception{
		String esUrl = DatadicUtil.getDataDicValue(
				DatadicKey.dataDicCategory.FILTER_HTTP_URL,
				DatadicKey.filterHttpUrl.DBTMAINENTSTATS_URL)  + "batchActivate/doActivateCogEdit.do";
        Map<String, Object> params = new HashMap<>();
        params.put("activateCog", activateCog);
        JSONObject myDrinkingJson = HttpReq.handerHttpReq(esUrl, JSON.toJSONString(params));
        BaseResult<VpsVcodeBatchActivateCog> result = JSONObject.toJavaObject(myDrinkingJson, BaseResult.class);
        String logMsg = "";
        if(null != result
        		&& Constant.ResultCode.SUCCESS.equals(result.getResult().getCode())
        		&& Constant.ResultCode.SUCCESS.equals(result.getResult().getBusinessCode())){
            logMsg = "审核成功";
        	// 状态及权限变动发短息通知激活人员
        	if(StringUtils.isNotBlank(activateCog.getPhoneNum())){
            	checkSendMsg(isCheck, oldUserPrivilege, oldUserStatus, activateCog, model);
        	}
        }else{
    		if("1".equals(isCheck)){
    		    logMsg = "审核失败";
        	}else{
                logMsg = "更新失败";
        	}
    		model.addAttribute("errMsg", logMsg);
        }
        logService.saveLog("batchActivate", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(activateCog), logMsg);
		return model;
    }


    /**
	 * 激活人员驳回
	 * @param </br>
	 * @return Model </br>
	 */
	@SuppressWarnings("unchecked")
	public Model rejectBatchActivateCog(VpsVcodeBatchActivateCog activateCog, Model model) {
		String esUrl = DatadicUtil.getDataDicValue(
				DatadicKey.dataDicCategory.FILTER_HTTP_URL,
				DatadicKey.filterHttpUrl.DBTMAINENTSTATS_URL) + "batchActivate/doActivateCogEdit.do";
        Map<String, Object> params = new HashMap<>();
        params.put("activateCog", activateCog);
        JSONObject myDrinkingJson = HttpReq.handerHttpReq(esUrl, JSON.toJSONString(params));
        BaseResult<VpsVcodeBatchActivateCog> result = JSONObject.toJavaObject(myDrinkingJson, BaseResult.class);
        String logMsg = "";
        if(null != result
        		&& Constant.ResultCode.SUCCESS.equals(result.getResult().getCode())
        		&& Constant.ResultCode.SUCCESS.equals(result.getResult().getBusinessCode())){
        	logMsg = "驳回成功";
        	// 状态及权限变动发短息通知激活人员
        	if(StringUtils.isNotBlank(activateCog.getPhoneNum())){
            	checkSendMsg(null, null, null, activateCog, model);
        	}
        }else{
            logMsg = "驳回失败";
        	model.addAttribute("errMsg", "驳回失败");
        }
        logService.saveLog("batchActivate", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(activateCog), logMsg);
		return model;

	}


    /**
     * 处理短信信息
     * @param </br>
     * @return StringBuilder </br>
     */
	private void checkSendMsg(String isCheck, String oldUserPrivilege,
			String oldUserStatus, VpsVcodeBatchActivateCog activateCog, Model model) {
		StringBuilder sb = new StringBuilder();

		if(Constant.ACTIVATE_USER_STATUS.USER_STATUS_3.equals(activateCog.getUserStatus())){
			sb.append("您的账户申请被驳回，请确认申请信息是否正确并重新提交注册申请。");
			model.addAttribute("errMsg", "驳回成功");
		}else{
			// 审核时修改了权限需要发信息提醒
	    	if("1".equals(isCheck) && !oldUserPrivilege.equals(activateCog.getUserPrivilege())){
	    		sb.append("您的账户已审核通过，账户权限：");
	    		if(Constant.ACTIVATE_USER_PRIVILEGE.USER_PRIVILEGE_1.equals(activateCog.getUserPrivilege())){
	    			sb.append("激活、查询检测。");
	    		}else if(Constant.ACTIVATE_USER_PRIVILEGE.USER_PRIVILEGE_2.equals(activateCog.getUserPrivilege())){
	    			sb.append("查询检测。");
	    		}
	    		model.addAttribute("errMsg", "审核成功");
	    	}

	    	// 更新时修改了权限或状态需要发信息提醒
	    	else if("0".equals(isCheck) &&
	    		(!oldUserPrivilege.equals(activateCog.getUserPrivilege())
	    			|| !oldUserStatus.equals(activateCog.getUserStatus()))){
	    		if(Constant.ACTIVATE_USER_STATUS.USER_STATUS_0.equals(activateCog.getUserStatus())){
	    			sb.append("您的账户已被停用，如有疑问，请联系相关人员。");
	    		}else if(Constant.ACTIVATE_USER_STATUS.USER_STATUS_2.equals(activateCog.getUserStatus())){
	    			sb.append("您的账户已被启用，");
	    			if(!oldUserPrivilege.equals(activateCog.getUserPrivilege())){
	    				if(Constant.ACTIVATE_USER_PRIVILEGE.USER_PRIVILEGE_1.equals(activateCog.getUserPrivilege())){
	            			sb.append("账户权限：激活、查询检测。");
	            		}else if(Constant.ACTIVATE_USER_PRIVILEGE.USER_PRIVILEGE_2.equals(activateCog.getUserPrivilege())){
	            			sb.append("账户权限：查询检测。");
	            		}
	    			}else{
	    				sb.append("如有疑问，请联系相关人员。");
	    			}

	    		}
	    		model.addAttribute("errMsg", "更新成功");
	    	}
		}

    	// 发短信提醒
    	if(null != sb && sb.length() > 0){
    		sb.append("[箱码激活管理]");
    		sb.append("【V积分】");
    		SendSMSUtil.sendSms(sb.toString(), activateCog.getPhoneNum());
    		log.error(activateCog.getPhoneNum() + "发送短信内容" + sb.toString());
    	}
	}


	public void exportActivateCogList(VpsVcodeBatchActivateCog queryBean, HttpServletResponse response) throws Exception {
		response.reset();
        response.setCharacterEncoding("GBK");
        response.setContentType("application/msexcel;charset=UTF-8");
        OutputStream outStream = response.getOutputStream();
        List<VpsVcodeBatchActivateCog> resultActivateCog  = null;
        String bookName = "";
        String[] headers = null;
        String[] valueTags = null;
        bookName = "激活人员导出";
		String esUrl = DatadicUtil.getDataDicValue(
				DatadicKey.dataDicCategory.FILTER_HTTP_URL,
				DatadicKey.filterHttpUrl.DBTMAINENTSTATS_URL) + "batchActivate/showActivateCogInfoList.do";
		queryBean.setServerName(DbContextHolder.getDBType());

        Map<String, Object> params = new HashMap<>();
        params.put("queryBean", queryBean);
        params.put("pageOrderInfo", new PageOrderInfo(0,100));
        JSONObject myDrinkingJson = HttpReq.handerHttpReq(esUrl, JSON.toJSONString(params));

        if(null == myDrinkingJson){
        	throw new BusinessException("远程服务器无法连接！");
        }

		BaseResult<Map<String, Object>> result = myDrinkingJson.getObject("result", BaseResult.class);
		if (null != result && Constant.ResultCode.SUCCESS.equals(result.getResult().getCode())
				&& Constant.ResultCode.SUCCESS.equals(result.getResult().getBusinessCode())) {
			resultActivateCog = JSON.parseArray(JSON.toJSONString(result.getReply().get("resultList")), VpsVcodeBatchActivateCog.class);
		}else {
			throw new BusinessException("远程服务器无法连接！");
		}
		if(!CollectionUtils.isEmpty(resultActivateCog)){
			for (VpsVcodeBatchActivateCog cog : resultActivateCog) {
				if("0".equals(cog.getUserStatus())){
					cog.setUserStatus("停用");
				}else if("1".equals(cog.getUserStatus())){
					cog.setUserStatus("待审核");
				}else if("2".equals(cog.getUserStatus())){
					cog.setUserStatus("正常");
				}else if("3".equals(cog.getUserStatus())){
					cog.setUserStatus("已驳回");
				}
				if("0".equals(cog.getUserPrivilege())){
					cog.setUserPrivilege("无权限");
				}else if("1".equals(cog.getUserPrivilege())){
					cog.setUserPrivilege("激活、查询检测");
				}else if("2".equals(cog.getUserPrivilege())){
					cog.setUserPrivilege("查询检测");
				}
			}
		}
		
		headers = new String[] { "openid", "激活人员", "手机号", "所属工厂", "操作权限", "用户状态","更新时间"};
		valueTags = new String[] { "openid", "userName", "phoneNum", "factoryName", "userPrivilege", "userStatus","createTime" };

        response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode(bookName, "UTF-8") + DateUtil.getDate() + ".xls");
        ExcelUtil<VpsVcodeBatchActivateCog> excel = new ExcelUtil<VpsVcodeBatchActivateCog>(); 
        excel.writeExcel(bookName, headers, valueTags, resultActivateCog, DateUtil.DEFAULT_DATETIME_FORMAT, outStream);
        outStream.close();
        response.flushBuffer();
	}
}

