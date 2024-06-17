package com.dbt.vpointsshop.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.bean.Constant.ResultCode;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.json.reply.BaseReplyResult;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.*;
import com.dbt.framework.wechat.message.bean.MsgAppletBean;
import com.dbt.framework.wechat.message.bean.MsgAppletData;
import com.dbt.framework.wechat.message.bean.MsgValue;
import com.dbt.platform.activity.service.VcodeActivityVpointsCogService;
import com.dbt.platform.appuser.bean.VpsConsumerThirdAccountInfo;
import com.dbt.platform.appuser.bean.VpsConsumerUserInfo;
import com.dbt.platform.appuser.dao.IVpsConsumerAccountInfoDao;
import com.dbt.platform.appuser.dao.IVpsConsumerThirdAccountInfoDao;
import com.dbt.platform.appuser.service.VpsConsumerUserInfoService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.*;
import com.dbt.vpointsshop.dao.VpointsExchangeDao;
import com.dbt.vpointsshop.dao.VpointsGoodsDao;
import com.dbt.vpointsshop.dto.*;
import lombok.Builder;
import lombok.Data;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 兑换记录service
 *
 * @author zhaohongtao
 * 2017年12月11日
 */
@Service
public class ExchangeService extends BaseService<VpointsExchangeLog> {

    @Autowired
    private VpointsExchangeDao exchangeDao;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private IVpsConsumerAccountInfoDao accountInfoDao;
    @Autowired
    private VpointsGoodsDao goodsDao;
    @Autowired
    private IVpsConsumerThirdAccountInfoDao thirdAccountInfoDao;
    @Autowired
    private VpsConsumerUserInfoService vpsConsumerUserInfoService;
    @Autowired
    private VcodeActivityVpointsCogService vpointsCogService;

    public VpointsExchangeLog findById(String exchangeId) {
        return exchangeDao.findById(exchangeId);
    }

