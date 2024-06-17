/*
 *
 */
package com.dbt.platform.activity.action;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.log.service.VpsOperationLogService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.*;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.bean.VcodeQrcodeBatchInfo;
import com.dbt.platform.activity.bean.VpsActivateBatchLog;
import com.dbt.platform.activity.bean.VpsVcodeQrcodeLib;
import com.dbt.platform.activity.dao.IVcodeQrcodeLibDao;
import com.dbt.platform.activity.service.VcodeActivityService;
import com.dbt.platform.activity.service.VcodeQrcodeBatchInfoService;
import com.dbt.platform.activity.service.VpsActivateBatchLogServiceImpl;
import com.dbt.platform.autocode.bean.VpsQrcodeOrder;
import com.dbt.platform.autocode.service.VpsQrcodeOrderService;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

/**
 * 文件名：VcodeQrcodeBatchInfoAction.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
 * 描述: V码二维码批次信息action<br>
 * 修改人: cpgu<br>
 * 修改时间：2016-04-22 10:30:15<br>
 * 修改内容：新增<br>
 */
@Controller
@RequestMapping("/qrcodeBatchInfo")
public class QrcodeBatchInfoAction extends BaseAction {
    private static final String LOCK_KEY = "vpsQrcodeOrder:create.lock";
    private static final int LOCK_TIMEOUT = 1000 * 60 * 30; //毫秒 

    @Autowired
    private VcodeActivityService vcodeActivityService;
    @Autowired
    private VcodeQrcodeBatchInfoService batchInfoService;
    @Autowired
    private VpsActivateBatchLogServiceImpl activateBatchLogService;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private VpsQrcodeOrderService qrcodeOrderService;
    @Autowired
    private VpsOperationLogService logService;
    @Autowired
    private IVcodeQrcodeLibDao qrcodeLibDao;

