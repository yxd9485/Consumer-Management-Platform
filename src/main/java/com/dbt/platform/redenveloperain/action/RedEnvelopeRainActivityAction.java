package com.dbt.platform.redenveloperain.action;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtil;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.bean.VcodeActivityHotAreaCog;
import com.dbt.platform.activity.bean.VcodeActivityRebateRuleCog;
import com.dbt.platform.activity.dao.IVcodeActivityRebateRuleCogDao;
import com.dbt.platform.activity.service.VcodeActivityHotAreaCogService;
import com.dbt.platform.activity.service.VcodeActivityRebateRuleCogService;
import com.dbt.platform.activity.service.VcodeActivityService;
import com.dbt.platform.activity.service.VcodeActivityVpointsCogService;
import com.dbt.platform.redenveloperain.bean.RedEnvelopeRainActivityEntity;
import com.dbt.platform.redenveloperain.dto.RedEnvelopeRainActivityQuery;
import com.dbt.platform.redenveloperain.service.RedEnvelopeRainActivityService;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/redEnvelopeRain")
public class RedEnvelopeRainActivityAction extends BaseAction {
    @Autowired
    private VcodeActivityHotAreaCogService hotAreaCogService;
    @Autowired
    private VcodeActivityService vcodeActivityService;
    @Autowired
    private RedEnvelopeRainActivityService redEnvelopeRainActivityService;
    @Autowired
    private VcodeActivityRebateRuleCogService rebateRuleCogService;
    @Autowired
    private VcodeActivityVpointsCogService vpointsCogService;
    /**
     * 跳转新增页面
     * @return
     */
    @RequestMapping("/showRedEnvelopeAdd")
    public String showRedEnvelopeAdd(HttpSession session,Model model){
        model.addAttribute("hotAreaList", hotAreaCogService.findHotAreaListByAreaCode("000000"));
        model.addAttribute("projectServerName", DbContextHolder.getDBType());
        model.addAttribute("shareActivityKeyList", vcodeActivityService.findAllVcodeActivityList(Constant.activityType.activity_type0, null));
        return "vcode/redRnvelope/showRedEnvelopeAdd";
    }