    /**
     * 兑换日志列表
     *
     * @param info
     * @param bean
     * @return
     */
    public List<VpointsExchangeLog> getExchangeList(PageOrderInfo info, VpointsExchangeLog bean) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("param", info);
        map.put("bean", bean);
        return exchangeDao.getExchangeList(map);
    }

    /**
     * 兑换日志总数
     *
     * @param info
     * @param bean
     * @return
     */
    public int getExchangeCount(PageOrderInfo info, VpointsExchangeLog bean) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("param", info);
        map.put("bean", bean);
        return exchangeDao.getExchangeCount(map);
    }

    /**
     * 实物奖列表
     *
     * @param info
     * @param bean
     * @return
     */
    public List<VpointsPrizeBean> getPrizeList(PageOrderInfo info, VpointsPrizeBean bean) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("param", info);
        map.put("bean", bean);
        return exchangeDao.getPrizeList(map);
    }

    /**
     * 实物奖总数
     *
     * @param info
     * @param bean
     * @return
     */
    public int getPrizeCount(PageOrderInfo info, VpointsPrizeBean bean) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("param", info);
        map.put("bean", bean);
        return exchangeDao.getPrizeCount(map);
    }

    /**
     * 兑换日志列表
     *
     * @param info
     * @param bean
     * @return
     */
    public List<VpointsRechargeLog> getRechargeList(PageOrderInfo info, VpointsRechargeLog bean) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("param", info);
        map.put("bean", bean);
        return exchangeDao.getRechargeList(map);
    }

    /**
     * 兑换日志总数
     *
     * @param info
     * @param bean
     * @return
     */
    public int getRechargeCount(PageOrderInfo info, VpointsRechargeLog bean) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("param", info);
        map.put("bean", bean);
        return exchangeDao.getRechargeCount(map);
    }

    public String getNickNameByKey(String userKey) {
        return exchangeDao.getNickNameByKey(userKey);
    }

    public String getOpenidByKey(String userKey) {
        return exchangeDao.getOpenidByKey(userKey);
    }

    /**
     * 订单管理列表
     *
     * @throws Exception
     */
    public List<VpointsExchangeLog> queryForExpressLst(
            VpointsExchangeLog queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        map.put("projectServerName", DbContextHolder.getDBType());
        List<VpointsExchangeLog> exchangeLogLsg = exchangeDao.queryForExpressLst(map);
        String[] addressAry = null;
        for (VpointsExchangeLog item : exchangeLogLsg) {
            item.setOrderStatus(transExchangeOrderStatus(item));
            if (StringUtils.isNotBlank(item.getAddress())) {
                addressAry = item.getAddress().split("-");
                if (addressAry.length >= 4) {
                    item.setProvince(addressAry[0]);
                    item.setCity(addressAry[1]);
                    item.setCounty(addressAry[2]);
                }
            }
            //判断是否是礼品卡
            String isGiftCard = "1";
            if ((isGiftCard.equals(item.getIsGiftCard()) && StringUtils.isNotEmpty(item.getGiftCardExchangeId())) || item.getGiftCardPay()>0) {
                item.setPayType(Constant.VPOINTS_PAY_TYPE.TYPE_3);
                item.setPayTypeName("礼品卡兑换");
                if (item.getExchangeVpoints() == 0 && item.getExchangePay() > 0) {
                    item.setPayTypeName("礼品卡兑换 + 现金支出");
                }
            } else {
                // 支付方式
                if (item.getExchangeVpoints() > 0 && item.getExchangePay() == 0) {
                    item.setPayType(Constant.VPOINTS_PAY_TYPE.TYPE_0);
                    item.setPayTypeName("积分支出");
                } else if (item.getExchangeVpoints() == 0 && item.getExchangePay() > 0) {
                    item.setPayType(Constant.VPOINTS_PAY_TYPE.TYPE_1);
                    item.setPayTypeName("现金支出");
                } else if (item.getExchangeVpoints() > 0 && item.getExchangePay() > 0) {
                    item.setPayType(Constant.VPOINTS_PAY_TYPE.TYPE_2);
                    item.setPayTypeName("混合支出");
                }
            }


            item.setExchangePayForExcel(MathUtil.round(item.getExchangePay() / 100D, 2));
            item.setDiscountsMoneyForExcel(MathUtil.round(item.getDiscountsMoney() / 100D, 2));
            item.setCouponDiscountPayForExcel(MathUtil.round(item.getCouponDiscountPay() / 100D, 2));
            //设置orderTYpe 内容
            pushOrderTypeName(item);
            if ("henanpz".equals(DbContextHolder.getDBType())
                    && !item.getExchangeChannel().equals(Constant.exchangeChannel.CHANNEL_9)) {
                item.setUpdateTime(null);
            }
        }
        return exchangeLogLsg;
    }

    public void pushOrderTypeName(VpointsExchangeLog item) {
        item.setOrderType(Constant.ExchangeOrderType.getOrderTypeName(item.getOrderType()));
        item.setGiftCardType(Constant.GiftCardType.getGiftCardTypeName(item.getGiftCardType()));
    }

    /**
     * 订单管理列表记录条数
     */
    public int queryForExpressCount(VpointsExchangeLog queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("projectServerName", DbContextHolder.getDBType());
        return exchangeDao.queryForExpressCount(map);
    }

    /**
     * 待发货表格下载
     */
    public void exportExchangeExpressList(VpointsExchangeLog
                                                  queryBean, PageOrderInfo pageInfo, HttpServletResponse response, List<VpointsBrandInfo> brandLst) throws Exception {
        response.reset();
        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/msexcel;charset=UTF-8");
        response.setContentType("application/vnd.ms-excel");
//        response.setContentType("application/octet-stream");
        OutputStream outStream = response.getOutputStream();

        // 获取导出信息
        pageInfo.setPagePerCount(0);
        long startTime = System.currentTimeMillis();
        List<VpointsExchangeLog> exchangeLogLst = new ArrayList<>(4000000);
        exchangeLogLst.addAll(queryForExpressLst(queryBean, pageInfo));
        long endTime = System.currentTimeMillis();
        log.warn("导出查询耗时=" + (endTime - startTime));

        final Class<?>[] vpointsExchangeLogExportClass = {null};
        List<Object> returnData = new ArrayList<>();
        String bookName = "";

        Map<String, Class<?>> classMap = new HashMap<>();
        classMap.put("0", "mengniu".equals(DbContextHolder.getDBType()) ? VpointsExchangeLogExportMN.class : VpointsExchangeLogExport.class);
        classMap.put("1", "mengniu".equals(DbContextHolder.getDBType()) ? VpointsExchangeLogTabs1ExportMN.class : VpointsExchangeLogTabs1Export.class);
        classMap.put("2", "mengniu".equals(DbContextHolder.getDBType()) ? VpointsExchangeLogTabs2ExportMN.class : VpointsExchangeLogTabs2Export.class);
        classMap.put("3", "mengniu".equals(DbContextHolder.getDBType()) ? VpointsExchangeLogTabsAllExportMN.class : VpointsExchangeLogTabsAllExport.class);
        classMap.put("4", "mengniu".equals(DbContextHolder.getDBType()) ? VpointsExchangeLogTabsAllExportMN.class : VpointsExchangeLogTabsAllExport.class);

        String tabsFlag = queryBean.getTabsFlag();
        if ("0".equals(tabsFlag)) {
            bookName = "待发货订单列表";
        } else if ("1".equals(tabsFlag)) {
            bookName = "已发货订单列表";
        } else if ("2".equals(tabsFlag)) {
            bookName = "已完成订单列表";
        } else if ("3".equals(tabsFlag)) {
            bookName = "订单列表";
        } else if ("4".equals(tabsFlag)) {
            bookName = "退货订单列表";
        }
        //蒙牛需要根据userkey获取用户实名认证信息
        long startTimemn = System.currentTimeMillis();
        String projectServerName = DbContextHolder.getDBType();
        Map<String, String> mnRealNameAndIdCardMap = new HashMap<>();
        if ("mengniu".equals(projectServerName)){
            List<String> userKeys = exchangeLogLst.stream().map(VpointsExchangeLog::getUserKey).collect(Collectors.toList());
            List<VpsConsumerUserInfo> vpsConsumerUserInfos = vpsConsumerUserInfoService.queryUserInfoByUserkeys(userKeys);
            for (VpsConsumerUserInfo info:vpsConsumerUserInfos) {
                String userKey = info.getUserKey();
                if (StringUtils.isNotBlank(info.getMnRealName())){
                    mnRealNameAndIdCardMap.put(userKey+"RealName",info.getMnRealName());
                }
                if (StringUtils.isNotBlank(info.getIdCard())){
                    mnRealNameAndIdCardMap.put(userKey+"IdCard",info.getIdCard());
                }
                if (StringUtils.isNotBlank(info.getUserRole())){
                    //用户角色0配送员、1终端老板、2经销商、3分销商、4网服
                    String userRole = info.getUserRole();
                    if (userRole.equals("0")){
                        userRole = "配送员";
                    }else if (userRole.equals("1")){
                        userRole = "终端老板";
                    }else if(userRole.equals("2")){
                        userRole = "经销商";
                    }else if(userRole.equals("3")){
                        userRole = "分销商";
                    }else if(userRole.equals("4")){
                        userRole = "网服";
                    }else{
                        userRole = "未知";
                    }
                    mnRealNameAndIdCardMap.put(userKey+"UserRole",userRole);
                }
            }
        }
        Map<String, String> brandMap = new HashMap<>();
        for (VpointsBrandInfo brandId : brandLst) {

            brandMap.put(brandId.getBrandId(), brandId.getBrandName());
        }

//        // 获取尊品卡分类
//        Map<String, String> prizeMap = vpointsCogService.queryBigPrizeForZunXiang();
        vpointsExchangeLogExportClass[0] = classMap.get(tabsFlag);
        final int[] index = {1};
        Class<?> finalVpointsExchangeLogExportClass = vpointsExchangeLogExportClass[0];
        List<Object> exportList = new ArrayList<>();
        final int[] i = {1};
        final int[] nnn = {1};
        log.warn("导出数据大小"+exchangeLogLst.size());

        exchangeLogLst.stream().filter(item -> !("0".equals(tabsFlag) && "1".equals(item.getExpressOrderFlag()))).forEach(item -> {
            ExchangeExport exchangeExport = new VpointsExchangeLogTabsAllExport();
            item.setBrandName(brandMap.get(item.getBrandId()));
            item.setIndex(String.valueOf(index[0]++));
            item.setOrderStatus(transExchangeOrderStatus(item));
            item.setExchangePayMoney(String.format("%.2f", item.getExchangePay() / 100D));
            item.setRefundPayMoney(String.format("%.2f", item.getRefundPay() / 100D));
            item.setIsGiftCard("1".equals(item.getIsGiftCard()) ? "是" : "否");
            item.setExpressOrderFlag("1".equals(item.getExpressOrderFlag()) ? "是" : "否");
            String giftCardStatus = "--";
            if (StringUtils.isNotEmpty(item.getGiftCardStatus())) {
                giftCardStatus = "0".endsWith(item.getGiftCardStatus()) ? "未领取" : "已领取";
            }
            item.setGiftCardStatus(giftCardStatus);
            if ("mengniu".equals(projectServerName)) {
                String userKey = item.getUserKey();
                if (mnRealNameAndIdCardMap.containsKey(userKey + "RealName")) {
                    item.setMnRealName(mnRealNameAndIdCardMap.get(userKey + "RealName"));
                }
                if (mnRealNameAndIdCardMap.containsKey(userKey + "IdCard")) {
                    item.setIdCard(mnRealNameAndIdCardMap.get(userKey + "IdCard"));
                }
                if (mnRealNameAndIdCardMap.containsKey(userKey + "UserRole")) {
                    item.setUserRole(mnRealNameAndIdCardMap.get(userKey + "UserRole"));
                }
            }
            Object addBean = null;
            try {
                addBean = finalVpointsExchangeLogExportClass.newInstance();
                long startSJ = System.currentTimeMillis();
                BeanCopier bc = BeanCopier.create(VpointsExchangeLog.class, finalVpointsExchangeLogExportClass, false);
                bc.copy(item, addBean, null);
//                FastBeanUtils.copy
                long endSJ = System.currentTimeMillis();
                Method setIndexMethod = addBean.getClass().getMethod("setIndex", int.class);
                setIndexMethod.invoke(addBean, i[0]++);
                exportList.add(addBean);
                nnn[0] += (endSJ - startSJ);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        long endTimemn = System.currentTimeMillis();
        log.warn("数据处理耗时=" + (endTimemn - startTimemn));
        log.warn("数据对象处理=" +  nnn[0]);
        returnData.addAll(exportList);

//        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(bookName, "UTF-8") + DateUtil.getDate() + ".csv");
        response.setHeader("Content-Disposition","attachment;fileName="+ URLEncoder.encode(bookName, "UTF-8")+DateUtil.getDate()+ ".xlsx");
        try {
            long exportStartTime = System.currentTimeMillis();
            log.warn("订单导出开始：" + (exportStartTime));
            EasyExcel
                    .write(outStream)
                    .autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .head(vpointsExchangeLogExportClass[0])
                    .sheet(bookName)
                    .doWrite(returnData);
            long exportEndTime = System.currentTimeMillis();
            log.warn("订单导出耗时=" + (exportEndTime - exportStartTime));

        } catch (Exception e) {
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + "下载失败" + DateUtil.getDate() + ".xlsx");
            EasyExcel
                    .write(response.getOutputStream())
                    .autoCloseStream(Boolean.TRUE)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet(bookName)
                    .doWrite(new ArrayList<>());

        } finally {
            if (outStream != null) {
                outStream.close();
            }
            response.flushBuffer();
        }

    }

    /**
     * 转换订单状态
     *
     * @param exchangeLog
     * @return
     */
    public String transExchangeOrderStatus(VpointsExchangeLog exchangeLog) {
        String orderStatus = "";
        if (exchangeLog != null) {
            switch (StringUtils.defaultString(exchangeLog.getExchangeStatus())) {
                case Constant.exchangeStatus.STATUS_MINUS_ONE:
                    orderStatus = "待支付";
                    break;

                case Constant.exchangeStatus.STATUS_0:
                    orderStatus = "兑换成功";
                    if (StringUtils.isNotBlank(exchangeLog.getGoodsReturnAudit())) {
                        orderStatus = "退货已驳回";
                    } else {
                        switch (StringUtils.defaultString(exchangeLog.getExpressStatus())) {
                            case Constant.expressStatus.STATUS_0:
                                orderStatus = "待发货";
                                break;
                            case Constant.expressStatus.STATUS_1:
                                orderStatus = "已发货";
                                break;
                            case Constant.expressStatus.STATUS_2:
                                orderStatus = "已完成";
                                break;
                        }
                    }
                    break;

                case Constant.exchangeStatus.STATUS_1:
                    orderStatus = "兑换失败";
                    break;

                case Constant.exchangeStatus.STATUS_2:
                    orderStatus = "兑换中";
                    break;

                case Constant.exchangeStatus.STATUS_3:
                    orderStatus = "订单已关闭";
                    break;

                case Constant.exchangeStatus.STATUS_4:
                    orderStatus = "微信退款申请";
                    break;

                case Constant.exchangeStatus.STATUS_5:
                    orderStatus = "未发货已撤单";
                    break;

                case Constant.exchangeStatus.STATUS_6:
                    orderStatus = "微信退款失败";
                    break;

                case Constant.exchangeStatus.STATUS_7:
                    orderStatus = "退货审核中";
                    break;

                case Constant.exchangeStatus.STATUS_8:
                    orderStatus = "退货处理中";
                    break;

                case Constant.exchangeStatus.STATUS_9:
                    orderStatus = "退货已完成";
                    break;

                case Constant.exchangeStatus.STATUS_10:
                    orderStatus = "待提交退货物流";
                    break;
            }
        }
        return orderStatus;
    }

    /**
     * 退货审核
     *
     * @param optType
     * @param exchangeId
     * @param goodsReturnAudit
     * @param currUser
     */
    public void updateGoodsReturnAudit(String optType, String exchangeId, String goodsReturnAudit, SysUserBasis currUser) {
        VpointsExchangeLog exchangeLog = findById(exchangeId);
        if (exchangeLog == null) {
            throw new BusinessException("exchangeId:" + exchangeId + "订单不存在");
        } else if (!Constant.exchangeStatus.STATUS_7.equals(exchangeLog.getExchangeStatus())) {
            throw new BusinessException("更新失败，订单状态异常");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("exchangeId", exchangeId);
        map.put("goodsReturnAuditUser", currUser.getPhoneNum());
        map.put("goodsReturnAuditStatus", optType);
        // 审核通过， 订单状态更新为退货处理中，等待用户提交退货物流信息
        if ("0".equals(optType)) {
            map.put("exchangeMsg", "待提交退货物流");
            map.put("exchangeStatus", Constant.exchangeStatus.STATUS_10);
            map.put("goodsReturnAudit", StringUtils.defaultIfBlank(goodsReturnAudit, "退货申请审核通过"));

            // 驳回、订单状态还原成已完成
        } else if ("1".equals(optType)) {
            map.put("exchangeMsg", "兑换成功");
            map.put("exchangeStatus", Constant.exchangeStatus.STATUS_0);
            map.put("goodsReturnAudit", StringUtils.defaultIfBlank(goodsReturnAudit, "退货申请驳回"));
        } else {
            throw new BusinessException("更新失败，操作类型异常");
        }

        // 更新退货的审核状态
        exchangeDao.updateGoodsReturnAudit(map);

        logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(map), "更新退货申请审核状态");
    }

    /**
     * 更新订单的物流信息
     *
     * @param exchangeId     订单编号
     * @param expressCompany 物流公司
     * @param expressNumber  物流单号
     */
    public void updateExpressInfo(String exchangeId, String expressCompany, String expressNumber) {
//        if (StringUtils.isBlank(exchangeId) 
//        		|| StringUtils.isBlank(expressCompany) 
//        		|| StringUtils.isBlank(expressNumber)) {
//            throw new BusinessException("更新失败，参数不完整！");
//        }

        if (StringUtils.isBlank(exchangeId)) {
            throw new BusinessException("更新失败，参数不完整！");
        }

        // 校验订单状态必为0:未发货
        VpointsExchangeLog exchangeLog = exchangeDao.findById(exchangeId);
        if (exchangeLog == null) {
            throw new BusinessException("更新失败，订单不存在！");

            // 非兑换成功订单
        } else if (!Constant.exchangeStatus.STATUS_0.equals(exchangeLog.getExchangeStatus())) {
            throw new BusinessException("更新失败，订单状态异常！");

            // 物流状态校验
        } else if (!Constant.expressStatus.STATUS_0.equals(exchangeLog.getExpressStatus())
                && !Constant.expressStatus.STATUS_1.equals(exchangeLog.getExpressStatus())) {
            throw new BusinessException("更新失败，订单状态异常！");
        }

        // 更新订单的物流单号及状态
        Map<String, Object> map = new HashMap<>();
        map.put("exchangeId", exchangeId);
        map.put("expressCompany", expressCompany);
        map.put("expressNumber", expressNumber);
        exchangeDao.updateExpressInfo(map);

        // 推送发货成功模板消息
        if (!expressNumber.equals(exchangeLog.getExpressNumber())
                || !expressCompany.equals(exchangeLog.getExpressCompany())) {
            exchangeLog.setExpressNumber(expressNumber);
            VpsConsumerThirdAccountInfo thirdAccountInfo =
                    thirdAccountInfoDao.queryThirdAccountInfoByUserKey(exchangeLog.getUserKey());
            if (null == thirdAccountInfo) {
                throw new BusinessException("更新失败，该订单用户不存在！");
            }
            sendWechatTmpMsg(thirdAccountInfo.getOpenid(),
                    exchangeLog, expressCompany, exchangeLog.getExpressNumber());
            // 礼品卡订单给用户A发送模板消息
            //0 是礼品卡 1 不是礼品卡
            String isGiftCard = "1";
            //0 未领取 1 已领取
            String giftCardStatus = "1";
            if (isGiftCard.equals(exchangeLog.getIsGiftCard()) && giftCardStatus.equals(exchangeLog.getGiftCardStatus())) {
                VpointsExchangeLog giftCardExchange = exchangeDao.queryExchangeLogByGiftCardExchangeId(exchangeLog.getGiftCardExchangeId());
                giftCardExchange.setGoodsName(exchangeLog.getGoodsName());
                sendWechatTmpMsg(giftCardExchange);
            }

            //todo 发送发货短信通知
            // 河北并且是原价商城才发送短信, 其他省区都发
            String expressSMS = DatadicUtil.getDataDicValue(DatadicUtil.dataDicCategory.VPOINTS_ESTORE_COG, DatadicUtil.dataDic.vpointsEstoreCog.vpointsExpressSMS);
            if (StringUtils.isNotBlank(expressSMS)) {
                if ("hebei".equals(DbContextHolder.getDBType())) {
//                VpointsGoodsInfo goodsInfo = goodsDao.getGoodsInfo(exchangeLog.getGoodsId());
//                if (goodsInfo != null && "28876dae-fde9-11e8-8180-224943c59afe".equals(goodsInfo.getBrandId())) {
                    if ("28876dae-fde9-11e8-8180-224943c59afe".equals(exchangeLog.getBrandId())) {
                        SendSMSMsg sendSMSMsg = new SendSMSMsg(exchangeLog.getUserName(), exchangeLog.getPhoneNum(), exchangeId, exchangeLog.getBrandName(), expressSMS);
                        SendSMSMsg.sendSMSMsg(sendSMSMsg);
                    }
                } else {
                    SendSMSMsg sendSMSMsg = new SendSMSMsg(exchangeLog.getUserName(), exchangeLog.getPhoneNum(), exchangeId, exchangeLog.getBrandName(), expressSMS);
                    SendSMSMsg.sendSMSMsg(sendSMSMsg);
                }
            }


        }
        logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(map), "更新物流单号");
    }

    /**
     * 更新订单签收状态，发货后15天
     */
    public void updateExpressSignJob() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("serverName", DbContextHolder.getDBType());
        exchangeDao.updateExpressSignJob(map);
    }

    /**
     * 更新Excel中订单的物流单号
     *
     * @throws Exception
     */
    public List<VpointsExchangeLog> importExchangeExpressList(MultipartFile file) throws Exception {

        // 校验文件
        if (file == null || file.isEmpty()) {
            throw new BusinessException("请选择要上传的发货表格清单！");

        } else if (!file.getOriginalFilename().toLowerCase().endsWith(".xlsx")
                && !file.getOriginalFilename().toLowerCase().endsWith(".xls") && !file.getOriginalFilename().toLowerCase().endsWith(".csv")) {
            throw new BusinessException("请上传正确的Excel格式发货表格清单！");
        }

        // 上传文件校验表头及属性名称
        String[] titleNames = {"序号", "订单编号", "支付单号", "兑换品牌", "兑换商品名称", "商品编号", "数量", "单品积分", "订单积分", "订单金额", "累计退款积分", "累计退款金额", "订单类型", "使用权益", "赠品名称", "赠品数量", "收件人", "电话", "省", "市", "县", "地址", "客户留言", "订单时间", "物流公司", "物流单号", "礼品卡兑换订单", "发货备注"};
        String[] entityNames = {"index", "exchangeId", "transactionId", "brandName", "goodsName", "goodsClientNo", "exchangeNum", "unitVpoints", "exchangeVpoints", "exchangePayMoney", "refundVpoints", "refundPayMoney", "orderType", "quanYiType", "giftGoodsName", "giftGoodsNum", "userName", "phoneNum", "province", "city", "county", "address", "customerMessage", "exchangeTime", "expressCompany", "expressNumber", "isGiftCard", "expressSendMessage"};

        ExcelUtil<VpointsExchangeLog> expressExcel = new ExcelUtil<VpointsExchangeLog>();
        List<VpointsExchangeLog> resultList = new ArrayList<VpointsExchangeLog>();
        List<VpointsExchangeLog> returnLst = new ArrayList<VpointsExchangeLog>();
        try {
            InputStream inputStream = file.getInputStream();
            Workbook workBook = new XSSFWorkbook(inputStream);
            resultList = expressExcel.readContent(workBook, resultList,
                    new VpointsExchangeLog(), 0, entityNames, titleNames);
        } catch (Exception e) {
            throw e;
        }

        Map<String, Object> map = null;

        // 校验并更新订单的物流单号
        List<Map<String, Object>> logLst = new ArrayList<>();
        if (CollectionUtils.isEmpty(resultList)) throw new BusinessException("上传的发货表格清单记录为空！");
        VpointsExchangeLog exchangeLog = null;
        for (VpointsExchangeLog item : resultList) {
            exchangeLog = exchangeDao.findById(item.getExchangeId());
            if (exchangeLog == null) {
                throw new BusinessException("更新失败，订单编号：" + item.getExchangeId() + "不存在！");
            } else if (!Constant.exchangeStatus.STATUS_0.equals(exchangeLog.getExchangeStatus())
                    || !Constant.expressStatus.STATUS_0.equals(exchangeLog.getExpressStatus())) {
                throw new BusinessException("更新失败，订单编号：" + item.getExchangeId() + "订单当前状态为" + transExchangeOrderStatus(exchangeLog));
            }

            // 校验关键字段
            if (exchangeLog.getExchangeVpoints() != item.getExchangeVpoints()
                    || !StringUtils.defaultString(exchangeLog.getGoodsClientNo()).equals(StringUtils.defaultString(item.getGoodsClientNo()))
                    || !StringUtils.defaultString(exchangeLog.getUserName()).equals(StringUtils.defaultString(item.getUserName()))
                    || !StringUtils.defaultString(exchangeLog.getPhoneNum()).equals(StringUtils.defaultString(item.getPhoneNum()))
                    || !StringUtils.defaultString(exchangeLog.getExchangeTime()).equals(StringUtils.defaultString(item.getExchangeTime()))) {
                throw new BusinessException("更新失败，订单编号：" + item.getExchangeId() + ", 订单信息与数据库不一致");
            }

            // 填充品牌Id, 在发短信时会, 用来判断是否可以发送短信
            item.setBrandId(exchangeLog.getBrandId());

            // 上传订单的物流单号存在且与数据库不一致时，更新物流单号
            if (StringUtils.isNotBlank(item.getExpressNumber())
                    && StringUtils.isNotBlank(item.getExpressCompany())) {
                boolean flag = false;
                map = new HashMap<>();
                map.put("exchangeId", item.getExchangeId());
                map.put("expressSendMessage", item.getExpressSendMessage());
                if (!item.getExpressNumber().equals(exchangeLog.getExpressNumber())) {
                    flag = true;
                    map.put("expressNumber", item.getExpressNumber());
                }
                if (!item.getExpressCompany().equals(exchangeLog.getExpressCompany())) {
                    flag = true;
                    map.put("expressCompany", item.getExpressCompany());
                }
                if (flag) {
                    exchangeDao.updateExpressInfo(map);
                    returnLst.add(item);
                    
                    // 加入操作日志Lst
                    logLst.add(map);
                }
            }

            // 操作日志
            if (logLst.size() == 100) {
                logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(logLst), "批量更新物流单号");
                logLst.clear();
            }
        }

        // 操作日志
        if (CollectionUtils.isNotEmpty(logLst)) {
            logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(logLst), "批量更新物流单号");
        }
        
        return returnLst;
    }
    
    /**
     * 待发货导入发货信息时短信通知
     */
    public void sendExpressSMS(List<VpointsExchangeLog> exchangeLogLst) {
        try {
            VpointsExchangeLog exchangeLog = null;
            VpsConsumerThirdAccountInfo thirdAccountInfo;
            for (VpointsExchangeLog item : exchangeLogLst) {
                exchangeLog = exchangeDao.findById(item.getExchangeId());
                
                // 推送发货成功模板消息
                thirdAccountInfo = thirdAccountInfoDao
                        .queryThirdAccountInfoByUserKey(exchangeLog.getUserKey());
                sendWechatTmpMsg(thirdAccountInfo.getOpenid(), exchangeLog, item.getExpressCompany(), item.getExpressNumber());
                
            }
            
            SendSMSMsg.sendBatchSMSMsg(exchangeLogLst);
        } catch (Exception e) {
            log.error("发送发货通知失败", e);
        }
    }

    /**
     * 推送发货成功模板消息
     *
     * @param exchangeLog
     * @param expressCompany
     * @param expressNumber
     */
    private void sendWechatTmpMsg(String openid, VpointsExchangeLog exchangeLog, String expressCompany, String expressNumber) {
        // 河北纯积分商城兑换订单不发发货通知
        if ("hebei".equals(DbContextHolder.getDBType())
                && Constant.exchangeChannel.CHANNEL_1.equals(exchangeLog.getExchangeChannel())) return;
        // 发送模板消息
        try {
            VpsConsumerThirdAccountInfo thirdAccountInfo =
                    thirdAccountInfoDao.queryThirdAccountInfoByUserKey(exchangeLog.getUserKey());
            // 公众号模板消息
//            WechatTemplateMsg templetMsg = new WechatTemplateMsg(thirdAccountInfo.getOpenid(), WechatTemplateMsg.msgType.EXPRESS_SEND);
//            String title = "您的订单已发货";
//            String address = exchangeLog.getAddress() + " " + exchangeLog.getUserName() + " " + exchangeLog.getPhoneNum();
//            String url = PropertiesUtil.getPropertyValue("wechat_h5_domain") + "v/fjmall/order.html?openid=" + openid +  "&orderState=receive";
//            templetMsg.initExpressSendMsg(title, exchangeLog.getExchangeId(), exchangeLog.getGoodsName(), expressCompany, expressNumber, address, url);
//            taskExecutor.execute(templetMsg);

            // 小程序模板消息
            String appid = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                    DatadicKey.filterWxPayTemplateInfo.PAAPPLET_APPID);
            String appsec = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                    DatadicKey.filterWxPayTemplateInfo.PAAPPLET_SECRET);
            String templateId = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                    DatadicKey.filterWxPayTemplateInfo.WECHAT_TMPMSG_PAAPPLET_EXPRESSSEND);
            String pagePath = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                    DatadicKey.filterWxPayTemplateInfo.WECHAT_TMPMSG_PAAPPLET_EXPRESSSEND_PAGEPATH);
            String messageOpenid = thirdAccountInfo.getPaOpenid();

            //只有会员体系开关开启并且支付金额大于0（即兑换类型是包含金额的）才会调用会员商城的推送相关参数
            if(DatadicUtil.isSwitchON(DatadicUtil.dataDicCategory.FILTER_SWITCH_SETTING, DatadicUtil.dataDic.filterSwitchSetting.SWITCH_VIP_SYSTEM) && exchangeLog.getExchangePay() > 0) {
                appid = DatadicUtil.getDataDicValue(
                        DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                        DatadicKey.filterWxPayTemplateInfo.MEMBERAPPLET_APPID);
                templateId = DatadicUtil.getDataDicValue(
                        DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                        DatadicKey.filterWxPayTemplateInfo.WECHAT_TMPMSG_MEMBER_APPLET_EXPRESSSEND);
                pagePath = DatadicUtil.getDataDicValue(
                        DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                        DatadicKey.filterWxPayTemplateInfo.WECHAT_TMPMSG_MEMBER_APPLET_EXPRESSSEND_PAGEPATH);
                messageOpenid = thirdAccountInfo.getMemberOpenid();
            }

            //开启了会员体系或者积分商城开关的需要带上省区标识
            if(DatadicUtil.isSwitchON(DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING, DatadicKey.filterSwitchSetting.SWITCH_VIP_SYSTEM))
                    ||DatadicUtil.isSwitchON(DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING, DatadicKey.filterSwitchSetting.SWITCH_PIJIUHUA))) {
                pagePath += "&projectFlag=" + DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_COMPANY_INFO, DatadicKey.filterCompanyInfo.PROJECT_FLAG);
            }
            
            ExpressStatusTempletMsg templetMsg = new ExpressStatusTempletMsg(messageOpenid, appid, appsec, templateId, pagePath, exchangeLog);
            taskExecutor.execute(templetMsg);
        } catch (Exception e) {
            log.error("推送已发货模板消息失败：exchangeId" + exchangeLog.getExchangeId(), e);
        }
    }

    /**
     * 礼品卡推送发货成功模板消息
     *
     * @param exchangeLog
     * @param expressCompany
     * @param expressNumber
     */
    private void sendWechatTmpMsg(VpointsExchangeLog exchangeLog) {
        // 河北纯积分商城兑换订单不发发货通知
        if ("hebei".equals(DbContextHolder.getDBType())
                && Constant.exchangeChannel.CHANNEL_1.equals(exchangeLog.getExchangeChannel())) return;
        // 发送模板消息
        try {
            VpsConsumerThirdAccountInfo thirdAccountInfo =
                    thirdAccountInfoDao.queryThirdAccountInfoByUserKey(exchangeLog.getUserKey());
            VpsConsumerUserInfo vpsConsumerUserInfo = vpsConsumerUserInfoService.queryUserInfoByOpenid(thirdAccountInfo.getOpenid());
            // 小程序模板消息
            if (StringUtils.isBlank(thirdAccountInfo.getPaOpenid())) return;
            String appid = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                    DatadicKey.filterWxPayTemplateInfo.PAAPPLET_APPID);
            String appsec = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                    DatadicKey.filterWxPayTemplateInfo.PAAPPLET_SECRET);
            String templateId = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                    DatadicKey.filterWxPayTemplateInfo.WECHAT_TMPMSG_PAAPPLET_EXPRESSSEND);
            String pagePath = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                    DatadicKey.filterWxPayTemplateInfo.WECHAT_TMPMSG_PAAPPLET_EXPRESSSEND_PAGEPATH);
            String messageOpenid = thirdAccountInfo.getPaOpenid();

            //只有会员体系开关开启并且支付金额大于0（即兑换类型是包含金额的）才会调用会员商城的推送相关体系
            if(DatadicUtil.isSwitchON(DatadicUtil.dataDicCategory.FILTER_SWITCH_SETTING, DatadicUtil.dataDic.filterSwitchSetting.SWITCH_VIP_SYSTEM) && exchangeLog.getExchangePay() > 0) {
            	appid = DatadicUtil.getDataDicValue(
                        DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                        DatadicKey.filterWxPayTemplateInfo.MEMBERAPPLET_APPID);
                templateId = DatadicUtil.getDataDicValue(
                        DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                        DatadicKey.filterWxPayTemplateInfo.WECHAT_TMPMSG_MEMBER_APPLET_EXPRESSSEND);
                pagePath = DatadicUtil.getDataDicValue(
                        DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                        DatadicKey.filterWxPayTemplateInfo.WECHAT_TMPMSG_MEMBER_APPLET_EXPRESSSEND_PAGEPATH);
                messageOpenid = thirdAccountInfo.getMemberOpenid();
            }

            //开启了会员体系或者积分开关的需要带上省区标识
            if(DatadicUtil.isSwitchON(DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING, DatadicKey.filterSwitchSetting.SWITCH_VIP_SYSTEM))
                    ||DatadicUtil.isSwitchON(DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING, DatadicKey.filterSwitchSetting.SWITCH_PIJIUHUA))) {
                pagePath += "&projectFlag=" + DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_COMPANY_INFO, DatadicKey.filterCompanyInfo.PROJECT_FLAG);
            }
            		
            ExpressStatusTempletMsg templetMsg = new ExpressStatusTempletMsg(messageOpenid, appid, appsec, templateId, pagePath, exchangeLog, exchangeLog.getGoodsName(), vpsConsumerUserInfo.getNickName());
            taskExecutor.execute(templetMsg);
        } catch (Exception e) {
            log.error("推送已发货模板消息失败：exchangeId" + exchangeLog.getExchangeId(), e);
        }
    }

    /**
     * 获取截止日期前所有未核销订单
     *
     * @param map verificationEndDate 核销截止日期
     * @return
     */
    public List<VpointsExchangeLog> queryVerificationByDate(String verificationEndDate, String brandKeys) {
        Map<String, Object> map = new HashMap<>();
        map.put("verificationEndDate", verificationEndDate);
        map.put("brandKeys", Arrays.asList(brandKeys.split(",")));
        return exchangeDao.queryVerificationByDate(map);
    }

    /**
     * 更新核销主键
     *
     * @param exchangeId     订单主键
     * @param verificationId 核销记录主键
     */
    public void updateVerificationId(String exchangeId, String verificationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("exchangeId", exchangeId);
        map.put("verificationId", verificationId);
        exchangeDao.updateVerificationId(map);
    }

    /**
     * 清除核销主键
     *
     * @param verificationId 核销记录主键
     */
    public void updateVerificationIdForClear(String verificationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("verificationId", verificationId);
        exchangeDao.updateVerificationIdForClear(map);
    }

    /**
     * 更新订单的收货信息
     *
     * @param exchangeId 订单号
     * @param userName   用户名称
     * @param phoneNum   电话
     * @param address    地址
     */
    public void updateOrderAddress(String exchangeId, String userName, String phoneNum, String address) {

        if (StringUtils.isBlank(exchangeId) || StringUtils.isBlank(userName)
                || StringUtils.isBlank(phoneNum) || StringUtils.isBlank(address)) {
            throw new BusinessException("更新失败，参数不完整！");
        }

        // 校验订单状态必为0:未发货
        VpointsExchangeLog exchangeLog = exchangeDao.findById(exchangeId);
        if (exchangeLog == null) {
            throw new BusinessException("更新失败，订单不存在！");
        } else if (Constant.expressStatus.STATUS_2.equals(exchangeLog.getExpressStatus())) {
            throw new BusinessException("更新失败，订单状态异常！");
        }

        // 更新订单收货信息
        Map<String, Object> map = new HashMap<>();
        map.put("exchangeId", exchangeId);
        map.put("userName", userName);
        map.put("phoneNum", phoneNum);
        map.put("address", address);
        exchangeDao.updateOrderAddress(map);

        logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(map), "更新订单收货信息");

    }

    /**
     * 撤单(业务已迁至接口)
     *
     * @param exchangeId
     */
    @Deprecated
    public void executeRevokeOrder(String exchangeId) {
        if (StringUtils.isBlank(exchangeId)) {
            throw new BusinessException("撤单失败，参数不完整！");
        }

        VpointsExchangeLog exchangeLog = exchangeDao.findById(exchangeId);
        if (null == exchangeLog) {
            throw new BusinessException("撤单失败，该订单不存在！");
        }
        Map<String, Object> map = new HashMap<>();

        // 返还商品的剩余数量
        map.clear();
        map.put("goodsId", exchangeLog.getGoodsId());
        map.put("exchangeNum", exchangeLog.getExchangeNum());
        goodsDao.updateGoodsRemains(map);

        // 更新订单状态
        map.clear();
        map.put("exchangeId", exchangeLog.getExchangeId());
        map.put("exchangeStatus", Constant.exchangeStatus.STATUS_5);
        map.put("expressStatus", Constant.expressStatus.STATUS_3);
        exchangeDao.updateOrderStatus(map);

        // 返还用户兑换积分
        map.clear();
        map.put("exchangeId", exchangeLog.getExchangeId()); // 用来记录日志订单号
        map.put("userKey", exchangeLog.getUserKey());
        map.put("vpoint", exchangeLog.getExchangeVpoints());
        accountInfoDao.updateForRevokeOrder(map);

        // 返还用户的商品兑换限制
        String limitKey = exchangeLog.getGoodsId() + "_" + exchangeLog.getUserKey();
        RedisApiUtil.getInstance().setHincrBySet(RedisApiUtil.CacheKey.vpointsShop.GOODS_LIMIT, limitKey, -exchangeLog.getExchangeNum());

        logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(map), "撤单：" + exchangeLog.getExchangeId());
    }

    /**
     * 撤单(业务已迁至接口)
     *
     * @param exchangeId
     */
    public void revokeOrder(String exchangeId, String refundType, String refundMoney, String refundVpoints, String revokeOrderReason) throws Exception {
        if (StringUtils.isBlank(exchangeId)) {
            throw new BusinessException("撤单失败，参数不完整！");
        }
        long refundPay = (long) MathUtil.round(Double.valueOf(StringUtils.defaultIfBlank(refundMoney, "0.00")) * 100, 0);
//        if (refundPay == 0 && "0".equals(StringUtils.defaultIfBlank(refundVpoints, "0"))) {
//            throw new BusinessException("撤单失败，请输入要退款金额或积分！");
//        }

        Map<String, Object> argsMap = new HashMap<>();
        argsMap.put("exchangeId", exchangeId);
        argsMap.put("refundType", refundType);
        argsMap.put("refundPay", refundPay);
        argsMap.put("refundVpoints", refundVpoints);
        argsMap.put("revokeOrderReason", revokeOrderReason);
        argsMap.put("projectServerName", DbContextHolder.getDBType());
        log.info("请求地址==" + DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_HTTP_URL,
                DatadicKey.filterHttpUrl.PROJECT_INTERFACE_URL));

        String interfaceUrl = DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_HTTP_URL,
                DatadicKey.filterHttpUrl.PROJECT_INTERFACE_URL) + "/vjifenInterface/vpoints/vpointsExchange/revokeExchangeInfo";
