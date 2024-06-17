package com.dbt.platform.activity.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.dbt.datasource.util.DbContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.CheckUtil;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ExcelUtil;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.util.RandomUtils;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.util.SpellUtil;
import com.dbt.framework.util.UUIDTools;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.bean.VcodeActivityLibRelation;
import com.dbt.platform.activity.bean.VcodeQrcodeBatchInfo;
import com.dbt.platform.activity.bean.VcodeQrcodePackInfo;
import com.dbt.platform.activity.bean.VpsActivateBatchLog;
import com.dbt.platform.activity.bean.VpsVcodeQrcodeLib;
import com.dbt.platform.activity.dao.IVcodeActivityCogDao;
import com.dbt.platform.activity.dao.IVcodeActivityLibRelationDao;
import com.dbt.platform.activity.dao.IVcodeQrcodeBatchInfoDao;
import com.dbt.platform.activity.dao.IVcodeQrcodeLibDao;
import com.dbt.platform.activity.dao.IVcodeQrcodePackInfoDao;
import com.dbt.platform.autocode.bean.VpsQrcodeOrder;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 文件名：VcodeQrcodeBatchInfoService.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
 * 描述: V码二维码批次信息service<br>
 * 修改人: cpgu<br>
 * 修改时间：2016-04-22 10:30:15<br>
 * 修改内容：新增<br>
 */
@Service("vcodeQrcodeBatchInfoService")
public class VcodeQrcodeBatchInfoService extends BaseService<VcodeQrcodeBatchInfo>{
    
    private Logger log = Logger.getLogger(VcodeQrcodeBatchInfoService.class);
    
	@Autowired
	private IVcodeQrcodeBatchInfoDao batchInfoDao;
	@Autowired
	private IVcodeQrcodePackInfoDao packInfoDao;
	@Autowired
	private IVcodeActivityLibRelationDao libRelationDao;
	@Autowired
	private IVcodeActivityCogDao iVcodeActivityCogDao;
	@Autowired
	private VcodeActivityLibRelationServiceImpl vcodeActivityLibRelationService;
	@Autowired
	private IVcodeQrcodeLibDao vcodeQrcodeLibDao;
	@Autowired
    private VpsActivateBatchLogServiceImpl activateBatchLogService;
	@Autowired
	private SkuInfoService skuInfoService;
	
	/**
	 * 生成码源批次列表
	 */
	public List<VcodeQrcodeBatchInfo> queryForLst(VcodeQrcodeBatchInfo
	        queryBean, PageOrderInfo pageInfo) throws Exception {
	    Map<String, Object> queryMap = new HashMap<String, Object>();
	    queryMap.put("queryBean", queryBean);
	    queryMap.put("pageInfo", pageInfo);
	    
	    return batchInfoDao.queryForLst(queryMap);
	}
	
	/**
	 * 生成码源批次列表记录总条数
	 */
	public int queryForCount(VcodeQrcodeBatchInfo queryBean) {
	    Map<String, Object> queryMap = new HashMap<String, Object>();
	    queryMap.put("queryBean", queryBean);
	    return batchInfoDao.queryForCount(queryMap);
	}
	
	/**
	 * 回传码源批次列表
	 */
	public List<VcodeQrcodeBatchInfo> queryForImportLst(VcodeQrcodeBatchInfo
	        queryBean, PageOrderInfo pageInfo) throws Exception {
	    Map<String, Object> queryMap = new HashMap<String, Object>();
	    queryMap.put("queryBean", queryBean);
	    queryMap.put("pageInfo", pageInfo);
	    
	    return batchInfoDao.queryForImportLst(queryMap);
	}
	
	/**
	 * 回传码源批次列表记录总条数
	 */
	public int queryForImportCount(VcodeQrcodeBatchInfo queryBean) {
	    Map<String, Object> queryMap = new HashMap<String, Object>();
	    queryMap.put("queryBean", queryBean);
	    return batchInfoDao.queryForImportCount(queryMap);
	}
	
    /**
     * 回传已使用码源批次列表
     */
    public List<VcodeQrcodeBatchInfo> queryForImportUsedLst(VcodeQrcodeBatchInfo
                                    queryBean, PageOrderInfo pageInfo) throws Exception {
        
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("queryBean", queryBean);
        queryMap.put("pageInfo", pageInfo);
        
        return batchInfoDao.queryForImportUsedLst(queryMap);
    }

    /**
     * 回传已使用码源批次列表记录总条数
     */
    public int queryForImportUsedCount(VcodeQrcodeBatchInfo queryBean) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("queryBean", queryBean);
        return batchInfoDao.queryForImportUsedCount(queryMap);
    }
    
