package com.dbt.framework.cache.bean;

import org.springframework.context.support.ApplicationObjectSupport;

/**
 * 文件名:MirrorCacheUtil.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.
 * 描述: 缓存工具类<br>
 * 修改人: jiquanwei<br>
 * 修改时间：2014-06-11 14:50:58<br>
 * 修改内容：新增<br>
 * 修改人: guchangpeng<br>
 * 修改时间：2014-07-15 20:09:58<br>
 * 修改内容：修改<br>
 */
public class CacheKey extends ApplicationObjectSupport {
	/**
	 * 缓存key
	 * 
	 * @author guchangpeng
	 * @date 2014-07-15
	 */
	public static class cacheKey {
		/** 数据字典缓存key:vps_sys_datadic_m **/
		public static final String KEY_VPS_SYS_DATA_DICM = "VpsSysDatadicM";
		/** 数据字典类型缓存key：vps_sys_diccategory_info **/
		public static final String KEY_VPS_SYS_DICCATEGORY_INFO = "VpsSysDiccategoryInfo";
		/** 省区数据源KEY**/
		public static final String KEY_PROJECT_SERVER_INFO="keyProjectServerInfo:";
		/** 省区数据源KEY**/
		public static final String KEY_PROJECT_SERVER_INFO_FOR_ITEM_VALUE="keyProjectServerInfoForItemValue:";
		/** 省区数据源更新List**/
		public static final String KEY_UPDATE_PROJECT_SERVER_INFO_LIST="key_update_project_server_info_list";

		/**
		 * 
		 * @author
		 * @createTime
		 * @description
		 */
		public static interface clientUser {

		}
		
		/**
		 * V码活动缓存Key
		 * 
		 * @author RoyFu
		 * @createTime 上午9:31:08
		 * @description
		 */
		public static interface vcode {
		    /** 平台V码活动 **/
		    public static final String KEY_VCODE_ACTIVITY_COG_NEW = "VpsVcodeActivityCog";
			/** 平台V码活动扩展表 **/
		    public static final String KEY_VCODE_ACTIVITY_COG_EXTENDS = "VpsVcodeActivityCogExtends";
		    /** 签到活动 **/
		    public static final String KEY_VCODE_SINGIN_COG = "VpsVcodeSigninCog";
		    /** 捆绑促销活动 **/
		    public static final String KEY_VCODE_BIND_PROMOTION_COG = "VpsVcodeBindPromotionCog";
		    /** 一码双奖活动 **/
		    public static final String KEY_VCODE_DOUBLE_PRIZE_COG = "VpsVcodeDoublePrizeCog";
		    /** 商城优惠券券 **/
		    public static final String KEY_VPOINTS_COUPON_COG = "keyVpointsCouponCog";
		    /** 活动与码库关联**/
		    static final String KEY_VCODE_ACTIVITY_LIB_RELATION = "keyVcodeActivityLibRelation";
		    /** V码可疑用户配置 **/
		    public static final String KEY_VPS_VCODE_ACTIVITY_BLACKLIST_COG = "VpsVcodeActivityBlacklistCog";
		    /** V码新版疑似黑名单key **/
		    public static final String KEY_VCODE_BLACKLIST_ACCOUNT_INFO = "VpsVcodeBlacklistAccountInfo0426";
		    /** V码新版疑似黑名单列表 **/
		    public static final String KEY_VCODE_BLACKLIST_ACCOUNT_LIST = "VcodeBlacklistAccountList";
		    /** 积分配置列表 **/
		    public static final String KEY_VCODE_ACTIVITY_VPOINTS_COG_LIST = "VpsVcodeActivityVpointsCogListNew";
		    /** V码新版疑似黑名单加入原因 **/
		    public static final String KEY_BLACKLIST_ACCOUNT_INFO_DOUBT_REASON = "BlacklistAccountInfoDoubtResaon";
		    /** 当前可疑用户的活动主键（临时缓存，入库后会删除） **/
		    public static final String KEY_BLACKLIST_ACCOUNT_INFO_ACTIVITYKEY = "keyBlacklistAccountInfoActivityKey";
		    /** 批码信息 **/
		    static final String EKY_VPS_VCODE_QRCODE_BATCHINFO = "VpsVcodeQrcodeBatchInfo0331";
		    /** 包码信息 **/
		    static final String EKY_VPS_VCODE_QRCODE_PACKINFO = "VpsVcodeQrcodePackInfo";
		    /** 奖项配置项对应图片**/
		    static final String KEY_VCODE_ACTIVITY_VPOINTS_COG_FORIMG = "VpsVcodeActivityVpointsCogForImg";
		    /** 平台优惠券活动 **/
			public static final String KEY_VCODE_ACTIVITY_TICKET_COG = "VpsVcodeActivityTicketCog";
			 /** 河北翻倍卡天数 **/
			public static final String ALLOWANCE_EXPIRE_TIME = "allowance_expire_time";
			/** 活动配置基础信息（popss） **/
			public static final String KEY_WECHAT_ACTIVITY_BASIC_INFO = "key_wechat_activity_basic_info";
			/** 用户当期参与微信运动缓存KEY **/
			static final String KEY_WECHATMOVEMENT_JOIN_USER = "keyWechatmovementJoinUser";
			/** 用户是否参与微信运动缓存标志 **/
			static final String KEY_USER_JOIN_WECHAT_MOVEMENT_FLAG = "keyUserJoinWechatMovementFlag";
			/** 用户参与微信运动缓存 **/
			static final String KEY_USER_JOIN_WECHAT_MOVEMENT = "keyUserJoinWechatMovement";
			/** 微信运动记录缓存 **/
			static final String KEY_WECHATMOVEMENT_USER_DETAIL = "keyWechatmovementUserDetail";
            /** 集盖活动**/
            static final String KEY_VCODE_BOOTTLECAP_ACTIVITY_COG = "VpsVcodeBottleCapActivityCog";
			/** V店惠邀请有礼活动 */
			static final String KEY_VCODE_VDH_INVITATION_ACTIVITY_COG = "keyVcodeVdhInvitationActivityCog";
            /** 频次活动 */
            static final String KEY_VCODE_ACTIVITY_FREQUENCY_COG = "keyVcodeActivityFrequencyCog";
			/** 邀请有礼活动 */
			static final String KEY_VCODE_ACTIVITY_INVITATION_COG = "keyVcodeActivityInvitationCog";
		}