//		String interfaceUrl = PropertiesUtil.getPropertyValue("project_interface") + "/vpoints/vpointsExchange/revokeExchangeInfo";
//		JSONObject revokeJson = HttpReq.handerHttpReq(interfaceUrl, JSON.toJSONString(argsMap));
        JSONObject revokeJson = HttpsUtil.doPostForJson(interfaceUrl, JSON.toJSONString(argsMap));
        if (null == revokeJson) {
            throw new BusinessException("远程服务器无法连接！");
        } else {
            // {"replyTime":1577698944791,"result":{"argsMap":{},"batchFlage":false,"businessCode":"1","code":"0","msg":"撤单失败，该订单状态异常！","validFlage":false}}
            BaseReplyResult baseResult = revokeJson.getObject("result", BaseReplyResult.class);
            if (baseResult != null) {
                if (!ResultCode.SUCCESS.equals(baseResult.getCode())
                        || !ResultCode.SUCCESS.equals(baseResult.getBusinessCode())) {
                    throw new BusinessException(StringUtils.defaultIfBlank(baseResult.getMsg(), "撤单失败"));
                }
            } else {
                throw new BusinessException("远程服务器无法连接！");
            }
        }
    }

    /**
     * 查询订单汇总数据
     *
     * @param infoKey
     * @param exchangeFlag 兑换标识：1普通兑换，2拼团兑换，3秒杀兑换
     * @return
     */
    public VpointsSeckillOrderStatistics findOrderStatistics(String activityKey, String exchangeFlag) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activityKey", activityKey);
        map.put("exchangeFlag", exchangeFlag);
        return exchangeDao.findOrderStatistics(map);
    }

    /**
     * 导出订单相关数据（秒杀订单、拼团订单、优惠券订单等）
     *
     * @param queryBean
     * @param response
     * @throws Exception
     */
    public void exportOrderRecord(VpointsExchangeLog queryBean, HttpServletResponse response) throws Exception {
        // 订单列表
        List<VpointsExchangeLog> resultList = queryForExpressLst(queryBean, null);

        // 响应头
        response.reset();
        response.setCharacterEncoding("GBK");
        response.setContentType("application/msexcel;charset=UTF-8");

        String bookName = "";
        String[] headers = null;
        String[] valueTags = null;
        OutputStream outStream = response.getOutputStream();

        if (StringUtils.isNotBlank(queryBean.getCouponKey())) {
            bookName = "优惠券核销数据";
            headers = new String[]{"订单编号", "商品名称", "购买件数", "订单积分", "优惠金额", "订单积分", "优惠积分", "收件人", "订单时间", "订单状态"};

            valueTags = new String[]{"exchangeId", "goodsName", "exchangeNum", "exchangePay", "couponDiscountPay", "exchangeVpoints", "couponDiscountVpoints", "userName", "", "exchangeTime", "orderStatus"};
        } else if (StringUtils.isNotBlank(queryBean.getSeckillActivityKey())) {
            bookName = "秒杀下单数据";
            headers = new String[]{"订单编号", "兑换品牌", "商品名称", "收件人", "购买方法", "订单金额", "优惠金额", "订单积分", "优惠积分", "订单时间", "物流公司", "物流单号", "发货时间", "收货时间", "订单状态"};

            valueTags = new String[]{"exchangeId", "brandName", "goodsName", "userName", "payTypeName", "exchangePayForExcel", "discountsMoneyForExcel", "exchangeVpoints", "discountsVpoints", "exchangeTime", "expressCompany", "expressNumber", "expressSendTime", "expressSignTime", "orderStatus"};
        } else if (StringUtils.isNotBlank(queryBean.getGroupBuyingActivityKey())) {
            bookName = "拼团下单数据";
            headers = new String[]{"订单编号", "兑换品牌", "商品名称", "收件人", "购买方法", "订单金额", "优惠金额", "订单积分", "优惠积分", "订单时间", "物流公司", "物流单号", "发货时间", "收货时间", "订单状态"};

            valueTags = new String[]{"exchangeId", "brandName", "goodsName", "userName", "payTypeName", "exchangePayForExcel", "discountsMoneyForExcel", "exchangeVpoints", "discountsVpoints", "exchangeTime", "expressCompany", "expressNumber", "expressSendTime", "expressSignTime", "orderStatus"};
        }


        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(bookName, "UTF-8") + DateUtil.getDate() + ".xls");
        ExcelUtil<VpointsExchangeLog> excel = new ExcelUtil<VpointsExchangeLog>();
        excel.writeExcel(bookName, headers, valueTags, resultList, DateUtil.DEFAULT_DATETIME_FORMAT, outStream);
        outStream.close();
        response.flushBuffer();
    }

    public List<VpointsExchangeLog> queryAllNotReceiveGiftCardOrder(Integer days) {
        return exchangeDao.queryAllNotReceiveGiftCardOrder(days);
    }

    public List<VpointsExchangeLog> findByShopOrderId(String shopOrderId) {
        return exchangeDao.findByShopOrderId(shopOrderId);
    }
}

