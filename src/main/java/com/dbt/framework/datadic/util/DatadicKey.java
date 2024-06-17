package com.dbt.framework.datadic.util;

import java.io.Serializable;

public class DatadicKey implements Serializable {

	private static final long serialVersionUID = 94070441318503088L;

	
	/***
	 * 数据字典类型code
	 * 
	 * @author gucapg
	 * @date 2014-11-04
	 */
	public static interface dataDicCategory {
        /** 数据常量配置 */
        public static final String DATA_CONSTANT_CONFIG = "data_constant_config";
		/** 发短信账户信息 **/
		public static final String SEND_SMS_ACCOUT = "send_sms_accout";
        /** 短信内容类型 **/
        public static final String CATEGROY_CODE_APP_GETSMS = "app_getsms";
        /** 企业基础信息类型 **/
        public static final String COMPANY_INFO = "company_info";
        /** 公共ID生成**/
        public static final String COMMON_ID = "common_id";
		/** filter企业基础信息**/
		public static final String FILTER_COMPANY_INFO = "filter_company_info";
		/** filter微信支付配置**/
		public static final String FILTER_WXPAY = "filter_wxPay";
		/** filter微信公账号级模板消息**/
		public static final String FILTER_WXPAY_TEMPLATE_INFO = "filter_wxPay_template_info";
		/** filterhttp调用URL配置**/
		public static final String FILTER_HTTP_URL = "filter_http_url";
		/** filter活动UI版本配置**/
		public static final String FILTER_ACTIVITYVERSION = "filter_activityVersion";
		/** filter基础奖项类型**/
		public static final String FILTER_PRIZENAME = "filter_prizeName";
		/** filter实物奖项类型**/
		public static final String FILTER_GRAND_PRIZE = "filter_grand_prize";
		/** filter优惠券类型**/
		public static final String FILTER_COUPON = "filter_coupon";
		/** filter特权卡**/
		public static final String FILTER_PRIVILEGE = "filter_privilege";
		/** filter集卡奖项类型**/
		public static final String FILTER_COLLECT_CARDS = "filter_collect_cards";
		/** filter特殊金额配置**/
		public static final String FILTER_SPECIAL_AMOUNT_SETTING = "filter_special_amount_setting";
		/** filter功能开关配置**/
		public static final String FILTER_SWITCH_SETTING = "filter_switch_setting";
		/** filter管理平台验证码**/
		public static final String FILTER_PLATFORM_VERIFYCODE = "filter_platform_verifycode";
		/** filter其他**/
		public static final String FILTER_OTHERS = "filter_others";
		/** filter一元乐享（兑付通）**/
        public static final String FILTER_LX = "filter_lx";
		/** 项目Job运行省区**/
        public static final String PROJECT_JOB = "project_job";
        /** filter活动模板配置-默认模板**/
        public static final String FILTER_ACT_DEFAULTUI = "filter_act_defaultUI";
		/** AI预警OPENID**/
		public static final String AI_REMIND_OPENID = "ai_remind_openid";
		/** 小票**/
		public static final String TICKET_COG = "ticket_cog";
		/** 积分商城相关配置 **/
		static final String VPOINTS_ESTORE_COG = "vpoints_estore_cog";
	}
	public static interface filterUI{
		/** filter活动模板配置-默认模板**/
		public static final String FILTER_ACTUI_DEFAULTPARAM = "filter_actUI_defaultParam";
	}
    
    /** 数据常量配置*/
    public static interface dataConstantConfig {
        
        /** 幸运金额区间 */
        public static final String LUCKY_MONEY_RANGE = "lucky_money_range";
        
        /** 记录天数 */
        public static final String RECORD_DAYS = "recordDays";
        
        /** 日消耗总积分阈值 */
        public static final String DAY_CONSUME_VPOINTS="day_consume_vpoints";
        
        /** 通知技术人员手机号 */
        public static final String NOTIFY_TECH_PHONES="notify_tech_phones";
        
        /** 扫码开关 1：开（可以正常操作）， 0：关： */
        public static final String SWEEP_SWITCH="sweep_switch";
        
        /** 扫码是否有不中奖 1：是， 0：否： */
        public static final String SWEEP_HAVENOPRIZE="sweep_havenoprize";
        
        /** 提现开关 1：开（可以正常操作）， 0：关： */
        public static final String EXTRACT_SWITCH="extract_switch";
        
        /** 当天每个用户最多提现金额： */
        public static final String EXTRACT_MONEY_APPOINT="extract_money_appoint";
        
        /** 单支付通道单用户每月提现金额限制： */
        public static final String EXTRACT_MONEY_PAY_USER_MONTH="extract_money_pay_user_month";
        
