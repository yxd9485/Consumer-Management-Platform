package com.dbt.vpointsshop.action;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.action.reply.BaseResult;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.StringUtil;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.VpointsCategoryType;
import com.dbt.vpointsshop.bean.VpointsExchangeLog;
import com.dbt.vpointsshop.bean.VpointsGoodsExchangeActivityCogEntity;
import com.dbt.vpointsshop.bean.VpointsGoodsExchangeActivityRecordEntity;
import com.dbt.vpointsshop.dto.VpointsExchangeActivityExport;
import com.dbt.vpointsshop.dto.VpointsGoodsExchangeActivityCogVO;
import com.dbt.vpointsshop.dto.VpointsGoodsExchangeActivityRecordVO;
import com.dbt.vpointsshop.service.IVpointsGoodsExchangeActivityCogService;
import com.dbt.vpointsshop.service.VpointsGoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城第二件半价活动 前端控制器
 * </p>
 *
 * @author wangshuda
 * @since 2022-07-11
 */
@Controller
@RequestMapping("/exchangeActivity")
public class VpointsGoodsExchangeActivityCogAction extends BaseAction {

    @Autowired
    private IVpointsGoodsExchangeActivityCogService goodsExchangeActivityCogService;

    @Autowired
    private VpointsGoodsService vpointsGoodsService;

