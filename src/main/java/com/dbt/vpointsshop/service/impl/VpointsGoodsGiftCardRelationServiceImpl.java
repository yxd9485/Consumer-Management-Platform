package com.dbt.vpointsshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dbt.vpointsshop.bean.VpointsGoodsGiftCardRelation;
import com.dbt.vpointsshop.dao.IVpointsGoodsGiftCardRelationDao;
import com.dbt.vpointsshop.service.IVpointsGoodsGiftCardRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 礼品卡活动商品关联表 服务实现类
 * </p>
 *
 * @author wangshuda
 * @since 2022-08-15
 */
@Service
public class VpointsGoodsGiftCardRelationServiceImpl extends ServiceImpl<IVpointsGoodsGiftCardRelationDao, VpointsGoodsGiftCardRelation> implements IVpointsGoodsGiftCardRelationService {

    @Override
    public void deleteByGiftCardInfoKey(String infoKey) {
        VpointsGoodsGiftCardRelation relation = new VpointsGoodsGiftCardRelation();
        relation.setGiftCardInfoKey(infoKey);
        this.baseMapper.delete(new QueryWrapper<>(relation));
    }
}
