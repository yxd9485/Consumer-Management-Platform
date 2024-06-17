package com.dbt.vpointsshop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.vpointsshop.bean.CouponBean;
import com.dbt.vpointsshop.bean.VpointsCouponBatch;
import com.dbt.vpointsshop.dao.VpointsCouponDao;

@Service
public class CouponService extends BaseService<VpointsCouponBatch>{
	@Autowired
	private VpointsCouponDao couponDao;
	/**
	 * 电子券批次列表
	 * @param info
	 * @param bean
	 * @return
	 */
	public List<VpointsCouponBatch> getBatchList(PageOrderInfo info, VpointsCouponBatch bean){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("param", info);
		map.put("bean", bean);
		return couponDao.getBatchList(map);
	}
	/**
	 * 电子券批次数量
	 * @param info
	 * @param bean
	 * @return
	 */
	public int getBatchCount(PageOrderInfo info, VpointsCouponBatch bean){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("param", info);
		map.put("bean", bean);
		return couponDao.getBatchCount(map);
	}
	/**
	 * 删除电子券批次
	 * @param batchKey
	 * @return
	 */
	public String delBatch(String batchKey){
		String isSuccess="deleteSuccess";
		int count=couponDao.getGoodsByBatch(batchKey);//批次是否有商品使用
		String batchTable=couponDao.getTalbeByBatch(batchKey);//批次对应表
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("batchTable", batchTable);
		map.put("batchKey", batchKey);
		int hcount=couponDao.getCouponByBatch(map);//批次是否有券被使用
		if(count>0||hcount>0){
			return "deleteFalse";
		}
		couponDao.delBatchByKey(batchKey);
		couponDao.delCouponByBatch(map);
		logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_3,
				batchKey, "删除电子券批次:" + batchKey);
		return isSuccess;
	}
	/**
	 * 添加批次
	 * @param batch
	 * @return
	 */
	public String addBatch(VpointsCouponBatch batch){
		String result="addSuccess";
		couponDao.saveBatch(batch);
		logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_1,
                JSON.toJSONString(batch), "创建电子券批次:" + batch.getBatchName());
		return result;
	}
	/**
	 * 添加电子券
	 * @param batchKey
	 * @param couponList
	 * @param userKey
	 * @return
	 */
	public String addCoupon(String batchKey,List<String> couponList,String userKey){
		// 入库
		String batchTable=couponDao.getTalbeByBatch(batchKey);//批次对应表
		List<CouponBean> list=new ArrayList<CouponBean>();
		CouponBean coupon;
		List<String> redisList=new ArrayList<String>();
		for (int i = 0,a=couponList.size(); i < a; i++) {
			coupon=new CouponBean();
			String vcode=couponList.get(i);
			coupon.setBatchKey(batchKey);
			coupon.setCouponVcode(vcode);
			coupon.fillFields(userKey);
			list.add(coupon);
			redisList.add(vcode);
		}
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("couponList", list);
        map.put("batchTable", batchTable);
        couponDao.saveCoupon(map);
        String couponListKey=RedisApiUtil.CacheKey.vpointsShop.COUPON+batchKey;
        RedisApiUtil.getInstance().addList(couponListKey, couponList.toArray(new String[couponList.size()]));//将列表放入redis
        logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_1,
                JSON.toJSONString(couponList), "创建电子券");
		return "addSuccess";
	}
	/**
	 * 更新批次
	 * @param batch
	 * @return
	 */
	public String updateBatch(VpointsCouponBatch batch){
		String isSuccess="addSuccess";
		couponDao.updateBatch(batch);
		logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_2,
                JSON.toJSONString(batch), "更新电子券：" + batch.getBatchName());
		return isSuccess;
	}
}
