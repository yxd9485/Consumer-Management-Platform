package com.dbt.platform.marquee.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.marquee.bean.MarqueeCogInfo;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

public interface MarqueeInfoDao extends IBaseDao<MarqueeCogInfo> {
    public List<MarqueeCogInfo> queryForLst(Map<String, Object> map);

    int queryForCount(Map<String, Object> map);

    // 判断添加或修改的跑马灯配置是否已存在
    boolean existMarquee(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("winType") String winType, @Param("infoKey") String infoKey);

    void deleteMarqueeInfoByCogInfo(@Param("infoKey") String infoKey,@Param("infoKeyList")   List<String> infoKeyList);

    List<String> queryNotDeleteMarqueeInfo(@Param("infoKey") String infoKey,@Param("limit") int limit);
}
