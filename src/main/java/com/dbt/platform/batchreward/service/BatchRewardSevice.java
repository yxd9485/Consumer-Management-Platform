package com.dbt.platform.batchreward.service;

import static com.dbt.framework.util.DateUtil.DEFAULT_DATE_FORMAT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.log.service.VpsOperationLogService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.HttpReq;
import com.dbt.framework.util.StringUtil;
import com.dbt.framework.util.UUIDTools;
import com.dbt.platform.activity.bean.VcodePrizeBasicInfo;
import com.dbt.platform.activity.service.VcodeActivityBigPrizeService;
import com.dbt.platform.appuser.bean.VpsConsumerUserInfo;
import com.dbt.platform.appuser.service.VpsConsumerAccountInfoService;
import com.dbt.platform.appuser.service.VpsConsumerUserInfoService;
import com.dbt.platform.batchreward.bean.VpsBatchRewardRecord;
import com.dbt.platform.batchreward.bean.VpsBatchRewardRecordDetails;
import com.dbt.platform.batchreward.dao.BatchRewardDao;
import com.dbt.platform.batchreward.dao.BatchRewardDetailsDao;
import com.dbt.platform.prize.bean.MajorInfo;
import com.dbt.platform.prize.service.MajorInfoService;
import com.dbt.platform.sweep.bean.VpsCommonPacksRecord;
import com.dbt.platform.sweep.dao.IVpsCommonPacksRecordDao;
import com.dbt.platform.system.bean.SysUserBasis;


@Service
public class BatchRewardSevice {
    @Autowired
    private BatchRewardDao batchRewardDao;
    @Autowired
    private VpsConsumerUserInfoService vpsConsumerUserInfoService;
    @Autowired
    private VcodeActivityBigPrizeService vcodeActivityBigPrizeService;
    @Autowired
    private IVpsCommonPacksRecordDao commonPacksRecordDao;
    @Autowired
    private VpsConsumerAccountInfoService consumerAccountInfoService;
    @Autowired
    private BatchRewardDetailsDao batchRewardDetailsDao;
    @Autowired
    private MajorInfoService majorInfoService;
    @Autowired
    protected VpsOperationLogService logService;

    public List<VpsBatchRewardRecord> queryForLst(VpsBatchRewardRecord queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        return batchRewardDao.queryForLst(map);
    }

    public int queryForCount(VpsBatchRewardRecord queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return batchRewardDao.queryForCount(map);
    }

    public Map<String, Object> importBatchRewardRecordList(List<List<Object>> readExcel, SysUserBasis currentUser, MultipartFile file) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        // 校验表头及属性名称
        List<Object> objects = readExcel.get(0);
        String[] headers = {"系统角色", "用户姓名", "手机号", "归属大区", "归属经销商",
                "合同年份", "商城积分", "省份", "城市", "区", "详细地址", "指定产品扫码件数", "应发积分",
                "应发金额(元)", "实物奖编号", "实物奖名称", "身份证号", "日期"};
        for (int i = 0; i < headers.length; i++) {
            if (objects.size() <= i || !headers[i].equals(objects.get(i))) {
                resultMap.put("errMsg", "导入失败，请检查Excel模板是否是最新版本！！");
                return resultMap;
            }
        }

        if ((readExcel.size() - 1) <= 0) {
            resultMap.put("errMsg", "导入表格中未检测到数据，请检查表格数据是否存在");
            return resultMap;
        }

