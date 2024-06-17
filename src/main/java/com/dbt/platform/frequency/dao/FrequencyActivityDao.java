package com.dbt.platform.frequency.dao;

import com.dbt.platform.frequency.bean.VpsVcodeActivityFrequencyCog;

import java.util.List;
import java.util.Map;

public interface FrequencyActivityDao {
    void create(VpsVcodeActivityFrequencyCog frequencyCog);

    VpsVcodeActivityFrequencyCog findById(String infoKey);

    List<VpsVcodeActivityFrequencyCog> queryForList(Map<String, Object> map);

    void update(VpsVcodeActivityFrequencyCog frequencyCog);

    void deleteById(Map<String, Object> map);

    int queryForCount(Map<String, Object> map);
    
    /**
     * 校验扫码活动指定时间内是否参与有效的频次活动
     * @param map keys：infoKey、vcodeActivityKey、beginDate、endDate
     * @return
     */
    List<VpsVcodeActivityFrequencyCog> validCogByVcodeActivityKey(Map<String, Object> map);
}
