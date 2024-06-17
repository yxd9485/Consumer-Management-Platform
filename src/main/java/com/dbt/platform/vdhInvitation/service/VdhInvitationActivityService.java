package com.dbt.platform.vdhInvitation.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.*;
import com.dbt.framework.zone.bean.SysAreaM;
import com.dbt.framework.zone.service.SysAreaService;
import com.dbt.platform.vdhInvitation.bean.BigRegionInfo;
import com.dbt.platform.vdhInvitation.bean.VpsTerminalGroupInfo;
import com.dbt.platform.vdhInvitation.bean.VpsVcodeVdhInvitationActivityCog;
import com.dbt.platform.vdhInvitation.dao.VdhInvitationActivityDao;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.vdhInvitation.util.VdhConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

@Service
public class VdhInvitationActivityService extends BaseService<VpsVcodeVdhInvitationActivityCog> {
    @Autowired
    private VdhInvitationActivityDao vdhInvitationActivityDao;
    @Autowired
    private SysAreaService sysAreaService;

    private final static String VDH_QUERY_TOKEN = "3d1c1119-f522-1750-8b0d-82547be8e712";

    /**
     * 根据查询条件查询符合记录
     * @param queryBean
     * @param pageInfo
     * @return
     */
    public List<VpsVcodeVdhInvitationActivityCog> queryForList(VpsVcodeVdhInvitationActivityCog queryBean, PageOrderInfo pageInfo, String isBegin) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        List<VpsVcodeVdhInvitationActivityCog> vpsVcodeVdhInvitationActivityCogs = vdhInvitationActivityDao.queryForList(map);
        if (StringUtils.isBlank(isBegin) || "1".equals(isBegin)){
            for (VpsVcodeVdhInvitationActivityCog activityCog: vpsVcodeVdhInvitationActivityCogs) {
                // 已投放总金额
                String totalMoneyCacheKey = RedisApiUtil.CacheKey.vdhInvitationActivity.VDH_INVITATION_ACTIVITY_TOTAL_MONEY
                        + Constant.DBTSPLIT + activityCog.getInfoKey();
                String totalMoneyCacheNum = RedisApiUtil.getInstance().get(totalMoneyCacheKey);
                if (StringUtils.isNotBlank(totalMoneyCacheNum)){
                    activityCog.setPutInMoney(Double.parseDouble(totalMoneyCacheNum));
                }else{
                    activityCog.setPutInMoney(0.00);
                }
            }
        }

