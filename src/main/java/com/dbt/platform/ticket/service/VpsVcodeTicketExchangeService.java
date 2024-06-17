package com.dbt.platform.ticket.service;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.*;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.ticket.bean.VpsVcodeTicketExchangeCog;
import com.dbt.platform.ticket.dao.IVpsVcodeTicketExchangeDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VpsVcodeTicketExchangeService {
    @Autowired
    private IVpsVcodeTicketExchangeDao vcodeTicketExchangeDao;

    public List<VpsVcodeTicketExchangeCog> queryForList(VpsVcodeTicketExchangeCog queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        List<VpsVcodeTicketExchangeCog> vpsVcodeTicketExchangeCogList = vcodeTicketExchangeDao.queryForList(map);
        for (VpsVcodeTicketExchangeCog vpsVcodeTicketExchangeCog : vpsVcodeTicketExchangeCogList) {
            String ticketExchangeCount = RedisApiUtil.CacheKey.ticket.TICKET_EXCHANGE_COUNT + Constant.DBTSPLIT + vpsVcodeTicketExchangeCog.getInfoKey();
            String ticketExchangeConsumeVpoints = RedisApiUtil.CacheKey.ticket.TICKET_EXCHANGE_CONSUME_VPOINTS + Constant.DBTSPLIT + vpsVcodeTicketExchangeCog.getInfoKey();
            vpsVcodeTicketExchangeCog.setTicketExchangeCount(StringUtils.isBlank(RedisApiUtil.getInstance().get(ticketExchangeCount)) ? "0" : RedisApiUtil.getInstance().get(ticketExchangeCount));
            vpsVcodeTicketExchangeCog.setTicketExchangeConsumeVpoints(StringUtils.isBlank(RedisApiUtil.getInstance().get(ticketExchangeConsumeVpoints)) ? "0" : RedisApiUtil.getInstance().get(ticketExchangeConsumeVpoints));
        }
        return vpsVcodeTicketExchangeCogList;
    }

    public int queryForCount(VpsVcodeTicketExchangeCog queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return vcodeTicketExchangeDao.queryForCount(map);
    }

    public void doTicketExchangeAdd(VpsVcodeTicketExchangeCog ticketExchangeCog, SysUserBasis currentUser) {
        ticketExchangeCog.setInfoKey(UUIDTools.getInstance().getUUID());
        ticketExchangeCog.setDeleteFlag("0");
        ticketExchangeCog.setCreateTime(DateUtil.getDateTime());
        ticketExchangeCog.setCreateUser(currentUser.getUserName());
        ticketExchangeCog.setUpdateTime(DateUtil.getDateTime());
        ticketExchangeCog.setUpdateUser(currentUser.getUserName());
        vcodeTicketExchangeDao.doTicketExchangeAdd(ticketExchangeCog);
    }

    public VpsVcodeTicketExchangeCog findById(String infoKey) {
        return vcodeTicketExchangeDao.findById(infoKey);
    }

    public void doTicketExchangeEdit(VpsVcodeTicketExchangeCog ticketExchangeCog, SysUserBasis currentUser) {
        ticketExchangeCog.setUpdateUser(currentUser.getUserName());
        ticketExchangeCog.setUpdateTime(DateUtil.getDateTime());
        vcodeTicketExchangeDao.doTicketExchangeEdit(ticketExchangeCog);
    }

    public void doTicketExchangeDelete(String infoKey, SysUserBasis currentUser) {
        Map<String, Object> map = new HashMap<>();
        map.put("infoKey", infoKey);
        map.put("updateUser", currentUser.getUserName());
        vcodeTicketExchangeDao.doTicketExchangeDelete(map);
    }
}