        /** 防黑客 -- IP类型： */
        public static final String HACKER_TYPE_IP_ERRNUM = "hacker_type_ip_errnum";
        
        /** 用户进入月度酒王排名的瓶数限制 */
        public static final String RANKING_MONTH_NUM_LIMIT = "RANKING_MONTH_NUM_LIMIT";
        /** 月度排名总中奖人数上限*/
        public static final String RANKING_MONTH_NUM_TOTAL = "RANKING_MONTH_NUM_TOTAL";
        
        /** 用户进入总排名的积分限制 */
        public static final String RANKING_TOTAL_NUM_LIMIT = "RANKING_TOTAL_NUM_LIMIT";
        
        /** 项目经理联系电话 */
        public static final String PM_PHONENUM = "pm_phonenum";
        
        /** 所有用户每天扫码到第12个，需要手机信息验证1次 */
        public static final String SWEEP_CODE_COUNT_EVERYDAY = "sweep_code_count_everyday";
        
        /** 防止工厂盗扫：黑名单规则加入24小时之内连续扫码2个SKU（随意搭配），系统自动加入黑名单。 */
        public static final String SWEEP_CODE_SKU_EVERYDAY = "sweep_code_sku_everyday";
        
        /** 扫码的地区及sku限制(不返利)：地区,地区...#sku,sku */
        public static final String SWEEP_CODE_RESTRAIN_AREA_SKU = "sweep_code_restrain_area_sku";
        
        /** 扫码的地区及sku限制(返利)：地区,地区...#sku,sku */
        public static final String SWEEP_CODE_RESTRAIN_AREA_SKU_REBATE = "sweep_code_restrain_area_sku_rebate";
        
        /** 模拟定位配置 */
        public static final String LOCAL_ADDRESS_COG = "local_address_cog";
        
        /** 一万一批次激活通知手机号 */
        public static final String BATCH_ACTIVATE_PHONENUM = "batch_activate_phonenum";
        
        /** 腾讯K币开关 */
        public static final String TENCENT_KCOINS_SWITCH = "tencent_kcoins_switch";
        
        /** 单瓶*/
        public static final String AI_DANPING="danping";
        
        /** 扫码大金额配置*/
        public static final String BIG_MONEY_ARY="big_money_ary";
        
        /** 河北翻倍卡天数 **/
        public static final String ALLOWANCE_EXPIRE_TIME = "allowance_expire_time";
        
         /** 直接翻倍红包**/
        public static final String SHOW_DOUBLE_FOR_CURRSCAN = "show_double_for_currscan";
		/** 统计分组不查询sku的省区 **/
		public static final String UNINCLUDEDSERVER = "unIncluded_server";
    }

    
    /**
     * AI预警OPENID
     */
    public static interface aiRemindOpenid{
        /** 项目经理 **/
        public static final String AI_REMIND_PM_OPENID = "ai_remind_pm_openid";
        /** 技术 **/
        public static final String AI_REMIND_TECH_OPENID = "ai_remind_tech_openid";
        /** 奖项占比预警 **/
        public static final String AI_REMIND_PRIZE_PERCENT = "ai_remind_prize_percent";
        /** 活动结束前48小时预警 **/
        public static final String AI_REMIND_ACTIVITY_END = "ai_remind_activity_end";
    }

	/**
	 * filter企业基础信息
	 * @author bin.zhang
	 */
	public static interface filterCompanyInfo{
		/** 接口项目名称 **/
		public static final String PROJECT_INTERFACE_CODE = "project_interface_code";
		/** 平台项目名称 **/
		public static final String PROJECT_PLANTFORM_CODE = "project_plantform_code";
		/** 管理平台中项目的汉字名称 **/
		public static final String PROJECT_NAME = "project_name";
		/** Redis缓存中项目前缀 **/
		public static final String PROJECT_SERVERNAME = "project_serverName";
		/** 订单前缀 **/
		public static final String PROJECT_ORDERPREFIX = "project_orderPrefix";
		/** Sys_Area_Info表中省编码的前两位，大于一个时用半角逗号分隔 **/
		public static final String PROJECT_AREA = "project_area";
		/** 项目标志 **/
		public static final String PROJECT_FLAG = "project_flag";
		/** Redis消息订阅通道 **/
		public static final String PROJECT_REDISCHANNEL = "project_redisChannel";
		/** 跟main工程关联的项目编号 **/
		public static final String PROJECT_ITEMVALUE = "project_itemValue";
		/** 红包领取金额限制 **/
		public static final String PACKGIFTS_LIMIT = "packgifts_limit";
		/** 首扫类型：USER、SKU、ACTIVITY **/
		public static final String FIRST_SWEEPTYPE = "first_sweeptype";
		/** 众号-Rio-（测试公众号）（小程序-微醺） **/
		public static final String COMPANY_WEIXUN_APPID = "company_weixun_appid";
		/** 众号-Rio-（秘钥）（小程序-微醺） **/
		public static final String COMPANY_WEIXUN_SECRET = "company_weixun_secret";
		/** 项目经理邮箱 **/
		public static final String PROJECT_EMAIL = "project_email";
		/** 礼品卡密码的项目经理邮箱 **/
		public static final String PROJECT_EMAIL_GIFT_CARD = "project_email_gift_card";
		/** SKU渠道 **/
		public static final String SKUCHANNEL = "sku_channel";
		/** 对应终端促销企业主键 **/
		public static final String VMTS_COMPANY_KEY = "vmts_company_key";
	}

