package com.dbt.framework.base.bean;

import com.dbt.framework.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @FileName Constant.java<br>
 * @author 谷长鹏<br>
 * @createTime 2014-02-19 15:44:56<br>
 * @description 常量类[请不要使用IDE工具进行格式化，对枚举数据有影响！！！]<br>
 * @Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.<br>
 */
public class Constant {

	public static final String SERVERNAME = PropertiesUtil.getPropertyValue("server_name");


	public final static String DBTSPLIT = "_";
    /** cache分组 **/
    public final static String GROUPSPLIT = "G-R-O-U-P";
    /** 多个参数分隔符 *. */
	public final static String THREEPAR_SPLIT_CHART = "#@";

	/**
	 * 用户session名
	 */
	public static final String USER_SESSION = "userSession";
	
	/**
	 * 功能开关标志
	 */
	public static interface SwitchFlag {
	    public final static String ON = "1";
	    public final static String OFF = "0";
	}
	
	/**
	 * 功能开关标志
	 */
	public static interface loginType {
	    /** pwd密码登录*/
	    public final static String PWD = "PWD";
	    /** uid登录*/
	    public final static String UID = "UID";
	}
    
	/**
     * 参与定时抽奖渠道类型
     */
    public static interface ticketLotteryChannelType {
        /** 酒行渠道**/
        static String type0 = "channelNum0";
        /** 餐饮渠道**/
        static String type1 = "channelNum1";
        /** KA渠道**/
        static String type2 = "channelNum2";
        /** 电商渠道**/
        static String type3 = "channelNum3";
        /** 分享**/
        static String type4 = "shareNum";
        /** 关注订阅号**/
        static String type5 = "subscriptionNum";
    }
	
    /**
     * 启用状态
     */
    public static interface statusFlag {
        /** 0未启用*/
        public final static String STATUS_0 = "0";
        /** 1已启用*/
        public final static String STATUS_1 = "1";
    }

    /**
     * 状态码：code
     */
    public static interface ResultCode {
        /**  -1. */
        public static final String MINUS_ONE = "-1";
        /** 成功：0 */
        public static final String SUCCESS = "0";
        /** 失败:1 */
        public static final String FILURE = "1";
        /** 业务状态码：2 */
        public static final String RESULT_TWO = "2";
        /** 业务状态码：3 */
        public static final String RESULT_THREE = "3";
        /** 业务状态码：4 */
        public static final String RESULT_FOURE = "4";
        /** 业务状态吗：5 */
        public static final String RESUlT_FIVE = "5";
        /** 业务状态吗：6 */
        public static final String RESULT_SIX = "6";
        /** 业务状态码:7 */
        public static final String RESULT_SEVEN = "7";
        /** 业务编码：8 */
        public static final String RESULT_EIGHT = "8";
        /** 业务编码：9 **/
        public static final String RESULT_NINE = "9";
        /** 业务编码：10 **/
        public static final String RESULT_TEN = "10";
        /** 业务编码：11 **/
        public static final String RESULT_ELEVEN = "11";
        /** 业务编码：12 **/
        public static final String RESULT_TWELVE = "12";
        /** 业务编码：13 **/
        public static final String RESULT_THIRTEEN = "13";
        /** 业务编码：14 **/
        public static final String RESULT_FOURTEEN = "14";
    }
    
	/**
	 * 缓存id
	 */
	public static final String CACHE_POOLID = "globalCache";
	public static Integer version = 1;
	public static String environment = "1";
	public static String NULL = null;
	public static String EMPTY = "";
	public static int pageSize = 10;
	/**
	 * 日志类型
	 */
	public static final String LOG_TYPE = "0";

	/**
	 * 操作状态
	 * 
	 * @author 姜世杰
	 * 
	 */
	public static interface OperateStatus {
		public final static int enable = 0;
		public final static int disable = 1;
	}

	/**
	 * 布尔类型
	 * 
	 * @author 2014-02-19
	 */
	public static interface booleanType {
		/** 表示true **/
		public static final String RIGHT_ = "1";
		/** 表示false **/
		public static final String ERROR_ = "0";
	}

	/**
	 * 操作标识
	 * 
	 * @author 姜世杰
	 */
	public static interface DbDelFlag {
		public final static String noDel = "0";
		public final static String del = "1";
	}

	public static interface Operation {
		public final static String ADD = "add";
		public final static String EDIT = "edit";
		public final static String DEL = "delete";
	}

	/**
	 * 默认排序号
	 */
	public final static long defaultOrderNum = 0;

	/**
	 * 如果有父子间关系的判断，此处记录初始化的父ID
	 */
	public final static long initParentRootId = -1;

	/**
	 * 当前时间时间+50年
	 */
	public static long dateAddFiftyYear = 1000 * 60 * 60 * 24 * 365 * 50;

	/**
	 * 资源池活动积分报警值
	 */
	public static final int resourcePoolAlarmValue = 5000000;
	/** 二维码表前缀 **/
	public static final String QRCODE_TABLE_PREFIX = "vps_qrcode_";

	/**
	 * 所属平台
	 */
	public static interface Platform {
		public final static String operation = "0000"; // 运维平台
		public final static String custom = "0001"; // 客户关系管理平台
	}

	/**
	 * 广告状态
	 */
	public static interface AdStatus {
		public final static String open = "0";
		public final static String pause = "1";
	}

	public static interface AdStatus_Text {
		public final static String open = "开启";
		public final static String pause = "暂停";
	}

	public static interface UserStatus_Text {
		public final static String use = "启用";
		public final static String freeze = "停用";
	}

	/**
	 * 活动类型
	 */
	public static interface EventsType {
		public final static String weekly_prize = "0000"; // 每周一奖
		public final static String base_activity = "0001"; // 基础活动
		public final static String prize_activity = "0002"; // 有奖活动
		public final static String club_activity = "0003"; // 俱乐部活动
	}


	/**
	 * 活动状态
	 */
	public static interface EventsStatus {
		public final static String wait = "5"; // 即将开始
		public final static String doing = "6"; // 进行中
		public final static String done = "7"; // 结束
		public final static String pause = "8"; // 暂停

	}

	public static interface EventsStatus_Text {
		public final static String wait = "未开始";
		public final static String doing = "进行中";
		public final static String done = "已完成";
		public final static String pause = "暂停";
	}

	/**
	 * 审核状态
	 */
	public static interface AuditStatus {
		public final static String wait = "0"; // 待审核
		public final static String done = "1"; // 已通过
		public final static String reject = "2"; // 已驳回
	}

	/**
	 * 审核类型
	 * 
	 */
	public static interface AuditType {
		/**
		 * 新增:0
		 */
		public final static String ADD_ZERO = "0";
		/**
		 * 编辑：1
		 */
		public final static String EDIT_ONE = "1";
		/**
		 * 删除2
		 */
		public final static String DEL_TWO = "2";

	}

	public static interface AuditStatus_Text {
		public final static String wait = "待审核";
		public final static String done = "已通过";
		public final static String reject = "已驳回";
	}

	public static interface ProfitStatus_Text {
		public final static String wait = "待返利";
		public final static String done = "已返利";
		public final static String reject = "未返利";
	}

	/**
	 * 商品所属
	 */
	public static interface GoodsBelongs {
		public final static String storage = "0000"; // 兑换商城
		public final static String weekly_prize = "0001"; // 每周一奖
		public final static String club_prize = "0002"; // 俱乐部兑奖
		public final static String scan_prize = "0003"; // 扫描中奖
	}

	public static interface GoodsBelongs_Text {
		public final static String storage = "兑换商城";
		public final static String weekly_prize = "每周一奖";
		public final static String club_prize = "俱乐部兑奖";
		public final static String scan_prize = "扫描中奖";
	}

	/**
	 * 商品类型(for APP)
	 */
	public static interface AppGoodsSort {
		public final static String cards = "0"; // 点卡
		public final static String credits = "1"; // 优惠券
		public final static String feature = "2"; // 特色
	}

	public static interface AppGoodsSort_Text {
		public final static String cards = "点卡";
		public final static String credits = "优惠券";
		public final static String feature = "特色";
	}

	/**
	 * 奖品类型
	 */
	public static interface GoodsType {
		public final static String virtual = "0"; // 虚拟
		public final static String material = "1"; // 实物
	}

	public static interface GoodsType_Text {
		public final static String virtual = "虚拟";
		public final static String material = "实物";
	}

	public static interface CardStatus {
		public final static String in_use = "0";
		public final static String no_use = "1";
	}

	/**
	 * 奖品状态
	 */
	public static interface Status_Text {
		public final static String ENOUGH = "奖池充足";
		public final static String NONE = "奖品不足";
	}

	/**
	 * 返利类型
	 * 
	 * @author wj1
	 * 
	 */
	public static interface Rebets_Type {
		public final static String vscan_code = "VE"; // 返利活动
		public final static String receipt_code = "RE"; // 扫小票活动
	}

	/**
	 * 扫小票活动是否显示活动说明 的状态字段
	 * 
	 * @author HaoQi
	 * 
	 */
	public static interface ReceiptEvents_ifShowDesc_Type {
		public final static String SHOW = "1"; // 显示
		public final static String HIDE = "0"; // 隐藏
	}