    /**
     * 生成码源批次列表
     */
    @RequestMapping("/showBatchInfoList")
    public String showBatchInfoList(HttpSession session, String pageParam, String queryParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VcodeQrcodeBatchInfo queryBean = new VcodeQrcodeBatchInfo(queryParam, Constant.QrcodeBatchType.type_0);
            queryBean.setCompanyKey(currentUser.getCompanyKey());
            List<VcodeQrcodeBatchInfo> resultLst = batchInfoService.queryForLst(queryBean, pageInfo);
            int countResult = batchInfoService.queryForCount(queryBean);

            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultLst);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryBean", queryBean);
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "vcode/qrcode/showBatchInfoList";
    }

    /**
     * 添加批次信息
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/doBatchInfoAdd")
    public String doBatchInfoAdd(HttpSession session, VcodeQrcodeBatchInfo batchInfo, Model model) {
        Map<String, String> resultMap = new HashMap<>();
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            batchInfo.setCompanyKey(currentUser.getCompanyKey());
            batchInfoService.addVcodeQrcodeBatchInfo(batchInfo, currentUser.getUserKey());
            resultMap.put("errMsg", "添加成功");

        } catch (BusinessException e) {
            resultMap.put("errMsg", e.getMessage());

        } catch (Exception e) {
            resultMap.put("errMsg", "添加失败");
            log.error("批次创建失败", e);
        }
        return JSON.toJSONString(resultMap);
    }

    /**
     * 修改批次信息
     */
    @ResponseBody
    @RequestMapping("/doBatchInfoEdit")
    public String doBatchInfoEdit(HttpSession session, VcodeQrcodeBatchInfo batchInfo, Model model) {
        Map<String, String> resultMap = new HashMap<>();
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            batchInfo.fillUpdateFields(currentUser.getUserKey());
            batchInfoService.updateVcodeQrcodeBatchInfo(batchInfo);
            resultMap.put("errMsg", "修改成功");

        } catch (BusinessException e) {
            resultMap.put("errMsg", e.getMessage());

        } catch (Exception e) {
            resultMap.put("errMsg", "修改失败");
            log.error("批次修改失败", e);
        }
        return JSON.toJSONString(resultMap);
    }

    /**
     * 获取批次信息
     */
    @ResponseBody
    @RequestMapping("/findBatchInfo")
    public String findBatchInfo(HttpSession session, String batchKey, Model model) {
        VcodeQrcodeBatchInfo batchInfo = null;
        try {
            batchInfo = batchInfoService.findById(batchKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("batchInfo", batchInfo);
        return JSON.toJSONString(resultMap);
    }

    /**
     * 删除记录
     */
    @RequestMapping("/doBatchInfoDelete")
    public String doBatchInfoDelete(HttpSession session, String batchKey, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            batchInfoService.deleteBatchInfoById(batchKey, currentUser.getUserKey());
            ;
            model.addAttribute("errMsg", "删除成功");
        } catch (Exception e) {
            model.addAttribute("errMsg", "删除失败");
            if (e.getMessage().contains("批次状态异常")) {
                model.addAttribute("errMsg", e.getMessage());
            }
            e.printStackTrace();
        }
        return "forward:showBatchInfoList.do";
    }

    /**
     * 跳转创建码源订单页面
     */
    @RequestMapping("/showQrcodeOrderAdd")
    public String showQrcodeOrderAdd(HttpSession session, String batchKey, Model model) {

        SysUserBasis currentUser = this.getUserBasis(session);
        Map<String, Object> checkMap = new HashMap<>();
        try {
            checkMap = batchInfoService.checkBatchInfoForQrcodeOrder(batchKey);

            // SKU
            List<SkuInfo> skuLst = skuInfoService
                    .loadSkuListByCompany(currentUser.getCompanyKey());

            // 赋码厂
            String qrcodeManufactureGro = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.COMPANY_INFO,
                    DatadicKey.companyInfo.COMPANY_QRCODE_MANUFACTURE_NAME);

            // 酒厂
            String factoryNameGro = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.COMPANY_INFO,
                    DatadicKey.companyInfo.COMPANY_FACTORY_NAME);

            model.addAttribute("clientOrderNo", checkMap.get("clientOrderNo"));
            model.addAttribute("batchKeys", batchKey);
            model.addAttribute("skuLst", skuLst);
            model.addAttribute("qrcodeManufactureGro", qrcodeManufactureGro);
            model.addAttribute("factoryNameGro", factoryNameGro);
            model.addAttribute("batchNameTips", checkMap.get("batchNameTips"));
            return "vcode/autocode/showQrcodeOrderAdd";

        } catch (BusinessException e) {
            model.addAttribute("errMsg", e.getMessage());

        } catch (Exception e) {
            log.error(e);
        }
        return "forward:showBatchInfoList.do";
    }

    /**
     * 回传码源批次列表
     */
    @RequestMapping("/showBatchImportList")
    public String showBatchImportList(HttpSession session, String pageParam, String queryParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VcodeQrcodeBatchInfo queryBean = new VcodeQrcodeBatchInfo(queryParam, Constant.QrcodeBatchType.type_2);
            queryBean.setCompanyKey(currentUser.getCompanyKey());
            List<VcodeQrcodeBatchInfo> resultLst = batchInfoService.queryForImportLst(queryBean, pageInfo);
            int countResult = batchInfoService.queryForImportCount(queryBean);

            List<VcodeActivityCog> activityCogList = vcodeActivityService
                    .findAllVcodeActivityList(Constant.activityType.activity_type0, "");

            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultLst);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryBean", queryBean);
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("tabsFlag", "1");
            model.addAttribute("activityCogList", activityCogList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "vcode/qrcode/showBatchImportMain";
    }

    /**
     * 回传码源批次列表
     */
    @RequestMapping("/showBatchImportUsedList")
    public String showBatchImportUsedList(HttpSession session, String pageParam, String queryParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VcodeQrcodeBatchInfo queryBean = new VcodeQrcodeBatchInfo(queryParam, Constant.QrcodeBatchType.type_5);
            queryBean.setCompanyKey(currentUser.getCompanyKey());
            List<VcodeQrcodeBatchInfo> resultLst = batchInfoService.queryForImportUsedLst(queryBean, pageInfo);
            int countResult = batchInfoService.queryForImportUsedCount(queryBean);

            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultLst);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryBean", queryBean);
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("tabsFlag", "2");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "vcode/qrcode/showBatchImportMain";
    }

    /**
     * 回传码源批次列表
     */
    @RequestMapping("/showBatchForActivityList")
    public String showBatchForActivityList(HttpSession session, String infoKey, String pageParam, String queryParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VcodeQrcodeBatchInfo queryBean = new VcodeQrcodeBatchInfo(queryParam, Constant.QrcodeBatchType.type_2);
            queryBean.setCompanyKey(currentUser.getCompanyKey());
            List<VcodeQrcodeBatchInfo> resultLst = batchInfoService.queryForImportLst(queryBean, pageInfo);
            int countResult = batchInfoService.queryForImportCount(queryBean);

            // 活动信息
            VcodeActivityCog activityCog = vcodeActivityService.findById(infoKey);

            List<VcodeActivityCog> activityCogList = vcodeActivityService
                    .findAllVcodeActivityList(Constant.activityType.activity_type0, activityCog.getVcodeActivityKey());

            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultLst);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryBean", queryBean);
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("tabsFlag", "2");
            model.addAttribute("activityCog", activityCog);
            model.addAttribute("activityCogList", activityCogList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return "vcode/qrcode/showBatchForActivityMain";
    }

    /**
     * 回传码源批次列表
     */
    @RequestMapping("/showBatchForActivityUsedList")
    public String showBatchForActivityUsedList(HttpSession session, String infoKey, String pageParam, String queryParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VcodeQrcodeBatchInfo queryBean = new VcodeQrcodeBatchInfo(queryParam, Constant.QrcodeBatchType.type_6);
            queryBean.setCompanyKey(currentUser.getCompanyKey());
            queryBean.setVcodeActivityKey(infoKey);
            List<VcodeQrcodeBatchInfo> resultLst = batchInfoService.queryForImportUsedLst(queryBean, pageInfo);
            int countResult = batchInfoService.queryForImportUsedCount(queryBean);

            // 活动信息
            VcodeActivityCog activityCog = vcodeActivityService.findById(infoKey);
            // 活动有效标志
            boolean activityValidFlag = DateUtil.getDate().compareTo(activityCog.getEndDate()) <= 0;

            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultLst);
            model.addAttribute("showCount", countResult);
            model.addAttribute("activityCog", activityCog);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryBean", queryBean);
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("tabsFlag", "1");
            model.addAttribute("infoKey", infoKey);
            model.addAttribute("activityValidFlag", activityValidFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "vcode/qrcode/showBatchForActivityMain";
    }

    /**
     * 移除活动下的批次到码源回传入库
     */
    @RequestMapping("/doUpdateBatchBackForImport")
    public String doSendBackBatchImport(HttpSession session, String batchKey, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            batchInfoService.updateBatchBackForImport(batchKey, currentUser.getPhoneNum(), currentUser.getUserKey());
            model.addAttribute("errMsg", "移出成功");

        } catch (BusinessException e) {
            model.addAttribute("errMsg", e.getMessage());

        } catch (Exception e) {
            model.addAttribute("errMsg", "移出失败");
            log.error(e.getMessage(), e);
        }
        return "forward:showBatchForActivityUsedList.do";
    }

    /**
     * 查询激活信息
     *
     * @param batchKey
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/showActivateBatchLog")
    public String showActivateBatchLog(String batchKey, HttpSession session, Model model) {
        List<VpsActivateBatchLog> logList = activateBatchLogService.queryList(batchKey);
        return JSON.toJSONString(logList);
    }

    /**
     * 显示批次有效期修改
     */
    @ResponseBody
    @RequestMapping("/showBatchValidDateUpdate")
    public String showBatchValidDateUpdate(HttpSession session, String batchKey) {
        return JSON.toJSONString(batchInfoService.initBatchValidDateUpdate(batchKey));
    }

    /**
     * 修改批次有效期
     */
    @ResponseBody
    @RequestMapping("/doBatchValidDateUpdate")
    public String doBatchValidDateUpdate(VcodeQrcodeBatchInfo vcodeQrcodeBatchInfo,
                                         String pageParam, String queryParam, Model model, HttpSession session) {
        String errMsg = "";
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            batchInfoService.updateBatchValidDate(vcodeQrcodeBatchInfo, currentUser.getUserKey(), currentUser.getPhoneNum());
            errMsg = "修改成功";
        } catch (Exception e) {
            errMsg = "修改失败";
            e.printStackTrace();
            log.error(e);
        }
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("errMsg", errMsg);
        return JSON.toJSONString(resultMap);
    }
