package com.dbt.platform.invitationActivity.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.invitationActivity.bean.VpsVcodeInvitationOrder;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Resource
public interface InvitationOrderDao extends IBaseDao<VpsVcodeInvitationOrder> {

    /**
     * 根据查询条件查询符合记录
     * @param map
     * @return
     */
    List<VpsVcodeInvitationOrder> queryForList(Map<String, Object> map);

    /**
     * 根据查询条件查询符合记录总数
     * @param map
     * @return
     */
    int queryForCount(Map<String, Object> map);

    /**
     * 检查订单名称
     * @param map
     * @return
     */
    VpsVcodeInvitationOrder checkName(Map<String, Object> map);

    /**
     * 根据infokey查询订单
     * @param infoKey
     * @return
     */
    VpsVcodeInvitationOrder findById(String infoKey);

    /**
     * 查询正在生成码的订单
     * @param activityKey
     * @return
     */
    VpsVcodeInvitationOrder findAfoot(String activityKey);

    /**
     * 物理删除订单根据订单主键
     * @param infoKey
     */
    void removeOrderByInfoKey(String infoKey);

    /**
     * 物理删除订单根据活动主键
     * @param activityKey
     */
    void removeOrderByActivityKey(String activityKey);
}
