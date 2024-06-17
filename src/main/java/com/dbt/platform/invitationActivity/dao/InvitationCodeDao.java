package com.dbt.platform.invitationActivity.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.invitationActivity.bean.VpsVcodeInvitationCode;
import com.dbt.platform.invitationActivity.bean.VpsVcodeInvitationOrder;
import org.apache.ibatis.annotations.Param;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Resource
public interface InvitationCodeDao extends IBaseDao<VpsVcodeInvitationCode> {

    /**
     * 根据查询条件查询符合记录
     * @param map
     * @return
     */
    List<VpsVcodeInvitationCode> queryForList(Map<String, Object> map);

    /**
     * 根据查询条件查询符合记录总数
     * @param map
     * @return
     */
    int queryForCount(Map<String, Object> map);

    /**
     * 下载注册码
     * @param map
     * @return
     */
    List<VpsVcodeInvitationCode> uploadCode(Map<String, Object> map);

    /**
     * 根据infokey查询注册码信息
     * @param infoKey
     * @return
     */
    VpsVcodeInvitationCode findById(String infoKey);

    /**
     * 根据订单主键删除邀请码
     * @param orderKey
     */
    void removeCodeByOrderKey(String orderKey);

    /**
     * 根据活动主键删除邀请码
     * @param activityKey
     */
    void removeCodeByActivityKey(String activityKey);

    /**
     * 根据活动主键删除邀请码
     * @param activityKey
     */
    void removeCodeByActivityKeyAndOrderKey(@Param("activityKey") String activityKey, @Param("orderKey") String orderKey);

    /**
     * 根据活动主键以及邀请码编码前缀查询最大编码
     * @param map
     * @return
     */
    String queryMaxCode(Map<String, Object> map);

    /**
     * 邀请码入库
     * @param map
     */
    void writeQrcodeToData(Map<String, Object> map);
}
