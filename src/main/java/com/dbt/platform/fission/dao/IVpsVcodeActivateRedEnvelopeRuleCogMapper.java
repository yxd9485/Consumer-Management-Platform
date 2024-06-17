package com.dbt.platform.fission.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.fission.bean.VpsVcodeActivateRedEnvelopeRuleCogEntity;
import com.dbt.platform.fission.vo.VpsVcodeActivateRedEnvelopeRuleCogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 激活红包规则（裂变红包） Mapper 接口
 * </p>
 *
 * @author wangshuda
 * @since 2022-01-11
 */
public interface IVpsVcodeActivateRedEnvelopeRuleCogMapper extends BaseMapper<VpsVcodeActivateRedEnvelopeRuleCogEntity> {
    IPage<VpsVcodeActivateRedEnvelopeRuleCogVO> selectPageVo(IPage<VpsVcodeActivateRedEnvelopeRuleCogEntity> page,
                                                             @Param("vo") VpsVcodeActivateRedEnvelopeRuleCogVO vo,
                                                             @Param("pageInfo") PageOrderInfo pageInfo);

    List<String> getTabs1SkuKey(@Param("tabs") String tabs,@Param("ruleKey") String ruleKey,
                                @Param("startDate")String startDate,@Param("endDate")String endDate,
                                @Param("shareType")String shareType);

    List<String> getTabs1ActivateList(@Param("tabs") String s,@Param("ruleKey") String ruleKey,
                                      @Param("startDate")String startDate,@Param("endDate")String endDate,
                                      @Param("shareType")String shareType);

    void updateActivityCog(VpsVcodeActivateRedEnvelopeRuleCogEntity entity);

    int queryOverlapActivity(Map<String, Object> map);

}
