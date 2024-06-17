/**
 * <pre>
 * Copyright Digital Bay Technology Group Co. Ltd.All Rights Reserved.
 *
 * Original Author: hanshimeng
 *
 * ChangeLog:
 * 下午3:27:23 by hanshimeng create
 * </pre>
 */
package com.dbt.platform.activity.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbt.platform.waitActivation.bean.WaitActivationRule;
import com.dbt.platform.waitActivation.service.WaitActivationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.zone.bean.SysAreaM;
import com.dbt.framework.zone.service.SysAreaService;
import com.dbt.platform.activity.bean.VcodeActivityHotAreaCog;
import com.dbt.platform.activity.bean.VcodeActivityHotAreaForAreaCode;
import com.dbt.platform.activity.bean.VcodeActivityRebateRuleCog;
import com.dbt.platform.activity.dao.IVcodeActivityHotAreaCogDao;

/**
 * 热区配置service
 */
@Service
public class VcodeActivityHotAreaCogService extends BaseService<VcodeActivityHotAreaCog>{

	@Autowired
	private IVcodeActivityHotAreaCogDao hotAreaCogDao;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private VcodeActivityRebateRuleCogService rebateRuleCogService;
    @Autowired
    private WaitActivationService waitActivationService;
    
    /**
     * 获取热区区域分组列表
     * @param queryBean 
     * @return
     * @throws Exception
     */
    public List<VcodeActivityHotAreaForAreaCode> queryAllHotArea(
    		VcodeActivityHotAreaForAreaCode queryBean) throws Exception {
        if(StringUtils.isNotBlank(queryBean.getCountyCode())){
        	queryBean.setAreaCode(queryBean.getCountyCode());
        }else if(StringUtils.isNotBlank(queryBean.getCityCode())){
        	queryBean.setAreaCode(queryBean.getCityCode().substring(0, 4));
        }else if(StringUtils.isNotBlank(queryBean.getProvinceCode())){
        	queryBean.setAreaCode(queryBean.getProvinceCode().substring(0, 2));
        }
    	
        // 获取所有有效热区
    	Map<String, Object> map = new HashMap<>();
    	map.put("queryBean", queryBean);
        List<VcodeActivityHotAreaCog> hotAreaCogLst = hotAreaCogDao.queryAll(map);
        
        // 按区域及类型分组
        String areaCode = "";
        VcodeActivityHotAreaForAreaCode hotAreaForAreaCode = new VcodeActivityHotAreaForAreaCode();
        List<VcodeActivityHotAreaForAreaCode> hotAreaForAreaCodeLst = new ArrayList<>();

        for (VcodeActivityHotAreaCog hotAreaItem : hotAreaCogLst) {
            
            // 规则按区域分组
            if (areaCode.equals(hotAreaItem.getAreaCode())) {
                hotAreaForAreaCode.getHotAreaCogList().add(hotAreaItem);
                
            } else {
                areaCode = hotAreaItem.getAreaCode();
                hotAreaForAreaCode = new VcodeActivityHotAreaForAreaCode(
                        hotAreaItem.getAreaCode(), getAreaNameByCode(hotAreaItem.getAreaCode()), hotAreaItem);
                hotAreaForAreaCodeLst.add(hotAreaForAreaCode);
            }
        }
        
        return hotAreaForAreaCodeLst;
    }

	/**
	 * 插入
	 * 
	 * @param hotAreaCog
	 */
	public void addRebateRuleCog(VcodeActivityHotAreaCog hotAreaCog) {
	    hotAreaCogDao.create(hotAreaCog);
	    logService.saveLog("vcodeActivityHotArea", Constant.OPERATION_LOG_TYPE.TYPE_1,
	                        JSON.toJSONString(hotAreaCog), "创建热区:" + hotAreaCog.getHotAreaName());
	}
    
    /**
     * 根据规则主键查询活动规则
     * 
     * @param hotAreaKey 活动规则主键
     */
    public VcodeActivityHotAreaCog findById(String hotAreaKey) {
        VcodeActivityHotAreaCog hotAreaCog = hotAreaCogDao.findById(hotAreaKey);
        hotAreaCog.setAreaName(getAreaNameByCode(hotAreaCog.getAreaCode()));
        return hotAreaCog;
    }
    
