package com.dbt.platform.activity.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.dao.ICommonDao;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.HttpReq;
import com.dbt.platform.activity.bean.VcodePrizeBasicInfo;
import com.dbt.platform.activity.dao.IVpsVcodePrizeRecordDao;
import com.dbt.platform.activity.service.VcodeActivityBigPrizeService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * @Description: 实物奖Action
 * @Author bin.zhang
 **/
@Controller
@RequestMapping("/vcodeActivityBigPrize")
public class VcodeActivityBigPrizeAction extends BaseAction {
    @Autowired
    private VcodeActivityBigPrizeService vcodeActivityBigPrizeService;
    @Autowired
    private ICommonDao commonDao;
    @Autowired
    private IVpsVcodePrizeRecordDao vpsVcodePrizeRecordDao;

    @RequestMapping("/showBigPrizeList")
    public String showBigPrizeList(HttpSession session,
                                        String queryParam, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VcodePrizeBasicInfo queryBean = new VcodePrizeBasicInfo(queryParam);
            List<VcodePrizeBasicInfo> resultList = vcodeActivityBigPrizeService.queryBigPrize(queryBean, pageInfo);
            int countResult = vcodeActivityBigPrizeService.countVcodeActivityList(queryBean);

            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultList);
            model.addAttribute("showCount", countResult);
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
        return "vcode/bigprize/showBigPrizeList";
    }

    /**
     * V码活动 添加页面
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/showBigPrizeAdd")
    public String showBigPrizeAdd(HttpSession session, Model model) {
        model.addAttribute("showLxPrizeType", StringUtils.isNotBlank(DatadicUtil
                .getDataDicValue(DatadicKey.dataDicCategory.FILTER_LX, DatadicKey.filterLx.LX_APPKEY)) ? "1" : "0");

        return "vcode/bigprize/showBigPrizeAdd";
    }

    /**
     * V码活动 编辑页面
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/showBigPrizeEdit")
    public String showVcodeActivityEdit(HttpSession session, String infoKey, String childTab, Model model) {
        try {
            // 查询当前活动
            VcodePrizeBasicInfo vcodePrizeBasicInfo = vcodeActivityBigPrizeService.findBigPrizeByKey(infoKey);
            model.addAttribute("vcodePrizeBasicInfo", vcodePrizeBasicInfo);
            if (StringUtils.isNotBlank(vcodePrizeBasicInfo.getLxPrizeType())
                    || StringUtils.isNotBlank(DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_LX, DatadicKey.filterLx.LX_APPKEY))) {
                model.addAttribute("showLxPrizeType", "1");
            } else {
                model.addAttribute("showLxPrizeType", "0");
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return "vcode/bigprize/showBigPrizeEdit";
    }


    @RequestMapping("/doBigPrizeAdd")
    public String doBigPrizeAdd(HttpSession session, VcodePrizeBasicInfo vcodePrizeBasicInfo, Model model) {
        String errMsg = "";
        SysUserBasis currentUser = this.getUserBasis(session);
        try {
            vcodePrizeBasicInfo.setCreateTime(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            vcodePrizeBasicInfo.setUpdateTime(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            vcodePrizeBasicInfo.setCreateUser(currentUser.getUserName());
            String prizeNo = getPrizeNo(vcodePrizeBasicInfo.getPrizeType());
            vcodePrizeBasicInfo.setPrizeNo(prizeNo);
            if(vcodePrizeBasicInfo.getCashPrize().equals("1")){
                vcodePrizeBasicInfo.setCashPrizeEndTime(null);
            }
            // 图片负载
            vcodePrizeBasicInfo.setPrizeContent(HttpReq.replaceImgUr(vcodePrizeBasicInfo.getPrizeContent()));
            vcodeActivityBigPrizeService.writePrizeInfo(vcodePrizeBasicInfo);
            CacheUtilNew.removeByKey(Constant.BIGPRIZEKEY.BIGPRIZEKEY);
            errMsg = "保存成功";
        } catch (Exception ex) {
            errMsg = "保存失败";
            log.error(ex.getMessage(), ex);
        }
        model.addAttribute("errMsg", errMsg);
        return "forward:/vcodeActivityBigPrize/showBigPrizeList.do";
    }

    /**
     * 获取奖品编号
     * @return
     * @param prizeType
     */
    private String getPrizeNo(String prizeType) {
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", "vps_vcode_prize_basic_info");
        map.put("bussionNoCol", "prize_no");
        map.put("bussionNoPrefix", prizeType);
        int maxCount = commonDao.getCount(map) + 1;
        return prizeType + String.format("%05d", maxCount);
    }

    /**
     * 删除记录
     */
    @RequestMapping("/doBigPrizeDel")
    public String doBigPrizeDel(HttpSession session, String infoKey, Model model , String prizeType){
        try{
            //获取大奖表是否已经中出过该奖品
            Map<String, String> param = new HashMap<>();
            prizeType = prizeType.contains(",") ? prizeType.replace("," ,"") : prizeType;
            param.put("prizeType" , prizeType);
            param.put("infoKey" , infoKey);
            param.put("prizeTypeToLowerCase", prizeType.toLowerCase());
            int count = vpsVcodePrizeRecordDao.getPrizeCount(param);
            if(count != 0){
                model.addAttribute("errMsg", "删除失败,该奖项已经中出过不能删除");
                return "forward:/vcodeActivityBigPrize/showBigPrizeList.do";
            }
            vcodeActivityBigPrizeService.delPrize(infoKey);
            model.addAttribute("errMsg", "删除成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "删除失败");
            log.error("删除失败", ex);
        }
        return "forward:/vcodeActivityBigPrize/showBigPrizeList.do";
    }


    /**
     * 修改大奖信息
     */
    @RequestMapping("/doBigPrizeEdit")
    public String doBigPrizeEdit(HttpSession session, VcodePrizeBasicInfo vcodePrizeBasicInfo, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            vcodePrizeBasicInfo.setUpdateTime(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            vcodePrizeBasicInfo.setUpdateUser(currentUser.getUserName());
            if(vcodePrizeBasicInfo.getCashPrize().equals("1")){
                vcodePrizeBasicInfo.setCashPrizeEndTime(null);
            }
            // 图片负载
            vcodePrizeBasicInfo.setPrizeContent(HttpReq.replaceImgUr(vcodePrizeBasicInfo.getPrizeContent()));
            vcodeActivityBigPrizeService.updatePrizeInfo(vcodePrizeBasicInfo);
            CacheUtilNew.removeByKey(Constant.BIGPRIZEKEY.BIGPRIZEKEY);
            model.addAttribute("errMsg", "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errMsg", "保存失败");
        }
        Map<String, Object> map = new HashMap<>();
        return "forward:/vcodeActivityBigPrize/showBigPrizeList.do";
    }



}
