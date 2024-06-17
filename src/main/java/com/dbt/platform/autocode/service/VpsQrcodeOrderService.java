package com.dbt.platform.autocode.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CheckUtil;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.DpiUtil;
import com.dbt.framework.util.FileOperateUtil;
import com.dbt.framework.util.LabelImage;
import com.dbt.framework.util.MailUtil;
import com.dbt.framework.util.PerformanceUtil;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.util.QrCodeUtil;
import com.dbt.framework.util.RandomUtils;
import com.dbt.framework.util.ReflectUtil;
import com.dbt.framework.util.UUIDTools;
import com.dbt.platform.activity.bean.VcodeQrcodeBatchInfo;
import com.dbt.platform.activity.bean.VcodeQrcodePackInfo;
import com.dbt.platform.activity.dao.IVcodeActivityLibRelationDao;
import com.dbt.platform.activity.dao.IVcodeQrcodeBatchInfoDao;
import com.dbt.platform.activity.dao.IVcodeQrcodeLibDao;
import com.dbt.platform.activity.dao.IVcodeQrcodePackInfoDao;
import com.dbt.platform.activity.service.VcodeQrcodeBatchInfoService;
import com.dbt.platform.autocode.bean.VpsQrcodeOrder;
import com.dbt.platform.autocode.dao.IVpsQrcodeOrderDao;
import com.dbt.platform.autocode.tools.CompressFileTools;
import com.dbt.platform.autocode.tools.MergeFileTools;
import com.dbt.platform.autocode.tools.TxtConvertMdbTools;
import com.dbt.platform.autocode.util.RandomDataUtil;
import com.dbt.platform.autocode.util.ShuffleUtil;
import com.dbt.platform.codefactory.bean.VpsVcodeFactory;
import com.dbt.platform.codefactory.bean.VpsWinery;
import com.dbt.platform.codefactory.dao.IVpsVcodeFactoryDao;
import com.dbt.platform.codefactory.dao.IVpsVcodeWineryDao;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 码源订单Service
 *
 * @author hanshimeng
 */
@Service("vpsQrcodeOrderService")
public class VpsQrcodeOrderService extends BaseService<VpsQrcodeOrder> {

    private final static String COMMON_PATH = PropertiesUtil.getPropertyValue("create_code_common_path");
    static ConcurrentLinkedQueue<String> queueTxt = new ConcurrentLinkedQueue<String>();

    @Autowired
    private IVpsQrcodeOrderDao vpsQrcodeOrderDao;
    @Autowired
    private IVcodeQrcodeLibDao vcodeQrcodeLibDao;
    @Autowired
    private IVcodeQrcodeBatchInfoDao vcodeQrcodeBatchInfoDao;
    @Autowired
    private IVcodeQrcodePackInfoDao vcodeQrcodePackInfoDao;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private IVpsVcodeFactoryDao iVpsVcodeFactoryDao;
    @Autowired
    private IVpsVcodeWineryDao iVpsVcodeWineryDao;
    @Autowired
    private IVcodeQrcodeBatchInfoDao batchInfoDao;
    @Autowired
    private VcodeQrcodeBatchInfoService vcodeQrcodeBatchInfoService;
    @Autowired
    private IVcodeQrcodePackInfoDao packInfoDao;
    @Autowired
    private IVcodeActivityLibRelationDao libRelationDao;

    @Autowired
    private BusinessService businessService;
    private final static Random random = new Random();

    /**
     * 码源订单列表
     *
     * @param vpsQrcodeOrder
     * @param pageInfo
     * @return
     */
    public List<VpsQrcodeOrder> queryForLst(VpsQrcodeOrder queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        return vpsQrcodeOrderDao.queryForLst(map);
    }

    /**
     * 码源订单列表count
     *
     * @param vpsQrcodeOrder
     * @param pageInfo
     * @return
     */
    public int queryForCount(VpsQrcodeOrder queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return vpsQrcodeOrderDao.queryForCount(map);
    }

    /**
     * 删除码源订单
     *
     * @param orderKey
     */
    public void deleteById(String orderKey) {

        VpsQrcodeOrder vpsQrcodeOrder = findById(orderKey);
        if (!Constant.QRCODE_ORDER_STATUS.order_status_0.equals(vpsQrcodeOrder.getOrderStatus())) {
            throw new BusinessException("该订单无法修改!");
        }

        // 标签路径
        String filePath = COMMON_PATH + "/" + vpsQrcodeOrder.getCreateTime().split(" ")[0].replaceAll("-", "") + "/"
                + DbContextHolder.getDBType() + "/" + vpsQrcodeOrder.getOrderKey();
        // 删除标签
        FileOperateUtil.delFolder(filePath);

        List<VcodeQrcodeBatchInfo> batchList = vcodeQrcodeBatchInfoDao.queryQrcodeBatchByOrderKey(orderKey);
        for (VcodeQrcodeBatchInfo info : batchList) {
            // 删除码包表
            packInfoDao.removeByBatchKey(info.getBatchKey());
            // 先删除相关的lib_relation及包记录
            libRelationDao.removeByBatchKey(info.getBatchKey());
        }
        // 清空批次订单主键
        vcodeQrcodeBatchInfoDao.removeByOrderKey(orderKey);
        // 删除订单
        vpsQrcodeOrderDao.deleteById(orderKey);

        logService.saveLog("vcodeActivityHotArea", Constant.OPERATION_LOG_TYPE.TYPE_3, orderKey, "删除未生成的码源订单");
    }

    /**
     * 根据主键查询码源订单
     *
     * @param orderKey
     * @return
     */
    public VpsQrcodeOrder findById(String orderKey) {
        return vpsQrcodeOrderDao.findById(orderKey);
    }

    /**
     * 根据主键查询码源订单
     *
     * @param orderKey
     * @return
     */
    public VpsQrcodeOrder findByOrderNo(String orderNo) {
        return vpsQrcodeOrderDao.findByOrderNo(orderNo);
    }

    /**
     * 修改订单状态
     *
     * @param orderKey
     * @param orderStatus 不管是否存在事务,都创建一个新的事务,原来的挂起,新的执行完毕,继续执行老的事务
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateOrderStatus(String orderKey, String orderStatus) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderKey", orderKey);
        map.put("orderStatus", orderStatus);
        vpsQrcodeOrderDao.updateOrderStatus(map);

        // 状态为失败时初始批次的生成状态
        if (Constant.QRCODE_ORDER_STATUS.order_status_2.equals(orderStatus)) {
            // 修改批次的生成状态
            map.put("vcodeFlag", "0");
            vcodeQrcodeBatchInfoDao.updateBatchForOrderKey(map);
        }
    }

    /**
     * 更新码源回传状态
     *
     * @param orderKey
     * @param importFlag
     * @param optUserKey
     */
    public void updateOrderImportFlag(String orderKey, String importFlag, String optUserKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderKey", orderKey);
        map.put("importFlag", importFlag);
        map.put("optUserKey", optUserKey);
        vpsQrcodeOrderDao.updateOrderImportFlag(map);
    }

