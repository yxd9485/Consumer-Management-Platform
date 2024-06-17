package com.dbt.vpointsshop.action;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.action.reply.BaseResult;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.VpointsGoodsInfo;
import com.dbt.vpointsshop.bean.VpsCombinationDiscountCog;
import com.dbt.vpointsshop.dto.VpsCombinationDiscountCogVO;
import com.dbt.vpointsshop.dto.VpsCombinationDiscountDTO;
import com.dbt.vpointsshop.service.IVpsCombinationDiscountCogService;
import com.dbt.vpointsshop.service.VpointsGoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author shuDa
 * @date 2022/4/7
 **/
@Controller
@RequestMapping("/vpointsCombinationDiscount")
public class VpointsCombinationDiscountAction  extends BaseAction {

    @Autowired
    private IVpsCombinationDiscountCogService iVpsCombinationDiscountCogService;
    @Autowired
    private VpointsGoodsService goodsService;

    @RequestMapping("/showList")
    public String getList(Model model, String pageParam, String queryParam, HttpSession session){
        PageOrderInfo info = new PageOrderInfo(pageParam);
        SysUserBasis user=getUserBasis(session);
        VpsCombinationDiscountCogVO paramVO = new VpsCombinationDiscountCogVO(queryParam);
        paramVO.setPageOrderInfo(info);
        Page<VpsCombinationDiscountDTO> list = iVpsCombinationDiscountCogService.getList(paramVO);
        List<VpsCombinationDiscountDTO> records = list.getRecords();
        List<VpsCombinationDiscountCogVO> resultList = new ArrayList<>();
        records.forEach(record ->{
            VpsCombinationDiscountCogVO cogVO = new VpsCombinationDiscountCogVO();
            BeanUtils.copyProperties(record, cogVO);
            cogVO.setStartTime(DateUtil.getDateTime(record.getStartTime(), DateUtil.DEFAULT_DATE_FORMAT));
            cogVO.setEndTime(DateUtil.getDateTime(record.getEndTime(), DateUtil.DEFAULT_DATE_FORMAT));
            resultList.add(cogVO);
        });
        model.addAttribute("showCount", list.getTotal());
        model.addAttribute("resultList", resultList);
        model.addAttribute("startIndex", info.getStartCount());
        model.addAttribute("countPerPage", info.getPagePerCount());
        model.addAttribute("currentPage", info.getCurrentPage());
        model.addAttribute("queryParam", queryParam);
        model.addAttribute("orderCol", info.getOrderCol());
        model.addAttribute("orderType", info.getOrderType());
        model.addAttribute("pageParam", pageParam);
        return "vpointsGoods/combinationDiscount/showList";
    }
    @RequestMapping("/add")
    public String add(Model model, String pageParam, String queryParam, HttpSession session){
        PageOrderInfo info = new PageOrderInfo(pageParam);
        SysUserBasis user=getUserBasis(session);
        try {
            PageOrderInfo page = new PageOrderInfo("");
            List<VpointsGoodsInfo> allGoods = goodsService.getGoodsList(page,  getGoodsListParam(page));
            //以选的且 活动进行中的活动 商品去除
            List<VpsCombinationDiscountDTO> dtoList = iVpsCombinationDiscountCogService.selectSetUpGoodsByOnline();
            Iterator<VpointsGoodsInfo> iterator = allGoods.iterator();
            while (iterator.hasNext()) {
                VpointsGoodsInfo goodsInfo = iterator.next();
                for (VpsCombinationDiscountDTO vpsCombinationDiscountDTO : dtoList) {
                    if(goodsInfo.getGoodsId().equals(vpsCombinationDiscountDTO.getGoodsA())
                            || goodsInfo.getGoodsId().equals(vpsCombinationDiscountDTO.getGoodsB())
                            || goodsInfo.getGoodsId().equals(vpsCombinationDiscountDTO.getGoodsC())){
                        iterator.remove();
                        break;
                    }
                }
            }
            model.addAttribute("goodsList",allGoods);
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("goodsList", null);
        }
        return "vpointsGoods/combinationDiscount/add";
    }
    @RequestMapping("/edit")
    public String edit(Model model, String pageParam, String cogKey, HttpSession session){
        PageOrderInfo info = new PageOrderInfo(pageParam);
        SysUserBasis user=getUserBasis(session);
        VpsCombinationDiscountCogVO cog = iVpsCombinationDiscountCogService.selectById(cogKey);
        PageOrderInfo page = new PageOrderInfo("");
        List<VpointsGoodsInfo> allGoods = goodsService.getGoodsList(page,  getGoodsListParam(page));
        model.addAttribute("goodsList",allGoods);
        model.addAttribute("cog", cog);
        return "vpointsGoods/combinationDiscount/edit";
    }
    @RequestMapping("/save")
    public String save(Model model, VpsCombinationDiscountCogVO queryParam, HttpSession session){
        SysUserBasis user=getUserBasis(session);
        iVpsCombinationDiscountCogService.insert(queryParam,user);
        return "forward:showList.do";
    }
    @RequestMapping("/update")
    public String update(Model model, VpsCombinationDiscountCogVO queryParam, HttpSession session){
        SysUserBasis user=getUserBasis(session);
        iVpsCombinationDiscountCogService.update(queryParam,user);
        return "forward:showList.do";
    }

    @RequestMapping("/checkGoodsInfo")
    @ResponseBody
    public String checkGoodsInfo(Model model, VpsCombinationDiscountCogVO queryParam, HttpSession session){
        SysUserBasis user=getUserBasis(session);
        BaseResult<VpsCombinationDiscountDTO> baseResult = iVpsCombinationDiscountCogService.checkGoodsInfo(queryParam);
        return JSON.toJSONString(baseResult);
    }
    @RequestMapping("/delete")
    public String delete(Model model, String cogKey, HttpSession session){
        SysUserBasis user=getUserBasis(session);
        iVpsCombinationDiscountCogService.delete(cogKey);
        return "forward:showList.do";
    }

    private VpointsGoodsInfo getGoodsListParam(PageOrderInfo page) {

        page.setPagePerCount(99999);
        page.setStartCount(0);
        VpointsGoodsInfo vpointsGoodsInfo = new VpointsGoodsInfo();
        vpointsGoodsInfo.setExchangeChannel("hebei".equals(DbContextHolder.getDBType()) ? Constant.exchangeChannel.CHANNEL_6 : Constant.exchangeChannel.CHANNEL_1);
        vpointsGoodsInfo.setGoodsStartTime(DateUtil.getDateTime());
        vpointsGoodsInfo.setGoodsEndTime(DateUtil.getDateTime());
        vpointsGoodsInfo.setGoodsStatus("0");
        vpointsGoodsInfo.setGoodsRemains(0);
        vpointsGoodsInfo.setYouPinFlag("1");
        vpointsGoodsInfo.setIsGift("1");
        return vpointsGoodsInfo;
    }
}