        String userRole = null;
        List<VpsConsumerUserInfo> userInfoLst = null;
        List<VpsBatchRewardRecordDetails> list = new ArrayList<>();
        String dateTime = DateUtil.getDateTime();
        StringBuffer errMsgBuffer = new StringBuffer();
        for (int i = 0; i < readExcel.size(); i++) {
            if (i == 0) {
                continue;
            }
            List<Object> batchRewardRecordDetail = readExcel.get(i);
            if (batchRewardRecordDetail.size() <= 0) {
                break;
            }
            VpsBatchRewardRecordDetails instance = new VpsBatchRewardRecordDetails();
            instance.setUserRole(batchRewardRecordDetail.get(0).toString());
            if (StringUtils.isBlank(instance.getUserRole())) {
                errMsgBuffer.append("第" + (i + 1) + "行," + "系统角色不能为空</br>");
                continue;
            } else {
                switch (instance.getUserRole().trim()) {
                case "配送员":
                    userRole = Constant.sfaUserRole.role_0;
                    break;
                case "终端店主":
                    userRole = Constant.sfaUserRole.role_1;
                    break;
                case "经销商":
                    userRole = Constant.sfaUserRole.role_2;
                    break;
                case "分销商":
                    userRole = Constant.sfaUserRole.role_3;
                    break;
                case "网服":
                    userRole = Constant.sfaUserRole.role_4;
                    break;
                case "管理":
                    userRole = Constant.sfaUserRole.role_5;
                    break;
                case "巡查":
                    userRole = Constant.sfaUserRole.role_6;
                    break;
                default:
                    errMsgBuffer.append("第" + (i + 1) + "行，系统角色：" + instance.getUserRole() + "不存在</br>");
                    continue;
                }
            }

            instance.setUserPhone(batchRewardRecordDetail.get(2).toString());
            if (StringUtils.isBlank(instance.getUserPhone())) {
                errMsgBuffer.append("第" + (i + 1) + "行，手机号不能为空，请校验上传文件</br>");
                continue;
            }
            
            // 依据系统角色及手机号校验用记是否已存在
            userInfoLst = vpsConsumerUserInfoService.queryByPhoneNumAndRole(instance.getUserPhone().trim(), userRole);
            if (CollectionUtils.isEmpty(userInfoLst)) {
                errMsgBuffer.append("第" + (i + 1) + "行，依据系统角色及手机号未查询到用户信息</br>");
                continue;
            } else {
                instance.setUserKey(userInfoLst.get(0).getUserKey());
            }

            instance.setUserName(batchRewardRecordDetail.get(1).toString());
            instance.setUserBigregion(batchRewardRecordDetail.get(3).toString());
            instance.setUserDealer(batchRewardRecordDetail.get(4).toString());
            instance.setContractYear(batchRewardRecordDetail.get(5).toString().trim().replace(".00", ""));
            if (StringUtils.isNotBlank(batchRewardRecordDetail.get(6).toString())) {
                instance.setMallVpoints(Long.parseLong(batchRewardRecordDetail.get(6).toString().trim().replace(".00", "")));
            } else {
                instance.setMallVpoints(0);
            }
            instance.setProvince(batchRewardRecordDetail.get(7).toString());
            instance.setCity(batchRewardRecordDetail.get(8).toString());
            instance.setCounty(batchRewardRecordDetail.get(9).toString());
            instance.setAddress(batchRewardRecordDetail.get(10).toString());
            instance.setAppointSkuSweepNum(Long.parseLong(batchRewardRecordDetail.get(11).toString().trim().replace(".00", "")));
            if (batchRewardRecordDetail.get(12) == null || StringUtils.isBlank(batchRewardRecordDetail.get(12).toString())) {
                errMsgBuffer.append("第" + (i + 1) + "行," + "应发积分不能为空，请校验上传文件</br>");
                continue;
            }
            instance.setRewardVpoints(Long.parseLong(batchRewardRecordDetail.get(12).toString().trim().replace(".00", "")));
            if (batchRewardRecordDetail.get(13) == null || StringUtils.isBlank(batchRewardRecordDetail.get(13).toString())) {
                errMsgBuffer.append("第" + (i + 1) + "行," + "应发金额不能为空，请校验上传文件</br>");
                continue;
            }
            instance.setRewardMoney(Double.parseDouble(batchRewardRecordDetail.get(13).toString().trim()));
            instance.setPrizeNo(batchRewardRecordDetail.get(14).toString().trim());
            if (StringUtils.isNotBlank(instance.getPrizeNo()) && null == vcodeActivityBigPrizeService.findBigPrizeByPrizeNo(instance.getPrizeNo())) {
                errMsgBuffer.append("第" + (i + 1) + "行," + "根据实物奖编号未查询到实物奖，请校验上传文件</br>");
                continue;
            }

            instance.setPrizeName(batchRewardRecordDetail.get(15).toString());
            instance.setIdcard(batchRewardRecordDetail.get(16).toString());
            instance.setExcelDate(DateUtil.changeTime(batchRewardRecordDetail.get(17).toString(), DEFAULT_DATE_FORMAT));
            instance.setCreateTime(dateTime);
            list.add(instance);
        }
        
