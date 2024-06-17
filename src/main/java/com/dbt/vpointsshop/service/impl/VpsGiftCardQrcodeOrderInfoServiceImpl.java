package com.dbt.vpointsshop.service.impl;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.dao.ICommonDao;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.*;
import com.dbt.platform.autocode.tools.CompressFileTools;
import com.dbt.vpointsshop.bean.VpointsGoodsGiftCardCog;
import com.dbt.vpointsshop.bean.VpsGiftCardQrcodeInfo;
import com.dbt.vpointsshop.bean.VpsGiftCardQrcodeOrderInfo;
import com.dbt.vpointsshop.dao.IVpointsGoodsGiftCardCogDao;
import com.dbt.vpointsshop.dao.IVpsGiftCardQrcodeOrderInfoDao;
import com.dbt.vpointsshop.dto.VpsGiftCardQrcodeOrderInfoDTO;
import com.dbt.vpointsshop.dto.VpsGiftCardQrcodeOrderInfoVO;
import com.dbt.vpointsshop.service.IVpsGiftCardQrcodeInfoService;
import com.dbt.vpointsshop.service.IVpsGiftCardQrcodeOrderInfoService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

import static com.dbt.framework.base.bean.Constant.OrderNoType.type_GF;
import static com.dbt.framework.util.MailUtil.sendMail;

/**
 * <p>
 * 礼品卡卡号订单表 服务实现类
 * </p>
 *
 * @author wangshuda
 * @since 2022-08-15
 */
@Service
public class VpsGiftCardQrcodeOrderInfoServiceImpl extends BaseService<VpsGiftCardQrcodeOrderInfo> implements IVpsGiftCardQrcodeOrderInfoService {

    private Logger log = Logger.getLogger(VpsGiftCardQrcodeOrderInfoServiceImpl.class);
    private final static String COMMON_PATH = PropertiesUtil.getPropertyValue("gift_card_qrcode_common_path");

    @Autowired
    private IVpsGiftCardQrcodeOrderInfoDao qrcodeOrderInfoDao;
    @Autowired
    private IVpsGiftCardQrcodeInfoService giftCardQrcodeInfoService;
    @Autowired
    private IVpointsGoodsGiftCardCogDao iVpointsGoodsGiftCardCogDao;
    @Autowired
    private ICommonDao commonDao;
    public String getRuleNo() {
        return this.getBussionNo("giftCardQrcodeOrder", "qrcode_order_key", type_GF);
    }
    /**
     * 列表
     */
    @Override
    public List<VpsGiftCardQrcodeOrderInfoVO> queryForLst(VpsGiftCardQrcodeOrderInfoVO queryBean, PageOrderInfo pageInfo) throws Exception {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("queryBean", queryBean);
        queryMap.put("pageInfo", pageInfo);

        return qrcodeOrderInfoDao.queryForLst(queryMap);
    }

