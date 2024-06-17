package com.dbt.vpointsshop.action;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.action.reply.BaseResult;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ReflectUtil;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.VpointsBrandInfo;
import com.dbt.vpointsshop.bean.VpointsCategoryType;
import com.dbt.vpointsshop.bean.VpointsExchangeLog;
import com.dbt.vpointsshop.bean.VpointsGoodsHalfPriceActivityCogEntity;
import com.dbt.vpointsshop.dto.*;
import com.dbt.vpointsshop.service.ExchangeService;
import com.dbt.vpointsshop.service.IVpointsGoodsHalfPriceActivityCogService;
import com.dbt.vpointsshop.service.VpointsGoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
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
@RequestMapping("/halfPriceActivity")
public class VpointsGoodsHalfPriceActivityCogAction extends BaseAction {

    @Autowired
    private IVpointsGoodsHalfPriceActivityCogService goodsHalfPriceActivityCogService;

    @Autowired
    private VpointsGoodsService vpointsGoodsService;
    @Autowired
    private ExchangeService exchangeService;
    @Autowired
    private VpointsGoodsService goodService;
    /**
     * 活动列表
     *
     * @param session
     * @param queryParam
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/showHalfPriceActivityList")
    public String showHalfPriceActivityList(HttpSession session, String queryParam, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpointsGoodsHalfPriceActivityCogEntity entity = new VpointsGoodsHalfPriceActivityCogEntity(queryParam);
            IPage<VpointsGoodsHalfPriceActivityCogVO> page1 = goodsHalfPriceActivityCogService.selectPageVO(pageInfo.initPage(), entity);

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
        return "vpointsGoods/halfPrice/showHalfPriceActivityList";
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
    @RequestMapping("/showHalfPriceDataList")
        public String showHalfPriceDataList(HttpSession session,VpointsGoodsHalfPriceActivityCogVO activityCogVO, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpointsExchangeLog queryBean = new VpointsExchangeLog();
            queryBean.setGoodsName(activityCogVO.getGoodsName());
            queryBean.setExchangeId(activityCogVO.getExchangeId());
            queryBean.setHalfPriceActivityInfoKey(activityCogVO.getInfoKey());
            queryBean.setExchangeStartDate(activityCogVO.getStartDate());
            queryBean.setExchangeEndDate(activityCogVO.getEndDate());
//            queryBean.setPayType(activityCogVO.getStartRealPay());
            queryBean.setPlatformNickName(currentUser.getNickName());
            queryBean.setStartExchangePay(StringUtils.isEmpty(activityCogVO.getStartRealPay()) ? 0 : new BigDecimal(activityCogVO.getStartRealPay()).multiply(new BigDecimal("100")).longValue());
            queryBean.setEndExchangePay(StringUtils.isEmpty(activityCogVO.getEndRealPay()) ? 0 : new BigDecimal(activityCogVO.getEndRealPay()).multiply(new BigDecimal("100")).longValue());
            // 当前用户可查看品牌信息
            List<VpointsBrandInfo> brandLst = goodService.queryBrandByParentId("0", currentUser.getUserName());
            List<String> brandIdLst = ReflectUtil.getFieldsValueByName("brandId", brandLst);
            queryBean.setBrandIdLst(brandIdLst);
            // 订单列表 (商城订单除待发货以外默认不加载数据)
            int countResult = exchangeService.queryForExpressCount(queryBean);
            List<VpointsExchangeLog> resultList = new ArrayList<>();
            if(countResult>0){
                resultList = exchangeService.queryForExpressLst(queryBean, pageInfo);
            }
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultList);
            model.addAttribute("showCount",countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("activityCogVO", activityCogVO);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vpointsGoods/halfPrice/showHalfPriceDataList";
    }


    /**
     * 活动列表新增页面
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/showHalfPriceActivityAdd")
    public String showHalfPriceActivityAdd(HttpSession session, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            List<VpointsCategoryType> firstCategoryList = vpointsGoodsService.getFirstCategoryList();
            model.addAttribute("firstCategoryList", firstCategoryList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vpointsGoods/halfPrice/showHalfPriceActivityAdd";
    }

    /**
     * 活动列表修改页面
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/showHalfPriceActivityEdit")
    public String showHalfPriceActivityEdit(HttpSession session, String infoKey, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            List<VpointsCategoryType> firstCategoryList = vpointsGoodsService.getFirstCategoryList();
            VpointsGoodsHalfPriceActivityCogVO activityCogVO = goodsHalfPriceActivityCogService.findByInfoKey(infoKey);
            model.addAttribute("activityCog", activityCogVO);
            model.addAttribute("activityJSON", JSONObject.toJSON(activityCogVO));
            model.addAttribute("firstCategoryList", firstCategoryList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vpointsGoods/halfPrice/showHalfPriceActivityEdit";
    }
    /**
     * 活动列表新增
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/halfPriceActivitySave")
    public String showHalfPriceActivitySave(HttpSession session,VpointsGoodsHalfPriceActivityCogVO vo, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            vo.setCreateUser(currentUser.getUserKey());
            vo.setUpdateUser(currentUser.getUserKey());
            boolean boo = goodsHalfPriceActivityCogService.create(vo);
            if(boo){
                model.addAttribute("refresh", "add_success");
            }else{
                model.addAttribute("errMsg","add_fail");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       return "forward:showHalfPriceActivityList.do";
    }
    /**
     * 活动列表修改
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/halfPriceActivityUpdate")
    public String showHalfPriceActivityUpdate(HttpSession session,VpointsGoodsHalfPriceActivityCogVO vo, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            vo.setUpdateUser(currentUser.getUserKey());
            boolean boo = goodsHalfPriceActivityCogService.update(vo);
            if(boo){
                model.addAttribute("refresh", "add_success");
            }else{
                model.addAttribute("errMsg","add_fail");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "forward:showHalfPriceActivityList.do";
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
            VpointsGoodsHalfPriceActivityCogVO vo = new VpointsGoodsHalfPriceActivityCogVO();
            vo.setInfoKey(infoKey);
            vo.setStartDate(startDate);
            vo.setEndDate(endDate);
            vo.setGoodsIdList(Arrays.asList(goodsIdList.split(",")));
            baseResult = goodsHalfPriceActivityCogService.checkGoodsActivity(vo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return JSONObject.toJSONString(baseResult);
    }
    @RequestMapping("importHalfPriceActivityData")
    public void importHalfPriceActivityData(HttpSession session, HttpServletResponse
            response,VpointsGoodsHalfPriceActivityCogVO activityCogVO, String pageParam, Model model){
        OutputStream outStream = null;

        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            outStream = response.getOutputStream();
            response.reset();
            response.setCharacterEncoding("GBK");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            // 获取导出信息
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            pageInfo.setPagePerCount(9999999);
            VpointsExchangeLog queryBean = new VpointsExchangeLog();
            queryBean.setGoodsName(activityCogVO.getGoodsName());
            queryBean.setExchangeId(activityCogVO.getExchangeId());
            queryBean.setHalfPriceActivityInfoKey(activityCogVO.getInfoKey());
            queryBean.setExchangeStartDate(activityCogVO.getStartDate());
            queryBean.setExchangeEndDate(activityCogVO.getEndDate());
//            queryBean.setPayType(activityCogVO.getStartRealPay());
            queryBean.setStartExchangePay(StringUtils.isEmpty(activityCogVO.getStartRealPay()) ? 0 : new BigDecimal(activityCogVO.getStartRealPay()).multiply(new BigDecimal("100")).longValue());
            queryBean.setEndExchangePay(StringUtils.isEmpty(activityCogVO.getEndRealPay()) ? 0 : new BigDecimal(activityCogVO.getStartRealPay()).multiply(new BigDecimal("100")).longValue());

            // 当前用户可查看品牌信息
            List<VpointsBrandInfo> brandLst = goodService.queryBrandByParentId("0", currentUser.getUserName());
            List<String> brandIdLst = ReflectUtil.getFieldsValueByName("brandId", brandLst);
            queryBean.setBrandIdLst(brandIdLst);


            List<VpointsExchangeLog> resultList = exchangeService.queryForExpressLst(queryBean, pageInfo);
            List<VpointsHalfPriceActivityExport> addDataList = new ArrayList<>();
            resultList.forEach(exchangeLog->{
                VpointsHalfPriceActivityExport export = new VpointsHalfPriceActivityExport();
                String goodsPrice = divideHalfUpTwo(new BigDecimal(exchangeLog.getGoodsOriginalPrice()));
                BigDecimal multiply = new BigDecimal(String.valueOf(BigDecimal.valueOf(exchangeLog.getExchangeNum() - 1)))
                                        .multiply(new BigDecimal(exchangeLog.getGoodsOriginalPrice()));
                String secondGoodsPrice = divideHalfUpTwo(new BigDecimal(String.valueOf(exchangeLog.getExchangePay())).subtract(multiply));
                String exchangePay = divideHalfUpTwo(new BigDecimal(exchangeLog.getExchangePay()));
                export.setExchangeId(exchangeLog.getExchangeId());
                export.setGoodsName(exchangeLog.getGoodsName());
                export.setGoodsPrice(String.valueOf(goodsPrice));
                export.setSecondGoodsPrice(String.valueOf(secondGoodsPrice));
                export.setExchangeNum(String.valueOf(exchangeLog.getExchangeNum()));
                export.setOrderType(exchangeLog.getOrderType());
                export.setPayTypeName(exchangeLog.getPayTypeName());
                export.setExchangePay(String.valueOf(exchangePay));
                export.setExchangeTime(exchangeLog.getExchangeTime());
                export.setAddress(exchangeLog.getUserName()+"-"+exchangeLog.getPhoneNum()+"-"
                        +exchangeLog.getAddress());
                export.setExchangeStatus(exchangeLog.getOrderStatus());
                addDataList.add(export);
            });
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode("折扣订单"+ DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT_SHT), "UTF-8") + DateUtil.getDate() + ".xls");
            long exportStartTime = System.currentTimeMillis();
            EasyExcel
                    .write(response.getOutputStream())
                    .autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .head(VpointsHalfPriceActivityExport.class)
                    .sheet("折扣订单")
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
                        .sheet("折扣订单")
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

