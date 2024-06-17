package com.dbt.platform.autocode.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.reply.BaseDataResult;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.bean.Constant.ResultCode;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.util.BeanFactoryUtil;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.util.ReflectUtil;
import com.dbt.framework.util.SendSMSUtil;
import com.dbt.platform.activity.bean.VcodeActivityLibRelation;
import com.dbt.platform.activity.bean.VcodeQrcodeBatchInfo;
import com.dbt.platform.activity.bean.VpsVcodeQrcodeLib;
import com.dbt.platform.activity.dao.IVcodeActivityLibRelationDao;
import com.dbt.platform.activity.dao.IVcodeQrcodeLibDao;
import com.dbt.platform.activity.service.VcodeQrcodeBatchInfoService;
import com.dbt.platform.autocode.bean.VpsQrcodeOrder;
import com.dbt.platform.codefactory.bean.VpsVcodeFactory;
import com.dbt.platform.codefactory.bean.VpsWinery;
import com.dbt.platform.codefactory.service.VpsVcodeFactoryService;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.system.bean.SysUserBasis;

@Service("vpsQrcodeSingleBatchService")
public class VpsQrcodeAutoOrderService {
    private Logger log = Logger.getLogger(VpsQrcodeAutoOrderService.class);
    private Logger autoOrderLog = Logger.getLogger("autoOrderLog");

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private VpsQrcodeOrderService qrcodeOrderService;
    @Autowired
    private VpsVcodeFactoryService vpsVcodeFactoryService;
    @Autowired
    private VpsQrcodeOrderService vpsQrcodeOrderService;
    @Autowired
    private IVcodeQrcodeLibDao qrcodeLibDao;
    @Autowired
    private IVcodeActivityLibRelationDao qrcodeLibRelationDao;
    @Autowired
    private VcodeQrcodeBatchInfoService batchInfoService;
    @Autowired
    private SkuInfoService skuInfoService;
    
