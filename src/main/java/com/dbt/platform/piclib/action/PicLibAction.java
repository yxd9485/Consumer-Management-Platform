package com.dbt.platform.piclib.action;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.piclib.bean.PicLibrary;
import com.dbt.platform.piclib.service.PicLibService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * @Author bin.zhang
 **/
@Controller
@RequestMapping("/picLib")
public class PicLibAction extends BaseAction {
    /**
     * 时间格式
     */
    private final String formatter = "yyyy-MM-dd HH:mm:ss";
    @Autowired
    private PicLibService picLibService;
    /**
     * 阶梯规则列表
     *
     * @param session
     * @param queryParam
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/showPicLibList")
    public String showPicLibList(HttpSession session,
                                   String queryParam, String pageParam, Model model, String group) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            PicLibrary queryBean = new PicLibrary(queryParam);
            if(StringUtils.isNotBlank(group)){
                queryBean.setPicGroup(group);
            }
            if (StringUtils.isNotEmpty(currentUser.getPicBrandType())) {
                queryBean.setPicBrandType(currentUser.getPicBrandType());
            }
            List<PicLibrary> resultList = picLibService.findPicLib(queryBean, pageInfo);
            List<PicLibrary> picLibrarys = picLibService.queryAll(pageInfo);
            if (StringUtils.isNotEmpty(currentUser.getPicBrandType())) {
                picLibrarys = picLibrarys.stream().filter(filter -> {
                    return currentUser.getPicBrandType().equals(filter.getPicBrandType());
                }).collect(Collectors.toList());
            }
            int countResult = picLibService.findPicLibCount(queryBean);
             model = getGroupModel(picLibrarys, model);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultList);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("picGroup", group);
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/piclib/showPicLibList";
    }
    @RequestMapping("/picLibList")
    public String picLibList(HttpSession session,
                                 String queryParam, String pageParam, Model model, String group) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            PicLibrary queryBean = new PicLibrary(queryParam);
            String dbType = DbContextHolder.getDBType();
            DbContextHolder.setDBType(null);
            
            // 依据dbType确定要查询素材所属品牌，格式：品牌:projectServerName1,projectServerName2...;品牌:projectServerName1,projectServerName2
            if (StringUtils.isBlank(dbType)) queryBean.setPicBrandType("QP"); // 默认青啤
            String[] brandProjectAry = StringUtils.defaultString(DatadicUtil.getDataDicValue(DatadicUtil
                    .dataDicCategory.DATA_CONSTANT_CONFIG, DatadicUtil.dataDic.dataConstantConfig.PIC_BRAND_PROJECT_SERVER)).split(";");
            String[] itemAry = null;
            List<String> itemLst = null;
            for (String item : brandProjectAry) {
                itemAry = item.split(":");
                if (itemAry.length != 2) continue;
                itemLst = Arrays.asList(itemAry[1].split(","));
                if (itemLst.contains(dbType)) {
                    queryBean.setPicBrandType(itemAry[0]);
                    break;
                }
            }
            
            if(StringUtils.isNotBlank(group)){
                queryBean.setPicGroup(group);
            }
            List<PicLibrary> resultList = picLibService.findPicLib(queryBean, pageInfo);

            int countResult = picLibService.findPicLibCount(queryBean);
            model = getGroupModel(resultList, model);
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
            model.addAttribute("group", group);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/templateui/picLibList";
    }

    /**
     * 获取分组的素材数量
     * @param resultList
     * @param model
     * @return
     */
    private Model getGroupModel(List<PicLibrary> resultList, Model model) {
        List<PicLibrary> logoList = new ArrayList<>();
        List<PicLibrary> sloganList = new ArrayList<>();
        List<PicLibrary> backList = new ArrayList<>();
        List<PicLibrary> redList = new ArrayList<>();
        List<PicLibrary> proList = new ArrayList<>();
        List<PicLibrary> tipList = new ArrayList<>();
        List<PicLibrary> btnList = new ArrayList<>();
        List<PicLibrary> adUpList = new ArrayList<>();
        List<PicLibrary> adHomeList = new ArrayList<>();
        List<PicLibrary> adShopList = new ArrayList<>();
        List<PicLibrary> actRuleList = new ArrayList<>();
        List<PicLibrary> adUpPicList = new ArrayList<>();
        List<PicLibrary> prizePicList = new ArrayList<>();
        List<PicLibrary> ladderUiList = new ArrayList<>();
        List<PicLibrary> vmtsPrizePicList = new ArrayList<>();
        List<PicLibrary> actZoneList = new ArrayList<>();
        List<PicLibrary> nylgPrizePicList = new ArrayList<>();
        List<PicLibrary> noticePicList = new ArrayList<>();
        for(PicLibrary p : resultList){
            switch(p.getPicGroup()){
                case Constant.picLibType.LOGO:
                    logoList.add(p);
                    break;
                case Constant.picLibType.SLOGAN:
                    sloganList.add(p);
                    break;
                case Constant.picLibType.BACK:
                    backList.add(p);
                    break;
                case Constant.picLibType.RED:
                    redList.add(p);
                    break;
                case Constant.picLibType.PRO:
                    proList.add(p);
                    break;
                case Constant.picLibType.TIP:
                    tipList.add(p);
                    break;
                case Constant.picLibType.BTN:
                    btnList.add(p);
                    break;
                case Constant.picLibType.ADUP:
                    adUpList.add(p);
                    break;
                case Constant.picLibType.ADHOME:
                    adHomeList.add(p);
                    break;
                case Constant.picLibType.ADSHOP:
                    adShopList.add(p);
                    break;
                case Constant.picLibType.ACTRULE:
                    actRuleList.add(p);
                    break;
                case Constant.picLibType.ADUPPIC:
                    adUpPicList.add(p);
                    break;
                case Constant.picLibType.PRIZEPIC:
                    prizePicList.add(p);
                    break;
                case Constant.picLibType.LADDERUI:
                    ladderUiList.add(p);
                    break;
                case Constant.picLibType.VMTSPRIZEPIC:
                    vmtsPrizePicList.add(p);
                    break;
                case Constant.picLibType.ACTZONE:
                    actZoneList.add(p);
                    break;
                case Constant.picLibType.NYLGPRIZEPIC:
                	nylgPrizePicList.add(p);
                	break;
                case Constant.picLibType.NOTICE:
                	noticePicList.add(p);
                	break;
            }
        }
        model.addAttribute("logoCount", logoList.size());
        model.addAttribute("sloganCount", sloganList.size());
        model.addAttribute("backCount", backList.size());
        model.addAttribute("redCount", redList.size());
        model.addAttribute("proCount", proList.size());
        model.addAttribute("tipCount", tipList.size());
        model.addAttribute("btnCount", btnList.size());
        model.addAttribute("adUpCount", adUpList.size());
        model.addAttribute("adHomeCount", adHomeList.size());
        model.addAttribute("adShopCount", adShopList.size());
        model.addAttribute("actRuleCount", actRuleList.size());
        model.addAttribute("adUpPicCount", adUpPicList.size());
        model.addAttribute("prizePicCount", prizePicList.size());
        model.addAttribute("ladderUiCount", ladderUiList.size());
        model.addAttribute("vmtsPrizeUiCount", vmtsPrizePicList.size());
        model.addAttribute("actZoneCount", actZoneList.size());
        model.addAttribute("nylgPrizeUiCount", nylgPrizePicList.size());
        model.addAttribute("noticePicCount", noticePicList.size());
        return model;
    }

