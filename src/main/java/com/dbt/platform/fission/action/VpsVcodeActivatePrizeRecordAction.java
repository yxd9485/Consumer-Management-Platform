package com.dbt.platform.fission.action;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ExcelUtil;
import com.dbt.platform.fission.bean.VpsVcodeActivatePrizeRecordEntity;
import com.dbt.platform.fission.service.IVpsVcodeActivatePrizeRecordService;
import com.dbt.platform.fission.vo.VpsVcodeActivatePrizeRecordVO;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 *  裂变红包兑换记录
 * </p>
 *
 * @author wangshuda
 * @since 2022-01-12
 */
@Controller
@RequestMapping("/activatePrizeRecord")
public class VpsVcodeActivatePrizeRecordAction extends BaseAction {
    @Autowired
    private IVpsVcodeActivatePrizeRecordService iVpsVcodeActivatePrizeRecordService;

    /**
     * 跳转二维码查询页面
     */
    @RequestMapping("/showQueryList")
    public String showQrcodeQueryView(HttpSession session,String queryParam, String pageParam,String activityKey, Model model){
        try{
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsVcodeActivatePrizeRecordVO queryBean = new VpsVcodeActivatePrizeRecordVO(queryParam);
            if(StringUtils.isNotEmpty(activityKey)){
                queryBean.setActivityKey(activityKey);
            }
            IPage<VpsVcodeActivatePrizeRecordEntity> ipage = iVpsVcodeActivatePrizeRecordService.selectPage(pageInfo,queryBean,currentUser.getCompanyKey());
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", ipage.getRecords());
            model.addAttribute("showCount", ipage.getTotal());
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("activityKey", activityKey);
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("nowTime", new LocalDate());
        }catch (Exception e){
            e.printStackTrace();
        }
        return "vcode/fission/record/showQueryList";
    }

    /**
     * 跳转二维码查询页面
     */
    @RequestMapping("/exportExcel")
    public void exportExcel(HttpSession session,String queryParam, String pageParam,String activityKey, Model model, HttpServletResponse response){
        try{
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsVcodeActivatePrizeRecordVO queryBean = new VpsVcodeActivatePrizeRecordVO(queryParam);
            if(StringUtils.isNotEmpty(activityKey)){
                queryBean.setActivityKey(activityKey);
            }
            List<VpsVcodeActivatePrizeRecordEntity> vpsVcodeActivatePrizeRecordEntities = iVpsVcodeActivatePrizeRecordService.exportList(pageInfo,queryBean, currentUser.getCompanyKey());
            response.reset();
            response.setCharacterEncoding("GBK");
            response.setContentType("application/msexcel;charset=UTF-8");
            OutputStream outStream = response.getOutputStream();
            String bookName = "激活积分红包名单";
            vpsVcodeActivatePrizeRecordEntities.forEach(vpsVcodeActivatePrizeRecordEntity -> {
                if ("1".equals(vpsVcodeActivatePrizeRecordEntity.getMoneyFlag())) {
                    vpsVcodeActivatePrizeRecordEntity.setStatus("已激活");
                }else{
                    vpsVcodeActivatePrizeRecordEntity.setStatus("未激活");
                }

                if (vpsVcodeActivatePrizeRecordEntity.getObtainFlag().equals("0")) {
                    vpsVcodeActivatePrizeRecordEntity.setObtainFlag("主动获得");
                } else if (vpsVcodeActivatePrizeRecordEntity.getObtainFlag().equals("1")) {
                    vpsVcodeActivatePrizeRecordEntity.setObtainFlag("被动获得");
                }
            });
            String[] headers = new String[] {"userID", "昵称", "二维码", "中出SKU","中出活动", "状态", "中出金额", "中出积分","奖励金额","奖励积分", "分享人","获得方式","中出时间", "结束时间"};
            String[] valueTags = new String[] {"userKey", "nickName", "winQrcodeContent", "winSkuName","ruleName", "status", "winningAmount", "winningPoint","rewardAmount","rewardPoint", "shareUserName","obtainFlag","winDate", "winEndTime"};
            response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode(bookName, "UTF-8") + DateUtil.getDate() + ".xls");
            ExcelUtil<VpsVcodeActivatePrizeRecordEntity> excel = new ExcelUtil<VpsVcodeActivatePrizeRecordEntity>();
            excel.writeExcel(bookName, headers, valueTags, vpsVcodeActivatePrizeRecordEntities, DateUtil.DEFAULT_DATETIME_FORMAT, outStream);
            outStream.close();
            response.flushBuffer();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

