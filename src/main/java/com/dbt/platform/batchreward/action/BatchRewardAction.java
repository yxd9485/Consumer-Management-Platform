package com.dbt.platform.batchreward.action;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.ReadExcel;
import com.dbt.platform.batchreward.bean.VpsBatchRewardRecord;
import com.dbt.platform.batchreward.service.BatchRewardSevice;
import com.dbt.platform.system.bean.SysUserBasis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批量发放奖励action
 */
@Controller
@RequestMapping("/batchreward")
public class BatchRewardAction extends BaseAction {

    @Autowired
    private BatchRewardSevice batchRewardSevice;

    /**
     * 发放记录列表
     */
    @RequestMapping("/showBatchRewardRecordList")
    public String showBatchRewardRecordList(HttpSession session, String queryParam, String pageParam, Model model) {
        SysUserBasis currentUser = this.getUserBasis(session);
        PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
        VpsBatchRewardRecord queryBean = new VpsBatchRewardRecord(queryParam);

        List<VpsBatchRewardRecord> resultList = null;
        try {
            resultList = batchRewardSevice.queryForLst(queryBean, pageInfo);
            int countResult = batchRewardSevice.queryForCount(queryBean);
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
            model.addAttribute("roleInfoAll", Arrays.asList(DatadicUtil.getDataDicValue(
                    "data_constant_config", "role_info").split(",")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "vcode/batchreward/showBatchRewardRecordList";
    }

    /**
     * 批量发放激励页面
     */
    @RequestMapping("/showBatchRewardRecordAdd")
    public String showMarqueeCogInfoAdd(HttpSession session, Model model) throws Exception {
        model.addAttribute("projectServerName", DbContextHolder.getDBType());
        return "vcode/batchreward/showBatchRewardRecordAdd";
    }

    /**
     * 验证上传文件
     */
    @ResponseBody
    @RequestMapping("/importBatchRewardRecordList")
    public String importBatchRewardRecordList(HttpSession session,
                                              @RequestParam("filePath") MultipartFile clientFile) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            List<List<Object>> readExcel = checkFile(clientFile);
            resultMap = batchRewardSevice.importBatchRewardRecordList(readExcel, currentUser, clientFile);
        } catch (BusinessException e) {
            resultMap.put("errMsg", e.getMessage());
            log.error(e.getMessage(), e);

        } catch (Exception e) {
            resultMap.put("errMsg", "激励表格上传失败");
            log.error(e.getMessage(), e);
        }
        return JSON.toJSONString(resultMap);
    }

    /**
     * 执行发放激励
     */
    @RequestMapping("/executeGrantReward")
    public String executeGrantReward(HttpSession session, Model model, String rewardDetailJson, String fileUrl,
                                     String userTotal, String vpointsTotal, String moneyTotal, String bigPrizeTotal) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            batchRewardSevice.executeGrantReward(rewardDetailJson, currentUser, fileUrl, userTotal, vpointsTotal, moneyTotal, bigPrizeTotal);
            model.addAttribute("errMsg", "发放成功");
        } catch (BusinessException e) {
            model.addAttribute("errMsg", "发放失败");
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            model.addAttribute("errMsg", "发放失败");
            log.error(e.getMessage(), e);
        }
        return "forward:showBatchRewardRecordList.do";
    }

    /**
     * excel解析
     *
     * @param batchFile
     * @return
     * @throws IOException
     */
    public List<List<Object>> checkFile(MultipartFile batchFile) throws IOException {
        String importfileFileName = batchFile.getOriginalFilename();
        InputStream input = batchFile.getInputStream();
        if (input.available() > 0) {
            List<List<Object>> readExcel
                    = ReadExcel.readExcel(input, importfileFileName.substring(importfileFileName.lastIndexOf(".") + 1), 18);

            return readExcel;
        }
        return null;
    }
}
