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
import com.dbt.framework.util.ReflectUtil;
import com.dbt.framework.util.StringUtil;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.VpointsBrandInfo;
import com.dbt.vpointsshop.bean.VpointsCategoryType;
import com.dbt.vpointsshop.bean.VpointsExchangeLog;
import com.dbt.vpointsshop.bean.VpointsGoodsGiftCardCog;
import com.dbt.vpointsshop.dto.VpointsGiftCardActivityExport;
import com.dbt.vpointsshop.dto.VpointsGoodsGiftCardCogVO;
import com.dbt.vpointsshop.dto.VpointsGoodsHalfPriceActivityCogVO;
import com.dbt.vpointsshop.dto.VpointsHalfPriceActivityExport;
import com.dbt.vpointsshop.service.ExchangeService;
import com.dbt.vpointsshop.service.IVpointsGoodsGiftCardCogService;
import com.dbt.vpointsshop.service.VpointsGoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.*;

/**
 * <p>
 * 礼品卡活动表 前端控制器
 * </p>
 *
 * @author wangshuda
 * @since 2022-08-15
 */
@Controller
@RequestMapping("/giftCardAction")
public class VpointsGoodsGiftCardCogAction extends BaseAction {

    @Autowired
    private IVpointsGoodsGiftCardCogService iVpointsGoodsGiftCardCogService;
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
    @RequestMapping("/showGoodsGiftCardActivityList")
    public String showGoodsGiftCardActivityList(HttpSession session, String queryParam, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpointsGoodsGiftCardCog entity = new VpointsGoodsGiftCardCog(queryParam);
            IPage<VpointsGoodsGiftCardCogVO> pageVO = iVpointsGoodsGiftCardCogService.selectPageVO(pageInfo.initPage(), entity);

            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", pageVO.getRecords());
            model.addAttribute("showCount", pageVO.getTotal());
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
        return "vpointsGoods/giftCard/showGiftCardActivityList";
    }

