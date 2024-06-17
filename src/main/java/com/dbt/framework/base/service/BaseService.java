package com.dbt.framework.base.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.dao.ICommonDao;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.log.service.VpsOperationLogService;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.StringUtil;
import com.dbt.framework.util.UUIDTools;

/**
 * 文件名：BaseService.java <br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
 * 描述: 基础service类 <br>
 * 修改人: 谷长鹏 <br>
 * 修改时间：2014-02-17 14:46:59<br>
 * 修改内容：新增 <br>
 */

public abstract class BaseService<T> {

	protected transient final Log log = LogFactory.getLog(getClass());
	
    @Autowired
    protected VpsOperationLogService logService;
    @Autowired
    private ICommonDao commonDao;

    /**
     * 生成UUID
     * @return
     */
	public String callUUID() {
		return UUIDTools.getInstance().getUUID();
	}
	
	/**
	 * 生成业务编号
	 * @param tableFlag		获取表名称标识
	 * @param bussionNoCol	编号列名 
	 * @param orderNoType	订单类型（业务类型）MY：码源订单，HD：扫码活动，DS：一码多扫，FB：逢百规则
	 * @return
	 */
	public String getBussionNo(String tableFlag, String bussionNoCol, String orderNoType){
		String bussionNo = "";
		String orderPrefix = DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_COMPANY_INFO, 
                DatadicKey.filterCompanyInfo.PROJECT_ORDERPREFIX);
		String bussionNoPrefix = orderPrefix + DateUtil.getCurrentYear() + orderNoType;
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", Constant.tableMap.get(tableFlag));
		map.put("bussionNoCol", bussionNoCol);
		map.put("bussionNoPrefix", bussionNoPrefix);
		int maxCount = commonDao.getCount(map) + 1;
		bussionNo = bussionNoPrefix + StringUtil.lPad(maxCount+"", "0", "mengniu".equals(DbContextHolder.getDBType()) ? 6 : 4);
		return bussionNo;
	}
	
	/**
	 * 检验业务名称是否重复
	 * @param tableFlag		获取表名称标识
	 * @param infoKeyCol	主键列名
	 * @param infoKey		主键值（修改页面需传递）
	 * @param nameCol		名称列名
	 * @param bussionName	名称值
	 * @return isExist		是否重复：0否，1是
	 */
	public String checkBussionName(String tableFlag, String infoKeyCol, String infoKey, String nameCol, String bussionName){
		String isExist = "0";
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", Constant.tableMap.get(tableFlag));
		map.put("infoKeyCol", infoKeyCol);
		map.put("infoKey", infoKey);
		map.put("nameCol", nameCol);
		map.put("bussionName", bussionName);
		if(commonDao.queryCountForName(map) > 0){
			isExist = "1";
		}
		return isExist;
	}
    
    protected String getContextUrl(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String protocol = request.getHeader("X-Forwarded-Scheme");
        if (StringUtils.isBlank(protocol)) {
            protocol = request.getScheme();
        }
        return protocol + url.substring(url.indexOf(":"), url.indexOf(request.getContextPath())) + request.getContextPath() + "/";
    }

}
