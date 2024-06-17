package com.dbt.vpointsshop.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.VpointsGoodsInfo;
import com.dbt.vpointsshop.bean.VpointsGroupBuyingActivityCog;
import com.dbt.vpointsshop.bean.VpointsSeckillActivityCog;
import com.dbt.vpointsshop.dao.IVpointsGroupBuyingActivityCogDao;
import com.dbt.vpointsshop.dao.IVpointsSeckillActivityCogDao;
import com.dbt.vpointsshop.util.PunchinImg;
import com.dbt.vpointsshop.util.impl.GoodsShareImgType;

/**
 *  商城秒杀活动Service
 * @author Administrator
 *
 */
@Service
public class VpointsGroupBuyingActivityService extends BaseService<VpointsGroupBuyingActivityCog> {

	@Autowired
	private IVpointsGroupBuyingActivityCogDao groupBuyingActivityCogDao;
	@Autowired
	private IVpointsSeckillActivityCogDao seckillActivityCogDao;
	@Autowired
	private VpointsGoodsService goodsService;
	
	/**
	 * 获取某个V码活动
	 * 
	 * @param vcodeActivityKey
	 * @return
	 */
	public VpointsGroupBuyingActivityCog loadActivityByKey(String infoKey) {
		VpointsGroupBuyingActivityCog activityCog = groupBuyingActivityCogDao.findById(infoKey);
	    return activityCog;
	}

	/**
	 * 活动列表
	 * 
	 * @param queryBean
	 * @param pageInfo
	 * @return
	 * @throws Exception 
	 */
	public List<VpointsGroupBuyingActivityCog> findVcodeActivityList(
			VpointsGroupBuyingActivityCog queryBean, PageOrderInfo pageInfo) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		List<VpointsGroupBuyingActivityCog> activityList = groupBuyingActivityCogDao.queryForLst(map);
        
