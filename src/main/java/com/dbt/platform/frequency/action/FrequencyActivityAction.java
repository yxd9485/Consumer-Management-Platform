package com.dbt.platform.frequency.action;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.activity.bean.VcodeActivityPerhundredCog;
import com.dbt.platform.activity.service.VcodeActivityService;
import com.dbt.platform.frequency.bean.VpsVcodeActivityFrequencyCog;
import com.dbt.platform.frequency.service.FrequencyActivityService;
import com.dbt.platform.system.bean.SysUserBasis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 频次活动action
 */
@Controller
@RequestMapping("/frequency")
public class FrequencyActivityAction extends BaseAction {
    @Autowired
    private FrequencyActivityService frequencyActivityService;
    @Autowired
    private VcodeActivityService activityService;
    /**
     * 获取列表
     */
    @RequestMapping("/showFrequencyCogList")
    public String showFrequencyCogList(HttpSession session, String queryParam, String pageParam, Model model) {
        try {
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsVcodeActivityFrequencyCog queryBean = new VpsVcodeActivityFrequencyCog(queryParam);
            SysUserBasis currentUser = this.getUserBasis(session);
            queryBean.setCompanyKey(currentUser.getCompanyKey());

            List<VpsVcodeActivityFrequencyCog> resultList = frequencyActivityService.queryForList(queryBean, pageInfo);
            int countResult = frequencyActivityService.queryForCount(queryBean);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultList);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryBean", queryBean);
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/frequency/showFrequencyCogList";
    }

    /**
     * 跳转新增页面
     */
    @RequestMapping("/showFrequencyCogAdd")
    public String showPerhundredAdd(HttpSession session, Model model){
        // 查询没有关联频次配置或已关联并过期的活动
        model.addAttribute("activityList", activityService.queryListForNotFrequencyCog(null));
        return "vcode/frequency/showFrequencyCogAdd";
    }

    /**
     * 跳转编辑页面
     */
    @RequestMapping("/showFrequencyCogEdit")
    public String showFrequencyCogEdit(HttpSession session, String infoKey, Model model){
        VpsVcodeActivityFrequencyCog frequencyCog = frequencyActivityService.findById(infoKey);
        model.addAttribute("activityList", activityService.queryListForNotFrequencyCog(infoKey));
        model.addAttribute("frequencyCog", frequencyCog);
        return "vcode/frequency/showFrequencyCogEdit";
    }

    /**
     * 新增频次规则
     */
    @RequestMapping("/addFrequencyCog")
    public String addFrequencyCog(HttpSession session, VpsVcodeActivityFrequencyCog frequencyCog, Model model){
        try{
            SysUserBasis currentUser = this.getUserBasis(session);
            frequencyCog.setCompanyKey(currentUser.getCompanyKey());
            frequencyCog.setDeleteFlag("0");
            frequencyCog.setCreateUser(currentUser.getUserName());
            frequencyCog.setCreateTime(DateUtil.getDateTime());
            frequencyCog.setUpdateUser(currentUser.getUserName());
            frequencyCog.setUpdateTime(DateUtil.getDateTime());
            frequencyActivityService.addFrequencyCog(frequencyCog);
            model.addAttribute("errMsg", "添加成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "添加失败");
            ex.printStackTrace();
        }
        return "forward:showFrequencyCogList.do";
    }

    /**
     * 修改频次规则
     */
    @RequestMapping("/editFrequencyCog")
    public String editFrequencyCog(HttpSession session, VpsVcodeActivityFrequencyCog frequencyCog, Model model){
        try{
            SysUserBasis currentUser = this.getUserBasis(session);
            frequencyCog.setCompanyKey(currentUser.getCompanyKey());
            frequencyCog.setDeleteFlag("0");
            frequencyCog.setUpdateUser(currentUser.getUserName());
            frequencyCog.setUpdateTime(DateUtil.getDateTime());
            frequencyActivityService.editFrequencyCog(frequencyCog);

            model.addAttribute("errMsg", "修改成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "修改失败");
            ex.printStackTrace();
        }
        return "forward:showFrequencyCogList.do";
    }

    /**
     * 刪除频次规则
     * @param session
     * @param infoKey
     * @param model
     * @return
     */
    @RequestMapping("/deleteFrequencyCog")
    public String deleteFrequencyCog(HttpSession session, String infoKey,
                                        String queryParam, String pageParam, Model model){
        try{
            SysUserBasis currentUser = this.getUserBasis(session);
           frequencyActivityService.deletePerhundredCog(infoKey, currentUser.getUserKey());
            model.addAttribute("errMsg", "删除成功");
        }catch(Exception e){
            model.addAttribute("errMsg", "删除失败");

        }
        return showFrequencyCogList(session, queryParam, pageParam, model);
    }
    
    /**
     * 检验名称是否重复
     * @param infoKey		主键（修改页面需传递）
     * @param bussionName	名称
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkBussionName")
    public String checkBussionName(String infoKey, String bussionName){
        return frequencyActivityService.checkBussionName(infoKey, bussionName);
    }

    /**
     * 校验扫码活动指定时间内是否参与有效的频次活动
     * @param infoKey 当前频次活动主键
     * @param vcodeActivityKeys  扫码活动主键集体
     * @param beginDate 活动开始日期
     * @param endDate   活动结束日期
     * @return
     */
    @ResponseBody
    @RequestMapping("/validCogByActivityKey")
    public String validCogByActivityKey(String infoKey, String vcodeActivityKeys, String beginDate, String endDate){
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", frequencyActivityService.validCogByActivityKey(infoKey, vcodeActivityKeys, beginDate, endDate));
        return JSON.toJSONString(map);
    }

}