	/**
	 * filter微信支付配置
	 * @author bin.zhang
	 */
	public static interface filterWxPay{
		/** 支付通道所属公司标志 **/
		public static final String PROJECT_PAY = "project_pay";
		/** 微信支付分配的商户号 **/
		public static final String WXPAY_MCH_BILLNO = "wxPay_mch_billno";
		/** 交易描述 **/
		public static final String TRANSFERS_DESC = "transfers_desc";
		/** 微信支付证书密码(密码默认是商户号) **/
		public static final String WXPAY_CERTPASSWORD = "wxPay_certPassword";
	}
	/**
	 * filter微信公账号级模板消息
	 * @author bin.zhang
	 */
	public static interface filterWxPayTemplateInfo{
		/** V积分返利会员卡公众号（测试公众号）（支付公众号）appid' **/
		public static final String VJF_APPID = "vjf_appid";
		/** 用户互动公众号appid（互动公众号） **/
		public static final String HD_APPID = "hd_appid";
		/** AI助手公众号appid **/
		public static final String AI_APPID = "ai_appid";
		/** 终端促销服务号appid **/
		public static final String TERMINAL_APPID = "terminal_appid";
		/** 用户互动消息模板id1(同一个模板对应每个公众号模板ID不同) **/
		public static final String MSG_TEMPLATE_ID1 = "msg_template_id1";
		/** 激活工具公众号appid（品牌服务） **/
		public static final String ACTIVATE_APPID = "activate_appid";
		/** 公众号-积分返利会员卡公众号（测试公众号）（企业公众号）appid **/
		public static final String COMPANY_APPID = "company_appid";
		/** 微信H5跳转域名 **/
		public static final String WECHAT_H5_DOMAIN = "wechat_h5_domain";
		/** 积分兑换成功通知 **/
		public static final String WECHAT_TMPMSG_EXCHANGE = "wechat_tmpMsg_exchange";
		/** 订单发货通知 **/
		public static final String WECHAT_TMPMSG_EXPRESSSEND = "wechat_tmpMsg_expressSend";
		/** 双十一优惠券提醒消息相关小程序appid **/
		public static final String APPLET_DOUBLEELEVEN_APPID = "applet_doubleeleven_appid";
		/** 双十一优惠券提醒消息相关小程序密钥 **/
		public static final String APPLET_DOUBLEELEVEN_SECRET = "applet_doubleeleven_secret";
		/** 提醒模板 **/
		public static final String WECHAT_TMPMSG_DOUBLEELEVENREMIND = "wechat_tmpMsg_doubleelevenRemind";
		/** 消息管理推送消息相关appid **/
		public static final String PAAPPLET_APPID = "paApplet_appid";
		/** 消息管理推送消息相关密钥 **/
		public static final String PAAPPLET_SECRET = "paApplet_secret";
		/** 消息管理推送消息相关会员商城appid **/
        public static final String MEMBERAPPLET_APPID = "memberApplet_appid";
		/** 消息管理推送-服务通知 **/
		public static final String WECHAT_TMPMSG_PAAPPLET_SERVICENOTIFICATIONS = "wechat_tmpMsg_paApplet_serviceNotifications";
		/** 消息管理推送-单品推送 **/
		public static final String WECHAT_TMPMSG_PAAPPLET_GOODSNOTIFICATIONS = "wechat_tmpMsg_paApplet_goodsNotifications";
		/** 消息管理推送-特殊推送 **/
		public static final String WECHAT_TMPMSG_PAAPPLET_SPECIALNOTIFICATIONS = "wechat_tmpMsg_paApplet_specialNotifications";
		/** 消息管理推送-到货订阅通知 **/
		public static final String WECHAT_TMPMSG_PAAPPLET_GOODSARRIVAL = "wechat_tmpMsg_paApplet_goodsArrival";
		/** 消息管理推送-订单发货通知 **/
		public static final String WECHAT_TMPMSG_PAAPPLET_EXPRESSSEND = "wechat_tmpMsg_paApplet_expressSend";
		/** 消息管理推送-订单发货通知跳转路径 **/
		public static final String WECHAT_TMPMSG_PAAPPLET_EXPRESSSEND_PAGEPATH = "wechat_tmpMsg_paApplet_expressSend_pagePath";
		/** 消息管理推送-会员商城订单发货通知模板 **/
		public static final String WECHAT_TMPMSG_MEMBER_APPLET_EXPRESSSEND = "wechat_tmpMsg_member_applet_expressSend";
		/** 消息管理推送-会员商城订单发货通知跳转路径 **/
		public static final String WECHAT_TMPMSG_MEMBER_APPLET_EXPRESSSEND_PAGEPATH = "wechat_tmpMsg_member_applet_expressSend_pagePath";
		/** 商城秒杀预约提醒 **/
		public static final String WECHAT_TMPMSG_PAAPPLET_SECKILLREMIND = "wechat_tmpmsg_paapplet_seckillremind";
		/** 竞价活动提醒 **/
		public static final String WECHAT_TMPMSG_PAAPPLET_BIDDINGREMIND = "wechat_tmpmsg_paapplet_biddingRemind";
		/** 酒量1V1PK小程序公众号 **/
		public static final String APPLET_DRINKCAPACITYPK_APPID  = "applet_drinkcapacitypk_appid ";
		/** 酒量1V1PK小程序密码 **/
		public static final String APPLET_DRINKCAPACITYPK_SECRET  = "applet_drinkcapacitypk_secret ";
		/** 1V1PK匹配成功模板消息 **/
		public static final String APPLET_TMPMSG_DRINKCAPACITYPK  = "applet_tmpmsg_drinkCapacityPk ";
		/** 1V1PK判定结果模板消息 **/
		public static final String APPLET_TMPMSG_DRINKCAPACITYPKRESULT  = "applet_tmpmsg_drinkCapacityPkResult ";
		/** 优惠券发放提醒（提交评论审核成功后推送） **/
		public static final String APPLET_COUPON_RECEIVE_PROVIDE_REMIND  = "applet_coupon_receive_provide_remind";
		/** 优惠券过期提醒 **/
		public static final String APPLET_COUPON_RECEIVE_EXPIRE_REMIND  = "applet_coupon_receive_expire_remind";
		/** 促销员报名评选审核提醒模板ID **/
		public static final String WECHAT_TMPMSG_PROMOTION_CHECK_RESULT  = "wechat_tmpmsg_promotion_check_result";
		/** 促销员报名评选审核模板跳转小程序PagePath **/
		public static final String WECHAT_TMPMSG_PROMOTION_CHECK_PAGEPATH  = "wechat_tmpmsg_promotion_check_pagepath";
		/** 河北礼品卡定时退款 微信消息推送模板*/
		public static final String WECHAT_TMPMSG_GIFT_CARD  = "wechat_tmpMsg_gift_card";
		/** 礼品卡赠送通知模板 **/
		public static final String WECHAT_TMPMSG_GIFT_CARD_GIVE_REMID  = "GiftCardGiveRemidJobHandler";


	}
	/**
	 * filter http调用URL配置
	 * @author bin.zhang
	 */
	public static interface filterHttpUrl{
		/** 通用统计服务url **/
		public static final String DBTMAINENTSTATS_URL = "DBTMainEntStats_url";
		/** 前端获取测试Token地址 **/
		public static final String WCT_TOKEN_URL_H5 = "wct_token_url_h5";
		/** 腾讯KB请求URL **/
		public static final String KB_URL = "kb_url";
		/** 前端识别openid有效性的URL **/
		public static final String BIZ_OPENID_URL = "biz_openid_url";
		/** 二维码url **/
		public static final String QRCODE_URL = "qrcode_url";
		/** 接口1服务器外网地址 **/
		public static final String PROJECT_INTERFACE_URL = "project_interface_url";
		/** 接口1服务器外网地址 **/
		public static final String WECHAT_SHANGHAI_EXTRACT_URL = "wechat_shanghai_extract_url";
		/**山东关注推送提现连接 **/
		public static final String WECHAT_SHANDONGAGT_EXTRACT_URL = "wechat_shandongagt_extract_url";
		/** CRM服务器外网地址 **/
		public static final String CRM_INTERFACE_URL = "crm_interface_url";
		/** 微信运动首页地址 **/
		public static final String WECHAT_MOVEMENT_HOME_PAGE_URL = "wechat_movement_home_page_url";
		/** 前端获取小程序测试Token地址 **/
		public static final String WCT_TOKEN_URL_xcx = "wct_token_url_xcx";
		/** 终端接口地址 **/
		public static final String VMTS_INTERFACE_URL = "vmts_interface_url";
		/** 一元乐享请求地址 **/
		public static final String LX_SERVERURL = "lx_serverUrl";
	}
	/**
	 * filter活动UI版本配置
	 * @author bin.zhang
	 */
	public static interface filterActivityVersion{
		/** 1 **/
		public static final String ACTIVITYVERSION_TYPE1 = "activityVersion_type1";
		/** 2 **/
		public static final String ACTIVITYVERSION_TYPE2 = "activityVersion_type2";
		/** 3 **/
		public static final String ACTIVITYVERSION_TYPE3 = "activityVersion_type3";
		/** 4 **/
		public static final String ACTIVITYVERSION_TYPE4 = "activityVersion_type4";
		/** 5 **/
		public static final String ACTIVITYVERSION_TYPE5 = "activityVersion_type5";
		/** 6 **/
		public static final String ACTIVITYVERSION_TYPE6 = "activityVersion_type6";
		/** 7 **/
		public static final String ACTIVITYVERSION_TYPE7 = "activityVersion_type7";
		/** 8 **/
		public static final String ACTIVITYVERSION_TYPE8 = "activityVersion_type8";
		/** 9 **/
		public static final String ACTIVITYVERSION_TYPE9 = "activityVersion_type9";
		/** 10 **/
		public static final String ACTIVITYVERSION_TYPE10 = "activityVersion_type10";
	}
	/**
	 * filter基础奖项类型
	 * @author bin.zhang
	 */
	public static interface filterPrizeName{
		/** 现金红包 **/
		public static final String PRIZE_TYPE0 = "prize_type0";
		/** 积分红包 **/
		public static final String PRIZE_TYPE1 = "prize_type1";
		/** 积分现金红包 **/
		public static final String PRIZE_TYPE2 = "prize_type2";
	}
	/**
	 * filter实物奖项类型
	 * @author bin.zhang
	 */
	public static interface filterGrandPrize{
		/** 一等奖-歌诗达邮轮之旅 **/
		public static final String PRIZE_TYPE5 = "prize_type5";
		/** 二等奖-草莓音乐节门票一张 **/
		public static final String PRIZE_TYPE6 = "prize_type6";
		/** 三等奖-俄罗斯之旅 **/
		public static final String PRIZE_TYPE7 = "prize_type7";
		/** P等奖-冬奥环球之旅 **/
		public static final String PRIZE_TYPEP = "prize_typeP";
		/** Q等奖-冰雪冬令营 **/
		public static final String PRIZE_TYPEQ = "prize_typeQ";
		/** R等奖-青啤有一套' **/
		public static final String PRIZE_TYPER = "prize_typeR";
		/** S等奖 **/
		public static final String PRIZE_TYPES = "prize_typeS";
		/** T等奖 **/
		public static final String PRIZE_TYPET = "prize_typeT";
		/** U等奖 **/
		public static final String PRIZE_TYPEU = "prize_typeU";
		/** V等奖 **/
		public static final String PRIZE_TYPEV = "prize_typeV";
		/** W等奖 **/
		public static final String PRIZE_TYPEW = "prize_typeW";
		/** X等奖 **/
		public static final String PRIZE_TYPEX = "prize_typeX";
		/** Y等奖 **/
		public static final String PRIZE_TYPEY = "prize_typeY";
		/** Z等奖 **/
		public static final String PRIZE_TYPEZ = "prize_typeZ";
		/** P01等奖 **/
		public static final String PRIZE_TYPEP01 = "prize_typeP01";
		/** P02等奖 **/
		public static final String PRIZE_TYPEP02 = "prize_typeP02";
		/** P03等奖 **/
		public static final String PRIZE_TYPEP03 = "prize_typeP03";
		/** P04等奖 **/
		public static final String PRIZE_TYPEP04 = "prize_typeP04";
		/** P05等奖 **/
		public static final String PRIZE_TYPEP05 = "prize_typeP05";
		/** P06等奖 **/
		public static final String PRIZE_TYPEP06 = "prize_typeP06";
		/** P07等奖 **/
		public static final String PRIZE_TYPEP07 = "prize_typeP07";
		/** P08等奖 **/
		public static final String PRIZE_TYPEP08 = "prize_typeP08";
		/** P09等奖 **/
		public static final String PRIZE_TYPEP09 = "prize_typeP09";
		/** P10等奖 **/
		public static final String PRIZE_TYPEP10 = "prize_typeP10";
		/** 爆款产品A套装 **/
		public static final String PRIZE_TYPEZ001001 = "prize_typeZ001001";
		/** Z001002-爆款产品B套装 **/
		public static final String PRIZE_TYPEZ001002 = "prize_typeZ001002";

	}
	/**
	 * filter优惠券类型
	 * @author bin.zhang
	 */
	public static interface filterCoupon{
		/** H优惠券（京东券） **/
		public static final String PRIZE_TYPEH = "prize_typeH";
		/** I优惠券 **/
		public static final String PRIZE_TYPEI = "prize_typeI";
		/** J优惠券 **/
		public static final String PRIZE_TYPEJ = "prize_typeJ";
		/** K优惠券 **/
		public static final String PRIZE_TYPEK = "prize_typeK";
		/** L优惠券 **/
		public static final String PRIZE_TYPEL = "prize_typeL";
		/** M优惠券 **/
		public static final String PRIZE_TYPEM = "prize_typeM";
		/** N优惠券 **/
		public static final String PRIZE_TYPEN = "prize_typeN";
	}
	/**
	 * filter特权卡
	 * @author bin.zhang
	 */
	public static interface filterPrivilege{
		/** 翻倍卡 **/
		public static final String ALLOWANCE_TYPEA = "allowance_typeA";
	}
	/**
	 * filter集卡奖项类型
	 * @author bin.zhang
	 */
	public static interface filterCollectCards{
		/** 类别 **/
		public static final String CARD_TYPE = "card_type";
		/** A卡 **/
		public static final String CARD_TYPEA = "card_typeA";
		/** B卡 **/
		public static final String CARD_TYPEB = "card_typeB";
		/** C卡 **/
		public static final String CARD_TYPEC = "card_typeC";
		/** D卡 **/
		public static final String CARD_TYPED = "card_typeD";
		/** E卡 **/
		public static final String CARD_TYPEE = "card_typeE";
		/** F卡 **/
		public static final String CARD_TYPEF = "card_typeF";
		/** G卡 **/
		public static final String CARD_TYPEG = "card_typeG";
		/** H卡 **/
		public static final String CARD_TYPEH = "card_typeH";
		/** I卡 **/
		public static final String CARD_TYPEI = "card_typeI";
		/** J卡 **/
		public static final String CARD_TYPEJ = "card_typeJ";
	}
	/**
	 * filter特殊金额配置
	 * @author bin.zhang
	 */
	public static interface filterSpecialAmountSetting{
		/** 金额对应的默认图片 **/
		public static final String QRCODE_DEFAULTCONTENTURL = "qrcode_defaultContentUrl";
		/** 电话 **/
		public static final String QRCODE_PHONENUM = "qrcode_phoneNum";
		/** 金额 **/
		public static final String QRCODE_MONEY_TYPE1 = "qrCode_money_type1";
		/** 奖项配置主键 **/
		public static final String QRCODE_VPOINTSCOGKEY_TYPE1 = "qrcode_vpointsCogKey_type1";
		/** 金额对应的图片 **/
		public static final String QRCODE_CODECONTENTURL_TYPE1 = "qrcode_codeContentUrl_type1";
		/** 获取限制次数 **/
		public static final String QRCODE_MAX_COUNT_TYPE1 = "qrCode_max_count_type1";
		/** 金额 **/
		public static final String QRCODE_MONEY_TYPE2 = "qrCode_money_type2";
		/** 奖项配置主键 **/
		public static final String QRCODE_VPOINTSCOGKEY_TYPE2 = "qrcode_vpointsCogKey_type2";
		/** 金额对应的图片 **/
		public static final String QRCODE_CODECONTENTURL_TYPE2 = "qrcode_codeContentUrl_type2";
		/** 获取限制次数 **/
		public static final String QRCODE_MAX_COUNT_TYPE2 = "qrCode_max_count_type2";
		/** 金额 **/
		public static final String QRCODE_MONEY_TYPE3 = "qrCode_money_type3";
		/** 奖项配置主键 **/
		public static final String QRCODE_VPOINTSCOGKEY_TYPE3 = "qrcode_vpointsCogKey_type3";
		/** 金额对应的图片 **/
		public static final String QRCODE_CODECONTENTURL_TYPE3 = "qrcode_codeContentUrl_type3";
		/** 获取限制次数 **/
		public static final String QRCODE_MAX_COUNT_TYPE3 = "qrCode_max_count_type3";
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
		/** 3.0crm精准营销开关 **/
		public static final String SWITCH_GROUP = "switch_group";
		/**运动达人PK赛开关 **/
		public static final String WECHAT_MOVEMENT_PK_SWITCH = "wechat_movement_pk_switch";
		/**积分商城-订单-发货-短信通知开关 **/
		public static final String SWITCH_SEND_MSG = "switch_send_msg";
        /** 省区会员体系开关**/
        public static final String SWITCH_VIP_SYSTEM = "switch_vip_system";
		/**积分商城开关 **/
		public static final String SWITCH_PIJIUHUA = "switch_piJiuHua";
	}
	/**
	 * filter管理平台验证码
	 * @author bin.zhang
	 */
	public static interface filterPlatformVerifycode{
		/** 管理平台验证码 **/
		public static final String PLATFORM_VERIFYCODE = "platform_verifycode";
		/** 月排名时间yyyyMM **/
		public static final String OTHER_MONTHRANK_ONLINETIME = "other_monthRank_onlineTime";
	}
    
