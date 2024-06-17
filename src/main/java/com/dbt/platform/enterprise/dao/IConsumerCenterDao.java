package com.dbt.platform.enterprise.dao;

import com.dbt.platform.enterprise.bean.ConsumerCenterCogInfo;

import java.util.List;

public interface IConsumerCenterDao {
    List<ConsumerCenterCogInfo> queryForLst();

    void create(List<ConsumerCenterCogInfo> resultConsumerCenterCogInfoList);

    void deleteConsumerCenter();
}