    /**
     * SKU列表查询【高印】
     * @return
     */
    public BaseDataResult<Map<String, Object>> querySkuForCPIS() {
        BaseDataResult<Map<String, Object>> baseResult = new BaseDataResult<>();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("sku", skuInfoService.queryForCPIS());
        baseResult.setData(resultMap);
        return baseResult.initReslut(ResultCode.SUCCESS, "查询成功");
    }
    
    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }

    /**
     * 创建批次
     * 
     * @param amount 批次码源数据
     * @return
     * @throws Exception 
     */
    public synchronized BaseDataResult<Map<String, Object>> addQrcodeOrder(int amount, String skuCode,
            String skuName, VpsVcodeFactory vpsVcodeFactory, VpsWinery vpsWinery, String qrcodeFactoryId, String qrcodeFactoryName,
            String qrcodeProductLineId, String qrcodeProductLineName, String workGroup, String machineId, String clientOrderNo, long timestamp) throws Exception {
        BaseDataResult<Map<String, Object>> baseResult = new BaseDataResult<>();
        if (amount > 500000) {
            return baseResult.initReslut(ResultCode.FILURE, "单次码源最大50万");
        }
        
        if (StringUtils.isBlank(skuCode) || StringUtils.isBlank(skuName)) {
            log.warn("skuCode:" + skuCode + " skuName:" + skuName);
            return baseResult.initReslut(ResultCode.FILURE, "参数不全");
        }
        
        // 校验时间戳
        if (Math.abs(System.currentTimeMillis() - timestamp) > 300000) {
            return baseResult.initReslut(ResultCode.FILURE, "系统时间异常");
        }

        // 校验SKU
        SkuInfo skuInfo = skuInfoService.findById(skuCode);
        if (skuInfo == null) {
            return baseResult.initReslut(ResultCode.FILURE, "SKU不存在");
        }
        
        // 初始化码源订单
        Date currTime = DateUtil.getNow();
        VpsQrcodeOrder qrcodeOrder = new VpsQrcodeOrder();
        qrcodeOrder.setProjectName("蒙牛");
        qrcodeOrder.setProjectOrderPrefix("MN");
        qrcodeOrder.setOrderName(vpsVcodeFactory.getFactoryName() + "-" + vpsWinery.getWineryName() + "-"
                + DateUtil.getDateTime(currTime, "yyyyMMdd") + "-" + skuInfo.getSkuName().replace("*", "X") + "-" + amount +"个("+ amount  +"个)");
        qrcodeOrder.setSkuKey(skuInfo.getSkuKey());
        qrcodeOrder.setQrcodeManufacture(vpsVcodeFactory.getFactoryName());
        qrcodeOrder.setBrewery(vpsWinery.getWineryName());
        qrcodeOrder.setQrcodeFormat(Constant.QRCODE_FORMAT.format_16);
        qrcodeOrder.setFileFormat("csv");
        qrcodeOrder.setChannelCount(1);
        qrcodeOrder.setPackagePassword("");
        qrcodeOrder.setIsWanBatch("1");
        qrcodeOrder.setOrderStatus(Constant.QRCODE_ORDER_STATUS.order_status_0);
        qrcodeOrder.setOrderQrcodeCount(amount);
        qrcodeOrder.setOrderTotalQrcodeCount(amount);
        qrcodeOrder.setImportFlag("0");
        qrcodeOrder.setOrderTime(DateUtil.getDateTime(currTime, DateUtil.DEFAULT_DATE_FORMAT));
        qrcodeOrder.setClientOrderNo(clientOrderNo);
        qrcodeOrder.setQrcodeFactoryId(qrcodeFactoryId);
        qrcodeOrder.setQrcodeFactoryName(qrcodeFactoryName);
        qrcodeOrder.setQrcodeProductLineId(qrcodeProductLineId);
        qrcodeOrder.setQrcodeProductLineName(qrcodeProductLineName);
        qrcodeOrder.setQrcodeWorkGroup(workGroup);
        qrcodeOrder.setQrcodeMachineId(machineId);
        qrcodeOrder.setQrcodePercent("100");
        qrcodeOrder.fillFields("AUTO");
        
        //计算批次信息
        List<VcodeQrcodeBatchInfo> batchLst = 
                vpsQrcodeOrderService.showBatchInfoLst(qrcodeOrder,vpsVcodeFactory, vpsWinery);
        qrcodeOrder.setBatchInfoList(batchLst);
        
        // 创建码源订单及批次
        SysUserBasis currentUser = new SysUserBasis();
        currentUser.setCompanyKey("201609141");
        currentUser.setUserKey("AUTO");
        qrcodeOrderService.create(qrcodeOrder, currentUser);
        
        // 生成码源
        taskExecutor.execute(new AutoOrderQrcodeCreateRunable(DbContextHolder.getDBType(), qrcodeOrder.getOrderKey()));
        
        // 返回批次编号
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("id", qrcodeOrder.getOrderKey());
        resultMap.put("state", "generating");
        baseResult.setData(resultMap);
        return baseResult.initReslut(ResultCode.SUCCESS, "码源订单创建成功");
    }
    
    /**
     * 生成码源
     * @param batchInfo
     * @throws Exception 
     */
    public void qrcodeOrderCreate(String orderKey) throws Exception {
        
        // 获取码源订单
        VpsQrcodeOrder qrcodeOrder = qrcodeOrderService.findById(orderKey);

        String dbType = DbContextHolder.getDBType();
        DbContextHolder.setDBType(null);
        VpsVcodeFactory vcodeFactory = vpsVcodeFactoryService.findByFactoryName(qrcodeOrder.getQrcodeManufacture());
        DbContextHolder.setDBType(dbType);
        
        // 判断同一赋码工厂同一SKU是否在生成码源，如果正在生成直接返回有job会重新触发生成
        String generatingRedisKey = RedisApiUtil.CacheKey.mengniuGY.SMALL_ORDER 
                + qrcodeOrder.getQrcodeFactoryName() + ":" + qrcodeOrder.getSkuKey() + ":" + "generating";
        if (StringUtils.isNotBlank(RedisApiUtil.getInstance().get(generatingRedisKey))) {
            return ;
        } else {
            RedisApiUtil.getInstance().set(generatingRedisKey, "generating", 10 * 60);
        }
        
        // 生成码源信息
        try {
            // 更新为生成中
            vpsQrcodeOrderService.updateOrderStatus(orderKey, Constant.QRCODE_ORDER_STATUS.order_status_3);
            
            // 生成码源
            vpsQrcodeOrderService.createCodeMain(qrcodeOrder, vcodeFactory, "AUTO", null);
        } catch (Exception e) {
            // 生成失败
            vpsQrcodeOrderService.updateOrderStatus(orderKey, Constant.QRCODE_ORDER_STATUS.order_status_2);
            SendSMSUtil.sendSmsByAIOpenid(true, false, "蒙牛高印码源订单生成码源失败", "orderKey:" + orderKey);
            throw e;
        } finally {
            // 释放Redis生成中状态
            RedisApiUtil.getInstance().del(true, generatingRedisKey);
        }
    }
    
    /**
     * 查询码源订单
     * 
     * @param orderKey  码源订单主键
     * @return
     * @throws Exception 
     */
    public BaseDataResult<Map<String, Object>> queryOrderStatus(String orderKey, String contextPath) throws Exception {
        BaseDataResult<Map<String, Object>> baseResult = new BaseDataResult<>();
        if (StringUtils.isBlank(orderKey)) {
            return baseResult.initReslut(ResultCode.FILURE, "参数不完整");
        }
        
        VpsQrcodeOrder qrcodeOrder = qrcodeOrderService.findById(orderKey);
        if (qrcodeOrder == null) {
            return baseResult.initReslut(ResultCode.FILURE, "码源订单不存在");
        }
        
        // 码源订单状态："⽣成数据中": "generating", "打包中": "ziping", "已完成":"completed"
        String orderStatus = "fail";
        String zipPath = null;
        if (Constant.QRCODE_ORDER_STATUS.order_status_1.equals(qrcodeOrder.getOrderStatus())
                || Constant.QRCODE_ORDER_STATUS.order_status_4.equals(qrcodeOrder.getOrderStatus())
                || Constant.QRCODE_ORDER_STATUS.order_status_5.equals(qrcodeOrder.getOrderStatus())) {
            orderStatus = "completed";
            zipPath = contextPath + "/upload/autocode/" + orderKey.substring(0, 8) + "/" + DbContextHolder.getDBType() 
                    + "/" + orderKey + "/4compress/" + qrcodeOrder.getOrderNo() + "_" + URLEncoder.encode(qrcodeOrder.getOrderName(), "UTF-8") + ".zip";
        } else if (Constant.QRCODE_ORDER_STATUS.order_status_0.equals(qrcodeOrder.getOrderStatus())
                || Constant.QRCODE_ORDER_STATUS.order_status_3.equals(qrcodeOrder.getOrderStatus())) {
            orderStatus = "generating";
        }
       
        // 初始化返回结果
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", orderKey);
        resultMap.put("state", orderStatus);
        resultMap.put("csv_zip", zipPath);
        baseResult.setData(resultMap);
        return baseResult.initReslut(ResultCode.SUCCESS, "查询成功");
    }
    
    /**
     * 码源文件上传
     * 
     * @param taskId 任务ID
     * @param file  码源文件
     * @return
     */
    public BaseDataResult<Map<String, Object>> executeBatchUploadForBatchAutocodeNo(String batchId, MultipartFile file, long timestamp) {
        BaseDataResult<Map<String, Object>> baseResult = new BaseDataResult<>();
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isBlank(batchId) || file == null || file.isEmpty()) {
            log.warn("batchId：" + batchId + " file：" + (file == null || file.isEmpty() ? "文件为空" : file.getOriginalFilename()));
            return baseResult.initReslut(ResultCode.FILURE, "参数不全");
        }
        
        // 校验时间戳
        if (Math.abs(System.currentTimeMillis() - timestamp) > 300000) {
            return baseResult.initReslut(ResultCode.FILURE, "系统时间异常");
        }

        // 导⼊任务状态（"校验⽂件": "validating", "导⼊数据中": "packing", "导⼊失败": "failed", "导⼊成功": "completed"）
        // 校验任务是否正在执行
        String taskId = UUID.randomUUID().toString().replace("-", "");
        String redisTaskKey = RedisApiUtil.CacheKey.mengniuGY.BATCH_UPLOAD_TASK + batchId;
        RedisApiUtil.getInstance().setHSet(redisTaskKey, taskId, "validating");
        RedisApiUtil.getInstance().expireAt(redisTaskKey, DateUtil.addDays(7).getTime() / 1000);
        
        // 解析码源及异常V码
        Set<String> errAutocodeNoSet = new HashSet<>();
        Map<String, Set<String>> autocodeNoMap = new HashMap<>();
        
        // 导⼊任务状态（"校验⽂件": "validating", "导⼊数据中": "packing", "导⼊失败": "failed", "导⼊成功": "completed"）
        String inputFileName = "";
        LineNumberReader qrcodeLineReader = null;
        try {
            // 备份高印上传的文件 
            inputFileName = file.getOriginalFilename();
            File copyFile = new File("/home/vjifen/" + DbContextHolder.getDBType() 
            + "/" + batchId + "/" + DateUtil.getDateTime("yyyyMMddHHmmss") + "_"+ inputFileName);
            FileUtils.copyInputStreamToFile(file.getInputStream(), copyFile);
            
            // 校验码源个数
            qrcodeLineReader = new LineNumberReader(new InputStreamReader(file.getInputStream()));
            qrcodeLineReader.skip(file.getSize());
            int qrcodeNum = qrcodeLineReader.getLineNumber();
            if (qrcodeNum > 10000) {
                return baseResult.initReslut(ResultCode.FILURE, "单次最多上传1万码源");
            }
            qrcodeLineReader.close();
            
            // 主键码源
            String autocodeNo, qrcodePrefix;
            qrcodeLineReader = new LineNumberReader(new InputStreamReader(file.getInputStream()));
            while (StringUtils.isNotBlank(autocodeNo = qrcodeLineReader.readLine())) {
                // 码源长度不为10位时
                if (autocodeNo.length() != 10) {
                    errAutocodeNoSet.add(autocodeNo);
                    continue;
                }
                
                // 码源前三位
                qrcodePrefix = autocodeNo.substring(0, 3);
                if (!autocodeNoMap.containsKey(qrcodePrefix)) {
                    autocodeNoMap.put(qrcodePrefix, new HashSet<>());
                }
                autocodeNoMap.get(qrcodePrefix).add(autocodeNo);
            }
            
        } catch (IOException e) {
            log.error("文件读取失败", e);
            return baseResult.initReslut(ResultCode.FILURE, "文件读取失败");
            
        } finally {
            if (qrcodeLineReader != null) {
                try {
                    qrcodeLineReader.close();
                } catch (IOException e) {
                    log.error("关闭文件异常", e);
                }
            }
        }
        
        // 分批次入库
        String currTime = DateUtil.getDateTime();
        int qrcodeNum = 0;
        Map<String, Object> map = null;
        List<String> autocodeNoLst = null;
        List<String> queryAutocodeNoLst = null;
        List<VpsVcodeQrcodeLib> qrcodeLibLst = null;
        VcodeQrcodeBatchInfo batchInfo = null; // 批次表
        VcodeActivityLibRelation libRelation = null;
        String batchImportLog = null;
        List<String> batchLogLst = new ArrayList<>();
        RedisApiUtil.getInstance().setHSet(redisTaskKey, taskId, "packing");
        for (String prefix : autocodeNoMap.keySet()) {
            // 解析的码源
            autocodeNoLst = new ArrayList<>(autocodeNoMap.get(prefix));
            
            // 获取libRelation
            libRelation = qrcodeLibRelationDao.findLibRelationByUniqueCode(prefix);
            if (libRelation == null) {
                for (String item : autocodeNoLst) {
                    errAutocodeNoSet.add(item);
                }
                continue;
            }
            
            // 码源个数
            qrcodeNum += autocodeNoMap.get(prefix).size();
            
            // 校验码源是否真实有效
            map = new HashMap<>();
            map.put("libName", libRelation.getLibName() + "_wan_bak");
            if (qrcodeLibDao.findTableIsExist(map) > 0) {
                map.put("list", autocodeNoLst);
                qrcodeLibLst = qrcodeLibDao.queryQrcodeByBatchAutocodeNo(map);
                queryAutocodeNoLst = ReflectUtil.getFieldsValueByName("batchAutocodeNo", qrcodeLibLst);
                if (CollectionUtils.isNotEmpty(queryAutocodeNoLst)) autocodeNoLst.removeAll(queryAutocodeNoLst);
            }
            if (CollectionUtils.isNotEmpty(autocodeNoLst)) {
                for (String item : autocodeNoLst) {
                    errAutocodeNoSet.add(item);
                }
                continue;
            }
            
            // 入库码源
            map.clear();
            map.put("libName", libRelation.getLibName());
            map.put("batchKey", libRelation.getBatchKey());
            map.put("list", qrcodeLibLst);
            map.put("createTime", currTime);
            try {
                qrcodeLibDao.batchInsertQrcodeAutocodeNo(map);
                
            } catch (Exception e) {
                // 获取异常码源
                map.put("list", new ArrayList<>(autocodeNoMap.get(prefix)));
                qrcodeLibLst = qrcodeLibDao.queryQrcodeByBatchAutocodeNo(map);
                for (VpsVcodeQrcodeLib lib : qrcodeLibLst) {
                    errAutocodeNoSet.add(lib.getBatchAutocodeNo());
                }
                continue;
            }
            
            // 更新每个批次的状态及回传码源量
            map.clear();
            map.put("libName", libRelation.getLibName() + "_wan_bak");
            map.put("list", queryAutocodeNoLst);
            qrcodeLibLst = qrcodeLibDao.queryQrcodeByBatchAutocodeNoGroup(map);
            for (VpsVcodeQrcodeLib item : qrcodeLibLst) {

                // 获取批次
                batchInfo = batchInfoService.findById(item.getBatchKey());
                if (batchInfo.getBatchDesc().endsWith("_WAN")) {
                    batchInfo.setBatchDesc(batchInfo.getBatchDesc().replace("_WAN", ""));
                }
                batchInfo.setQrcodeAmounts(Long.valueOf(item.getQrcodeAmount()));
                if (Constant.batchStatus.status0.equals(batchInfo.getStatus())
                        || Constant.batchStatus.status1.equals(batchInfo.getStatus())
                        || Constant.batchStatus.status2.equals(batchInfo.getStatus())) {
                    batchInfo.setStatus(Constant.batchStatus.status3);
                }
                
                // 更新批次的入库状态
                batchInfoService.updateForMN(batchInfo);
                batchImportLog = "batchId:" + batchId + " taskId:" + taskId + " file:" + inputFileName 
                        + " batchDesc:" + batchInfo.getBatchDesc() + " qrcodeNum:" + item.getQrcodeAmount();
                batchLogLst.add(batchImportLog);
                
                // 记录入库码源订单
                qrcodeOrderService.updateOrderImportFlag(batchInfo.getOrderKey(), "1", "AUTO");
            }
        }
        
        // 入库成功
        resultMap.put("id", taskId);
        baseResult.setData(resultMap);
        if (errAutocodeNoSet.isEmpty()) {
            resultMap.put("state", "completed");
            baseResult.initReslut(ResultCode.SUCCESS, "导入成功");
            RedisApiUtil.getInstance().setHSet(redisTaskKey, taskId, "completed");
            log.warn("batchId:" + batchId + " taskId:" + taskId + " file:" + inputFileName + " qrcodeNum:" + qrcodeNum + " --- 入库成功");
            for (String logItem : batchLogLst) {
                autoOrderLog.warn(logItem);
            }
            
        } else {
            // 返回异常码源
            String failedMsg = "错误码源:" + String.join(",", errAutocodeNoSet);
            resultMap.put("state", "failed");
            resultMap.put("failed_msg", failedMsg);
            RedisApiUtil.getInstance().setHSet(redisTaskKey, taskId + ":failedMsg", failedMsg);
            baseResult.initReslut(ResultCode.SUCCESS, "包含无效或重复码源");
            RedisApiUtil.getInstance().setHSet(redisTaskKey, taskId, "failed");
            log.warn("batchId:" + batchId + " taskId:" + taskId + " file:" + inputFileName + " --- 入库失败");
            throw new BusinessException("failed", JSON.toJSONString(baseResult));
        }
        return baseResult;
    }
    
    /**
     * 码源文件上传【串码回传，由于回传改为批次序号此方法暂时弃用】
     * 
     * @param taskId 任务ID
     * @param file  码源文件
     * @return
     */
    @Deprecated
    public BaseDataResult<Map<String, Object>> executeBatchUpload(String batchId, MultipartFile file) {
        BaseDataResult<Map<String, Object>> baseResult = new BaseDataResult<>();
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isBlank(batchId) || file == null || file.isEmpty()) {
            log.warn("batchId：" + batchId + " file：" + (file == null || file.isEmpty() ? "文件为空" : file.getOriginalFilename()));
            return baseResult.initReslut(ResultCode.FILURE, "参数不全");
        }
        
        // 导⼊任务状态（"校验⽂件": "validating", "导⼊数据中": "packing", "导⼊失败": "failed", "导⼊成功": "completed"）
        // 校验任务是否正在执行
        String taskId = UUID.randomUUID().toString().replace("-", "");
        String redisTaskKey = RedisApiUtil.CacheKey.mengniuGY.BATCH_UPLOAD_TASK + batchId;
        RedisApiUtil.getInstance().setHSet(redisTaskKey, taskId, "validating");
        RedisApiUtil.getInstance().expireAt(redisTaskKey, DateUtil.addDays(7).getTime() / 1000);
        
        // 解析码源及异常V码
        Set<String> errQrcodeSet = new HashSet<>();
        Map<String, Set<String>> qrcodeMap = new HashMap<>();
        
        // 导⼊任务状态（"校验⽂件": "validating", "导⼊数据中": "packing", "导⼊失败": "failed", "导⼊成功": "completed"）
        String inputFileName = "";
        LineNumberReader qrcodeLineReader = null;
        try {
            // 备份高印上传的文件 
            inputFileName = file.getOriginalFilename();
            File copyFile = new File("/home/vjifen/" + DbContextHolder.getDBType() 
                    + "/" + batchId + "/" + DateUtil.getDateTime("yyyyMMddHHmmss") + "_"+ inputFileName);
            FileUtils.copyInputStreamToFile(file.getInputStream(), copyFile);
            
            // 校验码源个数
            qrcodeLineReader = new LineNumberReader(new InputStreamReader(file.getInputStream()));
            qrcodeLineReader.skip(file.getSize());
            int qrcodeNum = qrcodeLineReader.getLineNumber();
            if (qrcodeNum > 10000) {
                return baseResult.initReslut(ResultCode.FILURE, "单次最多上传1万码源");
            }
            qrcodeLineReader.close();
            
            // 主键码源
            String qrcode, qrcodePrefix;
            qrcodeLineReader = new LineNumberReader(new InputStreamReader(file.getInputStream()));
            while (StringUtils.isNotBlank(qrcode = qrcodeLineReader.readLine())) {
                // 码源长度不为7位时
                if (qrcode.length() != 10) {
                    errQrcodeSet.add(qrcode);
                    continue;
                }
                
                // 码源前三位
                qrcodePrefix = qrcode.substring(0, 3);
                if (!qrcodeMap.containsKey(qrcodePrefix)) {
                    qrcodeMap.put(qrcodePrefix, new HashSet<>());
                }
                qrcodeMap.get(qrcodePrefix).add(qrcode.substring(3));
            }
            
        } catch (IOException e) {
            log.error("文件读取失败", e);
            return baseResult.initReslut(ResultCode.FILURE, "文件读取失败");
            
        } finally {
            if (qrcodeLineReader != null) {
                try {
                    qrcodeLineReader.close();
                } catch (IOException e) {
                    log.error("关闭文件异常", e);
                }
            }
        }
        
        // 分批次入库
        String currTime = DateUtil.getDateTime();
        int qrcodeNum = 0;
        Map<String, Object> map = null;
        List<String> qrcodeLst = null;
        List<String> queryQrcodeLst = null;
        List<String> errQrcodeLst = null;
        VcodeQrcodeBatchInfo batchInfo = null; // 批次表
        VcodeActivityLibRelation libRelation = null;
        RedisApiUtil.getInstance().setHSet(redisTaskKey, taskId, "packing");
        for (String prefix : qrcodeMap.keySet()) {
            // 解析的码源
            qrcodeLst = new ArrayList<>(qrcodeMap.get(prefix));
            
            // 获取libRelation
            libRelation = qrcodeLibRelationDao.findLibRelationByUniqueCode(prefix);
            if (libRelation == null) {
                for (String item : qrcodeLst) {
                    errQrcodeSet.add(prefix + item);
                }
                continue;
            }

            // 码源个数
            qrcodeNum += qrcodeMap.get(prefix).size();
            
            // 获取批次
            batchInfo = batchInfoService.findById(libRelation.getBatchKey());
            if (batchInfo.getBatchDesc().endsWith("_WAN")) {
                batchInfo.setBatchDesc(batchInfo.getBatchDesc().replace("_WAN", ""));
            }
            batchInfo.setQrcodeAmounts(Long.valueOf(qrcodeMap.get(prefix).size()));
            if (Constant.batchStatus.status0.equals(batchInfo.getStatus())
                    || Constant.batchStatus.status1.equals(batchInfo.getStatus())
                    || Constant.batchStatus.status2.equals(batchInfo.getStatus())) {
                batchInfo.setStatus(Constant.batchStatus.status3);
            }
            
            // 校验码源是否真实有效
            map = new HashMap<>();
            map.put("libName", libRelation.getLibName() + "_wan_bak");
            map.put("list", qrcodeLst);
            queryQrcodeLst = qrcodeLibDao.queryQrcode(map);
            if (CollectionUtils.isNotEmpty(queryQrcodeLst)) qrcodeLst.removeAll(queryQrcodeLst);
            if (CollectionUtils.isNotEmpty(qrcodeLst)) {
                for (String item : qrcodeLst) {
                    errQrcodeSet.add(prefix + item);
                }
                continue;
            }
            
            // 入库码源
            map.clear();
            qrcodeLst = new ArrayList<>(qrcodeMap.get(prefix));
            map.put("libName", libRelation.getLibName());
            map.put("batchKey", libRelation.getBatchKey());
            map.put("list", qrcodeLst);
            map.put("createTime", currTime);
            try {
                qrcodeLibDao.batchInsertQrcode(map);
                
            } catch (Exception e) {
                // 获取异常码源
                errQrcodeLst = qrcodeLibDao.queryQrcode(map);
                for (String item : errQrcodeLst) {
                    errQrcodeSet.add(prefix + item);
                }
                continue;
            }
            
            // 更新批次的入库状态
            batchInfoService.updateForMN(batchInfo);
            log.warn("batchId:" + batchId + " taskId:" + taskId + " file:" + inputFileName 
                    + " batchDesc:" + batchInfo.getBatchDesc() + " qrcodeNum:" + batchInfo.getQrcodeAmounts());
            
            // 记录入库码源订单
            qrcodeOrderService.updateOrderImportFlag(batchInfo.getOrderKey(), "1", "AUTO");
        }
        
        // 入库成功
        resultMap.put("id", taskId);
        baseResult.setData(resultMap);
        if (errQrcodeSet.isEmpty()) {
            resultMap.put("state", "completed");
            baseResult.initReslut(ResultCode.SUCCESS, "导入成功");
            RedisApiUtil.getInstance().setHSet(redisTaskKey, taskId, "completed");
            log.warn("batchId:" + batchId + " taskId:" + taskId + " file:" + inputFileName + " qrcodeNum:" + qrcodeNum + " --- 入库成功");
            
        } else {
            // 返回异常码源
            String failedMsg = "错误码源:" + String.join(",", errQrcodeSet);
            resultMap.put("state", "failed");
            resultMap.put("failed_msg", failedMsg);
            RedisApiUtil.getInstance().setHSet(redisTaskKey, taskId + ":failedMsg", failedMsg);
            baseResult.initReslut(ResultCode.SUCCESS, "包含无效或重复码源");
            RedisApiUtil.getInstance().setHSet(redisTaskKey, taskId, "failed");
            log.warn("batchId:" + batchId + " taskId:" + taskId + " file:" + inputFileName + " --- 入库失败");
            throw new BusinessException("failed", JSON.toJSONString(baseResult));
        }
        return baseResult;
    }
    
    /**
     * 码源文件上传状态查询
     * 
     * @param t_unit_batch_id   当日批次ID
     * @param id    上传文件任务ID
     * @return
     */
    public BaseDataResult<Map<String, Object>> queryBatchUploadStatus(String batchId, String taskId) {
        BaseDataResult<Map<String, Object>> baseResult = new BaseDataResult<>();
        if (StringUtils.isBlank(batchId) || StringUtils.isBlank(taskId)) {
            log.warn("batchId：" + batchId + " taskId：" + taskId);
            return baseResult.initReslut(ResultCode.FILURE, "参数不全");
        }
        
        String redisTaskKey = RedisApiUtil.CacheKey.mengniuGY.BATCH_UPLOAD_TASK + batchId;
        String state = RedisApiUtil.getInstance().getHSet(redisTaskKey, taskId);
        if (StringUtils.isBlank(state)) {
            baseResult.initReslut(ResultCode.FILURE, "任务不存在或已过期");
        } else {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("id", taskId);
            resultMap.put("state", state);
            if ("failed".equals(state)) {
                resultMap.put("failed_msg", RedisApiUtil.getInstance().getHSet(redisTaskKey, taskId + ":failedMsg"));
            }
            baseResult.setData(resultMap);
            baseResult.initReslut(ResultCode.SUCCESS, "查询成功");
        }
        
        return baseResult;
    }
    
    /**
     * 未生成成功的自动码源订单Job
     */
    public void dealUnCreateOrderJob() {
        List<VpsQrcodeOrder> qrcodeOrderLst = qrcodeOrderService.queryFailedOrderForAuto();
        for (VpsQrcodeOrder qrcodeOrder : qrcodeOrderLst) {
            taskExecutor.execute(new AutoOrderQrcodeCreateRunable(DbContextHolder.getDBType(), qrcodeOrder.getOrderKey()));
        }
    }
    
    /**
     * 未生成成功的自动码源订单Job
     */
    public void executeAutoQrcodeQrderBatchBindActivity() {
        qrcodeOrderService.executeAutoQrcodeQrderBatchBindActivity();
    }
}

/**
 * 生成码源
 */
class AutoOrderQrcodeCreateRunable implements Runnable {
    private Log log = LogFactory.getLog(this.getClass());
    private String dbType;
    private String orderKey;
    
    public AutoOrderQrcodeCreateRunable(String dbType, String orderKey) {
        this.dbType = dbType;
        this.orderKey = orderKey;
    }
    
    @Override
    public void run() {
        try {
            DbContextHolder.setDBType(dbType);
            VpsQrcodeAutoOrderService singleBatchService = 
                    (VpsQrcodeAutoOrderService) BeanFactoryUtil.getbeanFromWebContext("vpsQrcodeSingleBatchService");
            singleBatchService.qrcodeOrderCreate(orderKey);
        } catch (Exception e) {
            log.error("码源生成失败：" + e.getMessage(), e);
        }
    }
}