	/**
	 * 链接来源类型(新小票活动申请和审核已用到)
	 * 
	 * @author HaoQi
	 * 
	 */
	public static interface LinkType {
		/** 从菜单进入 */
		public static final String TYPE_MENU = "0";
		/** 从列表检索/分页进入 */
		public static final String TYPE_SEARCH = "1";
		/** 从返回按钮或者操作完毕后刷新列表进入 */
		public static final String TYPE_BACK = "2";
	}

	/**
	 * 定义在session中存入数据的key
	 * 
	 * @author HaoQi
	 * 
	 */
	public static interface SessionKeys {
		/** 小票返利申请查询条件sessionKey */
		public static final String RECEIPTS_ACTIVITY_APPLY_SEARCH = "receiptsActivityApplySearch";
		/** 小票返利审批查询条件sessionKey */
		public static final String RECEIPTS_ACTIVITY_APPROVE_SEARCH = "receiptsActivityApproveSearch";
	}

	public static Map<String, String> phoneMap = new HashMap<String, String>();
	static {
		phoneMap.put("100", "ios");
		phoneMap.put("200", "android");
		phoneMap.put("300", "塞班");
		phoneMap.put("400", "windowphone");
		phoneMap.put("500", "java");
		phoneMap.put("600", "黑莓");
		phoneMap.put("700", "MeeGo");
		phoneMap.put("800", "Bada");
		phoneMap.put("1000", "其他");
	}

	/**
	 * 平台id类型
	 * 
	 * @author cpgu
	 */
	public static interface platformIdType {
		public static final String ISO_ = "100";
		public static final String ANDROID_ = "200";
		public static final String SAIBAN_ = "300";
		public static final String windowphone_ = "400";
		public static final String JAVA_ = "500";
		public static final String HEIMEI_ = "600";
		public static final String MEEGO_ = "700";
		public static final String BADA_ = "800";
		public static final String OTHER_ = "900";

	}

	/**
	 * 奖品类型
	 */
	public static interface CardPwdStatus {
		public final static String valid = "0"; // 有效
		public final static String send = "1"; // 已发
	}

	public static interface CardPwdStatus_Text {
		public final static String valid = "有效";
		public final static String send = "已发";
	}

	/**
	 * 运费类型
	 */
	public static enum ExpressTypes {
		FREE("0", "免快递费"),
		WAIT_PAY("1", "货到付款"),
		;

		private String id;
		private String name;

		private ExpressTypes(String id, String name) {
			this.id = id;
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public static String getNameById(String id) {
			for (ExpressTypes type : ExpressTypes.values()) {
				if (id.equals(type.getId())) {
					return type.getName();
				}
			}
			return "";
		}
	}

	/**
	 * 赋码数量类型
	 */
	public static enum EndowedType {

		Package("1", "箱"),
		Bottle("2", "瓶"),
		Branch("3", "支"),
		Boxs("4", "盒"),
		Bag("5", "袋"),
		Can("6", "听/罐"),
		Dozen("7", "包/提"),
		Stick("8", "条"),
		Block("9", "块"),
		;

		private String id;
		private String name;