    /**
     * 重命名码库表
     *
     * @param libNameList
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void renameTableByLibNameList(List<String> libNameList) {
        Map<String, Object> map = new HashMap<>();
        map.put("dateTime", new DateTime().toString("MMddHHmmss"));
        for (String libName : libNameList) {
            map.put("libName", libName);
            if (vcodeQrcodeLibDao.findTableIsExist(map) > 0) {
                vcodeQrcodeLibDao.renameTable(map);
            }

            map.put("libName", libName + "_wan_bak");
            if (vcodeQrcodeLibDao.findTableIsExist(map) > 0) {
                vcodeQrcodeLibDao.renameTable(map);
            }
        }
    }

    /**
     * 码源生成主方法
     *
     * @param orderKey
     * @param model
     * @throws Exception
     */
    public Model createCodeMain(VpsQrcodeOrder qrcodeOrder, VpsVcodeFactory vcodeFactory, String optUserKey, Model model) throws Exception {
        if (null == qrcodeOrder) {
            throw new BusinessException("码源生成失败：订单不存在");
        }
        if (Constant.QRCODE_ORDER_STATUS.order_status_3.equals(qrcodeOrder.getOrderStatus())) {
            throw new BusinessException("码源生成失败：码源生成进行中...，请勿再次生成");
        }

        // 非自动生成时
        Map<String, Object> map = new HashMap<>();
        if (!"AUTO".equals(qrcodeOrder.getCreateUser())) {
            map.put("orderStatus", Constant.QRCODE_ORDER_STATUS.order_status_3);
            List<VpsQrcodeOrder> orderList = vpsQrcodeOrderDao.queryByList(map);
            if (CollectionUtils.isNotEmpty(orderList)) {
                throw new BusinessException("码源生成失败：其他码源订单生成未结束，请稍后再试...");
            }
        }

        // 码源生成所需批次
        String orderKey = qrcodeOrder.getOrderKey();
        List<VcodeQrcodeBatchInfo> batchList = vcodeQrcodeBatchInfoDao.queryQrcodeBatchByOrderKey(orderKey);
        if (CollectionUtils.isEmpty(batchList)) {
            throw new BusinessException("码源生成失败：订单关联批次为空，无法继续生成码源");
        }

        // 查询SKU信息
        SkuInfo skuInfo = skuInfoService.findById(qrcodeOrder.getSkuKey());

        // 订单创建时间
        String date = qrcodeOrder.getCreateTime().split(" ")[0].replaceAll("-", "");
        // 生成码源订单路径，格式：/data/upload/autocode/20180908（日期）/20180906153508123（订单编号）
        String filePath = COMMON_PATH + "/" + date + "/" + DbContextHolder.getDBType() + "/" + orderKey;

        // 失败时重新生成，需删除之前的数据
        if (Constant.QRCODE_ORDER_STATUS.order_status_2.equals(qrcodeOrder.getOrderStatus())) {
            // 重命名码库表数据
            if (!"AUTO".equals(qrcodeOrder.getCreateUser())) {
                List<String> libNameList = ReflectUtil.getFieldsValueByName("libName", batchList);
                renameTableByLibNameList(libNameList);
            }

            // 删除码库表（弃用）
//			map.clear();
//			map.put("libNameList", libNameList);
//			vcodeQrcodeLibDao.deleteByLibNameList(map);

            // 删除订单生成的文件
            FileOperateUtil.delAllFile(filePath);
        }

        // 修改订单状态为进行中
        updateOrderStatus(orderKey, Constant.QRCODE_ORDER_STATUS.order_status_3);

        // 是否一万一批次
        boolean isWanFlag = false;
        if ("1".equals(qrcodeOrder.getIsWanBatch())) {
            isWanFlag = true;
        }

        // 生成码源
        produceCode(filePath, qrcodeOrder, batchList, skuInfo, date, isWanFlag);

        // 更新批次状态.
        map.clear();
        map.put("vcodeFlag", "1");
        map.put("batchKeyList", ReflectUtil.getFieldsValueByName("batchKey", batchList));
        map.put("wanBatchFlag", vcodeFactory.getIsWanBatch());
        vcodeQrcodeBatchInfoDao.updateBatchForMap(map);

        // 该订单二维码的数量
        int orderQrcodeCount = ReflectUtil.getFieldsValueTotalByName("vcodeCounts", batchList);
        int realVcodeCounts = ReflectUtil.getFieldsValueTotalByName("realVcodeCounts", batchList);

        // 目录编号：1source,2merge,3convert,4compress
        String directory = "1source";

        // 文件处理
        if ("1".equals(vcodeFactory.getQrcodeMerageFlag())) {

            directory = "2merge";
            int channelCount = qrcodeOrder.getChannelCount();
            if (channelCount > 1) {
                // 多通道合并文件
                MergeFileTools.FileMerge(filePath, orderKey, date, channelCount, orderQrcodeCount, 0);
            } else {
                // 普通合并文件
//				MergeFileTools.FileMerge(orderKey, date);

                if (orderQrcodeCount <= 1000000) {
                    channelCount = 1;
                } else {
                    if (orderQrcodeCount % 1000000 == 0) {
                        channelCount = orderQrcodeCount / 1000000;
                    } else {
                        channelCount = orderQrcodeCount / 1000000 + 1;
                    }
                }
                MergeFileTools.FileMerge(filePath, orderKey, date, channelCount, orderQrcodeCount, 1000000);
            }
        }

        // txt转mdb
        if ("mdb".equals(qrcodeOrder.getFileFormat())) {
            TxtConvertMdbTools.txtConvertMdb(filePath, orderKey, date, directory);
            directory = "3convert";
        } else if ("csv".equals(qrcodeOrder.getFileFormat())) {
            TxtConvertMdbTools.txtConvertCSV(filePath, orderKey, date, directory);
            directory = "3convert";
        }

        // 压缩文件
        String fileName = qrcodeOrder.getOrderNo() + "_" + qrcodeOrder.getOrderName();
        CompressFileTools.compressFile(filePath, orderKey, date,
                fileName, directory, qrcodeOrder.getPackagePassword());

        // 生成的码源数量
        map.clear();
        map.put("wanbak", "");
        if (isWanFlag) {
            map.put("wanbak", "_wan_bak");
        }
        map.put("libNameList", ReflectUtil.getFieldsValueByName("libName", batchList));
        map.put("batchKeyList", ReflectUtil.getFieldsValueByName("batchKey", batchList));
        int dataQrcodeCount = vcodeQrcodeLibDao.findQrcodeCountByLibNames(map);

        // 实际生成的二维码和该订单所需二维码数量相差结果小于0，则视为异常
        if (dataQrcodeCount - realVcodeCounts < 0) {
            throw new BusinessException("生成码源数量异常：</br>订单二维码数量：" + orderQrcodeCount + "</br>实际生成码源量：" + dataQrcodeCount);
        }

        // 修改订单状态为成功
        updateOrderStatus(orderKey, Constant.QRCODE_ORDER_STATUS.order_status_1);
        if (model != null)
            model.addAttribute("errorMsg", "码源生成成功：</br>订单二维码数量：" + orderQrcodeCount + "</br>实际生成码源量：" + dataQrcodeCount);

        // 订单生成成功，非一万一批次的批次转到回传库状态下
        if ("0".equals(qrcodeOrder.getIsWanBatch())) {

            // 更新码源订单的回传状态为已回传
            updateOrderImportFlag(orderKey, "1", optUserKey);

            // 更新当前订单下的批次导入状态为已导入
            map.clear();
            map.put("importFlag", "1");
            map.put("importTime", DateUtil.getDateTime());
            map.put("status", Constant.batchStatus.status3);
            map.put("orderKey", orderKey);
            vcodeQrcodeBatchInfoDao.updateBatchForOrderKey(map);
        }

        // 删除生成码源的过程数据（1source、2merge、3convert和tempqrcode）
        // 1. 删除码源源文件
        FileOperateUtil.delFolder(filePath + "/1source");
        // 2. 删除码源合并文件
        FileOperateUtil.delFolder(filePath + "/2merge");
        // 3. 删除码源转换MDB文件
        if ("mdb".equals(qrcodeOrder.getFileFormat())
                || "csv".equals(qrcodeOrder.getFileFormat())) {
            FileOperateUtil.delFolder(filePath + "/3convert");
        }
        // 4. 删除码源入库文件
        FileOperateUtil.delFolder(filePath + "/tempqrcode");

        logService.saveLog("vpsQrcodeOrder", Constant.OPERATION_LOG_TYPE.TYPE_1,
                qrcodeOrder.getOrderKey(), "码源生成:" + fileName);
        return model;
    }

