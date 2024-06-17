package com.dbt.platform.publish.service;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.publish.bean.VpsAdRegion;
import com.dbt.platform.publish.bean.VpsAdShop;
import com.dbt.platform.publish.dao.ActRegionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActRegionService {
    @Autowired
    private ActRegionDao actRegionDao;

    public List<VpsAdRegion> queryForLst(VpsAdRegion queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        return actRegionDao.queryForLst(map);
    }

    public int queryForCount(VpsAdRegion queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return actRegionDao.queryForCount(map);
    }

    public void addAdRegion(VpsAdRegion vpsAdRegion) {
        if ("4".equals(vpsAdRegion.getJumpTyp())
                && !vpsAdRegion.getJumpUrl().startsWith("/")) {
            vpsAdRegion.setJumpUrl("/" + vpsAdRegion.getJumpUrl());
        }
        actRegionDao.addAdRegion(vpsAdRegion);
    }

    public List<VpsAdRegion> queryAll() {
        return actRegionDao.queryAll();
    }

    public VpsAdRegion findById(String infoKey) {
        return actRegionDao.findById(infoKey);
    }

    public void updateVpsAdRegion(VpsAdRegion vpsAdRegion) {
        if ("4".equals(vpsAdRegion.getJumpTyp())
                && !vpsAdRegion.getJumpUrl().startsWith("/")) {
            vpsAdRegion.setJumpUrl("/" + vpsAdRegion.getJumpUrl());
        }
        actRegionDao.updateVpsAdRegion(vpsAdRegion);
    }


    public void adRegionDelete(String infoKey) {
        VpsAdRegion vpsAdRegion = findById(infoKey);
        String redisKey = Constant.pubFalg.ACTREGION + Constant.DBTSPLIT + vpsAdRegion.getPosition();
        RedisApiUtil.getInstance().del(true, redisKey);
        actRegionDao.adRegionDelete(infoKey);
    }
}
