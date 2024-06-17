package com.dbt.platform.cityrank.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.util.StringUtil;
import com.dbt.platform.cityrank.bean.VpsVcodeMonthCityRankHistory;
import com.dbt.platform.cityrank.dao.IVpsVcodeMonthCityRankHistoryDao;

/**
 * 城市酒王月度历史排名记录表Service
 */
@Service
public class VpsVcodeMonthCityRankHistoryService 
                        extends BaseService<VpsVcodeMonthCityRankHistory> {
    @Autowired
    private IVpsVcodeMonthCityRankHistoryDao rankHistoryDao;
    
    // lua脚本ID
    private static String rankScript=""; 
    
    /**
     * 持久化上期排名信息
     * 
     * @param rankMonth
     * @param cityName
     * @param perRankNum
     */
    public void executeMonthCityRankHistory(String rankMonth, String cityName, int perRankNum) {
        
        // 获取排名数据
        List<VpsVcodeMonthCityRankHistory> rankHistoryLst = this.queryRankList(cityName, rankMonth, 0, perRankNum);
        
        // 进入排名人数符合揭榜人数
        if (rankHistoryLst != null) {
            if (rankHistoryLst.size() == perRankNum) {
                
                // 批量插入历史排名数据
                Map<String, Object> map = new HashMap<>();
                map.put("rankMonthLst", rankHistoryLst);
                rankHistoryDao.batchWrite(map);
                
                // TODO 依据数据字典排名与大奖关系插入大奖记录表

                log.error("rankMonth:" + rankMonth + " cityName:" + cityName + " perRankNum:" + perRankNum + " --- 排名信息持久化成功");
            } else {
                log.error("rankMonth:" + rankMonth + " cityName:" + cityName + " perRankNum:" + perRankNum + " --- 未达到揭榜人数");
            }
            log.info(JSON.toJSONString(rankHistoryLst));
        } else {
            log.error("rankMonth:" + rankMonth + " cityName:" + cityName + " perRankNum:" + perRankNum + " --- 无月度排名数据");
        }
        try {
            CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.MonthCityRank.VPS_VCODE_MONTHCITYRANK_HISTORY + "_" + cityName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取酒王排行榜
     */
    private List<VpsVcodeMonthCityRankHistory> queryRankList(String cityName, String rankMonth, int currPage, int pageSize) {
        int startIndex = currPage > 0 ? currPage * pageSize - pageSize : 0;// 开始索引数
        int endIndex=currPage > 0 ? currPage * pageSize-1 : pageSize-1;// 结束索引数
         // 季度后缀
        //从排序集合rankTest内获取前N名的用户及积分
        VpsVcodeMonthCityRankHistory record;
        List<VpsVcodeMonthCityRankHistory> list=new ArrayList<VpsVcodeMonthCityRankHistory>();
        String lua="local json=''"+         //定义返回值
                "   local msg"+             //定义头像昵称变量
                "   local ranks=redis.call('zrevrange',KEYS[1],ARGV[1],ARGV[2],'WITHSCORES')"+//获取指定月排名区间内的userKey及积分
                "   for key,value in pairs(ranks) do"+//循环
                "       if key%2==1 then"+//获取20名时，返回值为40个 单数为userKey，双数为积分
                "           msg=redis.call('hexists',KEYS[2],value)"+//根据userKey获取用户头像昵称
                "           if msg==1 then "+
                "               json=json..value..','..redis.call('hget',KEYS[2],value)..','"+//拼接字符串 value(userKey) msg(头像及昵称)
                "           else "+
                "               json=json..value..','..','..','"+
                "           end"+
                "       else"+
                "           json=json..value..';'"+//拼接字符串value(积分)
                "       end "+
                "   end"+
                " return json";//返回
        try {
            if(rankScript.equals("")){
                rankScript=RedisApiUtil.getInstance().scriptLoad(lua);
            }

            // 月度排名cacheKey, serverName:monthCityRank:yyyyMM:cityName
            String redisForRankKey = RedisApiUtil.getProjectServerName() + RedisApiUtil
                                .CacheKey.Ranking.MONTH_CITY_RANK + rankMonth + ":" + StringUtil.encodeUnicode(cityName);
            String userInfoKey = RedisApiUtil.getProjectServerName() + RedisApiUtil.CacheKey.Ranking.QUARTER_RANK_USEINFO; // 用户头像昵称缓存key
            String json = null;
            json = RedisApiUtil.getInstance().evalsha(rankScript, 2, redisForRankKey,userInfoKey,startIndex+"",endIndex+"").toString();
            if("0".equals(json)){
                rankScript=RedisApiUtil.getInstance().scriptLoad(lua);
                json = RedisApiUtil.getInstance().evalsha(rankScript, 2, redisForRankKey,userInfoKey,startIndex+"",endIndex+"").toString();
            }
            
            if(StringUtils.isNotBlank(json)){
                String score = null;
                String [] jsonSplit=json.split(";");
                for (int i = 0; i < jsonSplit.length; i++) {
                    String jsonUser=jsonSplit[i];
                    record=new VpsVcodeMonthCityRankHistory();
                    String[] jsonUserSplit=jsonUser.split(",");
                    record.setCityName(cityName);
                    record.setRankMonth(rankMonth);
                    record.setUserKey(jsonUserSplit[0]);//userKey
                    record.setHeadImgUrl(jsonUserSplit[1]);
                    record.setNickName(jsonUserSplit[2]);
                    if(i==(jsonSplit.length-1)&&jsonUserSplit[3].indexOf("_")>-1){
                        String[] jsonLast=jsonUserSplit[3].split("_");
                        // 未进入排名
                        if ("0".equals(jsonLast[1])) {
                            record.setRankNum(Integer.parseInt(jsonLast[1]));// 排行
                        } else {
                            record.setRankNum(Integer.parseInt(jsonLast[1])+1);// 排行
                        }
                        score = jsonLast[0];
                        if (score.length() > 7) {
                            score = score.substring(0, score.length() - 7);
                        }
                        record.setScanNum(Integer.parseInt(score));// 积分
                    }else{
                        record.setRankNum(startIndex+1);// 排行
                        score = jsonUserSplit[3];
                        if (score.length() > 7) {
                            score = score.substring(0, score.length() - 7);
                        }
                        record.setScanNum(Integer.parseInt(score));// 积分
                    }
                    list.add(record);
                    startIndex++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