/**
 * 发货通知
 */
class ExpressStatusTempletMsg implements Runnable {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String paOpenid;
    private String appid;
    private String appsec;
    private String templateId;
    private String pagePath;
    private VpointsExchangeLog exchangeLog;
    private String thing3;
    private String nickName;

    public ExpressStatusTempletMsg(String paOpenid, String appid,
                                   String appsec, String templateId, String pagePath, VpointsExchangeLog exchangeLog) {
        this.paOpenid = paOpenid;
        this.appid = appid;
        this.appsec = appsec;
        this.templateId = templateId;
        this.pagePath = pagePath;
        this.exchangeLog = exchangeLog;
    }

    public ExpressStatusTempletMsg(String paOpenid, String appid,
                                   String appsec, String templateId, String pagePath, VpointsExchangeLog exchangeLog, String thing3, String nickName) {
        this.paOpenid = paOpenid;
        this.appid = appid;
        this.appsec = appsec;
        this.templateId = templateId;
        this.pagePath = pagePath;
        this.exchangeLog = exchangeLog;
        this.thing3 = thing3;
        this.nickName = nickName;
    }

    @Override
    public void run() {
        String currDate = DateUtil.getDate();
        try {
            String sendText = this.initMsg(paOpenid, currDate);
            if (StringUtils.isNotBlank(sendText)) {
                boolean sendFlag = WechatUtil.sendAppletTemplateMsg(sendText, appid, appsec);
                log.error("发货通知小程序订阅消息发送" + (sendFlag ? "成功" : "失败") + "，paOpenid:" + paOpenid);
            }
        } catch (Exception e) {
            log.error("发货通知小程序订阅消息发送失败，paOpenid:" + paOpenid);
        }

    }