        // 返回校验信息
        if (errMsgBuffer.length() > 0) {
            resultMap.put("errMsg", errMsgBuffer.toString());
            return resultMap;
        }
        
        // 验证通过 将文件上传到服务器
        String fileUrl = HttpReq.uploadImgFile(file, HttpReq.PATH_TYPE.PATH);
        if (StringUtils.isNotEmpty(fileUrl)) {
            resultMap.put("fileUrl", fileUrl);
        } else {
            resultMap.put("errMsg", "上传文件失败！请稍后重试");
            return resultMap;
        }
        resultMap.put("errMsg", "校验通过");
        resultMap.put("resultList", list);
        long userTotal = 0;
        long vpointsTotal = 0;
        double moneyTotal = 0;
        long bigPrizeTotal = 0;
        for (VpsBatchRewardRecordDetails rewardRecordDetail : list) {
            userTotal += 1;
            vpointsTotal += rewardRecordDetail.getRewardVpoints();
            moneyTotal += rewardRecordDetail.getRewardMoney();
            if (StringUtils.isNotBlank(rewardRecordDetail.getPrizeNo())) {
                bigPrizeTotal += 1;
            }
        }
        resultMap.put("userTotal", userTotal);
        resultMap.put("vpointsTotal", vpointsTotal);
        resultMap.put("moneyTotal", moneyTotal);
        resultMap.put("bigPrizeTotal", bigPrizeTotal);

