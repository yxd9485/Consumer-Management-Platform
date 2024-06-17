package com.dbt.platform.waitActivation.dao;

import com.dbt.platform.waitActivation.bean.VpsWaitActivationPrizeCog;
import com.dbt.platform.waitActivation.bean.VpsWaitActivationPrizeSku;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author: LiangRunBin
 * @create-date: 2023/12/26 19:02
 */
public interface VpsWaitActivationPrizeCogDao {

    List<VpsWaitActivationPrizeCog> queryForLst(Map<String, Object> queryMap);

    List<VpsWaitActivationPrizeCog> queryAll();

    int queryForCount(Map<String, Object> queryMap);

    int insert(VpsWaitActivationPrizeCog vpsWaitActivationPrizeCog);

    int update(VpsWaitActivationPrizeCog vpsWaitActivationPrizeCog);

    VpsWaitActivationPrizeCog selectByKey(String prizeKey);

    int insertSkuList(@Param("skuKey") String skuKey, @Param("skuName") String skuName, @Param("shortName") String shortName, @Param("prizeKey") String prizeKey, @Param("createUser") String createUser);

    List<VpsWaitActivationPrizeSku> selectSkuKeysByPrizeKey(String prizeKey);

    int clearByPrizeKey(String prizeKey);

}
