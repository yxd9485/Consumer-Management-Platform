package com.dbt.platform.fission.dao;

import com.dbt.platform.fission.bean.VpsVcodeActivateSharePrizeCog;

import java.util.List;
import java.util.Map;

public interface IVpsVcodeActivateSharePrizeDao {
    void create(List<VpsVcodeActivateSharePrizeCog> prizeCogList);

    List<VpsVcodeActivateSharePrizeCog> queryPrizeCogByActivityKeyAndUserType(Map<String, Object> map);

    void deletePrizeCogByActivityKey(String activityKey);
}
