package com.dbt.platform.autocode.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.action.reply.BaseDataResult;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.bean.Constant.ResultCode;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.util.SendSMSUtil;
import com.dbt.platform.autocode.service.VpsQrcodeAutoOrderService;
import com.dbt.platform.codefactory.bean.VpsVcodeFactory;
import com.dbt.platform.codefactory.bean.VpsWinery;
import com.dbt.platform.codefactory.dao.IVpsVcodeFactoryDao;
import com.dbt.platform.codefactory.dao.IVpsVcodeWineryDao;

@Controller
@RequestMapping("/auth/order")
public class VpsQrcodeAutoOrderAction extends BaseAction {
    private Logger log = Logger.getLogger(VpsQrcodeAutoOrderAction.class);

    @Autowired
    private VpsQrcodeAutoOrderService autoOrderService;
    @Autowired
    private IVpsVcodeFactoryDao iVpsVcodeFactoryDao;
    @Autowired
    private IVpsVcodeWineryDao iVpsVcodeWineryDao;
    
    /**
     * SKU列表查询【高印】
     * @return
     */
    @ResponseBody
    @RequestMapping("/sku")
    public String querySku() {
        BaseDataResult<Map<String, Object>> baseResult = new BaseDataResult<>();
        try {
            baseResult = autoOrderService.querySkuForCPIS();
            
        } catch (Exception e) {
            initBaseDataResult(baseResult, e, "SKU列表查询失败");
        }
        return JSON.toJSONString(baseResult);
    }
    
    /**
     * 创建码源订单
     * 
     * @param amount 码源数量
     * @param skuName SKU名称
     * @param type 参数无用只用于记录
     * @param pack_units_enabled 参数无用只用于记录
     * @return
     */
    @ResponseBody
    @RequestMapping("/add")
    public String addBatch(int amount, String sku_code, String sku_name, String factory_id, String factory_name, String product_line_id, 
                    String product_line_name, String work_group, String machine_id, String client_order_no, long timestamp, String type, String pack_units_enabled) {
        //设置子线程共享
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(servletRequestAttributes,true);
        
        BaseDataResult<Map<String, Object>> baseResult = new BaseDataResult<>();
        try {
            String dbType = DbContextHolder.getDBType();
            DbContextHolder.setDBType(null);
            // 获取蒙牛默认的赋码厂及生成厂
            String vcodeFactoryId = null, wineryId = null;
            if ("202110252315CPIS".equals(DbContextHolder.getMapVal("MN-CLIENTID"))) {
                vcodeFactoryId = "81";
                wineryId = "81";
            } else if ("202401091013LYJG".equals(DbContextHolder.getMapVal("MN-CLIENTID"))) {
                vcodeFactoryId = "112";
                wineryId = "103";
            } else {
                return JSON.toJSONString(baseResult.initReslut(Constant.ResultCode.FILURE, "未识别码源厂"));
            }
            VpsVcodeFactory vpsVcodeFactory = iVpsVcodeFactoryDao.findById(vcodeFactoryId);
            VpsWinery vpsWinery = iVpsVcodeWineryDao.findById(wineryId);
            DbContextHolder.setDBType(dbType);
            
            sku_code = StringUtils.defaultIfBlank(sku_code, "");
            sku_name = StringUtils.defaultIfBlank(sku_name, "");
            baseResult = autoOrderService.addQrcodeOrder(amount, sku_code, sku_name, vpsVcodeFactory, vpsWinery, 
                    factory_id, factory_name, product_line_id, product_line_name, work_group, machine_id, client_order_no, timestamp);
            log.warn("clientOrderNo" + client_order_no + " skucode" + sku_code + " skuname:" + sku_name + " amount:" + amount + " type:" + type + " pack_units_enabled:" + pack_units_enabled + "码源订单创建成功");
            SendSMSUtil.sendSmsByAIOpenid(true, false, "蒙牛" + vpsVcodeFactory.getFactoryName() + "码源订单创建成功", "clientOrderNo:" + client_order_no);
        } catch (Exception e) {
            initBaseDataResult(baseResult, e, "码源订单创建失败");
            log.warn("CLIENTID:" + DbContextHolder.getMapVal("MN-CLIENTID") + "clientOrderNo" + client_order_no + " skucode" + sku_code + "skuname:" + sku_name + " amount:" + amount + " type:" + type + " pack_units_enabled:" + pack_units_enabled + "码源订单创建失败");
            SendSMSUtil.sendSmsByAIOpenid(true, false, "蒙牛自动码源订单创建失败", "clientOrderNo:" + client_order_no);
        }
        return JSON.toJSONString(baseResult);
    }
    
    /**
     * 查询码源订单状态
     * 
     * @param id 码源订单ID
     * @return
     */
    @ResponseBody
    @RequestMapping("/status")
    public String queryOrderStatus(HttpServletRequest request, String id) {
        BaseDataResult<Map<String, Object>> baseResult = new BaseDataResult<>();
        try {
            String contextPath = request.getRequestURL().toString();
            contextPath = contextPath.replace(request.getServletPath(), "");
            baseResult = autoOrderService.queryOrderStatus(id, contextPath);
            log.warn("id:" + id + "查询码源订单状态成功");
            
        } catch (Exception e) {
            initBaseDataResult(baseResult, e, "查询码源订单状态失败");
            log.warn("id:" + id + "查询码源订单状态失败");
        }
        return JSON.toJSONString(baseResult);
    }
    
    /**
     * 码源文件上传
     * 
     * @param t_unit_batch_id 当日批次ID
     * @param file 码源文件
     * @return
     */
    @ResponseBody
    @RequestMapping("/batchUpload")
    @SuppressWarnings("unchecked")
    public String queryOrderStatus(String t_unit_batch_id, MultipartFile file, long timestamp) {
        BaseDataResult<Map<String, Object>> baseResult = new BaseDataResult<>();
        try {
            baseResult = autoOrderService.executeBatchUploadForBatchAutocodeNo(t_unit_batch_id, file, timestamp);
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                baseResult = JSON.parseObject(e.getMessage(), BaseDataResult.class);
            } else {
                baseResult.initReslut(ResultCode.FILURE, "码源文件上传失败");
            }
            log.error(e.getMessage(), e);
        }
        return JSON.toJSONString(baseResult);
    }
    
    /**
     * 码源文件上传状态查询
     * 
     * @param t_unit_batch_id   当日批次ID
     * @param id    上传文件任务ID
     * @return
     */
    @ResponseBody
    @RequestMapping("/batchUploadStatus")
    public String queryOrderStatus(String t_unit_batch_id, String id) {
        BaseDataResult<Map<String, Object>> baseResult = new BaseDataResult<>();
        try {
            baseResult = autoOrderService.queryBatchUploadStatus(t_unit_batch_id, id);
        } catch (Exception e) {
            initBaseDataResult(baseResult, e, "码源文件上传状态查询失败");
        }
        return JSON.toJSONString(baseResult);
    }
}
