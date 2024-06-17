package com.dbt.platform.fission.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dbt.platform.fission.bean.VpsVcodeActivatePrizeRecordEntity;
import com.dbt.platform.fission.bean.VpsVcodeActivateShareRecord;
import com.dbt.platform.fission.vo.VpsVcodeActivatePrizeRecordVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangshuda
 * @since 2022-01-12
 */
public interface IVpsVcodeActivatePrizeRecordMapper extends BaseMapper<VpsVcodeActivatePrizeRecordEntity> {

    VpsVcodeActivateShareRecord getLimitConsumeAcount(String activityKey);

    IPage<VpsVcodeActivatePrizeRecordEntity> selectPageVo(IPage<VpsVcodeActivatePrizeRecordEntity> page,@Param("ew") VpsVcodeActivatePrizeRecordVO queryBean);
    List<VpsVcodeActivatePrizeRecordEntity> selectPageVo(@Param("ew") VpsVcodeActivatePrizeRecordVO queryBean);

    List<VpsVcodeActivatePrizeRecordEntity> findAllDataList();

    void save(VpsVcodeActivatePrizeRecordEntity newEntity);
}