    private String initMsg(String openid, String currDate) throws Exception {
        MsgAppletBean msg = new MsgAppletBean(PropertiesUtil.getPropertyValue("run_env"));
        msg.setTouser(openid);
        msg.setTemplate_id(templateId);

        // 跳转连接及参数 
        msg.setPage(this.pagePath);

        MsgAppletData data = new MsgAppletData();
        data.setCharacter_string1(new MsgValue(exchangeLog.getExchangeId(), null));
        data.setThing2(new MsgValue(exchangeLog.getGoodsShortName(), null));
        if (StringUtils.isEmpty(this.thing3)) {
            data.setThing3(new MsgValue("您的商品已发货，请注意查收", null));
        } else {
            String nickname = this.nickName == null ? "" : this.nickName;
            data.setThing3(new MsgValue("您朋友" + nickname + "的商品已发货，请注意查收", null));
        }

        msg.setData(data);
        return JSON.toJSONString(msg);
    }
}

/**
 * 短信通知
 */
@Data
@Builder
class SendSMSMsg {
    private static final org.apache.log4j.Logger smsLog = org.apache.log4j.Logger.getLogger("sendSmsLog");

    // 默认短信内容
    final String DEFAULT_SMS_CONTEXT = "【欢聚青啤】您购买的啤酒已由顺丰快递发出，如超72小时未收到货，请及时联系我们，祝您生活愉快!";