    /**
     * 生成二维码
     *
     * @param filePath    码源订单所在路径
     * @param qrcodeOrder 订单
     * @param batchList   批次
     * @param date        时间
     * @param isWanFlag   是否一万一批次
     * @throws Exception
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void produceCode(String filePath, VpsQrcodeOrder qrcodeOrder,
                            List<VcodeQrcodeBatchInfo> batchList, SkuInfo skuInfo, String date, boolean isWanFlag) throws Exception {
        File tempText = null;
//		File qrcodeFile = null; // 码源文件
        FileWriterWithEncoding qrcodeFileWiter = null; // 码源文件
        BufferedWriter qrcodeBufferWriter = null;
        String tmpFilePath = null;
        FileOutputStream datafileOut = null;
        Map<String, Object> map = new HashMap<>();
        String qrcodeFormat = qrcodeOrder.getQrcodeFormat();
        String qrcodeType = qrcodeOrder.getQrcodeType();
        String sourceFilePath = filePath + "/1source";
        String tempqrcodeFilePath = filePath + "/tempqrcode";
        File file = new File(sourceFilePath);
        file.mkdirs();
        file = new File(tempqrcodeFilePath);
        file.mkdirs();
        String separator = System.getProperty("line.separator");
        StringBuilder qrcodeStr = new StringBuilder();
        String autocodeNo = null;
        // 取sku订单前缀
        String autocodeNoPrefix = "";
        DpiUtil dpiUtil = new DpiUtil();
        //获取SKU序列号
        long autocodeNoCount = 0L;
        boolean autocodeFlag = false;
        if (Constant.QRCODE_FORMAT.format_7.equals(qrcodeFormat) || Constant.QRCODE_FORMAT.format_8.equals(qrcodeFormat) ||
                Constant.QRCODE_FORMAT.format_9.equals(qrcodeFormat) || Constant.QRCODE_FORMAT.format_10.equals(qrcodeFormat) ||
                Constant.QRCODE_FORMAT.format_11.equals(qrcodeFormat) || Constant.QRCODE_FORMAT.format_12.equals(qrcodeFormat) ||
                Constant.QRCODE_FORMAT.format_13.equals(qrcodeFormat) || Constant.QRCODE_FORMAT.format_14.equals(qrcodeFormat) ||
                Constant.QRCODE_FORMAT.format_15.equals(qrcodeFormat) || Constant.QRCODE_FORMAT.format_17.equals(qrcodeFormat)) {
            autocodeFlag = true;
            if (StringUtils.isBlank(skuInfo.getAutocodeNo())) {
                autocodeNoCount = 0L;

                //如果大于八位先获取前缀否则直接获取编号
            } else if (skuInfo.getAutocodeNo().length() > 8) {
                int index = skuInfo.getAutocodeNo().indexOf(skuInfo.getAutocodeNo().substring(skuInfo.getAutocodeNo().length() - 8));
                autocodeNoPrefix = skuInfo.getAutocodeNo().substring(0, index);
                autocodeNoCount = Long.parseLong(skuInfo.getAutocodeNo().substring(skuInfo.getAutocodeNo().length() - 8));
            } else {
                autocodeNoCount = Long.parseLong(skuInfo.getAutocodeNo());
            }
        }
//		String autocodeNoPrefix = null;
//		DpiUtil dpiUtil = new DpiUtil();
//		long autocodeNoCount = 0L;
//		if("laobaifj".equals(DbContextHolder.getDBType())){
//			autocodeNoPrefix = skuInfo.getAutocodeNo().substring(0, 4);
//			autocodeNoCount = Long.parseLong(skuInfo.getAutocodeNo().substring(4));
//		}
        PerformanceUtil.begin();

        // 二维码url
        // 测试：http://xt.vjifen.com/qr?c=p&v=二维码
        // 线上：HTTP://VJ1.TV/p/二维码
        String qrcodeUrl = DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_HTTP_URL,
                DatadicKey.filterHttpUrl.QRCODE_URL);
        String projectFlag = DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_COMPANY_INFO,
                DatadicKey.filterCompanyInfo.PROJECT_FLAG);

        // 河南兼容HP和h，默认为瓶装HP
        if ("henanpz".equals(DbContextHolder.getDBType())) {
            if (skuInfo.getSkuName().contains("罐")) {
                projectFlag = "h";
            }
        }
        String url = qrcodeUrl + projectFlag;
        if ("zuijiu".equals(DbContextHolder.getDBType()) && projectFlag.equals("JZ")) {
            url = url + "/" + qrcodeType;
        }
        if (qrcodeUrl.indexOf("vjifen") > -1) {
            url += "&v=";
        } else {
            url += "/";
        }

        try {
            // 赋码率
            float qrcodePercent = Float.valueOf(StringUtils.defaultIfBlank(qrcodeOrder.getQrcodePercent(), "100")) / 100f;

            // 批次序号
            int batchAutocodeNoCount = 0;
            for (VcodeQrcodeBatchInfo batchInfo : batchList) {
                batchAutocodeNoCount = 0;

                // 共享码库已创建码源
                Set<String> shareQrcodeLibSet = Collections.synchronizedSet(new HashSet<String>());

                // 创建码库表, smallBatchParentKey为空表明没有共享码库表
                if (StringUtils.isBlank(batchInfo.getSmallBatchParentKey())) {
                    PerformanceUtil.end(batchInfo.getLibName() + "创建表开始");
                    if (isWanFlag) {
                        map.clear();
                        map.put("serverName", DbContextHolder.getDBType());
                        map.put("libName", batchInfo.getLibName() + "_wan_bak");
                        vcodeQrcodeLibDao.createQrcodeTable(map);
                    }
                    map.put("serverName", DbContextHolder.getDBType());
                    map.put("libName", batchInfo.getLibName());
                    vcodeQrcodeLibDao.createQrcodeTable(map);
                    PerformanceUtil.end(batchInfo.getLibName() + "创建表结束");
                } else {
                    // 获取共享码库中
                    if (isWanFlag) {
                        map.put("libName", batchInfo.getLibName() + "_wan_bak");
                    } else {
                        map.put("libName", batchInfo.getLibName());
                    }
                    shareQrcodeLibSet.addAll(vcodeQrcodeLibDao.queryAllQrcode(map));

                    // 获取批次序号开始值
                    batchAutocodeNoCount = batchInfo.getBatchStartAutoNo() > 0 ? batchInfo.getBatchStartAutoNo() - 1 : 0;
                }

                // 包编码
                String packKey = null;
                String packCode = null;
                long packAmount = 0; // 每包（批次）码源量
                long prizeQrcodeNum = 0; // 实际赋码量
                long noPrizeQrcodeNum = 0; // 无奖赋码量
                long qrcodeIdx = 0; // 生成码源索引
                Set<String> set = null;
                Set<String> setAll = Collections.synchronizedSet(new HashSet<String>());

                PerformanceUtil.end(batchInfo.getLibName() + "码包查询开始");

                // 码包List
                List<VcodeQrcodePackInfo> packList = vcodeQrcodePackInfoDao.findPackListByBatchKey(batchInfo.getBatchKey());
                for (VcodeQrcodePackInfo packInfo : packList) {
                    qrcodeIdx = 0;
                    packCode = packInfo.getPackCode();
                    packAmount = packInfo.getQrcodeAmounts();
                    prizeQrcodeNum = (long) (packAmount * qrcodePercent);
                    noPrizeQrcodeNum = (long) (packAmount * (1 - qrcodePercent));
                    packKey = packInfo.getPackKey();
                    batchInfo.setRealVcodeCounts(batchInfo.getRealVcodeCounts() + prizeQrcodeNum);

                    // 生成的二维码文件
//					qrcodeFile = new File(sourceFilePath + "/" + packCode + ".txt");
                    if ("高印".equals(qrcodeOrder.getQrcodeManufacture())) {
                        qrcodeFileWiter = new FileWriterWithEncoding(sourceFilePath + "/" + packCode + ".txt", "GBK", true);
                    } else {
                        qrcodeFileWiter = new FileWriterWithEncoding(sourceFilePath + "/" + packCode + ".txt", "UTF-8", true);
                    }
                    qrcodeBufferWriter = new BufferedWriter(qrcodeFileWiter);

                    // 入库的二维码文件
                    tmpFilePath = tempqrcodeFilePath + "/" + packCode + ".txt";
                    tempText = new File(tmpFilePath);
                    datafileOut = new FileOutputStream(tempText, true);

                    PerformanceUtil.end(batchInfo.getLibName() + "生成" + prizeQrcodeNum + "个二维码set开始");
                    RandomDataUtil randomData = new RandomDataUtil();
                    // 生成码的数量
                    if ("guangxi".equals(DbContextHolder.getDBType())) {
                        // (8+31)
                        set = randomData.getVcodeGXQP(prizeQrcodeNum, setAll, shareQrcodeLibSet);
                    } else {
                        // 拉环（9+56）
                        if (Constant.QRCODE_FORMAT.format_1.equals(qrcodeOrder.getQrcodeFormat())) {
                            set = randomData.getVcodeLH(prizeQrcodeNum, setAll, shareQrcodeLibSet);
                            // （9+31）
                        } else {
                            set = randomData.getVcodeCM(prizeQrcodeNum, setAll, shareQrcodeLibSet);
                        }
                    }
                    PerformanceUtil.end(batchInfo.getLibName() + "生成" + prizeQrcodeNum + "个二维码set结束");
                    setAll.addAll(set);
                    Object[] array = set.toArray();
                    int setLen = array.length;
                    String tempSource = null;
                    PerformanceUtil.end(batchInfo.getLibName() + "写入文件开始");
                    for (int i = 0; i < setLen; ++i) {
                        //vcodeKey=活动唯一标识码=三位字母
                        String source = batchInfo.getVcodeUniqueCode() + array[i];//12位串码

                        // 用于生成图片的请求url
                        if (qrcodeUrl.indexOf("vjifen") > -1 || "admin".equals(qrcodeOrder.getCreateUser())) {
                            tempSource = source;
                        }

                        //如果SKU配置了序列号
                        if (autocodeFlag) {
                            autocodeNoCount = autocodeNoCount == 99999999 ? 1 : autocodeNoCount + 1;
                            // 如果配置了sku订单名称
                            if (StringUtils.isNotBlank(autocodeNoPrefix)) {
                                autocodeNo = autocodeNoPrefix + String.format("%08d", autocodeNoCount);
                            } else {
                                autocodeNo = String.format("%08d", autocodeNoCount);
                            }
                        }

                        // 批次序列号
                        if (Constant.QRCODE_FORMAT.format_16.equals(qrcodeFormat)) {
                            batchAutocodeNoCount++;
                            autocodeNo = batchInfo.getVcodeUniqueCode() + String.format("%07d", batchAutocodeNoCount);
                        }

                        // 转换码源格式
                        source = convertQrcodeFormat(url, source, autocodeNo, qrcodeFormat, qrcodeOrder.getPrizeDesc(), randomData);

                        // 表头
                        if (i == 0) {
                            // 蒙牛赋码厂
                            if ("高印".equals(qrcodeOrder.getQrcodeManufacture())) {
                                qrcodeBufferWriter.append("活动码序号,活动码链接,一级码序列号,一级码链接\r\n");
//		                        BufferedWriteUtil.bufferedWrite("活动码序号,活动码链接,一级码序列号,一级码链接", qrcodeFile, "GBK");
                            }
                        }
                        // 写入有奖码源文件
                        qrcodeIdx++;
                        qrcodeBufferWriter.append(source + "\r\n");
//						BufferedWriteUtil.bufferedWrite(source, qrcodeFile, "UTF-8");
                        // 写入无奖码源文件
                        for (int j = 0; j < noPrizeQrcodeNum / prizeQrcodeNum; j++) {
                            qrcodeBufferWriter.append(qrcodeOrder.getNoPrizeDesc() + "\r\n");
//						    BufferedWriteUtil.bufferedWrite(qrcodeOrder.getNoPrizeDesc(), qrcodeFile, "高印".equals(qrcodeOrder.getQrcodeManufacture()) ? "GBK" : "UTF-8");
                            qrcodeIdx++;
                        }

                        if (qrcodeUrl.indexOf("vjifen") > -1 || "admin".equals(qrcodeOrder.getCreateUser())) {
                            String imageUrl = sourceFilePath + "/" + "qrCode" + (i + 1) + ".png";
                            QrCodeUtil.encodeQRCodeImage(url + tempSource, null, imageUrl, 283,
                                    283, null);
                            dpiUtil.saveGridImage(new File(imageUrl));
                        }

                        // 入库文件
                        qrcodeStr.append(UUIDTools.getInstance().getUUID()).append("+")
                                .append(array[i].toString()).append("+")
                                .append(batchInfo.getCompanyKey()).append("+")
                                .append(packKey).append("+")
                                .append(batchInfo.getBatchKey()).append("+")
                                .append(DateUtil.getDateTime("")).append("+")
                                .append("0").append("+")
                                .append(batchAutocodeNoCount > 0 ? autocodeNo : "NaN").append("+")
                                .append(batchInfo.getLibName());

                        // 队列中的二维码写入文件
                        datafileOut.write((qrcodeStr.toString() + separator).getBytes());
                        // 清空二维码builder
                        qrcodeStr.setLength(0);
                    }

                    // 入库数据库文件
                    datafileOut.flush();
                    datafileOut.close();
                    PerformanceUtil.end(batchInfo.getLibName() + "写入文件结束");

                    // 二维码文件写入码库
                    map.clear();
                    map.put("libName", batchInfo.getLibName());
                    if (isWanFlag) {
                        map.put("libName", batchInfo.getLibName() + "_wan_bak");
                    }
                    map.put("tmpFilePath", tmpFilePath);
                    vcodeQrcodeLibDao.writeQrcodeToData(map);
                    PerformanceUtil.end(batchInfo.getLibName() + "二维码入库结束");
                    queueTxt.clear();
                }

                // 补全剩余无效码
                for (int j = 0; j < packAmount - qrcodeIdx; j++) {
                    qrcodeBufferWriter.append(qrcodeOrder.getNoPrizeDesc() + "\r\n");
                    // BufferedWriteUtil.bufferedWrite(qrcodeOrder.getNoPrizeDesc(), qrcodeFile, "GBK");
                }

                // 码源文件
                qrcodeBufferWriter.flush();
                qrcodeBufferWriter.close();
                qrcodeFileWiter.close();
                PerformanceUtil.end("本批次生成完成**************libName=" + batchInfo.getLibName());
            }

            // 更新SKU中的生成码编号
            if (autocodeFlag) {
                if (StringUtils.isNotBlank(autocodeNoPrefix)) {
                    skuInfo.setAutocodeNo(autocodeNoPrefix + String.format("%08d", autocodeNoCount));
                } else {
                    skuInfo.setAutocodeNo(String.format("%08d", autocodeNoCount));
                }
                skuInfoService.updateSkuInfo(skuInfo);
            }

        } catch (Exception e) {
            throw new Exception("生成码异常：", e);
        }
    }


    /**
     * 生成二维码（测试线程方式）
     * @param qrcodeOrder 订单
     * @param filePath         码源订单所在路径
     * @param batchList    批次
     * @param date    时间
     * @param isWanFlag    是否一万一批次
     * @throws Exception
     */
    /*@Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void produceCode(String filePath, VpsQrcodeOrder qrcodeOrder, List<VcodeQrcodeBatchInfo> batchList, String date, boolean isWanFlag) throws Exception {
        File tempText = null;
        File qrcodeFile = null;
        String tmpFilePath = null;
        FileOutputStream fileOut = null;
        Map<String, Object> map = new HashMap<>();
        String qrcodeFormat = qrcodeOrder.getQrcodeFormat();
        String sourceFilePath = filePath + "/1source";
        String tempqrcodeFilePath = filePath + "/tempqrcode";
        File file = new File(sourceFilePath); file.mkdirs();
        file = new File(tempqrcodeFilePath); file.mkdirs();
        String separator = System.getProperty("line.separator");
        PerformanceUtil.begin();

        // 二维码url
        // 测试：http://xt.vjifen.com/qr?c=p&v=二维码
        // 线上：HTTP://VJ1.TV/p/二维码
        String qrcodeUrl = PropertiesUtil.getPropertyValue("qrcode_url");
        String projectFlag = PropertiesUtil.getPropertyValue("project_flag");
        String url = qrcodeUrl + projectFlag;
        if(qrcodeUrl.indexOf("vjifen") > -1){
            url += "&v=";
        }else{
            url += "/";
        }

        try{
            for (VcodeQrcodeBatchInfo batchInfo : batchList) {
                // 创建码库表
                PerformanceUtil.end(batchInfo.getLibName()+"创建表开始");
                if(isWanFlag){
                    map.clear();
                    map.put("libName", batchInfo.getLibName() + "_wan_bak");
                    vcodeQrcodeLibDao.createQrcodeTable(map);
                }
                map.put("libName", batchInfo.getLibName());
                vcodeQrcodeLibDao.createQrcodeTable(map);
                PerformanceUtil.end(batchInfo.getLibName()+"创建表结束");

                // 包编码
                String packKey = null;
                String packCode = null;
                String packAmount = null;
                Set<String> set = null;
                Set<String> setAll=new HashSet<String>();
                ExecutorService es = Executors.newFixedThreadPool(4);
                CountDownLatch latch = new CountDownLatch(20);

                PerformanceUtil.end(batchInfo.getLibName()+"码包查询开始");

                // 码包List
                List<VcodeQrcodePackInfo> packList = vcodeQrcodePackInfoDao.findPackListByBatchKey(batchInfo.getBatchKey());
                for (VcodeQrcodePackInfo packInfo : packList) {
                    packCode = packInfo.getPackCode();
                    packAmount = packInfo.getQrcodeAmounts()+"";
                    packKey = packInfo.getPackKey();

                    // 生成的二维码文件
                    qrcodeFile = new File(sourceFilePath + "/" + packCode + ".txt");

                    // 入库的二维码文件
                    tmpFilePath=tempqrcodeFilePath + "/" + packCode + ".txt";
                    tempText = new File(tmpFilePath);
                    fileOut = new FileOutputStream(tempText, true);

                    PerformanceUtil.end(batchInfo.getLibName()+"生成"+packAmount+"个二维码set开始");
                    RandomDataUtil randomData = new RandomDataUtil();
                    // 生成码的数量
                    if("guangxi".equals(DbContextHolder.getDBType())){
                        // (8+31)
                        set = randomData.getVcodeGXQP(packAmount,setAll);
                    }else {
                        // 拉环（9+56）
                        if(Constant.QRCODE_FORMAT.format_1.equals(qrcodeOrder.getQrcodeFormat())){
                            set = randomData.getVcodeLH(packAmount,setAll);
                        // （9+31）
                        }else{
                            set = randomData.getVcodeCM(packAmount,setAll);
                        }
                    }
                    PerformanceUtil.end(batchInfo.getLibName()+"生成"+packAmount+"个二维码set结束");
                    setAll.addAll(set);
                    Object[] array=set.toArray();
                    int setLen = array.length;
                    PerformanceUtil.end(batchInfo.getLibName()+"写入文件开始");
                    for (int i = 0; i < setLen; ++i) {
                        //vcodeKey=活动唯一标识码=三位字母
                        String source = batchInfo.getVcodeUniqueCode() + array[i];//12位串码
                        // 转换码源格式
                        source = convertQrcodeFormat(source, qrcodeFormat, randomData);

                        BufferedWriteUtil.bufferedWrite(url + source, qrcodeFile);

                        queueTxt.offer(UUIDTools.getInstance().getUUID() + "+" + array[i].toString()
                                + "+" + batchInfo.getCompanyKey() + "+" + packKey + "+" + batchInfo.getBatchKey()
                                + "+" + batchInfo.getVcodeActivityKey() + "+" + DateUtil.getDateTime("") + "+"
                                + "0" + "+" + batchInfo.getLibName());
                    }


                    for (int m = 0; m < 20; ++m) {
                        es.submit(new WriteQrDbForTest(queueTxt, fileOut, latch));
                    }
                    latch.await();
                    PerformanceUtil.end(batchInfo.getLibName()+"写入文件结束");

                    // 二维码文件写入码库
                    map.clear();
                    map.put("libName", batchInfo.getLibName());
                    if(isWanFlag){
                        map.put("libName", batchInfo.getLibName()+ "_wan_bak");
                    }
                    map.put("tmpFilePath", tmpFilePath);
                    vcodeQrcodeLibDao.writeQrcodeToData(map);
                    PerformanceUtil.end(batchInfo.getLibName()+"二维码入库结束");

                    queueTxt.clear();
                    fileOut.flush();
                    fileOut.close();
                }
                es.shutdown();
                PerformanceUtil.end("本批次生成完成**************libName=" + batchInfo.getLibName());
            }
        }catch(Exception e){
            throw new Exception("生成码异常：", e);
        }
    }*/

