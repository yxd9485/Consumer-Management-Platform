/**
 * <pre>
 * Copyright Digital Bay Technology Group. Co. Ltd.All Rights Reserved.
 * 
 * Original Author: sunshunbo
 * 
 * ChangeLog:
 * 2015-5-6 by sunshunbo create
 * </pre>
 */
package com.dbt.framework.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ApplicationObjectSupport;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.cache.service.IMemcachedClient;

/**
 * <pre>
 * 缓存工具类.
 * </pre>
 *
 * @author 孙顺博 2015-1-31
 */
public class CacheUtilNew extends ApplicationObjectSupport {
    private final static Log log = LogFactory.getLog(CacheUtilNew.class);
	
	/** 0:缓存永远不失效 *. */
	public final static int SECONDS_TO_EXPIRE = 0;
	
	/** 缓存客户端对象. */
	private static IMemcachedClient mirrorCachClient = (IMemcachedClient) BeanFactoryUtil
			.getbeanFromWebContext("xmemcachedClient");
    
    private static final ThreadLocal<Set<String>> keyModelSet = new ThreadLocal<>();
    private static final ThreadLocal<Set<String>> groupModelSet = new ThreadLocal<>();
	
	/**
	 * 缓存key.
	 *
	 * @author guchangpeng
	 * @date 2014-07-15
	 */
	public static class cacheKey {
		
		/** 微信公众账号token：wctAccessToken *. */
		public static final String KEY_WCTACCESSTOKEN = "WctAccessToken";
		
		/** 数据字典缓存key:vps_sys_datadic_m *. */
		public static final String KEY_VPS_SYS_DATA_DICM = "VpsSysDatadicM";
		
		/** 数据字典类型缓存key：vps_sys_diccategory_info *. */
		public static final String KEY_VPS_SYS_DICCATEGORY_INFO = "VpsSysDiccategoryInfo";
		
		/** 验证码 */
		public static final String KEY_CAPTHCHA = "CaptchaUtil";
		/** 系统区域信息 **/
		public static final String KEY_SYS_AREA_M = "SysArea";
		
		/**
		 * 用户基础信息
		 * 
		 * @author RoyFu
		 */
		public static interface moduleUser {
			
			/** 用户信息 */
			public static final String KEY_USER_INFO = "VpsClientUserInfo";
			/** 个人账户 */
			public static final String KEY_PERSONAL_ACCOUNT = "VpsClientPersonalAccountInfo";
			/** 收获地址 */
			public static final String KEY_ADDRESS_INFO = "VpsClientAddressInfo";
			/** 设备信息 */
			public static final String KEY_DEVICE_TOKEN = "VpsClientDeviceTokenInfo";
			/** 三方账户 */
			public static final String KEY_THIRD_ACCOUNT = "VpsClientThirdAccountInfo";
			/** 固定Key|海军：发送超值返利缓存key **/
			public static final String KEY_NAVY_PREMIUMREBATE = "NavyPremiumRebate";
			/** 固定Key|海军：超值返利活动记录数 **/
			public static final String KEY_NAVY_PREMIUMERBATE_ACTIVITY_COUNT =
					"NavyPremiumRebateActivityCount";
			/** 扫海军超值返所有用户缓存key： 便于平台判断**/
			public static final String KEY_NAVY_SWEEP_NAVYPREMIUM = "SweepNavyPremium";
			/** 渠道 */
			public static final String KEY_CHANNEL = "ChannelM";
			/** 渠道与用户关系 */
			public static final String KEY_USER_CHANNEL = "UserChannelInfo";
			/** 关注 */
			public static final String KEY_SUBSCRIBE = "VpsWctSubscribeInfo";
			/** 统一活动缓存key：UnifyActivity **/
			public static final String KEY_UNIFY_ACTIVITY = "UnifyActivity";
            
            /** 酒王排名  */
            static final String VPS_VCODE_RANKING_RECORD = "vpsVcodeRankingRecord";
			
		}
		
		/**
		 * 扫码记录数
		 * @author jiquanwei
		 *
		 */
		public static interface vcodeScanCounts{
			/** 扫码次数计数**/
			public static final String KEY_VCODE_SCAN_COUNTS = "vcodeScanCounts";
			/** 二维码分表 **/
			public static final String KEY_VCODE_SCAN_SPLIT_TABLE = "keyVcodeScanSplitTable";
		}
		
		/**
		 * 数据字典信息
		 * 
		 * @author jiquanwei
		 */
		public static interface moduleDatadic {
			
