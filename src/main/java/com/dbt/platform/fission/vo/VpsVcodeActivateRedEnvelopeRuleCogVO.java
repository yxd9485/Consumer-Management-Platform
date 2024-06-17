package com.dbt.platform.fission.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
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
public class VpsVcodeActivateRedEnvelopeRuleCogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 激活红包规则主键
     */
    private String activityKey;
    /**
     * 激活红包规则编号
     */
    private String ruleNo;

    /**
     * 用户主键
     */
    private String userKey;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

    /**
     * 激活红包可领取个数
     */
    private Integer receiveNumber;

    /**
     * 激活红包领取时间限制 单位：天
     */
    private Integer receiveTimeLimit;

    /**
     * 激活红包任务完成时间限制 单位：天
     */
    private Integer taskCompletionTimeLimit;

    /**
     * 用户可领取激活红包个数限制
     */
    private Integer userReceiveNumberLimit;
    /**
     * 分享活动
     */
    private String shareVcodeActivityKey;
    private String[] shareVcodeActivityKeyArray;
    private String vcodeActivityName;

    /**
     * 激活SKU集合
     */
    private String activationSkuKeyList;
    private String[] activationSkuKeyArray;

    /**
     * 启用状态 0、停用 1、启用
     */
    private String stateFlag;

    /**
     * 限制类型0、当前规则 1、 每天
     */
    private String limitType;

    /**
     * 限制消费激活红包金额 单位 元
     */
    private String moneyLimit;
    private String moneyConfigFlag;

    /**
     * 限制消费瓶数 单位：瓶
     */
    private Integer numberLimit;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;
    /**
     * 有效规则 1 无效规则 2
     */
    private String tabsFlag;
    /**
     * 公众号链接
     */
    private String officialAccountUrl;
    /**
     * 公众号开关
     */
    private String officialAccountFlag;
    /**
     * 用户奖励开关
     */
    private String userRewardFlag;

    /**
     * 分享按钮指定区域
     */
    private String shareArea;
    /**
     * 激活红包分享按钮开关
     */
    private String shareFlag;
    /**
     * 分享奖励累心 1、现金 0、积分
     */
    private String rewardType;
    /**
     * 奖励金额范围 1,10
     */
    private String moneyRegion;
    /**
     * 奖励积分范围 1,10
     */
    private String pointRegion;

    /**
     * 限制分享奖励红包总额 单位 元
     */
    private String shareMoneyLimit;
    /**
     * 限制分享奖励积分总额 单位 元
     */
    private Integer sharePointLimit;
    /**
     * 分享奖励门槛
     */
    private Integer rewardReceiveLimit;
    /**
     *分享者获取激励红包个数
     */
    private Integer sharerGetRewardLimit;

    /**
     * 0、海报分享（给分享者红包） 1、二维码分享 2 全部同时分享  3、海报分享(给分享者带激活红包)
     */
    private Integer shareType;

    /**
     * 新用户类型 0当年未在本省区扫码 1累计未在本省区扫码 2指定天内未在本省区扫码
     */
    private String newUserType;

    /**
     * 分享者激励类型 0分享给新用户配置 1分享给老用户配置
     */
    private String shareAwardType;

    /**
     * 被分享者为新用户时被分享者获得待激活金额最小值
     */
    private Double acceptShareNewUserMinMoney;

    /**
     * 被分享者为新用户时被分享者获得待激活金额最大值
     */
    private Double acceptShareNewUserMaxMoney;

    /**
     * 每个分享者单日领取激励总次数上限
     */
    private Integer sharerGetRewardDayLimit;

    /**
     * 同一分享者领取次数限制
     */
    private Integer sharerUserReceiveNumLimit;

    /**
     * 激活红包任务完成截止时间
     */
    private String taskCompletionLimitDay;

    /**
     * 用户每天可领取激活红包个数限制
     */
    private Integer userReceiveNumberDayLimit;

    /**
     * 活动规则描述
     */
    private String roleDescribe;
    /**
     * 新用户判定条件(指定多少天未参与扫码)
     */
    private Integer newUserAsk;
    /**
     * 风险用户限制金额最小金额
     */
    private Double riskUserLimitMinMoney;
    /**
     * 风险用户限制金额最大金额
     */
    private Double riskUserLimitMaxMoney;



    private String[] newUserPrizeMinMoney;
    private String[] newUserPrizeMaxMoney;
    private String[] newUserPrizePercent;
    private String[] oldUserPrizeMinMoney;
    private String[] oldUserPrizeMaxMoney;
    private String[] oldUserPrizePercent;
    private String[] newUserPrizeNo;
    private String[] oldUserPrizeNo;


    public VpsVcodeActivateRedEnvelopeRuleCogVO(){}
    public VpsVcodeActivateRedEnvelopeRuleCogVO(String queryParam) {
      String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
      this.ruleNo = paramAry.length > 0 ? paramAry[0] : "";
      this.ruleName = paramAry.length > 1 ? paramAry[1] : "";
      this.stateFlag = paramAry.length > 2 ? paramAry[2] : "";
    }

}