    /**
     * 添加阶梯UI页面
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/showPicLibAdd")
    public String showPicLibAdd(HttpSession session, Model model) {
        Map<String, String> picBrandType = picLibService.getPicBrandType();
        SysUserBasis currentUser = this.getUserBasis(session);
        if(StringUtils.isNotEmpty(currentUser.getPicBrandType())){
            picBrandType = picBrandType.entrySet()
                    .stream().filter(fm -> {return currentUser.getPicBrandType().equals(fm.getKey());})
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        model.addAttribute("picBrandMap", picBrandType);
        return "vcode/piclib/showPicLibAdd";
    }
    /**
     * 创建规则
     */
    @RequestMapping("/doPicLibAdd")
    public String doPicLibAdd(HttpSession session, PicLibrary picLibrary , Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            picLibrary.setCreateTime(DateUtil.getDate(formatter));
            picLibrary.setCreateUser(currentUser.getUserName());
            picLibService.addPicLib(picLibrary);
            model.addAttribute("errMsg", "添加成功");
        }catch (Exception ex) {
            model.addAttribute("errMsg", "添加失败");
            log.error(ex.getMessage(), ex);
        }
        return showPicLibList(session,
                null, null,  model, null);
    }
    /**
     * 删除活动
     */
    @RequestMapping("/doPicLibDel")
    public String doPicLibDel(HttpSession session, Model model,String key) {
        try {
            List<String> infoKeys = new ArrayList<>(Arrays.asList(
                    StringUtils.defaultIfBlank(key, "").split(",")));
            picLibService.deleteById(infoKeys);
            model.addAttribute("errMsg", "删除成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "删除失败");
            log.error(ex.getMessage(), ex);
        }
        return "forward:showPicLibList.do";
    }
    /**
     * 更改ui
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/doBatchPicLibUpdate")
    public String doBatchPicLibUpdate(HttpSession session, String picGroup, String keyJson,  String info, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PicLibrary picLibrary = new PicLibrary();
            picLibrary.setPicGroup(picGroup);
            picLibrary.setUpdateTime(DateUtil.getDate(formatter));
            picLibrary.setUpdateUser(currentUser.getUserName());
            if(StringUtils.isNotBlank(info)){
                picLibrary.setInfoKey(info);
                picLibService.updatePicLib(picLibrary);
            }else{
                List<String> infoKeys = new ArrayList<>(Arrays.asList(
                        StringUtils.defaultIfBlank(keyJson, "").split(",")));
                picLibService.doBatchPicLibUpdate(infoKeys,picLibrary);
            }
            model.addAttribute("errMsg", "移动分组成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "移动分组失败");
            log.error(ex.getMessage(), ex);
        }
        return showPicLibList(session,
                null, null,  model, null);
    }

    /**
     * 跳转编辑页面
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/showPicLibEdit")
    public String showPicLibEdit(HttpSession session, Model model,String key) {
        try {
            //查询当前规则
            PicLibrary picLibrary = picLibService.findById(key);
            Map<String, String> picBrandType = picLibService.getPicBrandType();
            SysUserBasis currentUser = this.getUserBasis(session);
            if(StringUtils.isNotEmpty(currentUser.getPicBrandType())){
                picBrandType = picBrandType.entrySet()
                        .stream().filter(fm -> {return currentUser.getPicBrandType().equals(fm.getKey());})
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }
            model.addAttribute("picLibrary", picLibrary);
            model.addAttribute("picBrandMap", picBrandType);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return "vcode/piclib/showPicLibEdit";
    }

    /**
     * 更改ui
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/doPicLibEdit")
    public String doPicLibEdit(HttpSession session, PicLibrary picLibrary, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            //如果事默认图片则先更新原来的图片为非默认
            if (picLibrary.getIsDefault().equals("1")){
                picLibService.setPicUnDefault(picLibrary);
            }
            picLibrary.setUpdateTime(DateUtil.getDate(formatter));
            picLibrary.setUpdateUser(currentUser.getUserName());
            picLibService.updatePicLib(picLibrary);
            model.addAttribute("errMsg", "编辑成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "编辑失败");
            log.error(ex.getMessage(), ex);
        }
        return showPicLibList(session,
                null, null,  model, null);
    }

}
