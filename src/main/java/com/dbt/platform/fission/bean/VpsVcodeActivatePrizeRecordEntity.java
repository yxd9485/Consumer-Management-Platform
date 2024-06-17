package com.dbt.platform.fission.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangshuda
 * @since 2022-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VpsVcodeActivatePrizeRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String recordKey;

    /**
     * 中奖用户id
     */
    private String openid;

    /**
     * 中奖人昵称
     */
    private String nickName;

    /**
     * 中奖二维码
     */
    private String winQrcodeContent;

    /**
     * 中奖产品
     */
    private String winSkuName;

    /**
     * 中奖sku key
     */
    private String winSkuKey;

    private String ruleName;

    /**
     * 中奖状态
     */
    private String status;

    /**
     * 中出金额
     */
    private String winningAmount;
    /**
     * 激活状态
     * 0 未激活 1 已激活
     */
    private String moneyFlag;

    /**
     * 中出积分
     */
    private String winningPoint;

    /**
     * 分享人userKey
     */
    private String shareUserKey;
    /**
     * 分享人userKey
     */
    private String userKey;

    /**
     * 分享人昵称
     */
    private String shareUserName;

    /**
     * 中奖时间
     */
    private Date winDate;

    /**
     * 结束时间
     */
    private Date winEndTime;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;
    private String activityKey;
    /**
     * 活动key
     */
    private String vcodeActivityKey;
    /**
     * 奖励金额
     */
    private String rewardAmount;
    private Date rewardTime;
    /**
     * 奖励积分
     */
    private String rewardPoint;
    private String tablesIndex;
    private List<String> packTables;

    /**
     * 获得方式 0主动获得(主动点击分享卡片获取) 1被动获得(被分享人领取奖励给予)
     */
    private String obtainFlag;
    /**
     * 被分享人领取记录主键(获取方式为被动时写入)
     */
    private String activateRecord;
    /**
     * 实际激活用户主键(获取方式为被动时,具体扫码激活用户)
     */
    private String activateUserKey;

    public static final String USER_KEY = "user_key";

    public static final String USER_ID = "user_id";

    public static final String NICK_NAME = "nick_name";

    public static final String WIN_QRCODE_CONTENT = "win_qrcode_content";

    public static final String WIN_SKU_NAME = "win_sku_name";

    public static final String WIN_SKU_KEY = "win_sku_key";

    public static final String STATUS = "status";

    public static final String WINNING_AMOUNT = "winning_amount";

    public static final String WINNING_POINT = "winning_point";

    public static final String SHARE_USER_KEY = "share_user_key";

    public static final String SHARE_USER_NAME = "share_user_name";

    public static final String WIN_DATE = "win_date";

    public static final String WIN_END_TIME = "win_end_time";

    public static final String CREATE_TIME = "create_time";

    public static final String CREATE_USER = "create_user";

    public static final String UPDATE_TIME = "update_time";

    public static final String UPDATE_USER = "update_user";

}
