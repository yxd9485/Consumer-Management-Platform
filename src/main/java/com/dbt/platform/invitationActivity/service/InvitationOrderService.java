package com.dbt.platform.invitationActivity.service;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.invitationActivity.bean.VpsVcodeInvitationActivityCog;
import com.dbt.platform.invitationActivity.bean.VpsVcodeInvitationOrder;
import com.dbt.platform.invitationActivity.dao.InvitationOrderDao;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class InvitationOrderService extends BaseService<VpsVcodeInvitationOrder> {
    @Autowired
    private InvitationOrderDao invitationOrderDao;
    @Autowired
    private InvitationCodeService invitationCodeService;

    public List<VpsVcodeInvitationOrder> queryForList(VpsVcodeInvitationOrder queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        return invitationOrderDao.queryForList(map);
    }

    public int queryForCount(VpsVcodeInvitationOrder queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return invitationOrderDao.queryForCount(map);
    }

    /**
     * 新增订单
     * @param order
     */
    public void addOrder(VpsVcodeInvitationOrder order, SysUserBasis currentUser) {
        order.setInfoKey(UUID.randomUUID().toString());
        order.setCompanyKey(currentUser.getCompanyKey());
        order.setStatus("0");
        order.setDeleteFlag("0");
        order.setCreateUser(currentUser.getUserName());
        order.setCreateTime(DateUtil.getDateTime());
        order.setUpdateUser(currentUser.getUserName());
        order.setUpdateTime(DateUtil.getDateTime());
        invitationOrderDao.create(order);
    }

    /**
     * 修改订单(只有未生成邀请码的订单可以修改)
     * @param order
     */
    public String editOrder(VpsVcodeInvitationOrder order, SysUserBasis currentUser){
        VpsVcodeInvitationOrder dbOrder = findById(order.getInfoKey());
        if(dbOrder != null){
            if (!"0".equals(dbOrder.getStatus())){
                return "1".equals(dbOrder.getStatus()) ? "该订单正在生成邀请码，订单修改失败。" : "该订单邀请码已经生成，订单修改失败。";
            }
            VpsVcodeInvitationOrder afootOrder = findAfoot(order.getActivityKey());
            if (afootOrder!=null){
                return "活动内有订单正在生成邀请码，订单修改失败。";
            }
            VpsVcodeInvitationOrder tempOrder = new VpsVcodeInvitationOrder();
            tempOrder.setInfoKey(order.getInfoKey());
            tempOrder.setOrderName(order.getOrderName());
            tempOrder.setQrcodeType(order.getQrcodeType());
            tempOrder.setQrcodeNum(order.getQrcodeNum());
            tempOrder.setUpdateUser(currentUser.getUserName());
            tempOrder.setUpdateTime(DateUtil.getDateTime());
            invitationOrderDao.update(tempOrder);
        }else{
            return "无此订单，订单修改失败。";
        }
        return null;
    }

    /**
     * 更新邀请码订单
     * @param order
     */
    public void updateOrder(VpsVcodeInvitationOrder order){
        invitationOrderDao.update(order);
    }

    /**
     * 删除订单
     * @param infoKey
     */
    public String deleteOrder(String infoKey){
        VpsVcodeInvitationOrder order = findById(infoKey);
        if(order != null){
            if (!"0".equals(order.getStatus())){
                return "1".equals(order.getStatus()) ? "该订单邀请码正在生成中，订单删除失败。" : "该订单邀请码已经生成，订单删除失败。";
            }
            VpsVcodeInvitationOrder afootOrder = findAfoot(order.getActivityKey());
            if (afootOrder!=null){
                return "活动内有订单正在生成邀请码，订单删除失败。";
            }
            invitationOrderDao.removeOrderByInfoKey(infoKey);
            invitationCodeService.removeCodeByActivityKeyAndOrderKey(order.getActivityKey(),infoKey);
        }else{
            return "无此订单，订单删除失败。";
        }
        return null;
    }

    /**
     * 物理删除订单根据活动主键
     * @param activityKey
     */
    public void removeOrderByActivityKey(String activityKey){
        invitationOrderDao.removeOrderByActivityKey(activityKey);
    }

    /**
     * 根据活动主键查询活动
     * @param infoKey
     * @return
     */
    public VpsVcodeInvitationOrder findById(String infoKey) {
        return invitationOrderDao.findById(infoKey);
    }

    /**
     * 检查活动名称
     * @param currentUser
     * @param checkName
     * @param infoKey
     * @return
     */
    public String checkName(SysUserBasis currentUser, String checkName, String infoKey, String activityKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("companyKey", currentUser.getCompanyKey());
        map.put("checkName", checkName);
        map.put("infoKey", infoKey);
        map.put("activityKey", activityKey);
        VpsVcodeInvitationOrder resultData = invitationOrderDao.checkName(map);
        if(resultData==null){
            return "1";
        }
        return "0";
    }

    /**
     * 查询正在生成邀请码的订单
     * @param activityKey
     * @return
     */
    public VpsVcodeInvitationOrder findAfoot(String activityKey) {
        return invitationOrderDao.findAfoot(activityKey);
    }
}
