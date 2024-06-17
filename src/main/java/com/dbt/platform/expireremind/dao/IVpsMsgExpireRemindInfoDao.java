package com.dbt.platform.expireremind.dao;


import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.expireremind.bean.VpsMsgExpireRemindInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IVpsMsgExpireRemindInfoDao extends IBaseDao<VpsMsgExpireRemindInfo > {

    /**
     * 根据ID删除
     * @param infoKey
     * @return
     */
    int deleteByPrimaryKey(String infoKey);
    /**
     * 创建一条提醒记录
     * @param record
     * @return
     */
    int insert(VpsMsgExpireRemindInfo record);
    /**
     * 合计黑名单总数
     * @param record
     * @return
     */
    int insertSelective(VpsMsgExpireRemindInfo record);

    /**
     * 根据主键查询一条记录
     * @param infoKey
     * @return
     */
    VpsMsgExpireRemindInfo selectByPrimaryKey(String infoKey);

    /**
     * 更新记录信息
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(VpsMsgExpireRemindInfo record);

    /**
     * 合计黑名单总数
     * @param record
     * @return
     */
    int updateByPrimaryKey(VpsMsgExpireRemindInfo record);

    /**
     * 合计黑名单总数
     * @param
     * @return
     */
    List<VpsMsgExpireRemindInfo> selectByActivityParamer(HashMap map);

    /**
     * 根据参数查询
     * @param
     * @return
     */
   VpsMsgExpireRemindInfo selectByTypeAndActivityId(VpsMsgExpireRemindInfo record);

    /**
     * 统计条件提醒信息条数。
     */
    int  selectMsgExpireRemindTotalCondition(HashMap<String,Object> map);

    /**
     * 统计提醒信息条数。
     */
    int  selectMsgExpireRemindTotal();

    /***
     *
     *是否已读
     */
    void updateStatusForRead(Map key);

    /***
     *
     *   是否置顶
     */

    void stickMsg(Map<String, Object> map);

    /** *
     *根据条件查询
     */

    List<VpsMsgExpireRemindInfo> selectByActivityParamerCondition(HashMap<String, Object> map);

    /***
     * 查询超过三十天未更新的信息记录
     */
    List<String> findIdsByTime();

    /***
     * 标识为已删除
     */
   void  updateDelByInfoKey(Map key);

    /***
     * 删除信息
     */
    void  delByInfoKey(Map key);
    
}