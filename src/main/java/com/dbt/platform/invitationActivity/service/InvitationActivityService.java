package com.dbt.platform.invitationActivity.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.zone.bean.SysAreaM;
import com.dbt.framework.zone.service.SysAreaService;
import com.dbt.platform.invitationActivity.bean.VpsVcodeInvitationActivityCog;
import com.dbt.platform.invitationActivity.bean.VpsVcodeInvitationOrder;
import com.dbt.platform.invitationActivity.dao.InvitationActivityDao;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

@Service
public class InvitationActivityService extends BaseService<VpsVcodeInvitationActivityCog> {
    @Autowired
    private InvitationActivityDao invitationActivityDao;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private InvitationOrderService invitationOrderService;
    @Autowired
    private InvitationCodeService invitationCodeService;

    private final static String RANDOM_BASE = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 根据查询条件查询符合记录
     * @param queryBean
     * @param pageInfo
     * @return
     */
    public List<VpsVcodeInvitationActivityCog> queryForList(VpsVcodeInvitationActivityCog queryBean, PageOrderInfo pageInfo, String isBegin) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        List<VpsVcodeInvitationActivityCog> vpsVcodeInvitationActivityCogs = invitationActivityDao.queryForList(map);
        if (StringUtils.isBlank(isBegin) || "1".equals(isBegin)){
            for (VpsVcodeInvitationActivityCog activityCog:vpsVcodeInvitationActivityCogs) {
                // 已投放总金额
                String totalMoneyCacheKey = RedisApiUtil.CacheKey.invitationActivity.INVITATION_ACTIVITY_TOTAL_MONEY
                        + Constant.DBTSPLIT + activityCog.getInfoKey();
                String totalMoneyCacheNum = RedisApiUtil.getInstance().get(totalMoneyCacheKey);
                if (StringUtils.isNotBlank(totalMoneyCacheNum)){
                    activityCog.setPutInMoney(Double.parseDouble(totalMoneyCacheNum));
                }else{
                    activityCog.setPutInMoney(0.00);
                }
                // 已投放总积分
                String totalVpointsCacheKey = RedisApiUtil.CacheKey.invitationActivity.INVITATION_ACTIVITY_TOTAL_VPOINTS
                        + Constant.DBTSPLIT + activityCog.getInfoKey();
                String totalVpointsCacheNum = RedisApiUtil.getInstance().get(totalVpointsCacheKey);
                if (StringUtils.isNotBlank(totalVpointsCacheNum)){
                    activityCog.setPutInVpoints(Integer.parseInt(totalVpointsCacheNum));
                }else{
                    activityCog.setPutInVpoints(0);
                }
            }
        }