    /**
     * filter一元乐享（兑付通）
     * @author bin.zhang
     */
    public static interface filterLx{
        /** 乐享企业KEY（当前企业） **/
        public static final String LX_APPKEY = "lx_appKey";
        /** 乐享请求秘钥 **/
        public static final String LX_APPSECRET = "lx_appSecret";
    }
	/**
	 * filter其他
	 * @author bin.zhang
	 */
	public static interface filterOthers{
	    /** 忠实消费者回馈上线yyyyMM **/
	    public static final String OTHER_MONTHPRIZE_ONLINETIME = "other_monthPrize_onlineTime";
		/** 码源自动化密钥 **/
		public static final String AUTO_QRCODE_SECRET = "auto_qrcode_secret_";
	}
	
	/**
     * 项目job
     * @author bin.hanshimeng
     */
    public static interface ProjectJob{
    	/** 可疑用户一个月后自动进入黑名单job **/
    	public static final String DUBIOUS_USER_CONVERT_BLACK_USER = "dubious_user_convert_black_user";
    	/** 更新活动配置中各积分区间剩余数量job **/
    	public static final String UPDATE_BATH_WAIT_ACTIVITY_VPOINTS_COG = "update_bath_wait_activity_vpoints_cog";
    	/** 一码双奖清除已结束且未清除过的活动的标签job **/
    	public static final String CLEAR_DOUBLE_PRIZE_FOR_USER = "clear_double_prize_for_user";
    	/** 一码双奖清除过期的已中出奖项 job **/
    	public static final String CLEAR_DOUBLE_PRIZE_LOTTERY = "clear_double_prize_lottery";
    	/** 商城更新订单签收状态，发货后15天 job **/
    	public static final String UPDATE_EXPRESS_SIGN_JOB = "update_express_sign_job";
    	/** 模板消息推送  job **/
    	public static final String SEND_TEMPLATE_MSG = "send_template_msg";
    	/** 统计规则到期job **/
    	public static final String TOTAL_MSG_EXPIRE_REMIND = "total_msg_expire_remind";
    	/** 清楚到期提醒job **/
    	public static final String CLEAN_MSG_EXPIRE_REMIND_INFO = "clean_msg_expire_remind_info";
    	/** 统计红包个数job **/
    	public static final String TOTAL_RED_PACKET_MSG_EXPIRE_REMIND = "total_red_packet_msg_expire_remind";
    	/** 可疑用户释放job **/
    	public static final String UPDATE_RELEASE_OF_SUSPECTS = "update_release_of_suspects";
    	/** 月度城市酒王排行job **/
    	public static final String EXECUTE_RANK_HISTORY = "execute_rank_history";
    	/** 大奖回收job **/
    	public static final String RECYCLE_PRIZE = "recycle_Prize";
    	/** 更新产品销量 **/
		public static final String UPDATE_SHOWNUM = "update_showNum";
		/** 更新产品销量 **/
		public static final String CHECK_IMG = "check_img";
    	/** 商城秒杀预约提醒JOB **/
    	public static final String vpointsSecKillRmindJob = "vpoints_sec_kill_remind_job";
        /** 商城优惠券过期提醒JOB **/
        public static final String vpointsCouponExpireRmindJob = "vpoints_coupon_expire_remind_job";
    	
