package com.dbt.vpointsshop.service.impl;

import com.dbt.framework.base.service.BaseService;
import com.dbt.vpointsshop.bean.VpointsGoodsExchangeRelationCogEntity;
import com.dbt.vpointsshop.dao.IVpointsGoodsExchangeRelationCogDao;
import com.dbt.vpointsshop.service.IVpointsGoodsExchangeRelationCogService;
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
public class VpointsGoodsExchangeRelationCogServiceImpl extends BaseService<VpointsGoodsExchangeRelationCogEntity> implements IVpointsGoodsExchangeRelationCogService {

    @Autowired
    private IVpointsGoodsExchangeRelationCogDao iVpointsGoodsExchangeRelationCogDao;

}
