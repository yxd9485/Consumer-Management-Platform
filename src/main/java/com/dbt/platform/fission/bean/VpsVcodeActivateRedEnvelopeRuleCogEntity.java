package com.dbt.platform.fission.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 激活红包规则（裂变红包）
 * </p>
 *
 * @author wangshuda
 * @since 2022-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("vps_vcode_activate_red_envelope_rule_cog")
public class VpsVcodeActivateRedEnvelopeRuleCogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 激活红包规则主键
     */
      @TableId("ACTIVITY_KEY")
    private String activityKey;
    /**
     * 激活红包规则编号
     */
    @TableField("RULE_NO")
    private String ruleNo;

    /**
     * 用户主键
     */
    @TableField("USER_KEY")
    private String userKey;

    /**
     * 规则名称
     */
    @TableField("RULE_NAME")
    private String ruleName;

    /**
     * 开始时间
     */
    @TableField("START_DATE")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date startDate;

    /**
     * 结束时间
     */
    @TableField("END_DATE")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date endDate;
    /**
     * 激活红包分享按钮开关
     */
    @TableField("SHARE_FLAG")
    private String shareFlag;
    /**
     * 分享奖励累心 1、现金 0、积分
     */
    @TableField("REWARD_TYPE")
    private String rewardType;
    /**
     * 奖励金额范围
     */
    @TableField("MONEY_REGION")
    private String moneyRegion;
    /**
     * 奖励积分范围
     */
    @TableField("POINT_REGION")
    private String pointRegion;
    /**
     * 用户奖励开关
     */
    @TableField("USER_REWARD_FLAG")
    private String userRewardFlag;
    /**
     * 公众号链接
     */
    @TableField("OFFICIAL_ACCOUNT_URL")
    private String officialAccountUrl;
    /**
     * 公众号开关
     */
    @TableField("OFFICIAL_ACCOUNT_FLAG")
    private String officialAccountFlag;

    /**
     * 分享按钮指定区域
     */
    @TableField("SHARE_AREA")
    private String shareArea;



    /**
     * 激活红包可领取个数
     */
    @TableField("RECEIVE_NUMBER")
    private Integer receiveNumber;

    /**
     * 激活红包领取时间限制 单位：天
     */
    @TableField("RECEIVE_TIME_LIMIT")
    private Integer receiveTimeLimit;

    /**
     * 激活红包任务完成时间限制 单位：天
     */
    @TableField("TASK_COMPLETION_TIME_LIMIT")
    private Integer taskCompletionTimeLimit;
    /**
     * 用户可领取激活红包个数限制
     */
    @TableField("USER_RECEIVE_NUMBER_LIMIT")
    private Integer userReceiveNumberLimit;


    /**
     * 分享活动
     */
    @TableField("SHARE_VCODE_ACTIVITY_KEY")
    private String shareVcodeActivityKey;
    /**
     * 激活SKU集合
     */
    @TableField("ACTIVATION_SKU_KEY_LIST")
    private String activationSkuKeyList;

    /**
     * 启用状态 0、停用 1、启用
     */
    @TableField("STATE_FLAG")
    private String stateFlag;

    /**
     * 限制类型0、当前规则 1、 每天
     */
    @TableField("LIMIT_TYPE")
    private String limitType;

    /**
     * 限制消费激活红包金额 单位 元
     */
    @TableField("MONEY_LIMIT")
    private String moneyLimit;
    /**
     * 限制分享奖励红包总额 单位 元
     */
    @TableField("SHARE_MONEY_LIMIT")
    private String shareMoneyLimit;
    /**
     * 限制分享奖励积分总额 单位 元
     */
    @TableField("SHARE_POINT_LIMIT")
    private Integer sharePointLimit;
    /**
     * 限制消费瓶数 单位：瓶
     */
    @TableField("NUMBER_LIMIT")
    private Integer numberLimit;

    /**
     * 是否已有金额配置：0：否；1：是
     */
    @TableField("MONEY_CONFIG_FLAG")
    private String moneyConfigFlag;


    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("CREATE_USER")
    private String createUser;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("UPDATE_USER")
    private String updateUser;

    @TableField("DELETE_FLAG")
    private String deleteFlag;
    /**
     * 分享奖励门槛
     */
    private Integer rewardReceiveLimit;
    /**
     *分享者获取激励红包个数
     */
    @TableField("sharer_get_reward_limit")
    private Integer sharerGetRewardLimit;
    /**
     * 0、海报分享（给分享者红包） 1、二维码分享 2 全部同时分享  3、海报分享(给分享者带激活红包)
     */
    private Integer shareType;


    /**
     * 活动规则描述
     */
    @TableField("ROLE_DESCRIBE")
    private String roleDescribe;

    /**
     * 新用户类型 0当年未在本省区扫码 1累计未在本省区扫码 2指定天内未在本省区扫码
     */
    @TableField("NEW_USER_TYPE")
    private String newUserType;

    /**
     * 分享者激励类型 0分享给新用户配置 1分享给老用户配置
     */
    @TableField("SHARE_AWARD_TYPE")
    private String shareAwardType;

    /**
     * 被分享者为新用户时被分享者获得待激活金额最小值
     */
    @TableField("ACCEPT_SHARE_NEW_USER_MIN_MONEY")
    private Double acceptShareNewUserMinMoney;

    /**
     * 被分享者为新用户时被分享者获得待激活金额最大值
     */
    @TableField("ACCEPT_SHARE_NEW_USER_MAX_MONEY")
    private Double acceptShareNewUserMaxMoney;

    /**
     * 每个分享者单日领取激励总次数上限
     */
    @TableField("SHARER_GET_REWARD_DAY_LIMIT")
    private Integer sharerGetRewardDayLimit;

    /**
     * 同一分享者领取次数限制
     */
    @TableField("SHARER_USER_RECEIVE_NUM_LIMIT")
    private Integer sharerUserReceiveNumLimit;

    /**
     * 激活红包任务完成截止日期
     */
    @TableField("TASK_COMPLETION_LIMIT_DAY")
    private String taskCompletionLimitDay;

    /**
     * 被分享者每天可领取激活红包个数限制
     */
    @TableField("USER_RECEIVE_NUMBER_DAY_LIMIT")
    private Integer userReceiveNumberDayLimit;

    /**
     * 新用户判定条件(指定多少天未参与扫码)
     */
    @TableField("NEW_USER_ASK")
    private Integer newUserAsk;
    /**
     * 风险用户限制金额最小值
     */
    @TableField("RISK_USER_LIMIT_MIN_MONEY")
    private Double riskUserLimitMinMoney;
    /**
     * 风险用户限制金额最大值
     */
    @TableField("RISK_USER_LIMIT_MAX_MONEY")
    private Double riskUserLimitMaxMoney;


    public static final String ACTIVITY_KEY = "ACTIVITY_KEY";

    public static final String USER_KEY = "USER_KEY";

    public static final String RULE_NAME = "RULE_NAME";

    public static final String START_DATE = "START_DATE";

    public static final String END_DATE = "END_DATE";

    public static final String RECEIVE_TIME_LIMIT = "RECEIVE_TIME_LIMIT";

    public static final String TASK_COMPLETION_TIME_LIMIT = "TASK_COMPLETION_TIME_LIMIT";


    public static final String ACTIVATION_SKU_KEY_LIST = "ACTIVATION_SKU_KEY_LIST";

    public static final String STATE_FLAG = "STATE_FLAG";

    public static final String LIMIT_TYPE = "LIMIT_TYPE";

    public static final String MONEY_LIMIT = "MONEY_LIMIT";

    public static final String NUMBER_LIMIT = "NUMBER_LIMIT";

    public static final String CREATE_TIME = "CREATE_TIME";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String UPDATE_TIME = "UPDATE_TIME";

    public static final String UPDATE_USER = "UPDATE_USER";
    public static final String DELETE_FLAG = "DELETE_FLAG";

}