    	/** 微信运动同步步数提醒job **/
    	public static final String WECHAT_MOVEMENT_MESSAGE_SYNC_STEP = "wechat_movement_message_sync_step";
    	/** 微信运动每日结果job **/
    	public static final String WECHAT_MOVEMENT_COMPETITION_RESULT = "wechat_movement_competition_result";
    	/** 微信运动每日PK匹配job **/
    	public static final String WECHAT_MOVEMENT_PK_RELATION = "wechat_movement_pk_relation";
    	/** 微信运动每日PK结果job **/
    	public static final String WECHAT_MOVEMENT_PK_RESULT = "wechat_movement_pk_result";
    	
    	/** 河南竞价活动开始提醒job **/
    	public static final String BIDDING_ACTIVITY_START = "bidding_activity_start";
    	/** 河南竞价活动即将结束提醒job **/
    	public static final String BIDDING_ACTIVITY_END = "bidding_activity_end";
    	/** 未生成成功的自动码源订单Job **/
    	public static final String QRCODE_AUTO_ORDER_FAILED = "qrcode_auto_order_failed";
    	/** 礼品卡自动退款job **/
    	public static final String GIFT_CARD_REVOKE_ORDER_JOB = "gift_card_revoke_order_job";
		/** 跑马灯删除数据job **/
    	public static final String MARQUEE_INFO_UPDATE_JOB = "marquee_info_update_job";
		/** 分享裂变自动删除无效分享人数据job **/
    	public static final String CLEAR_ACTIVATE_RED_ENVELOPE_RECORD_JOB = "clear_activate_red_envelope_record_job";
		/** 礼品卡赠送通知job **/
		public static final String GIFT_CARD_GIVE_JOB_HANDLER = "gift_card_give_job_handler";
		public static final String BACK_MONEY_TO_RED_ENVELOPE_ACTIVITY_JOB = "back_money_to_red_envelope_job";

    }
    
