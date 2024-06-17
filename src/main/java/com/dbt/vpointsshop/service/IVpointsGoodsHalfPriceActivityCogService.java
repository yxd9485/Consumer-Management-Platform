package com.dbt.vpointsshop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dbt.framework.base.action.reply.BaseResult;
import com.dbt.vpointsshop.bean.VpointsGoodsExchangeActivityCogEntity;
import com.dbt.vpointsshop.bean.VpointsGoodsHalfPriceActivityCogEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dbt.vpointsshop.dto.VpointsGoodsHalfPriceActivityCogVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城第二件半价活动 服务类
 * </p>
 *
 * @author wangshuda
 * @since 2022-07-11
 */
public interface IVpointsGoodsHalfPriceActivityCogService  {
    public String getRuleNo();

    IPage<VpointsGoodsHalfPriceActivityCogVO> selectPageVO(IPage initPage, VpointsGoodsHalfPriceActivityCogEntity entity);

    boolean create(VpointsGoodsHalfPriceActivityCogVO vo);

    boolean update(VpointsGoodsHalfPriceActivityCogVO vo);

    VpointsGoodsHalfPriceActivityCogVO findByInfoKey(String infoKey);

    List<VpointsGoodsHalfPriceActivityCogVO> getCurrentActivity();

    BaseResult<Map<String, Object>> checkGoodsActivity(VpointsGoodsHalfPriceActivityCogVO vo);

    List<VpointsGoodsHalfPriceActivityCogVO> selectPageVO(VpointsGoodsHalfPriceActivityCogEntity entity);
}