        return resultMap;
    }

    public void executeGrantReward(String rewardDetailJson, SysUserBasis currentUser, String fileUrl,
                                   String userTotal, String vpointsTotal, String moneyTotal, String bigPrizeTotal) {
        // 增加发放记录
        VpsBatchRewardRecord batchRewardRecord = new VpsBatchRewardRecord();
        String infoKey = UUIDTools.getInstance().getUUID();
        batchRewardRecord.setInfoKey(infoKey);
        int count = batchRewardDao.queryCount(DateUtil.getDate(DateUtil.DEFAULT_DATE_FORMAT_MONTH).toString());
        String grantNo = "Jili-" + DateUtil.getDate(DateUtil.DEFAULT_DATE_FORMAT_MONTH_SHT) + StringUtil.lPad(count + 1 + "", "0", 3);
        batchRewardRecord.setGrantNo(grantNo);
        batchRewardRecord.setUserCount(Long.parseLong(userTotal));
        batchRewardRecord.setGrantVpoints(Long.parseLong(vpointsTotal));
        batchRewardRecord.setGrantMoney(Double.parseDouble(moneyTotal));
        batchRewardRecord.setGrantBigprize(Long.parseLong(bigPrizeTotal));
        batchRewardRecord.setFilePath(fileUrl);
        batchRewardRecord.setGrantTime(DateUtil.getDateTime());
        batchRewardRecord.setGrantUser(currentUser.getUserName());
        batchRewardDao.createBatchReward(batchRewardRecord);

        // 修改用户账户信息
        List<VpsBatchRewardRecordDetails> batchRewardRecordDetailsList = JSON.parseArray(rewardDetailJson, VpsBatchRewardRecordDetails.class);
        for (VpsBatchRewardRecordDetails rewardRecordDetails : batchRewardRecordDetailsList) {
            rewardRecordDetails.setBatchRewardKey(infoKey);
            if (rewardRecordDetails.getRewardVpoints() > 0 || rewardRecordDetails.getRewardMoney() > 0) {
                // 添加通用红包记录表记录
                VpsCommonPacksRecord vpsCommonPacksRecord = new VpsCommonPacksRecord();
                vpsCommonPacksRecord.setInfoKey(UUIDTools.getInstance().getUUID());
                vpsCommonPacksRecord.setUserKey(rewardRecordDetails.getUserKey());
                vpsCommonPacksRecord.setEarnTime(DateUtil.getDateTime());
                vpsCommonPacksRecord.setEarnMoney(rewardRecordDetails.getRewardMoney());
                vpsCommonPacksRecord.setEarnVpoints((int) rewardRecordDetails.getRewardVpoints());
                if (rewardRecordDetails.getRewardMoney() > 0 && rewardRecordDetails.getRewardVpoints() > 0) {
                    vpsCommonPacksRecord.setPrizeType("2");
                } else if ((rewardRecordDetails.getRewardMoney() <= 0 && rewardRecordDetails.getRewardVpoints() > 0)) {
                    vpsCommonPacksRecord.setPrizeType("1");
                } else {
                    vpsCommonPacksRecord.setPrizeType("0");
                }
                vpsCommonPacksRecord.setPrizeDesc("专项配送激励");
                vpsCommonPacksRecord.setEarnChannel(Constant.commonPacketChannel.CHANNEL_8);
                vpsCommonPacksRecord.setProvince(rewardRecordDetails.getProvince());
                vpsCommonPacksRecord.setCity(rewardRecordDetails.getCity());
                vpsCommonPacksRecord.setCounty(rewardRecordDetails.getCounty());
                commonPacksRecordDao.create(vpsCommonPacksRecord);

                // 更新账户金额
                consumerAccountInfoService.executeAddUserAccountPoints(rewardRecordDetails.getUserKey(), (int) rewardRecordDetails.getRewardVpoints(), rewardRecordDetails.getRewardMoney());
            }
            if (StringUtils.isNotBlank(rewardRecordDetails.getPrizeNo())) {
                // 添加实物奖记录
                VcodePrizeBasicInfo bigPrizeByPrizeNo = vcodeActivityBigPrizeService.findBigPrizeByPrizeNo(rewardRecordDetails.getPrizeNo());
                MajorInfo majorInfo = new MajorInfo();
                majorInfo.setInfoKey(UUID.randomUUID().toString());
                majorInfo.setCompanyKey(currentUser.getCompanyKey());
                majorInfo.setUserKey(rewardRecordDetails.getUserKey());
                majorInfo.setGrandPrizeType(rewardRecordDetails.getPrizeNo());
                majorInfo.setContractYear(rewardRecordDetails.getContractYear());
                majorInfo.setPrizeImg(bigPrizeByPrizeNo.getPrizeListPic());
                majorInfo.setPrizeName(bigPrizeByPrizeNo.getPrizeName());
                majorInfo.setEarnTime(DateUtil.getDateTime());
                majorInfo.setEarnMoney(rewardRecordDetails.getRewardMoney().toString());
                majorInfo.setPrizeVcode(UUIDTools.getInstance().getUUID());
                majorInfo.setProvince(rewardRecordDetails.getProvince());
                majorInfo.setCity(rewardRecordDetails.getCity());
                majorInfo.setCounty(rewardRecordDetails.getCounty());
                majorInfo.setUseStatus("0");
                majorInfo.setExchangeChannel(Constant.exchangeChannel.CHANNEL_14);
                if (StringUtils.isNotBlank(bigPrizeByPrizeNo.getCashPrizeEndDay())) {
                    majorInfo.setExpireTime(DateUtil.getDateTime(DateUtil.addDays(Integer.parseInt(bigPrizeByPrizeNo.getCashPrizeEndDay())), DateUtil.DEFAULT_DATETIME_FORMAT));
                } else if (StringUtils.isNotBlank(bigPrizeByPrizeNo.getCashPrizeEndTime())) {
                    majorInfo.setExpireTime(bigPrizeByPrizeNo.getCashPrizeEndTime());
                }
                majorInfoService.addFirstPrizeRecord(majorInfo);
            }

        }
        // 添加激励发放详细记录
        batchRewardDetailsDao.batchCreate(batchRewardRecordDetailsList);
        logService.saveLog("batchReward", Constant.OPERATION_LOG_TYPE.TYPE_1, infoKey, "发放专项激励:发放激励(元)" +
                batchRewardRecord.getGrantMoney() + " 发放积分" + batchRewardRecord.getGrantVpoints() + " 发放实物奖" + batchRewardRecord.getGrantBigprize());

    }
}
