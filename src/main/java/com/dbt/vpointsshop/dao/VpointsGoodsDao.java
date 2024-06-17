package com.dbt.vpointsshop.dao;

import java.util.List;
import java.util.Map;

import com.dbt.vpointsshop.bean.CompanyInfo;
import com.dbt.vpointsshop.bean.VpointsBrandInfo;
import com.dbt.vpointsshop.bean.VpointsCategoryType;
import com.dbt.vpointsshop.bean.VpointsCouponBatch;
import com.dbt.vpointsshop.bean.VpointsGoodsInfo;

import feign.Param;

public interface VpointsGoodsDao {
	/**
	 * 新增商品
	 * @param info
	 */
	public void addGoods(VpointsGoodsInfo info);
	/**
	 * 更新商品
	 * @param info
	 */
	public void updateGoods(VpointsGoodsInfo info);
	/**
	 * 删除商品
	 * @param goodsId
	 */
	public void delGoodsById(String goodsId);
	/**
	 * 商品列表
	 * @param map
	 * @return
	 */
	public List<VpointsGoodsInfo> getGoodsList(Map<String,Object> map);
	public int getGoodsCount(Map<String,Object> map);
	/**
	 * 获取一级品类
	 * @return
	 */
	public List<VpointsCategoryType> getFirstCategoryList();
	/**
	 * 根据父ID获取下级分类
	 * @param parentId
	 * @return
	 */
	public List<VpointsCategoryType> getCategoryByParent(String parentId);
	public List<CompanyInfo> getCompanyList();
	public List<VpointsCouponBatch> getBatchList();
	public int getUseCount(String batchKey,String goodsId);
	
	/**
	 * 依据品牌ID获取商品信息
	 * 
	 * @param map keys:brandId
	 */
	public List<VpointsGoodsInfo> queryGoodsByBrandId(Map<String, Object> map);
	
	/**
	 * 依据品牌父ID获取子品牌信息
	 * 
	 * @param map keys:parentId
	 */
	public List<VpointsBrandInfo> queryBrandByParentId(Map<String, Object> map);
	
	/**
	 * 商品展示排序
	 * 
	 * @param goodsId, goodShowSequence
	 */
	public void updateGoodShowSequence(Map<String, Object> map);
	
	/**
	 * 更新自定义销量排序
	 * 
	 * @param goodsId, saleNumSequence
	 */
	public void updateSaleNumSequence(Map<String, Object> map);
	
	/**
	 * 依据商品客户自定义编号获取
	 * 
	 * @param goodsClientNo
	 */
	public VpointsGoodsInfo findByGoodsClientNo(Map<String, Object> map);
	
	/**
	 * 返还商品的剩余数量
	 * @param map
	 */
	public void updateGoodsRemains(Map<String, Object> map);
	
	/**
	 * 预扣商品库存
	 * @param map
	 */
	public void updateGoodsRemainsForWithholding(Map<String, Object> map);

    List<VpointsGoodsInfo> getAllGoods();

	void updateGoodShowSales(Map<String, String> map);
	/**
	 * 获取商品列表
	 * @return
	 */
	public List<VpointsGoodsInfo> queryGoodsList(Map<String, Object> map);

	public VpointsGoodsInfo getGoodsInfo(@Param("goodsId") String goodsId);


    List<VpointsGoodsInfo> getAllGoodsForJob();

    public List<VpointsGoodsInfo> findByGoodsAsGift(Map<String, Object> map);
    /**
     * 查询指定省份配送商品列表
     * @param provinceLst
     * @return
     */
    public List<VpointsGoodsInfo> queryByShipmentProvince(Map<String, Object> map);

	List<VpointsGoodsInfo> getShopGoods(VpointsGoodsInfo vpointsGoodsInfo);
}
