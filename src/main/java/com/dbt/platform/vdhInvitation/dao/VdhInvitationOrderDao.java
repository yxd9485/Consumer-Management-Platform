package com.dbt.platform.vdhInvitation.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.vdhInvitation.bean.VpsVcodeVdhInvitationOrder;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Resource
public interface VdhInvitationOrderDao extends IBaseDao<VpsVcodeVdhInvitationOrder> {

    /**
     * 根据查询条件查询订单
     * @param map
     * @return
     */
    List<VpsVcodeVdhInvitationOrder> queryForList(Map<String, Object> map);

    /**
     * 分页总数
     * @param map
     * @return
     */
    int queryForCount(Map<String, Object> map);

    /**
     * 检查邀请码订单名称
     * @param map
     * @return
     */
    VpsVcodeVdhInvitationOrder checkName(Map<String, Object> map);

    /**
     * 根据id查询邀请码订单
     * @param infoKey
     * @return
     */
    VpsVcodeVdhInvitationOrder findById(String infoKey);

    /**
     * 查询正在生成邀请码的订单
     * @return
     */
    VpsVcodeVdhInvitationOrder findAfoot();

    /**
     * 物理删除订单根据邀请码订单主键
     * @param infoKey
     */
    void removeOrderByInfoKey(String infoKey);

}
