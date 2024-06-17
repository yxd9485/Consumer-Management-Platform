package com.dbt.platform.waitActivation.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.HttpReq;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.util.UUIDTools;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.waitActivation.bean.VpsWaitActivationPrizeCog;
import com.dbt.platform.waitActivation.bean.VpsWaitActivationPrizeSku;
import com.dbt.platform.waitActivation.dao.VpsWaitActivationPrizeCogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 待激活红包活动配置服务类
 */
@Service
public class WaitActivationPrizeService {

    @Autowired
    private SkuInfoService skuInfoService;

    @Resource
    VpsWaitActivationPrizeCogDao vpsWaitActivationPrizeCogDao;

    /**
     * 查询所有sku列表
     * */
    public  List<SkuInfo> loadAllSku(String companyKey){


        //消费者内的SKU
        List<SkuInfo> skuList = skuInfoService.loadSkuListByCompany(companyKey);

        //终端促销companyKey
        String vmtsCompanyKey = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_COMPANY_INFO, DatadicKey.filterCompanyInfo.VMTS_COMPANY_KEY);
        String vmtsURL = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_HTTP_URL, DatadicKey.filterHttpUrl.VMTS_INTERFACE_URL)+ "sku/querySkuList";

        //查询终端的SKU
        Map<String, String> params = new HashMap<String, String>();
        //固定查询青啤总部的sku
        params.put("companyKey", "20220114,"+vmtsCompanyKey);
        JSONObject resJson = HttpReq.handerHttpReq(vmtsURL , JSON.toJSONString(params));
        JSONArray skuArray =  resJson.getJSONObject("reply").getJSONArray("skuList");
        List<SkuInfo> vmtsSkuList = JSONArray.parseArray(JSON.toJSONString(skuArray),SkuInfo.class);
        //终端SKU的名字处理
        for (SkuInfo vmtsSku:vmtsSkuList){
            //区分标识
            if ("20220114".equals(vmtsSku.getCompanyKey())){
                vmtsSku.setSkuName("(青啤总部)"+vmtsSku.getSkuName());
            }else{
                vmtsSku.setSkuName("(终端)"+vmtsSku.getSkuName());
            }
            vmtsSku.setSkuKey("vmts:"+vmtsSku.getSkuKey());

        }

        //合并，排序
        List<SkuInfo> finalList = new ArrayList<>();
        finalList.addAll(skuList);
        finalList.addAll(vmtsSkuList);
        Collections.sort(finalList, new Comparator<SkuInfo>() {
            @Override
            public int compare(SkuInfo o1, SkuInfo o2) {
                return o1.getSkuName().compareTo(o2.getSkuName());
            }
        });

        return finalList;
    }


    /**
     * 查询列表
     * */
    public List<VpsWaitActivationPrizeCog> queryForList(VpsWaitActivationPrizeCog queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("queryBean", queryBean);
        queryMap.put("pageInfo", pageInfo);
        return vpsWaitActivationPrizeCogDao.queryForLst(queryMap);
    }

    /**
     * 查询所有有效配置
     * */
    public List<VpsWaitActivationPrizeCog> queryAll() {
        return vpsWaitActivationPrizeCogDao.queryAll();
    }

    /**
     * 查询总数-分页用
     * */
    public int queryForCount(VpsWaitActivationPrizeCog queryBean) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("queryBean", queryBean);
        return vpsWaitActivationPrizeCogDao.queryForCount(queryMap);
    }

    //新增待激活红包配置
    public void insertWaitActivationPrizeCog(VpsWaitActivationPrizeCog vpsWaitActivationPrizeCog, SysUserBasis currentUser) {

        //获取主键
        String prizeKey = UUIDTools.getInstance().getUUID();
        vpsWaitActivationPrizeCog.setCreateTime(DateUtil.getDateTime());
        vpsWaitActivationPrizeCog.setCreateUser(currentUser.getUserName());
        vpsWaitActivationPrizeCog.setPrizeKey(prizeKey);
        //新增配置
        vpsWaitActivationPrizeCogDao.insert(vpsWaitActivationPrizeCog);
        //获取选择的SkuList,去重
        Set<String> skuSet = new HashSet<> ( Arrays.asList(vpsWaitActivationPrizeCog.getSkus()));
        List<String> skuList = new ArrayList<>(skuSet);
        for (String sku :skuList){
            //拆分sku名字和KEY
            String[] skuInfo = sku.split("@@@");
            //新增关联的SKU关系
            vpsWaitActivationPrizeCogDao.insertSkuList(skuInfo[0],skuInfo[1],skuInfo[2],prizeKey,currentUser.getUserName());
        }


    }

    //编辑待激活红包配置
    public void updateWaitActivationPrizeCog(VpsWaitActivationPrizeCog vpsWaitActivationPrizeCog, SysUserBasis currentUser) {

        //设置更新信息
        vpsWaitActivationPrizeCog.setUpdateTime(DateUtil.getDateTime());
        vpsWaitActivationPrizeCog.setUpdateUser(currentUser.getUserName());
        //新增配置
        vpsWaitActivationPrizeCogDao.update(vpsWaitActivationPrizeCog);

        //删除原有关联
        vpsWaitActivationPrizeCogDao.clearByPrizeKey(vpsWaitActivationPrizeCog.getPrizeKey());
        //获取选择的SkuList,去重
        Set<String> skuSet = new HashSet<> ( Arrays.asList(vpsWaitActivationPrizeCog.getSkus()));
        List<String> skuList = new ArrayList<>(skuSet);
        for (String sku :skuList){
            //拆分sku名字和KEY
            String[] skuInfo = sku.split("@@@");
            //新增关联的SKU关系
            vpsWaitActivationPrizeCogDao.insertSkuList(skuInfo[0],skuInfo[1],skuInfo[2],vpsWaitActivationPrizeCog.getPrizeKey(),currentUser.getUserName());
        }

    }


    public VpsWaitActivationPrizeCog selectByKey(String prizeKey) {

        return vpsWaitActivationPrizeCogDao.selectByKey(prizeKey);

    }

    public List<VpsWaitActivationPrizeSku> getSkuKeysByPrizeKey(String prizeKey){

        return vpsWaitActivationPrizeCogDao.selectSkuKeysByPrizeKey(prizeKey);

    }

}
