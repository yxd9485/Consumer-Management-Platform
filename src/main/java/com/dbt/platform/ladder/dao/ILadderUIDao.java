package com.dbt.platform.ladder.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.ladder.bean.LadderUI;

import java.util.List;
import java.util.Map;

/**
 * @Description: 阶梯中奖ui dao
 * @Author bin.zhang
 **/
public interface ILadderUIDao extends IBaseDao<LadderUI> {
    List<LadderUI> findLadderUIByKey(Map<String, Object> map);

    int findLadderUICount(String ruleKey);

    void addLadderUI(LadderUI ladderUI);

    LadderUI findByInfoKey(String infoKey);

    void updateUI(LadderUI ladderUI);
}
