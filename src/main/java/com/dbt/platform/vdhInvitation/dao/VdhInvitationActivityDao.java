package com.dbt.platform.vdhInvitation.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.vdhInvitation.bean.VpsVcodeVdhInvitationActivityCog;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Resource
public interface VdhInvitationActivityDao extends IBaseDao<VpsVcodeVdhInvitationActivityCog> {

    /**
     * 根据查询条件查询活动
     * @param map
     * @return
     */
    List<VpsVcodeVdhInvitationActivityCog> queryForList(Map<String, Object> map);

    /**
     * 分页总数
     * @param map
     * @return
     */
    int queryForCount(Map<String, Object> map);

    /**
     * 检查活动名称
     * @param map
     * @return
     */
    VpsVcodeVdhInvitationActivityCog checkName(Map<String, Object> map);

    /**
     * 根据id查询活动
     * @param infoKey
     * @return
     */
    VpsVcodeVdhInvitationActivityCog findById(String infoKey);
}
