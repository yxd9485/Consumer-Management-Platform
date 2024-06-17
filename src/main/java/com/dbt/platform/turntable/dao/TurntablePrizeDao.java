package com.dbt.platform.turntable.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.turntable.bean.VpsTurntablePrizeCog;

import java.util.List;

public interface TurntablePrizeDao extends IBaseDao<VpsTurntablePrizeCog> {

    void createTurntablePrize(VpsTurntablePrizeCog turntablePrizeCog);

    int queryTurntablePrizeNum(String activityKey);

    List<VpsTurntablePrizeCog> queryTurntablePrizeByActivityKey(String infoKey);

    VpsTurntablePrizeCog queryTurntablePrizeByInfoKey(String infoKey);
}
