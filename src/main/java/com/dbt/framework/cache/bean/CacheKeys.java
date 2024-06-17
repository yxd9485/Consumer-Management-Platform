package com.dbt.framework.cache.bean;

/**
 * @author 作者姓名
 * @version 版本号
 * @createTime 2015年5月10日 下午1:02:23
 * @description 类说明
 */

public class CacheKeys {

	/**
	 * 用户中心，缓存Keys
	 * @author
	 * @createTime
	 * @description
	 */
	public static interface UserCenter {
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
		/** 固定Key|海军：超值返利活动记录数**/
		public static final String KEY_NAVY_PREMIUMERBATE_ACTIVITY_COUNT = "NavyPremiumRebateActivityCount";
		/** 渠道 */
		public static final String KEY_CHANNEL = "ChannelM";
		/** 渠道与用户关系 */
		public static final String KEY_USER_CHANNEL = "UserChannelInfo";
		/** 关注 */
		public static final String KEY_SUBSCRIBE = "VpsWctSubscribeInfo";
	}
	
	/**
	 * 用户消息，缓存Keys
	 * @author
	 * @createTime
	 * @description
	 */
	public static interface UserMessage {
		/** 未读消息 */
		public static final String KEY_UNREAD_MSG = "VpsPushmessageUnreadInfo";
		/** 我的消息记录 **/
		public static final String KEY_RECORDINFO = "VpsPushMessageRecordinfo";
	}
}