        return vpsVcodeInvitationActivityCogs;
    }

    /**
     * 根据查询条件查询符合记录总数
     * @param queryBean
     * @return
     */
    public int queryForCount(VpsVcodeInvitationActivityCog queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return invitationActivityDao.queryForCount(map);
    }

    /**
     * 新增活动
     * @param activityCog
     */
    public void addActivity(VpsVcodeInvitationActivityCog activityCog, SysUserBasis currentUser) {
        //完成任务sku处理
        String[] skuKeyArray = activityCog.getSkuKeyArray();
        if(skuKeyArray!=null && skuKeyArray.length>0){
            activityCog.setSku(String.join(",", skuKeyArray));
        }
        //老用户是否参加
        if (StringUtils.isBlank(activityCog.getOldUserIsInvolved())){
            activityCog.setOldUserIsInvolved("0");
        }
        //设置活动flag
        List<VpsVcodeInvitationActivityCog> flagList = invitationActivityDao.checkFlag();
        String randomFlag="";
        if (flagList!=null && flagList.size()>0){
            Map<String, Object> flagMap = new HashMap<>();
            for (VpsVcodeInvitationActivityCog activity: flagList) {
                flagMap.put(activity.getActivityFlag(),"");
            }
            for (int i = 0; i < 100; i++) {
                randomFlag = RandomStringUtils.random(3, RANDOM_BASE);
                if (!flagMap.containsKey(randomFlag)){
                    break;
                }
            }
        }else{
            randomFlag = RandomStringUtils.random(3, RANDOM_BASE);
        }
        if (StringUtils.isBlank(randomFlag)){
            throw new RuntimeException("创建邀请有礼物活动生成随机活动flag次数已达最大");
        }
        activityCog.setInfoKey(UUID.randomUUID().toString());
        activityCog.setActivityNo(getBussionNo("vps_vcode_invitation_activity_cog", "ACTIVITY_NO", Constant.OrderNoType.type_IV));
        activityCog.setActivityFlag(randomFlag);
        activityCog.setCompanyKey(currentUser.getCompanyKey());
        activityCog.setDeleteFlag("0");
        activityCog.setCreateUser(currentUser.getUserName());
        activityCog.setCreateTime(DateUtil.getDateTime());
        activityCog.setUpdateUser(currentUser.getUserName());
        activityCog.setUpdateTime(DateUtil.getDateTime());
        invitationActivityDao.create(activityCog);
    }

    /**
     * 修改活动页面跳转
     * @param infoKey
     * @return
     */
    public VpsVcodeInvitationActivityCog showEditActivity(Model model, String infoKey) {
        VpsVcodeInvitationActivityCog byId = invitationActivityDao.findById(infoKey);
        String sku = byId.getSku();
        if (StringUtils.isNotBlank(sku)){
            byId.setSkuKeyArray(sku.split(","));
        }
        return byId;
    }

    /**
     * 修改活动
     * @param activityCog
     */
    public void editActivity(VpsVcodeInvitationActivityCog activityCog, SysUserBasis currentUser) throws Exception {
        //完成任务sku处理
        String[] skuKeyArray = activityCog.getSkuKeyArray();
        if(skuKeyArray!=null && skuKeyArray.length>0){
            activityCog.setSku(String.join(",", skuKeyArray));
        }
        //老用户是否参加
        if (StringUtils.isBlank(activityCog.getOldUserIsInvolved())){
            activityCog.setOldUserIsInvolved("0");
            activityCog.setOldUserRule("");
        }
        activityCog.setUpdateUser(currentUser.getUserName());
        activityCog.setUpdateTime(DateUtil.getDateTime());
        invitationActivityDao.update(activityCog);
    }

    /**
     * 删除活动
     * @param infoKey
     * @param userName
     * @throws Exception
     */
    public String deleteActivity(String infoKey, String userName) throws Exception {
        VpsVcodeInvitationActivityCog activity = findById(infoKey);
        if(activity != null){
            // 订单检查
            VpsVcodeInvitationOrder order =  invitationOrderService.findAfoot(activity.getInfoKey());
            if (order!=null){
                return "活动内有订单正在生成邀请码，活动删除失败。";
            }

            // 删除活动
            VpsVcodeInvitationActivityCog activityTemp = new VpsVcodeInvitationActivityCog();
            activityTemp.setInfoKey(infoKey);
            activityTemp.setDeleteFlag("1");
            activityTemp.setUpdateUser(userName);
            activityTemp.setUpdateTime(DateUtil.getDateTime());
            invitationActivityDao.update(activityTemp);
            // 删除缓存
            String cacheKeyStrFlag = CacheKey.cacheKey.vcode.KEY_VCODE_ACTIVITY_INVITATION_COG + Constant.DBTSPLIT + activity.getActivityFlag();
            CacheUtilNew.removeGroupByKey(cacheKeyStrFlag);
            String cacheKeyStrInfoKey = CacheKey.cacheKey.vcode.KEY_VCODE_ACTIVITY_INVITATION_COG + Constant.DBTSPLIT + activity.getInfoKey();
            CacheUtilNew.removeGroupByKey(cacheKeyStrInfoKey);

            //删除订单
            invitationOrderService.removeOrderByActivityKey(activity.getInfoKey());

            //删除邀请码
            invitationCodeService.removeCodeByActivityKey(activity.getInfoKey());
        }
        return "";
    }

    /**
     * 检查活动名称
     * @param currentUser
     * @param checkName
     * @param infoKey
     * @return
     */
    public String checkName(SysUserBasis currentUser, String checkName, String infoKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("companyKey", currentUser.getCompanyKey());
        map.put("checkName", checkName);
        map.put("infoKey", infoKey);
        VpsVcodeInvitationActivityCog resultData = invitationActivityDao.checkName(map);
        if(resultData==null){
            return "1";
        }
        return "0";
    }

    /**
     * 检验活动日期及地区
     * @param startTime
     * @param endTime
     * @param area
     * @return
     */
    public String checkDateAndArea(String startTime, String endTime, String area, String infoKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("currentTime", DateUtil.getDate());
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("area", area);
        map.put("infoKey", infoKey);
        List<VpsVcodeInvitationActivityCog> resultData = invitationActivityDao.checkDateAndArea(map);
        if(resultData!=null && resultData.size()>0){
            String[] areaSplit = area.split(";");
            Map<String, Object> areaMap = new HashMap<>();
            for (int i = 0; i < areaSplit.length; i++) {
                areaMap.put(areaSplit[i],"");
            }
            Map<String, Object> repeatAreaMap = new HashMap<>();
            for (VpsVcodeInvitationActivityCog activity : resultData) {
                String rowArea = activity.getArea();
                String[] rowAreaSplit = rowArea.split(";");
                for (int i = 0; i < rowAreaSplit.length; i++) {
                    if (areaMap.containsKey(rowAreaSplit[i])){
                        repeatAreaMap.put(rowAreaSplit[i],"");
                    }
                }
            }
            if (repeatAreaMap.size()==0){
                return "0";
            }else{
                StringBuilder sb = new StringBuilder();
                for (String key : repeatAreaMap.keySet()) {
                    if (sb.length() > 0) {
                        sb.append("，");
                    }
                    sb.append(key);
                }
                return sb.toString();
            }
        }
        return "0";
    }

    /**
     * 根据活动主键查询活动
     * @param infoKey
     * @return
     */
    public VpsVcodeInvitationActivityCog findById(String infoKey) {
        return invitationActivityDao.findById(infoKey);
    }

    /**
     * 获取项目地区配置
     * @param model
     * @param area
     */
    public void getAreaJson(Model model, String area){
        // 获取项目地区配置
        List<String> areaCodeList;
        List<SysAreaM> areaLst;
        String projectArea = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_COMPANY_INFO,
                DatadicKey.filterCompanyInfo.PROJECT_AREA);
        if (!StringUtils.isBlank(projectArea)) {
            String[] areaCodeAry = projectArea.split(",");
            areaCodeList = new ArrayList<>();
            for (String item : areaCodeAry) {
                areaCodeList.add(item + "0000");
            }

            // 返回配置的省级区域
            areaLst = sysAreaService.queryAllByAreaCode(areaCodeList);

            if(CollectionUtils.isNotEmpty(areaLst)){
                // 山东省-济南市-平阴县;山东省-青岛市；河南省；
                String shipmentsArea = StringUtils.defaultIfBlank(area,"");
                String[] areaGroup = shipmentsArea.split(";");
                JSONObject json = null;
                List<JSONObject> areaList = new ArrayList<>();
                for (SysAreaM item : areaLst) {
                    json = new JSONObject();
                    json.put("id", item.getAreaCode());
                    json.put("pId", item.getParentCode());
                    json.put("name", item.getAreaName());
                    if(areaGroup.length > 0 && shipmentsArea.contains(item.getAreaName())){
                        String dbArea = sysAreaService.getAreaNameByCode(item.getAreaCode()).replaceAll(" ", "");
                        for (String areaItem : areaGroup) {
                            if(areaItem.startsWith(dbArea)){
                                json.put("checked", true);
                            }
                        }
                    }
                    areaList.add(json);
                }
                model.addAttribute("areaJson", JSON.toJSONString(areaList));
            }
        }
    }

    public void getAreaJsonAll(Model model, String area){
        List<SysAreaM> areaLst  = sysAreaService.findRegionList();
        if(CollectionUtils.isNotEmpty(areaLst)){
            //删除掉父码不是0的全部
            List<SysAreaM> areaLstTemp = new ArrayList<>();
            for (SysAreaM areaM:areaLst) {
                if (!"全部".equals(areaM.getAreaName()) || ("全部".equals(areaM.getAreaName()) && "0".equals(areaM.getParentCode())) ){
                    areaLstTemp.add(areaM);
                }
            }

            // 山东省-济南市-平阴县;山东省-青岛市；河南省；
            String shipmentsArea = StringUtils.defaultIfBlank(area,"");
            String[] areaGroup = shipmentsArea.split(";");
            JSONObject json = null;
            List<JSONObject> areaList = new ArrayList<>();
            for (SysAreaM item : areaLstTemp) {
                json = new JSONObject();
                json.put("id", item.getAreaCode());
                json.put("pId", item.getParentCode());
                json.put("name", item.getAreaName());
                if(areaGroup.length > 0 && shipmentsArea.contains(item.getAreaName())){
                    String dbArea = sysAreaService.getAreaNameByCode(item.getAreaCode()).replaceAll(" ", "");
                    for (String areaItem : areaGroup) {
                        if(areaItem.startsWith(dbArea)){
                            json.put("checked", true);
                        }
                    }
                }
                areaList.add(json);
            }
            model.addAttribute("areaJson", JSON.toJSONString(areaList));
        }
    }
}