        return vpsVcodeVdhInvitationActivityCogs;
    }

    /**
     * 根据查询条件查询符合记录总数
     * @param queryBean
     * @return
     */
    public int queryForCount(VpsVcodeVdhInvitationActivityCog queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return vdhInvitationActivityDao.queryForCount(map);
    }

    /**
     * 新增活动
     * @param activityCog
     */
    public void addActivity(VpsVcodeVdhInvitationActivityCog activityCog, SysUserBasis currentUser) {
        //完成任务sku处理
        String[] skuKeyArray = activityCog.getSkuKeyArray();
        if(skuKeyArray!=null && skuKeyArray.length>0){
            activityCog.setSku(String.join(",", skuKeyArray));
        }
        //老用户是否参加
        if (StringUtils.isBlank(activityCog.getOldUserIsInvolved())){
            activityCog.setOldUserIsInvolved("0");
        }
        activityCog.setInfoKey(UUID.randomUUID().toString());
        activityCog.setActivityNo(getBussionNo("vps_vcode_vdh_invitation_activity_cog", "ACTIVITY_NO", Constant.OrderNoType.type_VI));
        activityCog.setCompanyKey(currentUser.getCompanyKey());
        activityCog.setDeleteFlag("0");
        activityCog.setCreateUser(currentUser.getUserName());
        activityCog.setCreateTime(DateUtil.getDateTime());
        activityCog.setUpdateUser(currentUser.getUserName());
        activityCog.setUpdateTime(DateUtil.getDateTime());
        vdhInvitationActivityDao.create(activityCog);
    }

    /**
     * 修改活动页面跳转
     * @param infoKey
     * @return
     */
    public VpsVcodeVdhInvitationActivityCog showEditActivity(Model model, String infoKey) {
        VpsVcodeVdhInvitationActivityCog byId = vdhInvitationActivityDao.findById(infoKey);
        String sku = byId.getSku();
        if (StringUtils.isNotBlank(sku)){
            byId.setSkuKeyArray(sku.split(","));
        }
        String bigRegion = byId.getBigRegion();
        if (StringUtils.isNotBlank(bigRegion)){
            byId.setBigRegionArray(bigRegion.split(","));
        }
        String terminalGroup = byId.getTerminalGroup();
        if (StringUtils.isNotBlank(terminalGroup)){
            byId.setTerminalGroupArray(terminalGroup.split(","));
        }
        return byId;
    }

    /**
     * 修改活动
     * @param activityCog
     */
    public void editActivity(VpsVcodeVdhInvitationActivityCog activityCog, SysUserBasis currentUser) throws Exception {
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
        vdhInvitationActivityDao.update(activityCog);
    }

    /**
     * 删除活动
     * @param infoKey
     * @param userName
     * @throws Exception
     */
    public String deleteActivity(String infoKey, String userName) throws Exception {
        VpsVcodeVdhInvitationActivityCog activity = findById(infoKey);
        if(activity != null){
            // 删除活动
            VpsVcodeVdhInvitationActivityCog activityTemp = new VpsVcodeVdhInvitationActivityCog();
            activityTemp.setInfoKey(infoKey);
            activityTemp.setDeleteFlag("1");
            activityTemp.setUpdateUser(userName);
            activityTemp.setUpdateTime(DateUtil.getDateTime());
            vdhInvitationActivityDao.update(activityTemp);
            // 删除缓存
            String cacheKeyStrInfoKey = CacheKey.cacheKey.vcode.KEY_VCODE_VDH_INVITATION_ACTIVITY_COG + Constant.DBTSPLIT + activity.getInfoKey();
            CacheUtilNew.removeGroupByKey(cacheKeyStrInfoKey);
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
        VpsVcodeVdhInvitationActivityCog resultData = vdhInvitationActivityDao.checkName(map);
        if(resultData==null){
            return "1";
        }
        return "0";
    }

    /**
     * 根据活动主键查询活动
     * @param infoKey
     * @return
     */
    public VpsVcodeVdhInvitationActivityCog findById(String infoKey) {
        return vdhInvitationActivityDao.findById(infoKey);
    }

    /**
     * 获取行政区域
     * @param model
     * @param area
     */
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

    /**
     * 获取V店惠大区和终端分组
     * @param model
     */
    public void getVdhRegionAndGroup(Model model) throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("projectServerName",DbContextHolder.getDBType());
        paramMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
        paramMap.put(VdhConstants.FIELD_SIGN, VdhConstants.generateSignature(paramMap, VDH_QUERY_TOKEN));
        String paramJson = JSON.toJSONString(paramMap);

        String urlPrefix = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_HTTP_URL,
                DatadicKey.filterHttpUrl.LX_SERVERURL);
        JSONObject rs = HttpReq.handerHttpReq(urlPrefix + "bigRegion/queryRegionInfoByProjectServerName", paramJson);
        if (rs!=null){
            JSONObject resultObj = rs.getJSONObject("result");
            if (resultObj.getIntValue("code")==0 && resultObj.getIntValue("businessCode")==0){

                JSONObject replyObj = rs.getJSONObject("reply");
                JSONArray regionLstArray = replyObj.getJSONArray("regionInfoList");
                List<BigRegionInfo> regionLst = JSON.parseArray(regionLstArray.toJSONString(), BigRegionInfo.class);
                model.addAttribute("regionLst", regionLst);
                model.addAttribute("regionLstSize", regionLst.size());

                JSONArray groupLstArray = replyObj.getJSONArray("terminalGroupInfoList");
                List<VpsTerminalGroupInfo> groupLst = JSON.parseArray(groupLstArray.toJSONString(), VpsTerminalGroupInfo.class);
                model.addAttribute("groupLst", groupLst);
            }else{
                throw new RuntimeException("获取V店惠大区和终端分组远程调用失败");
            }
        }else{
            throw new RuntimeException("获取V店惠大区和终端分组远程调用失败");
        }
    }
}