        if(null != activityList && !activityList.isEmpty()){
            Object cacheObj = null;
            VpointsGroupBuyingActivityCog configItem = null;
            String currDate = DateUtil.getDate();
            String cacheStr = CacheKey.cacheKey.vcode.KEY_VCODE_ACTIVITY_COG_NEW + Constant.DBTSPLIT;
            for (VpointsGroupBuyingActivityCog item : activityList) {
                // 校验缓存
                cacheObj = CacheUtilNew.getCacheValue(cacheStr + item.getInfoKey());
                if(null == cacheObj) continue;
                configItem = JSON.parseObject(cacheObj.toString(), VpointsGroupBuyingActivityCog.class);
                if(null == configItem) continue;
                if(!item.getBeginDate().equals(configItem.getBeginDate())
                        || !item.getEndDate().equals(configItem.getEndDate())){
                    
                    if (currDate.compareTo(configItem.getEndDate()) > 0) {
                        item.setIsBegin("缓存异常(已结束)");
                        
                    } else if (currDate.compareTo(configItem.getBeginDate()) < 0){
                        item.setIsBegin("缓存异常(未开始)");
                        
                    } else {
                        item.setIsBegin("缓存异常(进行中)");
                    }
                }
            }
        }
		return activityList;
	}
	/**
	 * 活动列表条数
	 * 
	 * @param queryBean
	 * @return
	 */
	public int countVcodeActivityList(VpointsGroupBuyingActivityCog queryBean) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("queryBean", queryBean);
		return groupBuyingActivityCogDao.queryForCount(map);
	}

	/**
	 * 创建活动
	 * 
	 * @param activityCog
	 * @param currentUserKey
	 */
	public void writeActivityCog(VpointsGroupBuyingActivityCog activityCog, SysUserBasis optUser) throws Exception {
	    
	    // 校验名称是否重复
	    if ("1".equals(checkBussionName("groupBuyingActivityCog", "info_key", null, "activity_name", activityCog.getActivityName()))) {
	        throw new BusinessException("活动名称已存在");
	    }
	    
	    // 检验当前商品是否存在时间范围冲突
	    String errorInfo = checkActivityForGroupBuying(activityCog);
	    if(StringUtils.isNotBlank(errorInfo)) {
	    	throw new BusinessException(errorInfo);
	    }
	    
	    // 生成分享图片
	    // String shareImgUrl = setGoodsShareImg(activityCog);
	    String shareImgUrl = null;
	    activityCog.setShareImgUrl(shareImgUrl);
	    
	    // 完善活动信息
	    activityCog.setCrowdLimitType("0");
		activityCog.setInfoKey(UUID.randomUUID().toString());
        activityCog.setActivityNo(getBussionNo("groupBuyingActivityCog", "activity_no", Constant.OrderNoType.type_PT));
        activityCog.setGroupBuyingPay(new BigDecimal(activityCog.getGroupBuyingPay())
        		.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_UP).toString());
		activityCog.fillFields(optUser.getUserKey());

		// 保存活动
		groupBuyingActivityCogDao.create(activityCog);
		logService.saveLog("groupBuyingActivityCog", Constant.OPERATION_LOG_TYPE.TYPE_1, 
		        JSON.toJSONString(activityCog), "创建活动:" + activityCog.getActivityName());
		
		// 预扣商品库存
	    goodsService.updateGoodsRemainsForWithholding(activityCog.getGoodsId(), activityCog.getGroupBuyingTotalNum());
	}

	/**
	 * 更新活动
	 * 
	 * @param activityCog
	 * @param currentUserKey
	 * @throws IOException 
	 */
	public void updateActivityCog(VpointsGroupBuyingActivityCog activityCog, SysUserBasis optUser) throws Exception {
        
		// 校验名称是否重复
	    if ("1".equals(checkBussionName("groupBuyingActivityCog", "info_key", activityCog.getInfoKey(), "activity_name", activityCog.getActivityName()))) {
	        throw new BusinessException("活动名称已存在");
	    }
        
	    // 检验当前商品是否存在时间范围冲突
	    String errorInfo = checkActivityForGroupBuying(activityCog);
	    if(StringUtils.isNotBlank(errorInfo)) {
	    	throw new BusinessException(errorInfo);
	    }
	    
	    // 生成分享图片
//	    String shareImgUrl = setGoodsShareImg(activityCog);
	    String shareImgUrl = null;
	    activityCog.setShareImgUrl(shareImgUrl);
	    
        // 保存活动
	    activityCog.setCrowdLimitType("0");
	    activityCog.setGroupBuyingPay(new BigDecimal(activityCog.getGroupBuyingPay())
	    		.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_UP).toString());
        activityCog.fillUpdateFields(optUser.getUserKey());
        groupBuyingActivityCogDao.update(activityCog);
       
        // 删除活动缓存
        CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode
               .KEY_VCODE_ACTIVITY_COG_NEW + Constant.DBTSPLIT + activityCog.getInfoKey());
       
       logService.saveLog("groupBuyingActivityCog", Constant.OPERATION_LOG_TYPE.TYPE_2,
               JSON.toJSONString(activityCog), "修改活动:" + activityCog.getActivityName());
    }

	/**
	 * 判断商品及时间范围是否合法
	 * @param activityKey</br>
	 * @param goodsId</br>
	 * @param ruleType</br> 时间类型：1时间段，2周几
	 * @param startDate</br>
	 * @param endDate</br> 
	 * @return String flag 返回异常信息</br>
	 * @throws Exception 
	 */
	public String checkActivityForGroupBuying(VpointsGroupBuyingActivityCog activityCogInsert) throws Exception {
		String flag = "";
		
		// 判断秒杀
		List<VpointsSeckillActivityCog> activityCogList = seckillActivityCogDao.queryByGoodsId(activityCogInsert.getGoodsId());
		if(CollectionUtils.isNotEmpty(activityCogList)) {
			for (VpointsSeckillActivityCog item : activityCogList) {
				
				if("1".equals(checkActivityTime(activityCogInsert, item.getRuleType(), item.getBeginDate(), item.getEndDate(), item.getIsBegin()))) {
					flag = "与秒杀活动["+item.getActivityName()+"]存在时间冲突";
					break;
				}
			}
		}
		
		if(StringUtils.isBlank(flag)) {
			// 判断拼团
			List<VpointsGroupBuyingActivityCog> groupBuyingActivityList = groupBuyingActivityCogDao.queryByGoodsId(activityCogInsert.getGoodsId());
			if(CollectionUtils.isNotEmpty(groupBuyingActivityList)) {
				for (VpointsGroupBuyingActivityCog item : groupBuyingActivityList) {
					if(item.getInfoKey().equals(activityCogInsert.getInfoKey())) continue;
					
					if("1".equals(checkActivityTime(activityCogInsert, item.getRuleType(), item.getBeginDate(), item.getEndDate(), item.getIsBegin()))) {
						flag = "与拼团活动["+item.getActivityName()+"]存在时间冲突";
						break;
					}
				}
			}
		}
		return flag;
	}
	
	/**
	 * 判断商品及时间范围是否合法
	 * @param goodsId</br>
	 * @param ruleType</br> 时间类型：1时间段，2周几
	 * @param startDate</br>
	 * @param endDate</br> 
	 * @return String flag 返回异常信息</br>
	 * @throws Exception 
	 */
	public String checkActivityTime(VpointsGroupBuyingActivityCog activityCogInsert, String dbRuleType, String dbBeginDate, String dbEndDate, String dbIsBegin) throws Exception {
		String flag = "0";
		String ruleType = activityCogInsert.getRuleType(); 
		String beginDate = activityCogInsert.getBeginDate();
		String endDate = activityCogInsert.getEndDate();
		
		// 已过期
		if("2".equals(dbIsBegin)) {
			return flag;
		}
		
		// 新增活动时间为时间段
		if("1".equals(ruleType)) {
			
			// 数据库数据为时间段
			if("1".equals(dbRuleType)) {
				// as>be || ae<bs
				if(beginDate.compareTo(dbEndDate) > 0 || endDate.compareTo(dbBeginDate) < 0) {
					flag = "0";
				}else {
					flag = "1";
				}
				
			// 数据库数据为周几
			}else if("2".equals(dbRuleType)){
				Date newBeginDate = DateUtil.parse(beginDate, DateUtil.DEFAULT_DATE_FORMAT);
				Date newEndDate = DateUtil.parse(endDate, DateUtil.DEFAULT_DATE_FORMAT);
				long day = DateUtil.diffDays(newEndDate, newBeginDate);
				for (int i = 0; i <= day; i++) {
					int week = DateUtil.getDayOfWeek(DateUtil.addDays(newBeginDate, i));
					if(week >= Integer.parseInt(dbBeginDate) && week <= Integer.parseInt(dbEndDate)) {
						flag = "1";
						break;
					}
				}
			}
			
		// 新增活动时间为周几
		}else {
			// 数据库数据为时间段
			if("1".equals(dbRuleType)) {
				Date newBeginDate = DateUtil.parse(dbBeginDate, DateUtil.DEFAULT_DATE_FORMAT);
				Date newEndDate = DateUtil.parse(dbEndDate, DateUtil.DEFAULT_DATE_FORMAT);
				long day = DateUtil.diffDays(newEndDate, newBeginDate);
				for (int i = 0; i <= day; i++) {
					int week = DateUtil.getDayOfWeek(DateUtil.addDays(newBeginDate, i));
					if(week >= Integer.parseInt(beginDate) && week <= Integer.parseInt(endDate)) {
						flag = "1";
						break;
					}
				}
				
			// 数据库数据为周几
			}else if("2".equals(dbRuleType)){
				// as>be || ae<bs
				if(Integer.parseInt(beginDate) > Integer.parseInt(dbEndDate) || Integer.parseInt(endDate) < Integer.parseInt(dbBeginDate)) {
					flag = "0";
				}else {
					flag = "1";
				}
			}
		}
		return flag;
	}
	

	public VpointsGroupBuyingActivityCog findById(String vcodeActivityKey) {
		return groupBuyingActivityCogDao.findById(vcodeActivityKey);
	}

	/**
	 * 停止活动
	 * @param infoKey
	 * @throws Exception 
	 */
	public void updateActivityStop(String infoKey) throws Exception {
		groupBuyingActivityCogDao.updateActivityStop(infoKey);
		// 删除活动缓存
        CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode
               .KEY_VCODE_ACTIVITY_COG_NEW + Constant.DBTSPLIT + infoKey);
	}

	/**
	 * 删除活动
	 * @param infoKey
	 * @throws Exception 
	 */
	public void updateActivityDel(String infoKey) throws Exception {
		VpointsGroupBuyingActivityCog info = groupBuyingActivityCogDao.findById(infoKey);
		if(null != info) {
			groupBuyingActivityCogDao.updateActivityDel(infoKey);
			CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode
		               .KEY_VCODE_ACTIVITY_COG_NEW + Constant.DBTSPLIT + infoKey);
		}
	}
	
	/**
	 * 生成分享图片
	 * @param info
	 * @throws Exception 
	 */
	private String setGoodsShareImg(VpointsGroupBuyingActivityCog activityCog) throws Exception {
		
		VpointsGoodsInfo info = goodsService.getGoodsInfo(activityCog.getGoodsId());
		
		String goodsImg = info.getGoodsUrl().split(",")[0];
		// 优惠百分比
		String discounts = "";
		
		// 商品价格
		StringBuilder goodsPrizeStr = new StringBuilder(); 
		if(StringUtils.isNotBlank(activityCog.getGroupBuyingPay())){
			BigDecimal groupBuyingPay = new BigDecimal(activityCog.getGroupBuyingPay())
					.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_UP);
			goodsPrizeStr.append("￥");
			goodsPrizeStr.append(groupBuyingPay.toString());
			
			if(groupBuyingPay.compareTo(BigDecimal.ZERO) > 0) {
				discounts = new BigDecimal(info.getGoodsMoney()).subtract(groupBuyingPay).divide(new BigDecimal(info.getGoodsMoney())).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_UP).toString();
			}else {
				discounts = "100";
			}
			
		}
		if(activityCog.getGroupBuyingVpoints() > 0) {
			goodsPrizeStr.append("+");
			goodsPrizeStr.append(activityCog.getGroupBuyingVpoints());
			goodsPrizeStr.append("积分");
		}
		
        String[] tips = {"拼单立省" + discounts + "%",goodsPrizeStr.toString()};
        PunchinImg punchinImg = new GoodsShareImgType();
        String punchinImgPath = punchinImg.initImg(goodsImg, tips);
        return punchinImgPath;
	}

	public static void main(String[] args) {
		// new BigDecimal(activityCog.getSeckillPay()).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_UP).toString()
		System.out.println(new BigDecimal(125.00).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_UP).toString());
	}
}