	/**
	 * 企业基础信息
	 * @author hanshimeng
	 *
	 */
	public static interface companyInfo{
		/** 企业名称 **/
		public static final String COMPANY_NAME = "company_name";
		/** 企业负责人 **/
		public static final String COMPANY_LINK_USER = "company_link_user";
		/** 企业联系人 **/
		public static final String COMPANY_PHONE = "company_phone";
		/** 企业邮箱 **/
		public static final String COMPANY_EMAIL = "company_email";
		/** 企业合同日期 **/
		public static final String COMPANY_CONTRACT_DATE = "company_contract_date";
		/** 企业登录手机号 **/
		public static final String COMPANY_LOGIN_PHONE = "company_login_phone";
		/** 赋码厂 **/
		public static final String COMPANY_QRCODE_MANUFACTURE_NAME = "company_qrcode_manufacture_name";
		/** 酒厂 **/
		public static final String COMPANY_FACTORY_NAME = "company_factory_name";
		/** 省区标识 **/
		public static final String PROJECT_SERVER_NAME = "project_server_name";
	}

	/***
	 * 发短信账户信息详情
	 */
	public static interface accountOfsendSMS {
		
		/** 账户url **/
		public static final String ACCOUNT_URL = "account_url";
		/** 账户pwd */
		public static final String ACCOUNT_PWD = "account_pwd";
		/** 账户cid **/
		public static final String ACCOUNT_CID = "account_cid";
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/** 账户sname **/
		public static final String SNAME = "sname";
		/** 账户spwd **/
		public static final String SPWD = "spwd";
		/** 账户scorpid **/
		public static final String SCORPID = "scorpid";
		/** 账户sprdid **/
		public static final String SPRDID = "sprdid";
		/** 账户postUrl */
		public static final String POSTURL = "postUrl";
        