    /**
     * 转换码源格式
     *
     * @param url          url
     * @param source       源二维码
     * @param autocodeNo   序号
     * @param qrcodeFormat 码源格式：1.无串码, 2.有串码,串码之间有逗号 3.有串码,串码之间无逗号 4.有串码,串码之间有空格
     * @return
     */
    private String convertQrcodeFormat(String url, String source, String autocodeNo, String qrcodeFormat, String prizeDesc, RandomDataUtil randomData) {
        if (Constant.QRCODE_FORMAT.format_1.equals(qrcodeFormat)) {
            return url + source + (StringUtils.isBlank(prizeDesc) ? "" : "," + prizeDesc);
        } else if (Constant.QRCODE_FORMAT.format_20.equals(qrcodeFormat)) {
            // 二维码 奖项描述
            return url + source + (StringUtils.isBlank(prizeDesc) ? "" : " " + prizeDesc);
        } else if (Constant.QRCODE_FORMAT.format_21.equals(qrcodeFormat)) {
            // 二维码,奖项描述
            return url + source + (StringUtils.isBlank(prizeDesc) ? "" : "," + prizeDesc);
        } else if (Constant.QRCODE_FORMAT.format_22.equals(qrcodeFormat)) {
            // 二维码;奖项描述
            return url + source + (StringUtils.isBlank(prizeDesc) ? "" : ";" + prizeDesc);
        } else if (Constant.QRCODE_FORMAT.format_11.equals(qrcodeFormat)) {
            // 序号 二维码
            return autocodeNo + " " + url + source + (StringUtils.isBlank(prizeDesc) ? "" : " " + prizeDesc);
        }

        StringBuilder code = new StringBuilder();
        // 一次混排
        code.append(url).append(ShuffleUtil.shuffleMethod(source, randomData.getRandomSix()));//混排

        // 码源格式
        // 二维码,串前6,串后6-有串码
        if (Constant.QRCODE_FORMAT.format_2.equals(qrcodeFormat)) {
            if ("guangxi".equals(DbContextHolder.getDBType())) {
                code.append(",").append(source.substring(0, 5)).append(",").append(source.substring(5)).append(StringUtils.isBlank(prizeDesc) ? "" : "," + prizeDesc);
            } else {
                code.append(",").append(source.substring(0, 6)).append(",").append(source.substring(6)).append(StringUtils.isBlank(prizeDesc) ? "" : "," + prizeDesc);
            }
        } else if (Constant.QRCODE_FORMAT.format_3.equals(qrcodeFormat)) {
            // 二维码,串12
            code.append(",").append(source).append(StringUtils.isBlank(prizeDesc) ? "" : "," + prizeDesc);
        } else if (Constant.QRCODE_FORMAT.format_4.equals(qrcodeFormat)) {
            // 二维码 串前6 串后6
            code.append(" ").append(source.substring(0, 6)).append(" ").append(source.substring(6)).append(StringUtils.isBlank(prizeDesc) ? "" : " " + prizeDesc);
        } else if (Constant.QRCODE_FORMAT.format_5.equals(qrcodeFormat)) {
            // 二维码,,串前6,串后6
            code.append(",,").append(source.substring(0, 6)).append(",").append(source.substring(6)).append(StringUtils.isBlank(prizeDesc) ? "" : "," + prizeDesc);
        } else if (Constant.QRCODE_FORMAT.format_6.equals(qrcodeFormat)) {
            // 二维码 串12
            code.append(" ").append(source).append(StringUtils.isBlank(prizeDesc) ? "" : " " + prizeDesc);
        } else if (Constant.QRCODE_FORMAT.format_7.equals(qrcodeFormat)) {
            // 二维码,串前6,串后6,序号
            code.append(",").append(source.substring(0, 6)).append(",").append(source.substring(6)).append(",").append(autocodeNo).append(StringUtils.isBlank(prizeDesc) ? "" : "," + prizeDesc);
        } else if (Constant.QRCODE_FORMAT.format_8.equals(qrcodeFormat)) {
            // 二维码,串12,序号(主推)
            code.append(",").append(source).append(",").append(autocodeNo).append(StringUtils.isBlank(prizeDesc) ? "" : "," + prizeDesc);
        } else if (Constant.QRCODE_FORMAT.format_9.equals(qrcodeFormat)) {
            // 二维码 串12 序号(主推)
            code.append(" ").append(source).append(" ").append(autocodeNo).append(StringUtils.isBlank(prizeDesc) ? "" : " " + prizeDesc);
        } else if (Constant.QRCODE_FORMAT.format_10.equals(qrcodeFormat)) {
            // 二维码 串前6 串后6 序号
            code.append(" ").append(source.substring(0, 6)).append(" ").append(source.substring(6)).append(" ").append(autocodeNo).append(StringUtils.isBlank(prizeDesc) ? "" : " " + prizeDesc);
        } else if (Constant.QRCODE_FORMAT.format_12.equals(qrcodeFormat)) {
            // 序号 二维码,串前6,串后6
            code.insert(0, autocodeNo + " ").append(",").append(source.substring(0, 6)).append(",").append(source.substring(6)).append(StringUtils.isBlank(prizeDesc) ? "" : "," + prizeDesc);
        } else if (Constant.QRCODE_FORMAT.format_13.equals(qrcodeFormat)) {
            // 序号 二维码,串12
            code.insert(0, autocodeNo + " ").append(",").append(source).append(StringUtils.isBlank(prizeDesc) ? "" : "," + prizeDesc);
        } else if (Constant.QRCODE_FORMAT.format_14.equals(qrcodeFormat)) {
            // 序号 二维码 串前6 串后6
            code.insert(0, autocodeNo + " ").append(" ").append(source.substring(0, 6)).append(" ").append(source.substring(6)).append(StringUtils.isBlank(prizeDesc) ? "" : " " + prizeDesc);
        } else if (Constant.QRCODE_FORMAT.format_15.equals(qrcodeFormat)) {
            //  序号,二维码,串12
            code.insert(0, autocodeNo + ",").append(",").append(source).append(StringUtils.isBlank(prizeDesc) ? "" : "," + prizeDesc);
        } else if (Constant.QRCODE_FORMAT.format_16.equals(qrcodeFormat)) {
            //  批次序号,二维码
            code.insert(0, autocodeNo + ",").append(StringUtils.isBlank(prizeDesc) ? "" : "," + prizeDesc);
            ;
        } else if (Constant.QRCODE_FORMAT.format_17.equals(qrcodeFormat)) {
            //  序号,二维码,串前6,串后6
            code.insert(0, autocodeNo + ",").append(",").append(source.substring(0, 6)).append(",").append(source.substring(6)).append(StringUtils.isBlank(prizeDesc) ? "" : "," + prizeDesc);
        } else if (Constant.QRCODE_FORMAT.format_18.equals(qrcodeFormat)) {
            //  二维码,,串码,串前6,串后6
            code.append(",,").append(source).append(",").append(source.substring(0, 6)).append(",").append(source.substring(6)).append(StringUtils.isBlank(prizeDesc) ? "" : "," + prizeDesc);
        } else if (Constant.QRCODE_FORMAT.format_19.equals(qrcodeFormat)) {
            //  二维码,串码,串前6,串后6
            code.append(",").append(source).append(",").append(source.substring(0, 6)).append(",").append(source.substring(6)).append(StringUtils.isBlank(prizeDesc) ? "" : "," + prizeDesc);
        }
        return code.toString();
    }

