package com.dbt.platform.vdhInvitation.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.vdhInvitation.bean.VpsVcodeVdhInvitationCode;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Resource
public interface VdhInvitationCodeDao extends IBaseDao<VpsVcodeVdhInvitationCode> {

    /**
     * 根据查询条件查询订单
     * @param map
     * @return
     */
    List<VpsVcodeVdhInvitationCode> queryForList(Map<String, Object> map);

    /**
     * 分页总数
     * @param map
     * @return
     */
    int queryForCount(Map<String, Object> map);

    /**
     * 查询邀请码编码前缀最大编码
     * @param map
     * @return
     */
    String queryMaxCode(Map<String, Object> map);

    /**
     * 邀请码入库
     * @param map
     */
    void writeQrcodeToData(Map<String, Object> map);

    /**
     * 下载注册码
     * @param map
     * @return
     */
    List<VpsVcodeVdhInvitationCode> uploadCode(Map<String, Object> map);

    /**
     * 根据id查询注册码
     * @param infoKey
     * @return
     */
    VpsVcodeVdhInvitationCode findById(String infoKey);

    /**
     * 根据订单主键物理删除邀请码
     * @param orderKey
     */
    void removeCodeByOrderKey(String orderKey);
}
