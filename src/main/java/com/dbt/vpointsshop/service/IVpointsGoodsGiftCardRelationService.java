package com.dbt.vpointsshop.service;

import com.dbt.vpointsshop.bean.VpointsGoodsGiftCardRelation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 礼品卡活动商品关联表 服务类
 * </p>
 *
 * @author wangshuda
 * @since 2022-08-15
 */
public interface IVpointsGoodsGiftCardRelationService extends IService<VpointsGoodsGiftCardRelation> {

    void deleteByGiftCardInfoKey(String infoKey);
}