    /**
     * 生成批次
     *
     * @param vcodeBatchInfo
     * @return
     */
    public List<VcodeQrcodeBatchInfo> showBatchInfoLst(VpsQrcodeOrder vpsQrcodeOrder, VpsVcodeFactory vpsVcodeFactory, VpsWinery vpsWinery) {

        if (CheckUtil.isEmpty(vpsVcodeFactory) || CheckUtil.isEmpty(vpsWinery))
            throw new BusinessException("清选择赋码厂以及生产工厂!");

        if (CheckUtil.isEmpty(vpsQrcodeOrder.getOrderTime())) throw new BusinessException("请选择订单日期!");

        List<VcodeQrcodeBatchInfo> batchLst = new ArrayList<>();

        SkuInfo skuInfo = skuInfoService.findById(vpsQrcodeOrder.getSkuKey());

        String skuName = skuInfo.getShortName().replace("*", "X");
        // 批次前缀
        int batchNumPrefix = 1;
        // 获取当前订单日期
        String orderTime = vpsQrcodeOrder.getOrderTime().replace("-", "");

        // 批次编号
        String batchDesc = vpsQrcodeOrder.getProjectOrderPrefix() + vpsVcodeFactory.getFactoryShort()
                + vpsWinery.getWineryShort() + (StringUtils.isNotBlank(skuInfo.getAutocodePrefix()) ? skuInfo.getAutocodePrefix() : "");
        // 批次名称
        String batchName = vpsQrcodeOrder.getProjectName() + vpsVcodeFactory.getFactoryName() +
                vpsWinery.getWineryName() + skuName + orderTime;
        //查询该批次信息加上当前年
        String batchPrefix = "";
        if ("mengniu".equals(DbContextHolder.getDBType()) && "AUTO".equals(vpsQrcodeOrder.getCreateUser())) {
            batchPrefix = batchInfoDao.findExistBatchPrefix(batchDesc + orderTime);
        } else {
            batchPrefix = batchInfoDao.findExistBatchPrefix(batchDesc + DateUtil.getCurrentYear());
        }
        if (CheckUtil.isNumeric(batchPrefix)) {
            batchNumPrefix = Integer.valueOf(batchPrefix) + 1;
        }
        batchDesc += orderTime;
        // 通道数
        int channelCount = vpsQrcodeOrder.getChannelCount();
        //每批次码数
        long batchNum = vpsVcodeFactory.getBatchNum();
        //总码数
        long qrcodeToal = (long) (vpsQrcodeOrder.getOrderQrcodeCount());
        //损耗比,由于可以到小数 所以有可能为0则自动补1
        if (StringUtils.isNotBlank(vpsVcodeFactory.getLossRatio()) && Double.valueOf(vpsVcodeFactory.getLossRatio()) > 0) {
            long lossRatioNum = (long) (qrcodeToal * Double.valueOf(vpsVcodeFactory.getLossRatio()) / 100);
            qrcodeToal += (lossRatioNum <= 0 ? 1 : lossRatioNum);
        }
        Long sum = 0l;
        //剩余码数
        long surplusQrcode = qrcodeToal;
        //文件数
        int batchLstSize = (int) (qrcodeToal / batchNum + (qrcodeToal % batchNum > 0 ? 1 : 0));
        //实际文件数                                                                                  
        int actualBatch = (batchLstSize / channelCount) * channelCount + (batchLstSize % channelCount > 0 ? channelCount : 0);


        //临界值
        int criticalNum = batchLstSize - batchLstSize % channelCount;
        int surplusBatch = actualBatch - channelCount;
        if (criticalNum == actualBatch) {
            criticalNum = 0;
        } else {
            surplusQrcode = qrcodeToal - ((batchLstSize / channelCount) * channelCount * batchNum);
        }

        // 赋码率
        long qrcodeAmounts = 0;
        for (int i = 1; i <= actualBatch; i++) {
            //计算批次码数
            qrcodeAmounts = i <= criticalNum ? batchNum : (surplusBatch > 0 ? (i > surplusBatch ? (qrcodeToal - surplusBatch * batchNum) / channelCount : surplusBatch * batchNum / surplusBatch) : surplusQrcode / channelCount);

            batchLst.add(new VcodeQrcodeBatchInfo(i, batchDesc + String.format("%03d", batchNumPrefix),
                    batchName + String.format("%03d", batchNumPrefix), qrcodeAmounts, 1));
            batchNumPrefix += 1;
            sum += qrcodeAmounts;
            //增加合计行
            if (i == actualBatch) {
                batchLst.add(new VcodeQrcodeBatchInfo(++i, "", "合计", sum, actualBatch));
            }
        }

        return batchLst;
    }