    /**
     * 活动列表
     *
     * @param session
     * @param queryParam
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/showExchangeActivityList")
    public String showExchangeActivityList(HttpSession session, String queryParam, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpointsGoodsExchangeActivityCogEntity entity = new VpointsGoodsExchangeActivityCogEntity(queryParam);
            IPage<VpointsGoodsExchangeActivityCogVO> page1 = goodsExchangeActivityCogService.selectPageVO(pageInfo.initPage(), entity);

            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", page1.getRecords());
            model.addAttribute("showCount", page1.getTotal());
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vpointsGoods/exchange/showExchangeActivityList";
    }
    /**
     * 活动数据
     *
     * @param session
     * @param queryParam
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/showExchangeDataList")
    public String showExchangeDataList(HttpSession session, String queryParam, String pageParam,String infoKey, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpointsGoodsExchangeActivityRecordVO vo = new VpointsGoodsExchangeActivityRecordVO(queryParam);
            if (StringUtils.isEmpty(vo.getInfoKey()) && StringUtils.isNotEmpty(infoKey)) {
                vo.setInfoKey(infoKey);
            }
            vo.setExchangeStartTime(StringUtils.isNotEmpty(vo.getExchangeStartTime()) ? DateUtil.getStartDateTimeByDay(vo.getExchangeStartTime()) : null);
            vo.setExchangeEndTime(StringUtils.isNotEmpty(vo.getExchangeEndTime()) ? DateUtil.getEndDateTimeByDay(vo.getExchangeEndTime()) : null);
            IPage<VpointsGoodsExchangeActivityRecordVO> page1 =  goodsExchangeActivityCogService.selectDataPageVO(pageInfo.initPage(), vo);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", page1.getRecords());
            model.addAttribute("showCount", page1.getTotal());
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("infoKey", infoKey);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vpointsGoods/exchange/showExchangeDataList";
    }


    /**
     * 活动列表新增页面
     *
     * @param session
     * @param queryParam
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/showExchangeActivityAdd")
    public String showExchangeActivityAdd(HttpSession session, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            List<VpointsCategoryType> firstCategoryList = vpointsGoodsService.getFirstCategoryList();
            model.addAttribute("firstCategoryList", firstCategoryList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vpointsGoods/exchange/showExchangeActivityAdd";
    }

    /**
     * 活动列表修改页面
     *
     * @param session
     * @param queryParam
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/showExchangeActivityEdit")
    public String showExchangeActivityEdit(HttpSession session, String infoKey, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            List<VpointsCategoryType> firstCategoryList = vpointsGoodsService.getFirstCategoryList();
            VpointsGoodsExchangeActivityCogVO activityCogVO = goodsExchangeActivityCogService.findByInfoKey(infoKey);
            model.addAttribute("activityCog", activityCogVO);
            model.addAttribute("activityJSON", JSONObject.toJSON(activityCogVO));
            model.addAttribute("firstCategoryList", firstCategoryList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vpointsGoods/exchange/showExchangeActivityEdit";
    }
    /**
     * 活动列表新增
     *
     * @param session
     * @param queryParam
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/exchangeActivitySave")
    public String showExchangeActivitySave(HttpSession session,VpointsGoodsExchangeActivityCogVO vo, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            vo.setCreateUser(currentUser.getUserKey());
            vo.setUpdateUser(currentUser.getUserKey());
            boolean boo = goodsExchangeActivityCogService.create(vo);
            if(boo){
                model.addAttribute("errMsg", "成功");
            }else{
                model.addAttribute("errMsg","失败");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       return "forward:showExchangeActivityList.do";
    }
    /**
     * 活动列表修改
     *
     * @param session
     * @param queryParam
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/exchangeActivityUpdate")
    public String showExchangeActivityUpdate(HttpSession session,VpointsGoodsExchangeActivityCogVO vo, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            vo.setUpdateUser(currentUser.getUserKey());
            boolean boo = goodsExchangeActivityCogService.update(vo);
            if(boo){
                model.addAttribute("errMsg", "成功");
            }else{
                model.addAttribute("errMsg","失败");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "forward:showExchangeActivityList.do";
    }

    /**
     * 活动列表修改
     *
     * @return
     */
    @RequestMapping("/checkGoodsActivity")
    @ResponseBody
    public String checkGoodsActivity(String infoKey,String startDate,String endDate,String goodsIdList) {
            BaseResult<Map<String, Object>> baseResult = new BaseResult<Map<String, Object>>();
        try {
            VpointsGoodsExchangeActivityCogVO vo = new VpointsGoodsExchangeActivityCogVO();
            vo.setInfoKey(infoKey);
            vo.setStartDate(startDate);
            vo.setEndDate(endDate);
            vo.setGoodsIdList(Arrays.asList(goodsIdList.split(",")));
            baseResult = goodsExchangeActivityCogService.checkGoodsActivity(vo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return JSONObject.toJSONString(baseResult);
    }
    @RequestMapping("importExchangeActivityData")
    public void importExchangeActivityData(HttpSession session, HttpServletResponse
            response, String queryParam, String pageParam,String infoKey, Model model){
        OutputStream  outStream = null;

        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpointsGoodsExchangeActivityRecordVO vo = new VpointsGoodsExchangeActivityRecordVO(queryParam);
            if (StringUtils.isEmpty(vo.getInfoKey()) && StringUtils.isNotEmpty(infoKey)) {
                vo.setInfoKey(infoKey);
            }
            outStream = response.getOutputStream();
            response.reset();
            response.setCharacterEncoding("GBK");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            // 获取导出信息
            pageInfo.setPagePerCount(0);
            vo.setExchangeStartTime(StringUtils.isNotEmpty(vo.getExchangeStartTime()) ? DateUtil.getStartDateTimeByDay(vo.getExchangeStartTime()) : null);
            vo.setExchangeEndTime(StringUtils.isNotEmpty(vo.getExchangeEndTime()) ? DateUtil.getEndDateTimeByDay(vo.getExchangeEndTime()) : null);
            List<VpointsGoodsExchangeActivityRecordVO> recordVOList =  goodsExchangeActivityCogService.selectDataPageVO(vo);
            List<VpointsExchangeActivityExport> addDataList = new ArrayList<>();
            recordVOList.forEach(recordVO->{

                VpointsExchangeActivityExport export = new VpointsExchangeActivityExport();
                export.setExchangeId(recordVO.getExchangeId());
                export.setGoodsName(recordVO.getGoodsName());
                String goodsPrice = divideHalfUpTwo(new BigDecimal(String.valueOf(recordVO.getGoodsPrice())));
                export.setGoodsPrice(goodsPrice);
                export.setExchangeGoodsName(recordVO.getExchangeGoodsName());
                String exchangeGoodsPrice = divideHalfUpTwo(new BigDecimal(String.valueOf(recordVO.getExchangeGoodsPrice())));
                export.setExchangeGoodsPrice(exchangeGoodsPrice);
                export.setOrderType(recordVO.getExchangeLog().getOrderType());
                export.setPayTypeName(recordVO.getExchangeLog().getPayTypeName());
                String exchangeLogPay = String.valueOf(recordVO.getExchangeLog().getExchangePay());
                String exchangeOrderPay = String.valueOf(recordVO.getExchangeOrderLog().getExchangePay());
                BigDecimal add = new BigDecimal(exchangeLogPay).add(new BigDecimal(exchangeOrderPay));
                String exchangePay = divideHalfUpTwo(add);
                export.setExchangePay(exchangePay);
                export.setExchangeOrderId(recordVO.getExchangeOrderLog().getExchangeId());
                export.setExchangeNum(recordVO.getExchangeLog().getExchangeNum());
                export.setExchangeTime(recordVO.getExchangeLog().getExchangeTime());
                export.setAddress(recordVO.getExchangeLog().getUserName()+"-"
                        +recordVO.getExchangeLog().getPhoneNum()+"-"+recordVO.getExchangeLog().getAddress());
                export.setExchangeStatus("主商品--"+recordVO.getExchangeLog().getOrderStatus() +"\n"+
                        "  换购商品--"+recordVO.getExchangeOrderLog().getOrderStatus());
                addDataList.add(export);
            });
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode("换购订单"+DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT_SHT), "UTF-8") + DateUtil.getDate() + ".xls");
            long exportStartTime = System.currentTimeMillis();
            EasyExcel
                    .write(response.getOutputStream())
                    .autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .head(VpointsExchangeActivityExport.class)
                    .sheet("换购订单")
                    .doWrite(addDataList);
            long exportEndTime = System.currentTimeMillis();

        } catch (Exception e) {
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + "下载失败" + DateUtil.getDate() + ".xls");
            try {
                EasyExcel
                        .write(response.getOutputStream())
                        .autoCloseStream(Boolean.TRUE)
                        .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                        .sheet("换购订单")
                        .doWrite(new ArrayList<>());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                    response.flushBuffer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static String divideHalfUpTwo(BigDecimal num){
        BigDecimal hundred = new BigDecimal("100");
        return num.divide(hundred, 2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }


}