//  public List<VcodeQrcodeBatchInfo> queryForLst(
//          VcodeQrcodeBatchInfo vcodeQrcodeBatchInfo, PageOrderInfo pageOrderInfo) throws Exception {
//      Map<String, Object> queryMap = new HashMap<String, Object>();
//      queryMap.put("batchInfo", vcodeQrcodeBatchInfo);
//      queryMap.put("param", pageOrderInfo);
//      List<VcodeQrcodeBatchInfo> batchInfoList = this.iVcodeQrcodeBatchInfoDao
//              .queryQrcodeBatchInfoByList(queryMap);
//        if(null != batchInfoList && !batchInfoList.isEmpty()){
//            Object cacheValueStr = null;
//            String currDate = DateUtil.getDate();
//            VcodeQrcodeBatchInfo configItem = null;
//            String cacheStr = CacheKey.cacheKey.vcode.EKY_VPS_VCODE_QRCODE_BATCHINFO + Constant.DBTSPLIT;
//            for (VcodeQrcodeBatchInfo batchInfo : batchInfoList) {
//                cacheValueStr = CacheUtilNew.getCacheValue(cacheStr + batchInfo.getBatchKey());
//                if(null != cacheValueStr){
//                    configItem = JSON.parseObject(cacheValueStr.toString(), VcodeQrcodeBatchInfo.class);
//                    if(!batchInfo.getStartDate().equals(configItem.getStartDate())
//                            || !batchInfo.getEndDate().equals(configItem.getEndDate())){
//                        if (currDate.compareTo(configItem.getEndDate()) > 0) {
//                            batchInfo.setIsBegin("缓存异常(已结束)");
//                            
//                        } else if (currDate.compareTo(configItem.getStartDate()) < 0){
//                            batchInfo.setIsBegin("缓存异常(未开始)");
//                            
//                        } else {
//                            batchInfo.setIsBegin("缓存异常(进行中)");
//                        }
//                    }
//                }
//            }
//        }
//      return batchInfoList;
//  }
    
    /**
     * 根据id查询实体对象
     */
    public VcodeQrcodeBatchInfo findById(String batchKey) {
        return batchInfoDao.findById(batchKey);
    }
	
	 /**
	  * 创建码源批次
	  */
	public void addVcodeQrcodeBatchInfo(VcodeQrcodeBatchInfo batchInfo, String userKey) throws Exception {
	    
	    // 批次克隆份数
	    if (batchInfo.getBatchCopyNum() <= 0) batchInfo.setBatchCopyNum(1);
	    
	    // 每批次最多60万
	    if (batchInfo.getQrcodeAmounts() * batchInfo.getPackAmounts() > 600000) {
	        throw new BusinessException("单批次最大码源为60万个，建议最大50万！");
	    }
	    
	    // 批次编号及名称前缀
        int beginIndex = 1;
	    String batchDescPrefix = batchInfo.getBatchDesc();
	    String batchNamePrefix = batchInfo.getBatchName();
	    
	    // 截取批次编号索引
	    int batchDescLen = batchInfo.getBatchDesc().length();
	    if (batchDescLen > 3) {
	        String batchDescIndex = batchInfo.getBatchDesc().substring(batchDescLen - 3, batchDescLen);
	        if (CheckUtil.isNumeric(batchDescIndex)) {
	            beginIndex = Integer.valueOf(batchDescIndex);
	            batchDescPrefix = batchDescPrefix.substring(0, batchDescLen - 3);
	            batchNamePrefix = batchNamePrefix.substring(0, batchNamePrefix.length() - 3);
	        } 
	    }
	    
	    for (int i = beginIndex; i < beginIndex + batchInfo.getBatchCopyNum(); i++) {
	        
	        // 补充批次索引
	        batchInfo.setBatchDesc(batchDescPrefix + String.format("%03d", i));
	        batchInfo.setBatchName(batchNamePrefix + String.format("%03d", i));

	        // 校验批次编号
	        if (isExistActivityBatchDesc(batchInfo.getBatchDesc())) {
	            throw new BusinessException("批次编号：" + batchInfo.getBatchDesc() + "已存在");
	        }
	        
	        // 批次主表
	        String batchKey = UUIDTools.getInstance().getUUID();
	        batchInfo.setBatchKey(batchKey);
	        batchInfo.setVcodeFlag("0");
	        batchInfo.setImportFlag("0");
	        batchInfo.setStatus(Constant.batchStatus.status0);
	        batchInfo.fillFields(userKey);
	        batchInfoDao.create(batchInfo);
	        
	        // 创建批次相关的lib_relation及包记录
	        initRelationAndPackForBatch(batchInfo, null);
        }
        
        logService.saveLog("qrcodeBatchInfo", Constant.OPERATION_LOG_TYPE.TYPE_1,
                JSON.toJSONString(batchInfo), "创建批次:" + batchInfo.getBatchName() + " 复制批次个数" + batchInfo.getBatchCopyNum());

    }
	
	/**
	 * 创建批次相关的lib_relation及包记录
	 */
	public void initRelationAndPackForBatch(VcodeQrcodeBatchInfo batchInfo, VpsQrcodeOrder orderInfo) {
        // 批量保存二维码码包信息
        List<VcodeQrcodePackInfo> packInfoList = new ArrayList<VcodeQrcodePackInfo>();
        String libNameMiddle = SpellUtil.generateSequence(SpellUtil
                .getPinYinCapital(batchInfo.getBatchDesc()));
        for (int i = 1; i <= batchInfo.getPackAmounts().longValue(); i++) {
            VcodeQrcodePackInfo vcodeQrcodePackInfo = new VcodeQrcodePackInfo();
            vcodeQrcodePackInfo.fillFields(batchInfo.getCreateUser());
            vcodeQrcodePackInfo.setPackKey(UUIDTools.getInstance().getUUID());
            vcodeQrcodePackInfo.setBatchKey(batchInfo.getBatchKey());
            vcodeQrcodePackInfo.setQrcodeAmounts(batchInfo.getQrcodeAmounts());
            vcodeQrcodePackInfo.setPackCode(batchInfo.getBatchDesc());
            packInfoList.add(vcodeQrcodePackInfo);
        }
        packInfoDao.batchAddPackInfo(packInfoList);

        // 高印订单，按赋码工厂及SKU维度从redis缓存中获取上次的码源订单信息
        // 当大于50万或没有对应缓存时新创建关系表并放入缓存等后续订单使用
        if (orderInfo != null && "AUTO".equals(orderInfo.getCreateUser())) {
            // true共用码库表
            boolean shareLibRelationFlag = false;
            // 获取当前赋码工厂下当前SKU上次码库对应的批次
            String smallBatchParentKey = RedisApiUtil.getInstance().getHSet(RedisApiUtil.CacheKey
                    .mengniuGY.SMALL_ORDER + "smallBatchParentKey", orderInfo.getQrcodeFactoryName() + ":" + orderInfo.getSkuKey());
            if (StringUtils.isNotBlank(smallBatchParentKey)) {
                // 获取共享码库码源数量，如果加上当前申请量未大于50万则共享码库
                int smallOrderQrcodeAmount = Integer.valueOf(StringUtils.defaultIfBlank(RedisApiUtil.getInstance()
                        .getHSet(RedisApiUtil.CacheKey.mengniuGY.SMALL_ORDER + "smallOrderQrcodeAmount", smallBatchParentKey), "0"));
                if (smallOrderQrcodeAmount + batchInfo.getQrcodeAmounts() < 1000000) {
                    shareLibRelationFlag = true;
                    batchInfo.setSmallBatchParentKey(smallBatchParentKey); // 记录当前批次共享码库的批次
                    batchInfo.setBatchStartAutoNo(smallOrderQrcodeAmount + 1); // 记录当前批次的批次序号开始值
                } else {
                    smallBatchParentKey = null;
                }
            }
            
            // 设置共享码库的批次主键及码源数量
            smallBatchParentKey = StringUtils.defaultIfBlank(smallBatchParentKey, batchInfo.getBatchKey());
            RedisApiUtil.getInstance().setHSet(RedisApiUtil.CacheKey.mengniuGY.SMALL_ORDER 
                    + "smallBatchParentKey", orderInfo.getQrcodeFactoryName() + ":" + orderInfo.getSkuKey(), smallBatchParentKey);
            RedisApiUtil.getInstance().setHincrBySet(RedisApiUtil.CacheKey.mengniuGY.SMALL_ORDER 
                    + "smallOrderQrcodeAmount", smallBatchParentKey, batchInfo.getQrcodeAmounts());
            
            // 如果共用码源表，则不用创建新的lib_relation
            if (shareLibRelationFlag) return;
        }
        
        // 保存V码活动活动与码库关联
        long qrcodeAmounts = batchInfo.getQrcodeAmounts()
                * batchInfo.getPackAmounts();// 二维码数量
        List<VcodeActivityLibRelation> activityLibRelationList = new ArrayList<VcodeActivityLibRelation>();
        long record = getRecordCont(qrcodeAmounts);// 本次V码活动活动与码库记录数
        
        // 本地活动标识list
        List<String> codeList = libRelationDao.queryAllCodeList();
        List<String> libNameList = libRelationDao.queryAllLibNameList();
        
        for (int i = 1; i <= record; i++) {
            VcodeActivityLibRelation vcodeActivityLibRelation = new VcodeActivityLibRelation();
            vcodeActivityLibRelation.setInfoKey(UUIDTools.getInstance().getUUID());
            vcodeActivityLibRelation.setVcodeActivityKey(batchInfo
                    .getVcodeActivityKey());
            vcodeActivityLibRelation.setBatchKey(batchInfo.getBatchKey());
            vcodeActivityLibRelation.setVcodeUniqueCode(getUniqueCode(codeList));
            vcodeActivityLibRelation.setLibName(getUniqueLibName(libNameList, libNameMiddle, i));
            vcodeActivityLibRelation.setVcodeCounts(getVcodeCounts(i, record, qrcodeAmounts));
            activityLibRelationList.add(vcodeActivityLibRelation);
        }
        libRelationDao.batchAddActivityLibRelation(activityLibRelationList);
	}

	/**
	 * 修改码源批次
	 */
	public void updateVcodeQrcodeBatchInfo(VcodeQrcodeBatchInfo batchInfo) throws Exception {
	    VcodeQrcodeBatchInfo oldBatchInfo = batchInfoDao.findById(batchInfo.getBatchKey());
	    if (StringUtils.isNotBlank(oldBatchInfo.getVcodeActivityKey())
	            || StringUtils.isNotBlank(oldBatchInfo.getOrderKey())
	            || StringUtils.isNotBlank(oldBatchInfo.getStartDate())
	            || "1".equals(oldBatchInfo.getVcodeFlag())) {
	        throw new Exception("批次状态异常，不能修改！");
	    }
	    
        // 每批次最多60万
        if (batchInfo.getQrcodeAmounts() * batchInfo.getPackAmounts() > 600000) {
            throw new BusinessException("单批次最大码源为60万个，建议最大50万！");
        }
	    
	    // 更新主表信息
        batchInfoDao.update(batchInfo);
        
        // 如果码源数量发生变化
        if (batchInfo.getQrcodeAmounts() != oldBatchInfo.getQrcodeAmounts()
                || batchInfo.getPackAmounts() != oldBatchInfo.getPackAmounts()) {
            
            // 先删除相关的lib_relation及包记录
            libRelationDao.removeByBatchKey(batchInfo.getBatchKey());
            packInfoDao.removeByBatchKey(batchInfo.getBatchKey());
            
            // 创建批次相关的lib_relation及包记录
            initRelationAndPackForBatch(batchInfo, null);
        }
	    
	    logService.saveLog("qrcodeBatchInfo", Constant.OPERATION_LOG_TYPE.TYPE_2,
	                    JSON.toJSONString(batchInfo), "修改批次:" + batchInfo.getBatchName());
	}
	
	/**
	 * 删除生成码源记录
	 */
	public void deleteBatchInfoById(String batchKey, String optUserKey) throws Exception {
	    String[] batchKeyAry = batchKey.split(",");
	    VcodeQrcodeBatchInfo batchInfo = null;
	    for (String itemBatchKey : batchKeyAry) {
	        batchInfo = batchInfoDao.findById(itemBatchKey);
	        if (StringUtils.isNotBlank(batchInfo.getVcodeActivityKey())
	                || StringUtils.isNotBlank(batchInfo.getOrderKey())
	                || StringUtils.isNotBlank(batchInfo.getStartDate())
	                || "1".equals(batchInfo.getVcodeFlag())) {
	            throw new Exception(batchInfo.getBatchDesc() + "批次状态异常，不能删除！");
	        }
	        
	        batchInfoDao.removeById(itemBatchKey);
	        libRelationDao.removeByBatchKey(itemBatchKey);
	        
	        logService.saveLog("qrcodeBatchInfo", Constant.OPERATION_LOG_TYPE.TYPE_3,
	                JSON.toJSONString(batchInfo), "删除批次:" + batchInfo.getBatchName());
        }
	}
	
	/**
	 * 依据批次主键获取批次
	 */
	public List<VcodeQrcodeBatchInfo> queryBatchInfoByKey(List<String> batchKeyList) {
	    List<VcodeQrcodeBatchInfo> batchInfoLst = new ArrayList<>();
	    if (batchKeyList != null && !batchKeyList.isEmpty()) {
	        batchInfoLst = batchInfoDao.queryBatchInfoByKey(batchKeyList);
	    }
		return batchInfoLst;
	}
	
	/**
	 * 校验要创建码源订单的批次状态
	 * 
	 * @return map keys:clientOrderNo、batchNameTips
	 */
	public Map<String, Object> checkBatchInfoForQrcodeOrder(String batchKeys) throws BusinessException {
        
        // 批次主键集合
        List<String> batchKeyList = new ArrayList<>(Arrays.asList(
                            StringUtils.defaultIfBlank(batchKeys, "").split(",")));
        List<VcodeQrcodeBatchInfo> batchInfoList = queryBatchInfoByKey(batchKeyList);
        
        // 组建批次名称
        StringBuffer buffer = new StringBuffer("");
        String clientOrderNo = "";
        for (VcodeQrcodeBatchInfo item : batchInfoList) {
            buffer.append(item.getBatchName()).append("</br>");
            if(StringUtils.isNotBlank(item.getOrderKey()) || "1".equals(item.getVcodeFlag())){
                throw new BusinessException(item.getBatchName() + "已存在其他订单中或已生成过码源，无法创建码源订单");
            }
            
            if (StringUtils.isNotBlank(item.getVcodeActivityKey())
                    || StringUtils.isNotBlank(item.getOrderKey())
                    || StringUtils.isNotBlank(item.getStartDate())
                    || "1".equals(item.getVcodeFlag())) {
                throw new BusinessException(item.getBatchName() + "批次状态异常，无法创建码源订单");
            }
            
            if (StringUtils.isBlank(clientOrderNo)) {
                clientOrderNo = item.getClientOrderNo();
            } else if (!clientOrderNo.equals(item.getClientOrderNo())){
                throw new BusinessException(item.getClientOrderNo() + "客户订单号与其它不一致，无法创建码源订单");
            }
        }
        
        if (buffer.length() > 0) {
            buffer.insert(0, "您确定要创建码源订单吗? </br>");
            buffer.subSequence(0, buffer.length() -1);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("clientOrderNo", clientOrderNo);
        map.put("batchNameTips", buffer.toString());
        return map;
	}

	

	private String getUniqueCode(List<String> codeList) {
		if(codeList==null){
			codeList= new ArrayList<String>();
		}
		//和基本串码规则保持一致
		String data = "23456789ABCDEFGHJKMNPQRSTUVWXYZ";
		String codeStr = RandomUtils.randomString(data, 3);
		if (codeList.contains(codeStr)) {
			codeStr = getUniqueCode(codeList);
		}
		codeList.add(codeStr);
		return codeStr;
	}
	
	/**
	 * 获取唯一的码库名称
	 * @return
	 */
	private String getUniqueLibName(List<String> libNameLst, String libNameMiddle, int i) {
	    
	    String libName;
	    if (i == 0) {
	        libName = libNameMiddle;
	    } else {
	        libName = Constant.Vcode_qrcode_batchInfo
	                .LIB_NAME_PREFIX+ libNameMiddle + Constant.DBTSPLIT + i;
	    }
	    
	    if (libNameLst.contains(libName)) {
	        libName = libName + Constant.DBTSPLIT + RandomStringUtils.randomAlphanumeric(3);
	        libName = getUniqueLibName(libNameLst, libName, 0);
	    }
	    libNameLst.add(libName);
	    return libName;
	}

	/**
	 * 得到V码数量
	 * 
	 * @param i
	 *            循环变量
	 * @param record
	 *            总记录数
	 * @param qrcodeAmounts
	 *            总V码数
	 * @return long 得到V码数量
	 */
	private long getVcodeCounts(int i, long record, long qrcodeAmounts) {
		if (Constant.Vcode_qrcode_batchInfo.QRCODE_NUM_LIMIT >= qrcodeAmounts) {
			return qrcodeAmounts;
		}
		if (record == i) {
			return qrcodeAmounts - (record - 1) * Constant.Vcode_qrcode_batchInfo.QRCODE_NUM_LIMIT;
		}
		return Constant.Vcode_qrcode_batchInfo.QRCODE_NUM_LIMIT;
	}

	private long getRecordCont(long qrcodeAmounts) {
		if (Constant.Vcode_qrcode_batchInfo.QRCODE_NUM_LIMIT >= qrcodeAmounts) {
			return 1;
		}

		long record;
		if (qrcodeAmounts % Constant.Vcode_qrcode_batchInfo.QRCODE_NUM_LIMIT == 0) {
			record = qrcodeAmounts / Constant.Vcode_qrcode_batchInfo.QRCODE_NUM_LIMIT;
		} else {
			record = qrcodeAmounts / Constant.Vcode_qrcode_batchInfo.QRCODE_NUM_LIMIT + 1;
		}
		return record;
	}

	/**
	 * 根据活动key获取批次列表
	 * @param vcodeActivityKey
	 * @return
	 */
	public List<VcodeQrcodeBatchInfo> findVcodeQrcodeBatchInfoByActivityId(
			String vcodeActivityKey) {
		return batchInfoDao.findVcodeQrcodeBatchInfoByActivityId(vcodeActivityKey);
	}

	/**
	 * 判断批码说明是否存在
	 * @param batchDesc</br> 
	 * @return String </br>
	 */
	public boolean isExistActivityBatchDesc(String batchDesc) {
		return batchInfoDao.isExistActivityBatchDesc(batchDesc) != 0;
	}
    
    /**
     * 初始化批次有效期修改
     * @param batchKey
     * @return
     */
    public Map<String, Object> initBatchValidDateUpdate(String batchKey) {
        Map<String, Object> map = new HashMap<>();
        VcodeQrcodeBatchInfo batchInfo = findById(batchKey);
        map.put("batchKey", batchInfo.getBatchKey());
        map.put("clientOrderNo", batchInfo.getClientOrderNo());
        map.put("batchName", batchInfo.getBatchName());
        map.put("startDate", batchInfo.getStartDate());
        map.put("endDate", batchInfo.getEndDate());
        
        VcodeActivityCog activityCog = iVcodeActivityCogDao.findById(batchInfo.getVcodeActivityKey());
        map.put("activityName", activityCog.getVcodeActivityName());
        map.put("activityStartDate", activityCog.getStartDate());
        map.put("activityEndDate", activityCog.getEndDate());
        String currDate = DateUtil.getDate();
        if (currDate.compareTo(activityCog.getStartDate()) < 0) {
            map.put("activityName", map.get("activityName") + "(待上线)");
        } else if (currDate.compareTo(activityCog.getEndDate()) > 0) {
            map.put("activityName", map.get("activityName") + "(已下线)");
        } else {
            map.put("activityName", map.get("activityName") + "(已上线)");
        }
        
        if (!(StringUtils.isBlank(batchInfo.getStartDate())
                && StringUtils.isNotBlank(batchInfo.getEndDate()))) {
            SkuInfo skuInfo = skuInfoService.findById(batchInfo.getSkuKey());
            map.put("validDays", skuInfo.getValidDay());
        }
        
        map.put("diffDays", 0);
        if (StringUtils.isNotBlank(batchInfo.getStartDate()) 
                && StringUtils.isNotBlank(batchInfo.getEndDate())) {
            try {
                map.put("diffDays", DateUtil.diffDays(batchInfo.getEndDate(), batchInfo.getStartDate()) + 1);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        
        return map;
    }
    
    /**
     * 修改码源批次
     */
    public void updateBatchValidDate(VcodeQrcodeBatchInfo 
            vcodeQrcodeBatchInfo, String crrrentUserkey, String loginPhoneNum) throws Exception {
        String batchKey = vcodeQrcodeBatchInfo.getBatchKey();
        VcodeQrcodeBatchInfo oldVcodeQrcodeBatchInfo = batchInfoDao.findById(batchKey);
        
        // 如果修改批次的时间，则同步该批次下包的时间
        String newStartDate = vcodeQrcodeBatchInfo.getStartDate();
        String newEndDate = vcodeQrcodeBatchInfo.getEndDate();
        if(!newStartDate.equals(oldVcodeQrcodeBatchInfo.getStartDate())
                || !newEndDate.equals(oldVcodeQrcodeBatchInfo.getEndDate())){
            String updateTime = DateUtil.getDateTime();
            
            // 处理包开始结束时间
            List<VcodeQrcodePackInfo> packList = packInfoDao.findPackListByBatchKey(batchKey);
            if (CollectionUtils.isNotEmpty(packList)){
				VcodeQrcodePackInfo vcodeQrcodePackInfo = new VcodeQrcodePackInfo();
				vcodeQrcodePackInfo.setStartDate(newStartDate);
				vcodeQrcodePackInfo.setEndDate(newEndDate);
				vcodeQrcodePackInfo.setUpdateTime(updateTime);
				vcodeQrcodePackInfo.setUpdateUser(crrrentUserkey);
				vcodeQrcodePackInfo.setBatchKey(packList.get(0).getBatchKey());
				packInfoDao.batchUpdatePackInfoByPackKey(vcodeQrcodePackInfo);
			}

            // 如果批次被激活，则更新当前登录手机号为激活人
            vcodeQrcodeBatchInfo.setActivatePhone(loginPhoneNum);
            vcodeQrcodeBatchInfo.setActivateTime(DateUtil.getDateTime());
            vcodeQrcodeBatchInfo.setActivateUserName("运营管理平台");
            vcodeQrcodeBatchInfo.setActivateFactoryName("运营管理平台");
            log.warn(loginPhoneNum + "变更激活信息，变更前为" + JSON.toJSONString(oldVcodeQrcodeBatchInfo));
            
            // 添加激活日志
            VpsActivateBatchLog activateBatchLog = new VpsActivateBatchLog(
                    vcodeQrcodeBatchInfo.getBatchKey(), oldVcodeQrcodeBatchInfo.getBatchDesc(),
                    vcodeQrcodeBatchInfo.getActivateOpenid(), vcodeQrcodeBatchInfo.getActivateUserName(),
                    vcodeQrcodeBatchInfo.getActivatePhone(), vcodeQrcodeBatchInfo.getActivateFactoryName(),
                    vcodeQrcodeBatchInfo.getActivateTime(), newStartDate, newEndDate);
            activateBatchLogService.create(activateBatchLog);
            
        }
        
        // 批次所属合同年份
        if (StringUtils.isBlank(oldVcodeQrcodeBatchInfo.getContractYear())) {
            vcodeQrcodeBatchInfo.setContractYear(vcodeQrcodeBatchInfo.getStartDate().substring(0, 4));
        }
        
        vcodeQrcodeBatchInfo.fillUpdateFields(crrrentUserkey);
        this.batchInfoDao.update(vcodeQrcodeBatchInfo);
        
        // 删除批次缓存
        CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode.EKY_VPS_VCODE_QRCODE_BATCHINFO
                + Constant.DBTSPLIT + vcodeQrcodeBatchInfo.getBatchKey() + Constant.DBTSPLIT + DateUtil.getDate("yyyyMMdd"));
        
        logService.saveLog("qrcodeBatchInfo", Constant.OPERATION_LOG_TYPE.TYPE_2,
                JSON.toJSONString(vcodeQrcodeBatchInfo), "修改批次:" + vcodeQrcodeBatchInfo.getBatchName());
    }
    
    /**
     * 批次调整
     * @param batchKey</br>
     * @param batchName</br> 
     * @param vcodeActivityKey</br> 
     * @param oldVcodeActivityKey</br>  
     * @return String </br>
     * @throws Exception 
     */
    public void modifyAdjustbatch(String batchKey, String batchName, String endDate, 
            String vcodeActivityKey, String loginPhoneNum, String optUserKey) throws Exception {
        // 1.更新批次表对应的活动KEY及批次开始及结束时间
        List<String> batchKeyList = new ArrayList<>(Arrays.asList(
                StringUtils.defaultIfBlank(batchKey, "").split(",")));
        List<VcodeQrcodeBatchInfo> tempLst = queryBatchInfoByKey(batchKeyList);
        for (VcodeQrcodeBatchInfo item : tempLst) {
            if (StringUtils.isNotBlank(item.getVcodeActivityKey()) 
                    && !item.getVcodeActivityKey().equals(vcodeActivityKey)) {
                throw new BusinessException(item.getBatchDesc() + "已调整到别的活动!");
            }
        }
        
        VcodeQrcodeBatchInfo batchInfo = new VcodeQrcodeBatchInfo();
        if (batchKeyList.size() == 1 && StringUtils.isNotBlank(batchName)) {
            batchInfo.setBatchName(batchName);
        }
        batchInfo.setVcodeActivityKey(vcodeActivityKey);
        batchInfo.setEndDate(endDate);
        batchInfo.setAdjustUser(loginPhoneNum);
        batchInfo.setUpdateTime(DateUtil.getDateTime());
        Map<String, Object> map = new HashMap<>();
        map.put("batchKeyList", batchKeyList);
        map.put("batchInfo", batchInfo);
        map.put("optUserKey", optUserKey);
        
        batchInfoDao.updateBatchForAdjust(map);
        
        // 删除批次缓存
        if(batchKeyList.size() > 0){
            for (String key : batchKeyList) {
                CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode.EKY_VPS_VCODE_QRCODE_BATCHINFO 
                        + Constant.DBTSPLIT + key + Constant.DBTSPLIT + DateUtil.getDate("yyyyMMdd"));
            }
        }
        
        logService.saveLog("qrcodeBatchInfo", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(map), "批次调整");
    }
    
    /**
     * 修改批次所属合同年份
     */
    public void modifyContractYear(List<String> batchKeyLst, String contractYear,
                    String contractChangeDate, String nextContractYear, SysUserBasis currentUser) throws Exception {
        if (CollectionUtils.isEmpty(batchKeyLst)) {
            throw new BusinessException("批次主键为空");
        }
        
        Map<String, Object> map = new HashMap<>();
        map.put("batchKeyLst", batchKeyLst);
        map.put("contractYear", contractYear);
        map.put("contractChangeDate", StringUtils.defaultIfBlank(contractChangeDate, null));
        map.put("nextContractYear", nextContractYear);
        map.put("optUserKey", currentUser.getUserKey());
        
        batchInfoDao.updateContractYear(map);
        
        logService.saveLog("qrcodeBatchInfo", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(map), "修改批次所属合同年份");
    }

	/**
	 * 移除活动下的批次到码源回传入库
	 */
	public void updateBatchBackForImport(String batchKey, String optUserKey, String loginPhoneNum) throws Exception {
	    
	    // 批次主键信息
        List<String> batchKeyList = new ArrayList<>(Arrays.asList(
                            StringUtils.defaultIfBlank(batchKey, "").split(",")));
        
        // 组建操作日志
	    Map<String, Object> logMap = new HashMap<>();
	    logMap.clear();
	    logMap.put("optUser", loginPhoneNum);
        List<VcodeQrcodeBatchInfo> batchInfoLsg = queryBatchInfoByKey(batchKeyList);
        StringBuffer buffer = new StringBuffer();
        for (VcodeQrcodeBatchInfo item : batchInfoLsg) {
            logMap.put("vcodeActivityKey", item.getVcodeActivityKey());
            buffer.append("batchKey:").append(item.getBatchKey()).append(",");
            buffer.append("startDate:").append(item.getStartDate()).append(",");
            buffer.append("endDate:").append(item.getEndDate()).append(",");
            buffer.append("adjust_user:").append(item.getAdjustUser()).append(",");
            buffer.append("adjust_time:").append(item.getAdjustTime()).append(";");
            
            // 已开始或结束批次不能移出
            if (StringUtils.isNotBlank(item.getStartDate()) 
                    && DateUtil.getDate().compareTo(item.getStartDate()) >= 0) {
                throw new BusinessException("移出失败，移出批次中包含已开始或结束的批次");
            }
        }
        logMap.put("batchInfo", buffer.toString());
	    
        // 移出批次
		Map<String, Object> map = new HashMap<>();
		map.put("batchKeyList", batchKeyList);
		map.put("optUserKey", optUserKey);
		batchInfoDao.updateBatchBackForImport(map);
		
		// 删除批次缓存
		if(batchKeyList.size() > 0){
			for (String key : batchKeyList) {
				CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode.EKY_VPS_VCODE_QRCODE_BATCHINFO
				            + Constant.DBTSPLIT + key + Constant.DBTSPLIT + DateUtil.getDate("yyyyMMdd"));
			}
		}
		
		// 持久化操作日志
        logService.saveLog("qrcodeBatchInfo", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(logMap), "活动下批次退出码源回传入库");
	}

	/**
	 * 码源回传时批量创建批次
	 * @param orderNo 码源订单
	 * @param fileNameList 码包文件名称列表</br>
	 * @return void </br>
	 */
	public String batchVcodeQrcodeBatchInfo(VpsQrcodeOrder qrcodeOrder, List<String> fileNameList, String userKey) {
		String batchNo = null;
		String batchDesc = null;
		String batchName = null;
		String time = DateUtil.getDateTime();
		Set<String> batchNoList = new HashSet<>();
		List<VcodeQrcodeBatchInfo> batchInfoList = new ArrayList<VcodeQrcodeBatchInfo>();
		for (String fileName : fileNameList) {
			String[] fileNameGroup = fileName.substring(0, fileName.lastIndexOf(".")).split("-");
			if(fileNameGroup.length == 3){
				batchNo = fileNameGroup[0];
				batchDesc = fileNameGroup[2];
				batchName = fileNameGroup[2];
			}else{
				batchNo = fileNameGroup[2];
				batchDesc = fileNameGroup[3];
				batchName = fileNameGroup[3];
			}
			batchNoList.add(batchNo);
			VcodeQrcodeBatchInfo batchInfo = new VcodeQrcodeBatchInfo();
			batchInfo.setBatchKey(UUID.randomUUID().toString());
			batchInfo.setCompanyKey(qrcodeOrder.getCompanyKey());
			batchInfo.setClientOrderNo(qrcodeOrder.getClientOrderNo());
			batchInfo.setOrderKey(qrcodeOrder.getOrderKey());
			batchInfo.setSkuKey(qrcodeOrder.getSkuKey());
			batchInfo.setBatchNo(batchNo);
			batchInfo.setBatchDesc(batchDesc);
			batchInfo.setBatchName(batchName);
			batchInfo.setPackAmounts(1L);
			batchInfo.setVcodeFlag("1");
			batchInfo.setFileName(fileName);
			batchInfo.setStatus(Constant.batchStatus.status2);
			batchInfo.setDeleteFlag("0");
			batchInfo.fillFields(userKey);
			batchInfoList.add(batchInfo);
		}
		
		if(batchInfoList.size() > 0){
			batchInfoDao.batchAddBatchInfo(batchInfoList);
		}
		return !CollectionUtils.isEmpty(batchNoList) ? StringUtils.join(batchNoList.toArray(new String[batchNoList.size()]),",") : "";
	}
	
	/**
	 * 根据批码编号查询批次信息
	 * @param batchDesc 批次编号</br> 
	 * @return VcodeQrcodeBatchInfo </br>
	 */
	public VcodeQrcodeBatchInfo findVcodeQrcodeBatchInfoByBatchDesc(String batchDesc) {
		return batchInfoDao.findVcodeQrcodeBatchInfoByBatchDesc(batchDesc);
	}

	/**
	 * 二维码入库
	 * @param </br> 
	 * @return void </br>
	 */
	public Map<String, Object> batchInsertQrcode(Map<String, List<VpsVcodeQrcodeLib>> map,
			VcodeQrcodeBatchInfo batchInfo, String companyKey, int count, StringBuilder errorMsg, String fileName, Model model) {
		BufferedWriter writer = null;
		String createTime = DateUtil.getDateTime();
		File file = null;
		String batchNo = null;
		Set<String> libNameList = new HashSet<String>();
    	if(fileName.split("-").length == 3){
    		batchNo = fileName.substring(0, fileName.lastIndexOf(".")).split("-")[0];
    	}else{
    		batchNo = fileName.substring(0, fileName.lastIndexOf(".")).split("-")[2];
    	}
		
		try{
			for (String uniqueCode : map.keySet()) {
				// 码库信息
				VcodeActivityLibRelation libRelation = 
						vcodeActivityLibRelationService.findLibRelationByUniqueCode(uniqueCode);
				if (libRelation == null) continue;
				String libName = libRelation.getLibName();
				libNameList.add(libName);
				List<VpsVcodeQrcodeLib>  qrcodeList = map.get(uniqueCode);
				if(null != qrcodeList && !qrcodeList.isEmpty()){
					Map<String,Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put("libName", libName);
					paramsMap.put("batchKey", batchInfo.getBatchKey());
					paramsMap.put("companyKey", companyKey);
					paramsMap.put("list", qrcodeList);
					paramsMap.put("createTime", createTime);
					
					try{
						vcodeQrcodeLibDao.batchInsertQrcode(paramsMap);
					}catch(Exception e){
						if(e.getMessage().contains("QRCODECONTENT")){
	            			for (VpsVcodeQrcodeLib qrcode : qrcodeList) {
	            				paramsMap.clear();
	            				paramsMap.put("libName", libName);
	            				paramsMap.put("batchKey", batchInfo.getBatchKey());
	            				paramsMap.put("companyKey", companyKey);
	            				paramsMap.put("qrcodeContent", qrcode.getQrcodeContent());
								paramsMap.put("qrcodeType", Optional.ofNullable(qrcode.getQrcodeType()).orElse("0"));
	            				paramsMap.put("createTime", createTime);
	            				try{
	            					vcodeQrcodeLibDao.insertQrcode(paramsMap);
	            				}catch(Exception e2){
	            					count--;
	            					if(e2.getMessage().contains("QRCODECONTENT")){
	            						// 重复导入处理
	            						if(null == file){
	            							file = new File("/home/importQrcodeLog/" +DateUtil.getDate()+ "/" + batchNo + "/" + fileName);
	            							if(!file.getParentFile().exists()){
		            							file.getParentFile().mkdirs();
		            						}
	            						}
	            						
	            						if(null == writer){
	            							writer = new BufferedWriter(new FileWriter(file,true));
	            						}
	            						writer.write(uniqueCode + qrcode.getQrcodeContent());
	            						writer.newLine();
	            					}else{
	            						// 其他异常
	            						errorMsg.append("</br>" + fileName + "该二维码" + qrcode.getQrcodeContent() + "导入异常!" + e2.getMessage());
	            					}
	            				}
							}
						}else{
    						// 其他异常
    						errorMsg.append("</br>" + fileName + "批量导入异常!" + e.getMessage());
    					}
					}
					
					// 修改批次的状态为已导入
					batchInfo.setImportFlag(Constant.ImportFlag.ImportFlag_1);
					batchInfo.setImportTime(DateUtil.getDateTime());
					batchInfo.setStatus(Constant.batchStatus.status3);
					batchInfo.setQrcodeAmounts((long)count);
					batchInfoDao.update(batchInfo);
					
					 // 删除批次缓存
			        CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode.EKY_VPS_VCODE_QRCODE_BATCHINFO 
			                + Constant.DBTSPLIT + batchInfo.getBatchKey() + Constant.DBTSPLIT + DateUtil.getDate("yyyyMMdd"));
				}
			}
		}catch(Exception e3){
			e3.printStackTrace();
		}finally{
			if(writer != null){
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Map<String, Object> returnMap =new HashMap<>();
		returnMap.put("validCount", count);
		returnMap.put("libNameList", libNameList);
		return returnMap;
	}

	/**
	 * 最酒更新码库
	 * @param map
	 */
	private void updateCodeLib(Map<String,String> map) {

	}

	/**
	 * 删除批次
	 * @param </br> 
	 * @return void </br>
	 */
	public void deleteBatchInfo(VcodeQrcodeBatchInfo batchInfo) throws Exception {
		String batchKey = batchInfo.getBatchKey();
		batchInfoDao.deleteById(batchKey);
		
		// 删除批次对应的码包文件
		 String path = PropertiesUtil.getPropertyValue("qrcode_saveQrcodeFilePath");
		 File file = new File(path + File.separator + batchInfo.getFileName());
		 if(file.exists()){
			 file.delete();
		 }
		 
		// 删除批次缓存
       CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode.EKY_VPS_VCODE_QRCODE_BATCHINFO 
                   + Constant.DBTSPLIT + batchKey + Constant.DBTSPLIT + DateUtil.getDate("yyyyMMdd"));
       
       logService.saveLog("qrcodeBatchInfo", Constant.OPERATION_LOG_TYPE.TYPE_3, "batckKey:" + batchKey, "批次删除");
	}

	/**
	 * 查询激活批次List
	 * @param queryBean
	 * @param pageInfo
	 * @return
	 */
	public List<VcodeQrcodeBatchInfo> queryActivateBatchList(
	                        VcodeQrcodeBatchInfo queryBean, PageOrderInfo pageInfo) {
		Map<String, Object> map = new HashMap<>();
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		return batchInfoDao.queryActivateBatchList(map);
	}

	/**
	 * 查询激活批次Count
	 * @param queryBean
	 * @return
	 */
	public int queryActivateBatchCount(VcodeQrcodeBatchInfo queryBean) {
		Map<String, Object> map = new HashMap<>();
		map.put("queryBean", queryBean);
		return batchInfoDao.queryActivateBatchCount(map);
	}

	/**
	 * 导出激活批次
	 * @param queryBean
	 * @throws IOException 
	 */
	public void exportActivateBatchList(VcodeQrcodeBatchInfo queryBean, PageOrderInfo pageInfo, HttpServletResponse response) 
			throws IOException {
		// 获取导出信息
	    pageInfo.setPagePerCount(0);
		List<VcodeQrcodeBatchInfo> batchInfoList = queryActivateBatchList(queryBean, pageInfo);
		OutputStream outStream = response.getOutputStream();
		
		String[] headers = {"产品名称", "辅料箱码/垛码", "该箱/垛码数量", "激活工厂", "激活人员", "手机号", "激活时间"};
		String[] valueTags = {"skuName", "batchDesc", "vcodeCounts", "activateFactoryName", "activateUserName", "activatePhone", "activateTime"};
		ExcelUtil<VcodeQrcodeBatchInfo> excel = new ExcelUtil<VcodeQrcodeBatchInfo>(); 
		excel.writeExcel("激活批次列表", headers, valueTags, batchInfoList, DateUtil.DEFAULT_DATE_FORMAT, outStream);
		outStream.close();
	}

	/**
	 * 查询成功导入的二维码数量
	 * @param libNameList
	 * @return
	 */
	public int querySuccessImportQrcodeCount(Set<String> libNameList) {
		Map<String, Object> map = new HashMap<>();
		map.put("libNameList", libNameList);
		return vcodeQrcodeLibDao.querySuccessImportQrcodeCount(map);
	}

	/**
	 * 根据订单号修改批次
	 * @param
	 */
	public List<VcodeQrcodeBatchInfo> queryBatchExpireRemind() {
	    return batchInfoDao.queryBatchExpireRemind();
	}
	
	/**
     * 蒙牛高印更新批次状态
     * 
     * @param batchInfo
     */
    public void updateForMN(VcodeQrcodeBatchInfo batchInfo) {
        batchInfoDao.updateForMN(batchInfo);
    }

	/**
	 * 根据二维码内容和表名查询码库表对象
	 * @param libName
	 * @param qrcodeContent
	 * @return
	 */
	public VpsVcodeQrcodeLib queryQrcodeLibByQrcodeContent(String libName,String qrcodeContent) {
		if(StringUtils.isBlank(libName) || StringUtils.isBlank(qrcodeContent)){
			return null;
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("libName", libName);
		map.put("qrcodeContent", qrcodeContent);
		return vcodeQrcodeLibDao.queryVcodeQrcodeLibByQrcodeContent(map);
	}
	/**
	 * 根据码库表名称，条件为已扫过，时间倒叙，取第一条记录
	 * @param libName
	 * @return
	 */
	public VpsVcodeQrcodeLib queryVcodeQrcodeLibByIsFinishAndTimeDescAndFirst(String libName) {
		if(StringUtils.isBlank(libName)){
			return null;
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("libName", libName);
		return vcodeQrcodeLibDao.queryVcodeQrcodeLibByIsFinishAndTimeDescAndFirst(map);
	}

	/**
	 * 根据批次key查询批次活动
	 *
	 * @param batchKey
	 * @return VcodeQrcodeBatchInfo
	 */
	public VcodeQrcodeBatchInfo queryVcodeQrcodeBatchByBatchKey(String batchKey) {
		String cacheStr = CacheKey.cacheKey.vcode.EKY_VPS_VCODE_QRCODE_BATCHINFO
				+ Constant.DBTSPLIT + batchKey + Constant.DBTSPLIT + DateUtil.getDate("yyyyMMdd");
		Object cacheObject = null;
		try {
			cacheObject = CacheUtilNew.getCacheValue(cacheStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		VcodeQrcodeBatchInfo batchInfo = null;
		if(null != cacheObject){
			batchInfo = JSON.parseObject(cacheObject.toString(), VcodeQrcodeBatchInfo.class);
		}
		if (null == batchInfo) {
			batchInfo = batchInfoDao.queryVcodeQrcodeBatchByBatchKey(batchKey);
			try {
				CacheUtilNew.setCacheValue(cacheStr, 24*60*60, JSON.toJSONString(batchInfo));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return batchInfo;
	}

	public VpsVcodeQrcodeLib queryQrcodeLibByCol5(String libName,String col5) {
		if(StringUtils.isBlank(libName) || StringUtils.isBlank(col5)){
			return null;
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("libName", libName);
		map.put("col5", col5);
		return vcodeQrcodeLibDao.queryQrcodeLibByCol5(map);

	}
}
