package com.dbt.platform.fission.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.appuser.service.VpsConsumerUserInfoService;
import com.dbt.platform.fission.bean.VpsVcodeActivatePrizeRecordEntity;
import com.dbt.platform.fission.bean.VpsVcodeActivateShareRecord;
import com.dbt.platform.fission.dao.IVpsVcodeActivatePrizeRecordMapper;
import com.dbt.platform.fission.service.IVpsVcodeActivatePrizeRecordService;
import com.dbt.platform.fission.vo.VpsVcodeActivatePrizeRecordVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangshuda
 * @since 2022-01-12
 */
@Service
public class VpsVcodeActivatePrizeRecordServiceImpl extends ServiceImpl<IVpsVcodeActivatePrizeRecordMapper, VpsVcodeActivatePrizeRecordEntity> implements IVpsVcodeActivatePrizeRecordService {
    @Autowired
    private VpsConsumerUserInfoService consumerUserInfoService;
    @Autowired
    private IVpsVcodeActivatePrizeRecordMapper iVpsVcodeActivatePrizeRecordMapper;
    private final static String[] packTables = {"1", "2", "3", "4", "5", "6", "7" ,"8", "9", "10", "11", "12"};
    @Override
    public IPage<VpsVcodeActivatePrizeRecordEntity> selectPage(PageOrderInfo pageInfo, VpsVcodeActivatePrizeRecordVO queryBean, String companyKey) {
        IPage<VpsVcodeActivatePrizeRecordEntity> page = new Page<>();
        page.setCurrent(pageInfo.getCurrentPage());
        page.setPages(pageInfo.getStartCount());
        page.setSize(pageInfo.getPagePerCount());
        queryBean.setPackTables(Arrays.asList(packTables));
        QueryWrapper<VpsVcodeActivatePrizeRecordEntity> recordEntityQueryWrapper = paramInit(pageInfo,queryBean);
        return this.baseMapper.selectPageVo(page, queryBean);
    }
    @Override
    public List<VpsVcodeActivatePrizeRecordEntity> exportList( PageOrderInfo pageInfo, VpsVcodeActivatePrizeRecordVO queryBean, String companyKey) {
        queryBean.setPackTables(Arrays.asList(packTables));
        return this.baseMapper.selectPageVo(queryBean);
    }

    private QueryWrapper<VpsVcodeActivatePrizeRecordEntity>  paramInit(PageOrderInfo pageInfo,VpsVcodeActivatePrizeRecordVO queryBean){
        VpsVcodeActivatePrizeRecordEntity entity = new VpsVcodeActivatePrizeRecordEntity();
        entity.setUserKey(StringUtils.isEmpty(queryBean.getUserKey())?null:queryBean.getUserKey().trim());
        entity.setNickName(StringUtils.isEmpty(queryBean.getNickName())?null:queryBean.getNickName().trim());
        entity.setWinQrcodeContent(StringUtils.isEmpty(queryBean.getWinQrcodeContent())?null:queryBean.getWinQrcodeContent().trim());
        entity.setMoneyFlag(StringUtils.isEmpty(queryBean.getMoneyFlag())?null:queryBean.getMoneyFlag());
        entity.setPackTables(queryBean.getPackTables());
        QueryWrapper<VpsVcodeActivatePrizeRecordEntity> recordEntityQueryWrapper = new QueryWrapper<>(entity);
        try {
            if (StringUtils.isNotEmpty(queryBean.getStartDate())) {
                recordEntityQueryWrapper.ge("win_date", DateUtil.parse(DateUtil.getStartDateTimeByDay(queryBean.getStartDate()), DateUtil.DEFAULT_DATETIME_FORMAT));
            }
            if (StringUtils.isNotEmpty(queryBean.getEndDate())) {
                recordEntityQueryWrapper.le("win_date", DateUtil.parse(DateUtil.getEndDateTimeByDay(queryBean.getEndDate()), DateUtil.DEFAULT_DATETIME_FORMAT));
            }
            boolean isAsc = true;
            String desc = "desc";
            if(desc.equals(pageInfo.getOrderType())){
                isAsc = false;
            }
            if(StringUtils.isNotEmpty(pageInfo.getOrderCol())){
                recordEntityQueryWrapper.orderBy(true, isAsc, pageInfo.getOrderCol());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return recordEntityQueryWrapper;
    }

    @Override
    public VpsVcodeActivateShareRecord getLimitConsumeAcount(String activityKey) {
        return iVpsVcodeActivatePrizeRecordMapper.getLimitConsumeAcount(activityKey);
    }

    @Override
    public List<VpsVcodeActivatePrizeRecordEntity> findAllDataList() {
        return this.iVpsVcodeActivatePrizeRecordMapper.findAllDataList();
    }

    @Override
    public void insert(VpsVcodeActivatePrizeRecordEntity newEntity) {
        this.iVpsVcodeActivatePrizeRecordMapper.save(newEntity);
    }
}