			/** 数据字典 */
			public static final String KEY_DATADIC = "Vpssysdatadicm";
		}
		
		/**
		 * 消息
		 * 
		 * @author RoyFu
		 *
		 */
		public static interface moduleMsg {
			
			/** 未读消息 */
			public static final String KEY_UNREAD_MSG = "VpsPushmessageUnreadInfo";
			/** 我的消息记录 **/
			public static final String KEY_RECORDINFO = "VpsPushMessageRecordinfo";
		}

        /**
         * 活动
         * @author jiquanwei
         *
         */
        public static interface vodeActivityKey{
            /** 活动积分配置**/
            static final String KEY_VCODE_ACTIVITY_VPOINTS_COG_LIST = "VpsVcodeActivityVpointsCogList0508";
            /** 获得积分并将要减去的数量key**/
            public static final String ACTIVITY_FORVCODE_GET_POINTS_LIST = "ActivityForCodeGetPointsList0426";
            /** 活动规则List */
            static final String KEY_VCODE_ACTIVITY_REBATE_RULE_COG = "keyVcodeActivityRebateRuleCog";
            /** 无效的活动规则**/
			static final String KEY_INVALID_ACTIVITY_REBATE_RULE_COG = "keyInvalidActivityRebateRuleCog";
			/** 当天无效的活动规则**/
			static final String KEY_INVALID_EVERYDAY_ACTIVITY_REBATE_RULE_COG = "keyInvalidEveryDayActivityRebateRuleCog";
			/** 活动规则bean**/
			static final String KEY_VCODE_ACTIVITY_REBATE_RULE_BEAN = "keyVcodeActivityRebateRuleBean";
			/** 活动规则消费积分**/
			static final String KEY_REBATE_RULE_RESTRICT_VPOINTS = "keyRebateRuleRestrictVpoints0201";
			/** 活动规则消费金额**/
			static final String KEY_REBATE_RULE_RESTRICT_MONEY = "keyRebateRuleRestrictMoney0201";
			/** 活动规则消费瓶数**/
			static final String KEY_REBATE_RULE_RESTRICT_BOTTLE = "keyRebateRuleRestrictBottle0201";
			/** 当前规则下用户扫码次数**/
			static final String VCODE_USER_SCAN_QRCODE_COUNT = "vcodeUserScanQrcodeCount0201";
			/** 热区配置**/
			static final String VCODE_ACTIVITY_HOTAREA_COG = "vcodeActivityHotAreaCog";
			/** 爆点红包时间过期限制标志 **/
        	public static final String LIMIT_TIME_MARK = "limit_time_mark";
        	/** 活动的一码多扫规则 **/
			public static final String KEY_VCODE_ACTIVITY_MORESCAN_COG = "keyVcodeActivityMorescanCog";
			/**
			 * 活动的一码多扫规则 多条规则
			 */
			public static final String KEY_VCODE_ACTIVITY_MORESCAN_COGS = "keyVcodeActivityMorescanCogs";
			/**
			 * 红包雨活动
			 */
			public static final String KEY_RED_ENVELOPE_ACTIVITY_COGS = "keyRedEnvelopeRainActivityCogs";
			/** 活动的逢百规则 **/
			public static final String KEY_VCODE_ACTIVITY_PERHUNDRED_COG = "keyVcodeActivityPerhundredCog";
			/** 逢尾数规则 **/
			public static final String KEY_VCODE_PERMANTISSA_COG = "keyVcodePermantissaCog";
			/** 计算单瓶预算缓存金额**/
			static final String KEY_REBATE_RULE_BUDGET_MONEY = "keyRebateRuleBudgetMoney";
			/** 计算单瓶预算缓存瓶数**/
			static final String KEY_REBATE_RULE_BUDGET_BOTTLE = "keyRebateRuleBudgetBottle";
			/** 通用规则**/
			static final String KEY_COMMON_RULE_TYPE = "keyCommonRuleType";
			/** 阶梯规则**/
			static final String KEY_LADDER_RULE_COG = "keyLadderRuleCog";
			/** 首扫限制规则**/
			static final String KEY_REBATE_RULE_RESTRICT_FIRT = "keyRebateRuleRestrictFirst";
			/** 淘彩蛋规则**/
			static final String KEY_TAO_EASTEREGG_COG = "keyTaoEasterEggCog";
			/** 转盘活动配置缓存 **/
			public static final String  KEY_VCODE_TURNTABLE_ACTIVITY_COG = "VpsVcodeTurntableActivityCog";
			/** 天降红包活动缓存 **/
			public static final String  KEY_VPS_COMMON_RULE_COG = "keyVpsCommonRuleCog";
			/** 转盘活动奖品配置缓存 **/
			public static final String  KEY_VCODE_TURNTABLE_PRIZE_ACTIVITY_COG = "VpsVcodeTurntablePrizeActivityCog";
            /** 膨胀规则 **/
            public static final String  KEY_BENEDICTION_EXPAND_RULE = "keyBbenedictionExpandRule";
            /** 签到抽奖活动key **/
            public static final String  KEY_LOTTERY_SIGN_COG = "keyLotterySignCog";
			/** 红包雨规则消费金额**/
			static final String KEY_RED_ENVELOPE_RESTRICT_MONEY = "keyRedEnvelopeRestrictMoney";
			/** 扫码待激活红包配置关联的sku主键 **/
			static final String KEY_WAIT_ACTIVATION_SKU_KEY = "keyWaitActivationSkuKey";
			/** 扫码待激活红包配置关联的sku名称 **/
			static final String KEY_WAIT_ACTIVATION_SKU_NAME = "keyWaitActivationSkuName";
        }
        
