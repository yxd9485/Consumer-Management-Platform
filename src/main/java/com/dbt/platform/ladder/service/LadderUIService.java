package com.dbt.platform.ladder.service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.ladder.bean.LadderUI;
import com.dbt.platform.ladder.dao.ILadderUIDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 阶梯中奖UI
 * @Author bin.zhang
 **/
@Service
public class LadderUIService extends BaseService<LadderUI> {
    @Autowired
    private ILadderUIDao ladderUIDao;
    public List<LadderUI> findLadderUIByKey(String ruleKey, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("ruleKey", ruleKey);
        map.put("pageInfo", pageInfo);
        return ladderUIDao.findLadderUIByKey(map);
    }

    public int findLadderUICount(String ruleKey) {
        return ladderUIDao.findLadderUICount(ruleKey);
    }

    public void addLadderUI(LadderUI ladderUI) {
         ladderUIDao.addLadderUI(ladderUI);
    }

    public void deleteById(String infoKey) {
        ladderUIDao.deleteById(infoKey);
    }

    public LadderUI findById(String infoKey) {
        return ladderUIDao.findByInfoKey(infoKey);
    }

    public void updateUI(LadderUI ladderUI) {
        ladderUIDao.updateUI(ladderUI);
    }
}
