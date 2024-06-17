package com.dbt.framework.datadic.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.SysDataDic;
import com.dbt.framework.datadic.service.SysDataDicService;
import com.dbt.framework.util.BeanFactoryUtil;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;

/**
 * 文件名: DatadicUtil 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All
 * Rights Reserved. 描述: 数据字典工具类 修改人: Jiquanwei 修改时间：2014-08-18 11:22:45 修改内容：新增
 */
public class DatadicUtil {

	/**
	 * 根据cachekey,查询缓存数据
	 *
	 * @param categoryCode
	 * @return ArrayList
	 **/
	public static List<SysDataDic> getListByCategoryCode(String categoryCode) {
		List<SysDataDic> list = getDataDicByCagegoryCode(categoryCode);
		return list;
	}

	/**
	 * 根据cache和具体key查询value
	 * 
	 * @param categoryCode
	 * @param key
	 * @return string
	 **/
	public static String getDataDicValue(String categoryCode, String dataId) {
	    String dataVal = null;
	    try {
	        String cacheKeyStr = CacheKey.cacheKey.KEY_VPS_SYS_DATA_DICM + Constant.DBTSPLIT
	                + categoryCode.replace("_", "") + Constant.DBTSPLIT + dataId.replace("_", "");
	        dataVal = (String) CacheUtilNew.getCacheValue(cacheKeyStr);
	        if (StringUtils.isBlank(dataVal)) {
	            SysDataDic dataDic = getDatadicByCatCodeAndDataid(categoryCode, dataId);
	            if (dataDic != null) {
	                dataVal = dataDic.getDataValue();
	                if (StringUtils.isNotBlank(dataVal)){
	                	CacheUtilNew.setCacheValueOnly(cacheKeyStr, 0, dataVal);
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return StringUtils.defaultIfBlank(dataVal, "");
	}
	
	/**
	 * 获取对应的数据字典值，不取缓存
	 * @param categoryCode
	 * @param dataId
	 * @return
	 */
	public static String getDataDicValueNoCache(String categoryCode, String dataId) {
		String dataVal = null;
		SysDataDic dataDic = getDatadicByCatCodeAndDataid(categoryCode, dataId);
        if (dataDic != null) {
            dataVal = dataDic.getDataValue();
        }
		return StringUtils.defaultIfBlank(dataVal, "");
	}

	/**
	 * 根据cache和具体key查询对象
	 * 
	 * @param categoryCode
	 * @param key
	 * @return SysDataDic
	 **/
	public static SysDataDic getDataDicByCategoryAndDataId(String categoryCode, String dataId) {
		return getDatadicByCatCodeAndDataid(categoryCode, dataId);
	}

	/**
	 * 根据类型Code获取数据信息
	 * 
	 * @param categoryCode
	 * @return
	 */
	private static List<SysDataDic> getDataDicByCagegoryCode(String categoryCode) {
		SysDataDicService dataDicService = (SysDataDicService) BeanFactoryUtil
				.getbeanFromWebContext("sysDataDicService");
		return dataDicService.getDataDicByCagegoryCode(categoryCode);
	}

	/**
	 * 通过字典类型和dataid获取数据字典中的数据
	 * 
	 * @param categoryCode
	 * @param dataid
	 * @return
	 */
	private static SysDataDic getDatadicByCatCodeAndDataid(String categoryCode, String dataid) {
		SysDataDicService dataDicService = (SysDataDicService) BeanFactoryUtil
				.getbeanFromWebContext("sysDataDicService");
		return dataDicService.getDatadicByCatCodeAndDataid(categoryCode, dataid);
	}

	/***
	 * 数据字典类型code
	 */
	public static interface dataDicCategory {
		/** 数据常量配置 */
        public static final String DATA_CONSTANT_CONFIG = "data_constant_config";
		/** 提现 **/
		public static final String EXTRACT_CONFIG = "extract";
		/** 提现与积分配置 **/
		public static final String EXTRACT_MONEY_POINT_CONFIG = "extract_money_point";

		/** 华夏银行转账信息类别 **/
		public static final String HX_BANK_TRANSFER_TYPE = "hx_bank_transfer_type";

		/** 银行信息类别 **/
		public static final String BANK_INFO = "bank_info";
		
		 /** 路由规则类型**/
        static final String PACK_RECORD_ROUTER = "pack_record_router_config";
        /** 积分商城相关配置 **/
        static final String VPOINTS_ESTORE_COG = "vpoints_estore_cog";
        /** 月度城市酒王排名**/
        static final String MONTH_CITY_RANK = "month_city_rank";
        /** 一等奖相关内容 **/
        public static final String MAJOR_INFO = "major_info";
        /** 小票业务相关配置**/
        public static final String TICKET_COG = "ticket_cog";
        /** filter功能开关配置**/
        public static final String FILTER_SWITCH_SETTING = "filter_switch_setting";
	}

	/***
	 * 数据字典dataID
	 */
	public static class dataDic {
		/** 提现配置 **/
		public static interface extract_config {
			/** 提现开关 **/
			static final String EXTRACT_SWITCH = "extract_switch";
			/** 提现关闭提示语 **/
			static final String EXTRACT_SWITCH_TIPS = "extract_switch_tips";
			/** 提现说明 **/
			static final String EXTRACT_EXPLAIN = "extract_explain";
			/** 每日提现次数限制 **/
			static final String PREDAY_EXTRACT_LIMIT_COUNT = "preday_extract_limit_count";
			/** 每月提现限制及提示语 **/
			static final String PREMONTH_EXTRACT_LIMIT_COUNT = "premonth_extract_limit_count";
			/** 初始化绑定银行卡提示语 **/
			static final String INIT_BANKINFO_TIPS = "init_bankinfo_tips";
			/** 银行卡绑定上限 **/
			static final String EXTRACT_BIND_CARDNUMBER_LIMIT = "extract_bind_cardnumber_limit";
			/** 多少日到帐限制 **/
			static final String EXTRACT_AFTERDATE_EXPECTED = "extract_afterdate_expected";

			/** 跨行交易代码 - 积分提现xhj2001 **/
			static final String HX_TRANS_CODE_XHJ2001 = "hx_trans_code_xhj2001";
			/** 跨行批量转账交易结果查询xhj5002 **/
			static final String HX_TRANS_CODE_XHJ5002 = "hx_trans_code_xhj5002";

			/** 华夏行交易代码 - 积分提现xhj3006 **/
			static final String HX_TRANS_CODE_XHJ3006 = "hx_trans_code_xhj3006";
			/** 华夏行批量转账交易结果查询xhj3007 **/
			static final String HX_TRANS_CODE_XHJ3007 = "hx_trans_code_xhj3007";

			/** 单账户多日明细查询xhj0009 **/
			static final String HX_TRANS_CODE_XHJ0009 = "hx_trans_code_xhj0009";

			/** 查询单账户余额xhj0004 **/
			static final String HX_TRANS_CODE_XHJ0004 = "hx_trans_code_xhj0004";

			/** 经办人姓名 **/
			static final String HX_CUST_NAME = "hx_cust_name";
			/** 付款账号 **/
			static final String HX_ACCT_NO = "hx_acct_no";
			/** 摘要 **/
			static final String HX_SUMMARY = "hx_summary";
			/** 汇兑-业务类型编码 **/
			static final String HX_BUSI_TYPE_CODE_C200 = "hx_busi_type_code_c200";
			/** 汇兑-业务种类编码 **/
			static final String HX_BUSI_CATEGORY_CODE_02001 = "hx_busi_category_code_02001";

			/** 华夏银行code **/
			static final String BANK_HX_CODE = "bank_hx_code";

			/** socket服务器IP **/
			static final String SOCKET_IP = "socket_ip";

			/** socket服务器端口号 **/
			static final String SOCKET_PORT = "socket_port";

			/** 积分提现发送socket异常内容 **/
			static final String EXTRACT_SMS_SOCKET_CONTENT = "extract_sms_socket_content";

			/** 积分提现异常短信手机号 **/
			static final String EXTRACT_SMS_PHONE = "extract_sms_phone";

			/** 积分提现异常短信内容 **/
			static final String EXTRACT_SMS_CONTENT = "extract_sms_content";

			/** 查询提现记录默认起始日期 **/
			static final String DEFAULT_START_DATE = "default_start_date";

			/** 余额不足时发送短信给财务等人--手机号 **/
			static final String EXTRACT_SMS_FINANCE_PHONE = "extract_sms_finance_phone";

			/** 余额不足时发送短信给财务等人--短信内容 **/
			static final String EXTRACT_SMS_FINANCE_CONTENT = "extract_sms_finance_content";

			/** 华夏银行付款账号余额下限 **/
			static final String HX_ACCT_BALANCE = "hx_acct_balance";
			
			/** 华夏银行付款账号余额下限提示信息 **/
			static final String HX_BALANCE_SMS_CONTENT = "hx_balance_sms_content";
			
			/** 提现结束时间 **/
			static final String EXTRACT_END_TIME = "extract_end_time";
		}
		
		/**
         * 路由规则配置
         * @author dbt
         *
         */
        public static interface packRecordRouter{
            // add 2016-11-04 新的配置
            /** 第一阶段路由配置**/
            public static final String FIRST_PACK_RECORD_ROUTER_NEW = "first_pack_record_router_new";
            /** 第二阶段路由配置**/
            public static final String SECOND_PACK_RECORD_ROUTER_NEW = "second_pack_record_router_new";
            /** 第三阶段路由配置**/
            public static final String THIRD_PACK_RECORD_ROUTER_NEW = "third_pack_record_router_new";
            /** 第四阶段路由配置**/
            public static final String FOUR_PACK_RECORD_ROUTER_NEW = "four_pack_record_router_new";
        }
        
        /**
         * 积分商场相关配置
         */
        public static interface vpointsEstoreCog {
            /** 所有积分商场商品暂停兑换提示语 */
            public static final String pauseExchangeTips="pause_exchange_tips";
            /** 各品牌用户可查询品牌 */
            public static final String vpointsBrandLimit="vpoints_brand_limit";
            /** 积分商城-图文标题 */
            public static final String vpointsImageTextTitle="vpoints_wechat_imageText_title";
            /** 积分商城-图文内容 */
            public static final String vpointsImageTextDesc="vpoints_wechat_imageText_desc";
            /** 积分商城-图文跳转链接 */
            public static final String vpointsImageTextUrl="vpoints_wechat_imageText_url";
            /** 积分商城-图文显示图片 */
            public static final String vpointsImageTextImage="vpoints_wechat_imageText_image";
            /** 积分商城订单区分地区显示的节点时间 */
            public static final String vpointsOrderAreaTime="vpoints_order_area_time";
            /** 积分商城订单发货短信通知 */
            public static final String vpointsExpressSMS="vpoints_express_sms";
        }
        
        /**
         * 月度城市酒王排名
         */
        public static interface monthCityRank {
            /** 活动开始时间**/
            public static final String MCR_RANK_START_END_DATE = "mcr_rank_start_end_date";
            /** 每期排名显示条数**/
            public static final String MCR_PER_RANK_NUM = "mcr_per_rank_num";
            /** 每天计入排名的瓶数上限**/
            public static final String MCR_DAY_LIMIT_NUM = "mcr_day_limit_num";
            /** 排名称号与扫码瓶数关系**/
            public static final String MCR_RANK_TITLE_SCANNUM = "mcr_rank_title_scannum";
        }
        
        /** 一等奖相关内容 */
        public static interface majorInfo {
            
            /** 不需要发送短信通知的大奖类型*/
            public static final String FIRSTPRIZE_NO_NOTIFY_PRIZE = "FIRSTPRIZE_NO_NOTIFY_PRIZE";

            /** 大奖的活动截止时间及兑奖截止时间*/
            public static final String FIRSTPRIZE_STATUS = "FIRSTPRIZE_STATUS";
            
            /** 大奖查询起始时间*/
            public static final String FIRSTPRIZE_BEGIN_TIME = "FIRSTPRIZE_BEGIN_TIME";
            
            /** 不限制中奖次数的奖项类型配置*/
            public static final String FIRSTPRIZE_UNLIMIT_PRIZECOG = "firstprize_unlimit_prizecog";
            
            /** 回收奖项类型配置*/
            public static final String FIRSTPRIZE_RECOVERY_PRIZE = "firstprize_recovery_prize";
            
            
        }
        
        /** 数据常量配置*/
        public static interface dataConstantConfig {
            
            /** 活动版本集合 */
            public static final String ACTIVITY_VERSION = "activity_version";
            /** 锦鲤红包对应积分 */
            public static final String KIO_PACKED_MONEY = "kio_packed_money";
            /** 锦鲤红包时间限制 */
            public static final String KIO_PACKED_LIMIT_TIME = "kio_packed_limit_time";
			/** 展示销量递增递减数量 */
			public static final String SHOW_NUM_INTERVAL = "show_num_interval";
			/** 导购员投票时间*/
			public static final String OYSHO_VOTE_TIME = "oysho_vote_time";
			/** 图片素材库品牌*/
			public static final String PIC_BRAND_TYPE = "pic_brand_type";
			/** 图片素材库品牌与省区对应关系*/
            public static final String PIC_BRAND_PROJECT_SERVER = "pic_brand_project_server";
        }
        
        /**
         * 小票业务相关配置
         */
        public static interface ticketCog {
            /** 小票活动的开始及结束时间**/
            public static final String ACTIVITY_BEGIN_END_DATE = "ticket_activity_begin_end_date";
            /** 渠道限制次数**/
            public static final String TICKET_CHANNEL_LIMIT_NUM = "ticket_channel_limit_num_";
            /** 电商渠道V码前缀**/
            public static final String TICKET_CHANNEL4_VCODEUNIQUECODE = "ticket_channel4_vcodeuniquecode";
            /** 大奖抽奖时间 job **/
            public static final String LOTTERY_BIG_PRIZE = "lottery_big_prize";
            /** 抽奖额度 job **/
            public static final String LOTTERY_MONNEY_LIMIT = "lottery_monney_limit";
            /** 抽奖额度剩余 job **/
            public static final String LOTTERY_MONNEY_SURPLUS = "lottery_monney_surplus";
            /** 小票识别目标商品的正则 **/
            public static final String TARGET_GOODS_REGEX = "target_goods_regex";
            /** 小票识别百度url配置 **/
            public static final String TICKET_OCR_URL = "ticket_ocr_url";
            /** 促销员评选报名的开始及结束时间**/
            public static final String OYSHO_APPLY_BEGIN_END_DATE = "oysho_apply_begin_end_date";

        }
        
        /**
         * filter功能开关配置
         * @author bin.zhang
         */
        public static interface filterSwitchSetting{
            /** 周签到开关 **/
            public static final String SWITCH_WEEKSIGN = "switch_weekSign";
            /** 季度酒王排名开关 **/
            public static final String SWITCH_QUARTERRANK = "switch_quarterRank";
            /** 特殊金额配置开关 **/
            public static final String SWITCH_SPECIALMONEY = "switch_specialMoney";
            /** 欢乐送订阅开关 **/
            public static final String SWITCH_LOTTERY = "switch_lottery";
            /** 捆绑促销开关 **/
            public static final String SWITCH_BINDPROMOTION = "switch_bindPromotion";
            /** 一码双奖开关 **/
            public static final String SWITCH_DOUBLEPRIZE = "switch_doublePrize";
            /** 激活红包 **/
            public static final String SWITCH_ACTIVATE_RED_ENVELOP_RULE_COG = "switch_activateRedEnvelopeRuleCog";
            /** 码集卡开关 **/
            public static final String SWITCH_SCANCARD = "switch_scanCard";
            /** 关注集卡开关 **/
            public static final String SWITCH_ATTENTIONCARD = "switch_attentionCard";
            /** 签到集卡开关 **/
            public static final String SWITCH_SIGNCARD = "switch_signCard";
            /** 互动-情与利开关 **/
            public static final String SWITCH_CONNECT = "switch_connect";
            /** 月度酒王城市排名开关 **/
            public static final String SWITCH_MONTHCITYRANK = "switch_monthCityRank";
            /** 翻倍卡开关 **/
            public static final String SWITCH_ALLOWANCEA = "switch_allowanceA";
            /** 月奖品开关 **/
            public static final String SWITCH_MONTHPRIZE = "switch_monthPrize";
            /** 月排名开关 **/
            public static final String SWITCH_MONTHRANK = "switch_monthRank";
            /** 总排名开关 **/
            public static final String SWITCH_TOTALRANK = "switch_totalRank";
            /** 辽宁小程序转盘抽奖开关 **/
            public static final String SWITCH_TURNTABLE = "switch_turntable";
            /** 模块化转盘抽奖开关 **/
            public static final String SWITCH_MODULARIZATION_TURNTABLE = "switch_modularization_turntable";
            /** 二重惊喜开关 **/
            public static final String SWITCH_SURPRISE = "switch_surprise";
            /** 淘彩蛋开关 **/
            public static final String SWITCH_TAOEASTEREGG = "switch_taoEasterEgg";
            /** crm精细化营销开关 **/
            public static final String SWITCH_GROUP = "switch_group";
            /** crm是否企业微信微信好友开关 **/
            public static final String SWITCH_ENTERPRISEWECHAT = "switch_EnterpriseWeChat";
            /**一元乐享开关 **/
            public static final String SWITCH_LEXIANG = "switch_leXiang";
            /**河北NBY批次 **/
            public static final String SWITCH_HEBEI_NBY = "switch_hebei_nby";
            /**运动达人PK赛开关 **/
            public static final String WECHAT_MOVEMENT_PK_SWITCH = "wechat_movement_pk_switch";
            /**用户扫码次数开关 **/
            public static final String URER_SCAN_NUM = "urer_scan_num";
            /**大奖列表是否显示全部得开关 **/
            public static final String SHOW_ALL_PRIZE = "SHOW_ALL_PRIZE";
            /**过期红包及积分开关 **/
            public static final String SWITCH_CONSUMERACCOUNT_EXPIRE = "switch_consumerAccount_expire";
            /**账户余额小于0.3元30天过期开关 **/
            public static final String SWITCH_SMALL_SURPLUSMONEY_EXPIRE = "switch_small_surplusmoney_expire";
            /**小票业务分享获取定时抽奖机会 **/
            public static final String TICKET_SHARE_LOTTERY = "ticket_share_lottery";
            /** 竞价开关 **/
            public static final String SWITCH_BIDDING = "switch_bidding";
            /** 历史账户提现job **/
            public static final String SWITCH_SENDGIFTPACK_FORACCOUNT = "switch_sendgiftpack_foraccount";
            /** 长城九号送祝福开关 **/
            public static final String SWITCH_BENEDICTION_MSG = "switch_benediction_msg";
            /** 积盖兑红包开关 **/
            public static final String SWITCH_SCANNUMLOTTERY = "switch_scannumlottery";
            /** 强制获取地理位置开关 **/
            public static final String SWITCH_LOCATION = "switch_location";
            /** 扫码时大奖跑马灯开关 **/
            public static final String SWITCH_SWEEP_BIGPRIZE_MARQUEE = "switch_sweep_bigprize_marquee";
            /** 模块化跑马灯开关 **/
            public static final String SWITCH_MARQUEE_MODULAR = "switch_marquee_modular";
            /** 长城双节活动促销激励V+推广开关 **/
            public static final String SWITCH_PROMOTION_VPLUS = "SWITCH_PROMOTION_VPLUS";
            /** 中粮长城CNY促销同提现开关**/
            public static final String SWITCH_TICKET_PROMOTIONUSER_EXTRACT = "SWITCH_TICKET_PROMOTIONUSER_EXTRACT";
            /** 码中台扫码开关**/
            public static final String SWITCH_MZT_SWEEP = "switch_mzt_sweep";
            /** 爱创码中台扫码开关**/
            public static final String SWITCH_ACMZT_SWEEP = "switch_acmzt_sweep";

            /**
             * 嘉华扫码开关
             */
            public static final String SWITCH_JH_SWEEP = "switch_jh_sweep";

            /**
             * 最酒扫码开关
             */
            public static final String SWITCH_ZJ_SWEEP = "switch_jh_sweep";
            /** 省区会员体系开关**/
            public static final String SWITCH_VIP_SYSTEM = "switch_vip_system";
            /** 礼品卡开关 **/
            public static final String SWITCH_GIFT_CARD_OPENID = "switch_gift_card_openid";
            /** 版本升级逻辑开关 **/
            public static final String SWITCH_VERSION_UPDATE = "switch_version_update";
        }
	}

    /**
     * 功能开关是否已开启
     * @return true 功能已开启
     */
    public static boolean isSwitchON(String dataValue) {
        dataValue = StringUtils.defaultString(dataValue, "").trim();
        if (Constant.SwitchFlag.ON.equals(dataValue)
                || (dataValue.length() == 10 && dataValue.compareTo(DateUtil.getDate()) <= 0)
                || (dataValue.length() == 19 && dataValue.compareTo(DateUtil.getDateTime()) <= 0)) {
            return true;
        } 
        return false;
    }
    
    /**
     * 功能开关是否已关闭
     * @return true 功能已关闭
     */
    public static boolean isSwitchOFF (String dataValue) {
        return !DatadicUtil.isSwitchON(dataValue);
    }
    
    /**
     * 功能开关是否已开启
     * @param categoryCode
     * @param dataId
     * @return true 功能已开启
     */
    public static boolean isSwitchON(String categoryCode, String dataId) {
        return DatadicUtil.isSwitchON(DatadicUtil.getDataDicValue(categoryCode, dataId));
    }
    
    /**
     * 功能开关是否已关闭
     * @param categoryCode
     * @param dataId
     * @return true 功能已关闭
     */
    public static boolean isSwitchOFF(String categoryCode, String dataId) {
        return DatadicUtil.isSwitchOFF(DatadicUtil.getDataDicValue(categoryCode, dataId));
    }

}