        /** 平台登录手机号白名单列表 */
        public static final String VERI_CODE_PLATFROM_PHONENUMS = "veri_code_platfrom_phonenums";
	}
	
    /***
     * 短信内容(code=app_getsms)
     */
    public static interface app_sms_content {
        /** 广西项目一等奖短信内容**/
        public static final String QPGX_GETSMS_VERICODE = "qpgx_getsms_vericode";
        /** 广西项目验证码短信内容**/
        public static final String QPGX_GETSMS_CHECKCODE = "qpgx_getsms_checkcode";
        /** 管理平台验证码短信内容**/
        public static final String SEND_TYPE_CHECK_CODE_PLATFORM = "qpgx_getsms_checkcode_platform";
    }

    /** 统计相关 */
    public static interface ent_stats {
        /** 分类code */
        public static final String CATEGORY_CODE = "ent_stats";
        /** 总积分成本 */
        public static final String TOTAL_VPOINTS = "total_vpoints";
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
        /** 每日战报发送人**/
        public static final String TICKET_SEND_DAILYREPORT_MAIL = "ticket_send_dailyreport_mail";
        /** 每日战报抄送人**/
        public static final String TICKET_CC_DAILYREPORT_MAIL = "ticket_cc_dailyreport_mail";
        /** 每日大奖详情发送人**/
        public static final String TICKET_SEND_PRIZE_MAIL = "ticket_send_prize_mail";
        /** 运营操作促销员时间**/
        public static final String OPERATION_PROMOTION_TIME = "operation_promotion_time";

    }
    public static interface SfJSDKey{
		String sf_jsd_dev_id = "sf_jsd_dev_id";
		String sf_jsd_shop_id = "sf_jsd_shop_id";
		String sf_jsd_appKey = "sf_jsd_appKey";
	}
}
