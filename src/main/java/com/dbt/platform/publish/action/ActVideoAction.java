package com.dbt.platform.publish.action;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dbt.crm.CRMServiceServiceImpl;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.action.reply.BaseResult;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.*;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.publish.bean.VpsActVideo;
import com.dbt.platform.publish.bean.VpsActVideo;
import com.dbt.platform.publish.service.ActRuleService;
import com.dbt.platform.publish.service.VpsActVideoService;
import com.dbt.platform.publish.vo.VpsActVideoVO;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.VpointsGoodsInfo;
import com.dbt.vpointsshop.service.VpointsGoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @ClassName: ActRuleAction
 * @Description: 发布系统活动规则类
 * @author: bin.zhang
 * @date: 2019年12月20日 下午1:39:01
 */
@Controller
@RequestMapping("/actVideo")
public class ActVideoAction extends BaseAction {
    @Autowired
    private VpsActVideoService vpsActVideoService;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private VpointsGoodsService goodService;
    @Autowired
    private CRMServiceServiceImpl crmService;
    /**
     * @Title: showBatchInfoList
     * @Description: 活动规则展示列表
     */
    @RequestMapping("/showVideoList")
    public String showActRuleList(HttpSession session, String pageParam, String queryParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            if(currentUser!= null){
                PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
                VpsActVideoVO queryBean = new VpsActVideoVO(queryParam);
                IPage<VpsActVideo> data = vpsActVideoService.queryForLst(queryBean, pageInfo);

                List<VpsActVideoVO> resultList = new ArrayList<>();

                List<VpsActVideo> records = data.getRecords();
                records.forEach(record->{
                    VpsActVideoVO videoVO = new VpsActVideoVO();
                    BeanUtils.copyProperties(record, videoVO);
                    videoVO.setStartTime(DateUtil.getDateTime(record.getStartTime(), DateUtil.DEFAULT_DATETIME_FORMAT));
                    videoVO.setEndTime(DateUtil.getDateTime(record.getEndTime(), DateUtil.DEFAULT_DATETIME_FORMAT));
                    videoVO.setModTime(DateUtil.getDateTime(record.getUpdateTime(), DateUtil.DEFAULT_DATETIME_FORMAT));
                    resultList.add(videoVO);
                });
                model.addAttribute("currentUser", currentUser);
                model.addAttribute("resultList", resultList);
                model.addAttribute("showCount", data.getTotal());
                model.addAttribute("startIndex", pageInfo.getStartCount());
                model.addAttribute("countPerPage", pageInfo.getPagePerCount());
                model.addAttribute("currentPage", pageInfo.getCurrentPage());
                model.addAttribute("orderCol", pageInfo.getOrderCol());
                model.addAttribute("orderType", pageInfo.getOrderType());
                model.addAttribute("queryBean", queryBean);
                model.addAttribute("queryParam", queryParam);
                model.addAttribute("pageParam", pageParam);
                model.addAttribute("tabsFlag", "5");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "publish/mainpage/pubSysMain";
    }
    /**
     * 跳转编辑页面
     */
    @RequestMapping("/showVideoEdit")
    public String showActRuleEdit(HttpSession session, String videoKey, Model model) {
        SysUserBasis currentUser = this.getUserBasis(session);
        if(currentUser!= null) {
            String companyKey = currentUser.getCompanyKey();
            try{
                VpsActVideoVO vpsActVideo = vpsActVideoService.findById(videoKey );
                PageOrderInfo page = new PageOrderInfo("");
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
                List<VpointsGoodsInfo> allGoods = goodService.getGoodsList(page, vpointsGoodsInfo);
                model.addAttribute("goodsList", allGoods);
                String groupSwitch = DatadicUtil.getDataDicValue(DatadicKey
                        .dataDicCategory.FILTER_SWITCH_SETTING, DatadicKey.filterSwitchSetting.SWITCH_GROUP);
                if(DatadicUtil.isSwitchON(groupSwitch)) {
                    try {
                        model.addAttribute("groupList", crmService.queryVcodeActivityCrmGroup());
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
                model.addAttribute("groupSwitch", DatadicUtil.isSwitchON(groupSwitch) ? "1" : "0");
                model.addAttribute("vpsActVideo", vpsActVideo);
            }catch (Exception e){
                e.printStackTrace();
            }



        }
        return "/publish/adpub/editVideoAd";
    }
    /**
     * 跳转新增页面
     */
    @RequestMapping("/showVideoAdd")
    public String showActRuleAdd(HttpSession session, String videoKey, Model model) {
        SysUserBasis currentUser = this.getUserBasis(session);
        if(currentUser!= null){
            PageOrderInfo page = new PageOrderInfo("");
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
            List<VpointsGoodsInfo> allGoods = goodService.getGoodsList(page, vpointsGoodsInfo);
            model.addAttribute("goodsList", allGoods);
        }

        String groupSwitch = DatadicUtil.getDataDicValue(DatadicKey
                .dataDicCategory.FILTER_SWITCH_SETTING, DatadicKey.filterSwitchSetting.SWITCH_GROUP);
        if(DatadicUtil.isSwitchON(groupSwitch)) {
            try {
                model.addAttribute("groupList", crmService.queryVcodeActivityCrmGroup());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("groupSwitch", DatadicUtil.isSwitchON(groupSwitch) ? "1" : "0");
        return "/publish/adpub/addVideoAd";
    }

    /**
     * 新增
     */
    @RequestMapping("/save")
    public String save(HttpSession session, VpsActVideoVO vpsActVideoVO, Model model) {
        SysUserBasis currentUser = this.getUserBasis(session);
        if(currentUser!= null) {
            if (vpsActVideoVO.getAreaCode().contains(",")) {
                List<String> areaCodes = Arrays.asList(vpsActVideoVO.getAreaCode().split(","));
                LinkedHashSet<String> areaCodeHashSet = new LinkedHashSet<>(areaCodes);
                ArrayList<String> areaCodeListWithoutDuplicates = new ArrayList<>(areaCodeHashSet);
                vpsActVideoVO.setAreaCode(org.apache.commons.lang3.StringUtils.join(areaCodeListWithoutDuplicates, ","));
                List<String> areaNames = Arrays.asList(vpsActVideoVO.getAreaName().split(","));
                LinkedHashSet<String> areaNameHashSet = new LinkedHashSet<>(areaNames);
                ArrayList<String> areaNameListWithoutDuplicates = new ArrayList<>(areaNameHashSet);
                vpsActVideoVO.setAreaName(org.apache.commons.lang3.StringUtils.join(areaNameListWithoutDuplicates, ","));
            }
            vpsActVideoVO.setVideoKey(UUIDTools.getInstance().getUUID());
            vpsActVideoService.insert(vpsActVideoVO, currentUser);
        }
        return "forward:showVideoList.do";
    }
    /**
     * 更新
     */
    @RequestMapping("/update")
    public String update(HttpSession session, VpsActVideoVO vpsActVideoVO, Model model) {
        SysUserBasis currentUser = this.getUserBasis(session);
        if(currentUser!= null) {
            if (vpsActVideoVO.getAreaCode().contains(",")) {
                List<String> areaCodes = Arrays.asList(vpsActVideoVO.getAreaCode().split(","));
                LinkedHashSet<String> areaCodeHashSet = new LinkedHashSet<>(areaCodes);
                ArrayList<String> areaCodeListWithoutDuplicates = new ArrayList<>(areaCodeHashSet);
                vpsActVideoVO.setAreaCode(org.apache.commons.lang3.StringUtils.join(areaCodeListWithoutDuplicates, ","));
                List<String> areaNames = Arrays.asList(vpsActVideoVO.getAreaName().split(","));
                LinkedHashSet<String> areaNameHashSet = new LinkedHashSet<>(areaNames);
                ArrayList<String> areaNameListWithoutDuplicates = new ArrayList<>(areaNameHashSet);
                vpsActVideoVO.setAreaName(org.apache.commons.lang3.StringUtils.join(areaNameListWithoutDuplicates, ","));
            }
            vpsActVideoService.update(vpsActVideoVO, currentUser);
            RedisApiUtil.getInstance().del(true,"VpointsHomePageShow:");
        }
        return "forward:showVideoList.do";
    }
    /**
     * 删除
     */
    @RequestMapping("/delete")
    public String delete(HttpSession session, String  videoKey, Model model) {
        SysUserBasis currentUser = this.getUserBasis(session);
        if(currentUser!= null){
            vpsActVideoService.delete(videoKey);
            RedisApiUtil.getInstance().del(true,"VpointsHomePageShow:");
        }
        return "forward:showVideoList.do";
    }
    /**
     * 更新展示状态
     */
    @RequestMapping("/updateShowFlag")
    @ResponseBody
    public String updateShowFlag(HttpSession session, String  videoKey,String showFlag, Model model) {
        SysUserBasis currentUser = this.getUserBasis(session);
        BaseResult baseResult = new BaseResult();
        VpsActVideoVO vpsActVideoVO = new VpsActVideoVO();
        vpsActVideoVO.setShowFlag(showFlag);
        vpsActVideoVO.setVideoKey(videoKey);
        if("1".equals(showFlag)){
            vpsActVideoService.updateVpsActVideoShow(vpsActVideoVO);
        }else{
            vpsActVideoService.updateVpsActVideoNoShow(vpsActVideoVO);
        }
        RedisApiUtil.getInstance().del(true,"VpointsHomePageShow:");
        return JSON.toJSONString(baseResult.initReslut(Constant.ResultCode.SUCCESS, Constant.ResultCode.SUCCESS, "修改成功"));
    }
    /**
     * 跳转新增页面
     */
    @RequestMapping("/uploadVideo")
    @ResponseBody
    public  Map<String, Object> uploadVideo(HttpSession session, @RequestParam("filePath") MultipartFile file, Model model) {
        SysUserBasis currentUser = this.getUserBasis(session);
        String companyKey = currentUser.getCompanyKey();
        Map<String, Object> resultMap = new HashMap<>();
        String suffix = file.getOriginalFilename().substring(
                file.getOriginalFilename().lastIndexOf("."));
        if(suffix!=null && (suffix.equalsIgnoreCase(".mp4") || suffix.equalsIgnoreCase(".m4v") )){
            long maxFileSize = 10 * 1024 * 1024;
            long fileSize = file.getSize();
            if(fileSize > maxFileSize){
                resultMap.put("url", "");
                resultMap.put("status", 1);
                resultMap.put("msg", "视频大于10M");
            }else{
                try {
                    String imgUrl = HttpReq.uploadImgFile(file, HttpReq.PATH_TYPE.PATH);
                    if(StringUtils.isNotEmpty(imgUrl)){
                        resultMap.put("url",imgUrl);
                        resultMap.put("status", 200);
                        resultMap.put("msg", "成功");
                    }else{
                        resultMap.put("url", "");
                        resultMap.put("status", 1);
                        resultMap.put("msg", "上传失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return resultMap;
    }
}