//	
//	/**
//	 * 判断批码说明是否存在
//	 * @param </br> 
//	 * @return String </br>
//	 */
//	@ResponseBody
//	@RequestMapping("/isExistActivityBatchDesc")
//	public String isExistActivityBatchDesc(HttpSession session, String batchDesc){
//		Integer count = batchInfoService.isExistActivityBatchDesc(batchDesc);
//		if(count > 0){
//			return "1";
//		}else{
//			return "0";
//		}
//	}
//

//	
//	/**
//	 * 批次是否合法
//	 * 
//	 * @param </br>
//	 * @return String 1批码说明已存在；2 批次的时间不在活动范围内</br>
//	 */
//	@ResponseBody
//	@RequestMapping("/isLegal")
//	public String isLegal(HttpSession session, String vcodeActivityKey,
//			String startDate, String endDate, String batchDesc) {
//
//		//判断批码说明是否存在
//		Integer count = batchInfoService.isExistActivityBatchDesc(batchDesc);
//		if(count > 0){
//			return "1";
//		}else{
//			// 判断时间是否在活动范围内
//			String flag = vcodeActivityService.judgeActivityTime(vcodeActivityKey, startDate, endDate);
//			if("1".equals(flag)){
//				return "2";
//			}
//		}
//		return "0";
//	}

    /**
     * 跳转批次调整页面
     *
     * @param </br>
     * @return String </br>
     */
    @RequestMapping("/showBatchAdjust")
    public String showBatchAdjust(HttpSession session, String batchKey, Model model) {

        // 要调整的批次主键集合
        List<String> batchKeyList = new ArrayList<>(Arrays.asList(
                StringUtils.defaultIfBlank(batchKey, "").split(",")));
        List<VcodeQrcodeBatchInfo> batchInfoList =
                batchInfoService.queryBatchInfoByKey(batchKeyList);

        // 要调整批次的名称
        StringBuffer buffer = new StringBuffer("");
        for (VcodeQrcodeBatchInfo item : batchInfoList) {
            buffer.append(item.getBatchName()).append("</br>");
        }

        if (buffer.length() > 0) {
            buffer.insert(0, "您确定调整以下批次吗? </br>");
            buffer.subSequence(0, buffer.length() - 1);
        }

        // 排除当前批次所属活动
        VcodeQrcodeBatchInfo batchInfo = batchInfoList.get(0);
        List<VcodeActivityCog> activityCogList = vcodeActivityService
                .findAllVcodeActivityList(Constant.activityType.activity_type0, batchInfo.getVcodeActivityKey());

        model.addAttribute("batchKey", batchKey);
        model.addAttribute("batchInfo", batchInfo);
        model.addAttribute("batchNameTips", buffer.toString());
        model.addAttribute("batchInfoList", batchInfoList);
        model.addAttribute("activityCogList", activityCogList);
        return "vcode/qrcode/showBatchAdjust";
    }

    /**
     * 批次调整
     *
     * @param </br>
     * @return String </br>
     */
    @ResponseBody
    @RequestMapping("/doBatchAdjustAjax")
    public String doBatchAdjustAjax(HttpSession session, String batchKey,
                                    String batchName, String endDate, String vcodeActivityKey, Model model) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            batchInfoService.modifyAdjustbatch(batchKey, batchName,
                    endDate, vcodeActivityKey, currentUser.getPhoneNum(), currentUser.getUserKey());
            resultMap.put("errMsg", "批次调整成功");

        } catch (BusinessException e) {
            resultMap.put("errMsg", "批次调整失败，" + e.getMessage());

        } catch (Exception e) {
            resultMap.put("errMsg", "批次调整失败");
            log.error("批次调整失败：" + batchKey, e);
        }
        return JSON.toJSONString(resultMap);
    }

    /**
     * 修改批次所属合同年份
     */
    @ResponseBody
    @RequestMapping("/doContractYearAjax")
    public String doContractYearAjax(HttpSession session, String batchKey,
                                     String contractYear, String contractChangeDate, String nextContractYear, Model model) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            List<String> batchKeyLst = new ArrayList<>();
            if (StringUtils.isNotBlank(batchKey)) {
                batchKeyLst = Arrays.asList(batchKey.split(","));
            }
            batchInfoService.modifyContractYear(batchKeyLst, contractYear, contractChangeDate, nextContractYear, currentUser);

            // 删除批次缓存
            for (String key : batchKeyLst) {
                CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode.EKY_VPS_VCODE_QRCODE_BATCHINFO
                        + Constant.DBTSPLIT + key + Constant.DBTSPLIT + DateUtil.getDate("yyyyMMdd"));
            }

            resultMap.put("errMsg", "所属合同年份修改成功");

        } catch (Exception e) {
            initErrMsg(e, model, "所属合同年份修改失败");
            log.error("所属合同年份修改失败：" + batchKey, e);
        }
        return JSON.toJSONString(resultMap);
    }
