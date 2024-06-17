package com.dbt.platform.waitActivation.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.CacheUtilNew.cacheKey;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.waitActivation.bean.VpsWaitActivationPrizeCog;
import com.dbt.platform.waitActivation.bean.VpsWaitActivationPrizeSku;
import com.dbt.platform.waitActivation.service.WaitActivationPrizeService;

/**
 * 待激活红包活动配置控制层
 */
@Controller
@RequestMapping("/waitActivationPrize")
public class WaitActivationPrizeAction extends BaseAction {

    @Autowired
    private WaitActivationPrizeService waitActivationPrizeService;


    /**
     * 展示待激活红包活动配置列表
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/showWaitActivationPrizeCogList")
    public String showWaitActivationPrizeCogList(HttpSession session, Model model, String pageParam, String queryParam, VpsWaitActivationPrizeCog queryBean) {
        SysUserBasis currentUser = this.getUserBasis(session);

        List<SkuInfo> skuList = waitActivationPrizeService.loadAllSku(currentUser.getCompanyKey());
        model.addAttribute("skuList", skuList);

        PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
        List<VpsWaitActivationPrizeCog> resultLst = waitActivationPrizeService.queryForList(queryBean, pageInfo);
        int countResult = waitActivationPrizeService.queryForCount(queryBean);
        
        if(CollectionUtils.isNotEmpty(resultLst)) {
        	String nowDate = DateUtil.getDate();
        	String key = RedisApiUtil.CacheKey.SQWA_MONEY_LIMIT + Constant.DBTSPLIT;
        	for (VpsWaitActivationPrizeCog item : resultLst) {
        		item.setTotalMoney(StringUtils.defaultIfBlank(RedisApiUtil.getInstance().get(key + item.getPrizeKey()), "0.00"));
        		item.setDayMoney(StringUtils.defaultIfBlank(RedisApiUtil.getInstance().get(key + item.getPrizeKey() + Constant.DBTSPLIT + nowDate), "0.00"));
			}
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("resultList", resultLst);
        model.addAttribute("showCount", countResult);
        model.addAttribute("startIndex", pageInfo.getStartCount());
        model.addAttribute("countPerPage", pageInfo.getPagePerCount());
        model.addAttribute("currentPage", pageInfo.getCurrentPage());
        model.addAttribute("orderCol", pageInfo.getOrderCol());
        model.addAttribute("orderType", pageInfo.getOrderType());
        model.addAttribute("queryBean", queryBean);
        model.addAttribute("queryParam", queryParam);
        model.addAttribute("pageParam", pageParam);
        return "/vcode/waitActivation/showWaitActivationPrizeCogList";
    }


    /**
     * 展示待激活红包活动配置新增界面
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/showWaitActivationPrizeCogAdd")
    public String showAnniversaryShareAdd(HttpSession session, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            model.addAttribute("currentUser", currentUser);

            List<SkuInfo> skuList = waitActivationPrizeService.loadAllSku(currentUser.getCompanyKey());
            model.addAttribute("skuList", skuList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/vcode/waitActivation/showWaitActivationPrizeCogAdd";
    }

    /**
     * 待激活红包活动配置新增
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/doWaitActivationPrizeCogAdd")
    public String doWaitActivationPrizeCogAdd(HttpSession session, VpsWaitActivationPrizeCog vpsWaitActivationPrizeCog, Model model) {
        String errMsg = "";
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            model.addAttribute("currentUser", currentUser);

            if (StringUtils.isBlank(vpsWaitActivationPrizeCog.getEveryoneLimitNum())) {
                vpsWaitActivationPrizeCog.setEveryoneLimitNum(null);
            }
            if (StringUtils.isBlank(vpsWaitActivationPrizeCog.getDayMoneyLimit())) {
                vpsWaitActivationPrizeCog.setDayMoneyLimit(null);
            }
            if (StringUtils.isBlank(vpsWaitActivationPrizeCog.getTotalMoneyLimit())) {
                vpsWaitActivationPrizeCog.setTotalMoneyLimit(null);
            }

            //并发操作key
            String key = currentUser.getUserKey()+"_waitActivationPrizeCog_concurrencyKey";
            if (RedisApiUtil.getInstance().get(key) == null){
                RedisApiUtil.getInstance().set(key,"1",3);
            }else{
                model.addAttribute("errMsg", "并发操作！");
                return showWaitActivationPrizeCogList(session, model, null, null, new VpsWaitActivationPrizeCog());
            }

            waitActivationPrizeService.insertWaitActivationPrizeCog(vpsWaitActivationPrizeCog, currentUser);
            errMsg = "保存成功";
        } catch (Exception ex) {
            errMsg = "保存失败";
            ex.printStackTrace();
        }
        model.addAttribute("errMsg", errMsg);
        return showWaitActivationPrizeCogList(session, model, null, null, new VpsWaitActivationPrizeCog());
    }


    /**
     * 展示待激活红包活动配置编辑界面
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/showWaitActivationPrizeCogEdit")
    public String showWaitActivationPrizeCogEdit(HttpSession session, String prizeKey, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            model.addAttribute("currentUser", currentUser);
            VpsWaitActivationPrizeCog vpsWaitActivationPrizeCog = waitActivationPrizeService.selectByKey(prizeKey);
            model.addAttribute("cog", vpsWaitActivationPrizeCog);
            List<VpsWaitActivationPrizeSku> prizeSkuList = waitActivationPrizeService.getSkuKeysByPrizeKey(prizeKey);
            model.addAttribute("prizeSkuList", prizeSkuList);

            List<SkuInfo> skuList = waitActivationPrizeService.loadAllSku(currentUser.getCompanyKey());
            model.addAttribute("skuList", skuList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/vcode/waitActivation/showWaitActivationPrizeCogEdit";
    }

    /**
     * 待激活红包活动配置编辑
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/doWaitActivationPrizeCogEdit")
    public String doWaitActivationPrizeCogEdit(HttpSession session, VpsWaitActivationPrizeCog vpsWaitActivationPrizeCog, Model model) {
        String errMsg = "";
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            model.addAttribute("currentUser", currentUser);

            //并发操作key
            String key = currentUser.getUserKey()+"_waitActivationPrizeCog_concurrencyKey";
            if (RedisApiUtil.getInstance().get(key) == null){
                RedisApiUtil.getInstance().set(key,"1",3);
            }else{
                model.addAttribute("errMsg", "并发操作！");
                return showWaitActivationPrizeCogList(session, model, null, null, new VpsWaitActivationPrizeCog());
            }

            if (StringUtils.isBlank(vpsWaitActivationPrizeCog.getEveryoneLimitNum())) {
                vpsWaitActivationPrizeCog.setEveryoneLimitNum(null);
            }
            if (StringUtils.isBlank(vpsWaitActivationPrizeCog.getDayMoneyLimit())) {
                vpsWaitActivationPrizeCog.setDayMoneyLimit(null);
            }
            if (StringUtils.isBlank(vpsWaitActivationPrizeCog.getTotalMoneyLimit())) {
                vpsWaitActivationPrizeCog.setTotalMoneyLimit(null);
            }


            waitActivationPrizeService.updateWaitActivationPrizeCog(vpsWaitActivationPrizeCog, currentUser);
            
            String prizeKey = vpsWaitActivationPrizeCog.getPrizeKey();
            // 清除缓存
            CacheUtilNew.removeByKey(cacheKey.vodeActivityKey
                    .KEY_WAIT_ACTIVATION_SKU_KEY + Constant.DBTSPLIT + prizeKey);
            CacheUtilNew.removeByKey(cacheKey.vodeActivityKey
            		.KEY_WAIT_ACTIVATION_SKU_NAME + Constant.DBTSPLIT + prizeKey);
            
            // 无效的待激活红包奖项key
	        String cacheInvalidPrizeKey = RedisApiUtil.CacheKey.SQWA_INVALID_PRIZE_KEY;
            if(RedisApiUtil.getInstance().containsInSet(cacheInvalidPrizeKey, prizeKey)) {
            	RedisApiUtil.getInstance().removeSetValue(cacheInvalidPrizeKey, prizeKey);
            }
            
            errMsg = "编辑成功";
        } catch (Exception ex) {
            errMsg = "编辑失败";
            ex.printStackTrace();
        }
        model.addAttribute("errMsg", errMsg);
        return showWaitActivationPrizeCogList(session, model, null, null, new VpsWaitActivationPrizeCog());
    }


}
