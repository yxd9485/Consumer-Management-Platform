package com.dbt.platform.autocode.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.datadic.service.ServerInfoService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ExcelUtil;
import com.dbt.framework.util.ExportUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.util.SendSMSUtil;
import com.dbt.platform.activity.bean.VcodeQrcodeBatchInfo;
import com.dbt.platform.autocode.bean.VpsQrcodeOrder;
/**
 * 码源订单Action
 * @author hanshimeng
 *
 */
import com.dbt.platform.autocode.service.VpsQrcodeOrderService;
import com.dbt.platform.codefactory.bean.VpsVcodeFactory;
import com.dbt.platform.codefactory.bean.VpsWinery;
import com.dbt.platform.codefactory.dao.IVpsVcodeFactoryDao;
import com.dbt.platform.codefactory.dao.IVpsVcodeWineryDao;
import com.dbt.platform.codefactory.service.VpsVcodeFactoryService;
import com.dbt.platform.codefactory.service.VpsVcodeWineryService;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.vjifen.server.base.datasource.redis.RedisUtils;

@Controller
@RequestMapping("/vpsQrcodeOrder")
public class VpsQrcodeOrderAction extends BaseAction {

    private static final String LOCK_KEY = "vpsQrcodeOrder:create.lock";
    private static final int LOCK_TIMEOUT = 1000 * 60 * 2; //毫秒 

    @Autowired
    private VpsQrcodeOrderService vpsQrcodeOrderService;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private VpsVcodeFactoryService vpsVcodeFactoryService;
    @Autowired
    private VpsVcodeWineryService vpsVcodeWineryService;
    @Autowired
    private IVpsVcodeFactoryDao iVpsVcodeFactoryDao;
    @Autowired
    private IVpsVcodeWineryDao iVpsVcodeWineryDao;
    @Autowired
    private ServerInfoService serverInfoService;