    /**
     * 二次确认框
     *
     * @return
     */
    public VpsQrcodeOrder checkBatchInfo(VpsQrcodeOrder vpsQrcodeOrder,String dbType) {
        VpsVcodeFactory vpsVcodeFactory = iVpsVcodeFactoryDao.findById(vpsQrcodeOrder.getFactoryId());
        VpsWinery vpsWinery = iVpsVcodeWineryDao.findById(vpsQrcodeOrder.getWineryId());
        long orderQrcodeCount = (long) (vpsQrcodeOrder.getOrderQrcodeCount());
        long qrcodeToal = orderQrcodeCount;
        //损耗比
        if (StringUtils.isNotBlank(vpsVcodeFactory.getLossRatio()) && Double.valueOf(vpsVcodeFactory.getLossRatio()) > 0) {
            long lossRatioNum = (long) (qrcodeToal * Double.valueOf(vpsVcodeFactory.getLossRatio()) / 100);
            qrcodeToal += (lossRatioNum <= 0 ? 1 : lossRatioNum);
        }
        // 订单名称
        String orderName = vpsVcodeFactory.getFactoryName() + "-" + vpsWinery.getWineryName() + "-"
                + vpsQrcodeOrder.getOrderTime().replace("-", "") + "-" + vpsQrcodeOrder.getSkuName().replace("*", "X") + "-"
                + qrcodeToal + "个(" + orderQrcodeCount + "个)";

        vpsQrcodeOrder.setOrderQrcodeCount(orderQrcodeCount);
        vpsQrcodeOrder.setOrderTotalQrcodeCount(qrcodeToal);
        vpsQrcodeOrder.setOrderName(orderName);
        // 二次确认界面
        businessService.zuiJiuVpsQrcodeOrder(vpsQrcodeOrder,dbType);
        vpsQrcodeOrder.setIsWanBatch("1".equals(vpsQrcodeOrder.getIsWanBatch()) ? "是" : "否");
        vpsQrcodeOrder.setPackagePassword(getCharAndNumr());
        vpsQrcodeOrder.setIsLabel(vpsVcodeFactory.getIsLabel());
        vpsQrcodeOrder.setQrcodeFormat(vpsVcodeFactory.getQrcodeFormat());
        return vpsQrcodeOrder;
    }


