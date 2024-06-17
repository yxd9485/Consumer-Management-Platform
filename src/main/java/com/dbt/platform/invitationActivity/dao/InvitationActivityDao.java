package com.dbt.platform.invitationActivity.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.invitationActivity.bean.VpsVcodeInvitationActivityCog;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Resource
public interface InvitationActivityDao extends IBaseDao<VpsVcodeInvitationActivityCog> {

    /**
     * 根据查询条件查询符合记录
     * @param map
     * @return
     */
    List<VpsVcodeInvitationActivityCog> queryForList(Map<String, Object> map);

    /**
     * 根据查询条件查询符合记录总数
     * @param map
     * @return
     */
    int queryForCount(Map<String, Object> map);

    /**
     * 检查活动名称
     * @param map
     * @return
     */
    VpsVcodeInvitationActivityCog checkName(Map<String, Object> map);

    /**
     * 检验活动日期及地区
     * @param map
     * @return
     */
    List<VpsVcodeInvitationActivityCog> checkDateAndArea(Map<String, Object> map);

    /**
     * 根据infokey查询活动
     * @param infoKey
     * @return
     */
    VpsVcodeInvitationActivityCog findById(String infoKey);

    /**
     * 查询已用flag
     * @return
     */
    List<VpsVcodeInvitationActivityCog> checkFlag();
}
