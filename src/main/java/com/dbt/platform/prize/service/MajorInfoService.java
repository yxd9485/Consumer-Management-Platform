package com.dbt.platform.prize.service;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.bean.SysDataDic;
import com.dbt.framework.datadic.service.SysDataDicService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ExcelUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.activity.bean.VcodeActivityRebateRuleCog;
import com.dbt.platform.activity.bean.VcodeActivityVpointsCog;
import com.dbt.platform.activity.service.VcodeActivityRebateRuleCogService;
import com.dbt.platform.activity.service.VcodeActivityVpointsCogService;
import com.dbt.platform.prize.bean.MajorInfo;
import com.dbt.platform.prize.dao.IMajorInfoDao;
import com.dbt.platform.system.bean.SysUserBasis;
/**
 * @author hanshimeng
 * @createTime 2016年4月21日 下午4:26:50
 * @description 一等奖中奖名单Service
 */

@Service
public class MajorInfoService extends BaseService<MajorInfo>{

    @Autowired
    private IMajorInfoDao iMajorInfoDao;
    @Autowired
    private VcodeActivityVpointsCogService vpointsCogService;
    @Autowired
    private SysDataDicService sysDataDicService;
    @Autowired
    private VcodeActivityRebateRuleCogService rebateRuleCogService;
    
    /**
     * 分页查询中奖名单
     * 
     * @param params
     * @return
     */
    public List<MajorInfo> queryMajorInfoListForPage(MajorInfo queryBean, PageOrderInfo pageInfo, SysUserBasis optUser) throws Exception {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("queryBean", queryBean);
        args.put("pageInfo", pageInfo);
        args.put("projectServerName", DbContextHolder.getDBType());
        
        List<MajorInfo> majorLst = iMajorInfoDao.queryMajorInfoListForPage(args);
        
        String projectFlag = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_COMPANY_INFO, DatadicKey.filterCompanyInfo.PROJECT_FLAG);
        