//	
//	
//	/**
//	 * 删除批次
//	 * @param </br> 
//	 * @return String </br>
//	 */
//	@RequestMapping("{id}/delBatchInfo")
//	public String delBatchInfo(HttpSession session, @PathVariable("id") String vcodeActivityKey, String batchKey, Model model){
//		 VcodeQrcodeBatchInfo batchInfo = batchInfoService.findById(batchKey);
//		 if(null != batchInfo){
//			 try {
//				batchInfoService.deleteBatchInfo(batchInfo);
//				model.addAttribute("refresh", "del_batch_success");
//			} catch (Exception e) {
//				 model.addAttribute("refresh", "del_batch_failure");
//				e.printStackTrace();
//			}
//		 }
//		 return showBatchInfoList(vcodeActivityKey, null, null, session, model);
//	}
//	
//	

    /**
     * 跳转导入码包
     */
    @RequestMapping("/showQrcodeImport")
    public String showQrcodeImport(HttpSession session, Model model) {
        return "vcode/qrcode/showQrcodeImport";
    }



    /**
     * 检查活动关联SKU是否包含当前激活批次
     * 1-检查通过 其他-不通过
     */
    @ResponseBody
    @RequestMapping("/checkActivityContainSku")
    public String checkActivity(String batchKey, String vcodeActivityKey) {
        try {

            List<String> batchKeyList = Arrays.asList(batchKey.split(","));
            List<String> skuKeyList = vcodeActivityService.loadSkuKeyListByActivityKey(vcodeActivityKey);
            if (skuKeyList == null) {
                return "0";
            }
            for (String key : batchKeyList) {
                VcodeQrcodeBatchInfo batchInfo = batchInfoService.findById(key);
                if (!skuKeyList.contains(batchInfo.getSkuKey())) {
                    return "0";
                }
            }
            return "1";
        } catch (Exception e) {
            log.warn("检查活动关联SKU是否包含当前激活批次出现异常" + e.getMessage());
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 导入并入库
     */
    @ResponseBody
    @SuppressWarnings("unchecked")
    @RequestMapping("/doQrcodeImport")
    public String doQrcodeImport(HttpSession session, HttpServletResponse response,
                                 @RequestParam("filePath") MultipartFile clientFile, String orderNo, Model model) {
        //获取锁
        String lockId = RedisApiUtil.getInstance().tryGetDistributedLock(LOCK_KEY, null, LOCK_TIMEOUT);
        if (lockId == null) {
            outPrint(response, "请稍后再试，目前有任务正在执行中。。。");
            return null;
        }

        model.addAttribute("refresh", "import_fail");
        SysUserBasis currentUser = this.getUserBasis(session);
        int totalCount = 0;
        int totalValidCount = 0;
        int successImportCount = 0;
        boolean nextFlag = true;
        StringBuilder sbLog = new StringBuilder();
        StringBuilder showLog = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder("导入失败:");    // 提示信息
        StringBuilder difProjectMsg = new StringBuilder("非本项目二维码文件:"); // 本项目外码包异常提示信息
        try {
            String path = PropertiesUtil.getPropertyValue("qrcode_saveQrcodeFilePath") + "/" + DbContextHolder.getDBType();
            File file = new File(path);
            if (file.listFiles() != null && file.listFiles().length > 0) {
                errorMsg.append("出现此提示可能：").append("</br></br>");
                errorMsg.append("1.执行导入一段时间后出现此提示，是浏览器请求超时发生了重复请求，无需理会，耐心等待导入成功短信通知；").append("</br></br>");
                errorMsg.append("2.导入当时出现此提示，说明有导入操作还在执行中，请稍后再试。");
            } else {
                // 获取码源订单
                VpsQrcodeOrder qrcodeOrder = qrcodeOrderService.findByOrderNo(orderNo);
                if (qrcodeOrder == null) {
                    errorMsg = new StringBuilder("码源订单不存在！");

                } else if (!clientFile.isEmpty() && StringUtils.isNotBlank(path)) {
                    String batchNoGro = "";
                    List<String> fileNameList = ImportFileUtil.unZipSave(clientFile.getInputStream(), path);
                    if (fileNameList.size() > 1) {
                        Iterator<String> iterator = fileNameList.iterator();
                        while (iterator.hasNext()) {
                            String next = iterator.next();
                            if (next.indexOf("__MACOSX") > -1) {
                                iterator.remove();
                            }
                        }
                        //删除 mac 系统压缩文件的多余文件
                        File removeFile__MACOSX = new File(path + "/__MACOSX");
                        removeFile__MACOSX.delete();
                    }
                    if (null != fileNameList && !fileNameList.isEmpty()) {
                        Collections.sort(fileNameList);
                        Collections.reverse(fileNameList);

                        // 批量生成批次
                        try {
                            batchNoGro = batchInfoService.batchVcodeQrcodeBatchInfo(qrcodeOrder, fileNameList, currentUser.getUserKey());
                        } catch (Exception e) {
                            e.printStackTrace();
                            nextFlag = false;
                            // 重复创建
                            if (e.getMessage().toUpperCase().contains("IDX_BATCH_DESC")) {
                                errorMsg = new StringBuilder("请勿重复导入该文件");
                            } else {
                                errorMsg = new StringBuilder("创建批次失败,请重新导入");
                                log.error(errorMsg + e.getMessage());
                            }
                        }

                        // 码包中的二维码入库并修改批次状态
                        if (nextFlag) {
                            Set<String> libNameList = new HashSet<String>();
                            showLog.append("日志开始时间：" + DateUtil.getDateTime() + "\n</br>");
                            Map<String, Object> map = null;
                            for (String fileName : fileNameList) {
                                map = saveQrcode(qrcodeOrder, qrcodeOrder.getCompanyKey(), fileName, path, sbLog, model);

                                // 拼接错误日志
                                if (StringUtils.isNotBlank(String.valueOf(map.get("errorMsg")))) {
                                    errorMsg.append(map.get("errorMsg"));
                                }
                                if (StringUtils.isNotBlank(String.valueOf(map.get("difProjectMsg")))) {
                                    difProjectMsg.append(map.get("difProjectMsg"));
                                }

                                // 导入文件二维码总行数
                                if (StringUtils.isNotBlank(String.valueOf(map.get("count")))) {
                                    totalCount += (int) map.get("count");
                                }
                                // 导入文件二维码有效总行数
                                if (StringUtils.isNotBlank(String.valueOf(map.get("validCount")))) {
                                    totalValidCount += (int) map.get("validCount");
                                }

                                if (null != map.get("libNameList")) {
                                    libNameList.addAll((Set<String>) map.get("libNameList"));
                                }
                            }

                            // 批次序号
                            if (Constant.QRCODE_FORMAT.format_16.equals(qrcodeOrder.getQrcodeFormat())) {
                                for (String libName : libNameList) {
                                    qrcodeLibDao.updateBatchAutocodeNo(libName);
                                }
                            }

                            // 查询成功导入的二维码数量
                            if (libNameList.size() > 0) {
                                successImportCount = batchInfoService.querySuccessImportQrcodeCount(libNameList);
                            }

                            // 存储导入日志
                            if (null != sbLog) {
                                if (!"导入失败:".equals(errorMsg.toString())) {
                                    sbLog.append(errorMsg.toString()).append("\n</br>");
                                }
                                String resultInfo = (totalValidCount > 0 && successImportCount > 0 && Math.abs(totalValidCount - successImportCount) < 10
                                        && errorMsg.toString().length() < 6) ? "正常" : "异常：入库码量和有效码数相差" + Math.abs(totalValidCount - successImportCount) + "个，请手动排查";
                                showLog.append("生成批次：" + (!CollectionUtils.isEmpty(fileNameList) ? fileNameList.size() : 0) + " 个").append("\n</br>");
                                showLog.append("生成批号：" + batchNoGro).append("\n</br>");
                                showLog.append("回传二维码总数：" + totalCount + "，有效码数：" + totalValidCount).append("\n</br>");
                                showLog.append("成功入库二维码总数：" + successImportCount + " 个").append("\n</br>");
                                showLog.append("导入最终结果：" + resultInfo).append("\n</br>");
                                showLog.append("日志结束时间：" + DateUtil.getDateTime() + "\n</br>");
                                sbLog.append(showLog);
                                sbLog.append("\n");
                                File logFile = new File("/home/importQrcodeLog/" + DateUtil.getDate() + "/importFileLog/importFileLog.txt");
                                if (!logFile.getParentFile().exists()) {
                                    logFile.getParentFile().mkdirs();
                                }
                                BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
                                writer.write(sbLog.toString().replaceAll("</br>", ""));
                                writer.newLine();
                                writer.close();
                            }

                            // 导入成功后，修改码源订单的回传状态
                            qrcodeOrderService.updateOrderImportFlag(qrcodeOrder.getOrderKey(), "1", currentUser.getUserKey());
//                    		log.error("二维码入库执行完毕：该订单二维码总数：" + totalCount +",有效码数："+ totalValidCount);
                            //删除 mac 系统压缩文件的多余文件
                            File removeFile__MACOSX = new File(path + "/__MACOSX");
                            if (removeFile__MACOSX.exists()) {
                                File[] files = removeFile__MACOSX.listFiles();
                                for (File file1 : files) {
                                    file1.delete();
                                }
                                removeFile__MACOSX.delete();
                            }
                        }
                    }
                }
            }

            RedisApiUtil.getInstance().releaseDistributedLock(LOCK_KEY, lockId, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            errorMsg = new StringBuilder("导入失败");
            log.error(errorMsg.toString() + ex.getMessage(), ex);
        } finally {
            RedisApiUtil.getInstance().releaseDistributedLock(LOCK_KEY, lockId, null);
        }

        String sendMsg = "该二维码文件";

        if ("导入失败:".equals(errorMsg.toString()) && "非本项目二维码文件:".equals(difProjectMsg.toString())) {
            errorMsg = new StringBuilder("二维码导入成功</br>");
            sendMsg += clientFile.getOriginalFilename() + "导入成功；";
            sendMsg += "二维码总数：" + totalCount + "，有效码数：" + totalValidCount + "，成功入库数：" + successImportCount;
            model.addAttribute("refresh", "import_success");
        } else if (!"非本项目二维码文件:".equals(difProjectMsg.toString())) {
            if (!"导入失败:".equals(errorMsg.toString())) {
                errorMsg.append("</br></br>" + difProjectMsg.toString());
            } else {
                errorMsg = new StringBuilder("导入失败</br>" + difProjectMsg.toString());
            }
            sendMsg += clientFile.getOriginalFilename() + "导入失败。";
        }

        errorMsg.append(showLog);

        if (!"导入失败:".equals(errorMsg.toString())) {
            log.error(errorMsg.toString().replaceAll("</br>", ""));
        }

        if (nextFlag && !sendMsg.equals("该二维码文件")) {
            // 发送短信通知项目经理
            String phones = DatadicUtil.getDataDicValue("data_constant_config", "notify_tech_phones");
            if (!StringUtils.isEmpty(phones)) {
                sendMsg += "【V积分】";
                SendSMSUtil.sendSmsByMobileArray(sendMsg, phones);
            }
        }

        logService.saveLog("qrcodeBatchInfo", Constant.OPERATION_LOG_TYPE.TYPE_1,
                errorMsg.toString().contains("导入失败") ? "导入失败" : errorMsg.toString(), "一万一批次码源导入");

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("errMsg", errorMsg.toString());
        return JSON.toJSONString(resultMap);
    }
//    
//    /**
//     * 导入批次二维码
//     */
//    @RequestMapping("/importBatchQrcode")
//    public String importBatchQrcode(HttpSession session, 
//    		String vcodeActivityKey, String fileName, Model model) {
//    	model.addAttribute("refresh", "import_fail");
//    	String errorMsg = "导入失败:";	// 提示信息
//    	String difProjectMsg = "非本项目二维码文件:"; // 本项目外码包异常提示信息
//        try {
//            String path = PropertiesUtil.getPropertyValue("qrcode_saveQrcodeFilePath");
//            Map<String,Object> map = saveQrcode(vcodeActivityKey, fileName, path, null, model);
//         // 拼接错误日志
//			if(StringUtils.isNotBlank(String.valueOf(map.get("errorMsg")))){
//				errorMsg += map.get("errorMsg");
//			}
//			if(StringUtils.isNotBlank(String.valueOf(map.get("difProjectMsg")))){
//				difProjectMsg += map.get("difProjectMsg");
//			}
//			
//			// 导入文件二维码总行数
//			if(StringUtils.isNotBlank(String.valueOf(map.get("count"))) && StringUtils.isNotBlank(String.valueOf(map.get("validCount")))){
//				log.error("当前批次二维码入库执行完毕：二维码总数：" + map.get("count") +"，有效码数："+ map.get("validCount"));
//			}
//        } catch (Exception ex) {
//        	errorMsg = "导入失败";
//            log.error(errorMsg + ex.getMessage());
//        }
//        
//        if("导入失败:".equals(errorMsg) && "非本项目二维码文件:".equals(difProjectMsg)){
//        	errorMsg = "二维码导入成功";
//        	model.addAttribute("refresh", "import_success");
//        }else if(!"非本项目二维码文件:".equals(difProjectMsg)){
//        	errorMsg += "</br></br>" + difProjectMsg;
//        }
//        
//        if(!"导入失败:".equals(errorMsg)){
//        	log.error("导入二维码异常原因：" + errorMsg);
//        }
//        model.addAttribute("errorMsg", errorMsg);
//        return showBatchInfoList(vcodeActivityKey, null, null, session, model);
//    }

    /**
     * 解析后的二维码入库操作
     *
     * @param companyKey 企业主键</br>
     * @param fileName   文件名称</br>
     * @param path       文件路径</br>
     * @param errorMsg   提示信息</br>
     * @return boolean </br>
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> saveQrcode(VpsQrcodeOrder qrcodeOrder, String companyKey, String fileName, String path, StringBuilder sbLog, Model model) {
        StringBuilder errorMsg = new StringBuilder();
        String difProjectMsg = "";
        int count = 0;
        int validCount = 0;
        Set<String> libNameList = null;

        String batchDesc = null;
        if (fileName.split("-").length == 3) {
            batchDesc = fileName.substring(0, fileName.lastIndexOf(".")).split("-")[2];
        } else {
            batchDesc = fileName.substring(0, fileName.lastIndexOf(".")).split("-")[3];
        }

        VcodeQrcodeBatchInfo batchInfo =
                batchInfoService.findVcodeQrcodeBatchInfoByBatchDesc(batchDesc);
        if (null != batchInfo && Constant.ImportFlag.ImportFlag_0.equals(batchInfo.getImportFlag())) {
            try {
                // add 20180212 add 文件名称保持一致 hanshimeng
                if (StringUtils.isNotBlank(batchInfo.getFileName())) {
                    fileName = batchInfo.getFileName();
                }
                String encoding = "GBK";
                File file = new File(path + File.separator + fileName);

                // 判断文件是否存在
                if (file.isFile() && file.exists()) {
                    // 考虑到编码格式
                    InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                    BufferedReader bufferedReader = new BufferedReader(read);
                    // 不同项目验证标志 true通过
                    boolean isValidFlag = true;
                    // 读取文件行内容
                    String lineTxt = null;
                    // 文件中二维码的URL
                    String qrcodeUrl = null;
                    // 解析前的二维码
                    String sweepstr = null;
                    // 解析后的二维码Map
                    Map<String, String> sweepCodeMap = null;
                    // 二维码
                    String qrcode = null;
                    // 二维码标识（批次标识）
                    String vcodeUniqueCode = null;
                    // 存放二维码List
                    List<VpsVcodeQrcodeLib> qrcodeList = null;
                    // 最酒存放二维码list
                    List<VpsVcodeQrcodeLib> zuiJiuqrcodeList = null;
                    // 存放二维码List
                    List<String> traceabilityURLList = null;
                    // 项目标识
                    String projectFlag = "/" + DatadicUtil.getDataDicValue(
                            DatadicKey.dataDicCategory.FILTER_COMPANY_INFO,
                            DatadicKey.filterCompanyInfo.PROJECT_FLAG) + "/";

                    // 河南兼容HP和h，默认为瓶装HP
                    if ("henanpz".equals(DbContextHolder.getDBType())) {
                        if (StringUtils.isNotBlank(qrcodeOrder.getSkuName()) && qrcodeOrder.getSkuName().contains("罐")) {
                            projectFlag = "/h/";
                        }
                    }

                    // 山东，兼容终端促销码源
                    if ("shandongagt".equals(DbContextHolder.getDBType()) && qrcodeOrder.getOrderNo().startsWith("ZDSDVMTS")) {
                        projectFlag = "/T1/";
                    }
                    String traceabilityURL = null;
                    String[] lineTxtAry = null;
                    Map<String, List<VpsVcodeQrcodeLib>> map = new HashMap<String, List<VpsVcodeQrcodeLib>>();
                    Map<String, List<VpsVcodeQrcodeLib>> mapWcode = new HashMap<String, List<VpsVcodeQrcodeLib>>();

                    // 码源错误记录数，如果超过10个认为本批文件异常
                    int errorCount = 0;
                    while (null != (lineTxt = bufferedReader.readLine())) {
                        if ("".equals(lineTxt.trim())) continue;

                        // 解析二维码内容
                        lineTxtAry = lineTxt.split(",");
                        qrcodeUrl = lineTxtAry[0].trim();
                        if (!qrcodeUrl.contains(projectFlag.toLowerCase())
                                && !qrcodeUrl.contains(projectFlag.toUpperCase())) {
                            errorCount++;
                            if (errorCount >= 10) {
                                difProjectMsg = "</br>" + fileName + ":" + count + "行</br>";
                                isValidFlag = false;
                                model.addAttribute("refresh", "import_fail");
                                break;
                            }
                        }
                        // 中粮五星 溯源码回传
                        if ("zhongLWX".equals(DbContextHolder.getDBType())) {
                            traceabilityURL = null;
                            if (lineTxtAry.length > 1 && lineTxtAry[lineTxtAry.length - 1].trim().startsWith("http")) {
                                traceabilityURL = lineTxtAry[lineTxtAry.length - 1].trim();
                            }
                        }
                        sweepstr = qrcodeUrl.substring(qrcodeUrl.lastIndexOf("/") + 1, qrcodeUrl.length());

                        sweepCodeMap = QrCodeUtil.analysisQrcode(sweepstr);

                        // Todo 最酒 溯源码回传 需要重构临时方案
                        if ("zuijiu".equals(DbContextHolder.getDBType())) {
                            traceabilityURL = null;
                            if (lineTxtAry.length > 1
                                    && (lineTxtAry[lineTxtAry.length - 1].trim().startsWith("HTTP") || lineTxtAry[lineTxtAry.length - 1].trim().startsWith("http"))
                            ) {
                                String codeUrl = lineTxtAry[lineTxtAry.length - 1].trim();
                                traceabilityURL = codeUrl.substring(codeUrl.lastIndexOf("/") + 1, codeUrl.length());
                                this.buildCodeLib(mapWcode,zuiJiuqrcodeList,codeUrl,sweepstr);
                            }
                        }

                        // 获取二维码
                        qrcode = sweepCodeMap.get("vcode");
                        //获取批次标识
                        vcodeUniqueCode = sweepCodeMap.get("activitycode");
                        if (StringUtils.isNotBlank(vcodeUniqueCode)) {
                            VpsVcodeQrcodeLib qrcodeLib = new VpsVcodeQrcodeLib();
                            if (!map.containsKey(vcodeUniqueCode)) {
                                qrcodeList = new ArrayList<VpsVcodeQrcodeLib>();
                                qrcodeLib.setQrcodeContent(qrcode);
                                qrcodeLib.setTraceabilityURL(traceabilityURL);
                                qrcodeList.add(qrcodeLib);
                                map.put(vcodeUniqueCode, qrcodeList);

                            } else {
                                qrcodeList = map.get(vcodeUniqueCode);
                                qrcodeLib.setQrcodeContent(qrcode);
                                qrcodeLib.setTraceabilityURL(traceabilityURL);
                                qrcodeList.add(qrcodeLib);

                            }
                            count++;
                        }

//						log.error("处理第" + count + "行");
                    }

                    // 关闭文件
                    read.close();

                    // 二维码入库
                    if (map.size() > 0 && isValidFlag) {
                        try {
                            Map<String, Object> insertResultMap = batchInfoService.batchInsertQrcode(
                                    map, batchInfo, companyKey, count, errorMsg, fileName, model);
                            if ("zuijiu".equals(DbContextHolder.getDBType()) && !ObjectUtils.isEmpty(mapWcode)) {
                                this.buildInCode(mapWcode, batchInfo, companyKey, count, errorMsg, fileName, model);
                            }
                            validCount = null != insertResultMap.get("validCount") ? (int) insertResultMap.get("validCount") : 0;
                            libNameList = null != insertResultMap.get("libNameList") ? (Set<String>) insertResultMap.get("libNameList") : new HashSet<String>();
                            // 导入成功
                            file.delete();
                            if (null != sbLog) {
                                sbLog.append("导入文件：").append(fileName).append(" 成功，执行").append(count).append("--").append(validCount).append("条").append("\n");
                            }
                            log.warn("导入文件：" + fileName + " 成功，执行" + count + "--" + validCount + "条");
                        } catch (Exception e) {
                            e.printStackTrace();
                            model.addAttribute("refresh", "import_fail");
                            if (e.getMessage().contains("doesn't exist")) {
                                errorMsg.append("</br>" + fileName + "(码库表不存在，请先创建后再次导入)");
                            } else if (e.getMessage().contains("QRCODECONTENT")) {
                                errorMsg.append("</br>" + fileName + "(二维码已经存在，请勿重复导入)");
                            } else {
                                errorMsg.append("</br>" + fileName + "(二维码导入失败)");
                                log.error(errorMsg + e.getMessage());
                            }
                        }
                    }
                } else {
                    // 找不到解压后的文件
                    errorMsg.append("</br>" + fileName + "(找不到解压后的文件)");
                    model.addAttribute("refresh", "import_fail");
                }
            } catch (Exception e) {
                errorMsg.append("</br>" + fileName + "(读取文件内容出错)");
                model.addAttribute("refresh", "import_fail");
                log.error(errorMsg.toString() + e.getMessage());
                e.printStackTrace();
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("errorMsg", errorMsg.toString());
        map.put("difProjectMsg", difProjectMsg);
        map.put("count", count);
        map.put("validCount", validCount);
        map.put("libNameList", libNameList);
        return map;
    }


    /**
     * 提取出兼容最酒逻辑 盖外码
     * @param zuiJiuqrcodeList
     * @param qrcodeUrl
     * @param traceabilityURL
     */
    private void buildCodeLib(Map<String, List<VpsVcodeQrcodeLib>> mapWcode,List<VpsVcodeQrcodeLib> zuiJiuqrcodeList,String qrcodeUrl,String traceabilityURL) {
        // 二维码
        String qrcode = null;
        String sweepstr = null;
        // 二维码标识（批次标识）
        String vcodeUniqueCode = null;
        // 解析后的二维码Map
        Map<String, String> sweepCodeMap = null;
        sweepstr = qrcodeUrl.substring(qrcodeUrl.lastIndexOf("/") + 1, qrcodeUrl.length());
        sweepCodeMap = QrCodeUtil.analysisQrcode(sweepstr);
        // 获取二维码
        qrcode = sweepCodeMap.get("vcode");
        //获取批次标识
        vcodeUniqueCode = sweepCodeMap.get("activitycode");

        if (StringUtils.isNotBlank(vcodeUniqueCode)) {
            VpsVcodeQrcodeLib qrcodeLib = new VpsVcodeQrcodeLib();
            if (!mapWcode.containsKey(vcodeUniqueCode)) {
                zuiJiuqrcodeList = new ArrayList<VpsVcodeQrcodeLib>();
                qrcodeLib.setQrcodeContent(qrcode);
                qrcodeLib.setTraceabilityURL(traceabilityURL);
                zuiJiuqrcodeList.add(qrcodeLib);
                mapWcode.put(vcodeUniqueCode, zuiJiuqrcodeList);

            } else {
                zuiJiuqrcodeList = mapWcode.get(vcodeUniqueCode);
                qrcodeLib.setQrcodeContent(qrcode);
                qrcodeLib.setTraceabilityURL(traceabilityURL);
                zuiJiuqrcodeList.add(qrcodeLib);
            }
        }
    }
    /**
     * 最酒插入内码
     * @param map
     * @param batchInfo
     * @param companyKey
     * @param count
     * @param errorMsg
     * @param fileName
     * @param model
     */
    private void buildInCode(Map<String, List<VpsVcodeQrcodeLib>> mapIn,
                             VcodeQrcodeBatchInfo batchInfo, String companyKey, int count, StringBuilder errorMsg, String fileName, Model model) {
        batchInfoService.batchInsertQrcode(
                mapIn, batchInfo, companyKey, count, errorMsg, fileName, model);
    }


    /**
     * 输出打印锁获取失败
     */
    public void outPrint(HttpServletResponse response, String label) {
        try (PrintWriter out = response.getWriter()) {
            //去除页面乱码
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=utf-8");
            String builder = "<script type=\'text/javascript\' charset=\'UTF-8\'>" +
                    "alert(\'" + label + "\');window.close();" +
                    "</script>";
            out.print(builder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    
//	/**
//	 * 下载文件格式
//	 * 
//	 * @return
//	 */
//	@RequestMapping("/downloadFileFormat")
//	@Deprecated
//	public String downloadFileFormat(HttpServletResponse response) {
//		response.setContentType("text/plain");
//		response.addHeader("Content-Disposition", "attachment;filename=format_"
//				+ DateUtil.getDateTime(DateUtil.DEFAULT_DATE_FORMAT_SHT) + ".txt");
//		OutputStream outStream = null;
//		try {
//
//			String enter = "\r\n";
//
//			StringBuilder writeDataBuilder = new StringBuilder("");
//			writeDataBuilder.append("QPGXJD0812201605010001").append(enter);
//			writeDataBuilder.append("QPGXJD0812201605010002").append(enter);
//			writeDataBuilder.append("QPGXJD0812201605010003").append(enter);
//			writeDataBuilder.append("QPGXJD0812201605010004").append(enter);
//			writeDataBuilder.append("QPGXJD0812201605010005").append(enter);
//
//			String writeData = writeDataBuilder.toString();
//			// 写TXT文件
//			outStream = response.getOutputStream();
//			if (!StringUtils.isEmpty(writeData)) {
//				ExportUtil.writeTxtFile(response.getOutputStream(), writeData);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (null != outStream) {
//					outStream.close();
//				}
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//		}
//		return null;
//	}
//
//	/**
//	 * 异步导入文件
//	 * 
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("/importFile")
//	@Deprecated
//	public String importFile(@RequestParam("filePath") MultipartFile clientFile,
//			HttpServletRequest request, HttpServletResponse response) {
//		Map<String, String> resultMap = new HashMap<String, String>();
//		InputStream inputStream = null;
//		BufferedReader bufferedReader = null;
//		try {
//			if (!clientFile.isEmpty()) {
//				String path = clientFile.getOriginalFilename();
//				if (path.endsWith(".txt")) {
//					
//					StringBuffer sbBuffer = new StringBuffer("");
//					inputStream = clientFile.getInputStream();
//					bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//					String line = null;
//					int count = 0;
//					while ((line = bufferedReader.readLine()) != null) {
//						sbBuffer.append(line).append(",");
//						count++;
//					}
//					resultMap.put("count", String.valueOf(count));
//					resultMap.put("packNumStr", sbBuffer.toString());
//				}else {
//					resultMap.put("count", "上传文件错误，请上传TXT文件");
//					resultMap.put("packNumStr", "");
//				}
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (bufferedReader != null) {
//					bufferedReader.close();
//					bufferedReader = null;
//				}
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//			try {
//				if (inputStream != null) {
//					inputStream.close();
//					inputStream = null;
//				}
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//
//		return JSON.toJSONString(resultMap);
//	}

}
