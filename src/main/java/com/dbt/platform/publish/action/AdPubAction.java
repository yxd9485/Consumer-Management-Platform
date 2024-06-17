package com.dbt.platform.publish.action;

import com.alibaba.fastjson.JSON;
import com.dbt.crm.CRMServiceServiceImpl;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.activity.bean.VcodeActivityHotAreaCog;
import com.dbt.platform.activity.service.VcodeActivityHotAreaCogService;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.publish.bean.VpsAdHome;
import com.dbt.platform.publish.bean.VpsAdShop;
import com.dbt.platform.publish.bean.VpsAdUp;
import com.dbt.platform.publish.service.AdPubService;
import com.dbt.platform.system.bean.SysRole;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: AdPubAction
 * @Description: 广告发布action
 * @author: bin.zhang
 * @date: 2019年12月20日 下午4:18:18
 */
@Controller
@RequestMapping("/adPub")
public class AdPubAction extends BaseAction {
    public final String ALLSKU = "000000000-000";
    @Autowired
    private AdPubService adPubService;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private CRMServiceServiceImpl crmService;
    @Autowired
    private VcodeActivityHotAreaCogService hotAreaCogService;


    /**
     * 扫码弹窗 1 商城轮播 2 首页轮播 3
     */
    @RequestMapping("/showAdUpList")
    public String showAdPubList(HttpSession session, String pageParam, String queryParam, Model model) {
        try {
            List<SysRole> sysRoleList = this.getSecurityContext(session).getSysRoleList();
            List<String> roleList = sysRoleList.stream().map(SysRole::getRoleKey).collect(Collectors.toList());
            //如果是商城运营角色，默认首页轮播图
            if(roleList.contains("13")){
                model.addAttribute("roleType", "ShopOperate");
            }
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsAdUp queryBean = new VpsAdUp(queryParam);
            List<VpsAdUp> resultLst = adPubService.queryForLst(queryBean, pageInfo);
            int countResult = adPubService.queryForCount(queryBean);
            
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
            model.addAttribute("tabsFlag", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "publish/mainpage/pubSysMain";
    }

    /**
     * 修改弹窗广告
     */
    @RequestMapping("/adUpEdit")
    public String adUpEdit(HttpSession session, VpsAdUp vpsAdUp, Model model) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        SysUserBasis currentUser = this.getUserBasis(session);
        model.addAttribute("currentUser", currentUser);
        try {
            if (!"4".equals(vpsAdUp.getAdLoc()) && !"5".equals(vpsAdUp.getAdLoc())){
                String skuKey = vpsAdUp.getSkuKey().contains(",") ? vpsAdUp.getSkuKey().split(",")[0] : vpsAdUp.getSkuKey();
                if (vpsAdUp.getSkuKey().contains(ALLSKU)) {
                    String companyKey = currentUser.getCompanyKey();
                    List<SkuInfo> skuInfoList = skuInfoService.loadSkuListByCompany(companyKey);
                    List<String> skuKeys = new ArrayList<>();
                    skuKeys.add(ALLSKU);
                    for (SkuInfo s : skuInfoList) {
                        skuKeys.add(s.getSkuKey());
                    }
                    vpsAdUp.setSkuKey(StringUtils.join(skuKeys, ","));
                    vpsAdUp.setSkuNm("全部");
                } else {
                    SkuInfo skuInfo = skuInfoService.findById(skuKey);
                    vpsAdUp.setSkuNm(vpsAdUp.getSkuKey().contains(",") ? skuInfo.getSkuName() + "...." : skuInfo.getSkuName());
                }

                //sku去重m没有全部选项
                if (vpsAdUp.getSkuKey().contains(",") && !vpsAdUp.getSkuNm().equals("全部")) {
                    List<String> skuKeys = Arrays.asList(vpsAdUp.getSkuKey().split(","));
                    LinkedHashSet<String> hashSet = new LinkedHashSet<>(skuKeys);
                    ArrayList<String> listWithoutDuplicates = new ArrayList<>(hashSet);
                    vpsAdUp.setSkuKey(StringUtils.join(listWithoutDuplicates, ","));
                }
            } else {
                vpsAdUp.setSkuKey(null);
            }

            if ("1".equals(vpsAdUp.getAdLoc())){
                vpsAdUp.setFrontDelayed("");
            }


            //根据区域类型判断保存值
            if ("0".equals(vpsAdUp.getAreaType()) && vpsAdUp.getAreaCode().contains(",")) {
                List<String> areaCodes = Arrays.asList(vpsAdUp.getAreaCode().split(","));
                LinkedHashSet<String> areaCodeHashSet = new LinkedHashSet<>(areaCodes);
                ArrayList<String> areaCodeListWithoutDuplicates = new ArrayList<>(areaCodeHashSet);
                vpsAdUp.setAreaCode(StringUtils.join(areaCodeListWithoutDuplicates, ","));
                List<String> areaNames = Arrays.asList(vpsAdUp.getAreaName().split(","));
                LinkedHashSet<String> areaNameHashSet = new LinkedHashSet<>(areaNames);
                ArrayList<String> areaNameListWithoutDuplicates = new ArrayList<>(areaNameHashSet);
                vpsAdUp.setAreaName(StringUtils.join(areaNameListWithoutDuplicates, ","));

            }


            vpsAdUp.setModGmt(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            vpsAdUp.setModUser(currentUser.getUserName());
            if(StringUtils.isNotEmpty(vpsAdUp.getPopGroupName())){
                vpsAdUp.setPopGroupName(vpsAdUp.getPopGroupName().trim());
            }
            adPubService.updateVpsAdUp(vpsAdUp);
            String redisKey = Constant.pubFalg.ADUP;
            RedisApiUtil.getInstance().del(true, redisKey);
            model.addAttribute("errMsg", "修改成功");
        } catch (Exception e) {
            model.addAttribute("errMsg", "修改失败");
            log.error("广告发布修改失败", e);
        }
        return "forward:/adPub/showAdUpList.do";
    }

    /**
     * 修改首页轮播图
     */
    @RequestMapping("/adHomeEdit")
    public String adHomeEdit(HttpSession session, VpsAdHome vpsAdHome, Model model) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        SysUserBasis currentUser = this.getUserBasis(session);
        try {
            vpsAdHome.setModGmt(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            vpsAdHome.setModUser(currentUser.getUserName());
            if (vpsAdHome.getAreaCode().equals(Constant.pubFalg.ALLLOCAL)) {
                vpsAdHome.setAreaCode(Constant.pubFalg.ALLLOCAL + ",");
            }
            adPubService.updateVpsAdHome(vpsAdHome);
            List<VpsAdHome> adHomes = adPubService.queryAllAdHome();
            String redisKey = Constant.pubFalg.ADHOME;
            RedisApiUtil.getInstance().del(true, redisKey);
            RedisApiUtil.getInstance().set(redisKey, JSON.toJSONString(adHomes), 60 * 60 * 72);
            model.addAttribute("errMsg", "修改成功");
        } catch (Exception e) {
            model.addAttribute("errMsg", "修改失败");
            log.error("广告发布修改失败", e);
        }
        return "forward:/adPub/showHomeAdList.do";
    }

    /**
     * 修改商城轮播图
     */
    @RequestMapping("/adShopEdit")
    public String adShopEdit(HttpSession session, VpsAdShop vpsAdShop, Model model) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        SysUserBasis currentUser = this.getUserBasis(session);
        try {
            vpsAdShop.setModGmt(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            vpsAdShop.setModUser(currentUser.getUserName());
            if (vpsAdShop.getAreaCode().equals(Constant.pubFalg.ALLLOCAL)) {
                vpsAdShop.setAreaCode(Constant.pubFalg.ALLLOCAL + ",");
            }
            adPubService.updateVpsAdShop(vpsAdShop);
            List<VpsAdShop> vpsAdShops = adPubService.querAllAdShop();
            String redisKey = Constant.pubFalg.ADSHOP;
            RedisApiUtil.getInstance().del(true, redisKey);
            RedisApiUtil.getInstance().set(redisKey, JSON.toJSONString(vpsAdShops), 60 * 60 * 72);
            model.addAttribute("errMsg", "修改成功");
        } catch (Exception e) {
            model.addAttribute("errMsg", "修改失败");
            log.error("广告发布修改失败", e);
        }
        return "forward:/adPub/showShopAdList.do";
    }

    /**
     * 跳转新增弹窗页面
     */
    @RequestMapping("/showAdUpAdd")
    public String showAdPubAdd(HttpSession session, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            String companyKey = currentUser.getCompanyKey();
            model.addAttribute("companyKey", companyKey);
            model.addAttribute("currentUser", currentUser);
            List<SkuInfo> skuInfoList = new ArrayList<>();
            SkuInfo sku = new SkuInfo();
            sku.setSkuKey(ALLSKU);
            sku.setSkuName("全部");
            skuInfoList.add(sku);
            skuInfoList.addAll(skuInfoService.loadSkuListByCompany(companyKey));
            model.addAttribute("skuList", skuInfoList);

            //增加热区选项
            List<VcodeActivityHotAreaCog> hotAreaList = hotAreaCogService.findHotAreaListByAreaCode(null);
            model.addAttribute("hotAreaList", hotAreaList);

            try {
                adPubService.initCrmGroup(model);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "/publish/adpub/addUpAd";
    }

    /**
     * 跳转新增商城页面
     */
    @RequestMapping("/showAdShopAdd")
    public String showAdShopAdd(HttpSession session, Model model) {
        SysUserBasis currentUser = this.getUserBasis(session);
        model.addAttribute("currentUser", currentUser);
        try {
            adPubService.initCrmGroup(model);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "/publish/adpub/addShopAd";
    }

    /**
     * 跳转首页轮播新增
     */
    @RequestMapping("/showAdHomeAdd")
    public String showAdHomeAdd(HttpSession session, Model model) {
        model.addAttribute("projectServerName", DbContextHolder.getDBType());
        try {
            adPubService.initCrmGroup(model);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "/publish/adpub/addHomeAd";
    }

    /**
     * 跳转编辑页面
     */
    @RequestMapping("/showAdUpEdit")
    public String showAdPubEdit(HttpSession session, String adUpKey, Model model) {
        VpsAdUp vpsAdPub = adPubService.findById(adUpKey);
        if (vpsAdPub.getSkuKey() == null || "".equals(vpsAdPub.getSkuKey())){
            vpsAdPub.setSkuKey("000000000-000");
        }
        if (vpsAdPub.getSkuKey().contains(ALLSKU)) {
            vpsAdPub.setSkuKey(ALLSKU);
        }
        SysUserBasis currentUser = this.getUserBasis(session);
        String companyKey = currentUser.getCompanyKey();
        List<SkuInfo> skuInfoList = new ArrayList<>();
        SkuInfo sku = new SkuInfo();
        sku.setSkuKey(ALLSKU);
        sku.setSkuName("全部");
        skuInfoList.add(sku);
        skuInfoList.addAll(skuInfoService.loadSkuListByCompany(companyKey));
        model.addAttribute("skuList", skuInfoList);

        model.addAttribute("vpsAdPub", vpsAdPub);
        List<String> skuKeyList = new ArrayList<>();
        if (vpsAdPub.getSkuKey().contains(",")) {
            skuKeyList = Arrays.asList(vpsAdPub.getSkuKey().split(","));
        } else {
            skuKeyList.add(vpsAdPub.getSkuKey());
        }
        model.addAttribute("skuKeyList", skuKeyList);
        try {
            adPubService.initCrmGroup(model);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //增加热区选项
        List<VcodeActivityHotAreaCog> hotAreaList = hotAreaCogService.findHotAreaListByAreaCode(null);
        model.addAttribute("hotAreaList", hotAreaList);
        model.addAttribute("currentUser", currentUser);


        return "/publish/adpub/editUpAd";
    }

    /**
     * 添砖首页轮播编辑页面
     */
    @RequestMapping("/showAdHomeEdit")
    public String showAdHomeEdit(HttpSession session, String adHomeKey, Model model) {
        VpsAdHome vpsAdHome = adPubService.findHomeAdById(adHomeKey);
        model.addAttribute("vpsAdPub", vpsAdHome);
        model.addAttribute("projectServerName", DbContextHolder.getDBType());
        try {
            adPubService.initCrmGroup(model);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "/publish/adpub/editHomeAd";
    }

    /**
     * 跳转商城轮播编辑页面
     */
    @RequestMapping("/showAdShopEdit")
    public String showAdShopEdit(HttpSession session, String adShopKey, Model model) {
        VpsAdShop vpsAdShop = adPubService.findShopAdById(adShopKey);
        model.addAttribute("vpsAdPub", vpsAdShop);
        SysUserBasis currentUser = this.getUserBasis(session);
        model.addAttribute("currentUser", currentUser);
        try {
            adPubService.initCrmGroup(model);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "/publish/adpub/editShopAd";
    }


    /**
     * 删除记录
     */
    @RequestMapping("/adUpDelete")
    public String adPubDelete(HttpSession session, String adPubKey, Model model) {
        try {
            adPubService.adPubDelete(adPubKey);
            List<VpsAdUp> vpsAdUps = adPubService.quAllAdUp();
            String redisKey = Constant.pubFalg.ADUP;
            RedisApiUtil.getInstance().del(true, redisKey);
            RedisApiUtil.getInstance().set(redisKey, JSON.toJSONString(vpsAdUps), 60 * 60 * 72);
            model.addAttribute("errMsg", "删除成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "删除失败");
            log.error("删除失败", ex);
        }
        return "forward:showAdUpList.do";
    }

    /**
     * 删除首页轮播图
     */
    @RequestMapping("/adHomeDelete")
    public String adHomeDelete(HttpSession session, String adHomeKey, Model model) {
        try {
            adPubService.adHomeDelete(adHomeKey);
            List<VpsAdHome> vpsAdHomes = adPubService.queryAllAdHome();
            String redisKey = Constant.pubFalg.ADHOME;
            RedisApiUtil.getInstance().del(true, redisKey);
            RedisApiUtil.getInstance().set(redisKey, JSON.toJSONString(vpsAdHomes), 60 * 60 * 72);
            model.addAttribute("errMsg", "删除成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "删除失败");
            log.error("删除失败", ex);
        }
        return "forward:showHomeAdList.do";
    }

    /**
     * 删除商城轮播图
     */
    @RequestMapping("/adShopDelete")
    public String adShopDelete(HttpSession session, String adShopKey, Model model) {
        try {
            adPubService.adShopDelete(adShopKey);
            List<VpsAdShop> vpsAdShops = adPubService.querAllAdShop();
            String redisKey = Constant.pubFalg.ADSHOP;
            RedisApiUtil.getInstance().del(true, redisKey);
            RedisApiUtil.getInstance().set(redisKey, JSON.toJSONString(vpsAdShops), 60 * 60 * 72);
            model.addAttribute("errMsg", "删除成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "删除失败");
            log.error("删除失败", ex);
        }
        return "forward:showShopAdList.do";
    }

    /**
     * 添加新弹窗广告
     */
    @RequestMapping("/doAdUpAdd")
    public String doAdUpAdd(HttpSession session, VpsAdUp vpsAdPub, Model model) {
        String errMsg = "";
        SysUserBasis currentUser = this.getUserBasis(session);
        if (!"4".equals(vpsAdPub.getAdLoc()) && !"5".equals(vpsAdPub.getAdLoc())) {
            String skuKey = vpsAdPub.getSkuKey().contains(",") ? vpsAdPub.getSkuKey().split(",")[0] : vpsAdPub.getSkuKey();
            if (vpsAdPub.getSkuKey().contains(ALLSKU)) {
                vpsAdPub.setSkuNm("全部");
                String companyKey = currentUser.getCompanyKey();
                List<SkuInfo> skuInfoList = skuInfoService.loadSkuListByCompany(companyKey);
                List<String> skuKeys = new ArrayList<>();
                skuKeys.add(ALLSKU);
                for (SkuInfo s : skuInfoList) {
                    skuKeys.add(s.getSkuKey());
                }
                vpsAdPub.setSkuKey(StringUtils.join(skuKeys, ","));
            } else {
                SkuInfo skuInfo = skuInfoService.findById(skuKey);
                vpsAdPub.setSkuNm(vpsAdPub.getSkuKey().contains(",") ? skuInfo.getSkuName() + "...." : skuInfo.getSkuName());
            }

            if (vpsAdPub.getSkuKey().contains(",")) {
                List<String> skuKeys = Arrays.asList(vpsAdPub.getSkuKey().split(","));
                LinkedHashSet<String> hashSet = new LinkedHashSet<>(skuKeys);
                ArrayList<String> listWithoutDuplicates = new ArrayList<>(hashSet);
                vpsAdPub.setSkuKey(StringUtils.join(listWithoutDuplicates, ","));
            }
        }else {
            vpsAdPub.setSkuKey(null);
        }

        //根据区域类型判断保存值
        if ("0".equals(vpsAdPub.getAreaType()) && vpsAdPub.getAreaCode().contains(",")) {

            List<String> areaCodes = Arrays.asList(vpsAdPub.getAreaCode().split(","));
            LinkedHashSet<String> areaCodeHashSet = new LinkedHashSet<>(areaCodes);
            ArrayList<String> areaCodeListWithoutDuplicates = new ArrayList<>(areaCodeHashSet);
            vpsAdPub.setAreaCode(StringUtils.join(areaCodeListWithoutDuplicates, ","));
            List<String> areaNames = Arrays.asList(vpsAdPub.getAreaName().split(","));
            LinkedHashSet<String> areaNameHashSet = new LinkedHashSet<>(areaNames);
            ArrayList<String> areaNameListWithoutDuplicates = new ArrayList<>(areaNameHashSet);
            vpsAdPub.setAreaName(StringUtils.join(areaNameListWithoutDuplicates, ","));

        }


        if ("".equals(vpsAdPub.getFrontDelayed())){
            vpsAdPub.setFrontDelayed("3");
        }

        if ("1".equals(vpsAdPub.getAdLoc())){
            vpsAdPub.setFrontDelayed("");
        }

        try {
            vpsAdPub.setModUser(currentUser.getUserName());
            vpsAdPub.setCreGmt(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            vpsAdPub.setModGmt(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            if(StringUtils.isNotEmpty(vpsAdPub.getPopGroupName())){
                vpsAdPub.setPopGroupName(vpsAdPub.getPopGroupName().trim());
            }
            adPubService.addAdUp(vpsAdPub);
            String redisKey = Constant.pubFalg.ADUP;
            RedisApiUtil.getInstance().del(true, redisKey);
            errMsg = "保存成功";
            model.addAttribute("errMsg", errMsg);
        } catch (Exception ex) {
            errMsg = "保存失败";
            model.addAttribute("errMsg", errMsg);
            log.error(ex.getMessage(), ex);
        }
        model.addAttribute("errMsg", errMsg);
        return "forward:/adPub/showAdUpList.do";
    }


    /**
     * 获取可以弹出广告list
     *
     * @param adUps
     * @return
     * @throws Exception
     */
    private List<VpsAdUp> getAdUp(List<VpsAdUp> adUps) throws Exception {
        List<VpsAdUp> adUpList = new ArrayList<>();
        Date now = DateUtil.getNow();
        for (VpsAdUp a : adUps) {
            if (now.after(DateUtil.parse(a.getStGmt(), DateUtil.DEFAULT_DATETIME_FORMAT)) &&
                    now.before(DateUtil.parse(a.getEndGmt(), DateUtil.DEFAULT_DATETIME_FORMAT))) {
                adUpList.add(a);
            }
        }
        return adUpList;
    }

    /**
     * 添加新增首页广告
     */
    @RequestMapping("/doAdHomeAdd")
    public String doAdHomeAdd(HttpSession session, VpsAdHome vpsAdHome, Model model) {
        String errMsg = "";
        SysUserBasis currentUser = this.getUserBasis(session);
        try {
            vpsAdHome.setCreGmt(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            vpsAdHome.setModGmt(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            vpsAdHome.setModUser(currentUser.getUserName());
            if (vpsAdHome.getAreaCode().equals(Constant.pubFalg.ALLLOCAL)) {
                vpsAdHome.setAreaCode(Constant.pubFalg.ALLLOCAL + ",");
            }
            adPubService.addAdHome(vpsAdHome);
            String redisKey = Constant.pubFalg.ADHOME;
            List<VpsAdHome> adHomes = adPubService.queryAllAdHome();
            RedisApiUtil.getInstance().del(true, redisKey);
            RedisApiUtil.getInstance().set(redisKey, JSON.toJSONString(adHomes), 60 * 60 * 72);

            errMsg = "保存成功";
            model.addAttribute("errMsg", errMsg);
        } catch (Exception ex) {
            errMsg = "保存失败";
            model.addAttribute("errMsg", errMsg);
            log.error(ex.getMessage(), ex);
        }
        model.addAttribute("errMsg", errMsg);
        return "forward:/adPub/showHomeAdList.do";
    }

    /**
     * 添加新增商城广告
     */
    @RequestMapping("/doAdShopAdd")
    public String doAdShopAdd(HttpSession session, VpsAdShop vpsAdShop, Model model) {
        String errMsg = "";
        SysUserBasis currentUser = this.getUserBasis(session);
        try {
            vpsAdShop.setCreGmt(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            vpsAdShop.setModGmt(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            vpsAdShop.setModUser(currentUser.getUserName());
            if (vpsAdShop.getAreaCode().equals(Constant.pubFalg.ALLLOCAL)) {
                vpsAdShop.setAreaCode(Constant.pubFalg.ALLLOCAL + ",");
            }
            adPubService.addAdShop(vpsAdShop);
            String redisKey = Constant.pubFalg.ADSHOP;
            List<VpsAdShop> vpsAdShops = adPubService.querAllAdShop();
            RedisApiUtil.getInstance().del(true, redisKey);
            RedisApiUtil.getInstance().set(redisKey, JSON.toJSONString(vpsAdShops), 60 * 60 * 72);
            errMsg = "保存成功";
            model.addAttribute("errMsg", errMsg);
        } catch (Exception ex) {
            errMsg = "保存失败";
            model.addAttribute("errMsg", errMsg);
            log.error(ex.getMessage(), ex);
        }
        model.addAttribute("errMsg", errMsg);
        return "forward:/adPub/showShopAdList.do";
    }

    /**
     * 首页轮播广告
     */
    @RequestMapping("/showHomeAdList")
    public String showHomeAdList(HttpSession session, String pageParam, String queryParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsAdHome queryBean = new VpsAdHome(queryParam);
            List<VpsAdHome> resultLst = adPubService.queryHomeAdForLst(queryBean, pageInfo);
            int countResult = adPubService.queryHomeAdForCount(queryBean);

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
            List<SysRole> sysRoleList = this.getSecurityContext(session).getSysRoleList();
            List<String> roleList = sysRoleList.stream().map(SysRole::getRoleKey).collect(Collectors.toList());
            if(roleList.contains("13")){
                model.addAttribute("roleType", "ShopOperate");
            }
            model.addAttribute("tabsFlag", "2");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "publish/mainpage/pubSysMain";
    }

    /**
     * 商城轮播图
     */
    @RequestMapping("/showShopAdList")
    public String showShopAdList(HttpSession session, String pageParam, String queryParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsAdShop queryBean = new VpsAdShop(queryParam);
            List<VpsAdShop> resultLst = adPubService.queryShopAdForLst(queryBean, pageInfo);
            int countResult = adPubService.queryShopAdForCount(queryBean);

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
            List<SysRole> sysRoleList = this.getSecurityContext(session).getSysRoleList();
            List<String> roleList = sysRoleList.stream().map(SysRole::getRoleKey).collect(Collectors.toList());
            if(roleList.contains("13")){
                model.addAttribute("roleType", "ShopOperate");
            }

            model.addAttribute("tabsFlag", "3");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "publish/mainpage/pubSysMain";
    }
}
