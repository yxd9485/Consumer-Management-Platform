package com.dbt.platform.fission.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.fission.bean.VpsVcodeActivatePrizeRecordEntity;
import com.dbt.platform.fission.bean.VpsVcodeActivateShareRecord;
import com.dbt.platform.fission.vo.VpsVcodeActivatePrizeRecordVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangshuda
 * @since 2022-01-12
 */
public interface IVpsVcodeActivatePrizeRecordService extends IService<VpsVcodeActivatePrizeRecordEntity> {

    IPage<VpsVcodeActivatePrizeRecordEntity> selectPage(PageOrderInfo pageInfo, VpsVcodeActivatePrizeRecordVO queryParam, String companyKey);

    public List<VpsVcodeActivatePrizeRecordEntity> exportList(PageOrderInfo pageInfo,VpsVcodeActivatePrizeRecordVO queryBean, String companyKey);

    VpsVcodeActivateShareRecord getLimitConsumeAcount(String activityKey);

    List<VpsVcodeActivatePrizeRecordEntity> findAllDataList();

    void insert(VpsVcodeActivatePrizeRecordEntity newEntity);
}
