package com.dbt.vpointsshop.service.impl;

import com.dbt.framework.base.service.BaseService;
import com.dbt.vpointsshop.bean.VpointsGoodsHalfPriceRelationCogEntity;
import com.dbt.vpointsshop.dao.IVpointsGoodsHalfPriceRelationCogDao;
import com.dbt.vpointsshop.service.IVpointsGoodsHalfPriceRelationCogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 第二件半价活动和商品关联表 服务实现类
 * </p>
 *
 * @author wangshuda
 * @since 2022-07-11p
 */
@Service
public class VpointsGoodsHalfPriceRelationCogServiceImpl extends BaseService<VpointsGoodsHalfPriceRelationCogEntity> implements IVpointsGoodsHalfPriceRelationCogService {

    @Autowired
    private IVpointsGoodsHalfPriceRelationCogDao iVpointsGoodsHalfPriceRelationCogDao;

}
