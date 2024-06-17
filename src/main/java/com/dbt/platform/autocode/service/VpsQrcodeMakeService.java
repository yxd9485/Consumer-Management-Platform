package com.dbt.platform.autocode.service;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.reply.BaseDataResult;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.util.QrCodeUtil;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.bean.VcodeActivityLibRelation;
import com.dbt.platform.activity.bean.VcodeQrcodeBatchInfo;
import com.dbt.platform.activity.bean.VpsVcodeQrcodeLib;
import com.dbt.platform.activity.dao.IVcodeQrcodeLibDao;
import com.dbt.platform.activity.service.VcodeActivityLibRelationServiceImpl;
import com.dbt.platform.activity.service.VcodeActivityService;
import com.dbt.platform.activity.service.VcodeQrcodeBatchInfoService;
import com.dbt.platform.autocode.bean.VpsQrcodeOrder;
import com.dbt.platform.autocode.bean.VpsVcodeBatchSerialNoQueryLog;
import com.dbt.platform.autocode.dto.VpsQrcodeLibNameSaveDTO;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author shuDa
 * @date 2021/12/3
 **/
@Service("vpsQrcodeMakeService")
public class VpsQrcodeMakeService extends BaseService<VpsVcodeQrcodeLib> {
    private final static String COMMON_PATH = PropertiesUtil.getPropertyValue("create_code_common_path");
    @Autowired
    private VcodeActivityLibRelationServiceImpl vcodeActivityLibRelationService;
    @Autowired
    private VcodeQrcodeBatchInfoService vcodeQrcodeBatchInfoService;

    @Autowired
    private VcodeActivityService vcodeActivityService;
    @Autowired
    private IVcodeQrcodeLibDao iVcodeQrcodeLibDao;
    @Autowired
    private VpsQrcodeOrderService vpsQrcodeOrderService;
    @Autowired
    private VpsVcodeBatchSerialNoQueryLogService batchSerialNoQueryLogService;