        /**
         * 逢百中奖配置
         * @auther hanshimeng </br>
         * @version V1.0.0 </br>      
         * @createTime 2017年8月23日 </br>
         */
        public static interface dotRedpacketCog{
        
        	/** 逢百规则当天有效标志 **/
        	public static final String DOT_RULE_IS_VALID = "dot_ruleIs_valid";
        	/** 逢百规则中奖统计信息 **/
        	public static final String DOT_RULE_TOTAL_PRIZE = "dot_rule_total_prize";
        	/** 红包规则 **/
        	public static final String DOT_REDPACKET_RULE = "dot_redpacket_rule";
        	
        	/** 中奖时间 **/
        	public static final String DOT_PRIZE_TIME = "dot_prize_time";
        	/** 中奖版本号 **/
        	public static final String DOT_PRIZE_VERSION = "dot_prize_version";
        	/** 中奖金额**/
        	public static final String DOT_PRIZE_MONEY = "dot_prize_money";
        	/** 中奖用户**/
        	public static final String DOT_PRIZE_USER_LIST = "dot_prize_user_list";
        }
        
        /**
		 * 商城缓存
		 * @author hanshimeng
		 *
		 */
		public static interface shoppingMallKey{
			/** 商品key**/
			static final String KEY_GOODS_COG = "keyGoodsCog";
            /** 尊享卡对应商品key**/
            static final String EXCHANGE_CARD_TYPE = "exchangeCardType";
		}

        /**
         * 月度城市酒王排名
         */
        public static interface MonthCityRank {
            /** 月度城市酒王排名  */
            static final String VPS_VCODE_MONTHCITYRANK_HISTORY = "vpsVcodeMonthCityRankHistory";
        }
        
        /**
         * 酒量PK1v1
         */
        public static interface drinkCapacityPk{
            static final String DRINKCAPACITY_PK_STATISTICS = "drinkCapacity_pk_statistics";
            static final String DRINKCAPACITY_PK_RECORD = "drinkCapacity_pk_record";
        }
		/**
		 * 换购_折扣活动相关
		 */
		public static interface discountExchangeActivity{
			/** 折扣活动key **/
			public static final String halfPriceActivity = "halfPriceActivity";
			/** 换购活动 **/
			public static final String exchangeActivity = "exchangeActivity";
			/** 获取当前活动 **/
			public static final String getCurrentActivity = "getCurrentActivity";
		}
        /**
         * 会员体系任务相关
         */
        public static interface vipTask{
            /** 会员日常任务**/
            public static final String KEY_DAILY_TASK = "vipDailyTaskKey";
	}
	
	}
	
	/**
	 * <pre>
	 * 缓存查询类型.
	 * </pre>
	 *
	 * @author 孙顺博 2015-1-31
	 */
	public static interface CacheQueryType {
		
		/** 先从缓存获取，缓存不存在则从DB获取 *. */
		public static final String _DEFAULT = "cacheAnddb";
		
		/** 直接从DB获取 *. */
		public static final String _ONLYDB = "onlydb";
		
		/** 直接缓存获取 *. */
		public static final String _ONLYCACHE = "onlycache";
	}
	
	/**
	 * <pre>
	 * 缓存清理类型.
	 * </pre>
	 *
	 * @author 孙顺博 2015-1-31
	 */
	public static interface CacheClearType {
		