		/**
		 * 企业相关(包含企业信息、sku信息、品牌、品类)
		 * 
		 * @author guchangpeng
		 * @date 2014-07-17
		 */
		public static interface company {
			/** 企业信息 **/
			public static final String KEY_COMPANY_INFO = "companyInfo";
			/** sku信息 **/
			public static final String KEY_SKU_INFO = "sku_info";
			/** 企业积分池 */
			public static final String KEY_COMPANY_POINTPOOLINFO = "company_pointpoolinfo";
			/** 企业下的工厂 */
			public static final String KEY_COMPANY_FACTORY_INFO = "keyCompanyFactoryInfo";
			/** 企业下激活人员注册的二维码 */
			public static final String KEY_COMPANY_BATCH_ACTIVATE_QRCODE = "KeyCompanyBatchActivateQrcode";
			/** 大奖核销人员注册的二维码 */
			public static final String KEY_CHECK_USER_REGISTER_QRCODE = "keyCheckUserRegisterQrcode";
		}


        /**
         * 用户信息
         */
        public static interface USER_INFO {
            /** 用户信息 **/
            static final String KEY_CONSUMER_USER_INFO = "VpsConsumerUserInfo0426";
            /** 三方账户信息 **/
            static final String KEY_CONSUMER_THIRD_ACCOUNT_INFO = "VpsConsumerThirdAccountInfo";
            /** 用户fromid缓存KEY **/
            static final String KEY_USER_FORMID_INFO = "keyUserFormidInfo";
            
            /** 用户当期参与微信运动缓存KEY **/
			static final String KEY_WECHATMOVEMENT_JOIN_USER = "keyWechatmovementJoinUser";
			/** 用户是否参与微信运动缓存标志 **/
			static final String KEY_USER_JOIN_WECHAT_MOVEMENT_FLAG = "keyUserJoinWechatMovementFlag";
			/** 用户参与微信运动缓存 **/
			static final String KEY_USER_JOIN_WECHAT_MOVEMENT = "keyUserJoinWechatMovement";
			/** 用户参与微信运动缓存（PK赛） **/
			static final String KEY_USER_JOIN_WECHAT_MOVEMENT_PK = "keyUserJoinWechatMovementPk";
			/** 微信运动记录缓存 **/
			static final String KEY_WECHATMOVEMENT_USER_DETAIL = "keyWechatmovementUserDetail";
			/** 用户锦鲤红包缓存KEY **/
			static final String KEY_USER_KOI_REDPACKED_INFO = "keyUserKoiRedpackedInfo";
        }

