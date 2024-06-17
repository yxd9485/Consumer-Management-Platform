package com.dbt.platform.activity.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VcodePrizeBasicInfo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 实物奖dao
 * @Author bin.zhang
 **/
public interface IVcodeActivityBigPrizeDao extends IBaseDao<VcodePrizeBasicInfo> {
    /**
     * 获取实物奖列表
     */
    List<VcodePrizeBasicInfo> queryForLst(Map<String, Object> map);

    int queryForCount(Map<String, Object> map);

    void writePrize(VcodePrizeBasicInfo vcodePrizeBasicInfo);

    void delPrize(String infoKey);

    void updatePrizeInfo(VcodePrizeBasicInfo vcodePrizeBasicInfo);

    VcodePrizeBasicInfo findBigPrizeByKey(String infoKey);
    VcodePrizeBasicInfo findBigPrizeByPrizeNo(String prizeNo);

    List<VcodePrizeBasicInfo> queryAllBigPrize();
    
    /**
     * 获取门店绑定的实物奖
     */
    List<VcodePrizeBasicInfo> queryTerminalPrizeLst(Map<String, Object> map);
    int queryTerminalPrizeCount(Map<String, Object> map);

}