    /**
     * 更新
     * 
     * @param hotAreaCog
     */
    public void updateHotAreaCog(VcodeActivityHotAreaCog hotAreaCog) {
        
        // 更新活动规则
        hotAreaCogDao.updateHotAreaCog(hotAreaCog);
        
        // 更新缓存
        try {
            CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey
                    .VCODE_ACTIVITY_HOTAREA_COG + Constant.DBTSPLIT + hotAreaCog.getHotAreaKey());
        } catch (Exception e) {
            log.error(e);
        }
        logService.saveLog("vcodeActivityHotArea", Constant.OPERATION_LOG_TYPE.TYPE_2,
                            JSON.toJSONString(hotAreaCog), "修改热区:" + hotAreaCog.getHotAreaName());
    }
    
    /**
     * 根据主键删除热区配置
     * 
     * @param hotAreaKey 热区主键
     */
    public boolean deleteHotAreaCogById(String hotAreaKey, String userKey, Model model) {
        
        // 依据hotAreaKey查询所有相关的返利规则
        List<VcodeActivityRebateRuleCog> rebateRuleLst = 
                rebateRuleCogService.queryValidByHotAreaKey(hotAreaKey);
        if (rebateRuleLst != null && rebateRuleLst.size() > 0) {
            String typeName = "";
            String nowDate = DateUtil.getDate();
            String nowTime = DateUtil.getDate("HH:mm:ss");
            boolean inValidFlag = false;
            StringBuffer buffer = new StringBuffer();
            for (VcodeActivityRebateRuleCog item : rebateRuleLst) {
                inValidFlag = false;
                
                // 周几
                if (Constant.rebateRuleType.RULE_TYPE_4.equals(item.getRuleType())) {
                    inValidFlag = true;
                    typeName = "周几(" + item.getBeginDate() + "-" + item.getEndDate() + ")";
                    
                // 节假日、时间段
                } else if (Constant.rebateRuleType.RULE_TYPE_1.equals(item.getRuleType())
                                || Constant.rebateRuleType.RULE_TYPE_2.equals(item.getRuleType())){
                    if (!((item.getBeginDate().equals(item.getEndDate()) 
                            || nowDate.equals(item.getEndDate())) && nowTime.compareTo(item.getEndTime()) > 0)) {
                        inValidFlag = true;
                        typeName = Constant.rebateRuleType.RULE_TYPE_1.equals(item.getRuleType()) ? "节假日" : "时间段";
                        typeName = typeName + "(" + item.getBeginDate() + "-" 
                                + item.getEndDate() + " " + item.getBeginTime() + "-" + item.getEndTime() + ")";
                    }
                }
                if (inValidFlag) {
                    buffer.append("区域:").append(getAreaNameByCode(item.getAreaCode())).append(" ");
                    buffer.append("类型:").append(typeName).append("</br>");
                }
            }
            
            // 如果校验未通过
            if (buffer.length() > 0) {
                if (model != null) {
                    buffer.insert(0, "删除失败，以下规则正在使用本热区:</br>");
                    model.addAttribute("errorMsg", buffer.toString());
                    model.addAttribute("flag", "errMsg");
                }
                return false;
            }
        }

        //天降红包规则校验
        WaitActivationRule waitActivationRule = waitActivationService.findWaitActivationRule();
        if (hotAreaKey.equals(waitActivationRule.getHotAreaKey())
                && waitActivationRule.getStartDate().compareTo(DateUtil.getDate()) <= 0
                && waitActivationRule.getEndDate().compareTo(DateUtil.getDate()) >= 0) {
            model.addAttribute("errorMsg", "删除失败，天降红包正在使用本热区");
            model.addAttribute("flag", "errMsg");
            return false;
        }

        // 如果所有相关返利规则都已失败，则允许删除
        VcodeActivityHotAreaCog hotAreaCog = this.findById(hotAreaKey);
        hotAreaCog.setDeleteFlag(Constant.DbDelFlag.del);
        hotAreaCog.fillUpdateFields(userKey);
        this.updateHotAreaCog(hotAreaCog);
        logService.saveLog("vcodeActivityHotArea", Constant.OPERATION_LOG_TYPE.TYPE_3, JSON.toJSONString(hotAreaCog), "删除热区");
        return true;
    }
    
	/**
	 * 根据区域主键删除热区配置
	 * 
	 * @param areaCode 区域主键
	 */
	public void deleteHotAreaByAreaCode(String areaCode, String userKey, Model model) {
	    
	    // 依据areaCode获取其下的热区配置
	    List<VcodeActivityHotAreaCog> hotAreaCogLst = hotAreaCogDao.queryByAreaCode(areaCode);
	    
	    List<String> deleteKeyLst = new ArrayList<>();
	    StringBuffer buffer = new StringBuffer();
	    for (VcodeActivityHotAreaCog item : hotAreaCogLst) {
            // 组建不能删除的热区提示信息
            if (!deleteHotAreaCogById(item.getHotAreaKey(), userKey, null)) {
                buffer.append(item.getHotAreaName()).append("</br>");
            } else {
                deleteKeyLst.add(item.getHotAreaKey());
            }
            
        }
	    
	    if (buffer.length() > 0) {
            buffer.insert(0, "删除失败，以下热区正在被有效规则使用:</br>");
	        model.addAttribute("errorMsg", buffer.toString());
            model.addAttribute("flag", "errMsg");
	    } else {
	        model.addAttribute("flag", "is_delete");
	    }

	    Map<String, Object> map = new HashMap<>();
	    map.put("areaCode", areaCode);
	    map.put("delKeys", deleteKeyLst);
        logService.saveLog("vcodeActivityHotArea", Constant.OPERATION_LOG_TYPE.TYPE_3, JSON.toJSONString(map), "根据区域删除热区");
	}
    
    /**
     * 获取项目对应的省
     * @param activityCog
     * @return
     */
    public List<SysAreaM> queryAreaList() {

        // 获取项目地区配置
        List<String> areaCodeList = new ArrayList<>();
        List<SysAreaM> areaLst = new ArrayList<>();
        String areaCode = PropertiesUtil.getPropertyValue("project_area");
        if (!StringUtils.isEmpty(areaCode)) {
            String[] areaCodeAry = areaCode.split(",");
            areaCodeList = new ArrayList<>();
            for (String item : areaCodeAry) {
                areaCodeList.add(item + "0000");
            }
            
            // 返回配置的省级区域
            areaLst = sysAreaService.queryByAreaCode(areaCodeList);
            
        } else {
            // 返回所有省级区域
            areaLst = sysAreaService.findByParentId("0");
            areaLst.remove(0);
        }
        
        return areaLst;
    }
	
	/**
	 * 依据区域code获取本身及上级名称
	 * @param areaCode
	 * @return
	 */
	public String getAreaNameByCode(String areaCode) {
	    if (StringUtils.isEmpty(areaCode)) return "";
	    
	    String areaName = "";
        // 全部
        if ("000000".equals(areaCode)) {
            areaName = "全部";
            
        } else if ("000001".equals(areaCode)) {
            areaName = "省外";
            
        } else {
            SysAreaM areaM = sysAreaService.findById(areaCode);
            String finalName = areaM == null ? areaCode : areaM.getAreaName();
            
            // 最后2位不为00，则为县
            if (!"00".equals(areaCode.substring(4, 6))) {
                areaM = sysAreaService.findById(areaCode.substring(0, 4) + "00");
                areaName = areaM.getAreaName() + " - " + finalName;
                areaM = sysAreaService.findById(areaCode.substring(0, 2) + "0000");
                areaName = areaM.getAreaName() + " - " + areaName;
                
            // 市
            } else if (!"00".equals(areaCode.substring(2, 4))) {
                areaM = sysAreaService.findById(areaCode.substring(0, 2) + "0000");
                areaName = areaM.getAreaName() + " - " + finalName;
                
            // 省
            } else {
                areaName = finalName;
            }
        }
        return areaName;
	}
	
	/**
	 * 获取定位位图中心城市
	 * 
	 * @param areaName 区域名称
	 * @return Key:firstCity, secondCity
	 */
	public Map<String, String> getFinalCityName(String areaName) {
	    Map<String, String> localMap = new HashMap<String, String>();
	    
	    if (StringUtils.isNotBlank(areaName)) {
	        String[] tempAry = areaName.split("-");
	        if (tempAry.length > 1) {
                localMap.put("firstCity", tempAry[tempAry.length - 2].trim());
                localMap.put("secondCity", tempAry[tempAry.length - 1].trim());
	        } else {
	            localMap.put("firstCity", tempAry[0].trim());
	        }
	    } else {
	        localMap.put("firstCity", queryAreaList().get(0).getAreaName());
	    }
	    return localMap;
	}

	/**
	 * 根据地区areaCode获取热区List
	 * @param areaCode 地区code</br> 
	 * @return Object </br>
	 */
	public List<VcodeActivityHotAreaCog> findHotAreaListByAreaCode(String areaCode) {

        // if (StringUtils.isBlank(areaCode)) return new ArrayList<VcodeActivityHotAreaCog>();

        Map<String, Object> map = new HashMap<>();
	    Set<String> areaCodeSet = new HashSet<>();
	    if (StringUtils.isNotBlank(areaCode)) {
	        // 省外
	        if (areaCode.contains("000001")) {
	            String projectArea = DatadicUtil.getDataDicValue(DatadicKey
	                    .dataDicCategory.FILTER_COMPANY_INFO, DatadicKey.filterCompanyInfo.PROJECT_AREA);
	            if (StringUtils.isNotBlank(projectArea)) {
	                map.put("provinceOuterFlag", "1");
	                areaCodeSet.addAll(Arrays.asList(projectArea.split(",")));
	            }
	        // 非全部
	        } else if (!areaCode.contains("000000")){
	            String[] areaCodeAry = areaCode.split(",");
	            for (String item : areaCodeAry) {
	                if (StringUtils.isBlank(item)) continue; 
	                if("0000".equals(item.substring(2))){
	                    item = item.substring(0,2);
	                }else if("00".equals(item.substring(4))){
	                    item = item.substring(0,4);
	                }
	                areaCodeSet.add(item);
	            }
	        }
	        
	    }
        map.put("areaCodeLst", new ArrayList<>(areaCodeSet));
        return hotAreaCogDao.findHotAreaListByAreaCode(map);
	}
	
	/**
	 * 检验名称是否重复
	 * @param infoKey		主键（修改页面需传递）
	 * @param bussionName	名称
	 * @return
	 */
	public String checkBussionName(String infoKey, String bussionName) {
		return checkBussionName("vcodeActivityHotArea", "hotarea_key", infoKey, "hotarea_name", bussionName);
	}
}