    String userName;//用户名称
    String phone;//用户手机号
    String exchangeId;//订单编号
    String type;//类型 (商城发货; 大奖发货)
    String SMSContext;//短信内容

    public SendSMSMsg(String userName, String phone, String exchangeId, String type, String SMSContext) {
        this.userName = userName;
        this.phone = phone;
        this.exchangeId = exchangeId;
        this.type = type;
        this.SMSContext = SMSContext;
    }

    public SendSMSMsg(String userName, String phone, String exchangeId, String type) {
        this.userName = userName;
        this.phone = phone;
        this.exchangeId = exchangeId;
        this.type = type;
        this.SMSContext = DEFAULT_SMS_CONTEXT;
    }

    public String toLogString() {
        return "SendSMSMsg{" +
                "dbType='" + DbContextHolder.getDBType() +
                "', 类型='" + type +
                "', 订单号=" + exchangeId +
                "', 手机号=" + phone +
                "', 收件人=" + userName +
                "', 发送时间=" + DateUtil.getDateTime() +
                "', 短信内容='" + SMSContext +
                "'}";
    }

    /**
     * 发送短信
     *
     * @param sendSMSMsg
     */
    public static void sendSMSMsg(SendSMSMsg sendSMSMsg) {

        // 发送短信
        String groupSwitch = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING, DatadicKey.filterSwitchSetting.SWITCH_SEND_MSG);
        if (DatadicUtil.isSwitchON(groupSwitch)) {
            if (!StringUtils.isEmpty(sendSMSMsg.phone)) {
                // 日志记录
                smsLog.info(sendSMSMsg.toLogString());
                
                // 发送短信
                String sendSmsState = SendSMSUtil.sendSms2(sendSMSMsg.SMSContext, sendSMSMsg.phone);
                if (!"0".equalsIgnoreCase(sendSmsState)) {
                    smsLog.error(sendSMSMsg.toLogString());
                    return;
                }
            }
        }

    }

    /**
     * 批量发送短信, 积分商城-订单管理-批量数据发货
     *
     * @param resultList
     */
    public static void sendBatchSMSMsg(List<VpointsExchangeLog> resultList) {
        String expressSMS = DatadicUtil.getDataDicValue(DatadicUtil.dataDicCategory.VPOINTS_ESTORE_COG, DatadicUtil.dataDic.vpointsEstoreCog.vpointsExpressSMS);
        if (StringUtils.isBlank(expressSMS)) return;
        SendSMSMsg sendSMSMsg = null;
        for (VpointsExchangeLog exchangeLog : resultList) {
            if (StringUtils.isBlank(exchangeLog.getExpressCompany()) && StringUtils.isBlank(exchangeLog.getExpressNumber())) continue;
            if ("hebei".equals(DbContextHolder.getDBType()) && !"28876dae-fde9-11e8-8180-224943c59afe".equals(exchangeLog.getBrandId())) continue;
            
            sendSMSMsg = new SendSMSMsg(exchangeLog.getUserName(), exchangeLog.getPhoneNum(), exchangeLog.getExchangeId(), exchangeLog.getBrandName(), expressSMS);
            sendSMSMsg(sendSMSMsg);
        }
    }


    /**
     * 批量发送短信任务
     */
    static class BatchSendSmsTask extends Thread {
        // 批量发送时使用
        List<VpointsExchangeLog> resultList;

        public BatchSendSmsTask(List<VpointsExchangeLog> resultList) {
            this.resultList = resultList;
        }

        @Override
        public void run() {
            sendBatchSMSMsg(this.resultList);
        }
    }

}
