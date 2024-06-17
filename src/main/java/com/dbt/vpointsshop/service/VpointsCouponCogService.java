package com.dbt.vpointsshop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ReflectUtil;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.VpointsCouponCog;
import com.dbt.vpointsshop.bean.VpointsCouponCogGoodsLimit;
import com.dbt.vpointsshop.bean.VpointsCouponCollect;
import com.dbt.vpointsshop.dao.IVpointsCouponCogDao;

/**
 * 商城优惠券配置表Service
 */
@Service
public class VpointsCouponCogService extends BaseService<VpointsCouponCog> {

	@Autowired
	private IVpointsCouponCogDao couponCogDao;

	public VpointsCouponCog findByCouponKey(String couponKey) {
	    VpointsCouponCog couponCog = couponCogDao.findById(couponKey);
	    List<VpointsCouponCogGoodsLimit> goodsLimitLst = couponCogDao.queryGoodsLimitByCouponKey(couponKey);
	    if (CollectionUtils.isNotEmpty(goodsLimitLst)) {
	        couponCog.setGoodsIdAry(ReflectUtil.getFieldsValueByName("goodsId", goodsLimitLst).toArray(new String[] {}));
	    }
	    return couponCog;
	}
	
	public int deleteById(String couponKey, String userKey) {
	    Map<String, Object> map = new HashMap<>();
	    map.put("couponKey", couponKey);
	    map.put("updateUser", userKey);
	    map.put("updateTime", DateUtil.getDateTime());
	    return couponCogDao.deleteById(map);
	}

	/**
	 * 活动列表
	 */
	public List<VpointsCouponCog> queryVcodeActivityList(
	        VpointsCouponCog queryBean, PageOrderInfo pageInfo) throws Exception {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryBean", queryBean);
		queryMap.put("pageInfo", pageInfo);
		return couponCogDao.loadActivityList(queryMap);
	}

