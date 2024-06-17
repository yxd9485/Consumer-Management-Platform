package com.dbt.smallticket.dao;



import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.smallticket.bean.VpsTicketLotteryRecord;

/**
 * 小票每晚参与定时抽奖用户中奖概率表Dao
 */
public interface IVpsTicketLotteryRecordDao extends IBaseDao<VpsTicketLotteryRecord> {

    /**
     * 更新用户定时抽奖的参与情况
     * @param map keys:userKey、lotteryDate、channelType
     * @return
     */
    public int updateChannelNum(Map<String, Object> map);
    
}