		/**
		 * 礼包红包相关缓存key
		 * 
		 * @author jiquanwei
		 * @date 2014-11-27
		 */
		public static interface giftspack {
			/** 抢红包活动信息 **/
			public static final String KEY_VPS_GIFTSPACK_ACTIVITY_M = "vps_giftspack_activity_m";
			/** 用户礼包关系记录 */
			public static final String KEY_VPS_GIFTSPACK_USERPRESENT_INFO = "vps_giftspack_user_present_info";
			/** 抢红包活动奖品规则信息 **/
			public static final String KEY_VPS_GIFTSPACK_AWARDSRULE_INFO = "vps_giftspack_awardsrule_info";
			/** 幸运号记录 **/
			public static final String KEY_VPS_GIFTSPACK_JOB_INFO = "vps_giftspack_job_info";
			/** 礼品红包信息 **/
			public static final String KEY_VPS_GIFTSPACK_AWARDS_INFO = "vps_giftspack_awards_info";
			/** 抢红包活动礼券码信息 **/
			public static final String KEY_VPS_GIFTSPACK_COUPONCODE_INFO = "vps_giftspack_couponcode_info";
			/** 抢红包活动用户操作记录 **/
			public static final String KEY_VPS_GIFTSPACK_OPERATION_INFO = "vps_giftspack_operation_info";
			/** 抢红包活动规则关联关系 **/
			public static final String KEY_VPS_GIFTSPACK_ASSOCIATE_INFO = "vps_giftspack_associate_info";
			/** 浮动利率 */
			public static final String KEY_PFEFIX_FLOATINGPROFIT = "prefix_floatingProfit";
			/** 前N排名 */
			public static final String KEY_PREFIX_RANKING = "prefix_ranking";
			/** 剩余总成本 */
			public static final String KEY_PREFIX_LEAVINGS = "prefix_leavings";
			/** 将已分配出的前n名放入此缓存 */
			public static final String KEY_PREFIX_WAITINGDB = "prefix_waitingdb";
			/** 除前n名剩余数量 */
			public static final String KEY_PREFIX_LEAVINGS_COUNTS = "prefix_leavings_counts";
			/** 当前已抢数量 */
			public static final String KEY_PREFIX_PRECOUNTS = "prefix_precounts";
			/** 当前总成本 */
			public static final String KEY_PREFIX_TOTALREBATES = "prefix_totalRebates";
			/** 奖品剩余数量[用户扣除奖品剩余数] **/
			public static final String KEY_PREFIX_AWARDS_REST = "prefix_awards_rest";
			/** 累计所有阶梯数量 */
			public static final String KEY_PREFIX_FLOATING_COUNTS = "prefix_floating_counts";
			/** 公有的奖品剩余数量，运营需要用上的 **/
			public static final String KEY_PUBLIC_VPS_GIFTSPACK_AWARDSREST = "public_giftspack_awardsrest";
			/** 兑换点与奖品关联关系key **/
			public static final String KEY_VPS_OFFLINE_STORE_AWARDS_RELATION = "vps_offline_store_awards_relation";
			/** 线下兑换终端兑换点 **/
			public static final String VPS_OFFLINESTOREM = "vps_offline_store_m";
			/** 线下兑换终端兑换点 **/
			public static final String KEY_VPS_OFFLINE_SERVICE_USER_M = "vps_offline_service_user_m";
		}

		/**
		 * 扫码黑名单
		 * 
		 * @author hanshimeng
		 * 
		 */
		public static interface vcodeBlack {
			/** 扫码白名单 **/
			public static final String KEY_VCODE_WHITE = "key_vcode_white";
			/** 扫码黑名单 **/
			public static final String KEY_VCODE_BLACK = "key_vcode_black";
			/** 所有黑名单列表 **/
			public static final String KEY_ALL_BLACK_LIST = "VpsBlackListInfo";
		}

        /**
         * 验证码
         * @author jiquanwei
         *
         */
        public static interface captcha{
            /** 验证码**/
            public static final String KEY_SEND_CAPTCHA_SMS = "SendCaptchaSmsForPlatfrom";
        }
        
        /**
		 * 竞价活动相关
		 * @author Administrator
		 *
		 */
		public static interface biddingActivity{
			/** 竞价活动KEY **/
		    public static final String KEY_BIDDING_ACTIVITY_COG = "keyBiddingActivityCog";
		    
		}

		/**
		 * 裂变活动相关
		 * @author Administrator
		 *
		 */
		public static interface ShareRecord{
			/** 分享记录统计Key **/
			public static final String KEY_SHARE_RECORD = "VpsVcodeActivateShareRecord";
            /*分享裂变待激活红包活动缓存*/
            static final String SHARE_ACTIVATE_ACTIVITY_KEY = "share_activate_activity_key";
            /*分享裂变待激活红包分享者获取奖项缓存*/
            static final String SHARE_ACTIVATE_SHARE_USER_PRIZE_KEY = "share_activate_share_user_prize_key";
		}

	}
}