    /**
     * 活动数据
     *
     * @param session
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/showGoodsGiftCardDataList")
    public String showGoodsGiftCardDataList(HttpSession session,VpointsGoodsGiftCardCogVO cogVO, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpointsExchangeLog queryBean = new VpointsExchangeLog();
            queryBean.setGoodsName(StringUtils.isEmpty(cogVO.getGoodsName())?null:cogVO.getGoodsName().trim());
            queryBean.setExchangeId(StringUtils.isEmpty(cogVO.getExchangeId())?null:cogVO.getExchangeId().trim());
            queryBean.setGiftCardInfoKey(cogVO.getInfoKey());
            queryBean.setExchangeStartDate(cogVO.getStartDate());
            queryBean.setExchangeEndDate(cogVO.getEndDate());
            queryBean.setGiftCardInfoKey(cogVO.getInfoKey());
            queryBean.setStartExchangePay(StringUtils.isEmpty(cogVO.getStartRealPay()) ? 0 : new BigDecimal(cogVO.getStartRealPay()).multiply(new BigDecimal("100")).longValue());
            queryBean.setEndExchangePay(StringUtils.isEmpty(cogVO.getEndRealPay()) ? 0 : new BigDecimal(cogVO.getEndRealPay()).multiply(new BigDecimal("100")).longValue());
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
            model.addAttribute("activityCogVO", cogVO);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vpointsGoods/giftCard/showGiftCardDataList";
    }


    /**
     * 活动列表新增页面
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/showGiftCardActivityAdd")
    public String showGoodsGiftCardActivityAdd(HttpSession session, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            List<VpointsCategoryType> firstCategoryList = vpointsGoodsService.getFirstCategoryList();
            model.addAttribute("firstCategoryList", firstCategoryList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vpointsGoods/giftCard/showGiftCardActivityAdd";
    }

    /**
     * 活动列表修改页面
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/showGiftCardActivityEdit")
    public String showGoodsGiftCardActivityEdit(HttpSession session, String infoKey, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            List<VpointsCategoryType> firstCategoryList = vpointsGoodsService.getFirstCategoryList();
            VpointsGoodsGiftCardCogVO giftCardCog = iVpointsGoodsGiftCardCogService.findByInfoKey(infoKey);
            model.addAttribute("firstCategoryList", firstCategoryList);
            model.addAttribute("activityCog", giftCardCog);
            model.addAttribute("activityJSON", JSONObject.toJSON(giftCardCog));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vpointsGoods/giftCard/showGiftCardActivityEdit";
    }

    /**
     * 活动列表新增
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/giftCardActivitySave")
    public String showGoodsGiftCardActivitySave(HttpSession session, VpointsGoodsGiftCardCogVO vo, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            vo.setCreateUser(currentUser.getUserKey());
            vo.setUpdateUser(currentUser.getUserKey());
            boolean boo = iVpointsGoodsGiftCardCogService.create(vo);
            if (boo) {
                model.addAttribute("refresh", "add_success");
            } else {
                model.addAttribute("errMsg", "add_fail");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "forward:showGoodsGiftCardActivityList.do";
    }

    /**
     * 活动列表修改
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/giftCardActivityUpdate")
    public String showGoodsGiftCardActivityUpdate(HttpSession session, VpointsGoodsGiftCardCogVO vo, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            vo.setUpdateUser(currentUser.getUserKey());
            boolean boo = iVpointsGoodsGiftCardCogService.update(vo);
            if (boo) {
                model.addAttribute("refresh", "add_success");
            } else {
                model.addAttribute("errMsg", "add_fail");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "forward:showGoodsGiftCardActivityList.do";
    }

    /**
     * 检查商品是否包含其他礼品卡活动
     *
     * @return
     */
    @RequestMapping("/checkGoodsActivity")
    @ResponseBody
    public String checkGoodsActivity(String infoKey, String startDate, String endDate, String goodsIdList) {
        BaseResult<Map<String, Object>> baseResult = new BaseResult<Map<String, Object>>();
        try {
            VpointsGoodsGiftCardCogVO vo = new VpointsGoodsGiftCardCogVO();
            vo.setInfoKey(infoKey);
            vo.setStartDate(startDate);
            vo.setEndDate(endDate);
            vo.setGoodsIdList(Arrays.asList(goodsIdList.split(",")));
            baseResult = iVpointsGoodsGiftCardCogService.checkGoodsActivity(vo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return JSONObject.toJSONString(baseResult);
    }
    @RequestMapping("importGiftCardActivityData")
    public void importGiftCardActivityData(HttpSession session, HttpServletResponse
            response, VpointsGoodsGiftCardCogVO cogVO, String pageParam, Model model){
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
            queryBean.setGoodsName(StringUtils.isEmpty(cogVO.getGoodsName())?null:cogVO.getGoodsName().trim());
            queryBean.setExchangeId(StringUtils.isEmpty(cogVO.getExchangeId())?null:cogVO.getExchangeId().trim());
            queryBean.setGiftCardInfoKey(cogVO.getInfoKey());
            queryBean.setExchangeStartDate(cogVO.getStartDate());
            queryBean.setExchangeEndDate(cogVO.getEndDate());
            queryBean.setGiftCardInfoKey(cogVO.getInfoKey());
            queryBean.setPayType(cogVO.getStartRealPay());
            queryBean.setPlatformNickName(currentUser.getNickName());
            queryBean.setStartExchangePay(StringUtils.isEmpty(cogVO.getStartRealPay()) ? 0 : new BigDecimal(cogVO.getStartRealPay()).multiply(new BigDecimal("100")).longValue());
            queryBean.setEndExchangePay(StringUtils.isEmpty(cogVO.getEndRealPay()) ? 0 : new BigDecimal(cogVO.getEndRealPay()).multiply(new BigDecimal("100")).longValue());

            // 当前用户可查看品牌信息
            List<VpointsBrandInfo> brandLst = goodService.queryBrandByParentId("0", currentUser.getUserName());
            List<String> brandIdLst = ReflectUtil.getFieldsValueByName("brandId", brandLst);
            queryBean.setBrandIdLst(brandIdLst);

            List<VpointsExchangeLog> resultList = exchangeService.queryForExpressLst(queryBean, pageInfo);
            List<VpointsGiftCardActivityExport> addDataList = new ArrayList<>();
            resultList.forEach(exchangeLog->{
                VpointsGiftCardActivityExport export = new VpointsGiftCardActivityExport();
                String exchangePay = divideHalfUpTwo(new BigDecimal(exchangeLog.getExchangePay()));
                export.setExchangeId(exchangeLog.getExchangeId());
                export.setGoodsName(exchangeLog.getGoodsName());
                export.setGiftCardName(exchangeLog.getGiftCardName());
                export.setGiftCardType(exchangeLog.getGiftCardType());
                export.setPayTypeName(exchangeLog.getPayTypeName());
                export.setExchangePay(String.valueOf(exchangePay));
                export.setExchangeTime(exchangeLog.getExchangeTime());
                export.setAddress(exchangeLog.getUserName()+"-"+exchangeLog.getPhoneNum()+"-"
                        +exchangeLog.getAddress());
                export.setExchangeStatus(exchangeLog.getOrderStatus());
                addDataList.add(export);
            });
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode("礼品卡订单"+ DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT_SHT), "UTF-8") + DateUtil.getDate() + ".xls");
            long exportStartTime = System.currentTimeMillis();
            EasyExcel
                    .write(response.getOutputStream())
                    .autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .head(VpointsGiftCardActivityExport.class)
                    .sheet("礼品卡订单")
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
                        .sheet("礼品卡订单")
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