    /**
     * 码源订单列表
     *
     * @param session
     * @param vpsQrcodeOrder
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("showQrcodeOrderList")
    public String showQrcodeOrderList(HttpSession session, String queryParam, String pageParam, Model model) {
        try {

            String dbType = DbContextHolder.getDBType();
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsQrcodeOrder queryBean = new VpsQrcodeOrder(queryParam);
            List<VpsQrcodeOrder> resultList = vpsQrcodeOrderService.queryForLst(queryBean, pageInfo);

            int countResult = vpsQrcodeOrderService.queryForCount(queryBean);

            // SKU
            List<SkuInfo> skuLst = skuInfoService
                    .loadSkuListByCompany(currentUser.getCompanyKey());

            // 赋码厂
            VpsVcodeFactory queryFactoryBean = new VpsVcodeFactory();
            VpsWinery queryWineryBean = new VpsWinery();
            queryFactoryBean.setProjectServer(DbContextHolder.getDBType());
            queryWineryBean.setProjectServer(DbContextHolder.getDBType());
            DbContextHolder.setDBType(null);
            List<VpsVcodeFactory> qrcodeManufactureGro = vpsVcodeFactoryService.queryForLst(queryFactoryBean, null);

            // 酒厂
            List<VpsWinery> factoryNameGro = vpsVcodeWineryService.queryForLst(queryWineryBean, null);
            for (VpsQrcodeOrder order : resultList) {
                VpsVcodeFactory factory = vpsVcodeFactoryService.findByFactoryName(order.getQrcodeManufacture());
                if (null != factory) {
                    order.setIsLabel(factory.getIsLabel());
                }
            }
            DbContextHolder.setDBType(dbType);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultList);
            model.addAttribute("showCount", countResult);
            model.addAttribute("skuLst", skuLst);
            model.addAttribute("qrcodeManufactureGro", qrcodeManufactureGro);
            model.addAttribute("factoryNameGro", factoryNameGro);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("projectServerName", DbContextHolder.getDBType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/autocode/showQrcodeOrderList";
    }


    /**
     * 删除码源订单
     *
     * @param session
     * @param vpsQrcodeOrder
     * @param model
     * @return
     */
    @RequestMapping("doQrcodeOrderDelete")
    public String doQrcodeOrderDelete(HttpSession session, String orderKey,
                                      String queryParam, String pageParam, Model model) {
        try {
            vpsQrcodeOrderService.deleteById(orderKey);
            model.addAttribute("errMsg", "删除成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "删除失败");
            ex.printStackTrace();
        }
        return "forward:showQrcodeOrderList.do";
    }


    /**
     * 生成码源
     *
     * @param session
     * @param orderKey
     * @param model
     * @return
     */
    @RequestMapping("createCode")
    public String createCode(HttpSession session, HttpServletResponse response,
                             String orderKey, String queryParam, String pageParam, Model model) {
        String lockId = null;
        try {
            //获取锁
            lockId = RedisApiUtil.getInstance().tryGetDistributedLock(LOCK_KEY, null, LOCK_TIMEOUT);
            if (lockId == null) {
                model.addAttribute("errorMsg", "请稍后再试，目前有任务正在生成中。。。");
                model.addAttribute("refresh", "create_qrcode_fail");
            } else {
                SysUserBasis currentUser = this.getUserBasis(session);
                VpsQrcodeOrder qrcodeOrder = vpsQrcodeOrderService.findById(orderKey);

                String dbType = DbContextHolder.getDBType();
                DbContextHolder.setDBType(null);
                VpsVcodeFactory vcodeFactory = vpsVcodeFactoryService.findByFactoryName(qrcodeOrder.getQrcodeManufacture());
                DbContextHolder.setDBType(dbType);

                model = vpsQrcodeOrderService.createCodeMain(qrcodeOrder, vcodeFactory, currentUser.getUserKey(), model);
                model.addAttribute("refresh", "create_qrcode_success");
                RedisApiUtil.getInstance().releaseDistributedLock(LOCK_KEY, lockId, null);
            }
        } catch (BusinessException ex) {
            model.addAttribute("errorMsg", ex.getMessage());
            model.addAttribute("refresh", "create_qrcode_success");

        } catch (Exception ex) {
            model.addAttribute("errorMsg", "码源生成失败：" + ex.getMessage());
            model.addAttribute("refresh", "create_qrcode_fail");
            ex.printStackTrace();
        } finally {
            RedisApiUtil.getInstance().releaseDistributedLock(LOCK_KEY, lockId, null);
        }

        // 修改订单状态为失败
        String errMsg = model.asMap().get("errorMsg").toString();
        if (errMsg.indexOf("码源生成成功") < 0 && !errMsg.contains("其他码源订单生成未结束")) {
            vpsQrcodeOrderService.updateOrderStatus(orderKey, Constant.QRCODE_ORDER_STATUS.order_status_2);
        }
        return "forward:showQrcodeOrderList.do";
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


    /**
     * 跳转创建码源订单页面
     */
    @RequestMapping("/showQrcodeOrderAdd")
    public String showQrcodeOrderAdd(HttpSession session, Model model) {

        SysUserBasis currentUser = this.getUserBasis(session);
        try {
            // SKU
            List<SkuInfo> skuLst = skuInfoService
                    .loadSkuListByCompany(currentUser.getCompanyKey());
            // 赋码厂
            VpsVcodeFactory queryBean = new VpsVcodeFactory();
            queryBean.setProjectServer(DbContextHolder.getDBType());
            DbContextHolder.setDBType(null);
            List<VpsVcodeFactory> factoryList = vpsVcodeFactoryService.queryForLst(queryBean, null);

            model.addAttribute("skuLst", skuLst);
            model.addAttribute("factoryList", factoryList);
            model.addAttribute("currentUser", currentUser);
            return "vcode/autocode/showQrcodeOrderAdd";

        } catch (BusinessException e) {
            model.addAttribute("errMsg", e.getMessage());

        } catch (Exception e) {
            log.error(e);
        }
        return "forward:showQrcodeOrderList.do";
    }

    /**
     * 查询生产厂下拉框
     *
     * @throws NoSuchAlgorithmException
     */
    @RequestMapping("/findWineryList")
    @ResponseBody
    public String findWineryList(HttpSession session, String factoryId, Model model) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            DbContextHolder.setDBType(null);
            VpsVcodeFactory factory = vpsVcodeFactoryService.findById(factoryId);
            List<VpsWinery> wineryLst = vpsVcodeWineryService.findWineryByFactoryLst(factory.getServerWinery());
            resultMap.put("wineryLst", wineryLst);
            resultMap.put("factory", factory);
        } catch (BusinessException e) {
            resultMap.put("errMsg", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(resultMap);
    }


    /**
     * 预览批次信息
     *
     * @param session
     * @param factoryId
     * @param model
     * @return
     */
    @RequestMapping("/showBatchInfoLst")
    @ResponseBody
    public String showBatchInfoLst(HttpSession session, VpsQrcodeOrder vpsQrcodeOrder, Model model) {
        String dbType = DbContextHolder.getDBType();
        vpsQrcodeOrderService.getProjectFlag(vpsQrcodeOrder);
        DbContextHolder.setDBType(null);
        VpsVcodeFactory vpsVcodeFactory = iVpsVcodeFactoryDao.findById(vpsQrcodeOrder.getFactoryId());
        VpsWinery vpsWinery = iVpsVcodeWineryDao.findById(vpsQrcodeOrder.getWineryId());
        Map<String, Object> resultMap = new HashMap<>();
        try {
            DbContextHolder.setDBType(dbType);
            List<VcodeQrcodeBatchInfo> batchLst = vpsQrcodeOrderService.showBatchInfoLst(vpsQrcodeOrder, vpsVcodeFactory, vpsWinery);
            resultMap.put("batchLst", batchLst);
        } catch (BusinessException e) {
            resultMap.put("errMsg", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(resultMap);
    }


    /**
     * 确认机制
     */
    @ResponseBody
    @RequestMapping("/checkBatchInfo")
    public String checkBatchInfo(HttpSession session, VpsQrcodeOrder vpsQrcodeOrder) {
        String dbType = DbContextHolder.getDBType();
        vpsQrcodeOrder.setSkuName(skuInfoService.findById(vpsQrcodeOrder.getSkuKey()).getShortName());
        DbContextHolder.setDBType(null);
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap.put("vpsQrcodeOrder", vpsQrcodeOrderService.checkBatchInfo(vpsQrcodeOrder,dbType));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultMap.put("errMsg", "异常");
        }
        return JSON.toJSONString(resultMap);
    }


    /**
     * 新增码源订单
     *
     * @param session
     * @param vpsQrcodeOrder
     * @param model
     * @return
     */
    @RequestMapping("addQrcodeOrderAndBatch")
    public String addQrcodeOrderAndBatch(HttpSession session, VpsQrcodeOrder vpsQrcodeOrder, Model model) {
        try {
            vpsQrcodeOrderService.getProjectFlag(vpsQrcodeOrder);
            String dbType = DbContextHolder.getDBType();
            //计算批次信息
            DbContextHolder.setDBType(null);
            VpsVcodeFactory vpsVcodeFactory = iVpsVcodeFactoryDao.findById(vpsQrcodeOrder.getFactoryId());
            VpsWinery vpsWinery = iVpsVcodeWineryDao.findById(vpsQrcodeOrder.getWineryId());
            DbContextHolder.setDBType(dbType);
            List<VcodeQrcodeBatchInfo> batchLst = vpsQrcodeOrderService.showBatchInfoLst(vpsQrcodeOrder, vpsVcodeFactory, vpsWinery);
            vpsQrcodeOrder.setBatchInfoList(batchLst);
            DbContextHolder.setDBType(dbType);
            SysUserBasis currentUser = this.getUserBasis(session);
            vpsQrcodeOrderService.create(vpsQrcodeOrder, currentUser);
            model.addAttribute("errMsg", "添加成功");
        } catch (BusinessException e) {
            model.addAttribute("errMsg", e.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "添加失败");
            ex.printStackTrace();
            log.error(ex);
        }
        return "forward:showQrcodeOrderList.do";
    }


    /**
     * 发送码包邮件
     *
     * @param session
     * @param factoryId
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/sendMeilMessage")
    public String sendMeilMessage(HttpServletRequest request, VpsQrcodeOrder vpsQrcodeOrder, Model model) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            vpsQrcodeOrderService.sendMeilMessage(request, vpsQrcodeOrder);
            resultMap.put("errMsg", "发送成功");
            SendSMSUtil.sendSms("尊敬的:" + vpsQrcodeOrder.getBagName() + ",订单名称:" + vpsQrcodeOrder.getOrderName()
                    + "的码源包邮件已经发送到:" + vpsQrcodeOrder.getBagEmail() + ",请注意查收【V积分】", vpsQrcodeOrder.getBagTel());
        } catch (BusinessException e) {
            model.addAttribute("errMsg", e.getMessage());
        } catch (Exception e) {
            resultMap.put("errMsg", "发送失败");
            e.printStackTrace();
        }
        return JSON.toJSONString(resultMap);
    }


    /**
     * 发送密码邮件
     *
     * @param session
     * @param factoryId
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/sendMeilPassWord")
    public String sendMeilPassWord(HttpSession session, VpsQrcodeOrder vpsQrcodeOrder, Model model) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            vpsQrcodeOrderService.sendMeilPassWord(vpsQrcodeOrder);
            resultMap.put("errMsg", "发送成功");
            SendSMSUtil.sendSms("尊敬的:" + vpsQrcodeOrder.getPasswordName() + ",订单名称:" + vpsQrcodeOrder.getOrderName()
                    + "的密码邮件已经发送到:" + vpsQrcodeOrder.getPasswordEmail() + ",请注意查收【V积分】", vpsQrcodeOrder.getPasswordTel());
        } catch (BusinessException e) {
            resultMap.put("errMsg", e.getMessage());
        } catch (Exception e) {
            resultMap.put("errMsg", "发送失败");
            e.printStackTrace();
        }
        return JSON.toJSONString(resultMap);
    }

    /**
     * 发送密码邮件
     *
     * @param session
     * @param factoryId
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/exportOrder")
    public void exportOrder(HttpSession session, String queryParam, String pageParam, Model model, HttpServletResponse response) {
        try {


            String dbType = DbContextHolder.getDBType();
            SysUserBasis currentUser = this.getUserBasis(session);
            VpsQrcodeOrder queryBean = new VpsQrcodeOrder(queryParam);
            List<VpsQrcodeOrder> resultList = vpsQrcodeOrderService.queryExportDataForLst(queryBean);
            // 赋码厂
            VpsVcodeFactory queryFactoryBean = new VpsVcodeFactory();
            VpsWinery queryWineryBean = new VpsWinery();
            queryFactoryBean.setProjectServer(DbContextHolder.getDBType());
            queryWineryBean.setProjectServer(DbContextHolder.getDBType());
            DbContextHolder.setDBType(null);
            for (VpsQrcodeOrder order : resultList) {
                VpsVcodeFactory factory = vpsVcodeFactoryService.findByFactoryName(order.getQrcodeManufacture());
                if (null != factory) {
                    order.setIsLabel(factory.getIsLabel());
                }
            }
            DbContextHolder.setDBType(dbType);
            for (VpsQrcodeOrder order : resultList) {
                //回传码数量
                List<VcodeQrcodeBatchInfo> batchInfoList = order.getBatchInfoList();
                BigDecimal qrcodeAmountSum = new BigDecimal("0");
                BigDecimal qrcodeAmount = new BigDecimal("0");
                BigDecimal packageAmount = new BigDecimal("0");
                List<String> vcodeUniqueCodeList = new ArrayList<>();
                for (int i = 0; i < batchInfoList.size(); i++) {
                    if ("3".equals(batchInfoList.get(i).getStatus()) || "4".equals(batchInfoList.get(i).getStatus())) {
                        VcodeQrcodeBatchInfo vcodeQrcodeBatchInfo = batchInfoList.get(i);
                        qrcodeAmount = new BigDecimal(vcodeQrcodeBatchInfo.getQrcodeAmounts());
                        packageAmount = new BigDecimal(vcodeQrcodeBatchInfo.getPackAmounts());
                        qrcodeAmountSum = qrcodeAmountSum.add(qrcodeAmount);
                        vcodeUniqueCodeList.add(StringUtils.isEmpty(vcodeQrcodeBatchInfo.getVcodeUniqueCode()) ? "" : vcodeQrcodeBatchInfo.getVcodeUniqueCode());
                    }
                }
                order.setQrcodeAmountSum(qrcodeAmountSum.toString());
                //码源订单前三位集合
                order.setVcodeUniqueCode(String.join(",", vcodeUniqueCodeList));

                //订单状态
                if ("0".equals(order.getOrderStatus())) {
                    order.setOrderStatus("未生成");
                }
                if ("1".equals(order.getOrderStatus())) {
                    order.setOrderStatus("生成成功");
                }
                if ("2".equals(order.getOrderStatus())) {
                    order.setOrderStatus("生成失败");
                }

                if ("3".equals(order.getOrderStatus()) || "4".equals(order.getOrderStatus()) || "5".equals(order.getOrderStatus())) {
                    if ("0".equals(order.getImportFlag())) {
                        order.setOrderStatus("未回传");
                    }
                    if ("1".equals(order.getImportFlag())) {
                        order.setOrderStatus("已回传");
                    }
                }
                //是否需要回传
                if ("0".equals(order.getIsWanBatch())) {
                    order.setIsWanBatch("否");
                }
                if ("1".equals(order.getIsWanBatch())) {
                    order.setIsWanBatch("是");
                }
                //回传状态
                if ("0".equals(order.getImportFlag())) {
                    order.setImportFlag("未回传");
                }
                if ("1".equals(order.getImportFlag())) {
                    order.setImportFlag("已回传");
                }

            }
            response.reset();
            response.setCharacterEncoding("GBK");
            response.setContentType("application/msexcel;charset=UTF-8");
            OutputStream outStream = response.getOutputStream();
            String bookName = "码源订单";
            String[] headers = new String[]{"码源订单编号", "订单名称", "客户订单编号", "SKU主键",
                    "SKU名称", "赋码厂", "生产工厂", "细分工厂名称",
                    "细分工厂产线名称", "订单状态", "创建时间", "订单码源数量",
                    "是否需要回传", "回转状态", "回传时间", "回传码源数量",
                    "码源前三位集合"};
            String[] valueTags = new String[]{"orderNo", "orderName", "clientOrderNo", "skuKey",
                    "skuName", "qrcodeManufacture", "brewery", "qrcodeFactoryName",
                    "qrcodeProductLineName", "orderStatus", "createTime", "orderQrcodeCount",
                    "isWanBatch", "importFlag", "importTime", "qrcodeAmountSum",
                    "vcodeUniqueCode"};

            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(bookName, "UTF-8") + DateUtil.getDate() + ".xls");
            ExcelUtil<VpsQrcodeOrder> excel = new ExcelUtil<VpsQrcodeOrder>();
            excel.writeExcel(bookName, headers, valueTags, resultList, DateUtil.DEFAULT_DATETIME_FORMAT, outStream);
            outStream.close();
            response.flushBuffer();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送邮件二次确认框
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/confirmMeil")
    public String confirmMeil(HttpSession session, String orderKey, Model model) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String dbType = DbContextHolder.getDBType();
            VpsQrcodeOrder vpsQrcodeOrder = vpsQrcodeOrderService.findById(orderKey);
            DbContextHolder.setDBType(null);
            VpsVcodeFactory factory = vpsVcodeFactoryService.findByFactoryName(vpsQrcodeOrder.getQrcodeManufacture());
            DbContextHolder.setDBType(dbType);
            vpsQrcodeOrder.setMail(factory);
            vpsQrcodeOrder.setCsEmail(DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_COMPANY_INFO,
                    DatadicKey.filterCompanyInfo.PROJECT_EMAIL));
            resultMap.put("vpsQrcodeOrder", vpsQrcodeOrder);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultMap.put("errMsg", e.getMessage());
        }
        return JSON.toJSONString(resultMap);
    }

    /**
     * 重新生成打印标签
     *
     * @param session
     * @param factoryId
     * @param model
     * @return
     */
    @RequestMapping("/newAddLabel")
    public String newAddLabel(HttpSession session, HttpServletResponse response,
                              String orderKey, String queryParam, String pageParam, Model model) {
        try {
            String DBType = DbContextHolder.getDBType();
            SysUserBasis currentUser = this.getUserBasis(session);
            VpsQrcodeOrder qrcodeOrder = vpsQrcodeOrderService.findById(orderKey);
            DbContextHolder.setDBType(null);
            VpsVcodeFactory factory = vpsVcodeFactoryService.findByFactoryName(qrcodeOrder.getQrcodeManufacture());
            if (null != factory) {
                qrcodeOrder.setIsLabel(factory.getIsLabel());
            }
            DbContextHolder.setDBType(DBType);
            String mess = vpsQrcodeOrderService.newAddLabel(qrcodeOrder, currentUser, model);
            model.addAttribute("errorMsg", mess);
            model.addAttribute("refresh", "create_qrcode_success");
        } catch (Exception e) {
            model.addAttribute("errorMsg", "重新生成打印标签失败：" + e.getMessage());
            model.addAttribute("refresh", "create_qrcode_success");
            e.printStackTrace();
        }
        return "forward:showQrcodeOrderList.do";
    }

    /**
     * 下载码包文件
     */
    @GetMapping("/downloadCode")
    public String downloadCode(String orderKey, String projectServerName, String orderNo, String orderName,
                               String token, HttpServletRequest request, HttpServletResponse response) {
        try {
            DbContextHolder.setDBType(projectServerName);
            VpsQrcodeOrder qrcodeOrder = vpsQrcodeOrderService.findById(orderKey);
            String downLoadFileName = orderNo + "_" + qrcodeOrder.getOrderName();
            if (StringUtils.defaultString(orderName).endsWith("_labeImage")) {
                downLoadFileName += "_labeImage";
            }
            if (StringUtils.isNotEmpty(token)) {
                String t = (String) RedisUtils.get().STRING.HASH.hGet(RedisApiUtil.CacheKey.codeToken.VJIFENCOM_TOKEN, downLoadFileName);
                if (!token.equals(t)) {
                    outPrint(request, response);
                    return null;
                }
            }

            String name = orderKey.substring(0, 8) + "/" + projectServerName + "/" + orderKey + "/4compress/" + downLoadFileName + ".zip";
            String path = getRootPath("autocode/" + name);
            File file = new File(path);
            //文件判断
            if (!file.exists()) {
                System.out.println("file not found");
                outPrint(request, response);
            } else {
                response.setContentType("application/msexcel;charset=UTF-8");
                response.setHeader("Content-disposition", "attachment; filename=invoice.xls");
                ExportUtil.download(file, request, response);
            }
        } catch (Exception e) {
            try {
                outPrint(request, response);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public void outPrint(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        //去除页面乱码
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            StringBuilder builder = new StringBuilder();
            builder.append("<script type=\'text/javascript\' charset=\'UTF-8\'>");
            builder.append("alert(\'暂无数据\');window.close();");
            builder.append("</script>");
            out.print(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getRootPath(String name) {
        String webappRoot = "/data/upload/";
        return webappRoot + name;
    }

    public static void main(String[] args) {
//		List<VcodeQrcodeBatchInfo> batchLst = new ArrayList<>();
//		int batchNumPrefix =  1;
//		long qrcodeToal = 100;
//		long batchNum = 100;
//		int channelCount = 2;
//		System.out.println(103/2);
//		  //剩余码数
//        long surplusQrcode = qrcodeToal;
//        //文件数
//        int batchLstSize = (int) (qrcodeToal / batchNum + (qrcodeToal %  batchNum > 0 ? 1 : 0));
//        //实际文件数                                                                                  
//        int actualBatch  = (batchLstSize / channelCount) * channelCount + (batchLstSize % channelCount > 0 ? channelCount :0);
//       
//        //临界值
//		int criticalNum = batchLstSize - batchLstSize % channelCount;
//		if (criticalNum == actualBatch){
//			criticalNum = 0;
//		}else{		
//	        surplusQrcode = qrcodeToal - ((batchLstSize / channelCount) * channelCount * batchNum);
//		}
//              
//		int surplusBatch = actualBatch-channelCount;
//		for (int i = 1; i <= actualBatch; i++) {
//			batchLst.add(new VcodeQrcodeBatchInfo(i, "AAA" + String.format("%03d", batchNumPrefix),
//					"AAA" + String.format("%03d", batchNumPrefix),
//					i <= criticalNum ? batchNum : (surplusBatch > 0 ? (i >surplusBatch?(qrcodeToal - surplusBatch * batchNum) / channelCount: surplusBatch * batchNum / surplusBatch): surplusQrcode / channelCount)));
//			batchNumPrefix += 1;
//		}
//		for (VcodeQrcodeBatchInfo vcodeQrcodeBatchInfo : batchLst) {
//			System.out.println("码数:"+vcodeQrcodeBatchInfo.getQrcodeAmounts());
//		}s
        SendSMSUtil.sendSms("【V积分】尊敬的:" + "张三" + ",订单名称:" + "广东紫泉1-4444-20200901-崂山8度500X12箱啤【2018】-101个(100个)"
                + "的码源包邮件已经发送到:" + "123@qq.com" + ",请注意查收。", "13633917647");
    }


}
