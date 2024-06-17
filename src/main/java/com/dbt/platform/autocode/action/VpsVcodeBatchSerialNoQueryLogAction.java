package com.dbt.platform.autocode.action;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ExcelUtil;
import com.dbt.platform.autocode.bean.VpsVcodeBatchSerialNoQueryLog;
import com.dbt.platform.autocode.service.VpsVcodeBatchSerialNoQueryLogService;
import com.dbt.platform.system.bean.SysUserBasis;

@Controller
@RequestMapping("/batchSerialNo")
public class VpsVcodeBatchSerialNoQueryLogAction extends BaseAction {

    @Autowired
    private VpsVcodeBatchSerialNoQueryLogService batchSerialNoLogService;

    /**
     * 列表
     */
    @RequestMapping("showSerialNoLogList")
    public String showSerialNoLogList(HttpSession session, String queryParam, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsVcodeBatchSerialNoQueryLog queryBean = new VpsVcodeBatchSerialNoQueryLog(queryParam);
            List<VpsVcodeBatchSerialNoQueryLog> resultList = batchSerialNoLogService.queryForLst(queryBean, pageInfo);

            int countResult = batchSerialNoLogService.queryForCount(queryBean);

            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultList);
            model.addAttribute("showCount", countResult);
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
        return "vcode/autocode/showSerialNoLogList";
    }


    /**
     * 导出
     *
     * @param session
     * @param factoryId
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/exportSerialNoLog")
    public void exportSerialNoLog(HttpSession session, String queryParam, String pageParam, Model model, HttpServletResponse response) {
        try {
            VpsVcodeBatchSerialNoQueryLog queryBean = new VpsVcodeBatchSerialNoQueryLog(queryParam);
            List<VpsVcodeBatchSerialNoQueryLog> resultList = batchSerialNoLogService.queryForLst(queryBean, null);
            response.reset();
            response.setCharacterEncoding("GBK");
            response.setContentType("application/msexcel;charset=UTF-8");
            OutputStream outStream = response.getOutputStream();
            String bookName = "二维码序列查询日志";
            String[] headers = new String[] {"二维码序号", "码源订单编号", "码源订单名称", "码源订单客户编号","赋码厂", "赋码厂编码", "产线名称", "班组","设备编号", "SKU主键", "SKU名称", "批次编码", "原因", "查询人手机号", "查询时间"};
            String[] valueTags = new String[] {"batchSerialNo", "orderNo", "orderName", "clientOrderNo", "qrcodeManufacture", "qrcodeFactoryName", "qrcodeProductLineName",  "qrcodeWorkGroup", "qrcodeMachineId", "skuKey","skuName", "batchDesc","queryNotes", "queryUserPhoneNum", "createTime"};

            response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode(bookName, "UTF-8") + DateUtil.getDate() + ".xls");
            ExcelUtil<VpsVcodeBatchSerialNoQueryLog> excel = new ExcelUtil<VpsVcodeBatchSerialNoQueryLog>();
            excel.writeExcel(bookName, headers, valueTags, resultList, DateUtil.DEFAULT_DATETIME_FORMAT, outStream);
            outStream.close();
            response.flushBuffer();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