        // 奖项类型
        Map<String, String> prizeTypeMap = vpointsCogService.queryAllPrizeType(false, true, false, false, false, false, null);
        String prizeTypeSuffix = "";
        String currDateTime = DateUtil.getDateTime();
        for (MajorInfo item : majorLst) {
            if (StringUtils.isNotBlank(item.getGrandPrizeType())) {
                if(prizeTypeMap.containsKey(item.getGrandPrizeType())){
                    prizeTypeSuffix = "-随机";
                } else if (Pattern.matches("0|1|2", item.getGrandPrizeType()) 
                        || prizeTypeMap.containsKey(item.getGrandPrizeType().toLowerCase())){
                    prizeTypeSuffix = "-定投";
                }
                if ("0".equals(item.getGrandPrizeType())) {
                    item.setGrandPrizeType(StringUtils.defaultIfBlank(prizeTypeMap.get("5"), "0"));
                } else if ("1".equals(item.getGrandPrizeType())) {
                    item.setGrandPrizeType(StringUtils.defaultIfBlank(prizeTypeMap.get("6"), "1"));
                } else if ("2".equals(item.getGrandPrizeType())) {
                    item.setGrandPrizeType(StringUtils.defaultIfBlank(prizeTypeMap.get("7"), "2"));
                } else if (StringUtils.isNotBlank(item.getGrandPrizeType())) {
                    item.setGrandPrizeType(StringUtils.defaultIfBlank(
                            prizeTypeMap.get(item.getGrandPrizeType().toUpperCase()), item.getGrandPrizeType()));
                }
                item.setGrandPrizeType(item.getGrandPrizeType() + prizeTypeSuffix);
            }
            
            // 平台核销的大奖需要设置核销人员和核销手机号字段
            if(Constant.CHECK_STATUS.status_1.equals(item.getCheckStatus()) && StringUtils.isBlank(item.getCheckOpenid()) 
            		&& StringUtils.isNotBlank(item.getCheckRemarks()) && item.getCheckRemarks().indexOf("-") > 0){
            	item.setCheckPhoneNum(item.getCheckRemarks().split("-")[0]);
            	item.setCheckUserName(item.getCheckRemarks().split("-")[1]);
            }
            
            // 兑奖截止状态
            item.setExpireFlag(false);
            if (StringUtils.isNotBlank(item.getExpireTime())) {
                if (currDateTime.compareTo(item.getExpireTime()) > 0) {
                    item.setExpireFlag(true);
                }
            }
            
            // 码中台
            if ("1".equals(item.getMztFlag())) {
                item.setPrizeVcodeUrl("HTTP://VJ1.TV/ZT/" + item.getPrizeVcode());
            } else {
                item.setPrizeVcodeUrl("HTTP://VJ1.TV/" + projectFlag + "/" + item.getPrizeVcode());
            }
        }
        return majorLst;
    }
    
    /**
     * 中奖名单总条数
     * 
     * @param queryParam
     * @return
     */
    public int queryMajorInfoListForTotal(MajorInfo queryBean, SysUserBasis optUser) {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("queryBean", queryBean);
        args.put("projectServerName", DbContextHolder.getDBType());
        return iMajorInfoDao.queryMajorInfoListForTotal(args);
    }
    
    /**
     * 依据主键查询
     * 
     * @param infoKey
     * @return
     */
    public MajorInfo findMajorInfoByInfoKey(String infoKey) throws Exception {
        MajorInfo majorInfo = iMajorInfoDao.findMajorInfoByInfoKey(infoKey);
        
        if(Constant.CHECK_STATUS.status_1.equals(majorInfo.getCheckStatus())
        		&& StringUtils.isBlank(majorInfo.getCheckOpenid())
        		&& StringUtils.isNotBlank(majorInfo.getCheckRemarks()) && majorInfo.getCheckRemarks().indexOf("-") > 0){
        	majorInfo.setCheckPhoneNum(majorInfo.getCheckRemarks().split("-")[0]);
        	majorInfo.setCheckUserName(majorInfo.getCheckRemarks().split("-")[1]);
        }

        // 奖项类型
        Map<String, String> prizeTypeMap = vpointsCogService.queryAllPrizeType(false, true, false, false, false, false, null);
        if (StringUtils.isNotBlank(majorInfo.getGrandPrizeType())) {
            String prizeTypeSuffix = "";
            if(prizeTypeMap.containsKey(majorInfo.getGrandPrizeType())){
                prizeTypeSuffix = "-随机";
            } else if (Pattern.matches("0|1|2", majorInfo.getGrandPrizeType()) 
                    || prizeTypeMap.containsKey(majorInfo.getGrandPrizeType().toLowerCase())){
                prizeTypeSuffix = "-定投";
            }
            if ("0".equals(majorInfo.getGrandPrizeType())) {
                majorInfo.setGrandPrizeType(StringUtils.defaultIfBlank(prizeTypeMap.get("5"), "0"));
            } else if ("1".equals(majorInfo.getGrandPrizeType())) {
                majorInfo.setGrandPrizeType(StringUtils.defaultIfBlank(prizeTypeMap.get("6"), "1"));
            } else if ("2".equals(majorInfo.getGrandPrizeType())) {
                majorInfo.setGrandPrizeType(StringUtils.defaultIfBlank(prizeTypeMap.get("7"), "2"));
            } else if (StringUtils.isNotBlank(majorInfo.getGrandPrizeType())) {
                majorInfo.setGrandPrizeType(StringUtils.defaultIfBlank(
                        prizeTypeMap.get(majorInfo.getGrandPrizeType().toUpperCase()), majorInfo.getGrandPrizeType()));
            }
            majorInfo.setGrandPrizeType(majorInfo.getGrandPrizeType() + prizeTypeSuffix);
        }
        return majorInfo;
    }
    
    /**
     * 依据订单主键查询
     * 
     * @param exchangeId
     * @return
     */
    public MajorInfo findMajorInfoByExchangeId(String exchangeId) throws Exception {
        return iMajorInfoDao.findMajorInfoByExchangeId(exchangeId);
    }
    
    /**
     * 待发货表格下载
     */
    public void exportPrizeList(MajorInfo queryBean, HttpServletResponse response, SysUserBasis currUser) throws Exception {
        response.reset();
        response.setCharacterEncoding("GBK");
        response.setContentType("application/msexcel;charset=UTF-8");
        OutputStream outStream = response.getOutputStream();
        
        // 获取导出信息
        String currDateTime = DateUtil.getDateTime();
        List<MajorInfo> prizeLst = queryMajorInfoListForPage(queryBean, null, currUser);
        for (MajorInfo item : prizeLst) {
            item.setProvince(item.getProvince() + "-" + StringUtils.defaultIfBlank(item.getCity(), "") + "-" + StringUtils.defaultIfBlank(item.getCounty(), ""));
            
            if(Constant.CHECK_STATUS.status_0.equals(item.getCheckStatus())){
            	item.setCheckStatus("未兑奖");
            }else if(Constant.CHECK_STATUS.status_1.equals(item.getCheckStatus())){
            	item.setCheckStatus("已兑奖");
            	
            	// 平台核销的大奖需要设置核销人员和核销手机号字段
                if(StringUtils.isBlank(item.getCheckOpenid())
                		&& StringUtils.isNotBlank(item.getCheckRemarks()) && item.getCheckRemarks().indexOf("-") > 0){
                	item.setCheckPhoneNum(item.getCheckRemarks().split("-")[0]);
                	item.setCheckUserName(item.getCheckRemarks().split("-")[1]);
                }
            }

            // 已过期：未领取且兑奖已截止
            if (StringUtils.isNotBlank(item.getExpireTime()) && "0".equals(item.getUseStatus())) {
                if (currDateTime.compareTo(item.getExpireTime()) > 0) {
                    item.setCheckStatus("已过期");
                }
            }
        }

        String bookName = "";
        String[] headers = null;
        String[] valueTags = null;
        bookName = "大奖查询导出";
        if("liaoning".equals(DbContextHolder.getDBType())){
        	headers = new String[] {"记录主键", "用户主键", "用户昵称", "奖品类型", "中奖V码", "联系人", "手机号码", "身份证号", 
            		"配送地址", "物流公司", "物流单号", "发货备注", "中奖区域", "中奖SKU", "中奖时间", "领取时间", "兑奖截止时间", "兑奖时间", "兑奖人", "兑奖人手机号", "兑奖状态","大奖来源","兑换门店"};
        	
        	valueTags = new String[] {"infoKey", "userKey", "nickName", "grandPrizeType", "prizeVcode", "userName", "phoneNum", 
            		"idCard", "address", "expressCompany", "expressNumber", "expressSendMessage", "province", "skuName", "earnTime", "useTime", "expireTime", "checkTime", "checkUserName", "checkPhoneNum", "checkStatus","prizeSource","terminalName"};
        } else{
        	headers = new String[] {"记录主键", "用户主键", "用户昵称", "奖品类型", "中奖V码", "联系人", "手机号码", "身份证号", 
            		"配送地址", "物流公司", "物流单号", "发货备注", "中奖区域", "中奖SKU", "中奖渠道", "中奖时间", "领取时间", "兑奖截止时间", "兑奖时间", "兑奖人", "兑奖人手机号", "兑奖状态","兑换门店"};
        	valueTags = new String[] {"infoKey", "userKey", "nickName", "grandPrizeType", "prizeVcode", "userName", "phoneNum", 
            		"idCard", "address", "expressCompany", "expressNumber", "expressSendMessage", "province", "skuName", "projectChannel", "earnTime", "useTime", "expireTime", "checkTime", "checkUserName", "checkPhoneNum", "checkStatus","terminalName"};
        }

        response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode(bookName, "UTF-8") + DateUtil.getDate() + ".xls");
        ExcelUtil<MajorInfo> excel = new ExcelUtil<MajorInfo>(); 
        excel.writeExcel(bookName, headers, valueTags, prizeLst, DateUtil.DEFAULT_DATETIME_FORMAT, outStream);
        outStream.close();
        response.flushBuffer();
    }

    /**
     * 核销大奖
     * @param infoKey	大奖主键
     * @param userName	核销人
     * @param phoneNum  核销人手机号
     * @return 
     */
	public void executeCheckMajorInfo(String infoKey, String expressCompany,
	                String expressNumber, String expressSendMessage, SysUserBasis currentUser) {
		MajorInfo majorInfo = iMajorInfoDao.findById(infoKey);
		if(null == majorInfo){
		    throw new BusinessException("核销失败：奖项不存在");
		}
		
		// 如果已核销，则只更新收货地址
		majorInfo.setExpressCompany(expressCompany);
		majorInfo.setExpressNumber(expressNumber);
		majorInfo.setExpressSendMessage(expressSendMessage);
		if (Constant.CHECK_STATUS.status_1.equals(majorInfo.getCheckStatus())) {
	        logService.saveLog("firstPrize", Constant.OPERATION_LOG_TYPE.TYPE_2, 
	                JSON.toJSONString(majorInfo), "大奖主键:" + infoKey + "，修改物流信息");
		} else {
		    majorInfo.setCheckTime(DateUtil.getDateTime());
		    majorInfo.setCheckRemarks(currentUser.getPhoneNum() + "-" + currentUser.getUserName());
		    majorInfo.setCheckStatus(Constant.CHECK_STATUS.status_1);
		    logService.saveLog("firstPrize", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(majorInfo), 
		                        "大奖主键:" + infoKey + ",核销人：" + currentUser.getPhoneNum() + "-" + currentUser.getUserName());
		}
		iMajorInfoDao.update(majorInfo);
	}
	
	/**
     * 获取当天过期要回收的扫码中出的大奖
     * 
     * @param expireTime 过期时间yyyy-MM-dd
     * @param recoveryPrizes
     * @return
     */
    public List<MajorInfo> queryForExpired(String expireTime, List<String> recoveryPrizes) {
    	Map<String,Object> param = new HashMap<>();
    	param.put("expireTime", expireTime);
		param.put("recoveryPrizeList", CollectionUtils.isEmpty(recoveryPrizes) ? null : recoveryPrizes);
        return iMajorInfoDao.queryForExpired(param);
    }
    
    /**
     * 大奖回收
     * @param majorInfo
     * @throws Exception 
     */
    public void executeRecyclePrize(MajorInfo majorInfo) throws Exception {
        // 大奖日志信息
        String logMsg = "prizeVcode:" + majorInfo.getPrizeVcode() + " prizeType:" + majorInfo.getPrizeName();
        
        // 校验大奖的规则有效性
        if (StringUtils.isBlank(majorInfo.getVcodeActivityKey())
                || StringUtils.isBlank(majorInfo.getRebateRuleKey())
                    || StringUtils.isBlank(majorInfo.getVpointsCogKey())) {
            log.warn(logMsg + " --- 回收失败：缺少规则或奖项配置项主键");
            return;
            
        } else {
            // 获取大奖对应的规则
            VcodeActivityRebateRuleCog rebateRuleCog = rebateRuleCogService.findById(majorInfo.getRebateRuleKey());
            if (rebateRuleCog == null || Constant.DbDelFlag.del.equals(rebateRuleCog.getDeleteFlag())) {
                log.warn(logMsg + " --- 回收失败：规则不存在或已删除");
                return;
            } else if (Constant.rebateRuleType.RULE_TYPE_1.equals(rebateRuleCog.getRuleType())
                    || Constant.rebateRuleType.RULE_TYPE_2.equals(rebateRuleCog.getRuleType())) {
                String currDateTime = DateUtil.getDateTime();
                if (currDateTime.compareTo(rebateRuleCog.getBeginDate() + " " + rebateRuleCog.getBeginTime()) < 0 
                        || currDateTime.compareTo(rebateRuleCog.getEndDate() + " " + rebateRuleCog.getEndTime()) > 0) {
                    log.warn(logMsg + " --- 回收失败：规则已失效不需回收");
                    return;
                }
            }
        }
        
        // 获取大奖区域数量配置
        String prizeType = vpointsCogService.transformPrizeType(majorInfo.getGrandPrizeType());
        String prizeAreaDataDicId = DatadicUtil.dataDic.majorInfo.FIRSTPRIZE_STATUS + prizeType + "_AREA";
        SysDataDic sysDataDic = DatadicUtil.getDataDicByCategoryAndDataId(
                            DatadicUtil.dataDicCategory.MAJOR_INFO, prizeAreaDataDicId);
        if (StringUtils.isNotBlank(majorInfo.getCounty()) 
                && sysDataDic != null && StringUtils.isNotBlank(sysDataDic.getDataValue())) {
            // 获取奖品各区域的配置,中奖区域格式：省_总数量_剩余数量,市_总数量_剩余数量,市_总数量_剩余数量...;其它省的配置(注意：逗号、分号及下划线都是半角字符；省市全名；剩余数量是实时扣除且总数量仅用于显示
            String province = "", city = "";
            String[] itemAreaAry = null;
            String[] prizeAreaAry = sysDataDic.getDataValue().split(";");
            for (String itemArea : prizeAreaAry) {
                province = majorInfo.getProvince();
                if (majorInfo.getProvince().equals(majorInfo.getCity())) {
                    city = majorInfo.getCounty();
                } else {
                    city = majorInfo.getCity();
                }
                if (itemArea.contains(province) || itemArea.contains(city)) {
                    itemAreaAry = itemArea.split(",");
                    for (String item : itemAreaAry) {
                        if (item.contains(province) || item.contains(city)) {
                            sysDataDic.setDataValue(sysDataDic.getDataValue().replace(item, 
                                    item.substring(0, item.lastIndexOf("_")) + "_" + (Integer.valueOf(item.split("_")[2]) + 1)));
                        }
                    }
                    break;
                }
            }
            if (!sysDataDicService.updateDataValueForRowNum(sysDataDic)) {
                throw new BusinessException("更新数据时并发异常");
            }
        }
        
        // 如果是上传的奖项配置项，则对应的剩余数量+1
        if (majorInfo.getVpointsCogKey().length() == 36) {
            // 反扣当前奖项配置项的剩余数量redis缓存
            List<VcodeActivityVpointsCog> cogLst = new ArrayList<VcodeActivityVpointsCog>();
            VcodeActivityVpointsCog vpointsCog = new VcodeActivityVpointsCog();
            vpointsCog.setVpointsCogKey(majorInfo.getVpointsCogKey());
            vpointsCog.setRangeVal(-1L);
            cogLst.add(vpointsCog);
            vpointsCogService.updateBathWaitActivityVpointsCog(cogLst);
            String redisKey = RedisApiUtil.CacheKey.ActivityVpointsCog
                    .VPS_VCODE_ACTIVITY_VPOINTS_COG_NUM + ":" + majorInfo.getRebateRuleKey();
            RedisApiUtil.getInstance().setHincrBySet(redisKey, majorInfo.getVpointsCogKey(), 1);
        }
        
        // 标记为已回收
        iMajorInfoDao.updateRecycleFlag(majorInfo.getInfoKey());

        log.warn(logMsg + " --- 回收成功");
        
    }

	public List<MajorInfo> queryPrizeTypeLst(String prizeType,boolean isTicketFlag) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("prizeType", prizeType);
	    args.put("isTicketFlag", isTicketFlag);
		return iMajorInfoDao.queryPrizeTypeLst(args);
	}

    public void addFirstPrizeRecord(MajorInfo majorInfo) {
        iMajorInfoDao.addFirstPrizeRecord(majorInfo);
    }
}
