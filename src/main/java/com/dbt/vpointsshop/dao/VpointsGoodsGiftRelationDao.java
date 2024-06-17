package com.dbt.vpointsshop.dao;

import com.dbt.vpointsshop.bean.VpointsGoodsGiftRelation;
import com.dbt.vpointsshop.bean.VpointsGoodsInfo;

import java.util.List;
import java.util.Map;

public interface VpointsGoodsGiftRelationDao {

    public void addGiftAsGoods(VpointsGoodsGiftRelation gift);

    /**
     * 删除商品关联赠品信息
     *
     * @param map 商品主键
     * @return
     */
    public int deleteByGiftKeys(Map map);

    public List<VpointsGoodsGiftRelation> findGoodsByGiftId(String giftId);

}
