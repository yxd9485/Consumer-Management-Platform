package com.dbt.vpointsshop.service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.vpointsshop.bean.VpointsGoodsGiftRelation;
import com.dbt.vpointsshop.bean.VpointsGoodsInfo;
import com.dbt.vpointsshop.dao.VpointsGoodsGiftRelationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class VpointsGoodsGiftRelationService extends BaseService<VpointsGoodsGiftRelation> {

    @Autowired
    private VpointsGoodsGiftRelationDao goodsGiftRelationDao;
    /**
     * 添加商品赠品关系
     * @param info
     * @return
     */
    public String addGoodsGift(VpointsGoodsGiftRelation info){
        goodsGiftRelationDao.addGiftAsGoods(info);
        String isSuccess="addSuccess";
        return isSuccess;
    }

    /**
     * 删除赠品关系
     * @param giftId
     * @return
     */
    public String deleteByGiftKeys(String giftId){
        HashMap<String, Object> map = new HashMap<>();
        map.put("giftId",giftId);
        goodsGiftRelationDao.deleteByGiftKeys(map);
        String isSuccess="deleteSuccess";
        return isSuccess;
    }

    /**查询赠品对应商品
     * @param giftId
     * @return
     */
    public List<VpointsGoodsGiftRelation> findGoodsByGiftId(String giftId){
        return  goodsGiftRelationDao.findGoodsByGiftId(giftId);
    }

}