		private EndowedType(String id, String name) {
			this.id = id;
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	/**
	 * 产品来源
	 */
	public static interface ApplicationType {
		public final static String APP = "1"; // V积分APP
		public final static String H5site = "2"; // H5站
		public final static String weixin = "3"; // 微信
	}

	/**
	 * 消息类型
	 */
	public static interface MessageType {
		public final static String system = "0"; // 系统通告
		public final static String service = "1"; // 服务通知
	}

	/**
	 * 消息阅读状态
	 */
	public static interface ReadStatus {
		public final static String not = "0"; // 未读
		public final static String done = "1"; // 已读
	}

	/**
	 * 账户类型
	 */
	public static interface AccountType {
		public final static String maintainer = "0"; // 运维(DBT)
		public final static String enterprise = "1"; // 企业主账号
		public final static String sub_snterprise = "2"; // 企业子账号
	}

	/**
	 * 积分方向
	 */
	public static interface IntegrationDirection {
		public final static String decrease = "0"; // 减少
		public final static String increase = "1"; // 增加
	}

	/**
	 * 积分来源
	 */
	public static interface IntegrationOccurType {
		public final static String scan_vcode = "0"; // 扫一扫
		public final static String shake_shake = "1"; // 摇一摇动作
		public final static String weekly_price = "4"; // 每周一奖
		public final static String dbt_given = "5"; // DBT赠送
		public final static String receipts = "6"; // 超市返利
		public final static String receipts_activity = "9"; // 全民拍小票
	}

	/**
	 * 角色key
	 */
	public static interface UserKey {
		/**
		 * 消费者最高权限角色：customer_rolekey
		 */
		public final static String CUSTOMER_ROLEKEY = "customer_rolekey";
		/**
		 * 运营最高权限角色：dbt_rolekey
		 */
		public final static String DBT_USERTYPE = "dbt_usertype";
	}

	/** 兑换奖品状态 */
	public static class exchangeState {
		/** 等待审核状态 */
		public static final String AUDIT_STATE = "0";
		/** 等待排期状态 */
		public static final String SCHEDULE_STATE = "1";
		/** 正在进行状态 */
		public static final String PROCESSING_STATE = "2";
		/** 已经下线状态 */
		public static final String OFFLINE_STATE = "3";
	}

	/**
	 * 活动类型
	 */
	public static interface activityType {
	    /** 扫码活动 */
	    public static final String activity_type0 = "0";
	    /** 一罐一码活动 */
	    public static final String activity_type1 = "1";
	    /** 一瓶一码活动 */
	    public static final String activity_type2 = "2";
	    /** 一万一批活动 */
	    public static final String activity_type3 = "3";
	    /** 签到活动 */
	    public static final String activity_type4 = "4";
	    /** 捆绑促销活动 */
	    public static final String activity_type5 = "5";
	    /** 一码双奖活动 */
		public static final String activity_type6 = "6";
	    /** 裂变红包 */
		public static final String activity_type7 = "7";
		/** 转盘抽奖活动 */
		public static final String activity_type8 = "8";
		/** 转盘抽奖活动--精细化营销版本 */
		public static final String activity_type9 = "9";
		/**红包雨**/
		public static final String activity_type12 = "12";
		/**天降红包**/
		public static final String activity_type13 = "13";
	}

	// 时间默认为空的起止串
	public static String SD = "00000000000000";
	public static String ED = "99991231235959";

	/**
	 * 奖等名称
	 * 
	 * @author gucp 2014-05-22
	 */
	public static interface prizeLevelName {
		public static String ONE_ = "1等奖";
		public static String TWO_ = "2等奖";
		public static String THREE_ = "3等奖";
	}

	/**
	 * 系统用户
	 * 
	 * @author gucp 2014-07-25
	 */
	public static interface sysUserBasis {
		/** DBT用户类型 **/
		public static final String USERTYPE_0 = "0";
		/** 企业主账户 **/
		public static final String USERTYPE_1 = "1";
		/** 企业子账户 **/
		public static final String USERTYPE_2 = "2";

		/** 是否为外协：1-外协 **/
		static final String USER_IF_OUT_SOURCE_1 = "1";
		/** 是否为外协：0-内部员工 **/
		static final String USER_IF_OUT_SOURCE_0 = "0";
	}

	/**
	 * 微信小票活动奖品类型
	 * 
	 * @author RoyFu
	 * @createTime
	 * @description
	 */
	public static interface WctReceiptPrizeType {
		public static final String PHONE_BILL = "1"; // 电话费
		public static final String TICKET_CODE = "2"; // 电影票
		public static final String FACIAL_MASK = "3"; // 面膜
	}

	/**
	 * 
	 */
	public static interface MessageCode {
		public static final String receipts = "1"; // 小票
		public static final String vcode = "2"; // V码
		public static final String activity = "3"; // 活动
		public static final String convert = "4"; // 兑换
	}

	/**
	 * 发送消息来源类型
	 */
	public static interface MessageSourceType {
		public static final String message = "1"; // 消息
		public static final String receipts = "2"; // 小票
		public static final String vcode = "3"; // V码
		public static final String activity = "4"; // 活动
		public static final String convert = "5"; // 兑换
	}

	/**
	 * 发送消息来源标题码
	 */
	public static interface MessageSourceCode {
		public static final String receipts = "小票返利"; // 小票
		public static final String newTask = "新手任务活动"; // 活动
		public static final String extraAwards = "额外奖励"; // APP上传小票，预选正确的返利商品，送额外积分奖励
	}

	public static String MESSAGE_NEW_TASK = "完成新手任务";
	public static String MESSAGE_EXTRA = "您于#{1}上传的小票，额外获得了#{2}V积分";

	public static interface ApprovalOpinion {
		public final static int AGREE = 0; // 同意
		public final static int NOT_RECEIPTS = 1; // 不是小票

		public final static int NOT_CLEAR = 2; // 小票内容不清楚
		public final static int NOT_CLEAR_NOT_SMOOTH = 21; // 小票不平整
		public final static int NOT_CLEAR_AUGULAR_BIAS = 22; // 拍摄角度偏
		public final static int NOT_CLEAR_FONT_SMALL = 23; // 字迹过小无法识别
		public final static int NOT_CLEAR_IMG_DIM = 24; // 图片模糊不对焦

		public final static int NOT_INTACT = 3; // 小票不完整
		public final static int NOT_INTACT_HEAD_TAIL = 31; // 首尾信息不完整
		public final static int NOT_INTACT_LEFT_RIGHT = 32; // 左右信息不完整
		public final static int NOT_INTACT_KEEP_OUT = 33; // 小票信息遮挡

		public final static int OVER_PERIOD = 4; // 小票超期
		public final static int RECEIPTS_DUPLICATED = 5; // 小票重复
		public final static int NOT_IN_RULE = 6; // 小票不符合返利规则
		public final static int INNER_TEST = 7; // 内部测试
		public final static int NOT_SUPPORT = 8; // 暂不支持超市返利
		public final static int REBATE_AT_DISCOUNT = 200; // 折扣返利
		public final static int VIRTUAL_MEMBER_RECEIPTS = 201;// 联合会员小票
		public final static int USER_REISSUE_POINTS = 202;// 用户补偿积分
	}

	/**
	 * 运营推送消息状态
	 * 
	 * @author Raymond
	 * 
	 */
	public static interface VpsPushMessageInfoM {
		public final static String status_0 = "0"; // 草稿状态
		public final static String status_1 = "1"; // 待发送状态
		public final static String status_2 = "2"; // 已发送状态
	}

	/**
	 * 请求PUSH服务器之后返回的状态
	 * 
	 * @author Raymond
	 * 
	 */
	public static interface VpsPushMessageStatus {
		public final static String status_0 = "0"; // 推送成功
		public final static String status_1 = "1"; // 处理失败
		public final static String status_2 = "2"; // 参数为空
	}

	/**
	 * 推送类型
	 * 
	 * @author Raymond
	 * 
	 */
	public static interface VpsPushMessagePushType {
		public final static String type_1 = "1"; // 即时推送
		public final static String type_2 = "2"; // 定时推送
	}

	/**
	 * 推送方式
	 * 
	 * @author Raymond
	 * 
	 */
	public static interface VpsPushMessagePushMethod {
		public final static String method_1 = "1"; // 前端PUSH
		public final static String method_2 = "2"; // 后端我的消息
	}

	/**
	 * 推送类型
	 * 
	 * @author Raymond
	 * 
	 */
	public static interface VpsPushMessageType {
		public final static String all_of_users = "0"; // 所有用户
		public final static String rebate_activity = "1"; // 返利活动
	}

	public static interface DepartmentCode {
		public final static String dbt = "DBT";
		public final static String operate_system = "OPS";
	}

	/** 抢红包活动的状态 */
	public static interface GiftspackActivityStatus {
		/** 草稿 */
		public final static String state_draft = "0";
		/** 审批通过(即将开始) */
		public final static String state_approved = "1";
		/** 进行中 */
		public final static String state_ing = "2";
		/** 已结束 */
		public final static String state_end = "3";
	}

	/** 抢红包活动奖品规则 */
	public static interface giftspackAwardsruleInfo {
		/** 实物奖：1 **/
		public final static String AWARDS_TYPE_1 = "1";
		/** 积分：2 **/
		public final static String AWARDS_TYPE_2 = "2";
		/** 抵用券：3 **/
		public final static String AWARDS_TYPE_3 = "3";
	}

	public static interface UserPointsType {
		public final static int RECEIPT_PROFIT = 1;
		public final static int RECEIPT_EXTRA = 2;
		public final static int NEWUSER_TASK = 3;
		public final static int RECEIPT_NAVYCODE = 4;
		public final static int QR_CODE = 5;
		public final static int CREDIT_CODE = 6;
		public final static int GIFTS_PACK = 7;
		public final static int USER_RECHARGE = 8;
		public final static int GOOD_KIDS = 9;

	}

	public static interface PushMessageType {
		public final static String RECEIPTS = "1";
	}

	public static interface PushMessageCommand {
		public final static String RECEIPTS = "ptpPushMessage";
	}

	public static interface BOOLEAN_TYPE {

		/** 表示true *. */
		public static final String TRUE_ = "1";

		/** 表示false *. */
		public static final String FALSE_ = "0";
	}

	/**
	 * 附件上传类型
	 * 
	 * @author gucp
	 * @date 20140707
	 */
	public static interface ATTACH_UPLOAD_TYPE {
		/** 头像、图片:1 */
		public static int IMG_TYPE = 1;
		/** 小票:2 */
		public static int RECEIPT_TYPE = 2;
		/** 微信小票图片路径：3 */
		public static int WCT_PATH_TYPE = 3;
		/** 微信头像上传：4 **/
		public static int WCT_HEAD_TYPE = 4;
	}

	/**
	 * 首页活动
	 * 
	 * @author
	 * @createTime
	 * @description
	 */
	public static interface HomepageModule {
		public static String activity_1 = "MODULE_SLATE_BLOCK_1";
		public static String activity_2 = "MODULE_SLATE_BLOCK_2";
		public static String activity_3 = "MODULE_SLATE_BLOCK_3";
		public static String activity_4 = "MODULE_SLATE_BLOCK_4";
		public static String activity_5 = "MODULE_SLATE_BLOCK_5";
	}

	/**
	 * 用户类型
	 * 
	 * @author
	 * @date
	 */
	public static interface UserType {
		public static String USER_TYPE_1 = "1";
		public static String USER_TYPE_2 = "2";
	}

	/**
	 * 特殊符号
	 * 
	 * @author RoyFu
	 * @createTime 下午1:46:53
	 * @description
	 */
	public static interface SpecialSymbol {
		public static String SP_ACCOUNTS = "#_account_#";
	}

	public static enum DoubtType {
		DOUBT_USER("41", "V码可疑用户"), ;

		private String id;
		private String name;

		private DoubtType(String id, String name) {
			this.id = id;
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	/** : */
	public final static String EXTRACT_SPLIT_MH = ":";
	
	/** @ */
	public final static String EXTRACT_SPLIT_AT = "@";

	/** @@@@ */
	public final static String EXTRACT_SPLIT = "@@@@";

	/** # */
	public final static String EXTRACT_SPLIT_CHART = "#";

	/** | */
	public final static String EXTRACT_SPLIT_VERTICAL = "|";

	/** && */
	public final static String EXTRACT_SPLIT_CT = "&&";

	/** 换行 */
	public final static String EXTRACT_STR = "\r\n";

	/** testInfo */
	public final static String EXTRACT_TEST_INFO = "testInfo";

	
	/**
	 * 黑名单类别
	 * 
	 * @author
	 * @date
	 */
	public static interface blackListType {
		/** 用户key 1*/
		public static String BLACK_LIST_USERKEY = "1";
	}
    
    /**
     * 用户状态 0正常， 1可疑，2黑名单
     * @auther hanshimeng </br>
     * @version V1.0.0 </br>      
     * @createTime 2017年4月10日 </br>
     */
    public static interface userStatus{
        /** 正常用户 **/
        static String USERTYPE_0 = "0";
        /** 可疑用户 **/
        static String USERTYPE_1 = "1";
        /** 黑名单 **/
        static String USERTYPE_2 = "2";
        /** 校验手机号 **/
        static String USERTYPE_3 = "3";
        /** 白名单用户 **/
        static String USERTYPE_4 = "4";
    }
	
	/**
	 * 是否黑名单
	 * @author
	 * @date
	 */
	public static interface blackWhiteList {
		/** 黑名单 1*/
		public static String BLACK_LIST_BLACK = "1";
		/** 白名单 2*/
		public static String BLACK_LIST_WHITE = "2";
	}

	/**
	 * 二维码批量信息
	 */
	public static interface Vcode_qrcode_batchInfo {
		/** 二维码记录条数数量上限，大于此数，则需要在V码活动与码库表中，产生多条记录 **/
		static final int QRCODE_NUM_LIMIT = 4000000;
		/** 码库名前缀**/
		static final String LIB_NAME_PREFIX = "vps_qrcode_";
	}
    
    /**
     * 活动返利类型
     * @auther hanshimeng </br>
     * @version V1.0.0 </br>      
     * @createTime 2017年11月29日 </br>
     */
    public static interface ActivityRebateType{
        /** 首扫返利：金额 **/
        static final String firstRebateType_0 = "0";
        /** 首扫返利：积分 **/
        static final String firstRebateType_1 = "1";
        /** 可疑返利：金额 **/
        static final String doubtRebateType_0 = "0";
        /** 可疑返利：积分 **/
        static final String doubtRebateType_1 = "1";
    }
    
    /**
     * 批次状态类型
     */
    public static interface QrcodeBatchType {
        
        /** 0：新创建批次 */
        public final static String type_0 = "0";
        
        /**  1：码源订单下批次 */
        public final static String type_1 = "1";
        
        /**  2：回传入库批次 */
        public final static String type_2 = "2";
        
        /**  3：活动下的批次 */
        public final static String type_3 = "3";
        
        /**  4：箱码激活查询 */
        public final static String type_4 = "3";
        
        /**  5：已使用批次查询 */
        public final static String type_5 = "5";
        
        /**  6：扫码活动下已挂接批次 */
        public final static String type_6 = "6";
    }
    
    /**
     * 扫码类型
     */
    public static interface ScanType {
        
        /** 0：首扫 */
        public final static String type_0 = "0";
        
        /**  1：普扫 */
        public final static String type_1 = "1";
    }
    
    /**
     * 奖项配置类型
     */
    public static interface PrizeCogType {
        
        /** 0：Excel */
        public final static String type_0 = "0";
        
        /** 1：规则模板 */
        public final static String type_1 = "1";
    }
    
    /**
     * 奖项随机类型
     */
    public static interface PrizeRandomType {
        
        /** 0：随机 */
        public final static String type_0 = "0";

        /** 1：固定 */
        public final static String type_1 = "1";
    }
    
    /**
     * 大奖中奖渠道
     */
    public static interface exchangeChannel {
        /** 1商城兑换 */
        static String CHANNEL_1 = "1";
        /** 2商城抽奖 */
        static String CHANNEL_2 = "2";
        /** 3扫码中奖 */
        static String CHANNEL_3 = "3";
        /** 4一码双奖 */
        static String CHANNEL_4 = "4";
        /** 5逢尾数 */
        static String CHANNEL_5 = "5";
        /** 6河北原价商城 */
        static String CHANNEL_6 = "6";
        /** 7集卡兑换 */
//        static String CHANNEL_7 = "7";
        /** 8 商城拼团兑换 **/
        static String CHANNEL_8 = "8";
        /** 9竞价赢奖 */
        static String CHANNEL_9= "9";
        /** 10转盘抽奖 */
        static String CHANNEL_10= "10";
		/** 11积盖兑换 */
		static String CHANNEL_11= "11";
		/** 12二重惊喜 */
		static String CHANNEL_12= "12";
		/** 13海报中奖 */
		static String CHANNEL_13= "13";
		/** 14直接发放激励 */
		static String CHANNEL_14= "14";

    }
    
    /**
     * 公共红包中奖渠道：1扫码中奖；
     * @auther hanshimeng </br>
     * @version V1.0.0 </br>      
     * @createTime 2017年11月24日 </br>
     */
    public static interface commonPacketChannel{
    	/** 1 集卡兑换**/
    	static String CHANNEL_1 = "1";
    	/** 2 商城兑换 **/
    	static String CHANNEL_2 = "2";
    	/** 3 商城抽奖 **/
    	static String CHANNEL_3 = "3";
    	/** 4 锦鲤红包**/
    	static String CHANNEL_4 = "4";
		/** 4 专项配送激励**/
		static String CHANNEL_8 = "8";
    }
    
    /**
     * 积分商城兑换类型：1实物、2电子券、3流量、4积分、5现金、6谢谢惠顾
     */
    public static interface exchangeType {
        /** 1实物 */
        static String TYPE_1 = "1";
        /** 2电子券 */
        static String TYPE_2 = "2";
        /** 3流量 */
        static String TYPE_3 = "3";
        /** 4积分 */
        static String TYPE_4 = "4";
        /** 5现金 */
        static String TYPE_5 = "5";
        /** 6谢谢惠顾 */
        static String TYPE_6 = "6";
        static String TYPE_8 = "8";
    }
    
    /**
     * 兑换状态：-1=待支付、0=兑换成功、1=兑换失败、2=兑换中、3=订单已关闭、4=退款申请、 5=撤单
     * @author hanshimeng
     *
     */
    public static interface exchangeStatus{
    	/** -1待支付 */
    	static String STATUS_MINUS_ONE = "-1";
    	/** 0兑换成功 */
    	static String STATUS_0 = "0";
        /** 1兑换失败 */
        static String STATUS_1 = "1";
        /** 2兑换中*/
        static String STATUS_2 = "2";
        /** 3订单已关闭*/
        static String STATUS_3 = "3";
        /** 4微信退款申请*/
        static String STATUS_4 = "4";
        /** 5未发货已撤单*/
        static String STATUS_5 = "5";
        /** 6微信退款失败*/
        static String STATUS_6 = "6";
        /** 7退货审核中*/
        static String STATUS_7 = "7";
        /** 8退货处理中*/
        static String STATUS_8 = "8";
        /** 9退货已完成*/
        static String STATUS_9 = "9";
        /** 10=待提交退货物流*/
        static String STATUS_10 = "10";
    }
    
    /**
     * 物流状态：0未发货、1已发货、2已完成
     */
    public static interface expressStatus {
        /** 0未发货 */
        static String STATUS_0 = "0";
        /** 1已发货 */
        static String STATUS_1 = "1";
        /** 2已完成 */
        static String STATUS_2 = "2";
        /** 3已撤单 */
        static String STATUS_3 = "3";
    }
    
    /**
     * 核销状态：0待核销、1已核销、2已终止核销
     */
    public static interface verificationStatus {
        /** 0待核销 */
        static String STATUS_0 = "0";
        /** 1已核销 */
        static String STATUS_1 = "1";
        /** 2已终止核销 */
        static String STATUS_2 = "2";
    }
    
    /**
     *  一等奖定投奖品类型
     */
    public static interface GrandPrizeType{
        /** 一等奖-游轮 **/
        static String GRANDPRIZETYPE_0 = "0";
        /** 二等奖**/
        static String GRANDPRIZETYPE_1 = "1";
        /** 三等奖**/
        static String GRANDPRIZETYPE_2 = "2";
    }

    /**
     * 奖品类型
     * @auther hanshimeng </br>
     * @version V1.0.0 </br>      
     * @createTime 2016年12月20日 </br>
     */
    public static interface PrizeType{
        
        /**  现金红包*. */
        public final static String status_0 = "0";
        /**  积分红包*. */
        public final static String status_1 = "1";
        /**  积分现金红包*. */
        public final static String status_2 = "2";
        /**  联通大礼包*. */
        public final static String status_4 = "4";
        
        /**  游轮  */
        public final static String status_5 = "5";
        
        /**  二等奖 */
        public final static String status_6 = "6";
        
        /**  三等奖 */
        public final static String status_7 = "7";
		/**  谢谢参与 */
		public final static String status_8 = "8";
        /** P奖- **/
        public final static String status_P = "P";
        /** Q奖 **/
        public final static String status_Q = "Q";
        /** R奖 **/
        public final static String status_R = "R";
        /** S奖 **/
        public final static String status_S = "S";
        /** T奖 **/
        public final static String status_T = "T";
        /** U奖 **/
        public final static String status_U = "U";
        /** V奖 **/
        public final static String status_V = "V";
        /** W奖 **/
        public final static String status_W = "W";
        /** X奖 **/
        public final static String status_X = "X";
        /** Y奖 **/
        public final static String status_Y = "Y";
        /** Z奖 **/
        public final static String status_Z = "Z";
        /** P01奖 **/
        public final static String status_P01 = "P01";
        /** P02奖 **/
        public final static String status_P02 = "P02";
        /** P03奖 **/
        public final static String status_P03 = "P03";
        /** P04奖 **/
        public final static String status_P04 = "P04";
        /** P05奖 **/
        public final static String status_P05 = "P05";
        /** P06奖 **/
        public final static String status_P06 = "P06";
        /** P07奖 **/
        public final static String status_P07 = "P07";
        /** P08奖 **/
        public final static String status_P08 = "P08";
        /** P09奖 **/
        public final static String status_P09 = "P09";
        /** P10奖 **/
        public final static String status_P10 = "P10";
        /** Z001001奖-尊享卡-金额 **/
        public final static String status_Z001001 = "Z001001";
        /** Z002001奖-尊享卡-折扣 **/
        public final static String status_Z002001 = "Z002001";
        
        /** H优惠券（京东券） **/
        public final static String status_H = "H";
        /** I优惠券 **/
        public final static String status_I = "I";
        /** J优惠券 **/
        public final static String status_J = "J";
        /** K优惠券 **/
        public final static String status_K = "K";
        /** L优惠券 **/
        public final static String status_L = "L";
        /** M优惠券 **/
        public final static String status_M = "M";
        /** N优惠券 **/
        public final static String status_N = "N";
        
        public final static String status_M01 = "M01";
        public final static String status_M02 = "M02";
        public final static String status_M03 = "M03";
        public final static String status_M04 = "M04";
        public final static String status_M05 = "M05";
        public final static String status_M06 = "M06";
        public final static String status_M07 = "M07";
        public final static String status_M08 = "M08";
        public final static String status_M09 = "M09";
        public final static String status_M10 = "M10";
        public final static String status_M11 = "M11";
        public final static String status_M12 = "M12";
        
        /**享乐奖*/
        public final static String status_LX01 = "LX01";
        /**享乐奖*/
        public final static String status_LX02 = "LX02";
        /**享乐奖*/
        public final static String status_LX03 = "LX03";
        /**享乐奖*/
        public final static String status_LX04 = "LX04";
        /**享乐奖*/
        public final static String status_LX05 = "LX05";
    }
    
    /**
     * 奖品类型Map
     * @auther hanshimeng </br>
     * @version V1.0.0 </br>      
     * @createTime 2016年12月20日 </br>
     */
    @SuppressWarnings("serial")
    public static Map<String,String> prizeTypeMap = new HashMap<String, String>(){{
        put("现金红包", PrizeType.status_0);
        put("积分红包", PrizeType.status_1);
        put("积分现金红包", PrizeType.status_2);
        put("联通大礼包", PrizeType.status_4);
        put("一等奖",   PrizeType.status_5); 
        put("二等奖",   PrizeType.status_6); 
        put("三等奖",   PrizeType.status_7); 
        put("P等奖",   PrizeType.status_P); 
        put("Q等奖",   PrizeType.status_Q); 
        put("R等奖",   PrizeType.status_R); 
        put("S等奖",   PrizeType.status_S); 
        put("T等奖",   PrizeType.status_T);
        put("U等奖",   PrizeType.status_U);
        put("V等奖",   PrizeType.status_V);
        put("W等奖",   PrizeType.status_W);
        put("X等奖",   PrizeType.status_X);
        put("Y等奖",   PrizeType.status_Y);
        put("Z等奖",   PrizeType.status_Z);
        put("P01等奖",   PrizeType.status_P01);
        put("P02等奖",   PrizeType.status_P02);
        put("P03等奖",   PrizeType.status_P03);
        put("P04等奖",   PrizeType.status_P04);
        put("P05等奖",   PrizeType.status_P05);
        put("P06等奖",   PrizeType.status_P06);
        put("P07等奖",   PrizeType.status_P07);
        put("P08等奖",   PrizeType.status_P08);
        put("P09等奖",   PrizeType.status_P09);
        put("P10等奖",   PrizeType.status_P10);
        put("Z001001等奖",   PrizeType.status_Z001001);
        put("Z002001等奖",   PrizeType.status_Z002001);
        put("H优惠券",   PrizeType.status_H);
        put("I优惠券",   PrizeType.status_I);
        put("J优惠券",   PrizeType.status_J);
        put("K优惠券",   PrizeType.status_K);
        put("L优惠券",   PrizeType.status_L);
        put("M优惠券",   PrizeType.status_M);
        put("N优惠券",   PrizeType.status_N);
        
        put("M00",   PrizeType.status_0);
        put("M01",   PrizeType.status_M01);
        put("M02",   PrizeType.status_M02);
        put("M03",   PrizeType.status_M03);
        put("M04",   PrizeType.status_M04);
        put("M05",   PrizeType.status_M05);
        put("M06",   PrizeType.status_M06);
        put("M07",   PrizeType.status_M07);
        put("M08",   PrizeType.status_M08);
        put("M09",   PrizeType.status_M09);
        put("M10",   PrizeType.status_M10);
        put("M11",   PrizeType.status_M11);
        put("M12",   PrizeType.status_M12);
        
        put("LX01等奖",   PrizeType.status_LX01);
        put("LX02等奖",   PrizeType.status_LX02);
        put("LX03等奖",   PrizeType.status_LX03);
        put("LX04等奖",   PrizeType.status_LX04);
        put("LX05等奖",   PrizeType.status_LX05);
    }};
    
    /**
     * 周期类型:0周、1月、2天、3活动周期
     */
    public static interface periodType {
        /** 0周**/
        static String PERIOD_TYPE0 = "0";
        /** 1月**/
        static String PERIOD_TYPE1 = "1";
        /** 2天**/
        static String PERIOD_TYPE2 = "2";
        /** 3活动周期**/
        static String PERIOD_TYPE3 = "3";
    }
    
    /**
     * 签到活动SKU限制关系：0并且、1或者、3混合
     */
    public static interface signSkuRelationType {
        /** 并且**/
        static String RELATION_TYPE0 = "0";
        /** 或者**/
        static String RELATION_TYPE1 = "1";
        /** 包含**/
        static String RELATION_TYPE2 = "2";
    }
    
    /**
     * 签到类型:0天、1次
     */
    public static interface signType {
        /** 0天**/
        static String SIGN_TYPE0 = "0";
        /** 1次**/
        static String SIGN_TYPE1 = "1";
    }
    
    /**
     * 批次状态：0新建、1创建码源订单、2已回传、3已挂接活动
     */
    public static interface batchStatus {
        /** 0新建**/
        static String status0 = "0";
        /** 1创建码源订单**/
        static String status1 = "1";
        /** 2导入中**/
        static String status2 = "2";
        /** 3已回传**/
        static String status3 = "3";
        /** 3已挂接活动**/
        static String status4 = "4";
    }
    
    /**
     * 黑名单原因
     * @auther hanshimeng </br>
     * @version V1.0.0 </br>      
     * @createTime 2017年7月7日 </br>
     */
    public static interface BlackUserFlag{
    	/** 系统默认用户 **/
    	static String ONE = "1";
    	/** 防黑客盗扫用户 **/
    	static String TWO = "2";
    	/** 防工厂盗扫用户 **/
    	static String THREE = "3";
    	/** 可疑自动加入黑名单JOB **/
    	static String FOUR = "4";
    }
    
    /**
     * 可疑规则类型 1 系数，2 区间（金额或积分）
     * @auther hanshimeng </br>
     * @version V1.0.0 </br>      
     * @createTime 2017年12月8日 </br>
     */
    public static interface doubtRuleType{
        /** 系数 **/
        static String RULE_TYPE_1 = "1";
        /** 区间（金额或积分） **/
        static String RULE_TYPE_2 = "2";
    }

    /**
     * 疑似黑名单可疑原因.
     *
     * @author jiquanwei
     */
    public static interface DoubtReasonType{
        
        /**  同一分钟扫码次数大于等于X次*. */
        static String DOUBTRESON_ONE = "1";
        
        /**  同一天扫码次数大于等于X次*. */
        static String DOUBTRESON_TWO = "2";
        
        /**  历史累计扫码次数大于等于X次*. */
        static String DOUBTRESON_THREE = "3";
        
        /**  手动添加入可疑 */
        static String DOUBTRESON_FOUR = "4";
        
        /** 其他**/
        static String DOUBTRESON_EIGHT = "8";
    }
    
    /**
     * 二维码导入批次状态
     * @auther hanshimeng </br>
     * @version V1.0.0 </br>      
     * @createTime 2017年7月31日 </br>
     */
    public static interface ImportFlag{
    	/** 未导入 **/
    	static String ImportFlag_0 = "0";
    	/** 已导入 **/
    	static String ImportFlag_1 = "1";
    }
    
    /**
     * 幸运用户级别
     * @auther hanshimeng </br>
     * @version V1.0.0 </br>      
     * @createTime 2017年02月23日 </br>
     */
    public static interface luckLevel{
        /** 非幸运用户  */
        static String LEVEL0 = "0";
        /** 幸运用户  */
        static String LEVEL1 = "1";
        /** 餐饮服务员  */
        static String LEVEL2 = "2";
        /** 扫码专员  */
        static String LEVEL3 = "3";
        /** 回收站用户 */
        static String LEVEL4 = "4";
        /** 非黑名单用户 */
        static String LEVEL5 = "5";
    }
    
    /**
	 * 活动返利规则类型
	 * @auther hanshimeng </br>
	 * @version V1.0.0 </br>      
	 * @createTime 2016年12月7日 </br>
	 */
	public static interface rebateRuleType{
		/** 节假日 **/
		static String RULE_TYPE_1 = "1";
		/** 时间段 **/
		static String RULE_TYPE_2 = "2";
		/** 周几 **/
		static String RULE_TYPE_3 = "3";
		/** 每天 **/
		static String RULE_TYPE_4 = "4";
		/** 自然周签到红包（已废弃请勿使用） **/
		static String RULE_TYPE_5 = "5";
	}
	
	@SuppressWarnings("serial")
    public static Map<String, String> serverNameMap = new HashMap<String, String>(){{
    	put("henan","henan");
    	put("huanan","huanan");
    	put("hebei","hebei");
    	put("guangxi","guangxi");
    	put("zhejiang","zhejiang");
    	put("hunan","hunan");
    	put("heilj","hlj");
    	put("qpec","ds");
    	put("sichuan","sc");
    	put("shanxi","sx");
    	put("shandongagt","sd");
    	put("XinYM","ym");
    	put("beixiao","bj");
    	put("fujian","fujian");
    	put("BYinMai","am");
    	put("yunnan","yunnan");
    	put("jiangxi","jiangxi");
    	put("xianqp","xian");
    	put("qmbaipi","qmbp");
    	put("shanxifj","fenjiu");
    }};
    
    /**
     * 活动菜单类型
     * @auther hanshimeng </br>
     * @version V1.0.0 </br>      
     * @createTime 2018年4月11日 </br>
     */
    public static interface activityMenuType{
    	static String MENU_TYPE_ACTIVITY = "activity";
    	static String MENU_TYPE_RULE = "rule";
    	static String MENU_TYPE_QRCODE = "qrcode";
    }
    
    /**
	 * 激活人员权限 0 无权限，1 激活，2 查询检测， 3 激活+查询检测
	 * @auther hanshimeng </br>
	 * @version V1.0.0 </br>      
	 * @createTime 2018年4月13日 </br>
	 */
	public static interface ACTIVATE_USER_PRIVILEGE{
		/** 无权限 **/
		public static String USER_PRIVILEGE_0 = "0";
		/** 激活、查询检测 **/
		public static String USER_PRIVILEGE_1 = "1";
		/** 查询检测 **/
		public static String USER_PRIVILEGE_2 = "2";
	}
	
	/**
	 * 用户状态：0 停用，1 待审核，2 已审核，3 驳回
	 * @auther hanshimeng </br>
	 * @version V1.0.0 </br>      
	 * @createTime 2018年4月13日 </br>
	 */
	public static interface ACTIVATE_USER_STATUS{
		/** 0 停用 **/
		public static String USER_STATUS_0 = "0";
		/** 1 待审核 **/
		public static String USER_STATUS_1 = "1";
		/** 2 已审核 **/
		public static String USER_STATUS_2 = "2";
		/** 3 驳回 **/
		public static String USER_STATUS_3 = "3";
	}
	
	/**
	 * 审核通用状态：0 默认，1通过，2 未通过，3 驳回
	 * @auther hanshimeng </br>
	 * @version V1.0.0 </br>      
	 * @createTime 2018年4月13日 </br>
	 */
	public static interface COMMON_CHECK_STATUS{
		/** 0 默认 **/
		public static String CHECK_STATUS_0 = "0";
		/** 1 通过 **/
		public static String CHECK_STATUS_1 = "1";
		/** 2 未通过 **/
		public static String CHECK_STATUS_2 = "2";
		/** 3 驳回 **/
		public static String CHECK_STATUS_3 = "3";
	}
	
	/**
	 * 微信创建二维码类型
	 * @auther hanshimeng </br>
	 * @version V1.0.0 </br>      
	 * @createTime 2018年4月19日 </br>
	 */
	public static interface CREATE_QRCODE_ACTION_NAME{
		/** 临时的整型参数值 **/
		public static String QR_SCENE = "QR_SCENE";
		/** 临时的字符串参数值 **/
		public static String QR_STR_SCENE = "QR_STR_SCENE";
		/** 永久的整型参数值 **/
		public static String QR_LIMIT_SCENE = "QR_LIMIT_SCENE";
		/** 永久的字符串参数值 **/
		public static String QR_LIMIT_STR_SCENE = "QR_LIMIT_STR_SCENE";
	}
	
	/**
	 * 操作日志类型
	 * @author hanshimeng
	 *
	 */
	public static interface OPERATION_LOG_TYPE{
		/** 0登录 **/
		public static String TYPE_0 = "0";
		/** 1新增 **/
		public static String TYPE_1 = "1";
		/** 2修改 **/
		public static String TYPE_2 = "2";
		/** 3删除 **/
		public static String TYPE_3 = "3";
	}
	
	/**
	 * 日志菜单名称
	 */
	@SuppressWarnings("serial")
	public static Map<String, String> menuMap = new HashMap<String, String>(){{
		put("login", "登录");
		put("user", "幸运用户");
		put("skuInfo", "产品设置");
		put("shopping", "商城管理");
		put("promotion", "捆绑升级");
		put("checkUser", "核销管理");
		put("sysDataDic", "数据字典");
		put("firstPrize", "中奖用户");
		put("blacklist", "黑名单用户");
		put("vcodeSignin", "万能签到");
		put("doubleprize", "一码双奖");
		put("vcodeActivity", "活动管理");
		put("ladderRuleCog", "阶梯规则");
		put("vcodeDoubtUser", "可疑用户");
		put("qrcodeBatchInfo", "码源管理");
		put("batchActivate", "激活人员管理");
		put("vpsQrcodeOrder", "码源订单管理");
		put("companyRecharge", "企业充值管理");
        put("rebateRuleTemplet", "活动规则模板");
		put("vcodeActivityHotArea", "超级热区");
		put("vcodeTicketActivity", "优惠券活动");
		put("vcodeActivityRebateRule", "规则管理");
		put("vcodeActivityMorescanCog", "一码多扫");
		put("vcodeActivityPerhundredCog", "逢百规则");
		put("taoEasterEggCog", "淘彩蛋规则");
		put("couponCog", "商城优惠券");
		put("vcodePrizeBasicInfo", "大奖模块化");
	}};
	
	/**
	 * 业务表名称
	 */
	@SuppressWarnings("serial")
	public static Map<String, String> tableMap = new HashMap<String, String>(){{
		put("login", "登录table");
		put("user", "幸运用户table");
		put("shopping", "商城管理table");
		put("skuInfo", "vps_sku_info");
		put("spuInfo", "vps_spu_info");
		put("sysDataDic", "数据字典table");
		put("blacklist", "黑名单用户table");
		put("vcodeDoubtUser", "可疑用户table");
		put("qrcodeBatchInfo", "码源管理table");
		put("batchActivate", "激活人员管理table");
		put("companyRecharge", "企业充值管理table");
		put("vcodeSignin", "vps_vcode_signin_cog");
		put("ladderRuleCog", "vps_ladder_rule_cog");
		put("vcodeActivityRebateRule", "规则管理table");
		put("vcodeActivity", "vps_vcode_activity_cog");
		put("vpsQrcodeOrder", "vps_qrcode_order_info");
		put("vcodeTicketInfo", "vps_vcode_ticket_info");
		put("doubleprize", "vps_vcode_doubleprize_cog");
		put("biddingActivity", "vps_bidding_activity_cog");
		put("promotion", "vps_vcode_bindpromotion_cog");
		put("sendTemplateMsg", "send_template_message");
		put("doubtTemplet", "vps_vcode_activity_doubt_templet");
		put("vpointsVerification", "vpoints_verification_info");
		put("vcodeTicketActivity", "vps_vcode_ticket_activity_cog");
		put("vcodeActivityHotArea", "vps_vcode_activity_hotarea_cog");
		put("vcodeActivityMorescanCog", "vps_vcode_activity_morescan_cog");
		put("rebateRuleTemplet", "vps_vcode_activity_rebate_rule_templet");
		put("vcodeActivityPerhundredCog", "vps_vcode_activity_perhundred_cog");
		put("vpsVcodePerMantissaCog", "vps_vcode_permantissa_cog");
		put("vpsVcodeTaoEasterEggCog", "vps_vcode_tao_easteregg_cog");
		put("vpsVcodeTerminal", "vps_terminal_info");
		put("seckillActivityCog", "vpoints_seckill_activity");
		put("groupBuyingActivityCog", "vpoints_group_buying_activity");
		put("couponCog", "vpoints_coupon_cog");
		put("vpsTicketRecord", "vps_ticket_record");
		put("fission", "vps_vcode_activate_red_envelope_rule_cog");
		put("VpsCombinationDiscountCog", "vps_combination_discount_cog");
		put("vpsVcodeBenedictionExpandRule", "vps_vcode_benediction_expand_rule");
		put("halfPrice", "vpoints_goods_half_price_activity_cog");
		put("exchageActivity", "vpoints_goods_exchange_activity_cog");
		put("giftCard", "vpoints_goods_gift_card_cog");
		put("giftCardQrcodeOrder", "vps_gift_card_qrcode_order_info");
		put("vps_vcode_batch_serial_qr_record", "vps_vcode_batch_serial_qr_record");
		put("vps_vcode_vdh_invitation_activity_cog", "vps_vcode_vdh_invitation_activity_cog");
        put("vps_vcode_activity_frequency_cog", "vps_vcode_activity_frequency_cog");
		put("vps_vcode_invitation_activity_cog", "vps_vcode_invitation_activity_cog");
	}};
    
    /**
     * 订单类型
     */
    public static interface OrderNoType {
		/** VI:V店惠邀请有礼 */
		public final static String type_VI = "VI";

    	/** JJ：竞价活动 */
        public final static String type_JJ = "JJ";
        
        /** MY：码源订单 */
        public final static String type_MY = "MY";
        
        /** HD：扫码活动 */
        public final static String type_HD = "HD";
        
        /** DS：一码多扫 */
        public final static String type_DS = "DS";
        
        /** FB：逢百规则 */
        public final static String type_FB = "FB";
        
        /** FW：逢尾数规则 */
        public final static String type_FW = "FW";
        
        /** SJ：一码双奖 */
        public final static String type_SJ = "SJ";
        
        /** KB：捆绑升级 */
        public final static String type_KB = "KB";
        
        /** QD：万能签到 */
        public final static String type_QD = "QD";
        
        /** HX：核销记录 */
        public final static String type_HX = "HX";
        
        /** TK：优惠券 */
        public final static String type_TK = "TK";
        
        /** XX：模板消息推送 */
        public final static String type_XX = "XX";
        
        /** JT：阶梯 */
        public final static String type_JT = "JT";
        
        /** CD：彩蛋 */
        public final static String type_CD = "CD";
        
        /** MD：门店 */
        public final static String type_MD = "MD";
        
        /** YHQ：商城优惠券 */
        public final static String type_YHQ = "YHQ";
        
        /** MS：商城秒杀活动 */
        public final static String type_MS = "MS";
        
        /** PT：商城拼团活动 */
        public final static String type_PT = "PT";

        /**JH：激活红包 */
        public final static String type_JH = "JH";
        
        /** ZHYH：组合优惠 */
        public final static String type_ZHYH = "ZHYH";

        /** PZ：膨胀规则 */
        public final static String type_PZ = "PZ";
		/** BJHD：半价活动 */
		public final static String type_BJHD = "BJHD";
		/** BJHD：换购活动 */
		public final static String type_HGHD = "HGHD";
		/** GF:礼品卡 */
		public final static String type_GF = "GF";
        /** PC:频次 */
        public final static String type_PC = "PC";
		/** IV:邀请有礼 */
		public final static String type_IV = "IV";
    }
	
	/**
	 * 二维码订单状态
	 * @author hanshimeng
	 *
	 */
	public static interface QRCODE_ORDER_STATUS{
		/** 订单起始状态 0**/
		public static String order_status_0 = "0";
		/** 生成成功 1**/
		public static String order_status_1 = "1";
		/** 生成失败 2**/
		public static String order_status_2 = "2";
		/** 进行中 3**/
		public static String order_status_3 = "3";
		/** 发送邮件**/
		public static String order_status_4 = "4";
		/** 发送密码**/
		public static String order_status_5 = "5";
	}
	
	/**
	 * 码源格式：1.无串码, 2.有串码,串码之间有逗号 3.有串码,串码之间无逗号 4.有串码,串码之间有空格
	 * @author hanshimeng
	 *
	 */
	public static interface QRCODE_FORMAT{
		/** 1.二维码【无串码(拉环9+56)】 **/
		public static String format_1 = "1";
		/** 2.二维码,串前6,串后6【串码之间有逗号（广西8+31，其他9+31）】 **/
		public static String format_2 = "2";
		/** 3.二维码,串12【有串码,串码之间无逗号（9+31）】 **/
		public static String format_3 = "3";
		/** 4.二维码 串前6 串后6【有串码,串码之间有空格（9+31）】 **/
		public static String format_4 = "4";
		/** 5.二维码,,串前6,串后6【有串码,串码之间有逗号,内容和串码之间两个逗号（9+31）】 **/
		public static String format_5 = "5";
		/** 6.二维码 串12 **/
		public static String format_6 = "6";
		
		/** 7. 二维码,串前6,串后6,序号**/
		public static String format_7 = "7";
		/** 8. 二维码,串12,序号(主推)**/
		public static String format_8 = "8";
		/** 9. 二维码 串12 序号(主推)**/
		public static String format_9 = "9";
		/** 10. 二维码 串前6 串后6 序号**/
		public static String format_10 = "10";
		/** 11. 序号 二维码**/
		public static String format_11 = "11";
		/** 12. 序号 二维码,串前6,串后6**/
		public static String format_12 = "12";
		/** 13. 序号 二维码,串12**/
		public static String format_13 = "13";
		/** 14. 序号 二维码 串前6 串后6**/
		public static String format_14 = "14";
		/** 15. 序号,二维码,串12**/
		public static String format_15 = "15";
        /** 16. 批次序号,二维码**/
        public static String format_16 = "16";
        /** 17. 序号,二维码,串前6,串后6**/
        public static String format_17 = "17";
        /** 18. 二维码,,串码,串前6,串后6**/
        public static String format_18 = "18";
        /** 19. 二维码,串码,串前6,串后6**/
        public static String format_19 = "19";
        /** 20. 二维码 奖项描述**/
        public static String format_20 = "20";
        /** 21. 二维码,奖项描述**/
        public static String format_21 = "21";
        /** 22. 二维码;奖项描述**/
        public static String format_22 = "22";
	}

	/**
	 * 消息类型：0规则到期、1规则剩余钱数小于10%、2规则剩余红包个数小于10%、3批次即将结束
	 * @author hanshimeng
	 *
	 */
	public static interface MSG_Type{
		/** 0.规则到期 **/
		public static String remind_0 = "0";
		/** 1.剩余钱数**/
		public static String remind_1 = "1";
		/** 2.剩余积分 **/
		public static String remind_2 = "2";
		/** 3.红包个数**/
		public static String remind_3 = "3";
		/** 4.批次结束**/
		public static String remind_4 = "4";
		/** 5.阈值，红包金额和积分金额 **/
		public static double threshold=0.1;
	}

	public static interface ACTIVITY_TYPE{
		/** 0.扫码活动 **/
		public static String activityType_0 = "0";
		/** 1.万能签到活动 **/
		public static String activityType_4 = "4";
		/** 2.捆绑升级活动 **/
		public static String activityType_5 = "5";
		/** 3.一码双奖 **/
		public static String activityType_6 = "6";

	}
	
	/**
	 * 券类型：0链接，1券码
	 * @author hanshimeng
	 *
	 */
	public static interface TICKET_TYPE{
		/** 0.链接 **/
		public static String ticketType_0 = "0";
		/** 1.券码 **/
		public static String ticketType_1 = "1";
		/** 2.识别图片 **/
		public static String ticketType_2 = "2";
		/** 3.动态券码 **/
		public static String ticketType_3 = "3";
		/** 4.活动编码 **/
		public static String ticketType_4 = "4";
	}
	
	/**
	 * 获取地址
	 */
	public static String filePath=
			System.getProperty("os.name").startsWith("Linux")
			?"/"+getClassStatic().getResource("/").toString().substring(6).replaceAll("WEB-INF/classes/", "")
			:getClassStatic().getResource("/").toString().substring(6).replaceAll("WEB-INF/classes/", "");
	private static final Class getClassStatic(){
		return new Object(){
			public Class getClassStatic(){
				return this.getClass();
			}
		}.getClassStatic();
	}
	
	/**
	 * 模板消息类型：1.服务通知，2.单品推送，3.特殊推送 
	 * @author hanshimeng
	 *
	 */
	public static interface TEMPLATE_TYPE{
		/** 1.服务通知 **/
		public static String type_1 = "1";
		/** 2.单品推送 **/
		public static String type_2 = "2";
		/** 3.特殊推送  **/
		public static String type_3 = "3";
	}
	
	/**
	 * 商城跳转URL类型：0无跳转，1跳转商城
	 * @author hanshimeng
	 *
	 */
	public static interface TEMPLATE_URL_TYPE{
		/** 0无跳转 **/
		public static String type_0 = "0";
		/** 1跳转商城 **/
		public static String type_1 = "1";
	} 
	
	/**
	 * 核销状态
	 * @author hanshimeng
	 *
	 */
	public static interface CHECK_STATUS{
		/** 未核销 **/
		public static String status_0 = "0";
		/** 已核销 **/
		public static String status_1 = "1";
	}
	
	/**
	 * 通用规则类型
	 * @author hanshimeng
	 *
	 */
	public static interface COMMON_RULE_TYPE{
		/** 1. 首扫规则 **/
		public static String status_1 = "1";
		/** 2. 任务基础规则 **/
		public static String status_2 = "2";
		/** 3. 签到任务规则 **/
		public static String status_3 = "3";
		/** 4. 分享任务规则 **/
		public static String status_4 = "4";
        /** 5. 通用配置规则 **/
        public static String status_5 = "5";
		/** 6. 天降红包规则 **/
		public static String status_6 = "6";
	}
	
	/**
     * 首扫类型：用户：USER，sku：SKU，活动：ACTIVITY，分组：GROUP
     */
    public static interface FirstSweepType {
        /** 按userKey区分*/
        static String TYPE_USER = "USER";
        /** 按skuKey区分*/
        static String TYPE_SKU = "SKU";
        /** 按activityKey区分*/
        static String TYPE_ACTIVITY = "ACTIVITY";
        /** 按group区分*/
        static String TYPE_GROUP = "GROUP";
    }
    
    /**
     * 小程序模板类型：1活动信息提醒，2比赛结果通知，3任务完成通知 ，4周赛提醒通知
     * @author hanshimeng
     *
     */
    public static interface APPLET_TEMPLATE_TYPE{
        /** 1活动信息提醒 **/
        public static String type_1 = "1";
        /** 2比赛结果通知 **/
        public static String type_2 = "2";
        /** 3任务完成通知 **/
        public static String type_3 = "3";
        /** 4周赛提醒通知 **/
        public static String type_4 = "4";
    }
    
    /**
     * 酒量1V1比赛状态：0进行中、1分出胜负、2平局、3流局
     */
    public static interface PkStatus{
        /** 0 进行中**/
        static final String status_0 = "0";
        /** 1分出胜负 **/
        static final String status_1 = "1";
        /** 2平局 **/
        static final String status_2 = "2";
        /** 3流局 **/
        static final String status_3 = "3";
    }
    
    /**
     * 阶梯规则：0无，1每天，2时间
     */
    public static interface ladderRuleFalg{
        /** 0 无 **/
        static final String flag_0 = "0";
        /** 1 每天 **/
        static final String flag_1 = "1";
        /** 2 时间段 **/
        static final String flag_2 = "2";
    }
    
    /**
	 * 推送消息类型：1公众号，2小程序
	 */
	public static interface sendMessageType {
		public final static String messageType_1 = "1"; // 公众号
		public final static String messageType_2 = "2"; // 小程序
	}
	/**
	 * 发布系统常量
	 */
	public static interface pubFalg{
		/**
		 * 弹窗redis标识
		 */
		public final String ADUP = "adUp";
		/**
		 * 首页轮播redis
		 */
		public final String ADHOME = "adHome";
		/**
		 * 商城轮播redis标识
		 */
		public final String ADSHOP = "adShop";
		/**
         * 活动专区redis标识
         */
        public final String ACTREGION = "actRegion";
		/**
		 * 活动规则redis标识
		 */
		public final String ACTRULE = "actRule:";
		/**
		 * 图片服务器图片前缀
		 */
		public final String PICPREFIX = "https://img.vjifen.com/images/vma/";
		/**
		 * http
		 */
		public final String HTTP = "http";
		/**
		 * 全部地区
		 */
		public  final String ALLLOCAL = "000000";
	}
	/**
	 * 发布系统素材类型
	 */
	public static interface picLibType{
		/**
		 * LOGO
		 */
		public final String LOGO = "1";
		/**
		 * SLOGAN
		 */
		public final String SLOGAN = "2";
		/**
		 * 背景图
		 */
		public final String BACK = "3";
		/**
		 * 红包
		 */
		public final String RED = "4";
		/**
		 *产品图
		 */
		public final String PRO = "5";
		/**
		 *提示图
		 */
		public final String TIP = "6";
		/**
		 * 按钮
		 */
		public final String BTN = "7";
		/**
		 * 扫码弹窗
		 */
		public final String ADUP = "8";
		/**
		 * 首页轮播
		 */
		public final String ADHOME = "9";
		/**
		 * 商城轮播
		 */
		public final String ADSHOP = "10";
		/**
		 * 活动规则
		 */
		public final String ACTRULE = "11";
		/**
		 * 图片链接
		 */
		public final String ADUPPIC = "12";
		/**
		 * 实物奖
		 */
		public final String PRIZEPIC = "13";
		/**
		 * 阶梯弹窗
		 */
		public final String LADDERUI = "14";
		/**
		 * 终端促销实物奖
		 */
		public final String VMTSPRIZEPIC = "15";
        /**
         * 活动专区
         */
        public final String ACTZONE = "16";
        /**
         *  N元乐购实物奖
         */
        public final String NYLGPRIZEPIC = "17";
        /**
         *  	公告图
         */
        public final String NOTICE = "18";
	}
	
	/**
	 * 小程序消息类型：1报名成功，2活动开赛，3周赛开赛，4同步步数，5每日活动达标，6周赛达标，7邀请好友，8关注公众号
	 * @author hanshimeng
	 *
	 */
	public static interface APPLET_MSG_TYPE{
		/** 1报名成功 **/
		public static String type_1 = "1";
		/** 2活动开赛 **/
		public static String type_2 = "2";
		/** 3周赛开赛 **/
		public static String type_3 = "3";
		/** 4同步步数 **/
		public static String type_4 = "4";
		/** 5每日活动达标 **/
		public static String type_5 = "5";
		/** 6周赛达标 **/
		public static String type_6 = "6";
		/** 7邀请好友 **/
		public static String type_7 = "7";
		/** 8关注公众号 **/
		public static String type_8 = "8";
	}
	
	/**
	 * PK状态：1获胜，2失败，3平局
	 * @author hanshimeng
	 *
	 */
	public static interface PK_STATUS{
		/** 1获胜 **/
		public static String status_1 = "1";
		/** 2失败 **/
		public static String status_2 = "2";
		/** 3平局 **/
		public static String status_3 = "3";
	}
	
	/**
	 * 翻倍卡类型：A：主动翻倍卡，B:被动翻倍卡
	 */
	@SuppressWarnings("serial")
	public static Map<String, String> allowanceTypeMap = new HashMap<String, String>(){{
		put("A:主动翻倍卡","A");
		put("B:被动翻倍卡","B");
	}};
	/**
	 * 积分KEY
	 */
	public static interface VPOINTSkEY {

		/** 普通 */
		public final static String commmonKey = "b89a122f-edd4-11ea-bd51-6e6d36e3ad65";

		/** 特殊 */
		public final static String specialKey = "c06eabd1-edd4-11ea-bd51-6e6d36e3ad65";
	}
	/**
	 * 实物奖key
	 */
	public static interface BIGPRIZEKEY {

		/** 实物奖key */
		public final  String BIGPRIZEKEY = "bigPrize";
	}
	
	
	/**
	 * 积分KEY
	 */
	public static interface VPOINTS_PAY_TYPE {
		
		/** 积分支付 */
		public final static String TYPE_0 = "0";
		/** 现金支付 */
		public final static String TYPE_1 = "1";
		/** 混合支付 */
		public final static String TYPE_2 = "2";
		/** 礼品卡兑换 */
		public final static String TYPE_3 = "3";
	}
	
	/**
	 * 获取微信aceessToken对应前端的serverName字段
	 */
	public final static Map<String, String> SERVERMAP = new HashMap<String, String>() {
		{
			put("wx7e48fbb1befc2f82","lnqp");//辽宁
			put("wx1d5e0ffc015303ab","hbqp");//河北
			put("wxc6b64f531c742368","hgqq");//皇冠曲奇
			put("wx014fb36aca70ec5d","zzqp");//河南瓶装
			put("wxd1511deac84a80bb","sdqp");//山东
			put("wx68b7621a49aac038","qmbp");//全麦白啤
//			put("wx3c7514cdd5b65f5e","zhongLCNY");//中粮长城春节CNY
			put("wxe6ad56b07630c34d","zhongLCNY");//中粮长城CNY
			put("wxdcbb213b99d36de7","quechao");//雀巢茶萃
			put("wx1848455ef1f030ab","changxianghui");//会员体系
			put("wx771bdde7a439afd6","MZ");//蒙牛支码
		}
	};
	
	/**
	 * 竞价擂台类型
	 * @author Administrator
	 *
	 */
	public static interface BIDDING_ACTIVITY_TYPE{
		/** 1.日擂台 **/
		public static final String TYPE_1 = "1";
		/** 2.月擂台 **/
		public static final String TYPE_2 = "2";
	}
	
	/**
	 * 订单评价类型
	 * @author Administrator
	 *
	 */
	public static interface EXCHANGE_COMMENT_TYPE{
		/** 1.商城评论 **/
		public static final String TYPE_1 = "1";
		/** 2.竞价晒单 **/
		public static final String TYPE_2 = "2";
	}
	
	/**
	 * 兑换标识：1普通兑换，2拼团兑换，3秒杀兑换
	 * @author hanshimeng
	 *
	 */
	public static interface EXCHANGE_FLAG{
	    /** 1普通兑换 **/
	    public static String flag_1 = "1";
	    /** 2拼团兑换 **/
	    public static String flag_2 = "2";
	    /** 3秒杀兑换 **/
	    public static String flag_3 = "3";
	}
	
	/**
	 * 人群限制类型
	 */
	public static interface crowdLimitType{
	    /** 0默认0不限制 **/
	    public static String type0 = "0";
	    /** 1黑名单不可参与 **/
	    public static String type1 = "1";
	    /** 2指定群组参与 **/
	    public static String type2 = "2";
		/** 3指定群组参不与 **/
		public static String type3 = "3";
	}

	/**
	 * 跑马灯相关
	 */
	public static interface marquee{
		/** 跑马灯配置redis **/
		public final String MARQUEECOGKEY = "marqueeCog";
		/** 中奖类型 0普通扫码中出 **/
		public final String TYPE_0 = "0";
	}
	/**
	 * 订单类型
	 */
	public static enum  ExchangeOrderType{
		orderType_0("0","常规订单"),
		orderType_1("1","秒杀订单"),
		orderType_2("2","拼团订单"),
		orderType_3("3","尊享卡类型"),
		orderType_4("4","礼品卡订单"),
		orderType_5("5", "组合优惠订单"),
		orderType_6("6", "折扣订单"),
		orderType_7("7", "换购订单");
		private String orderType;
		private String orderTypeName;
		ExchangeOrderType(String orderType,String orderTypeName){
			 this.orderType = orderType;
			 this.orderTypeName = orderTypeName;
		}

		public String getOrderType() {
			return orderType;
		}

		public String getOrderTypeName() {
			return orderTypeName;
		}

		public static String getOrderTypeName(String orderType) {
			ExchangeOrderType[] values = ExchangeOrderType.values();
			if (StringUtils.isEmpty(orderType)) {
				return "";
			}
			for (ExchangeOrderType value : values) {
				if(orderType.equals(value.getOrderType())){
					return value.getOrderTypeName();
				}
			}
			return "";
		}
	}
	/**
	 * 礼品卡类型
	 */
	public static enum  GiftCardType{
		giftCardType_0("0","普通卡"),
		giftCardType_2("1","充值卡"),
		giftCardType_1("2","兑付卡");
		private String giftCardType;
		private String giftCardTypeName;
		GiftCardType(String giftCardType,String giftCardTypeName){
			this.giftCardType = giftCardType;
			this.giftCardTypeName = giftCardTypeName;
		}

		public String getGiftCardType() {
			return giftCardType;
		}

		public String getGiftCardTypeName() {
			return giftCardTypeName;
		}
		public static String getGiftCardTypeName(String orderType) {
			GiftCardType[] values = GiftCardType.values();
			if (StringUtils.isEmpty(orderType)) {
				return "";
			}
			for (GiftCardType value : values) {
				if(orderType.equals(value.getGiftCardType())){
					return value.getGiftCardTypeName();
				}
			}
			return "";
		}
	}
    /**
     * 小票渠道类型
     */
    public static interface ticketChannel {
        /** 酒行渠道**/
        static String CHANNEL0 = "0";
        /** 餐饮渠道**/
        static String CHANNEL1 = "1";
        /** KA渠道**/
        static String CHANNEL2 = "2";
        /** 电商渠道**/
        static String CHANNEL3 = "3";
        /** 定时抽奖**/
        static String CHANNEL4 = "4";
        /** 促销激励**/
        static String CHANNEL5 = "5";
        /** 销售之星**/
        static String CHANNEL6 = "6";
        /** 二重惊喜**/
        static String CHANNEL7 = "7";
    }
    /**
     * FSA与消费对接用户角色
     */
    public static interface sfaUserRole {
        
        /** 0配送员 */
        public static final String role_0 = "0";
        /** 1终端老板 *. */
        public static final String role_1 = "1";
        /** 2经销商 *. */
        public static final String role_2 = "2";
        /** 3分销商 *. */
        public static final String role_3 = "3";
        /** 4网服 *. */
        public static final String role_4 = "4";
        /** 5管理 *. */
        public static final String role_5 = "5";
        /** 6巡查 *. */
        public static final String role_6 = "6";
    }

	/**
	 * 二维码业务类型
	 */
	public static interface qrcodeBusinessType {
		/** 箱码 */
		public static final String TYPE_X = "X";
		/** 外码 */
		public static final String TYPE_W = "W";
		/** 内码 */
		public static final String TYPE_N = "N";

		/** 邀请有礼活动 */
		public static final String TYPE_AIVT = "AIVT";
		/* V店惠邀请有礼*/
		public static final String TYPE_VINV = "VINV";
	}
}
