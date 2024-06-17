package com.dbt.platform.ticket.dao;

import com.dbt.platform.ticket.bean.VpsVcodeTicketExchangeCog;

import java.util.List;
import java.util.Map;

public interface IVpsVcodeTicketExchangeDao {
    List<VpsVcodeTicketExchangeCog> queryForList(Map<String, Object> map);

    int queryForCount(Map<String, Object> map);


    void doTicketExchangeAdd(VpsVcodeTicketExchangeCog ticketExchangeCog);

    VpsVcodeTicketExchangeCog findById(String infoKey);

    void doTicketExchangeEdit(VpsVcodeTicketExchangeCog ticketExchangeCog);

    void doTicketExchangeDelete(Map<String, Object> map);
}