    public String executeMakeUp(SysUserBasis userInfo, Map<String, String> analysisQrcodeMap,  BaseDataResult<Map<String, Object>> baseResult) {
        // 3、根据二维码前三位（唯一标识）查询所属码库表信息，不存在直接返回告知
        // 依据V码前3位获取 vps_vcode_activity_lib_relation
        VcodeActivityLibRelation libRelation = vcodeActivityLibRelationService.findLibRelationByUniqueCode(analysisQrcodeMap.get("activitycode"));
        if(libRelation==null){
            log.warn("serverName:" + DbContextHolder.getDBType() + "依据" + analysisQrcodeMap.get("activitycode") + "查询不到对应的lib_relation关系, userKey:" + userInfo.getUserKey());
            return JSON.toJSONString( baseResult.initReslut("0", "这个二维码不存在"));
        }
        //4根据批次主键查询所属订单信息，不存在直接返回告知
        //根据批次key 获取V码二维码批次信息bean
        VcodeQrcodeBatchInfo batchInfo = vcodeQrcodeBatchInfoService.findById(libRelation.getBatchKey());
        if(batchInfo==null ){
            log.info("serverName:" + DbContextHolder.getDBType() + "根据表名:" + libRelation.getLibName() + "对应的batchkey:" + libRelation.getBatchKey() + "查询不到批次");
            return JSON.toJSONString(baseResult.initReslut("0", "批次不存在!"));
        }
        VpsQrcodeOrder qrcodeOrder = vpsQrcodeOrderService.findById(batchInfo.getOrderKey());
        //非一万一批次二维码禁止补录
        if ((qrcodeOrder == null) || Constant.BOOLEAN_TYPE.FALSE_.equals(qrcodeOrder.getIsWanBatch())) {
            baseResult.initReslut("0", "非一万一批次二维码禁止补录!");
            return JSON.toJSONString(baseResult);
        }
        //二维码未回传
        if( (qrcodeOrder == null) ||  Constant.ImportFlag.ImportFlag_0.equals(qrcodeOrder.getImportFlag())){
            log.info("serverName:" + DbContextHolder.getDBType() + "根据表名:" + libRelation.getLibName() + " orderKey:"+batchInfo.getOrderKey()  + " 对应的batchkey:" + libRelation.getBatchKey() + " 二维码未回传 " + analysisQrcodeMap);
            return JSON.toJSONString( baseResult.initReslut("0", "二维码未回传!"));
        }
        // 依据lib_relation码库表 和二维码内容查询二维码是否存在
        VpsVcodeQrcodeLib vcodeQrcodeLib = vcodeQrcodeBatchInfoService.queryQrcodeLibByQrcodeContent(libRelation.getLibName(),analysisQrcodeMap.get("vcode"));
        if (vcodeQrcodeLib != null) {
            //存在时返回告知二维码已存在，并且还需返回二维码所属批次编号，批次名称，批次开始结束时间，所属活动名称，活动开始结束时间等
            batchInfo = vcodeQrcodeBatchInfoService.findById(vcodeQrcodeLib.getBatchKey());
            if(batchInfo.getVcodeActivityKey()==null){
                log.info("serverName:" + DbContextHolder.getDBType() + "根据表名:" + libRelation.getLibName() + " orderKey:"+batchInfo.getOrderKey()  + " 对应的batchkey:" + vcodeQrcodeLib.getBatchKey() +"  activityKey:"+ batchInfo.getVcodeActivityKey()+ "二维码未关联活动 " + analysisQrcodeMap);
                return JSON.toJSONString(baseResult.initReslut("0", "二维码未关联活动!"));
            }
            VcodeActivityCog vcodeActivityCog = vcodeActivityService.findById(batchInfo.getVcodeActivityKey());
            if(vcodeActivityCog==null){
                log.info("serverName:" + DbContextHolder.getDBType() + "根据表名:" + libRelation.getLibName() + " orderKey:"+batchInfo.getOrderKey()  + " 对应的batchkey:" + vcodeQrcodeLib.getBatchKey() +"  activityKey:"+batchInfo.getVcodeActivityKey()+ "未查询到二维码关联活动 "+ analysisQrcodeMap);
                return JSON.toJSONString( baseResult.initReslut("0", "未查询到二维码关联活动!"));
            }

            Map<String, Object> addMap = new HashMap<>();
            addMap.put("batchDesc", batchInfo.getBatchDesc());
            addMap.put("batchName", batchInfo.getBatchName());
            addMap.put("batchStartDate", batchInfo.getStartDate());
            addMap.put("batchEndDate", batchInfo.getEndDate());
            addMap.put("activityName", vcodeActivityCog.getVcodeActivityName());
            addMap.put("activityStartDate", vcodeActivityCog.getStartDate());
            addMap.put("activityEndDate", vcodeActivityCog.getEndDate());
            baseResult.setData(addMap);
            return JSON.toJSONString(baseResult);
        }
        //7、判断订单是否属于一万一批次
        if (Constant.BOOLEAN_TYPE.TRUE_.equals(qrcodeOrder.getIsWanBatch())) {
            // 7.1、是：需要根据码库备份表查询二维码是否存在，表名格式：码库表_wan_bak
            VpsVcodeQrcodeLib wanBatchVcodeQrcodeLib = vcodeQrcodeBatchInfoService
                    .queryQrcodeLibByQrcodeContent(libRelation.getLibName()+ "_wan_bak",analysisQrcodeMap.get("vcode"));
            if(wanBatchVcodeQrcodeLib==null){
                baseResult.initReslut("0", "二维码无效");
                return JSON.toJSONString(baseResult);
            }else{
                //补录二维码
                makeUpQrcode(libRelation.getLibName(),analysisQrcodeMap.get("vcode"), wanBatchVcodeQrcodeLib.getBatchAutocodeNo());
                baseResult.initReslut("0", "补录成功");
                return JSON.toJSONString(baseResult);
            }
        }else{
            //补录二维码
//            makeUpQrcode(libRelation.getLibName(),analysisQrcodeMap.get("vcode"));
            baseResult.initReslut("0", "非一万一批次二维码禁止补录!");
            return JSON.toJSONString(baseResult);
        }

    }
    public String executeQuery(SysUserBasis userInfo, Map<String, String> analysiscol5Map, BaseDataResult<Map<String, Object>> baseResult,String batchQueryId) {
        // 依据V码前3位获取 vps_vcode_activity_lib_relation
        VcodeActivityLibRelation libRelation = vcodeActivityLibRelationService.findLibRelationByUniqueCode(analysiscol5Map.get("code"));
        if(libRelation==null){
            log.warn("serverName:" + DbContextHolder.getDBType() + "依据" + analysiscol5Map.get("code") + "查询不到对应的lib_relation关系, userKey:" + userInfo.getUserKey());
            return JSON.toJSONString( baseResult.initReslut("0", "二维码不存在!"));
        }

        //根据批次key 获取V码二维码批次信息bean
        VcodeQrcodeBatchInfo batchInfo = vcodeQrcodeBatchInfoService.findById(libRelation.getBatchKey());
        if(batchInfo==null ){
            log.info("serverName:" + DbContextHolder.getDBType() + "根据表名:" + libRelation.getLibName() + "对应的batchkey:" + libRelation.getBatchKey() + "查询不到批次");
            return JSON.toJSONString(baseResult.initReslut("0", "批次不存在!"));
        }
        VpsQrcodeOrder qrcodeOrder = vpsQrcodeOrderService.findById(batchInfo.getOrderKey());
        if(qrcodeOrder == null){
            return JSON.toJSONString(baseResult.initReslut("0", "码源订单不存在!"));
        }
        //判断是不是一万一批次
        VpsVcodeQrcodeLib VcodeQrcodeLib = new VpsVcodeQrcodeLib();
        String imageUrl = null;
        if (Constant.BOOLEAN_TYPE.TRUE_.equals(qrcodeOrder.getIsWanBatch())) {
            VcodeQrcodeLib = vcodeQrcodeBatchInfoService
                    .queryQrcodeLibByCol5(libRelation.getLibName(),analysiscol5Map.get("col5"));
            if(VcodeQrcodeLib==null){
                baseResult.setErrmsg("未回传");
                //警告作用
                baseResult.setErrcode("2");
                VcodeQrcodeLib = vcodeQrcodeBatchInfoService
                        .queryQrcodeLibByCol5(libRelation.getLibName()+ "_wan_bak",analysiscol5Map.get("col5"));
            }
        }else{
             VcodeQrcodeLib = vcodeQrcodeBatchInfoService
                    .queryQrcodeLibByCol5(libRelation.getLibName(),analysiscol5Map.get("col5"));
        }
        if(VcodeQrcodeLib==null){
            return JSON.toJSONString(baseResult.initReslut("0", "未查询到二维码!"));
        }
        batchInfo = vcodeQrcodeBatchInfoService.findById(VcodeQrcodeLib.getBatchKey());
        if(batchInfo==null ){
            log.info( "根据表名:" + libRelation.getLibName() + "对应的batchkey:" + VcodeQrcodeLib.getBatchKey() + "查询不到批次");
            return JSON.toJSONString(baseResult.initReslut("0", "批次不存在!"));
        }
        
        // 增加查询日志
        VpsVcodeBatchSerialNoQueryLog batchSerialNoQueryLog = batchSerialNoQueryLogService.findByBatchSerialNo(analysiscol5Map.get("col5"));
        if (batchSerialNoQueryLog == null) {
            batchSerialNoQueryLog = new VpsVcodeBatchSerialNoQueryLog();
            batchSerialNoQueryLog.setInfoKey(UUID.randomUUID().toString());
            batchSerialNoQueryLog.setBatchSerialNo(analysiscol5Map.get("col5"));
            batchSerialNoQueryLog.setQueryUserPhoneNum(userInfo.getPhoneNum());
            batchSerialNoQueryLog.setOrderKey(qrcodeOrder.getOrderKey());
            batchSerialNoQueryLog.setBatchKey(VcodeQrcodeLib.getBatchKey());
            batchSerialNoQueryLog.fillFields(userInfo.getUserKey());
            batchSerialNoQueryLog.setQueryNotes(analysiscol5Map.get("queryNotes"));
            batchSerialNoQueryLogService.addSeriallNOLog(batchSerialNoQueryLog);
        }
        batchQueryId = StringUtils.isEmpty(batchQueryId) ? "1source" : batchQueryId;
        imageUrl = qrcodeCreate(analysiscol5Map.get("code")+VcodeQrcodeLib.getQrcodeContent(), analysiscol5Map.get("col5"),batchQueryId);
        Map<String, Object> resultMap = new HashMap<>();
        VcodeActivityCog vcodeActivityCog = vcodeActivityService.findById(batchInfo.getVcodeActivityKey());
        if(vcodeActivityCog!=null){
            resultMap.put("activityName", vcodeActivityCog.getVcodeActivityName());
            resultMap.put("activityStartDate", vcodeActivityCog.getStartDate());
            resultMap.put("activityEndDate", vcodeActivityCog.getEndDate());
        }
        if (qrcodeOrder != null) {
            resultMap.put("orderNo", qrcodeOrder.getOrderNo());
            resultMap.put("orderName", qrcodeOrder.getOrderName());
            resultMap.put("clientOrderNo", qrcodeOrder.getClientOrderNo());
            resultMap.put("qrcodeManufacture", qrcodeOrder.getQrcodeManufacture());
            resultMap.put("qrcodeFactoryName", qrcodeOrder.getQrcodeFactoryName());
            resultMap.put("qrcodeProductLineName", qrcodeOrder.getQrcodeProductLineName());
            resultMap.put("qrcodeMachineId", qrcodeOrder.getQrcodeMachineId());
            resultMap.put("qrcodeWorkGroup", qrcodeOrder.getQrcodeWorkGroup());
        }
        resultMap.put("batchDesc", batchInfo.getBatchDesc());
        resultMap.put("batchName", batchInfo.getBatchName());
        resultMap.put("batchStartDate", batchInfo.getStartDate());
        resultMap.put("batchEndDate", batchInfo.getEndDate());
        resultMap.put("qrcodeImg", getImgStr(imageUrl));
        resultMap.put("qrcodeImgUrl", imageUrl);
        baseResult.setData(resultMap);
        return JSON.toJSONString(baseResult.initReslut("0", "成功"));
    }
    /**
     * 将图片转换成Base64编码
     * @param imgFile 待处理图片
     * @return
     */
    private static String getImgStr(String imgFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(data);
    }

