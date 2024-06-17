package com.dbt.vpointsshop.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dbt.framework.base.action.reply.BaseResult;
import com.dbt.vpointsshop.bean.VpointsGoodsExchangeActivityCogEntity;
import com.dbt.vpointsshop.dto.VpointsGoodsExchangeActivityCogVO;
import com.dbt.vpointsshop.dto.VpointsGoodsExchangeActivityRecordVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城第二件半价活动 Mapper 接口
 * </p>
 *
 * @author wangshuda
 * @since 2022-07-11
 */
public interface IVpointsGoodsExchangeActivityCogDao extends BaseMapper<VpointsGoodsExchangeActivityCogEntity> {

    IPage<VpointsGoodsExchangeActivityCogEntity> selectPageVO(IPage<VpointsGoodsExchangeActivityCogEntity> initPage,
                                                              @Param("ew")VpointsGoodsExchangeActivityCogEntity entityQueryWrapper);

    VpointsGoodsExchangeActivityCogEntity findByInfoKey(String infoKey);

    void updateByInfoKey(VpointsGoodsExchangeActivityCogEntity entity);

    List<VpointsGoodsExchangeActivityCogEntity> getCurrentActivity(@Param("date") String date);

    List<VpointsGoodsExchangeActivityCogEntity> checkGoodsActivity(VpointsGoodsExchangeActivityCogVO vo);
}