    /**
     * 新增码源订单
     *
     * @param vpsQrcodeOrder
     * @throws Exception
     */
    public void create(VpsQrcodeOrder vpsQrcodeOrder, SysUserBasis currentUser) throws Exception {
        // 新增码源订单
        if (CollectionUtils.isEmpty(vpsQrcodeOrder.getBatchInfoList())) {
            throw new BusinessException("根据码源订单未生成批次信息");
        }
        // 订单主键
        vpsQrcodeOrder.setOrderKey(new DateTime().toString("yyyyMMddHHmmss") + RandomUtils.randString(3));
        // 获取订单编号
        vpsQrcodeOrder.setOrderNo(getBussionNo("vpsQrcodeOrder", "order_no", Constant.OrderNoType.type_MY));
        vpsQrcodeOrder.setCompanyKey(currentUser.getCompanyKey());
        vpsQrcodeOrder.setUserPhone(currentUser.getPhoneNum());
        vpsQrcodeOrder.fillFields(currentUser.getUserKey());
        vpsQrcodeOrder.setOrderQrcodeCount(vpsQrcodeOrder.getOrderTotalQrcodeCount());
        vpsQrcodeOrderDao.create(vpsQrcodeOrder);
        // 创建码源批次
        addVcodeQrcodeBatchInfo(vpsQrcodeOrder, currentUser);

        logService.saveLog("vpsQrcodeOrder", Constant.OPERATION_LOG_TYPE.TYPE_1, JSON.toJSONString(vpsQrcodeOrder),
                "新增码源订单:" + vpsQrcodeOrder.getOrderName());
    }


    /**
     * 创建码源批次
     */
    public void addVcodeQrcodeBatchInfo(VpsQrcodeOrder order, SysUserBasis currentUser) throws Exception {
        // 生成标签
        String date = order.getCreateTime().split(" ")[0].replaceAll("-", "");
        // 生成码源订单路径
        String filePath = COMMON_PATH + "/" + date + "/" + DbContextHolder.getDBType() + "/" + order.getOrderKey();
        int total = 0;
        boolean flag = "1".equals(order.getIsLabel()) ? true : false;

        for (VcodeQrcodeBatchInfo info : order.getBatchInfoList()) {
            if (StringUtils.isBlank(info.getBatchDesc()))
                break;
            total += 1;
            info.setBatchKey(UUIDTools.getInstance().getUUID());
            info.setCompanyKey(currentUser.getCompanyKey());
            if (isExistActivityBatchDesc(info.getBatchDesc())) {
                throw new BusinessException("批次编号：" + info.getBatchDesc() + "已存在");
            }
            info.setOrderKey(order.getOrderKey());
            info.setClientOrderNo(order.getClientOrderNo());
            info.setSkuKey(order.getSkuKey());
            info.setVcodeFlag("0");
            info.setImportFlag("0");
            info.setStatus(Constant.batchStatus.status0);
            info.fillFields(currentUser.getUserKey());

            // 创建批次相关的lib_relation及包记录
            vcodeQrcodeBatchInfoService.initRelationAndPackForBatch(info, order);

            // 创建批次信息，由于需要初始化smallBatchParentKey，所以放于initRelationAndPackForBatch方法后
            batchInfoDao.create(info);

            //生成标签
            if (flag) {
                // 生成批次图片
                String imageUrl = filePath + "/" + "1source" + "/" + total + ".png";
                ;
                File file = new File(imageUrl);
                file.mkdirs();
                QrCodeUtil.encodeQRCodeImage(info.getBatchDesc(), null, imageUrl, 283, 283, null);
                new DpiUtil().saveGridImage(new File(imageUrl));

                String batchNum = info.getBatchDesc().substring(info.getBatchDesc().length() - 3);
                String batchArray[] = {info.getBatchDesc(), order.getProjectName(), order.getSkuName(),
                        order.getQrcodeManufacture() + order.getBrewery() + batchNum, imageUrl, order.getOrderTime(), batchNum};
                LabelImage.myGraphicsGeneration(batchArray, imageUrl);
                log.warn("orderNo:" + order.getOrderNo() + " batchDesc:" + info.getBatchDesc() + " 生成批次标签");
            }

            logService.saveLog("qrcodeBatchInfo", Constant.OPERATION_LOG_TYPE.TYPE_1, JSON.toJSONString(info),
                    "创建批次:" + info.getBatchName());
        }

        // 将标签设置为压缩文件
        if (flag) {
            String fileName = order.getOrderNo() + "_" + order.getOrderName() + "_" + "labeImage";
            CompressFileTools.compressFile(filePath, order.getOrderKey(), date, fileName, "1source", null);
            FileOperateUtil.delFolder(filePath + "/1source");
        }
    }

