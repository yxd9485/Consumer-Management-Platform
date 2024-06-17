package com.dbt.vpointsshop.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dbt.vpointsshop.bean.VpointsGoodsExchangeActivityCogEntity;
import com.dbt.vpointsshop.bean.VpointsGoodsHalfPriceActivityCogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dbt.vpointsshop.dto.VpointsGoodsHalfPriceActivityCogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商城第二件半价活动 Mapper 接口
 * </p>
 *
 * @author wangshuda
 * @since 2022-07-11
 */
public interface IVpointsGoodsHalfPriceActivityCogDao extends BaseMapper<VpointsGoodsHalfPriceActivityCogEntity> {

    IPage<VpointsGoodsHalfPriceActivityCogEntity> selectPageVO(IPage<VpointsGoodsHalfPriceActivityCogEntity> initPage,
                                                               @Param("ew") VpointsGoodsHalfPriceActivityCogEntity entity);

    VpointsGoodsHalfPriceActivityCogEntity findByInfoKey(String infoKey);

    void updateByInfoKey(VpointsGoodsHalfPriceActivityCogEntity entity);

    List<VpointsGoodsHalfPriceActivityCogVO> getCurrentActivity(@Param("date") String date);

    List<VpointsGoodsHalfPriceActivityCogVO> checkGoodsActivity(VpointsGoodsHalfPriceActivityCogVO vo);
}
