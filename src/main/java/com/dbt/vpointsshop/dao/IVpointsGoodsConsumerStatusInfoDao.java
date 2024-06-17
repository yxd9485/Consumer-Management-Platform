package com.dbt.vpointsshop.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.vpointsshop.bean.VpointsGoodsConsumerStatusInfo;

/**
 * 商品到货通知
 */
public interface IVpointsGoodsConsumerStatusInfoDao extends IBaseDao<VpointsGoodsConsumerStatusInfo> {

    /**
     * 依据商品id获取要通知的用户
     * 
     * @param map keys:goodsId
     * @return
     */
    public List<VpointsGoodsConsumerStatusInfo> queryArrivalNoticeByGoodsId(Map<String, Object> map);
    
    /**
     * 依据商品id更新订阅状态
     * 
     * @param map keys:goodsId、arrivalNoticeFlag
     * @return
     */
    public void updateArrivalNoticeFlag(Map<String, Object> map);
    
    /**
     * 获取商城秒杀预约提醒记录
     * 
     * @param map keys:remindStartTime、remindEndTime
     * @return
     */
    public List<VpointsGoodsConsumerStatusInfo> querySecKillRemind(Map<String, Object> map);
    
    /**
     * 批量更新秒杀发送状态
     */
    public void updateSecKillRemindFlagForSend(List<String> infoKeyLst);
    
    /** 商城拼团活动提醒 **/
	public List<VpointsGoodsConsumerStatusInfo> queryGroupActivityRemind(Map<String, Object> map);
	
	/** 商城秒杀活动提醒 **/
	public List<VpointsGoodsConsumerStatusInfo> querySeckillActivityRemind(Map<String, Object> map);
}