    private String qrcodeCreate(String content,String imageName,String batchQueryId){
        // 二维码url
        // 测试：http://xt.vjifen.com/qr?c=p&v=二维码
        // 线上：HTTP://VJ1.TV/p/二维码
        String qrcodeUrl = DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_HTTP_URL,
                DatadicKey.filterHttpUrl.QRCODE_URL);
        String projectFlag = DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_COMPANY_INFO,
                DatadicKey.filterCompanyInfo.PROJECT_FLAG);
        String url = qrcodeUrl + projectFlag;
        if(qrcodeUrl.indexOf("vjifen") > -1){
            url += "&v=";
        }else{
            url += "/";
        }
        url += content;
        String imageUrl = COMMON_PATH + "/queryQrcode/"+DbContextHolder.getDBType()+"/" +batchQueryId+"/"+imageName+".png";
        File file = new File(COMMON_PATH + "/queryQrcode/"+DbContextHolder.getDBType()+"/" +batchQueryId);
        if (!file.exists()){
            file.mkdirs();
        }
        QrCodeUtil.encodeQRCodeImage(url, null, imageUrl, 283, 283, null);
        return imageUrl;
    }

    /**
     * 补录二维码
     * @param libName
     * @param qrcodeContent
     */
    private synchronized void makeUpQrcode(String libName,String qrcodeContent, String batchAutocodeNo) {
        //查询最近扫码的记录 重置属性新增到码库
        VpsVcodeQrcodeLib limitFirstQrcodeLibData = vcodeQrcodeBatchInfoService.queryVcodeQrcodeLibByIsFinishAndTimeDescAndFirst(libName);
        if(limitFirstQrcodeLibData==null){
            limitFirstQrcodeLibData = vcodeQrcodeBatchInfoService.queryVcodeQrcodeLibByIsFinishAndTimeDescAndFirst(libName+"_wan_bak");
        }
        VpsQrcodeLibNameSaveDTO vpsQrcodeLibNameSaveDTO = new VpsQrcodeLibNameSaveDTO();
        vpsQrcodeLibNameSaveDTO.setVcodeKey(UUID.randomUUID().toString());
        vpsQrcodeLibNameSaveDTO.setVcodeActivityKey(limitFirstQrcodeLibData.getVcodeActivityKey());
        vpsQrcodeLibNameSaveDTO.setCompanyKey(limitFirstQrcodeLibData.getCompanyKey());
        vpsQrcodeLibNameSaveDTO.setBatchKey(limitFirstQrcodeLibData.getBatchKey());
        vpsQrcodeLibNameSaveDTO.setPackKey(limitFirstQrcodeLibData.getPackKey());
        vpsQrcodeLibNameSaveDTO.setQrcodeContent(qrcodeContent);
        vpsQrcodeLibNameSaveDTO.setQrcodeType(limitFirstQrcodeLibData.getQrcodeType());
        vpsQrcodeLibNameSaveDTO.setLibName(limitFirstQrcodeLibData.getLibName());
        vpsQrcodeLibNameSaveDTO.setFixedAmount(limitFirstQrcodeLibData.getFixedAmount());
        vpsQrcodeLibNameSaveDTO.setBatchAutocodeNo(batchAutocodeNo);
        vpsQrcodeLibNameSaveDTO.setUseStatus("0");
        //测试要求改成当前时间
        //vpsQrcodeLibNameSaveDTO.setCreDate(DateUtil.parse(limitFirstQrcodeLibData.getCreDate(),""));
        vpsQrcodeLibNameSaveDTO.setCreDate(new Date());
        iVcodeQrcodeLibDao.insertVpsQrcodeLib(vpsQrcodeLibNameSaveDTO);

    }


}