    /**
     * 跳转修改页面
     * @return
     */
    @RequestMapping("/showRedEnvelopeEdit")
    public String showRedEnvelopeEdit(HttpSession session,String activityKey,String isBegin,Model model){
        model.addAttribute("hotAreaList", hotAreaCogService.findHotAreaListByAreaCode("000000"));
        RedEnvelopeRainActivityQuery byId = redEnvelopeRainActivityService.findById(activityKey);
        Map<String, Object> map = new HashMap<>();
        map.put("vcodeActivityKey", byId.getInfoKey());
        List<VcodeActivityRebateRuleCog> rebateRuleCogList = null;
        try {
            rebateRuleCogList = rebateRuleCogService.queryRebateRuleCogListByActivityKey(activityKey,null,"2",false,null);
            rebateRuleCogList.get(0).setVpointsCogLst(vpointsCogService.queryVpointsCogByrebateRuleKey(rebateRuleCogList.get(0).getRebateRuleKey()));
            String rebateRuleMoneyStr = CacheUtilNew.cacheKey.vodeActivityKey.KEY_RED_ENVELOPE_RESTRICT_MONEY + activityKey;
            if (byId.getRestrictTimeType() == 0) {
                byId.setUseRestrictMoney(Double.parseDouble(StringUtils.defaultIfBlank(RedisApiUtil.getInstance().getHSet((rebateRuleMoneyStr), "Total"), "0.00")));
            }else{
                byId.setUseRestrictMoney(Double.parseDouble(StringUtils.defaultIfBlank(RedisApiUtil.getInstance().getHSet(rebateRuleMoneyStr, DateUtil.getDate()), "0.00")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("projectServerName", DbContextHolder.getDBType());
        model.addAttribute("entity", byId);
        model.addAttribute("isBegin", isBegin);
        model.addAttribute("rebateRuleCogList", rebateRuleCogList.get(0));
        model.addAttribute("shareActivityKeyList", vcodeActivityService.findAllVcodeActivityList(Constant.activityType.activity_type0, null));
        return "vcode/redRnvelope/showRedEnvelopeEdit";
    }
    /**
     * 分页查询
     * @return
     */
    @RequestMapping("/showRedEnvelopeQueryList")
    public String showRedEnvelopeQueryList(HttpSession session, String queryParam , String pageParam,Model model){
        RedEnvelopeRainActivityQuery param = new RedEnvelopeRainActivityQuery(queryParam);
//        RedEnvelopeRainActivityQuery param = new RedEnvelopeRainActivityQuery();
        PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
        IPage page = pageInfo.initPage();
        try {
            page = redEnvelopeRainActivityService.queryPage(page,param);


        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        model.addAttribute("resultList", page.getRecords());
        model.addAttribute("startIndex", pageInfo.getStartCount());
        model.addAttribute("countPerPage", pageInfo.getPagePerCount());
        model.addAttribute("showCount", page.getTotal());
        model.addAttribute("currentPage", pageInfo.getCurrentPage());
        model.addAttribute("orderCol", pageInfo.getOrderCol());
        model.addAttribute("orderType", pageInfo.getOrderType());
        	model.addAttribute("pageParam", pageParam);

        model.addAttribute("queryParam", queryParam);
//        model.addAttribute("pageParam", pageParam);
        return "vcode/redRnvelope/showRedEnvelopeQueryList";
    }
    /**
     * 新增
     * @return
     */
    @RequestMapping("redEnvelopeSave")
    public String showRedEnvelopeSave(HttpSession session,RedEnvelopeRainActivityQuery query,Model model){
        SysUserBasis currentUser = this.getUserBasis(session);
        try {
            String key = StringUtils.isNotBlank(DbContextHolder.getDBType()) ? DbContextHolder.getDBType() : "dataSource";
            String cacheKeyStr = CacheUtilNew.cacheKey.vodeActivityKey.KEY_RED_ENVELOPE_ACTIVITY_COGS + Constant.DBTSPLIT + key;
            // 删除红包雨扫码活动缓存
            CacheUtilNew.removeGroupByKey(cacheKeyStr);
            Boolean boo = redEnvelopeRainActivityService.create(query,currentUser);
            if(boo){
                model.addAttribute("flag", "add_success");
            }else{
                model.addAttribute("flag", "add_fail");
            }
            model.addAttribute("msg", "");
        }catch (BusinessException businessException){
            businessException.printStackTrace();
            model.addAttribute("flag", "add_fail");
            model.addAttribute("msg", Constant.DBTSPLIT+businessException.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("flag", "add_fail");
        }
        return "forward:showRedEnvelopeQueryList.do";
    }

    /**
     * 修改
     * @return
     */
    @RequestMapping("redEnvelopeUpdate")
    public String showRedEnvelopeUpdate(HttpSession session,RedEnvelopeRainActivityQuery query,Model model){
        SysUserBasis currentUser = this.getUserBasis(session);
        try {
            String key = StringUtils.isNotBlank(DbContextHolder.getDBType()) ? DbContextHolder.getDBType() : "dataSource";
            String cacheKeyStr = CacheUtilNew.cacheKey.vodeActivityKey.KEY_RED_ENVELOPE_ACTIVITY_COGS + Constant.DBTSPLIT + key;
            // 删除红包雨扫码活动缓存
            CacheUtilNew.removeGroupByKey(cacheKeyStr);
            Map<String, Object> map = new HashMap<>();
            map.put("vcodeActivityKey", query.getInfoKey());
            List<VcodeActivityRebateRuleCog> rebateRuleCogList = null;
            try {
                rebateRuleCogList = rebateRuleCogService.queryRebateRuleCogListByActivityKey(query.getInfoKey(),null,"2",false,null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            query.setRebateRuleCogList(rebateRuleCogList);
            Boolean boo = redEnvelopeRainActivityService.updateById(query,currentUser);
            if(boo){
                model.addAttribute("flag", "add_success");
            }else{
                model.addAttribute("flag", "add_fail");
            }
            model.addAttribute("msg", "");
        }catch (BusinessException businessException){
            businessException.printStackTrace();
            model.addAttribute("flag", "add_fail");
            model.addAttribute("msg", Constant.DBTSPLIT+businessException.getErrCode());
        }catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("flag", "add_fail");
        }
        return "forward:showRedEnvelopeQueryList.do";
    }

    /**
     * 检验名字是否重复
     */
    @RequestMapping(value = "/checkDateTime")
    @ResponseBody
    public boolean checkName(HttpSession session,RedEnvelopeRainActivityQuery query, Model model) {
        SysUserBasis currentUser = this.getUserBasis(session);
        return redEnvelopeRainActivityService.checkDateTime(query);
    }
    /**
     * 删除
     * @return
     */
    @RequestMapping("redEnvelopeDelete")
    public String showRedEnvelopeDelete(HttpSession session,String activityKey){
        RedEnvelopeRainActivityEntity entity = new RedEnvelopeRainActivityEntity();
        try {
            String key = StringUtils.isNotBlank(DbContextHolder.getDBType()) ? DbContextHolder.getDBType() : "dataSource";
            String cacheKeyStr = CacheUtilNew.cacheKey.vodeActivityKey.KEY_RED_ENVELOPE_ACTIVITY_COGS + Constant.DBTSPLIT + key;
            // 删除红包雨扫码活动缓存
            CacheUtilNew.removeGroupByKey(cacheKeyStr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        entity.setInfoKey(activityKey);
        entity.setDeleteFlag("1");
        redEnvelopeRainActivityService.updateById(entity);
        return "forward:showRedEnvelopeQueryList.do";
    }
}
