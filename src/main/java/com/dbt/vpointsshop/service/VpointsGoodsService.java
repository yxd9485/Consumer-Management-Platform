package com.dbt.vpointsshop.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dbt.framework.base.action.reply.BaseResult;
import com.dbt.vpointsshop.bean.*;
import com.dbt.vpointsshop.dto.VpointsGoodsGiftCardCogVO;
import com.dbt.vpointsshop.dto.VpointsGoodsHalfPriceActivityCogVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.HttpReq;
import com.dbt.framework.util.MathUtil;
import com.dbt.framework.util.ReflectUtil;
import com.dbt.framework.util.StringUtil;
import com.dbt.platform.activity.service.VcodeActivityVpointsCogService;
import com.dbt.vpointsshop.dao.VpointsGoodsDao;
import org.springframework.util.CollectionUtils;

/**
 * 积分商城商品处理
 * @author zhaohongtao
 *2017年11月16日
 */
@Service
public class VpointsGoodsService extends BaseService<VpointsGoodsInfo>{
	
	@Autowired
	private VpointsGoodsDao goodsDao;
	@Autowired
	private VpointsGoodsConsumerStatusInfoService goodsConsumerStatusInfoService;
    @Autowired
    private VcodeActivityVpointsCogService vpointsCogService;
    @Autowired
    private IVpointsGoodsExchangeActivityCogService exchangeActivityCogService;
    @Autowired
    private IVpointsGoodsHalfPriceActivityCogService halfPriceActivityCogService;
    @Autowired
    private IVpointsGoodsGiftCardCogService goodsGiftCardCogService;



	
	/**
	 * 新增兑换商品
	 * @param info
	 * @return
	 */
	public String addGoods(VpointsGoodsInfo info) {
        
        // 支付价格转换成分
        info.setGoodsPay((long)MathUtil.round(info.getGoodsPayMoney() * 100, 0));
        
        // 图片负载
        info.setGoodsContent(HttpReq.replaceImgUr(info.getGoodsContent()));
        
        goodsDao.addGoods(info);
        String isSuccess="addSuccess";
		logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_1,
                JSON.toJSONString(info), "创建兑换商品:" + info.getGoodsName());
		return isSuccess;
	}
	/**
	 * 更新商品
	 * @param info
	 * @return
	 */
	public String updateGoods(VpointsGoodsInfo info){
		
        // 支付价格转换成分
        info.setGoodsPay((long)MathUtil.round(info.getGoodsPayMoney() * 100, 0));
        
        info.setShowSales(StringUtils.defaultIfBlank(info.getShowSales(), null));
        
        // 图片负载
        info.setGoodsContent(HttpReq.replaceImgUr(info.getGoodsContent()));
        
        String isSuccess="addSuccess";
        goodsDao.updateGoods(info);
		logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_2,
                JSON.toJSONString(info), "更新兑换商品:" + info.getGoodsName());
		
		// 到货通知
		if ("1".equals(info.getArrivalNoticeFlag())) {
		    goodsConsumerStatusInfoService.noticeByGoodsId(info);
		}
		return isSuccess;
	}
	
	/**
	 * 删除商品
	 * @param goodsId
	 * @return
	 */
	public String delGoods(String goodsId){
		String isSuccess="deleteSuccess";
		goodsDao.delGoodsById(goodsId);
		logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_3,
				goodsId, "删除兑换商品:" + goodsId);
		return isSuccess;
	}
	/**
	 * 商品列表
	 * @param info
	 * @param bean
	 * @return
	 */
	public List<VpointsGoodsInfo> getGoodsList(PageOrderInfo info, VpointsGoodsInfo bean){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("param", info);
		map.put("bean", bean);
		return goodsDao.getGoodsList(map);
	}
	/**
	 * 商品总数
	 * @param info
	 * @param bean
	 * @return
	 */
	public int getGoodsCount(PageOrderInfo info, VpointsGoodsInfo bean){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("param", info);
		map.put("bean", bean);
		return goodsDao.getGoodsCount(map);
	}
	/**
	 * 获取一级分类
	 * @return
	 */
	public List<VpointsCategoryType> getFirstCategoryList(){
		return goodsDao.getFirstCategoryList();
	}
	/**
	 * 根据父ID获取下级分类
	 * @param parentId
	 * @return
	 */
	public List<VpointsCategoryType> getCategoryByParent(String parentId){
		return goodsDao.getCategoryByParent(parentId);
	}
	/**
	 * 公司列表
	 * @return
	 */
	public List<CompanyInfo> getCompanyList(){
		return goodsDao.getCompanyList();
	}
	/**
	 * 批次列表
	 * @param companyKey
	 * @return
	 */
	public List<VpointsCouponBatch> getBatchList(){
		return goodsDao.getBatchList();
	}
	/**
	 * 批次电子券已使用数量
	 * @param batchKey
	 * @param goodsId
	 * @return
	 */
	public int getUseCount(String batchKey,String goodsId){
		return goodsDao.getUseCount(batchKey,goodsId);
	}
	
	/**
	 * 依据品牌ID获取商品信息
	 * @param brandId
	 * @return
	 */
	public List<VpointsGoodsInfo> queryGoodsByBrandId(String brandId) {
	    Map<String, Object> map = new HashMap<>();
	    map.put("brandId", brandId);
	    return goodsDao.queryGoodsByBrandId(map);
	}
	
	/**
	 * 依据品牌父ID获取子品牌信息
	 * @param parentId
	 * @return
	 */
	public List<VpointsBrandInfo> queryBrandByParentId(String parentId) {
	    Map<String, Object> map = new HashMap<>();
	    map.put("parentId", parentId);
	    return goodsDao.queryBrandByParentId(map);
	}
	
	/**
	 * 依据品牌父ID获取当前登录用户名可查看的子品牌信息
	 * @param parentId
	 * @param userName 登录用户名
	 * @return
	 */
	public List<VpointsBrandInfo> queryBrandByParentId(String parentId, String userName) {
	    List<VpointsBrandInfo> brandInfoLst = new ArrayList<>();
	    
	    // 获取品牌信息
        List<VpointsBrandInfo> tempGoodsLst = queryBrandByParentId(parentId);
	    
	    // 获取各品牌用户可查询品牌配置
	    String[] brandLimitAry = DatadicUtil.getDataDicValue(DatadicUtil.dataDicCategory
	            .VPOINTS_ESTORE_COG, DatadicUtil.dataDic.vpointsEstoreCog.vpointsBrandLimit).split(";");
	    String[] itemAry = null;
	    String[] brandAry = null;
	    for (String item : brandLimitAry) {
            itemAry = item.split(":");
            if (userName.equals(itemAry[0])) {
                brandAry = itemAry[1].split(",");
                if ("ALL".equals(brandAry[0])) {
                    brandInfoLst = tempGoodsLst;
                } else {
                    for (VpointsBrandInfo brandItem : tempGoodsLst) {
                        for (String brandName : brandAry) {
                            if (brandName.equals(brandItem.getBrandName())) {
                                brandInfoLst.add(brandItem);
                            }
                        }
                    }
                }
                break;
            }
        }
	    
	    return brandInfoLst;
	}
	
	/**
	 * 依据商品客户自定义编号获取
	 */
	public VpointsGoodsInfo findByGoodsClientNo(String goodsClientNo) {
	    Map<String, Object> map = new HashMap<>();
	    map.put("goodsClientNo", goodsClientNo);
	    return goodsDao.findByGoodsClientNo(map);
	}
	
	/**
	 * 校验商品客户自定义编号是否已存在
	 * 
	 * @param goodsId  编辑时当前记录主键
	 * @param goodsClientNo    商品客户自定义编号
	 */
	public void validGoodsClientNo(String goodsId, String goodsClientNo) {
	    VpointsGoodsInfo goodsInfo = findByGoodsClientNo(goodsClientNo);
	    if (goodsInfo != null && !goodsInfo.getGoodsId().equals(goodsId)) {
	        throw new BusinessException("添加失败" + goodsInfo.getGoodsClientNo() + goodsInfo.getGoodsShortName() + "已存在");
	    }
	}
	
	/**
	 * 清除尊享卡相关缓存
	 */
	public void removeExchangeCardCache() throws Exception {
	    Map<String, String> prizeMap = vpointsCogService.queryBigPrizeForZunXiang();
	    try {
    	    for (String prizeType : prizeMap.keySet()) {
                CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.shoppingMallKey.EXCHANGE_CARD_TYPE 
                        + Constant.DBTSPLIT + prizeType + Constant.DBTSPLIT + DateUtil.getDateTime("yyyyMMdd"));
            }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * 获取商品列表
	 * @param brandId
	 * @return
	 */
	public List<VpointsGoodsInfo> queryGoodsList(String unexpiredFalg, List<String> goodsIdLst) {
		return queryGoodsList(Constant.exchangeChannel.CHANNEL_1, unexpiredFalg, goodsIdLst);
	}
	
	/**
	 * 获取商品列表
	 * @param brandId
	 * @return
	 */
	public List<VpointsGoodsInfo> queryGoodsList(String exchangeChannel, String unexpiredFalg, List<String> goodsIdLst) {
	    // 商品渠道
        List<String> exchangeChannelLst = new ArrayList<>();
        if ("hebei".equals(DbContextHolder.getDBType())) {
            exchangeChannelLst.add(Constant.exchangeChannel.CHANNEL_6);
        } else {
            exchangeChannelLst.add(exchangeChannel);
        }
        
	    Map<String, Object> map = new HashMap<>();
	    map.put("exchangeChannelLst", exchangeChannelLst);
	    if(StringUtils.isNotBlank(unexpiredFalg)) {
	    	map.put("unexpiredFalg", unexpiredFalg);
	    }
	    map.put("goodsIdLst", goodsIdLst);
	    return goodsDao.queryGoodsList(map);
	}
    
    /**
     * 查询指定省份配送商品列表
     * @param provinceLst
     * @return
     */
    public List<VpointsGoodsInfo> queryByShipmentProvince(String shipmentProvince, String goodsId) {
        
        // 获取有效商品列表
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("exchangeChannel", "hebei".equals(DbContextHolder.getDBType()) ? Constant.exchangeChannel.CHANNEL_6 : Constant.exchangeChannel.CHANNEL_1);
        map.put("shipmentProviceLst", Arrays.asList(StringUtils.defaultString(shipmentProvince).split(",")));
        List<VpointsGoodsInfo> goodsInfoLst = goodsDao.queryByShipmentProvince(map);
        
        // 编辑时无效SKU的回显
        if (StringUtils.isNotBlank(goodsId)) {
            if (goodsInfoLst == null) goodsInfoLst = new ArrayList<VpointsGoodsInfo>();
            List<String> validGoodsIdLst = ReflectUtil.getFieldsValueByName("goodsId", goodsInfoLst);
            
            VpointsGoodsInfo goodsItem = null;
            List<String> goodsIdLst = Arrays.asList(goodsId.split(","));
            for (String itemId : goodsIdLst) {
                if (!validGoodsIdLst.contains(itemId)) {
                    goodsItem = goodsDao.getGoodsInfo(itemId);
                    goodsItem.setGoodsName(goodsItem.getGoodsName() + "(已无效)");
                    goodsInfoLst.add(goodsItem);
                }
            }
        }
        
        return goodsInfoLst;
    }
	
	public VpointsGoodsInfo getGoodsInfo(String goodsId) {
		return goodsDao.getGoodsInfo(goodsId);
	}

	public List<VpointsGoodsInfo> getAllGoods() {
		return goodsDao.getAllGoods();
	}
	public List<VpointsGoodsInfo> getAllGoodsForJob() {
		return goodsDao.getAllGoodsForJob();
	}

	public void updateGoodShowSales(Map<String, String> map) {
		goodsDao.updateGoodShowSales(map);
	}
	
	/**
	 * 预扣商品库存
	 * @param goodsId
	 * @param goodsRemains
	 */
	public void updateGoodsRemainsForWithholding(String goodsId, int goodsRemains) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("goodsId", goodsId);
	    map.put("goodsRemains", goodsRemains);
	    goodsDao.updateGoodsRemainsForWithholding(map);
	}

	/**
	 * 查询可关联赠品的商品
	 * @return
	 */
	public List<VpointsGoodsInfo> findByGoodsAsGift(Map<String, Object> map) {
		return goodsDao.findByGoodsAsGift(map);
	}

	public BaseResult<List<VpointsGoodsInfo>> getShopGoods(VpointsGoodsInfo vpointsGoodsInfo) {

//		vpointsGoodsInfo.setSecKill("1");
//		vpointsGoodsInfo.setIsGroupBuying("0");
		List<VpointsCategoryType> firstCategoryList = getFirstCategoryList();
		// 获取商品信息
		List<VpointsGoodsInfo> goodsLst = goodsDao.getShopGoods(vpointsGoodsInfo);
		Map<String, String> addHash = new HashMap<>();
		firstCategoryList.stream().forEach(cate -> {
			addHash.put(cate.getCategoryType(), cate.getCategoryName());
		});
		goodsLst.stream().forEach(goods->{
			goods.setCategoryParent(addHash.get(goods.getCategoryParent()));
		});
		List<String> goodsIdList = new ArrayList<>();
		if (StringUtils.isNotEmpty(vpointsGoodsInfo.getGoodsIdArrayStr())) {
			String[] split = StringUtils.split(vpointsGoodsInfo.getGoodsIdArrayStr(), ",");
			goodsIdList = Arrays.asList(split);
		}
		List<VpointsGoodsInfo> newList = new ArrayList<>();
		//去掉折扣活动商品
		if ("1".equals(vpointsGoodsInfo.getHalfPriceActivityType())) {
			List<VpointsGoodsHalfPriceActivityCogVO> currentActivity = halfPriceActivityCogService.getCurrentActivity();
			List<String> collect = currentActivity.stream().map(VpointsGoodsHalfPriceActivityCogVO::getGoodsId).collect(Collectors.toList());
			List<String> finalGoodsIdList = goodsIdList;
			goodsLst = goodsLst.stream().filter(filter -> {
				if (!collect.contains(filter.getGoodsId()) || finalGoodsIdList.contains(filter.getGoodsId())) {
					return true;
				}
				return false;
			}).collect(Collectors.toList());

		}
		////去掉换购活动商品
		if ("1".equals(vpointsGoodsInfo.getExchangeActivityType())) {
			List<VpointsGoodsExchangeActivityCogEntity> currentActivity = exchangeActivityCogService.getCurrentActivity();
			List<String> collect = currentActivity.stream().map(VpointsGoodsExchangeActivityCogEntity::getActivityGoodsId).collect(Collectors.toList());
			List<String> finalGoodsIdList = goodsIdList;
			goodsLst = goodsLst.stream().filter(filter -> {
				if (!collect.contains(filter.getGoodsId()) || finalGoodsIdList.contains(filter.getGoodsId())) {
					return true;
				}
				return false;
			}).collect(Collectors.toList());
		}
		//去掉礼品卡活动商品
		if ("1".equals(vpointsGoodsInfo.getIsGiftCard())) {
			List<VpointsGoodsGiftCardCogVO> currentActivity = goodsGiftCardCogService.getCurrentActivity();
			List<String> collect = currentActivity.stream().map(VpointsGoodsGiftCardCogVO::getGoodsId).collect(Collectors.toList());
			List<String> finalGoodsIdList = goodsIdList;
			goodsLst = goodsLst.stream().filter(filter -> {
				if (!collect.contains(filter.getGoodsId()) || finalGoodsIdList.contains(filter.getGoodsId())) {
					return true;
				}
				return false;
			}).collect(Collectors.toList());
		}
		// 返回查询结果
		BaseResult<List<VpointsGoodsInfo>> result= new BaseResult<>();
		result.setReply(goodsLst);
		return result.initReslut(Constant.ResultCode.SUCCESS, Constant.ResultCode.SUCCESS, "查询成功");
	}
}