    /**
     * 判断批码说明是否存在
     *
     * @param batchDesc</br>
     * @return String </br>
     */
    public boolean isExistActivityBatchDesc(String batchDesc) {
        return batchInfoDao.isExistActivityBatchDesc(batchDesc) != 0;
    }

    /**
     * 随机六位密码
     *
     * @return
     */
    private static String getCharAndNumr() {
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

    /**
     * 获取项目中文以及前缀
     *
     * @param vpsQrcodeOrder
     */
    public void getProjectFlag(VpsQrcodeOrder vpsQrcodeOrder) {
        vpsQrcodeOrder.setProjectName(DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_COMPANY_INFO,
                DatadicKey.filterCompanyInfo.PROJECT_NAME));
        vpsQrcodeOrder.setProjectOrderPrefix(DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_COMPANY_INFO,
                DatadicKey.filterCompanyInfo.PROJECT_ORDERPREFIX));
    }


    /**
     * 发送码包
     *
     * @param request
     * @param vpsQrcodeOrder
     * @param factory
     * @throws Exception
     */
    public void sendMeilMessage(HttpServletRequest request, VpsQrcodeOrder vpsQrcodeOrder) throws Exception {
        getProjectFlag(vpsQrcodeOrder);
        vpsQrcodeOrder.setSkuName(skuInfoService.findById(vpsQrcodeOrder.getSkuKey()).getShortName());
        vpsQrcodeOrder.setBatchInfoList(vcodeQrcodeBatchInfoDao.queryVcodeQrcodeBatchInfoByOrderKey(vpsQrcodeOrder.getOrderKey()));
        MailUtil.sendMail(this.getContextUrl(request), vpsQrcodeOrder, true);
        updateOrderByStatus(vpsQrcodeOrder.getOrderKey(), Constant.QRCODE_ORDER_STATUS.order_status_4);
    }

    /**
     * 发送密码
     *
     * @param vpsQrcodeOrder
     * @param factory
     * @throws Exception
     */
    public void sendMeilPassWord(VpsQrcodeOrder vpsQrcodeOrder) throws Exception {
        if (!Constant.QRCODE_ORDER_STATUS.order_status_4.equals(vpsQrcodeOrder.getOrderStatus()))
            throw new BusinessException("请先发送码包邮件再发送密码邮件");
        getProjectFlag(vpsQrcodeOrder);
        MailUtil.sendMail(null, vpsQrcodeOrder, false);
        updateOrderByStatus(vpsQrcodeOrder.getOrderKey(), Constant.QRCODE_ORDER_STATUS.order_status_5);
    }

    /**
     * 修改订单状态
     *
     * @param orderKey
     * @param orderStatus
     */
    public void updateOrderByStatus(String orderKey, String orderStatus) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderKey", orderKey);
        map.put("orderStatus", orderStatus);
        vpsQrcodeOrderDao.updateOrderStatus(map);
    }
    
    /**
     * 获取未生成或生成失败的2分钟前自动下单的码源订单
     */
    public List<VpsQrcodeOrder> queryFailedOrderForAuto() {
        return vpsQrcodeOrderDao.queryFailedOrderForAuto();
    }

    /**
     * 自动码源订单相关批次绑定活动并激活
     */
    public void executeAutoQrcodeQrderBatchBindActivity() {
        vpsQrcodeOrderDao.updateAutoQrcodeQrderBatchBindActivity();
    }

    /**
     * 重新生成打印标签
     *
     * @param qrcodeOrder
     * @param vcodeFactory
     * @param userKey
     * @param model
     * @return
     */
    public String newAddLabel(VpsQrcodeOrder order, SysUserBasis currentUser, Model model) throws Exception {
        // 生成标签
        String date = order.getCreateTime().split(" ")[0].replaceAll("-", "");
        // 生成码源订单路径
        String filePath = COMMON_PATH + "/" + date + "/" + DbContextHolder.getDBType() + "/" + order.getOrderKey();

        ArrayList<String> files = new ArrayList<String>();
        File fileLabel = new File(filePath + "/4compress");
        File[] tempList = fileLabel.listFiles();
        if (tempList == null) {
            return "目标目录不存在！";
        }

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(tempList[i].toString());
            }
        }
        for (String item : files) {
            if (item.contains("labeImage")) {
                return "所选码源订单已存在标签文件，请检查是否正确！";
            }
        }

        getProjectFlag(order);
        SkuInfo skuInfo = skuInfoService.findById(order.getSkuKey());
        order.setSkuName(skuInfo.getShortName());

        int total = 0;
        boolean flag = "1".equals(order.getIsLabel()) ? true : false;

        for (VcodeQrcodeBatchInfo info : order.getBatchInfoList()) {
            total += 1;
            //生成标签
            if (flag) {
                // 生成批次图片
                String imageUrl = filePath + "/" + "1source" + "/" + total + ".png";
                ;
                File file = new File(imageUrl);
                file.mkdirs();
                QrCodeUtil.encodeQRCodeImage(info.getBatchDesc(), null, imageUrl, 283, 283, null);
                new DpiUtil().saveGridImage(new File(imageUrl));

                String batchNum = info.getBatchDesc().substring(info.getBatchDesc().length() - 3);
                String batchArray[] = {info.getBatchDesc(), order.getProjectName(), order.getSkuName(),
                        order.getQrcodeManufacture() + order.getBrewery() + batchNum, imageUrl, order.getOrderTime(), batchNum};
                LabelImage.myGraphicsGeneration(batchArray, imageUrl);
            }
        }

        // 将标签设置为压缩文件
        if (flag) {
            String fileName = order.getOrderNo() + "_" + order.getOrderName() + "_" + "labeImage";
            CompressFileTools.compressFile(filePath, order.getOrderKey(), date, fileName, "1source", null);
            FileOperateUtil.delFolder(filePath + "/1source");
        }
        return "重新生成打印标签成功";
    }


    public static ArrayList<String> getFiles(String path) {
        ArrayList<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(tempList[i].toString());
            }
        }
        return files;
    }


    public List<VpsQrcodeOrder> queryExportDataForLst(VpsQrcodeOrder queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return vpsQrcodeOrderDao.queryExportDataForLst(map);
    }

    public static void main(String[] args) throws Exception {

        BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:/hongen.txt")), "UTF-8"));


        long orderQrcodeNum = 1000;
        float qrcodePercent = Float.valueOf(StringUtils.defaultIfBlank("1", "100")) / 100f;

        int batchNum = 3;
        long batchQrcodeNum = (long) (orderQrcodeNum / batchNum);
        System.out.println("batchQrcodeNum:" + batchQrcodeNum);

        long prizeNum = (long) (batchQrcodeNum * qrcodePercent);
        System.out.println("prizeNum:" + prizeNum);

        long noPrizeNum = (long) (batchQrcodeNum * (1 - qrcodePercent));
        System.out.println("noPrizeNum:" + noPrizeNum);

        long perNoPrizeNum = noPrizeNum / prizeNum;
        System.out.println("preNoPrizeNum:" + perNoPrizeNum);

        int idx = 0;
        for (int k = 0; k < batchNum; k++) {
            for (int i = 0; i < prizeNum; i++) {
                System.out.println("qrcode" + i);
                bufferWriter.append("qrcode" + i + "\r\n");
                idx++;
                for (int j = 0; j < perNoPrizeNum; j++) {
                    System.out.println("谢谢" + j);
                    bufferWriter.append("谢谢" + j + "\r\n");
                    idx++;
                }
            }
            for (int j = 0; j < batchQrcodeNum - idx; j++) {
                bufferWriter.append("谢谢" + j + "\r\n");
            }
            idx = 0;
        }

        bufferWriter.flush();
        bufferWriter.close();
    }
}