		/** 不清理缓存. */
		public static final String _DEFAULT = "noClear";
		
		/** 删除当前组所有缓存. */
		public static final String _CLEARGROUP = "clearGroup";
		
		/** 删除当前类所有缓存. */
		public static final String _CLEARCATEGORY = "clearCategory";
		
		// public static final String _REGEXRELATE ="regexRelate";
	}
    
    
    /**
     * <pre>
     * 获取缓存Key.
     * </pre>
     *
     * @author 孙顺博 2015-1-31
     * @param entityName 实体名称如User
     * @param id 关键业务ID，没有则为null
     * @param methodName 方法名
     * @param parameters 相关参数，没有则为null
     * @return 缓存Key
     * @throws Exception the exception
     */
	@Deprecated
	@SuppressWarnings("unchecked")
    public static StringBuffer getCacheKey(String entityName, String id, String methodName,
            Object[] parameters) throws Exception {
        StringBuffer sBuffer = new StringBuffer("");
        sBuffer.append(entityName).append(Constant.DBTSPLIT);
        sBuffer.append(id).append(Constant.DBTSPLIT);
        sBuffer.append(methodName).append(Constant.DBTSPLIT);
        
        StringBuffer paramBuffer = new StringBuffer("");
        if (null != parameters && parameters.length > 0) {
            try {
                for (Object parameter : parameters) {
                    if (parameter instanceof Map) {
                        Map<String, Object> map = (Map<String, Object>) parameter;
                        Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Entry<String, Object> entry = iterator.next();
                            paramBuffer.append(entry.getValue()).append(Constant.DBTSPLIT);
                            
                        }
                    } else if (parameter instanceof List) {
                        List<Object> objList = (List<Object>) parameter;
                        for (Object object : objList) {
                            paramBuffer.append(object).append(Constant.DBTSPLIT);
                        }
                    } else if (parameter instanceof String || parameter instanceof Integer
                            || parameter instanceof Double || parameter instanceof Float
                            || parameter instanceof Long || parameter instanceof Boolean
                            || parameter instanceof Date) {
                        paramBuffer.append(parameter).append(Constant.DBTSPLIT);
                    } else {
                        Field[] fields = parameter.getClass().getDeclaredFields();
                        Object fieldValueObject = null;
                        for (Field field : fields) {
                            /*
                             * if (Modifier.isStatic(field.getModifiers()) ||
                             * Modifier.isFinal(field.getModifiers())) { continue; }
                             */
                            field.setAccessible(true);// 此对象不使用安全检查
                            if (Modifier.isTransient(field.getModifiers())) {
                                continue;
                            }
                            fieldValueObject = field.get(parameter);
                            if (fieldValueObject != null
                                    && StringUtils.isNotBlank(fieldValueObject.toString())) {
                                paramBuffer.append(fieldValueObject.toString()).append(
                                        Constant.DBTSPLIT);
                            }
                        }
                    }
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            
        }
        // 获取参数hashcode码
        String paramHash = String.valueOf(paramBuffer.toString().hashCode());// 警告：一定是toString后hashcode否则Object本身的hashcode可能不同
        return sBuffer.append(paramHash);
    }
    
    /**
     * <pre>
     * 设置缓存值.
     * </pre>
     *
     * @author sunshunbo 2015-5-6
     * @param keyName the key name
     * @param obj the obj
     * @throws Exception the exception
     */
    public static void setCacheValue(String keyName, Object obj) throws Exception {
        int expireTime = SECONDS_TO_EXPIRE;
        setCacheValueCommon(keyName, obj, expireTime);
    }
    
    @SuppressWarnings("unchecked")
    private static void setCacheValueCommon(String keyName, Object obj, int expireTime)
            throws Exception {
        String[] keys = keyName.split(Constant.DBTSPLIT);
        String entityName = "";
        String id = "";
        if (keys != null && keys.length > 2) {
            entityName = keys[0];
            id = keys[1];
        }
        // 维护entityName_id对应的一组缓存key
        if (!CheckUtil.isEmpty(id)) {
            String idCacheKey = entityName + Constant.DBTSPLIT + id + "G-R-O-U-P";
            Object object = mirrorCachClient.get(idCacheKey);
            List<String> idValueList = null;
            if (!CheckUtil.isEmpty(object)) {
                idValueList = (List<String>) object;
                if (!CheckUtil.isEmpty(idValueList)) {
                    if (!idValueList.contains(keyName)) {
                        idValueList.add(keyName);
                        mirrorCachClient.set(idCacheKey, expireTime, idValueList);
                    }
                } else {
                    idValueList = new ArrayList<String>();
                    idValueList.add(keyName);
                    mirrorCachClient.set(idCacheKey, expireTime, idValueList);
                }
            } else {
                idValueList = new ArrayList<String>();
                idValueList.add(keyName);
                mirrorCachClient.set(idCacheKey, expireTime, idValueList);
            }
        }
        // 基础存放
        mirrorCachClient.set(keyName, expireTime, obj);
    }
    
    /**
     * <pre>
     * 设置缓存值(可设置过期时间).
     * </pre>
     *
     * @author sunshunbo 2015-5-6
     * @param keyName the key name
     * @param secondsToExpire the seconds to expire
     * @param obj the obj
     * @throws Exception the exception
     */
    public static void setCacheValue(String keyName, int secondsToExpire, Object obj)
            throws Exception {
        setCacheValueCommon(keyName, obj, secondsToExpire);
    }
    
    /**
     * 设置缓存值(不含分组)
     * @param keyName
     * @param secondsToExpire
     * @param obj
     * @throws Exception
     */
    public static void setCacheValueOnly(String keyName, int secondsToExpire, Object obj)
            throws Exception {
        mirrorCachClient.set(keyName, secondsToExpire, obj);
    }
    
    /**
     * <pre>
     * 获取缓存值.
     * </pre>
     *
     * @author sunshunbo 2015-5-6
     * @param keyName the key name
     * @return the object
     * @throws Exception the exception
     */
    public static Object getCacheValue(String keyName) throws Exception {
        return mirrorCachClient.get(keyName);
    }


    /**
     * 移除当前缓存区域的所有对象
     * @throws IOException 
     */
    public static void removeAll() throws IOException {
        mirrorCachClient.deleteAll();
    }
    
    /**
     * <pre>
     * 删除指定Key的缓存对象.
     * </pre>
     *
     * @author 孙顺博 2015-1-31
     * @param keyName the key name
     * @throws Exception the exception
     */
    public static void removeByKey(String keyName) throws Exception {
        mirrorCachClient.delete(keyName);
    }

	public static void removeByPrefix(String keyName) throws Exception {
		mirrorCachClient.removeByPrefix(keyName);
	}
    
    /**
     * <pre>
     * 删除指定Key的一组缓存对象.
     * </pre>
     *
     * @author sunshunbo 2015-5-6
     * @param keyName the key name 模块_id即可
     * @throws Exception the exception
     */
    @SuppressWarnings("unchecked")
    public static void removeGroupByKey(String keyName) throws Exception {
        String[] keys = keyName.split(Constant.DBTSPLIT);
        String entityName = "";
        String id = "";
        if (keys != null && keys.length > 1) {
            entityName = keys[0];
            id = keys[1];
        }
        if (!CheckUtil.isEmpty(entityName) && !CheckUtil.isEmpty(id)) {
            String idCacheKey = entityName + Constant.DBTSPLIT + id + "G-R-O-U-P";
            Object object = mirrorCachClient.get(idCacheKey);
            mirrorCachClient.delete(idCacheKey);
            if (!CheckUtil.isEmpty(object)) {
                List<String> idValueList = (List<String>) object;
                if (!CheckUtil.isEmpty(idValueList)) {
                    for (String key : idValueList) {
                        mirrorCachClient.delete(key);
                    }
                } 
            } 
        }
        mirrorCachClient.delete(keyName);
    }

    public static void addKeyModelVal(String key) {
        if (keyModelSet.get() == null) {
            keyModelSet.set(new HashSet<String>());
        }
        keyModelSet.get().add(key);
    }

    public static void addGroupModelVal(String key) {
        if (groupModelSet.get() == null) {
            groupModelSet.set(new HashSet<String>());
        }
        groupModelSet.get().add(key);
    }

    public static void clearModelCache() {
        if (keyModelSet.get() != null) {
            for (String key : keyModelSet.get()) {
                try {
                    CacheUtilNew.removeByKey(key);
                } catch (Exception e) {
                    log.error("清除缓存Key失败：" + key);
                }
            }
        }
        if (groupModelSet.get() != null) {
            for (String key : groupModelSet.get()) {
                try {
                    CacheUtilNew.removeGroupByKey(key);
                } catch (Exception e) {
                    log.error("清除缓存groupKey失败：" + key);
                }
            }
        }
    }
}