    /**
     * 列表记录总条数
     */
    @Override
    public int queryForCount(VpsGiftCardQrcodeOrderInfoVO queryBean) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("queryBean", queryBean);
        return qrcodeOrderInfoDao.queryForCount(queryMap);
    }

    public VpsGiftCardQrcodeOrderInfo findById(String qrcodeOrderKey) {
        VpsGiftCardQrcodeOrderInfo byId = qrcodeOrderInfoDao.findById(qrcodeOrderKey);
        if(StringUtils.isNotEmpty(byId.getStartDate())){
            byId.setStartDate(byId.getStartDate().split(" ")[0]);
        }
        if(StringUtils.isNotEmpty(byId.getEndDate())){
            byId.setEndDate(byId.getEndDate().split(" ")[0]);
        }
        return byId;
    }

    /**
     * 创建码源
     */
    @Override
    public void addOrderInfo(VpsGiftCardQrcodeOrderInfoVO orderInfo, String userKey) {

        // 每批次最多100万
        if (orderInfo.getQrcodeNum() > 1000000) {
            throw new BusinessException("单次兑付卡码源最大个数100万！");
        }

        // 初始化订单
        orderInfo.setQrcodeOrderKey(getRuleNo());
        orderInfo.setOrderStatus(Constant.QRCODE_ORDER_STATUS.order_status_0);
        orderInfo.setUpdateUser(userKey);
        orderInfo.setUpdateTime(DateUtil.getDateTime());
        orderInfo.setDeleteFlag("0");
        orderInfo.setCreateUser(userKey);
        orderInfo.setCreateTime(DateUtil.getDateTime());
        if(StringUtils.isNotEmpty(orderInfo.getEndDate())){
            orderInfo.setEndDate(DateUtil.getEndDateTimeByDay(orderInfo.getEndDate()));
        }
        qrcodeOrderInfoDao.insert(orderInfo);

        logService.saveLog("giftCardQrcodeOrder", Constant.OPERATION_LOG_TYPE.TYPE_1,
                JSON.toJSONString(orderInfo), "创建兑付卡码源订单:" + orderInfo.getOrderName());
    }

    /**
     * 修改码源
     */
    @Override
    public String updateOrderInfo(VpsGiftCardQrcodeOrderInfo orderInfo) {
        String msg = "修改成功";
        // 每批次最多100万
        if (orderInfo.getQrcodeNum() > 1000000) {
            throw new BusinessException("单次兑付卡码源最大个数100万！");
        }

        // 已生成过不能修改码源个数
        VpsGiftCardQrcodeOrderInfo oldOrderInfo = qrcodeOrderInfoDao.findById(orderInfo.getQrcodeOrderKey());
        if (Constant.QRCODE_ORDER_STATUS.order_status_1.equals(oldOrderInfo.getOrderStatus())
                && orderInfo.getQrcodeNum().intValue() != oldOrderInfo.getQrcodeNum().intValue()) {
            throw new BusinessException("兑付卡码源已生成过，不能修改订单数量");
        }

        if (!"1".equals(oldOrderInfo.getActivateStatus())) {
            if(StringUtils.isNotEmpty(orderInfo.getActivateUser())
                    && StringUtils.isNotEmpty(orderInfo.getStartDate()) && StringUtils.isNotEmpty(orderInfo.getEndDate())){
                msg = "成功激活";
            }
        }
        if(StringUtils.isNotEmpty(orderInfo.getActivateUser())
            && StringUtils.isNotEmpty(orderInfo.getStartDate()) && StringUtils.isNotEmpty(orderInfo.getEndDate())){
            orderInfo.setActivateStatus("1");
        }
        // 更新主表信息
        orderInfo.setEndDate(DateUtil.getEndDateTimeByDay(orderInfo.getEndDate()));
        qrcodeOrderInfoDao.update(orderInfo);

        logService.saveLog("giftCardQrcodeOrder", Constant.OPERATION_LOG_TYPE.TYPE_2,
                JSON.toJSONString(orderInfo), "修改兑付卡码源订单:" + orderInfo.getOrderName());
        return msg;
    }

    /**
     * 删除生成码源记录
     */
    @Override
    public void deleteOrderInfoById(String orderKey, String optUserKey) throws Exception {
        String[] orderKeyAry = orderKey.split(",");
        VpsGiftCardQrcodeOrderInfo orderInfo = null;
        for (String itemOrderKey : orderKeyAry) {
            orderInfo = qrcodeOrderInfoDao.findById(itemOrderKey);
            if (!Constant.QRCODE_ORDER_STATUS.order_status_0.equals(orderInfo.getOrderStatus())
                    && !Constant.QRCODE_ORDER_STATUS.order_status_2.equals(orderInfo.getOrderStatus())) {
                throw new Exception(orderInfo.getOrderName() + "订单状态异常，不能删除！");
            }

            // 删除订单
            qrcodeOrderInfoDao.deleteById(itemOrderKey);
            // 物理删除订单相关的兑付卡码源
            qrcodeOrderInfoDao.removeGiftCardQrcodeByOrderKey(itemOrderKey);

            logService.saveLog("giftCardQrcodeOrder", Constant.OPERATION_LOG_TYPE.TYPE_3,
                    JSON.toJSONString(orderInfo), "删除兑付卡码源订单:" + orderInfo.getOrderName());
        }
    }

    /**
     * 生成码
     * @throws Exception
     */
    @Override
    public void createCodeMain(VpsGiftCardQrcodeOrderInfo orderInfo, String optUserKey) throws Exception {

        // 更新订单状态为生成中
        String orderKey = orderInfo.getQrcodeOrderKey();
        orderInfo.setOrderStatus(Constant.QRCODE_ORDER_STATUS.order_status_3);
        qrcodeOrderInfoDao.update(orderInfo);

        // 订单创建时间
        String date = DateUtil.getDateTime(orderInfo.getCreateTime(), DateUtil.DEFAULT_DATE_FORMAT_SHT);
        // 生成码源订单路径，格式：/data/upload/giftCardQrcode/20180908（日期）/20180906153508123（订单编号）
        String filePath = COMMON_PATH + "/" + date + "/" + orderKey;

        // 失败时重新生成，需删除之前的数据
        if(Constant.QRCODE_ORDER_STATUS.order_status_2.equals(orderInfo.getOrderStatus())){

            // 删除订单生成的文件
            FileOperateUtil.delAllFile(filePath);

            // 删除订单
            qrcodeOrderInfoDao.deleteById(orderKey);
            // 物理删除订单相关的兑付卡码源
            qrcodeOrderInfoDao.removeGiftCardQrcodeByOrderKey(orderKey);
        }

        // 生成码源文件
        String separator = System.getProperty("line.separator");
        DpiUtil dpiUtil = new DpiUtil();
        String giftCardQrcode = null;
        String giftCardQrcodeUrl = PropertiesUtil.getPropertyValue("project_gift_card_qrcode_url")+"/" + DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_COMPANY_INFO,
                DatadicKey.filterCompanyInfo.PROJECT_FLAG) + "/GIFTCARD/";;
        String giftCardQrcodePath = filePath + "/1source";
        String giftCardQrcodeImg = null;
        StringBuffer buffer = new StringBuffer();
        File file = new File(giftCardQrcodePath); file.mkdirs();
        String dataFileName = filePath + "/" + orderKey + ".txt";
        FileOutputStream dataFile = new FileOutputStream(dataFileName);
        BufferedWriter qrcodeFile = new BufferedWriter(new FileWriter(giftCardQrcodePath + "/" + orderKey + ".txt")) ;
        try {
            for (int i = 0; i < orderInfo.getQrcodeNum(); i++) {
                giftCardQrcode = UUID.randomUUID().toString().replaceAll("-","");
                String mm = DateUtil.getDate("MM");
                String yy = DateUtil.getDate("yy");
                String bussionNoPrefix = "NO." + yy + mm + "01";
                Map<String, Object> map = new HashMap<>();
                map.put("tableName","vps_gift_card_qrcode_info");
                map.put("bussionNoCol", "order_no");
                map.put("bussionNoPrefix", bussionNoPrefix);
                int maxCount = commonDao.getCount(map) + 1;
                String bussionNo = bussionNoPrefix + StringUtil.lPad(maxCount+"", "0", 6);

                // 生成码源文件及图片
                qrcodeFile.write(giftCardQrcodeUrl + giftCardQrcode+","+bussionNo);
                qrcodeFile.newLine();
                giftCardQrcodeImg = giftCardQrcodePath + "/giftCardQrcode" + (i + 1) + ".png";
                QrCodeUtil.encodeQRCodeImage(giftCardQrcodeUrl + giftCardQrcode, null, giftCardQrcodeImg, 283, 283, null);
                dpiUtil.saveGridImage(new File(giftCardQrcodeImg));
                // 目前不需要程序合并印刷的背景图片
                //new BgImgUtilType1().initBgImg(giftCardQrcodeImg, "");

                VpsGiftCardQrcodeInfo qrcodeInfo = new VpsGiftCardQrcodeInfo();
                qrcodeInfo.setOrderNo(bussionNo);
                qrcodeInfo.setQrcodeOrderKey(orderKey);
                qrcodeInfo.setGiftCardQrcode(giftCardQrcode);
                qrcodeInfo.setCreateTime(DateUtil.getNow());
                giftCardQrcodeInfoService.save(qrcodeInfo);
                // 写入data文件
                buffer.append(giftCardQrcode).append("+").append(orderKey).append("+").append(separator);
                dataFile.write(buffer.toString().getBytes());
            }

        } catch (Exception e) {
            log.error("生成兑付卡码源异常", e);
            throw e;
        } finally {
            dataFile.flush();
            dataFile.close();

            qrcodeFile.flush();
            qrcodeFile.close();
        }

        // 生成的码源文件入库
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("tmpFilePath", dataFileName);
//        qrcodeOrderInfoDao.writeQrcodeToData(map);

        // 压缩文件
        String fileName = orderInfo.getOrderName() + "_"+ orderInfo.getQrcodeNum();
        String password = getCharAndNumr();
        CompressFileTools.compressFile(filePath, orderKey, date, fileName, "1source", password);
        VpointsGoodsGiftCardCog vpointsGoodsGiftCardCog = iVpointsGoodsGiftCardCogDao.selectById(orderInfo.getGiftCardInfoKey());
        VpsGiftCardQrcodeOrderInfoDTO dto = new VpsGiftCardQrcodeOrderInfoDTO();
        BeanUtils.copyProperties(orderInfo, dto);
        dto.setGiftCardName(vpointsGoodsGiftCardCog.getGiftCardName());
        dto.setPassword(password);
        dto.setPasswordEmailName(DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_COMPANY_INFO,
                DatadicKey.filterCompanyInfo.PROJECT_NAME));
        dto.setPasswordEmail(DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_COMPANY_INFO,
                DatadicKey.filterCompanyInfo.PROJECT_EMAIL_GIFT_CARD));
        dto.setCsEmail(DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_COMPANY_INFO,
                DatadicKey.filterCompanyInfo.PROJECT_EMAIL));
        sendMail(dto);

        // 修改订单状态为成功
        orderInfo.setOrderStatus(Constant.QRCODE_ORDER_STATUS.order_status_1);
        qrcodeOrderInfoDao.update(orderInfo);

        // 1. 删除码源源文件
        FileOperateUtil.deleteFile(dataFileName);
        FileOperateUtil.delFolder(filePath + "/1source");

        logService.saveLog("giftCardQrcodeOrder", Constant.OPERATION_LOG_TYPE.TYPE_1, orderKey, "码源生成:" + fileName);
    }

    /**
     * 重置兑付卡码源
     * @param giftCardQrcode
     */
    public void resetGiftCardQrcode(String giftCardQrcode) {
        qrcodeOrderInfoDao.resetGiftCardQrcode(giftCardQrcode);
    }

    /**
     * 随机六位密码
     * @return
     */
    private static String getCharAndNumr() {
        Random random = new Random();
        String valSb = new String();
        for (int i = 1; i <= 6; i++) {
            if (i % 2 == 0) {
                valSb += (char) (97 + random.nextInt(26));
            } else {
                valSb += String.valueOf(random.nextInt(10));
            }
        }
        return valSb;
    }
}