	/**
	 * 活动列表条数
	 * 
	 * @param queryBean
	 * @return
	 */
	public int countVcodeActivityList(VpointsCouponCog queryBean) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryBean", queryBean);
		return couponCogDao.countActivityList(queryMap);
	}
	
	/**
	 * 更新优惠券相关商品限制
	 * 
	 * @param couponKey    优惠券主键
	 * @param goodsIdAry   相关商品主键
	 * @param optUser      操作人
	 */
	private void initCouponGoodsLimit(String couponKey, String[] goodsIdAry, String optUser) {
	    
	    // 删除之前的配置
	    couponCogDao.removeGoodsLimitByCouponKey(couponKey);
	    
	    // 批量插入优惠券的商品限制
	    if (goodsIdAry != null && goodsIdAry.length > 0) {
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("couponKey", couponKey);
	        map.put("goodsIdAry", goodsIdAry);
	        map.put("createUser", optUser);
	        map.put("createTime", DateUtil.getDateTime());
	        couponCogDao.batchWritGoodsLimit(map);
	    }
	}

	/**
	 * 创建
	 */
	public void addCouponCog(VpointsCouponCog couponCog, SysUserBasis currUser) throws Exception {
	    // 初始化活动主键
	    couponCog.setCouponKey(UUID.randomUUID().toString());
	    // 生成编号
	    couponCog.setCouponNo(getBussionNo("couponCog", "coupon_no", Constant.OrderNoType.type_YHQ));
	    couponCog.fillFields(currUser.getUserKey());
        initProperties(couponCog);
	    couponCogDao.create(couponCog);
	    
	    // 初始化优惠券的商品限制
	    this.initCouponGoodsLimit(couponCog.getCouponKey(), couponCog.getGoodsIdAry(), currUser.getUserKey());
	    
	    logService.saveLog("couponCog", Constant.OPERATION_LOG_TYPE.TYPE_1, JSON.toJSONString(couponCog), "创建商城优惠券");
	}

	/**
	 * 更新
	 */
    public void updateCouponCog(VpointsCouponCog couponCog, SysUserBasis currUser) throws Exception {
        VpointsCouponCog oldCouponCog = this.findByCouponKey(couponCog.getCouponKey());
        if (oldCouponCog == null) throw new BusinessException("优惠券不存在");
        if (Integer.valueOf(StringUtils.defaultIfBlank(oldCouponCog.getCouponReceiveNum(), "0")) 
                > Integer.valueOf(StringUtils.defaultIfBlank(couponCog.getCouponNum(), "0"))) {
            throw new BusinessException("发行量不能小于已领取量，优惠券已领取" + oldCouponCog.getCouponReceiveNum() + "张");
        }
        
	    // 更新
        couponCog.fillUpdateFields(currUser.getUserKey());
        initProperties(couponCog);
	    couponCogDao.update(couponCog);

        // 初始化优惠券的商品限制
        this.initCouponGoodsLimit(couponCog.getCouponKey(), couponCog.getGoodsIdAry(), currUser.getUserKey());
        
        logService.saveLog("couponCog", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(couponCog), "修改商城优惠券");
	}
	
	/** 
	 * 删除
	 */
	public void deleteCouponCog(String couponKey, SysUserBasis currUser) throws Exception {
	    VpointsCouponCog couponCog = this.findByCouponKey(couponKey);
	    if (couponCog == null) throw new BusinessException("优惠券不存在");
	    
	    if (DateUtil.getDate().compareTo(couponCog.getStartDate()) >= 0) {
	        throw new BusinessException("已开始或结束的优惠券不能删除");
	    }
        
        if (Integer.valueOf(StringUtils.defaultIfBlank(couponCog.getCouponReceiveNum(), "0")) > 0) {
            throw new BusinessException("优惠券已被领取过不能删除");
        }
	    
	    // 删除活动及商品关系
	    this.deleteById(couponKey, currUser.getUserKey());

        logService.saveLog("couponCog", Constant.OPERATION_LOG_TYPE.TYPE_3, JSON.toJSONString(couponCog), "删除商城优惠券");
	}

	/**
	 * 数据汇总
	 * @param couponKey
	 * @return
	 */
	public VpointsCouponCollect findCouponCollect(String couponKey) {
		return couponCogDao.findCouponCollect(couponKey);
	}
	
	private void initProperties(VpointsCouponCog couponCog) {
	    if (couponCog == null) return;
	    couponCog.setExpireDateDays(StringUtils.defaultIfBlank(couponCog.getExpireDateDays(), null));
	    couponCog.setPersonDayLimit(StringUtils.defaultIfBlank(couponCog.getPersonDayLimit(), null));
	    couponCog.setPersonWeekLimit(StringUtils.defaultIfBlank(couponCog.getPersonWeekLimit(), null));
	    couponCog.setPersonMonthLimit(StringUtils.defaultIfBlank(couponCog.getPersonMonthLimit(), null));
	    couponCog.setPersonTotalLimit(StringUtils.defaultIfBlank(couponCog.getPersonTotalLimit(), null));
        couponCog.setExpireRemindDay(StringUtils.defaultIfBlank(couponCog.getExpireRemindDay(), null));
        
        if (ArrayUtils.isNotEmpty(couponCog.getGoodsIdAry())) {
            List<String> goodsIdLst = new ArrayList<String>();
            for (String item : couponCog.getGoodsIdAry()) {
               if (!goodsIdLst.contains(item)) goodsIdLst.add(item);
            }
            String[] goodsIdAry = goodsIdLst.toArray(new String[] {});
            couponCog.setGoodsId(StringUtils.join(goodsIdAry, ","));
            couponCog.setGoodsIdAry(goodsIdAry);
        }
        
        if (ArrayUtils.isNotEmpty(couponCog.getAreaValidLimitAry())) {
            couponCog.setAreaValidLimit(StringUtils.join(couponCog.getAreaValidLimitAry(), ","));
        }
	}
	
	/**
	 * 获取商城优惠券
	 * @param allFlag
	 * @param couponNoLst
	 * @return
	 */
	public List<VpointsCouponCog> queryValidCouponList(boolean allFlag, List<String> couponNoLst) {
	    Map<String, Object> map = new HashMap<>();
	    map.put("allFlag", allFlag ? "1" : "0");
	    map.put("couponNoLst", couponNoLst);
	    return couponCogDao.queryValidCouponList(map);
	}
}
