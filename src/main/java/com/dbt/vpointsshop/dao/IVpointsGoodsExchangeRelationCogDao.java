package com.dbt.vpointsshop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dbt.vpointsshop.bean.VpointsGoodsExchangeRelationCogEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 第二件半价活动和商品关联表 Mapper 接口
 * </p>
 *
 * @author wangshuda
 * @since 2022-07-11
 */
public interface IVpointsGoodsExchangeRelationCogDao extends BaseMapper<VpointsGoodsExchangeRelationCogEntity> {

    void deleteByInfoKey(@Param("infoKey") String infoKey);

    List<VpointsGoodsExchangeRelationCogEntity> selectByExchangeInfoKey(@Param("infoKey") String infoKey);
}
