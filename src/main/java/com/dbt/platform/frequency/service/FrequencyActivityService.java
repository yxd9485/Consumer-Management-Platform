package com.dbt.platform.frequency.service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.bean.VcodeActivityPerhundredCog;
import com.dbt.platform.activity.service.VcodeActivityService;
import com.dbt.platform.frequency.bean.VpsVcodeActivityFrequencyCog;
import com.dbt.platform.frequency.dao.FrequencyActivityDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FrequencyActivityService extends BaseService<VcodeActivityPerhundredCog> {
    @Autowired
    private FrequencyActivityDao frequencyActivityDao;
    @Autowired
    private VcodeActivityService vcodeActivityService;

    public List<VpsVcodeActivityFrequencyCog> queryForList(VpsVcodeActivityFrequencyCog queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        return frequencyActivityDao.queryForList(map);
    }

    public int queryForCount(VpsVcodeActivityFrequencyCog queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return frequencyActivityDao.queryForCount(map);
    }

    public void addFrequencyCog(VpsVcodeActivityFrequencyCog frequencyCog) {
        // 生成编号
        frequencyCog.setInfoKey(UUID.randomUUID().toString());
        frequencyCog.setFrequencyNo(getBussionNo(
                "vps_vcode_activity_frequency_cog", "FREQUENCY_NO", Constant.OrderNoType.type_PC));

        // 获取最小和最大时间
        queryMinMaxDate(frequencyCog);

        // 倍数数据排序
        frequencyCog = dealPerItemsSort(frequencyCog);
        frequencyActivityDao.create(frequencyCog);
    }


    public void editFrequencyCog(VpsVcodeActivityFrequencyCog frequencyCog) throws Exception {

        // 删除原有缓存
        VpsVcodeActivityFrequencyCog oldFrequencyCog = frequencyActivityDao.findById(frequencyCog.getInfoKey());
        String[] oldVcodeActivityKeyArr = oldFrequencyCog.getVcodeActivityKey().split(",");
        for (String oldVcodeActivityKey : oldVcodeActivityKeyArr) {
            String cacheKeyStr = CacheKey.cacheKey.vcode
                    .KEY_VCODE_ACTIVITY_FREQUENCY_COG
                    + Constant.DBTSPLIT + oldVcodeActivityKey + Constant.DBTSPLIT + DateUtil.getDate();
            CacheUtilNew.removeGroupByKey(cacheKeyStr);
        }
        // 获取最小和最大日期
        queryMinMaxDate(frequencyCog);

        // 倍数数据排序
        frequencyCog = dealPerItemsSort(frequencyCog);

        frequencyActivityDao.update(frequencyCog);
    }

    /**
     * 获取最小和最大日期
     * @param frequencyCog
     */
    private void queryMinMaxDate(VpsVcodeActivityFrequencyCog frequencyCog) {
        String[] validDateRangeGro = frequencyCog.getValidTimeRange().split(",");
        if(validDateRangeGro.length == 1){
            frequencyCog.setMinValidTime(validDateRangeGro[0].split("#")[0] + " " + validDateRangeGro[0].split("#")[2]);
            frequencyCog.setMaxValidTime(validDateRangeGro[0].split("#")[1] + " " + validDateRangeGro[0].split("#")[3]);
        }else{
            List<String> minDateList = new ArrayList<>();
            List<String> maxDateList = new ArrayList<>();
            for (int i = 0; i < validDateRangeGro.length; i++) {
                minDateList.add(validDateRangeGro[i].split("#")[0] + " " + validDateRangeGro[i].split("#")[2]);
                maxDateList.add(validDateRangeGro[i].split("#")[1] + " " + validDateRangeGro[i].split("#")[3]);
            }
            Collections.sort(minDateList);
            Collections.sort(maxDateList);
            frequencyCog.setMinValidTime(minDateList.get(0));
            frequencyCog.setMaxValidTime(maxDateList.get(maxDateList.size()-1));
        }
    }

    /**
     * 倍数数据排序
     * @param frequencyCog
     * @return
     */
    private VpsVcodeActivityFrequencyCog dealPerItemsSort(VpsVcodeActivityFrequencyCog frequencyCog) {
        // 格式：10:100.00:20;30:300.00:0;20:200.00:10
        String[] items = frequencyCog.getFreItems().split(";");
        Arrays.sort(items, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.parseInt(o1.split(":")[1]) - Integer.parseInt(o2.split(":")[1]);
            }
        });
        frequencyCog.setFreItems(StringUtils.join(items,";"));
        return frequencyCog;
    }


    public VpsVcodeActivityFrequencyCog findById(String infoKey) {
        return frequencyActivityDao.findById(infoKey);
    }
    
    /**
     * 检验名称是否重复
     * @param infoKey		主键（修改页面需传递）
     * @param bussionName	名称
     * @return
     */
    public String checkBussionName(String infoKey, String bussionName) {
        return checkBussionName("vps_vcode_activity_frequency_cog", "info_key", infoKey, "FREQUENCY_NAME", bussionName);
    }

    /**
     * 校验扫码活动指定时间内是否参与有效的频次活动
     * @param infoKey
     * @param vcodeActivityKeys
     * @param beginDate
     * @param endDate
     * @return
     */
    public String validCogByActivityKey(String infoKey, String vcodeActivityKeys, String beginDate, String endDate) {
        String errMsg = "";
        List<String> vcodeActivityKeyLst = Arrays.asList(vcodeActivityKeys.split(","));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("infoKey", infoKey);
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        List<VpsVcodeActivityFrequencyCog> frequencyCogLst = null;
        for (String activityKey : vcodeActivityKeyLst) {
            if (StringUtils.isBlank(activityKey)) continue;
            map.put("vcodeActivityKey", activityKey);
            frequencyCogLst = frequencyActivityDao.validCogByVcodeActivityKey(map);
            if (frequencyCogLst != null && frequencyCogLst.size() > 0) {
                for (VpsVcodeActivityFrequencyCog item : frequencyCogLst) {
                    if (!item.getInfoKey().equals(infoKey)) {
                        VcodeActivityCog cog = vcodeActivityService.findById(activityKey);
                        errMsg = cog.getVcodeActivityName() + "在此时间段与频次" + item.getFrequencyName() + "有冲突";
                        break;
                    }
                }
            }
        }
        return errMsg;
    }

    public void deletePerhundredCog(String infoKey, String userKey) throws Exception {
        VpsVcodeActivityFrequencyCog oldFrequencyCog = findById(infoKey);
        if(null != oldFrequencyCog){
            // 删除缓存
            String[] oldVcodeActivityKeyArr = oldFrequencyCog.getVcodeActivityKey().split(",");
            for (String oldVcodeActivityKey : oldVcodeActivityKeyArr) {
                String cacheKeyStr = CacheKey.cacheKey.vcode
                        .KEY_VCODE_ACTIVITY_FREQUENCY_COG
                        + Constant.DBTSPLIT + oldVcodeActivityKey + Constant.DBTSPLIT + DateUtil.getDate();
                CacheUtilNew.removeGroupByKey(cacheKeyStr);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("infoKey", infoKey);
            map.put("updateUser", userKey);
            frequencyActivityDao.deleteById(map);
        }
    }
}
