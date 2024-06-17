package com.dbt.platform.ticket.dao;

import com.dbt.platform.ticket.bean.VpsVcodeTicketUserRecord;

import java.util.List;
import java.util.Map;

public interface IVpsVcodeTicketUserRecordDao {
    List<VpsVcodeTicketUserRecord> queryList(Map<String, Object> map);

    int queryForCount(Map<String, Object> map);
}
