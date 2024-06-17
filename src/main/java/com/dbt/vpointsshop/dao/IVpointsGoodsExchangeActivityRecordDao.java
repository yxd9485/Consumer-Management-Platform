package com.dbt.vpointsshop.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dbt.vpointsshop.bean.VpointsGoodsExchangeActivityCogEntity;
import com.dbt.vpointsshop.bean.VpointsGoodsExchangeActivityRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dbt.vpointsshop.dto.VpointsGoodsExchangeActivityRecordVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商城商品活动记录表 Mapper 接口
 * </p>
 *
 * @author wangshuda
 * @since 2022-07-16
 */
public interface IVpointsGoodsExchangeActivityRecordDao extends BaseMapper<VpointsGoodsExchangeActivityRecordEntity> {

    IPage<VpointsGoodsExchangeActivityRecordVO> selectDataPageVO(IPage initPage,@Param("ew") VpointsGoodsExchangeActivityRecordVO vo);
    List<VpointsGoodsExchangeActivityRecordVO> selectDataPageVO(@Param("ew") VpointsGoodsExchangeActivityRecordVO vo);
}
