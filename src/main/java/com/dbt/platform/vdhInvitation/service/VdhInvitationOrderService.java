package com.dbt.platform.vdhInvitation.service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.invitationActivity.bean.VpsVcodeInvitationOrder;
import com.dbt.platform.invitationActivity.dao.InvitationOrderDao;
import com.dbt.platform.invitationActivity.service.InvitationCodeService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.vdhInvitation.bean.VpsVcodeVdhInvitationOrder;
import com.dbt.platform.vdhInvitation.dao.VdhInvitationOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class VdhInvitationOrderService extends BaseService<VpsVcodeVdhInvitationOrder> {
    @Autowired
    private VdhInvitationOrderDao vdhInvitationOrderDao;
    @Autowired
    private VdhInvitationCodeService vdhInvitationCodeService;

    /**
     * 根据查询条件查询订单
     * @param queryBean
     * @param pageInfo
     * @return
     */
    public List<VpsVcodeVdhInvitationOrder> queryForList(VpsVcodeVdhInvitationOrder queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        return vdhInvitationOrderDao.queryForList(map);
    }

    /**
     * 分页总数
     * @param queryBean
     * @return
     */
    public int queryForCount(VpsVcodeVdhInvitationOrder queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return vdhInvitationOrderDao.queryForCount(map);
    }

    /**
     * 新增订单
     * @param order
     */
    public void addOrder(VpsVcodeVdhInvitationOrder order, SysUserBasis currentUser) {
        order.setInfoKey(UUID.randomUUID().toString());
        order.setCompanyKey(currentUser.getCompanyKey());
        order.setStatus("0");
        order.setDeleteFlag("0");
        order.setCreateUser(currentUser.getUserName());
        order.setCreateTime(DateUtil.getDateTime());
        order.setUpdateUser(currentUser.getUserName());
        order.setUpdateTime(DateUtil.getDateTime());
        vdhInvitationOrderDao.create(order);
    }

    /**
     * 修改订单(只有未生成邀请码的订单可以修改)
     * @param order
     */
    public String editOrder(VpsVcodeVdhInvitationOrder order, SysUserBasis currentUser){
        VpsVcodeVdhInvitationOrder dbOrder = findById(order.getInfoKey());
        if(dbOrder != null){
            if (!"0".equals(dbOrder.getStatus())){
                return "1".equals(dbOrder.getStatus()) ? "该订单正在生成邀请码，订单修改失败。" : "该订单邀请码已经生成，订单修改失败。";
            }
            VpsVcodeVdhInvitationOrder afootOrder = findAfoot();
            if (afootOrder!=null){
                return "有其他订单正在生成邀请码，订单修改失败。";
            }
            VpsVcodeVdhInvitationOrder tempOrder = new VpsVcodeVdhInvitationOrder();
            tempOrder.setInfoKey(order.getInfoKey());
            tempOrder.setOrderName(order.getOrderName());
            tempOrder.setOrderNum(order.getOrderNum());
            tempOrder.setUpdateUser(currentUser.getUserName());
            tempOrder.setUpdateTime(DateUtil.getDateTime());
            vdhInvitationOrderDao.update(tempOrder);
        }else{
            return "无此订单，订单修改失败。";
        }
        return null;
    }

    /**
     * 修改邀请码订单状态
     * @param order
     */
    public void updateOrderStatus(VpsVcodeVdhInvitationOrder order, SysUserBasis currentUser, String status){
        VpsVcodeVdhInvitationOrder orderTemp = new VpsVcodeVdhInvitationOrder();
        orderTemp.setInfoKey(order.getInfoKey());
        orderTemp.setStatus(status);
        orderTemp.setUpdateUser(currentUser.getUserName());
        orderTemp.setUpdateTime(DateUtil.getDateTime());
        vdhInvitationOrderDao.update(orderTemp);
    }

    /**
     * 删除订单
     * @param infoKey
     */
    public String deleteOrder(String infoKey){
        VpsVcodeVdhInvitationOrder order = findById(infoKey);
        if(order != null){
            if (!"0".equals(order.getStatus())){
                return "1".equals(order.getStatus()) ? "该订单邀请码正在生成中，订单删除失败。" : "该订单邀请码已经生成，订单删除失败。";
            }
            VpsVcodeVdhInvitationOrder afootOrder = findAfoot();
            if (afootOrder!=null){
                return "有其他订单正在生成邀请码，订单删除失败。";
            }
            vdhInvitationOrderDao.removeOrderByInfoKey(infoKey);
        }else{
            return "无此订单，订单删除失败。";
        }
        return null;
    }

    /**
     * 检查订单名称
     * @param currentUser
     * @param checkName
     * @param infoKey
     * @return
     */
    public String checkName(SysUserBasis currentUser, String checkName, String infoKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("companyKey", currentUser.getCompanyKey());
        map.put("checkName", checkName);
        map.put("infoKey", infoKey);
        VpsVcodeVdhInvitationOrder resultData = vdhInvitationOrderDao.checkName(map);
        if(resultData==null){
            return "1";
        }
        return "0";
    }

    /**
     * 查询正在生成邀请码的订单
     * @return
     */
    public VpsVcodeVdhInvitationOrder findAfoot() {
        return vdhInvitationOrderDao.findAfoot();
    }

    /**
     * 根据id查询邀请码订单
     * @param infoKey
     * @return
     */
    public VpsVcodeVdhInvitationOrder findById(String infoKey) {
        return vdhInvitationOrderDao.findById(infoKey);
    }
}
