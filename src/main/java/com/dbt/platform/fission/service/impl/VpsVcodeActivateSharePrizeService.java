package com.dbt.platform.fission.service.impl;

import com.dbt.platform.fission.bean.VpsVcodeActivateSharePrizeCog;
import com.dbt.platform.fission.dao.IVpsVcodeActivateSharePrizeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VpsVcodeActivateSharePrizeService {

    @Autowired
    private IVpsVcodeActivateSharePrizeDao activateSharePrizeDao;

    public void create(List<VpsVcodeActivateSharePrizeCog> prizeCogList) {
        activateSharePrizeDao.create(prizeCogList);
    }

    public List<VpsVcodeActivateSharePrizeCog> queryPrizeCogByActivityKeyAndUserType(String activityKey, String prizeUserType) {
        Map<String,Object> map = new HashMap<>();
        map.put("activityKey",activityKey);
        map.put("prizeUserType",prizeUserType);
        return activateSharePrizeDao.queryPrizeCogByActivityKeyAndUserType(map);
    }

    public void deletePrizeCogByActivityKey(String activityKey) {
        activateSharePrizeDao.deletePrizeCogByActivityKey(activityKey);
    }
}
