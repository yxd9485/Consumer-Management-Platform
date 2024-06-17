package com.dbt.platform.job;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.vpointsshop.bean.VpointsGoodsInfo;
import com.dbt.vpointsshop.service.VpointsGoodsService;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * 更新展示销量
 */
@Service("UpdateGoodShowSalesJob")
public class UpdateGoodShowSalesJob {
    @Autowired
    private VpointsGoodsService goodService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 更新销量
     * @throws InterruptedException
     */
    public void updateShowNum() throws InterruptedException {
        // 获取job执行的省区
        DbContextHolder.clearDBType();
        Set<String> nameList = null;
        String projectServerNames = DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.PROJECT_JOB,
                DatadicKey.ProjectJob.UPDATE_SHOWNUM);
        if(org.apache.commons.lang.StringUtils.isBlank(projectServerNames)) return;

        if(!"ALL".equals(projectServerNames)){
            nameList = new HashSet<String>(Arrays.asList(projectServerNames.split(",")));
        }else{
            nameList = ((Map<String, ServerInfo>) RedisApiUtil.getInstance()
                    .getObject(false, CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO)).keySet();
        }
        // 循环执行任务
        for (String projectServerName : nameList) {
            this.updateShowNum(projectServerName);
            Thread.sleep(500);
        }

    }
    @RequestMapping("/updateShowNum")
    private void updateShowNum(String projectServerName) {
        DbContextHolder.setDBType(projectServerName);
        List<VpointsGoodsInfo> goodsInfoList =  goodService.getAllGoodsForJob();
        //获取数据字典的配置随机区间
        if (StringUtils.isBlank(DatadicUtil.getDataDicValue(
                DatadicUtil.dataDicCategory.DATA_CONSTANT_CONFIG,
                DatadicUtil.dataDic.dataConstantConfig.SHOW_NUM_INTERVAL))){
            log.warn("请配置展示销量随即递减区间！！");
            return;
        }
       String interval = DatadicUtil.getDataDicValue(
                DatadicUtil.dataDicCategory.DATA_CONSTANT_CONFIG,
                DatadicUtil.dataDic.dataConstantConfig.SHOW_NUM_INTERVAL);
        String randNum = interval;
        Map<String, String> map = new HashMap<>();
        if(interval.contains(",")){
            String[] showNumInterval =
                    interval.split(",");
            if(showNumInterval[1].equals(showNumInterval[0])){
                randNum = String.valueOf(Integer.valueOf(showNumInterval[0]));
            }else {
                randNum = String.valueOf(RandomUtils.nextInt(Integer.valueOf(showNumInterval[0]) , Integer.valueOf(showNumInterval[1]) + 1));
            }
        }
        for(VpointsGoodsInfo g : goodsInfoList){
            //如果商品进30天展示数量不为空 且 和设置的展示销量基数的差值大于递增递减数，则更新
            try{
                if(StringUtils.isNotBlank(g.getShowSales()) && (Integer.valueOf(g.getShowSales()).compareTo(Integer.valueOf(g.getShowSalesBase())) > 0)  && (Integer.valueOf(g.getShowSales()) -
                        Integer.valueOf(g.getShowSalesBase()).compareTo(Integer.valueOf(randNum)))> 0 && (Integer.valueOf(g.getShowSales()).compareTo(Integer.valueOf(randNum)) > 0)){
                    map.put("goodsId", g.getGoodsId());
                    map.put("interval", randNum);
                    goodService.updateGoodShowSales(map);
                }
            }catch (Exception e){
               System.out.println(g.getGoodsId());
            }


        }
    }

    public static void main(String[] args) {
        String interval = "10,10";
        String showNumInterval[] =
                interval.split(",");
        Random r = new Random();
        Integer c = Integer.valueOf(showNumInterval[1]) - Integer.valueOf(showNumInterval[0]);
        String randNum = String.valueOf(r.nextInt(1));
        System.out.println(randNum);
    }
}
